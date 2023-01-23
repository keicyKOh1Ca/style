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
