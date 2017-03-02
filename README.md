# RPBot
A simple pen & paper roleplaying bot written in Java. 
The purpose of this bot is to supplement pen & paper roleplayers that use Discord.

Contributions are always welcome.

## How to setup
Open the project in the IDE of your choice. It is assumed you know how to import new projects, as well as how to import libraries.

### Setting up the Bot (Discordapp.com)
1. Go to Discordapp.com and [create a "New App"](https://discordapp.com/developers/applications/me)
2. Note down the given ID if your App.
4. Add your own App ID to the oAuth2 URL: https://discordapp.com/api/oauth2/authorize?client_id=<YOUR-ID-HERE>&scope=bot&permissions=0
5. Enter the URL into your browser, and select which channel you want the bot to connect. 
6. Done. The bot should now appear as "Offline" in the selected Discord channel.

### Bot Login (Method 1)
1. Go to Discordapp.com and [copy the Token value of your bot](https://discordapp.com/developers/applications/me)
2. Create a token.txt textfile and add it to the root of your project
3. Open token.txt and store the token on the first line. NB: Never share your token file. If you do, other people can login as YOUR bot.
4. Run Main.java and the bot should login. Test it by typing in '/roll 1d6'.

## Available Commands

| Command       | Example of use      | Description                                                                                        |
|---------------|--------------------------------------------------------------------------------------------------------------------------|
| roll          | /roll 1d6           | Generates dices equal to the first number, and rolls values between 1 and the second number.       |
| calc          | /calc (1*2)/2       | Calculates mathematical expressions.                                                               |


## Additional Information (Discord4J and Command4J)
Check out the documentation for [Discord4J](https://github.com/austinv11/Discord4J) and the [Discord4J Plugin: Command4J](https://github.com/Discord4J-Addons/Commands4J).

You can also ask for help on the official [Discord4J Discord Server](https://discord.gg/NxGAeCY).

## License
GPLv3 License

Copyright (C) 2016 Ivan P. Skodje

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

![GPLv3](http://www.gnu.org/graphics/gplv3-127x51.png)


