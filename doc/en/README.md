# Discord Trigger Stuff
Discord Trigger Stuff, DTS in short, is a application that allow you to broadcast triggers to a Discord or play then locally. It hold triggers separately from ACT but it require it to work. You can select which triggers you want broadcast to Discord and which ones you want to play locally. It also support importing and exporting existing triggers directly from ACT allowing for easy migration. 

# Requirements
1. Advanced Combat Tracker with FF14 plugin installed.
2. Java JRE 8u40 or newer. [You can get newest version of Java here](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
3. Discord API token for discord related functionality. [You can get API token here](https://discordapp.com/developers/applications/)
4. VoiceRSS API token for Text To Speech. [You can get API token here](http://www.voicerss.org/personel)

# Installation
Simply extract .7zip file, and run DiscordTriggerStuff.jar. No installation is needed, however keep in mind that application will create some files and folders when run so you might want to keep it in separate folder.

# Languages
DST is currently available in 4 languages, English, German, French and Japanese. However only English is finished. German, French and Japanese uses inaccurate machine translation. Size of the buttons can be wrong and labels can be cut.

# Usage
1. Start ACT and DST.
2. Select log file to scan. Log file can be found automatically is you select log file folder, otherwise you have to select log file manually.
3. Everything is set, and should working now.

# Libs used
* [Discord4J](https://github.com/Discord4J/Discord4J)
* [VoiceRSS SDK](http://www.voicerss.org/sdk/)
* [SLF4J](https://www.slf4j.org/)
