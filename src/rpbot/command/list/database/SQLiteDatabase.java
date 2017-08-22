/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.command.list.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpbot.command.list.classes.Item;
import rpbot.command.list.classes.ItemList;

/**
 *
 * @author Ivan Skodje
 */
public class SQLiteDatabase implements Database
{

	private static String databaseURL;
	private static Connection connection = null;
	private static Statement statement = null;
	private String databaseName = "UndefinedDatabase";
	private static PreparedStatement preparedStatement = null;

	/*
	In order to work a SQLite database, we nmeed to load a database name (or create it)
	 */
	public SQLiteDatabase(String databaseName)
	{
		// Put database url together
		databaseURL = "jdbc:sqlite:" + databaseName + ".db";

		// Get database name
		this.databaseName = databaseName;
	}

	/**
	 * Connect to database
	 */
	@Override
	public void connect()
	{
		try
		{
			// Setup driver and establish connection
			Class.forName("org.sqlite.JDBC");
		}
		catch (ClassNotFoundException ex)
		{
			Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
		}
		try
		{
			connection = DriverManager.getConnection(databaseURL);
		}
		catch (SQLException ex)
		{
			Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
		}

		// Setup tables (if they haven't been)
		setupTables();
	}

	/**
	 * Executes a query, returns resultset
	 *
	 * @param query
	 * @return
	 */
	@Override
	public ResultSet executeQuery(String query)
	{
		ResultSet resultSet = null;

		try
		{
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		}
		catch (SQLException ex)
		{
			System.err.println(ex.getMessage());
		}

		return resultSet;
	}

	/**
	 * Executes an action, returns a boolean
	 *
	 * @param query
	 * @return
	 */
	@Override
	public boolean executeAction(String query)
	{
		try
		{
			statement = connection.createStatement();
			statement.execute(query);
			return true;
		}
		catch (SQLException ex)
		{
			System.err.println(ex.getMessage());
			return false;
		}
	}

