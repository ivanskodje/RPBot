/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.command.list.database;

import java.sql.ResultSet;
import rpbot.command.list.classes.ItemList;
import rpbot.command.list.classes.Item;

/**
 *
 * @author Ivan Skodje
 */
public interface Database
{

	/* Establishes connection with server */
	public void connect();

	/* Executes a query, returns the resultset */
	public ResultSet executeQuery(String query);

	/* Executes a query action, returns a boolean to determine success or failure */
	public boolean executeAction(String query);

	/* Inserts a ItemList into database, and returns the same ItemList with the associated database ID */
	public ItemList insertList(ItemList list);

	/* Inserts a ItemList into database, and returns the same ItemList with the associated database ID */
	public Item insertItem(Item list);

	/* Removes a List from database */
	public boolean removeList(int id);

	/* Removes an Item from database */
	public boolean removeItem(int id);

	/* Remove all lists with matching parent id */
	public boolean removeListsWithParentId(int id);

	/* Remove all items with matching list id */
	public boolean removeItemsWithListId(int id);

}
