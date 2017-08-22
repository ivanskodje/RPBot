/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.client;

import com.darichey.discord.api.CommandRegistry;
import rpbot.command.Calc;
import rpbot.command.Help;
import rpbot.command.Name;
import rpbot.command.Roll;
import rpbot.command.Table;
import rpbot.command.list.List;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * Client
 *
 * @author ivanskodje
 */
public class Client
{

	/**
	 * Initiates the Client singleton
	 *
	 * @param client
	 */
	public static void init(IDiscordClient client)
	{
		// Setup Prefix
		CommandRegistry.getForClient(client).setPrefix("!");
		CommandRegistry.getForClient(client).register(Help.register());
		CommandRegistry.getForClient(client).register(Roll.register());
		CommandRegistry.getForClient(client).register(Calc.register());
		CommandRegistry.getForClient(client).register(Name.register());
		CommandRegistry.getForClient(client).register(Table.register());
		CommandRegistry.getForClient(client).register(List.register());
		// CommandRegistry.getForClient(client).register(Sheet.register());
	}

	/**
	 * Send message to channel
	 *
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
