# JP1からPowerShellを起動しようとしたら、エラーになる・・

## １．試したこと

①直打ちで指定のバッチを起動する。<br>
　→起動し、想定している結果となる。<br>
　　※プロキシの設定等が有効とならなかったため、バッチ内からPowerShell内に変更し起動確認<br>
<br>
②JP1から起動する。<br>
　→起動はするが、以下エラーとなる。<br>
<br>
<pre>
　「
　Connect-MsolService : 'Connect-MsolService' コマンドはモジュール 'MSOnline' で見つかりましたが、このモジュールを読み込むこ_
　とができませんでした。詳細については、'Import-Module MSOnline' を実行してください。
　発生場所 D:\COMPANY_EXT_TestB\batch\IF\AzureAD\Connect-Msol_new.ps1:43 文字:1
　+ Connect-MsolService -Credential $cred
　+ ~~~~~~~~~~~~~~~~~~~
  　  + CategoryInfo          : ObjectNotFound: (Connect-MsolService:String) [], CommandNotFoundException
    　+ FullyQualifiedErrorId : CouldNotAutoloadMatchingModule
　 
　New-MsolUser : 'New-MsolUser' コマンドはモジュール 'MSOnline' で見つかりましたが、このモジュールを読み込むことができません
　でした。詳細については、'Import-Module MSOnline' を実行してください。
　発生場所 D:\COMPANY_EXT_TestB\batch\IF\AzureAD\Connect-Msol_new.ps1:55 文字:41
　+ Import-Csv -Path $input_file | foreach {New-MsolUser -DisplayName $_. ...
　+                                         ~~~~~~~~~~~~
  　  + CategoryInfo          : ObjectNotFound: (New-MsolUser:String) [], CommandNotFoundException
    　+ FullyQualifiedErrorId : CouldNotAutoloadMatchingModule_
　」
</pre>
　※JP1から起動する際に、環境変数等変わる為、JP1のPCジョブ内で環境変数を設定するもダメ。<br>
　　また、直打ちとJP1との起動時の環境変数の差異も発生していたが、それをPCジョブ内の<br>
　　環境変数欄に設定するもダメ。<br>
<br>
③PS1実行時の環境変数を確認<br>
　PS1実行時のパスの設定をバッチファイルで実施していた為、PS1ファイル内で<br>
　環境変数の確認を実施（$env:PSModulePathコマンドで確認）<br>
<br>
　・・・・すると直打ち起動とJP1起動時とで差があった。<br>
<br>
　直打ち：*c:\Program Files\WindowsPowerShell\Modules*<br>
　JP1起動：*c:\Program Files (x86)\WindowsPowerShell\Modules*<br>
<br>
　上記パスはJP1起動32bitがで直打ちが64bitだったので、恐らく32bit起動してそう・・<br>
　なので、バッチで指定していたパスがダメそうだったのでPS1内で<br>
　環境変数の設定を行うが、それでもダメ。<br>
<br>
　設定例：*$env:PSModulePath="c:\Program Files\WindowsPowerShell\Modules;～"*<br>
<br>
・・・・何故？
<br>
## ２．原因

どうも32bitアプリケーションから64bitのアプリケーションを実行すると32bitのパスに<br>
自動的にリダイレクトされてしまうようで、それが原因でモジュールが違うよエラーとなって<br>
しまってるらしい。<br>
<br>
その為、JP1上から起動する時は、以下の設定で行う必要がる。<br>
<br>
*%systemroot%\sysnative\実行ファイル名*
<br>
参考URL：
・64bit Windows 上で 32bit アプリケーションから 64bit アプリケーションを実行する方法
[[https://webcache.googleusercontent.com/search?q=cache:ZK2ZJNyax2oJ:https://learnin.hatenablog.com/entry/20110424/p1+&cd=5&hl=ja&ct=clnk&gl=jp&client=opera]]

・9.5.1　WOW64環境でx86対応のJP1/AJS3を使用する場合の注意事項
[[http://itdoc.hitachi.co.jp/manuals/3021/30213B1310/AJSH0171.HTM]]

今回設定した内容は添付の通り
![jp1_setting](https://user-images.githubusercontent.com/74536272/181411006-9785dfd5-070a-410b-b479-0e71156e21f4.png)
