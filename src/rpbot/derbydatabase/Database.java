package rpbot.derbydatabase;

import java.sql.ResultSet;
import rpbot.sheetdata.Item;
import rpbot.sheetdata.SheetData;
import sx.blah.discord.handle.obj.IUser;

/**
 *
 * @author ivanskodje
 */
public interface Database
{

	/* Establishes connection with server */
	public void connect();

	/* Executes a query, returns the resultset */
	public ResultSet executeQuery(String query);

	/* Executes a query action, returns a boolean to determine success or failure */
	public boolean executeAction(String query);

	/* Insert an user to the database */
	// public void insertCategory(int userId, String category);
	/**
	 * Insert an item into the database
	 */
	public boolean insertItem(Item item);

	/**
	 * Updates an existing item (key) in the database
	 */
	public boolean updateItem(Item item);

	/**
	 * Deletes an item from the database *
	 */
	public boolean deleteItem(Item item);

	/* Sets the need for user authorization upon connecting to the database */
	public void setAuthorization(Boolean enable);
}
