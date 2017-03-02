/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot;

import rpbot.client.Client;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.util.DiscordException;

/**
 * Main
 * @author ivanskodje
 */
public class Main
{

	private static String token = ""; // Bot token

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{		
		try (BufferedReader reader = new BufferedReader(new FileReader("token.txt")))
		{
			// Load token
			// NB: Never share your token, or upload it to any repository!
			token = reader.readLine();
			
			// Initiate Main Client
			Client.init(new ClientBuilder().withToken(token).login());

		} 
		catch (IOException | DiscordException ex) 
		{
			ex.printStackTrace();
		}
	}
}
