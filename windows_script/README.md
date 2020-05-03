# Setup

JDKを以下からダウンロードします。

https://jdk.java.net/14/

README.md (このファイル) があるディレクトリに解凍し、ディレクトリ名を `jdk` に変更します。

`midi2musicxml.bat` を実行することで GUI が起動します。

# Build Installer (Optional, 開発者向け)

JDKを取得しPATHを通します。version 14以降が必要です。

WIXをダウンロード、インストールします。
https://wixtoolset.org/

自動的にPATHが設定されるので再起動します。

ソースコードを取得してインストーラをビルドします。

```
git clone https://github.com/kaibadash/midi2musicxml
cd midi2musicxml
gradlew shadowJar
jpackage --main-class com.pokosho.midi2musicxml.MainKt -i build/libs -n midi2musicxml --main-jar midi2musicxml.jar --icon src/main/resources/crab.ico --vendor kaiba --app-version 0.0.1
```




