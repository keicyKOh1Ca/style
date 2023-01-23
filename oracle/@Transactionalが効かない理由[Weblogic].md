# @Transactionalアノテーションが効かない？？（Weblogic19c）

---
1. APからDBへアクセスする際のトランザクション処理について、@Transactionalを利用した。
2. １トランザクション内で複数の処理がある場合、例外が発生してもロールバックされない。
   ※直前までコミットされてしまう。
3. 以前対応したシステムでは例外が発生したタイミングでロールバックされ、全て無効となった。
4. 問題なかったAPサーバはPayaraサーバを利用していた。
5. 問題あるものはWeblogicサーバを利用している。
6. 違いはなにか？
---
~~~java
・サンプルソース
package beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import service.Inserter;

@Named
@RequestScoped
public class Bb
{
	@NotNull
	@Min(1)
	@Max(99999)
	private Integer number;
	@NotBlank
	private String name;
	@NotBlank
	@Email
	private String mail;
	
	@Inject
	Inserter inserter;
	
	public String execute1()
	{
		inserter.createAll();
		//
		return null;
	}

	public String execute2()
	{
		inserter.createAndSelect();
		//
		return null;
	}

	public void clear()
	{
		number = null;
		name = mail = null;
	}
	
	public Integer getNumber()
	{
		return number;
	}
	
	public void setNumber(Integer number)
	{
		this.number = number;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getMail()
	{
		return mail;
	}
	
	public void setMail(String mail)
	{
		this.mail = mail;
	}
}

/* entityManagerを利用して対応 */
package service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import beans.Bb;
import db.CommonDb;
import db.EmpDb;
import db.MeiboDb;
import entity.Emp;
import entity.Meibo;

@Stateless
public class Inserter
{
	@Inject
	MeiboDb meiboDb;
	@Inject
	EmpDb empDb;
	@Inject
	Bb bb;
	@Inject
	CommonDb commonDb;

	// rollbackされる
	@Transactional(rollbackOn = {Exception.class})
	public void createAll()
	{
		try {
			commonDb.clear();

			// meibo insert
			Meibo meibo = new Meibo();
			meibo.setNumber_1(bb.getNumber());
			meibo.setName_1(bb.getName());
			meibo.setMail(bb.getMail());
			commonDb.persist(meibo);

			// EMP insert
			Emp emp = new Emp(bb.getNumber(), "1", bb.getName() + "氏");
			commonDb.persist(emp);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// rollbackされない
	@Transactional(rollbackOn = {Exception.class})
	public void createAndSelect()
	{
		try {
			commonDb.clear();

			// meibo insert
			Meibo meibo = new Meibo();
			meibo.setNumber_1(bb.getNumber());
			meibo.setName_1(bb.getName());
			meibo.setMail(bb.getMail());
			commonDb.persist(meibo);

			// createNativeQuery
			commonDb.select();
			// EMP insert
			Emp emp = new Emp(bb.getNumber(), "1", bb.getName() + "氏");
			commonDb.persist(emp);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
~~~

~~~xhtml
・サンプル画面
<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://xmlns.jcp.org/jsf/html">
<h:head>
	<title>サンプル</title>
</h:head>
<h:body>
	<h1>データベースへの新規登録</h1>
	<h:form>
		<h:panelGrid columns="3">
				番号<h:inputText id="number" value="#{bb.number}" />
			<h:message for="number" styleClass="error" />
				氏名<h:inputText id="name" value="#{bb.name}" />
			<h:message for="name" styleClass="error" />
				メール<h:inputText id="mail" value="#{bb.mail}" />
			<h:message for="mail" styleClass="error" />
		</h:panelGrid>
		<h:commandButton value="rollbackされる" action="#{bb.execute1()}" />
		<h:commandButton value="rollbackされない" action="#{bb.execute2()}" />
		<br />日付<h:outputText id="names" value="#{bb.name}" />
	</h:form>
</h:body>
</html>
~~~
---
### 原因

・Oracleドライバで非XAを利用している場合、WeblogicのJTAの中の「グローバル・トランザクションのサポート」を有効化にしなければ
　トランザクションが動作しないよう。
 　その為、非XAドライバを選択をする場合は、「グローバル・トランザクションのサポート」を有効化
  　もしくはXAドライバを素直に利用することをお勧めする。
   
[Oracle WebLogic Server JDBCデータ・ソースの管理](https://docs.oracle.com/cd/F32751_01/weblogic-server/14.1.1.0/wlsig/installing-oracle-weblogic-server-and-coherence-software.html#GUID-E4241C14-42D3-4053-8F83-C748E059607A)

