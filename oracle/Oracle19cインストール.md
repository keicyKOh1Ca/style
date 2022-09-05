# Oracle DB 21c XE インストール＆DB構築手順

<pre>
基本は以下、URLのマニュアル参考としているが一部異なる部分もあるので、それを記載
[https://docs.oracle.com/en/database/oracle/oracle-database/21/xeinl/installing-oracle-database-xe.html#GUID-728E4F0A-DBD1-43B1-9837-C6A460432733](https://docs.oracle.com/cd/F19136_01/ladbi/creating-an-oracle-software-owner-user.html#GUID-C1E5CEA8-741A-4500-B03E-B4A6BC1E87BB)
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
|4|___vi /home/oracle/install/db_install.rsp※1___||

[Google先生](https://www.google.co.jp/)
<pre>
※1以下レスポンスファイルサンプル

</pre>

---
### 6. 環境変数設定
|#|command|
|:--:|:--|
|1|___export ORACLE_SID=XE___|
|2|___export ORAENV_ASK=NO___|
|3|___. /opt/oracle/product/21c/dbhomeXE/bin/oraenv___|
---
### 7. DB構築
|#|command|
|:--:|:--|
|1|___/etc/init.d/oracle-xe-21c configure___|
<pre>
※最低大文字英字１つ、小文字英字１つ、数字１つからなるsys,system等のパスワードを入力を求められるので
　控えておく。
 また「データベースの作成が完了しました。～」のメッセージが出力されたらOK
</pre>
---
### 8.接続確認
・oracleユーザにスイッチし、「. /opt/oracle/product/21c/dbhomeXE/bin/oraenv」を実行後、
　SYSDBAとしてsqlplusにて接続(OS認証)し、接続出来ればOK
 |#|command|
 |:--:|:--|
 |1|___sqlplus / as sysdba___|
---
### 9.まとめ
・今回は21cなので、当然プラガブルDB（以下PDB）構成となっており、ここまので工程を完了したら既に、PDBは
　存在する。
  ただし、PDB上にスキーマを作成する必要もあるので、忘れずに。
  
・XEのPDBの上限は3つ迄、かつ最大ストレージ容量は12Gの2CPUまでとなる。

・もちろんPDBなのでリスナー接続が必須のため、tnsnamesの設定もお忘れなく。




