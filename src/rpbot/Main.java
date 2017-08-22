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
import rpbot.derbydatabase.Database;
import rpbot.derbydatabase.DatabaseHandler;
import rpbot.derbydatabase.DerbyDatabase;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.util.DiscordException;

/**
 * Main
 *
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
		String repositoryName = "MyRepository";
		String username = repositoryName.toLowerCase();
		String password = "password";

		try (BufferedReader reader = new BufferedReader(new FileReader("token.txt")))
		{
			// Load token
			// NB: Never share your token, or upload it to any repository!
			token = reader.readLine();

			// Initiate Main Client
			Client.init(new ClientBuilder().withToken(token).login());

			// Create a Database and add it to the Handler
			Database database = new DerbyDatabase(repositoryName, username, password); // This is the part that can be changed if you want to use another database
			DatabaseHandler.init(database);
		}
		catch (IOException | DiscordException ex)
		{
			ex.printStackTrace();
		}
	}
}
