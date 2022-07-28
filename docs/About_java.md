# １．Javaとは

　複雑なことは、置いといてJavaはOSに依存せず、どの環境でも同じように動作することが出来る言語で
　またオブジェクト言語である。（※重要）
　なぜOSに依存しないかというと、Javaが実行する環境はJVMという仮想環境で動作する為である。
　そのJVMがOSとの懸け橋の役割をしている為、OSに依存しない。

# ２．JavaEE（JakartaEE）とは

　Javaは様々なAPIの組み合わせで動作する。
　例えば、Eclipseで「Hello　World」を出力するプログラムを作成する場合にも
　JavaのAPIを用いて、結果を出力している。
　※System.out.println()とか

　その様々なAPIの総称をJava SE（Java Platform, Standard Edition）と呼ぶ
　※（昔はJ2SEとか言われてた。）

　じゃあJavaEE（昔はJ2EE）っていうのは？
　JavaSEを包括している大規模な開発を行う場合に使用するAPI群


  +JakartaEEの機能+
　<pre>
　・JSF（JavaServer Faces）
　　高機能なユーザー・インタフェースを効率的に作るためのWebアプリケーション・フレームワーク

　・Java Servlet
　　JSFが内部的に使用しているサーバ・サイドのコンポーネント技術

　・EJB
　　ビジネス・ロジックの開発に使用

　・JPA（Java Persistence API）
　　Javaプログラムから、通常のJavaオブジェクトと同様にデータベースの操作が行えるようにする
　　オブジェクト／リレーショナル（O/R）マッピング・フレームワーク

　・JAX-RS
　　作成したコンポーネントをWebサービスとして公開するための機能

　・CDI（Container Dependency Injection）
　　依存性の注入（DI：Dependency Injection）」機能を備えたDIフレームワーク。
　　上記各層のコンポーネント間をアノテーションによって連携させるための規約を定めている。
　　CDIを使うことで、各層の密結合を防ぎ、アプリケーション開発／保守時のテストが
　　容易になるというメリットが得られる
　</pre>

参考URL：
・Java SEとJDK、JRE、JVMの違いに関する解説
[[https://www.javadrive.jp/start/install/index5.html#section1]]

