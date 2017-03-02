# Discord-Java-RPBot
A simple pen & paper roleplaying bot written in Java. 
The purpose of this is to supplement pen & paper roleplayers that communicate via Discord.

## How to Use

### Setting up the Bot (Discordapp.com)
1. Go to Discordapp.com and [create a "New App"](https://discordapp.com/developers/applications/me)
2. Note down the ID if your App.
4. Add your own App ID to the oAuth2 URL: https://discordapp.com/api/oauth2/authorize?client_id=<YOUR-ID-HERE>&scope=bot&permissions=0
5. Enter the URL into your browser, and select which channel you want the bot to connect. 
6. Done. The bot should now appear as "Offline" in your Discord channel.

### Logging into the Bot (Method 1)
1. Go to Discordapp.com and [copy the Token value of your bot](https://discordapp.com/developers/applications/me)
2. Create a token.txt textfile and add it to the root of your project
3. Open token.txt and store the token on the first line
4. Done, you 

## Commands
Command Name                    |  Example of use | Description
:-------------------------:|:-------------------------:
/roll | /roll 1d6 | Generates dices equal to the first number, and rolls values between 1 and the second number. (e.g. /roll 2d4 will roll two 4-sided dices)
/calc | /calc (1*2)/2 | Calculates mathematical expressions.

## License
GPLv3 License
Discord-Java-RPBot - A simple roleplaying bot for Discord
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


