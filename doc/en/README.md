# !WARNING!
Due multiple reasons, I have to roll back to Java 8. As much I wanted to stick to Java 10 to avoid people having to reinstall Java I'm forced to do so. High incompatibility of JVM, plug-ins and dependencies. Oracle license change, removal JRE 10 from public download. And my mistake by using wrong byte code level. I will migrate project back to Java 8. Java 8 going to be supported till end of 2020. Would like to apologize for trouble I caused. 
 
## Discord Trigger Stuff
Discord Trigger Stuff, DTS in short, is a application that allow you to broadcast triggers to a Discord or play then locally. It hold triggers separately from ACT but it require it to work. You can select which triggers you want broadcast to Discord and which ones you want to play locally. It also support importing and exporting existing triggers directly from ACT allowing for easy migration. 

## Table of contents
1. [Requirements](https://github.com/Orthae/DiscordTriggerStuff/blob/master/doc/en/README.md#requirements)
2. [Download](https://github.com/Orthae/DiscordTriggerStuff/blob/master/doc/en/README.md#requirements)
3. [Installation](https://github.com/Orthae/DiscordTriggerStuff/blob/master/doc/en/README.md#requirements)
4. [Languages](https://github.com/Orthae/DiscordTriggerStuff/blob/master/doc/en/README.md#requirements)
5. [Usage](https://github.com/Orthae/DiscordTriggerStuff/blob/master/doc/en/README.md#requirements)


## Requirements
1. Advanced Combat Tracker with FF14 plugin installed.
2. Java SE JRE 8. It work with higher versions of Java due high JVM incompatibility, [You can get newest version of Java 8 here](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
3. Discord API token for discord related functionality. [You can get API token here](https://discordapp.com/developers/applications/)
4. VoiceRSS API token for Text To Speech. [You can get API token here](http://www.voicerss.org/personel)

## Download
Newest version is always available in releases section. [You can find release section here](https://github.com/Orthae/DiscordTriggerStuff/releases)

## Installation
No installation is needed, however keep in mind that application will create some files and folders when launched so you might want to keep it in separate folder. It doesn't store anything outside folder it is running in.

## Languages
DST is currently available in 4 languages, English, German, French and Japanese. However only English is finished. German, French and Japanese uses inaccurate machine translation. Size of the buttons can be wrong and labels can be cut.

## Usage
1. Start ACT and DST.
2. Select log file to scan. Log file can be found automatically is you select log file folder, otherwise you have to select log file manually.
3. Everything is set, and should working now.

## Libs used
* [Discord4J](https://github.com/Discord4J/Discord4J)
* [VoiceRSS SDK](http://www.voicerss.org/sdk/)
* [SLF4J](https://www.slf4j.org/)
