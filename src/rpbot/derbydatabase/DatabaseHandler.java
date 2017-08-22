/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.derbydatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import rpbot.sheetdata.Item;
import sx.blah.discord.handle.obj.IUser;

/**
 *
 * @author Ivan Skodje
 */
public class DatabaseHandler
{

    // Database
    private static Database database;

    // All Items
    private static ArrayList<Item> items = new ArrayList<>();

    /**
     * Initiate and connects with given database
     *
     * @param db
     */
    public static void init(Database db)
    {
        // Setup connection
        database = db;
        database.connect();

        // Populate data
        populateData();
    }

    /* Loads and populates all clients + projects + tasks */
    private static void populateData()
    {
        loadItems();
    }

    /**
     * Loads clients to Memory in DatabaseHandler
     */
    private static void loadItems()
    {
        System.out.println("POPULATED ITEM?");
        String QUERY = "SELECT * FROM ITEMS";
        ResultSet resultSet = database.executeQuery(QUERY);

        try
        {
            // Iterate through all clients
            while (resultSet.next())
            {
                Item newItem = new Item();
                newItem.setUserId(resultSet.getString("userId"));
                newItem.setKey(resultSet.getString("keyValue"));
                newItem.setValue(resultSet.getString("value"));
                items.add(newItem);
                
                
                
                Collections.sort(items, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public static void addItem(Item item)
    {
        // Before adding, check if the USER already have an item with that name
        for(Item i : items)
        {
            if(i.getUserId().equals(item.getUserId()))
            {
                if(i.getKey().equals(item.getKey()))
                {
                    System.out.println("item already exist!");
                    if(database.updateItem(item))
                    {
                        items.remove(i);
                        items.add(item);
                    }
                    return; 
                }
            }
        }
        
        
        System.out.println("ADDING ITEM!");
        if(database.insertItem(item))
        {
            items.add(item);
        }
    }
    
    
    public static Item getItem(IUser user, String keyValue)
    {
        System.out.println("There are a total of " + items.size() + " items");
        
        for(Item item : items)
        {
            System.out.println("You: " + user.getID() + "\nChecking ID: " + item.getUserId());
            if(item.getUserId().equals(user.getID()))
            {
                if(item.getKey().equals(keyValue))
                {
                    return item;
                }
            }
            
        }
        return null;
    }
    
    
    public static ArrayList<Item> getItems(IUser user)
    {
        ArrayList<Item> temp = new ArrayList<>();
        for(Item item : items)
        {
            if(item.getUserId().equals(user.getID()))
            {
                temp.add(item);
            }
        }
        
        return temp;
    }
    
    
    public static boolean deleteItem(IUser user, String keyValue)
    {
        for(Item item : items)
        {
            if(item.getUserId().equals(user.getID()))
            {
                if(item.getKey().equals(keyValue))
                {
                    if(database.deleteItem(item))
                    {
                        items.remove(item);
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
}
