# javaEE(jakartaEE) tips

### １．バリデーション

何かと便利な「Bean Validation」で

~~~java
・ビーンクラス
@Data // lombok
public class XXBean implements Serializable
{
  @NotNull(message="null NG1", groups = ValiGrp.class)
  private String str_1;
  @NotNull(message="null NG2", groups = ValiGrp.class)
  private String str_2;
  @NotNull(message="null NG3", groups = ValiGrp.class)
  private String str_3;
}
~~~

~~~java
・メインクラス
public class Main
  public String samp()
  {
    Set<ConstraintViolation<XXBean>> results = validator.validate(xxBean, ValiGrp.class);
    result.forEach(result -> {
      FacesContext context = FacesContext.getCurrentInstance();
      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getMessage(), "");
      context.addMessage("ERROR", message);
    });

    if (result.size() > 0)
    {
      System.out.println("=-------------");
      return null;
    }
  }
~~~

のようなクラスがあった時にどれか一つのチェックとかで

~~~java
・メインクラス
public class Main
  public String samp()
  {
    XXBean xxbean = new XXBean();
    xxbean.setStr_1(null);
    xxbean.setStr_2(null);
    xxbean.setStr_3(null);
  
    Set<ConstraintViolation<XXBean>> results = validator.validate(xxBean, ValiGrp.class);
    result.forEach(result -> {
      FacesContext context = FacesContext.getCurrentInstance();
      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, result.getMessage(), "");
      context.addMessage("ERROR", message);
    });

    if (result.size() == 3) // 全てnullの場合にエラーとする。
    {
      System.out.println("=-------------");
      return null;
    }
  }
~~~

あとはmessageはpropertyにセットでもいいし、アノテーションのメッセージを全て統一しておけばいい。
結構便利

また以下の設定を忘れるとnull判定が出来なくなるのも注意

<pre>
<context-param>
    <param-name>javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL</param-name>
    <param-value>true</param-value>
</context-param>
</pre>

### ２．jsfのレンダリング

これは少しびっくりなんだけど、htmlのボタン等のタグのイベント「onclick」とかあるが、つい癖とかで「onClick」とか大文字を挟んだりすると
どうもレンダリングされない。
docにも記載されてなくて、レンダリング後のページのソースを確認したところ、レンダリングされていなかった。

### ３．jsf初期処理

初期処理でも色々あるが、以下の通り纏める。

|#|初期化処理|内容|
|:--:|:--:|:--:|
|1|コンストラクタ|CDIが実行できるようになる前に処理が開始|
|2|ポストコンストラクタ|CDIが実行できるようになって処理が開始|
|3|f:viewAction|オブジェクトの生成にかかわらず、初期画面表示時|

その為、基本は2の@PostConstructを利用するケースが多い。
3については以下

~~~xhtml
<body>
  <f:metadata>
    <f:viewAction action="#{actionBean.init()}"/>
  </f:metadata>
</body>
~~~

jsfに記載し、バッキングビーンのメソッドを呼び出すイメージ
