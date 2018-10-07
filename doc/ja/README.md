警告これは不正確な機械翻訳です

# Discord Trigger Stuff
Discord Trigger Stuffは、DTSをトリプルにブロードキャストしたり、ローカルで再生したりするためのアプリケーションです。 ACTとは別にトリガーを保持しますが、動作させる必要があります。 Discordにブロードキャストするトリガーと、ローカルで再生するトリガーを選択できます。また、既存のトリガーをACTから直接インポートおよびエクスポートすることも可能で、簡単に移行できます。

# 要件
1. FF14プラグインを搭載したアドバンスドコンバットトラッカー。
2. Java JRE 8u40以降。 [Javaの最新バージョンを入手できます]（https://www.oracle.com/technetwork/java/javase/downloads/index.html）
3. discord関連機能のAPIトークンを解読する。 [ここでAPIトークンを取得できます]（https://discordapp.com/developers/applications/）
4. Text To SpeechのVoiceRSS APIトークン。 [ここでAPIトークンを取得できます]（http://www.voicerss.org/personel）

# インストール
.7zipファイルを抽出し、DiscordTriggerStuff.jarを実行するだけです。インストールは必要ありませんが、アプリケーションは実行時にいくつかのファイルとフォルダを作成し、別のフォルダに保存しておきたいことに注意してください。

# 言語
DSTは、現在、英語、ドイツ語、フランス語、日本語の4言語で利用できます。ただし、英語のみが終了します。ドイツ語、フランス語、日本語では機械翻訳が不正確です。ボタンのサイズが間違っている可能性があり、ラベルをカットすることができます。

# 使用法
1. ACTとDSTを開始します。
2. スキャンするログファイルを選択します。ログファイルは自動的に見つけることができますあなたはログファイルフォルダを選択して、それ以外の場合は手動でログファイルを選択する必要があります。
3. すべてが設定され、今作業する必要があります。

# Libs used
* [Discord4J]（https://github.com/Discord4J/Discord4J）
* [VoiceRSS SDK]（http://www.voicerss.org/sdk/）
* [SLF4J]（https://www.slf4j.org/）
