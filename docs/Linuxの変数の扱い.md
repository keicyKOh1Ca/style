# １．コマンドで

<pre>
# ファイル名取得(拡張子あり)
basename '/hoge01/hoge02/hoge03.4.txt'
# hoge03.4.txt

# ファイル名を取り出す(拡張子なし)
basename '/hoge01/hoge02/hoge03.4.txt' .txt
# hoge03.4

dirname '/hoge01/hoge02/hoge03.4.txt'
# /hoge01/hoge02
</pre>

#  ２．変数内でのパターンマッチ

<pre>
形式：
${変数#パターン}   # 先頭から最短一致した部分を取り除く
${変数##パターン}  # 先頭から最長一致した部分を取り除く
${変数%パターン}   # 末尾から最短一致した部分を取り除く
${変数%%パターン}  # 末尾から最長一致した部分を取り除く

例：
#!/bin/bash
fpath='/hoge01/hoge02/hoge03.4.txt'

# ファイル名を取り出す（拡張子あり）
faname_ext="${fpath##*/}"
echo $fname_ext
# hoge03.4.txt

# ファイル名を取り出す（拡張子なし）
fname="${fname_ext%.*}"
echo $fname
# hoge03.4

# 拡張子を取り出す
fext="${fpath##*.}"
echo $fext
#txt

# ディレクトリを取り出す
fdir="${fpath%/*}"
echo $fdir
# /hoge01/hoge02

# 拡張子を変更する
echo "${fpath%.*}.log"
# /hoge01/hoge02/hoge03.4.log
</pre>
