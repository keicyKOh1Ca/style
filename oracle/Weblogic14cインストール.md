# Weblogic14c インストール＆構築手順

<pre>
基本は以下、URLのマニュアル参考としているが一部異なる部分もあるので、それを記載
[2 Oracle WebLogic ServerとCoherencのソフトウェアのインストール]([https://docs.oracle.com/cd/F32751_01/weblogic-server/14.1.1.0/wlsig/installing-weblogic-server-developers.html](https://docs.oracle.com/cd/F32751_01/weblogic-server/14.1.1.0/wlsig/installing-oracle-weblogic-server-and-coherence-software.html#GUID-E4241C14-42D3-4053-8F83-C748E059607A))
</pre>

---
### 0. インストール環境＆前提条件
- AWS EC2
- Linux（redhat8）
- 2cpu memory:8G
- Weblogic＋jdk(ver 11.0.14)のインストーラは事前にOracleのサイトからDLして、EC2へアップ済
  ※/home/ec2-user配下
---
### 1. teratermにて、EC2インスタンスに接続
- ユーザはec2ユーザでもOKだし、自作のアカウントでも結構
---
### 2. jdkインストール
|#|command|explanation|
|:--:|:--|:--|
|1|___su - root___|rootに切り替え|
|2|___mkdir -p  /usr/java___|java格納Dir作成|
|3|___cd /home/ec2-user/JDK_11.0.14/___||
|4|___rpm -ivh --prefix=/usr/java  jdk-11.0.14_linux-x64_bin.rpm___||
|5|___cd /usr/java/jdk-11.0.14/conf/security___|※1|
|6|___vi java.security___|※1|
|7|___[i]___|viインサートモード※1|
|8|___networkaddress.cache.ttl=0___|キャッシュ無効※1|
|9|___networkaddress.cache.negative.ttl=0___|キャッシュ無効※1|
|10|___[:wq]___|vi終了※1|

<pre>
※1：DNSキャッシュの無効化設定
</pre>
> Java 仮想マシン (JVM) は DNS 名参照をキャッシュし、JVM がホスト名をIPアドレスに変換するとき
> 指定期間 IP アドレスをキャッシュする為

---
### 3. Weblogicインストール

・Oracleユーザ作成
|#|command|explanation|
|:--:|:--|:--|
|1|___su - root___|rootに切り替えてなければ|
|2|___useradd -m -d /home/oracle oracle___|oracleユーザ作成|
|3|___passwd oracle___|oracleパスワード設定|
|4|___groupadd oinstall___|oracleグループ作成|
|5|___usermod -g oinstall oracle___|oracleグループ紐付け|

・weblogicディレクトリ作成
|#|command|explanation|
|:--:|:--|:--|
|1|___su - root___|rootに切り替えてなければ|
|2|___mkdir -p /u01/app/weblogic/Oracle_Home___||
|3|___mkdir -p /u01/app/weblogic/WLS-install___||
|4|___mkdir -p /u01/app/weblogic/oraInventory___||
|5|___chmod -R 755 /u01/app/weblogic/___|権限変更|
|6|___chown -R oracle:oinstall /u01/app/weblogic/___|オーナー変更|

・zipファイル解凍
|#|command|explanation|
|:--:|:--|:--|
|1|___su - root___|rootに切り替えてなければ|
|2|___cp /home/ec2-user/WeblogicInstaller.zip /home/oracle/WeblogicInstaller.zip___|WeblogicInstaller.zipは任意の名前|
|3|___chmod -R 755 /home/oracle/WeblogicInstaller.zip___|権限変更|
|4|___chown -R oracle:oinstall /home/oracle/WeblogicInstaller.zip___|オーナー変更|
|5|___su - oracle___|oracleにスイッチ|
|6|___cd /home/oracle/___||
|7|___unzip WeblogicInstaller.zip -d /u01/app/weblogic/WLS-install___||

・環境変数設定
|#|command|explanation|
|:--:|:--|:--|
|1|___su - oracle___|oracleに切り替えてなければ|
|2|___export JAVA_HOME="/usr/java/jdk-11.0.14/bin"___|echo $JAVA_HOMEで確認|
|3|___export PATH="/usr/java/jdk-11.0.14/bin:$PATH"___|echo $PATHで確認|

・レスポンスファイルの作成＆ロケーションファイルの作成
|#|command|explanation|
|:--:|:--|:--|
|1|___su - oracle___|oracleに切り替えてなければ|
|2|___cd /u01/app/weblogic/WLS-install___||
|3|___touch resposefile.rsp___||
|4|___touch oraInst.loc___||
|5|___vi resposefile.rsp___|※1|
|6|___vi resposefile.rsp___|※2|


<pre>
※1：レスポンスファイル例
[ENGINE]

#DO NOT CHANGE THIS.
Response File Version=1.0.0.0.0

[GENERIC]

#The oracle home location. This can be an existing Oracle Home or a new Oracle Home
ORACLE_HOME=/u01/app/weblogic/Oracle_Home


#Set this variable value to the Installation Type selected. e.g. WebLogic Server, Coherence, Complete with Examples.
INSTALL_TYPE=WebLogic Server

#Provide the My Oracle Support Username. If you wish to ignore Oracle Configuration Manager configuration provide empty string for user name.
MYORACLESUPPORT_USERNAME=

#Provide the My Oracle Support Password
MYORACLESUPPORT_PASSWORD=

#Set this to true if you wish to decline the security updates. Setting this to true and providing empty string for My Oracle Support username will ignore the Oracle Configuration Manager configuration
DECLINE_SECURITY_UPDATES=true

#Set this to true if My Oracle Support Password is specified
SECURITY_UPDATES_VIA_MYORACLESUPPORT=false

#Provide the Proxy Host
PROXY_HOST=

#Provide the Proxy Port
PROXY_PORT=

#Provide the Proxy Username
PROXY_USER=

#Provide the Proxy Password
PROXY_PWD=

#Type String (URL format) Indicates the OCM Repeater URL which should be of the format [scheme[Http/Https]]://[repeater host]:[repeater port]
COLLECTOR_SUPPORTHUB_URL=

※2：ロケーションファイル例
inventory_loc =  /u01/app/weblogic/oraInventory
inst_group = oinstall
</pre>

・weblogicインストール
|#|command|explanation|
|:--:|:--|:--|
|1|___su - oracle___|oracleに切り替えてなければ|
|2|___cd /u01/app/weblogic/WLS-install___||
|3|___java -jar fmw_14.1.1.0.0_wls.jar -silent -responseFile  /u01/app/weblogic/WLS-install/resposefile.rsp -invPtrLoc  /u01/app/weblogic/WLS-install/oraInst.loc___|インストール|
|4|___chmod -R 755 /u01/app/weblogic/Oracle_Home/___|※インストール後、権限が変わっている為|
---
### 4. 管理サーバ＆管理対象サーバ構築
長くなったので別ファイルで。
[Weblogicサーバ構築へ](https://github.com/keicyKOh1Ca/style/tree/master/oracle)/Weblogicサーバ構築.md "構築へ")