	@Override
	public ItemList insertList(ItemList list)
	{
		try
		{
			String query;

			// If the parent list id is not set (0), only insert the name
			// This implies that this list is a root list and will appear on top without any parent.
			if (list.getParentListId() == 0)
			{
				query = "INSERT INTO LIST (name) values (?)";
				preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, list.getName());
				preparedStatement.executeUpdate();
			}
			else
			{
				query = "INSERT INTO LIST (parent_list_id, name) values (?, ?)";
				preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
				preparedStatement.setInt(1, list.getParentListId());
				preparedStatement.setString(2, list.getName());
				preparedStatement.executeUpdate();
			}

			// Get ID from inserted client
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next())
			{
				// Id
				int id = rs.getInt(1);

				// Add client id to object
				list.setId(id);

				// Return object
				return list;
			}
		}
		catch (SQLException ex)
		{
			String sqlState = ex.getSQLState();

			System.err.println(ex.getMessage());

			// Duplicate Name
			// Error Docu: https://db.apache.org/derby/docs/10.1/ref/rrefexcept71493.html
			if (sqlState.equals("23505"))
			{
				System.err.println("List name already exist.\nPlease choose another." + ex.getMessage());
			}
			else
			{
				System.err.println("Unable to execute query.\n" + ex.getMessage());
			}
		}

		return null;
	}

	@Override
	public Item insertItem(Item item)
	{
		try
		{
			String query = "INSERT INTO ITEM (list_id, name, json_content) values (?, ?, ?)";
			preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, item.getListId());
			preparedStatement.setString(2, item.getName());
			preparedStatement.setString(3, item.getJsonContent());
			preparedStatement.executeUpdate();

			// Get ID from inserted client
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next())
			{
				// Id
				int id = rs.getInt(1);

				// Add client id to object
				item.setId(id);

				// Return object
				return item;
			}
		}
		catch (SQLException ex)
		{
			String sqlState = ex.getSQLState();

			// Duplicate Name
			// Error Docu: https://db.apache.org/derby/docs/10.1/ref/rrefexcept71493.html
			if (sqlState.equals("23505"))
			{
				System.err.println("Item name already exist.\nPlease choose another." + ex.getMessage());
			}
			else
			{
				System.err.println("Unable to execute query.\n" + ex.getMessage());
			}
		}

		return null;
	}

	/**
	 * Setup Database Tables for ItemList
	 */
	private void setupTables()
	{
		setupListTable();
		setupItemTable();
	}


	/*
	Setup the ItemList Table
	 */
	private void setupListTable()
	{
		String tableName = "LIST";
		String query = "CREATE TABLE " + tableName + "("
				+ "id INTEGER NOT NULL,\n"
				+ "parent_list_id INTEGER,\n"
				+ "name VARCHAR(200) NOT NULL UNIQUE,\n"
				+ "dateCreated DATE default CURRENT_DATE,\n"
				+ "PRIMARY KEY(id), \n"
				+ "FOREIGN KEY(parent_list_id) REFERENCES list(id)"
				+ ")";

		// Create table if it does not already exist
		createTable(tableName, query);
	}

	/*
	Setup the Item Table
	 */
	private void setupItemTable()
	{
		String tableName = "ITEM";
		String query = "CREATE TABLE " + tableName + "("
				+ "id INTEGER NOT NULL,\n"
				+ "list_id INTEGER,\n"
				+ "name VARCHAR(200) NOT NULL,\n"
				+ "json_content VARCHAR(4096),\n"
				+ "dateCreated DATE default CURRENT_DATE,\n"
				+ "PRIMARY KEY(id),\n"
				+ "FOREIGN KEY(list_id) REFERENCES list(id)"
				+ ")";

		// Create table if it does not already exist
		createTable(tableName, query);
	}

	/**
	 * Creates a table given a table name and full query
	 *
	 * @param tableName
	 * @param query
	 * @return
	 */
	public boolean createTable(String tableName, String query)
	{
		try
		{
			statement = connection.createStatement();
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet tables = databaseMetaData.getTables(null, null, tableName.toUpperCase(), null);

			// If table does NOT already, create the table
			if (!tables.next())
			{
				statement.execute(query);
				return true;
			}
		}
		catch (SQLException ex)
		{
			System.err.println("Unable to create table.\n" + ex.getMessage());
		}
		return false;
	}

	@Override
	public boolean removeItem(int id)
	{
		try
		{
			String query = "DELETE FROM ITEM WHERE id=" + id;
			preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			if (preparedStatement.executeUpdate() > 0)
			{
				return true;
			}
		}
		catch (SQLException ex)
		{
			String sqlState = ex.getSQLState();

			// Duplicate Name
			// Error Docu: https://db.apache.org/derby/docs/10.1/ref/rrefexcept71493.html
			if (sqlState.equals("23505"))
			{
				System.err.println("Item name already exist.\nPlease choose another." + ex.getMessage());
			}
			else
			{
				System.err.println("Unable to execute query.\n" + ex.getMessage());
			}

			return false;
		}
		return false;
	}

	@Override
	public boolean removeList(int id)
	{
		try
		{
			String query = "DELETE FROM LIST WHERE id=" + id;
			preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			if (preparedStatement.executeUpdate() > 0)
			{
				return true;
			}
		}
		catch (SQLException ex)
		{
			String sqlState = ex.getSQLState();

			// Duplicate Name
			// Error Docu: https://db.apache.org/derby/docs/10.1/ref/rrefexcept71493.html
			if (sqlState.equals("23505"))
			{
				System.err.println("Item name already exist.\nPlease choose another." + ex.getMessage());
			}
			else
			{
				System.err.println("Unable to execute query.\n" + ex.getMessage());
			}

			return false;
		}
		return false;
	}

	@Override
	public boolean removeListsWithParentId(int id)
	{
		try
		{
			String query = "DELETE FROM LIST WHERE parent_list_id=" + id;
			preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			if (preparedStatement.executeUpdate() > 0)
			{
				return true;
			}
		}
		catch (SQLException ex)
		{
			String sqlState = ex.getSQLState();

			// Duplicate Name
			// Error Docu: https://db.apache.org/derby/docs/10.1/ref/rrefexcept71493.html
			if (sqlState.equals("23505"))
			{
				System.err.println("Item name already exist.\nPlease choose another." + ex.getMessage());
			}
			else
			{
				System.err.println("Unable to execute query.\n" + ex.getMessage());
			}

			return false;
		}
		return false;
	}

	@Override
	public boolean removeItemsWithListId(int id)
	{
		try
		{
			String query = "DELETE FROM ITEM WHERE list_id=" + id;
			preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			if (preparedStatement.executeUpdate() > 0)
			{
				return true;
			}
		}
		catch (SQLException ex)
		{
			String sqlState = ex.getSQLState();

			// Duplicate Name
			// Error Docu: https://db.apache.org/derby/docs/10.1/ref/rrefexcept71493.html
			if (sqlState.equals("23505"))
			{
				System.err.println("Item name already exist.\nPlease choose another." + ex.getMessage());
			}
			else
			{
				System.err.println("Unable to execute query.\n" + ex.getMessage());
			}

			return false;
		}
		return false;
	}
}
