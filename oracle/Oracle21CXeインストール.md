# Oracle DB 21c XE インストール＆DB構築手順

<pre>
基本は以下、URLのマニュアル参考としているが一部異なる部分もあるので、それを記載
https://docs.oracle.com/en/database/oracle/oracle-database/21/xeinl/installing-oracle-database-xe.html#GUID-728E4F0A-DBD1-43B1-9837-C6A460432733
</pre>

---
### 0. インストール環境＆前提条件
- AWS EC2
- Linux（redhat8）
- 2cpu memory:8G
- Oracleのインストーラは事前にOracleのサイトからDLして、EC2へアップ済
---
### 1. teratermにて、EC2インスタンスに接続
- ユーザはec2ユーザでもOKだし、自作のアカウントでも結構
---
### 2. ユーザの切り替え
|#|command|
|:--:|:--|
|1|___su - root___|
---
### 3. Oracle Preインストール実施※1
・DL先は任意（今回は/tmp配下に適用）
|#|command|
|:--:|:--|
|1|curl -o oracle-database-preinstall-21c-1.0-1.el8.x86_64.rpm https://yum.oracle.com/repo/OracleLinux/OL8/appstream/x86_64/getPackage/oracle-database-preinstall-21c-1.0-1.el8.x86_64.rpm※2|
|2|dnf -y localinstall oracle-database-preinstall-21c-1.0-1.el8.x86_64.rpm※3|

<pre>
※1：このインストーラはOracleDBのインストールの前に、必要要件である、Linuxのモジュール等を自動的にインストールするもの
以下のように依存関係パッケージを自動的にインストールしてくれる。※基本は足りない場合、手動でリポジトリをセットし、インストールする。
================================================================================================================
 パッケージ                          Arch        バージョン               リポジトリー                    サイズ
================================================================================================================
インストール:
 oracle-database-preinstall-21c      x86_64      1.0-1.el8                @commandline                     30 k
依存関係のインストール:
 compat-openssl10                    x86_64      1:1.0.2o-4.el8_6         rhel-8-appstream-rhui-rpms      1.1 M
 gssproxy                            x86_64      0.8.0-20.el8             rhel-8-baseos-rhui-rpms         119 k
 keyutils                            x86_64      1.5.10-9.el8             rhel-8-baseos-rhui-rpms          66 k
 libICE                              x86_64      1.0.9-15.el8             rhel-8-appstream-rhui-rpms       74 k
 libSM                               x86_64      1.2.3-1.el8              rhel-8-appstream-rhui-rpms       48 k
 libX11-xcb                          x86_64      1.6.8-5.el8              rhel-8-appstream-rhui-rpms       14 k
 libXcomposite                       x86_64      0.4.4-14.el8             rhel-8-appstream-rhui-rpms       29 k
 libXinerama                         x86_64      1.1.4-1.el8              rhel-8-appstream-rhui-rpms       16 k
 libXmu                              x86_64      1.1.3-1.el8              rhel-8-appstream-rhui-rpms       75 k
 libXrandr                           x86_64      1.5.2-1.el8              rhel-8-appstream-rhui-rpms       34 k
 libXt                               x86_64      1.1.5-12.el8             rhel-8-appstream-rhui-rpms      185 k
 libXv                               x86_64      1.0.11-7.el8             rhel-8-appstream-rhui-rpms       20 k
 libXxf86dga                         x86_64      1.1.5-1.el8              rhel-8-appstream-rhui-rpms       26 k
 libXxf86misc                        x86_64      1.0.4-1.el8              rhel-8-appstream-rhui-rpms       23 k
 libXxf86vm                          x86_64      1.1.4-9.el8              rhel-8-appstream-rhui-rpms       19 k
 libdmx                              x86_64      1.1.4-3.el8              rhel-8-appstream-rhui-rpms       22 k
 libverto-libevent                   x86_64      0.3.0-5.el8              rhel-8-baseos-rhui-rpms          16 k
 nfs-utils                           x86_64      1:2.3.3-51.el8           rhel-8-baseos-rhui-rpms         504 k
 policycoreutils-python-utils        noarch      2.9-16.el8               rhel-8-baseos-rhui-rpms         252 k
 quota                               x86_64      1:4.04-14.el8            rhel-8-baseos-rhui-rpms         214 k
 quota-nls                           noarch      1:4.04-14.el8            rhel-8-baseos-rhui-rpms          95 k
 rpcbind                             x86_64      1.2.5-8.el8              rhel-8-baseos-rhui-rpms          70 k
 xorg-x11-utils                      x86_64      7.5-28.el8               rhel-8-appstream-rhui-rpms      135 k
 xorg-x11-xauth                      x86_64      1:1.0.9-12.el8           rhel-8-appstream-rhui-rpms       39 k

トランザクションの概要
================================================================================================================
インストール  25 パッケージ

※2：マニュアルのURLは「appstream」の部分がRedhat7のURLの「latest」となっているため、DLエラーとなっていた。記載されているURLが間違っている。
※3：dnfはLinux8から用意されたyumの後継コマンド
</pre>
---
### 4. perlのインストール
|#|command|
|:--:|:--|
|1|___yum install -y perl___|
<pre>
oracleDBのプログラム内にperlで実行するものがあるので、プリインストールされていれば基本必要ないかもだけど、念のため
</pre>
---
### 5. OracleDBのインストール
・スワップ領域がない場合、作成する。
<br>
|#|command|explanation|
|:--:|:--|:--|
|1|___swapon -s ( or cat /proc/swaps)___|スワップ領域が適用されているか|
|2|___dd if=/dev/zero of=/tmp/swapfile bs=1M count=2000___|ない場合、作成※1|
|3|___mkswap /tmp/swapfile___|スワップ領域として適用|
|4|___swapon /tmp/swapfile___|スワップ領域ON|
|5|___swapon -s ( or cat /proc/swaps)___|スワップ領域が適用されているか|
<pre>
※1：マウント先をスワップ領域としてもOKだが、今回は容量の関係上ファイルを作成
</pre>

・OracleXEインストール
|#|command|
|:--:|:--|
|1|___dnf -y localinstall /home/test_user/oracle-database-xe-21c-1.0-1.ol8.x86_64.rpm___|
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





