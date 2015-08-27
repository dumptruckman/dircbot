# dircbot

An IRC bot I wrote using [PircBot](https://github.com/davidlazar/PircBot).

## Usage

Check the downloads folder for a jar file.

Run the bot with the follow command or similar:

`java -jar ./dircbot-1.0-SNAPSHOT-jar-with-dependencies.jar some.irc.server:6667 -n nickname -v -c #bottesting -p adminpassword`

Startup Arguments:

* <server> - the first argument is required and is the server to connect to.
* n [nickname] - the bot's irc nick.
* c - comma separated list of channels to join on connect.
* p [admin password] - the password to login as bot admin, which is required for some commands.
* v - verbose logging
* j - this flag allows anyone to use the join command
* R - this flag disables free rolling (which is where you just roll dice without a command word)
* s - success dice mode... you know what this is if you're using this bot.

Once connected, use the `help` command to learn more about the available commands for the bot.
From a channel, you must prepend commands with a `!`. The exception to this is free rolling.