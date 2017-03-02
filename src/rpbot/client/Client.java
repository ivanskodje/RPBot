/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.client;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandRegistry;
import rpbot.command.Calc;
import rpbot.command.Roll;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * Client
 * @author ivanskodje
 */
public class Client
{	
	/**
	 * Initiates the Client singleton
	 * @param client 
	 */
	public static void init(IDiscordClient client)
	{
		// Command Registry Config
		CommandRegistry.getForClient(client).setPrefix("/");
		
		// Command: roll
		Command roll = new Command("roll");
		roll.withAliases("dice", "rolling", "d", "r");
		roll.onExecuted((context) ->
		{
			Roll.Execute(context);
		});
		CommandRegistry.getForClient(client).register(roll);
		
		// Command: calc
		Command calc = new Command("calc");
		calc.withAliases("c", "math", "kalk", "calculate");
		calc.onExecuted((context) ->
		{
			Calc.Execute(context);
		});
		CommandRegistry.getForClient(client).register(calc);
	}
	
	
	/**
	 * Send message to channel
	 * @param channel
	 * @param message 
	 */
	public static void sendMessage(IChannel channel, String message)
	{
		try
		{
			channel.sendMessage(message);
		} 
		catch (MissingPermissionsException | DiscordException | RateLimitException ex)
		{
			ex.printStackTrace();
		}
	}
}
