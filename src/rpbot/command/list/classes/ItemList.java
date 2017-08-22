/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpbot.command.list.classes;

/**
 *
 * @author Ivan Skodje
 */
public class ItemList
{

	private int id;
	private String name;
	private int parentListId;

	public ItemList(String name)
	{
		this.name = name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getParentListId()
	{
		return parentListId;
	}

	public void setParentListId(int parentListId)
	{
		this.parentListId = parentListId;
	}

}
