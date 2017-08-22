package rpbot.sheetdata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author Ivan Skodje
 */
public class Item
{
    String userId;
    String key;
    String value;
    
    
    public Item()
    {
        
    }
    
    public Item(String userId, String name, String value)
    {
        this.userId = userId;
        this.key = name;
        this.value = value;
    }

    public Item(String jsonString)
    {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        
        // Populate data
        // Category ID
        JsonElement categoryIdElement = jsonObject.get("userId");
        if(!categoryIdElement.isJsonNull())
        {
            this.userId = categoryIdElement.getAsString();
        }
        
        // Name
        JsonElement nameElement = jsonObject.get("name");
        if(!nameElement.isJsonNull())
        {
            this.key = nameElement.getAsString();
        }
        
        // Value
        JsonElement valueElement = jsonObject.get("value");
        if(!valueElement.isJsonNull())
        {
            this.value = valueElement.getAsString();
        }
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
    
    
    
    
    
    public String toJson()
    {
        try
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("categoryId", userId);
            jsonObject.addProperty("name", key);
            jsonObject.addProperty("value", value);
            return jsonObject.toString();
        }
        catch(Exception ex)
        {
            return "EXCEPTION: " + ex.getMessage();
        }
    }
}
