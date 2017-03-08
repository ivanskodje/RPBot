/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.command;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import rpbot.client.Client;
import sx.blah.discord.handle.obj.IUser;

/**
 * The purpose of this command is to provide you with different tables (oddities, artifacts, etc).
 * @author ivanskodje
 */
public class Table
{
	/**
	 * Executes the command
	 * @param context 
	 */
	public static void Execute(CommandContext context)
	{
		// Username (@<name>#id)
		IUser userName = context.getMessage().getAuthor();
		
		// Get all arguments and put them into an arraylist for better string manipulation
		ArrayList<String> arguments = new ArrayList<>();
		
		// The Table row number we are going to get
		int num = 0;
		
		// Get all arguments (there MUST be arguments, but there is no need for a number)
		String[] args = context.getArgs();
		
		// If there are no arguments, display a list of valid commands
		if(args.length < 1)
		{
			invalidExpression(context);
			return;
		}
		
		// Add all arguments into an ArrayList for greater manipulation
		for(String str : args)
		{
			arguments.add(str);
		}

		try
		{
			// Assign as integer
			num = Integer.parseInt(arguments.get(arguments.size()-1));
			
			// If num is less than 1, stop.
			if(num <= 0)
			{
				invalidExpression(context);
				return;
			}
			
			// Remove last arg
			arguments.remove(arguments.size()-1);
			
			// If we have no more arguments, display error
			if(arguments.size() < 1)
			{
				invalidExpression(context);
				return;
			}
			
		}
		catch(NumberFormatException ex)
		{
			// No defined number: no need to throw error as it is expected.
		}

		String commandVars = "";	// The commands names
		String fileName = "";		// The file name
		
		// Update name variables so that we can get a filename and check for matching command names
		for(String str : arguments)
		{
			commandVars += str + " ";
			fileName += str + ".";
		}
		
		// Remove last character from each string (which have an empty space or a dot)
		commandVars = commandVars.substring(0, commandVars.length()-1);
		fileName = fileName.substring(0, fileName.length()-1);
		
		
		// Do we have a valid book that matches the arguments?
		if(getCommandVariables().contains(commandVars))
		{
			// Full data path to the table
			String dataPath = "src/rpbot/data/table/" + fileName;
			
			// Table data (which we will output)
			String tableData = "";
			
			// Max number of lines
			int maxLines = 0;
			
			try
			{
				// Get number of table lines from first line
				maxLines = Integer.parseInt(Files.readAllLines(Paths.get(dataPath)).get(0));
				
				// If num is 0, it means no num was specified - select a random value
				if(num <= 0)
				{
					Random rnd = new Random();
					num = rnd.nextInt(maxLines) + 1;
				}
				else if(num > maxLines)
				{
					// Inform about max table length
					Client.sendMessage(context.getMessage().getChannel(), userName + ":\n**" + commandVars + "** has a total of " + (maxLines) + " options. Try a different value.");
					return;
				}
				
				tableData = Files.readAllLines(Paths.get(dataPath)).get(num);
				Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + tableData);
			} 
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			invalidExpression(context);
			return;
		}
	}
	
	
	
	
	public static ArrayList<String> getCommandVariables()
	{
		// Folder with the tables
		File folder = new File("src/rpbot/data/table");
		
		// Get a list of files
		File[] listOfFiles = folder.listFiles();
		
		// Tables
		ArrayList<String> fileNames = new ArrayList<>();
		
		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if (listOfFiles[i].isFile()) 
			{
				fileNames.add(listOfFiles[i].getName().replace(".", " "));
			} 
			/*
			else if (listOfFiles[i].isDirectory()) 
			{
				System.out.println("Directory " + listOfFiles[i].getName());
			}
			*/
		}
		
		return fileNames;
	}
	
	
	/* 
	Used to register table as a command
	*/
	public static Command register()
	{
		// Command: table
		Command table = new Command("table");
		table.onExecuted((context) ->
		{
			Table.Execute(context);
		});
		
		return table;
	}

	
	/* 
	Runs whenver the user runs an invalid expression
	*/
	private static void invalidExpression(CommandContext context)
	{
		// Username
		IUser userName = context.getMessage().getAuthor();
		
		// Message
		String msg = "__**Table Commands:**__\n";
			
		// Append all valid commands
		for(String str : getCommandVariables())
		{
			msg += "!table " + str + "\n";
		}
		
		// Send list of valid commands
		Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + msg);
	}
}
