/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.sheetdata;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;

/**
 *
 * @author Ivan Skodje
 */
public class SheetData
{
    /* Unique User ID */
    private int id;
    
    /* User data stored as json */
    private ArrayList<Item> items;
    
    
    /**
     * Constructor
     * @param userId 
     */
    public SheetData(int userId)
    {
        this.id = userId;
    }
    
    /**
     * Add new item
     * @param newItem 
     */
    public void addItem(Item newItem)
    {
        items.add(newItem);
    }
    
    /**
     * Returns an array of items
     * @return 
     */
    public ArrayList<Item> getItems()
    {
        return items;
    }
    
    public String toJson()
    {
        try
        {
            return "";
        }
        catch(Exception ex)
        {
            return "EXCEPTION: " + ex.getMessage();
        }
    }
}
