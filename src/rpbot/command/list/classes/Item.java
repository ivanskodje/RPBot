/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.command.list.classes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Ivan Skodje
 */
public class Item
{

	private int id;
	private int listId;
	private String name;
	private ArrayList<Property> properties;

	public Item(String name)
	{
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getListId()
	{
		return listId;
	}

	public void setListId(int listId)
	{
		this.listId = listId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ArrayList<Property> getProperties()
	{
		return properties;
	}

	public void setProperties(ArrayList<Property> properties)
	{
		this.properties = properties;
	}

	public String getJsonContent()
	{
		Gson gson = new Gson();
		return gson.toJson(properties);
	}

	public void setJsonContent(String jsonContent)
	{
		Gson gson = new Gson();
		Type collectionType = new TypeToken<Collection<Property>>()
		{
		}.getType();
		properties = gson.fromJson(jsonContent, collectionType);
	}

}
