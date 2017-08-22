/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.command.list;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandContext;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import rpbot.client.Client;
import rpbot.command.list.classes.Item;
import rpbot.command.list.classes.ItemList;
import rpbot.command.list.classes.Property;
import rpbot.command.list.database.DatabaseHandler;
import rpbot.command.list.database.SQLiteDatabase;
import sx.blah.discord.handle.obj.IUser;

/**
 *
 * @author Ivan Skodje
 */
public class List
{

	private static DatabaseHandler databaseHandler;

	private final static String[] ADMINS =
	{
		"237888140633702400" // Me
	};

	/**
	 * Executes the command
	 *
	 * @param context
	 */
	public static void Execute(CommandContext context)
	{
		// Username (@<name>#id)
		IUser userName = context.getMessage().getAuthor();

		int argSize = context.getArgs().length;

		// NO ARGUMENTS
		// If we have no arguments, display root lists
		if (argSize < 1)
		{
			ArrayList<ItemList> lists = databaseHandler.getLists();
			String output = "**[** *ID* **]**\t\t**List Name**\n\n";
			for (ItemList list : lists)
			{
				output += "[" + list.getId() + "]   " + list.getName() + "\n";
			}

			Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + output);
			return;
		}

		if (argSize >= 2)
		{
			String arg1 = context.getArgs()[0].toLowerCase();
			String name = "";

			if (arg1.equals("add"))
			{
				for (String arg : context.getArgs())
				{
					name += arg + " ";
				}

				// Remove trailing whitespaces
				name = name.trim();
				name = name.replaceFirst(arg1 + " ", "");

				ItemList list = new ItemList(name);
				list = databaseHandler.insertList(list);

				if (list != null)
				{
					Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + name + " was successfully added.");
					return;
				}
			}
			else if (arg1.equals("remove"))
			{
				// Check if user have permission to use remove
				if (!hasAdmin(userName.getID()))
				{
					System.out.println("userName.mention(): " + userName.mention());
					Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + "For safety reasons, you do not have permission to remove a list or item.\n\n"
							+ "Contact <@!237888140633702400> for assistance if you screwed up! \n(Don't worry, it is expected. A lot of work remains to do to make this user friendly...)");
					return;
				}

				String arg2 = context.getArgs()[1].toLowerCase(); // item | list

