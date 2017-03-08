/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.command;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandContext;
import rpbot.client.Client;
import sx.blah.discord.handle.obj.IUser;

/**
 *
 * @author ivanskodje
 */
public class Help
{
	/**
	 * Executes the command
	 * @param context 
	 */
	public static void Execute(CommandContext context)
	{
		// Username (@<name>#id)
		IUser userName = context.getMessage().getAuthor();
		
		// Generate help text
		// TODO: Automatically generate help text instead of hardcoding the string
		String helpText = "__**Command List:**__\n"
				+ "roll, calc, name, table";
		
		// Send a list of commands
		Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + helpText);
	}
	
	
	/* 
	Used to register table as a command
	*/
	public static Command register()
	{
		// Command: help
		Command help = new Command("help");
		help.withAliases("h");
		help.onExecuted((context) ->
		{
			Help.Execute(context);
		});
		
		return help;
	}
}
