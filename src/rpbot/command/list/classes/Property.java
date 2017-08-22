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
public class Property
{

	// Property name
	private String name;

	// Property description
	private String description;

	/**
	 * An Item may contain several Properties, a Property belongs only to one
	 * List
	 *
	 * @param name
	 */
	public Property(String name)
	{
		this(name, "");
	}

	public Property(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	/**
	 * Set property name
	 *
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Return property name
	 *
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set property description
	 *
	 * @param description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Return property description
	 *
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}

}
