/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.command;

import com.darichey.discord.api.CommandContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import rpbot.client.Client;
import sx.blah.discord.handle.obj.IUser;

/**
 *
 * @author ivanskodje
 */
public class Name
{
	/**
	 * Executes the command
	 * @param context 
	 */
	public static void Execute(CommandContext context)
	{
		// Get our argument
		String arg = "";
		
		// If we have no arguments, send a warning message and return
		if(context.getArgs().length < 1)
		{
			// Generate a random name
			Random random = new Random();
			int rndInt = random.nextInt(2); // Adding +1 since we dont want 0 to be an option
			System.out.println("<----- RND INT : " + rndInt);
			if(rndInt == 1)
			{
				arg = "male";
			}
			else
			{
				arg = "female";
			}
		}
		else
		{
			arg = context.getArgs()[0].toLowerCase();
		}
		
		// Username (@<name>#id)
		IUser userName = context.getMessage().getAuthor();
		
		// If the argument is "help", display help text
		if(arg.equals("help"))
		{
			Client.sendMessage(context.getMessage().getChannel(), userName + ":\n!name male | female");
			return;
		}
		
		// First name
		String firstName = "";
		
		// Last name
		String lastName = "";
				
		// Male name
		if(arg.equals("male"))
		{
			String malePath = "src/rpbot/data/names/male.firstname.list";
			
			// Generate firstname
			try 
			{
				// Get number of lines
				int lineNumbers = Integer.parseInt(Files.readAllLines(Paths.get(malePath)).get(0));
				
				// Generate random number
				Random random = new Random();
				int lineNum = random.nextInt(lineNumbers) + 1; // Adding +1 since we dont want 0 to be an option
				
				// Assign firstname
				firstName = Files.readAllLines(Paths.get(malePath)).get(lineNum).toLowerCase();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		// Female name
		else if(arg.equals("female"))
		{
			String femalePath = "src/rpbot/data/names/female.firstname.list";
			
			// Generate firstname
			try 
			{
				// Get number of lines
				int lineNumbers = Integer.parseInt(Files.readAllLines(Paths.get(femalePath)).get(0));
				
				// Generate random number
				Random random = new Random();
				int lineNum = random.nextInt(lineNumbers) + 1; // Adding +1 since we dont want 0 to be an option
				
				// Assign firstname
				firstName = Files.readAllLines(Paths.get(femalePath)).get(lineNum).toLowerCase();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	
		try 
		{
			// Lastname path
			String lastNamePath = "src/rpbot/data/names/all.surname.list";

			// Get number of lines
			int lineNumbers = Integer.parseInt(Files.readAllLines(Paths.get(lastNamePath)).get(0));

			// Generate random number
			Random random = new Random();
			int lineNum = random.nextInt(lineNumbers) + 1; // Adding +1 since we dont want 0 to be an option

			// Assign firstname
			lastName = Files.readAllLines(Paths.get(lastNamePath)).get(lineNum).toLowerCase();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

		// Set final firstname and lastname
		firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
		lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
		
		// Send result
		Client.sendMessage(context.getMessage().getChannel(), userName + ": " + firstName + " " + lastName);
	}
}
