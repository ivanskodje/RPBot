/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.command.list.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import rpbot.command.list.classes.Item;
import rpbot.command.list.classes.ItemList;

/**
 *
 * @author Ivan Skodje
 */
public class DatabaseHandler
{

	// Database
	private Database database = null;

	// Setup a Gson 
	private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	// ItemList folder path
	private final static String PATH = "data/lists/";

	/**
	 * Data Handler
	 *
	 * @param db
	 */
	public DatabaseHandler(Database db)
	{
		// Setup connection
		database = db;
		database.connect();
	}

	public ItemList insertList(ItemList list)
	{
		return database.insertList(list);
	}

	public Item insertItem(Item item)
	{
		return database.insertItem(item);
	}

	public ArrayList<ItemList> getAllLists()
	{
		ArrayList<ItemList> lists = new ArrayList<>();

		// Select all lists regardless of parenthood
		String QUERY = "SELECT * FROM LIST";
		ResultSet resultSet = database.executeQuery(QUERY);

		try
		{
			// Iterate through all clients
			while (resultSet.next())
			{
				// Id
				int columnId = resultSet.getInt("id");

				// Parent ItemList ID (if any)
				int parentListId = resultSet.getInt("parent_list_id");

				// Name
				String columnName = resultSet.getString("name");

				// Create object
				ItemList list = new ItemList(columnName); // Name
				list.setId(columnId); // ID
				list.setParentListId(parentListId); // Parent ItemList ID

				// Add to ArrayList
				lists.add(list);

				// Sort clients alphabetically
				Collections.sort(lists, (o1, o2) -> o1.getName().compareTo(o2.getName()));
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}

		return lists;
	}

	/**
	 * Loads lists the root lists that does not have any parent lists
	 *
	 * @return
	 */
	public ArrayList<ItemList> getLists()
	{
		ArrayList<ItemList> lists = new ArrayList<>();

		// Select all lists that does not have any parent lists
		String QUERY = "SELECT * FROM LIST WHERE parent_list_id is null";
		ResultSet resultSet = database.executeQuery(QUERY);

		try
		{
			// Iterate through all clients
			while (resultSet.next())
			{
				// Id
				int columnId = resultSet.getInt("id");

				// Parent ItemList ID (if any)
				// int parentListId = resultSet.getInt("parent_list_id");
				// Name
				String columnName = resultSet.getString("name");

				// Create object
				ItemList list = new ItemList(columnName);
				list.setId(columnId);
				// list.setParentListId(parentListId); // Parent ItemList ID

				// Add to ArrayList
				lists.add(list);

				// Sort clients alphabetically
				Collections.sort(lists, (o1, o2) -> o1.getName().compareTo(o2.getName()));
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}

		return lists;
	}

	/**
	 * Returns a matching ItemList, or a null if none exist
	 *
	 * @param id
	 * @return
	 */
	public ItemList getList(int id)
	{
		// Select all lists that does not have any parent lists
		String QUERY = "SELECT * FROM LIST WHERE id=" + id;
		ResultSet resultSet = database.executeQuery(QUERY);

		try
		{
			// Iterate through all clients
			while (resultSet.next())
			{
				// Id
				int columnId = resultSet.getInt("id");

				// Parent ItemList ID (if any)
				int parentListId = resultSet.getInt("parent_list_id");

				// Name
				String columnName = resultSet.getString("name");

				// Create object
				ItemList list = new ItemList(columnName);
				list.setId(columnId);
				list.setParentListId(parentListId); // Parent ItemList ID

				return list;
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	public ArrayList<ItemList> getListsFromParentId(int id)
	{
		ArrayList<ItemList> lists = new ArrayList<>();

		// Select all lists that does not have any parent lists
		String QUERY = "SELECT * FROM LIST WHERE parent_list_id=" + id;
		ResultSet resultSet = database.executeQuery(QUERY);

		try
		{
			// Iterate through all clients
			while (resultSet.next())
			{
				// Id
				int columnId = resultSet.getInt("id");

				// Parent ItemList ID (if any)
				int parentListId = resultSet.getInt("parent_list_id");

				// Name
				String columnName = resultSet.getString("name");

				// Create object
				ItemList list = new ItemList(columnName);
				list.setId(columnId);
				list.setParentListId(parentListId); // Parent ItemList ID

				// Add to array
				lists.add(list);

				// Sort clients alphabetically
				Collections.sort(lists, (o1, o2) -> o1.getName().compareTo(o2.getName()));

			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}

		return lists;
	}

	public ArrayList<Item> getItemsFromListId(int id)
	{
		ArrayList<Item> items = new ArrayList<>();

		// Select all lists that does not have any parent lists
		String QUERY = "SELECT * FROM ITEM WHERE list_id=" + id;
		ResultSet resultSet = database.executeQuery(QUERY);

		try
		{
			// Iterate through all clients
			while (resultSet.next())
			{
				// Id
				int columnId = resultSet.getInt("id");

				// Name
				String columnName = resultSet.getString("name");

				// Create object
				Item item = new Item(columnName);
				item.setId(columnId);

				// Add to array
				items.add(item);

				// Sort clients alphabetically
				Collections.sort(items, (o1, o2) -> o1.getName().compareTo(o2.getName()));
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}

		return items;
	}

	public Item getItem(int id)
	{
		// Select all lists that does not have any parent lists
		String QUERY = "SELECT * FROM ITEM WHERE id=" + id;
		ResultSet resultSet = database.executeQuery(QUERY);

		try
		{
			// Iterate through all clients
			while (resultSet.next())
			{
				// Id
				int columnId = resultSet.getInt("id");

				// List ID
				int columnListId = resultSet.getInt("list_id");

				// Name
				String columnName = resultSet.getString("name");

				// Json content
				String columnJsonContent = resultSet.getString("json_content");

				// Create object
				Item item = new Item(columnName);
				item.setId(columnId);
				item.setListId(columnListId);
				item.setJsonContent(columnJsonContent);
				return item;
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}

		// If this runs, we failed to get item
		return null;
	}

	public boolean removeItem(int id)
	{
		return database.removeItem(id);
	}

	public boolean removeList(int id)
	{
		return database.removeList(id);
	}

	public boolean removeListsWithParentId(int id)
	{
		return database.removeListsWithParentId(id);
	}

	public boolean removeItemsWithListId(int id)
	{
		return database.removeItemsWithListId(id);
	}
}
