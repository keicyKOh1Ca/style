# Weblogic14c インストール＆構築手順（その２）

<pre>
基本は以下、URLのマニュアル参考としているが一部異なる部分もあるので、それを記載
[2 Oracle WebLogic ServerとCoherencのソフトウェアのインストール]([https://docs.oracle.com/cd/F32751_01/weblogic-server/14.1.1.0/wlsig/installing-weblogic-server-developers.html](https://docs.oracle.com/cd/F32751_01/weblogic-server/14.1.1.0/wlsig/installing-oracle-weblogic-server-and-coherence-software.html#GUID-E4241C14-42D3-4053-8F83-C748E059607A))
</pre>

---
### 1. teratermにて、EC2インスタンスに接続
- ユーザはec2ユーザでもOKだし、自作のアカウントでも結構
---
### 2. ドメイン作成
|#|command|explanation|
|:--:|:--|:--|
|1|___su - oracle___|oracleに切り替え|
|2|___touch /u01/app/weblogic/WLS-install/domainTmp.py___|テンプレートファイル作成|
|3|___vi /u01/app/weblogic/WLS-install/domainTmp.py___||
|4|___[i]___|viインサートモード|
|5|___テンプレートファイル作成___|※1|
|6|___[:wq]___|vi終了|
|7|___export JAVA_HOME="/usr/java/jdk-11.0.14"___|環境変数設定（echo $JAVA_HOMEで確認）|
|8|___export PATH="/usr/java/jdk-11.0.14/bin:$PATH"___|環境変数設定（echo $PATHで確認）|
|9|___export ORACLE_HOME="/u01/app/weblogic/Oracle_Home"___|環境変数設定（echo $ORACLE_HOMEで確認）|
|10|___. /u01/app/weblogic/Oracle_Home/wlserver/server/bin/setWLSEnv.sh___||
|11|___java weblogic.WLST /u01/app/weblogic/WLS-install/domainTmp.py___|ドメイン作成|
|12|___chmod -R 755 /u01/app/weblogic/Oracle_Home/user_projects/domains/DomainD01___|権限付与|
|13|___su - root___|rootにスイッチ|
|14|___chown -R samp_user:samp_user /u01/app/weblogic/Oracle_Home/user_projects/domains/DomainD01___|※2|

<pre>
※1：テンプレートファイル例
# テンプレート指定
readTemplate("/u01/app/weblogic/Oracle_Home/wlserver/common/templates/wls/wls.jar")

# 管理サーバ構成
# サーバ名やポート指定
cd('Servers/AdminServer')
set('Name','Admin')
set('ListenAddress','')
set('ListenPort', 7011)

# weblogic管理コンソールのユーザ（ユーザはデフォルト[weblogic]）・パスワード指定
cd('/')
cd('Security/base_domain/User/weblogic')
# Please set password here before using this script, e.g. cmo.setPassword('value')
cmo.setPassword('任意のパスワード')

# ドメイン書き込みとテンプレートドメインクローズ
setOption('OverwriteDomain', 'true')
setOption('DomainName','DomainD01')
setOption('ServerStartMode', 'prod')	# ここをdevにした場合、開発モード
writeDomain('/u01/app/weblogic/Oracle_Home/user_projects/domains/DomainD01')
closeTemplate()

# 終了
exit()

※2
起動したいユーザを、例えば別のユーザ（アプリユーザとか）にしたい場合の設定
</pre>

---
### 3. 管理サーバ起動＋停止＆ユーザ登録

・startWeblogic.sh修正
|#|command|explanation|
|:--:|:--|:--|
|1|___su - samp_user___|起動OSユーザを指定していれば※指定していなければoracleユーザでも可|
|2|___cd /u01/app/weblogic/Oracle_Home/user_projects/domains/DomainD01/bin___||
|3|___vi startWebLogic.sh___||
|4|___[i]___|viインサートモード|
|5|___※1___|以下修正内容|
|6|___[:wq]___|vi終了|

<pre>
※1：追加内容
JAVA_OPTIONS=" ${SAVE_JAVA_OPTIONS}"
の部分をコメントアウトし、
JAVA_OPTIONS="-Djava.security.egd=file:///dev/urandom ${SAVE_JAVA_OPTIONS}"
を追記

https://docs.oracle.com/cd/F25597_01/document/products/E13153_01/wlcp/wlss40/configwlss/jvmrand.html
</pre>

・管理サーバ起動＋停止
|#|command|explanation|
|:--:|:--|:--|
|1|___./startWebLogic.sh &___|※1|
|2|___http://{ipアドレス}:{レスポンスファイルで指定したポート番号}/console___|コンソール起動|
|3|___./stopWebLogic.sh___||

<pre>
※1：起動時に毎回ユーザとパスワード（ユーザはデフォルト「weblogic」、パスワードはテンプレートファイルに指定したもの）を
　 　聞かれるので、boot.propertiesファイルを作成しておくこと。（本番運用時とかは必須）
　 　また、仮想サーバ毎に設定可能（管理対象が別サーバであれば納得）

　 　設置場所：${WLS_HOME}/user_projects/domains/DomainD01/servers/Admin/security
　 　　　　　　→securityディレクトリがなければ作成する。
　 　　　　　　　その場合、boot.propertiesファイルもないので、作成すること。
　 　１．boot.propertiesファイルをviで編集し、以下の内容を記載する。
　 　=>username=起動ユーザ名
　　　 password=起動パスワード
　　　 ※デフォルトはユーザ「weblogic」だが、weblogicコンソール上で作成したユーザも可能（ロール（グループ）はきちんと設定しておく必要あり）
　 　２．一度、boot.propertiesを作成しておくと、次回startWeblogic.shを起動したタイミングで
　 　　　そのファイルを参照に起動する。更にboot.properties内の情報も暗号化される。
</pre>

・ユーザ作成※デフォルトユーザが嫌な場合
> [weblogicユーザ作成](https://docs.oracle.com/cd/E57761_01/gg-monitor/GMINS/user_man.htm)
<br>
> [デフォルトユーザ不要の場合](https://docs.oracle.com/cd/F32751_01/weblogic-server/14.1.1.0/wlach/taskhelp/security/DeleteUsers.html)

---
### 4. 管理対象サーバ起動＋停止＆ユーザ登録

[管理対象サーバ作成](https://github.com/keicyKOh1Ca/style/tree/master/oracle/Weblogic管理対象サーバ作成.png)

|#|command|explanation|
|:--:|:--|:--|
|1|___su - samp_user___|起動OSユーザを指定していれば※指定していなければoracleユーザでも可|
|2|___cd /u01/app/weblogic/Oracle_Home/user_projects/domains/DomainD01/bin___||
|3|___./startManagedWebLogic.sh  {管理対象サーバ作成で設定したサーバ名} t3://{ipアドレス}:{管理サーバのポート}___|※1|
|4|___./stopManagedWebLogic.sh  {管理対象サーバ作成で設定したサーバ名} t3://{ipアドレス}:{管理サーバのポート}___|管理対象サーバ停止|

<pre>
※1：管理対象サーバの起動時においても、ユーザとパスワードが求められるので
　　 管理対象サーバ用のboot.prpertiesを作成しておくと良い。
　　 その場合、${WLS_HOME}/user_projects/domains/DomainD01/servers配下に
　　 管理対象サーバ用のディレクトリがあるのでその配下に作成する。
</pre>
---
### 5. JNDI設定

[JNDI作成](https://github.com/keicyKOh1Ca/style/tree/master/oracle/JNDI作成.md)

---
### 6. まとめ

<pre>
・weblogic自体のインストール＆サーバ設定は何度もやってて、いつか手順をまとめておこうと思っていたが、
　今回管理対象サーバも作成することになったので、いい機会なので作成した。

・仮想サーバの立て方が今回、どのようにするか分かったので時間があるときに、サーバ間で
　管理サーバと管理対象サーバの設定を実装してみたい。
</pre>
