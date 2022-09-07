# Oracle DB 19c インストール＆DB構築手順

<pre>
基本は以下、URLのマニュアル参考としているが一部異なる部分もあるので、それを記載
[データベース・インストレーション・ガイドfor Linux](https://docs.oracle.com/cd/F19136_01/ladbi/creating-an-oracle-software-owner-user.html#GUID-C1E5CEA8-741A-4500-B03E-B4A6BC1E87BB)
</pre>

---
### 0. インストール環境＆前提条件
- AWS EC2
- Linux（redhat8）
- 4cpu memory:16G
- Oracleのインストーラは事前にOracleのサイトからDLして、EC2へアップ済（/home/ec2_user）
---
### 1. teratermにて、EC2インスタンスに接続
- ユーザはec2ユーザでもOKだし、自作のアカウントでも結構
---
### 2. ユーザの切り替え
|#|command|
|:--:|:--|
|1|___su - root___|
---
### 3. Oracleアカウント & グループ作成
・Oracleユーザ作成
|#|command|
|:--:|:--|
|1|___useradd --uid 54321 oracle___|
|2|___passwd oracle___|
<pre>
ユーザー oracle のパスワードを変更。
新しいパスワード:
新しいパスワードを再入力してください:
passwd: すべての認証トークンが正しく更新できました。
</pre>

・Oracleグループ作成
|#|command|explanation|
|:--:|:--|:--|
|1|___groupadd --gid 54421 oinstall___||
|2|___groupadd --gid 54322 dba___||
|3|___groupadd --gid 54323 oper___||
|4|___groupadd --gid 54324 backupdba___||
|5|___groupadd --gid 54325 dgdba___||
|6|___groupadd --gid 54326 kmdba___||
|7|___groupadd --gid 54330 racdba___||
|8|___usermod  -g oinstall -G dba,oper,backupdba,dgdba,kmdba,racdba oracle___|oracleユーザにグループ付与|

・Oracleユーザ環境変数設定
|#|command|explanation|
|:--:|:--|:--|
|1|___vi .bash_profile___||
|2|___[i]___|viインサートモード|
|3|___export PATH___||
|4|___export ORACLE_BASE=/u01/app/oracle___||
|5|___export ORACLE_HOME=/u01/app/oracle/product/19.0.0/DB_HOME___||
|6|___export ORACLE_SID=DB01___||
|7|___export NLS_LANG=Japanese_Japan.AL32UTF8___||
|8|___export PATH=$ORACLE_HOME/bin:$PATH___||
|9|___[:wq]___|vi終了|
---
### 4. Oracleディレクトリ作成
|#|command|explanation|
|:--:|:--|:--|
|1|___su - root___|rootにスイッチ|
|2|___mkdir -p /u01/app/oracle___||
|3|___mkdir -p /u01/app/oracle/product/19.0.0/DB_HOME___||
|4|___mkdir -p /u01/app/oraInventory___||
|5|___chown -R oracle:oinstall /u01/app/oracle___|oracleオーナーに変更|
|6|___chown -R oracle:oinstall /u01/app/oraInventory___|oracleオーナーに変更|
|7|___chmod -R 775 /u01/app___|rwx権限変更|
---
### 5. Oracleユーザリソース変更
|#|command|explanation|
|:--:|:--|:--|
|1|___su - root___|rootにスイッチ|
|2|___vi /etc/security/limits.conf___||
|3|___[i]___|viインサートモード|
|4|___oracle          soft    nproc            2047___||
|5|___oracle          hard    nproc           16384___||
|6|___oracle          soft    nofile           1024___||
|7|___oracle          hard    nofile          65536___||
|8|___oracle          soft    stack           10240___||
|9|___oracle          hard    stack           32768___||
|10|___[:wq]___|vi終了|
|11|___cat /etc/security/limits.conf___|設定確認|

---
### 6. Oracle Preインストール実施※1
・DL先は任意（今回は/tmp配下に適用）
|#|command|
|:--:|:--|
|1|curl -o oracle-database-preinstall-19c-1.0-2.el8.x86_64.rpm https://yum.oracle.com/repo/OracleLinux/OL8/appstream/x86_64/getPackage/oracle-database-preinstall-19c-1.0-2.el8.x86_64.rpm※2|
|2|yum install oracle-database-preinstall-19c-1.0-2.el8.x86_64.rpm※3|

<pre>
※1：このインストーラはOracleDBのインストールの前に、必要要件である、Linuxのモジュール等を自動的にインストールするもの
以下のように依存関係パッケージを自動的にインストールしてくれる。※基本は足りない場合、手動でリポジトリをセットし、インストールする。
依存関係が解決しました。
=====================================================================================================================
 パッケージ                            Arch          バージョン              リポジトリー                      サイズ
=====================================================================================================================
インストール:
 oracle-database-preinstall-19c        x86_64        1.0-2.el8               @commandline                       31 k
依存関係のインストール:
 gssproxy                              x86_64        0.8.0-20.el8            rhel-8-baseos-rhui-rpms           119 k
 keyutils                              x86_64        1.5.10-9.el8            rhel-8-baseos-rhui-rpms            66 k
 libICE                                x86_64        1.0.9-15.el8            rhel-8-appstream-rhui-rpms         74 k
 libSM                                 x86_64        1.2.3-1.el8             rhel-8-appstream-rhui-rpms         48 k
 libX11-xcb                            x86_64        1.6.8-5.el8             rhel-8-appstream-rhui-rpms         14 k
 libXcomposite                         x86_64        0.4.4-14.el8            rhel-8-appstream-rhui-rpms         29 k
 libXinerama                           x86_64        1.1.4-1.el8             rhel-8-appstream-rhui-rpms         16 k
 libXmu                                x86_64        1.1.3-1.el8             rhel-8-appstream-rhui-rpms         75 k
 libXrandr                             x86_64        1.5.2-1.el8             rhel-8-appstream-rhui-rpms         34 k
 libXt                                 x86_64        1.1.5-12.el8            rhel-8-appstream-rhui-rpms        185 k
 libXv                                 x86_64        1.0.11-7.el8            rhel-8-appstream-rhui-rpms         20 k
 libXxf86dga                           x86_64        1.1.5-1.el8             rhel-8-appstream-rhui-rpms         26 k
 libXxf86misc                          x86_64        1.0.4-1.el8             rhel-8-appstream-rhui-rpms         23 k
 libXxf86vm                            x86_64        1.1.4-9.el8             rhel-8-appstream-rhui-rpms         19 k
 libdmx                                x86_64        1.1.4-3.el8             rhel-8-appstream-rhui-rpms         22 k
 libverto-libevent                     x86_64        0.3.0-5.el8             rhel-8-baseos-rhui-rpms            16 k
 nfs-utils                             x86_64        1:2.3.3-51.el8          rhel-8-baseos-rhui-rpms           504 k
 quota                                 x86_64        1:4.04-14.el8           rhel-8-baseos-rhui-rpms           214 k
 quota-nls                             noarch        1:4.04-14.el8           rhel-8-baseos-rhui-rpms            95 k
 rpcbind                               x86_64        1.2.5-8.el8             rhel-8-baseos-rhui-rpms            70 k
 xorg-x11-utils                        x86_64        7.5-28.el8              rhel-8-appstream-rhui-rpms        135 k
 xorg-x11-xauth                        x86_64        1:1.0.9-12.el8          rhel-8-appstream-rhui-rpms         39 k

トランザクションの概要
=====================================================================================================================
インストール  23 パッケージ

※2：マニュアルのURLは「appstream」の部分がRedhat7のURLの「latest」となっているため、DLエラーとなっていた。記載されているURLが間違っている。
※3：dnfはLinux8から用意されたyumの後継コマンド

またインストール時に「compat-libcap1-1.10パッケージの欠落エラーが発生」するが、無視可能のよう。
[3 Oracle Database 19cのLinuxに影響する問題](https://docs.oracle.com/cd/F19136_01/rnrdm/linux-platform-issues.html#GUID-2F31EF36-D154-470A-A3B7-9631DF245BAD)
</pre>
---
### 7. perlのインストール
|#|command|
|:--:|:--|
|1|___yum install -y perl___|
<pre>
oracleDBのプログラム内にperlで実行するものがあるので、プリインストールされていれば基本必要ないかもだけど、念のため
</pre>
---
### 8. OracleDBのインストール
・スワップ領域がない場合、作成する。
<br>
|#|command|explanation|
|:--:|:--|:--|
|1|___swapon -s ( or cat /proc/swaps)___|スワップ領域が適用されているか|
|2|___dd if=/dev/zero of=/tmp/swapfile bs=1M count=4000___|ない場合、作成※1|
|3|___mkswap /tmp/swapfile___|スワップ領域として適用|
|4|___swapon /tmp/swapfile___|スワップ領域ON|
|5|___swapon -s ( or cat /proc/swaps)___|スワップ領域が適用されているか|
<pre>
※1：マウント先をスワップ領域としてもOKだが、今回は容量の関係上ファイルを作成
また、スワップ・サイズに対する要件もあるので、注意すること。（インストール時、警告が発生する。）

[Oracle Database Clientのサーバー構成チェックリスト](https://docs.oracle.com/cd/F19136_01/lacli/server-configuration-checklist-for-oracle-database-client.html#GUID-CE906345-5274-4E57-B6B9-0840A9D7226E)
【RAMに対してのスワップ領域の割当て】
256MB               ：RAMのサイズの3倍
256MBから512MBまで   ：RAMのサイズの2倍
512MBから2GBまで     ：RAMのサイズの1.5倍
2GBから16GBまで　    ：RAMのサイズと同じ
16GB超　            ：16GB
</pre>

・Oracleインストール
|#|command|explanation|
|:--:|:--|:--|
|1|___su - oracle___|oracleユーザへスイッチ|
|2|___cd /u01/app/oracle/product/19.0.0/DB_HOME___||
|3|___unzip -q /home/oracle/INST_ORA19c.zip___|zipファイル名は任意の名前としている（※ファイル名は異なる）|

・レスポンスファイル作成
|#|command|explanation|
|:--:|:--|:--|
|1|___mkdir /home/oracle/install/___||
|2|___ls -ltr  /home/oracle/___||
|3|___cp -p  ${ORACLE_HOME}/install/response/db_install.rsp /home/oracle/install/db_install.rsp___||
|4|___vi /home/oracle/install/db_install.rsp※1___|[db_install.rsp](https://github.com/keicyKOh1Ca/style/blob/master/oracle/db_install.rsp)|
|5|___export CV_ASSUME_DISTID=OL7___|[3.8 Oracle Linux 8およびRed Hat Enterprise Linux 8に関する既知の問題および不具合](https://docs.oracle.com/cd/F19136_01/rnrdm/linux-platform-issues.html#GUID-254C296D-F61D-49FC-827C-0F5CB8528C56)|
|6|___${ORACLE_HOME}/runInstaller -silent -responseFile /home/oracle/install/db_install.rsp___||

<pre>
<b>!!!インストール時に一部オプションでの警告が発生するが、正常にインストールはされる。</b>
</pre>
---
### 9. インストール後、スクリプト実行
|#|command|explanation|
|:--:|:--|:--|
|1|___su - root___|rootへスイッチ|
|2|___/u01/app/oraInventory/orainstRoot.sh___||
|3|___/u01/app/oracle/product/19.0.0/DB_HOME/root.sh___||
---
### 10. DB構築事前準備
・初期化パラメータファイル作成
|#|command|explanation|
|:--:|:--|:--|
|1|___${ORACLE_HOME}/dbs/init.ora ./initDB.ora___|任意の場所でもOK|
<pre>
※initDB.oraサンプル
db_name='CDB01'
memory_target=4G　　　#<=ここはデフォルト1Gになっているが、それだと少なすぎとエラーになるため、上げる。
processes = 150
audit_file_dest='$ORACLE_BASE/admin/CDB01/adump'   #<=DIR作成を先に実施しておく
audit_trail ='db'
db_block_size=8192
db_domain=''
db_recovery_file_dest='$ORACLE_BASE/fast_recovery_area'　　#<=DIR作成を先に実施しておく
db_recovery_file_dest_size=2G
diagnostic_dest='$ORACLE_BASE'
dispatchers='(PROTOCOL=TCP) (SERVICE=ORCLXDB)'
open_cursors=300
remote_login_passwordfile='EXCLUSIVE'
undo_tablespace='UNDOTBS1'
# You may want to ensure that control files are created on separate physical
# devices
#control_files = (ora_control1, ora_control2)
#↓制御ファイルのパスを指定しておき、更にDIRは作成しておくこと（今回は3つ定義している。）
control_files = ('任意の場所/control01.ctl', '任意の場所/control02.ctl', '任意の場所/control03.ctl')
compatible ='19.0.0'　　　　　　　　　#<=デフォルト(11.0.2）のままにしておくと、未対応エラーとなるため、該当の19.0.0に設定しておく。
enable_pluggable_database=true　　　#<=レスポンスファイルでマルチテナント構成を指定しているのに、このパラメータを指定しないと、CDB作成時にエラーとなるので設定
</pre>

・tnsnamas.oraの指定
<pre>
# CDB用
CDB01 =
    (DESCRIPTION =
        (ADDRESS = (PROTOCOL = TCP)(HOST = 【ホスト名かIPアドレス指定】)(PORT = 1521))
        (CONNECT_DATA = (SERVER = DEDICATED)(SERVICE_NAME = CDB01))
    )
# PDB用
PDB01 =
    (DESCRIPTION =
        (ADDRESS = (PROTOCOL = TCP)(HOST = 【ホスト名かIPアドレス指定】)(PORT = 1521))
        (CONNECT_DATA = (SERVER = DEDICATED)(SERVICE_NAME = PDB01))
    )
</pre>

・dbディレクトリ作成
|#|command|explanation|
|:--:|:--|:--|
|1|___mkdir /u01/app/oracle/product/oradata/CDB01/___||
|2|___mkdir /u01/app/oracle/product/oradata/CDB01/pdbseed___||
|3|___mkdir /u01/app/oracle/product/oradata/CDB01/pdbseed/datafile___||

・spfile作成
|#|command|explanation|
|:--:|:--|:--|
|1|___sqlplus / as sysdba___|OS認証接続|
|2|___CREATE SPFILE FROM PFILE = '/u01/app/oracle/product/19.0.0/DB_HOME/dbs/initDB.ora';___|spfile作成|
|3|___!ls -ltr ${ORACLE_HOME}/dbs___|spfile確認|
---
### 11. CDB作成

|#|command|explanation|
|:--:|:--|:--|
|1|___startup nomount___|インスタンス起動|
|2|___mount -t tmpfs shmfs -o size=10g /dev/shm___|-- 上記１でエラー（ORA-00845: MEMORY_TARGET not supported on this system）が発生した場合、以下コマンド実施|
|3|___create database実行___|[createCDB.sql](https://github.com/keicyKOh1Ca/style/blob/master/oracle/createCDB.sql)|
|4|___@$ORACLE_HOME/rdbms/admin/catcdb.sql___|CDBセットアップスクリプト実行※1|
|5|___ALTER SYSTEM SET log_archive_format='%T_%S_%r.dbf' scope=BOTH;___| log_archive_format設定（アーカイブログモードにしたい場合のみ）|
|4|___ALTER DATABASE ARCHIVELOG;___|アーカイブモード変更（mount状態で実施）（アーカイブログモードにしたい場合のみ）|

<pre>
※1スクリプト実行時以下のインプットを求められるので、入力
SQL> host perl -I &&rdbms_admin &&rdbms_admin_catcdb --logDirectory &&1 --logFilename &&2
1に値を入力してください: 任意のログ出力先ディレクトリ
2に値を入力してください: 任意のログファイル名
Enter new password for SYS: レスポンスファイル時に設定したSYSのパスワード
Enter new password for SYSTEM: レスポンスファイル時に設定したSYSTEMのパスワード
Enter temporary tablespace name: 一時表領域名（ここではTEMPを指定）
</pre>
---
### 12. PDB作成
|#|command|explanation|
|:--:|:--|:--|
|1|___sqlplus / as sysdba___|sqlplusを抜けている場合|
|2|___!mkdir /u01/app/oracle/product/oradata/PDBS___||
|3|___startup___|※oracleがオープンしていない場合|
|4|___create pluggable database PDB01 admin user 任意のユーザ名 identified by 任意のパスワード file_name_convert = ('/u01/app/oracle/product/oradata/CDB01/pdbseed/', '/u01/app/oracle/product/oradata/PDBS/');___|※seedを利用し作成|
|5|___alter session set container = PDB01___|pdbへ切り替え|
|6|___show con_name___|'PDB01'が表示されればOK|
---
### 13. リスナーからのPDB接続

|#|command|explanation|
|:--:|:--|:--|
|1|___lsnrctl status___|リスナーの状態確認|
|2|___lsnrctl start___|リスナーが立ち上がっていない場合、起動|
|3|___sqlplus PDB時に設定したユーザ/PDB時に設定したパスワード@PDB01___|PDBへ接続（※SIDは上記10でtnsnamesに設定したもの）|

<pre>
!!!接続されればOK
</pre>

### 14. まとめ

<pre>
・21cのXEインストールとは、結構色々設定する必要があるが、configureにてPDBを作成することも可能なので
　そちらもでOK
・preInstallは必須、preInstallをやらないとカーネルパラメータやら、依存パッケージのインストールやら
　手動でやらないといけないので。
・XEと違い、通常のSE2を今回インストールしたので、少し制限は緩和されるがSE2の場合
　PDBの上限が3つ迄となる。(19cは。21cだとどうなのか？）
</pre>

