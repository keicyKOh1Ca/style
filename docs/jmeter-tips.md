# jmeter tips

### １．javaEE(jsf)上にておける注意点

レンダリングされたHTMLには「javax.faces.ViewState」というhiddenタイプのタグが自動生成される。
これは現在の画面（ビュー）を表すためのコードとなり、これが変更されるとビューの復元が出来なくなる。
※CSRF（Cross-Site Request Forgeries）対策としても取り扱っている。

なので、jmeterで記録したシナリオについて変更と、「httpクッキーマネージャ」を設定する。
※httpクッキーはおそらくjsfのライフサイクル上でCookie情報を利用する為か？

#### ①httpクッキーマネージャ設定
![httpクッキーマネージャ](https://github.com/keicyKOh1Ca/style/blob/master/docs/jmeter_cookei_manager.png)

#### ②正規表現抽出（「javax.faces.ViewState」対応）
![正規表現設定](https://github.com/keicyKOh1Ca/style/blob/master/docs/jmeter_regular_expression_extraction.png)