				// Item or ID?
				if (arg2.equals("item") && argSize >= 3)
				{
					String arg3 = context.getArgs()[2];
					int id = Integer.parseInt(arg3);

					// Remove item with the name ID
					if (databaseHandler.removeItem(id))
					{
						Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + "Item was successfully deleted.");
					}
					else
					{
						Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + "Not a valid Item ID.");
					}
				}
				// ID
				else if (arg2.matches("^-?\\d+$"))
				{
					int id = Integer.parseInt(arg2);
					// Remove item with the name ID
					if (databaseHandler.removeList(id))
					{
						// Set all lists that had deleted list as deleted
						databaseHandler.removeListsWithParentId(id);
						databaseHandler.removeItemsWithListId(id);

						// OK
						Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + "List was successfully deleted.");
					}
					else
					{
						Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + "Not a valid List ID.");
						return;
					}
				}
			}
		}

		if (argSize == 1)
		{
			// Get first argument
			String arg1 = context.getArgs()[0].toLowerCase();

			if (arg1.equals("help"))
			{
				String helpText = "__Commands for retrieving data:__\n"
						+ "**!list** 				-> prints out the main lists stored\n"
						+ "**!list all** 			-> prints out all lists, even sub-lists.\n"
						+ "**!list [id] **			-> prints out any sublists belonging to list [id]\n"
						+ "**!list [id] items **	-> prints out a list of items belonging to list [id]\n"
						+ "**!list item [id]** 	-> prints out details of item [id]\n"
						+ "\n"
						+ "__Commands for adding data:__\n"
						+ "**!list add [name]**					-> add a new list\n"
						+ "**!list [id] add item [item data]**		-> add an item inside a List\n"
						+ "**!list [id] add list [name]**			-> add a list as a child of another list\n"
						+ "\n"
						+ "__Commands for removing data (ADMINS ONLY FOR SAFETY REASONS):__\n"
						+ "**!list remove item [id]**				-> Removes an Item from the list [id]\n"
						+ "**!list remove [id]**					-> Removes an entire list and all its contents";
				Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + helpText);
			}

			// ARGUMENT: ALL
			// If the command was all, display all lists regardless of 
			if (arg1.equals("all"))
			{
				ArrayList<ItemList> lists = databaseHandler.getAllLists();
				String output = "**[** *ID* **]**\t\t**List Name**\n\n";
				for (ItemList list : lists)
				{
					output += "[" + list.getId() + "]   " + list.getName() + "\n";
				}

				Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + output);

				return;
			}
			// ARGUMENT: ID
			else if (arg1.matches("^-?\\d+$"))
			{
				int id = Integer.parseInt(arg1);
				ArrayList<ItemList> lists = databaseHandler.getListsFromParentId(id);

				// If we got no list, then we failed finding matching ID
				if (lists.isEmpty())
				{
					Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + "No matching list found. Did you enter the correct ID?");
				}
				else
				{
					String output = "**[** *ID* **]**\t\t**List Name**\n\n";
					for (ItemList list : lists)
					{
						output += "[" + list.getId() + "]   " + list.getName() + "\n";
					}

					Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + output);
				}
				return;
			}
		}
		else if (argSize == 2)
		{
			String arg1 = context.getArgs()[0].toLowerCase();
			String arg2 = context.getArgs()[1].toLowerCase();

			// If first arg is an integer, it must be an ID
			if (arg1.matches("^-?\\d+$"))
			{
				int id = Integer.parseInt(arg1);

				// If second arg is items, display the items belonging to the list ID
				if (arg2.equals("items"))
				{
					ArrayList<Item> items = databaseHandler.getItemsFromListId(id);

					// If we got no list, then we failed finding matching ID
					if (items.isEmpty())
					{
						Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + "No matching list found. Did you enter the correct ID?");
					}
					else
					{
						String output = "**[** *ID* **]**\t\t**Item Name**\n\n";
						for (Item item : items)
						{
							output += "[" + item.getId() + "]   " + item.getName() + "\n";
						}

						Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + output);
					}
				}
			}
			// First argument is not an integer, must be a string
			else if (arg1.matches("item"))
			{
				// If second argument is an integer, we got an ID
				if (arg2.matches("^-?\\d+$"))
				{
					int id = Integer.parseInt(arg2);
					Item item = databaseHandler.getItem(id);
					Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + getItemInfoFromItem(item));
					return;
				}
			}
		}
		// [ID] add item|list <data>
		else if (argSize >= 4)
		{
			// Args
			String arg1 = context.getArgs()[0].toLowerCase(); // ID
			String arg2 = context.getArgs()[1].toLowerCase(); // add | remove
			String arg3 = context.getArgs()[2].toLowerCase(); // list | item

			// Args that will be removed
			String removeArgs = "!list " + arg1 + " " + arg2 + " " + arg3 + " ";

			// Pure text from input
			String dataText = context.getMessage().getContent().replaceAll(removeArgs, "");

			// ARG1 - Get ID
			if (arg1.matches("^-?\\d+$"))
			{
				int id = Integer.parseInt(arg1);

				// Check that the List ID exists
				if (databaseHandler.getList(id) == null)
				{
					// Do nothing, list ID does not exist.
					Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + "List ID does not exist.");
					return;
				}

				if (arg2.equals("add"))
				{
					// Add item, with dataText being the Item Data
					if (arg3.equals("item"))
					{
						// Convert the pure text into an Item
						Item item = getItemFromText(dataText);

						// Add List ID from arg to the item
						int listId = Integer.parseInt(arg1);
						item.setListId(listId);

						item = databaseHandler.insertItem(item);

						if (item != null)
						{
							String itemInfo = getItemInfoFromItem(item);
							Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + itemInfo);
							return;
						}
					}
					// Add List, with dataText being the List Name
					else if (arg3.equals("list"))
					{

						// Add List ID from arg to the item
						int listId = Integer.parseInt(arg1);

						// Create List
						ItemList list = new ItemList(dataText);

						// Insert parent ID into the list
						list.setParentListId(listId);

						// Insert list to database
						list = databaseHandler.insertList(list);

						if (list != null)
						{
							ItemList parent = databaseHandler.getList(list.getParentListId());
							Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + "Successfully added list " + list.getName() + " (" + list.getId() + ") into " + parent.getName() + " (" + parent.getId() + ").");
							return;
						}
					}
				}
				else if (arg2.equals("remove"))
				{
					// TODO
					return;
				}
			}
			else
			{
				Client.sendMessage(context.getMessage().getChannel(), userName + ":\n" + ""
						+ "Not a valid input.\n"
						+ "**Examples of use:**\n\n"
						+ "!list [ID] add item <item data here>\n"
						+ "!list [ID] add list <list name here>\n");
			}
		}
	}

	/**
	 * Returns true if the input ID matches any listed inside ADMINS array
	 *
	 * @param inputStr
	 * @return
	 */
	public static boolean hasAdmin(String inputStr)
	{
		return Arrays.stream(ADMINS).parallel().anyMatch(inputStr::contains);
	}

	/* 
	Used to register table as a command
	 */
	public static Command register()
	{
		// Setup database
		SQLiteDatabase database = new SQLiteDatabase("MyList");
		databaseHandler = new DatabaseHandler(database);

		// Command: help
		Command command = new Command("list");
		command.withAliases("l");
		command.onExecuted((context) ->
		{
			List.Execute(context);
		});

		return command;
	}

	private static Item getItemFromText(String dataText)
	{
		// Get text
		String text = dataText;

		// Get pasted string in array (separated by newline)
		String[] pastedString = text.split("\\r?\\n");

		// Properties belonging to the Artifact
		ArrayList<Property> properties = new ArrayList<>();

		// Artifact - First line will always be the title
		Item newItem = new Item(pastedString[0]);

		// Iterate each line (except the first)
		for (int i = 1; i < pastedString.length; i++)
		{
			// Check if we have a : that indicates a "type".
			if (pastedString[i].contains(":"))
			{
				// Set Property name
				String[] splitText = pastedString[i].split(":");
				properties.add(new Property(splitText[0]));

				// Set property description
				String description = pastedString[i].replace(splitText[0] + ":", "").replaceFirst("^\\s*", "");
				properties.get(properties.size() - 1).setDescription(description);
			}
			else
			{
				// Does not contain : means we are appending the description to the last one
				// Get last description string
				if (!properties.isEmpty())
				{
					String description = properties.get(properties.size() - 1).getDescription();

					// Append to description string
					description += "\n" + pastedString[i];

					// Set new description
					properties.get(properties.size() - 1).setDescription(description);
				}

			}
		}

		// Set properties
		newItem.setProperties(properties);

		// Returns the Item
		return newItem;
	}

	private static String getItemInfoFromItem(Item item)
	{
		// Build string
		String text = "__**" + item.getName() + "**__\n\n";

		// Get each description
		for (Property desc : item.getProperties())
		{
			text += "**" + desc.getName() + "**:\n" + desc.getDescription() + "\n\n";
		}

		return text;
	}
}
