package rpbot.command;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import rpbot.Main;
import rpbot.client.Client;
import rpbot.derbydatabase.DatabaseHandler;
import rpbot.sheetdata.Item;
import rpbot.sheetdata.SheetData;
import sx.blah.discord.handle.obj.IUser;

/**
 * For storing and retrieving user owned character sheets
 *
 * @author Ivan Skodje
 */
public class Sheet
{

    ArrayList<SheetData> playerSheets = new ArrayList<>();

    /* Commands 
    - create : Creates a sheet for the user
    - edit <property> <new_value>
    - <property> : Returns property value(s) - Outputs the old value and new value
    
     */
    /**
     * Executes the command
     *
     * @param context
     */
    public static void Execute(CommandContext context)
    {
        // Get our argument
        String[] args = context.getArgs();

        // If we have no arguments, send a warning message and return
        if (args.length < 1)
        {
            invalidExpression(context);
            return;
        }

        if(args[0].equals("get"))
        {
            String keyValue = args[1];
            Item myItem = DatabaseHandler.getItem(context.getMessage().getAuthor(), keyValue);
            
            if(myItem == null)
            {
                Client.sendMessage(context.getMessage().getChannel(), context.getMessage().getAuthor() + ":\n" + "Does not exist!");
                return;
            }
            
            // Output the item
            if(myItem.getValue() == null)
            {
                Client.sendMessage(context.getMessage().getChannel(), context.getMessage().getAuthor() + ":\n" + myItem.getKey());
            }
            else
            {
                Client.sendMessage(context.getMessage().getChannel(), context.getMessage().getAuthor() + ":\n" + myItem.getKey() + ": " + myItem.getValue());
            }
        }
        
        if(args[0].equals("set"))
        {
            Item newItem = new Item();
            for (int i = 1; i < args.length; i++)
            {
                // Store first value as property
                if (i == 1)
                {
                    // Set ID
                    newItem.setUserId(context.getMessage().getAuthor().getID());

                    // Add Key Value
                    newItem.setKey(args[i]);
                }
                else if (i == 2)
                {
                    // Add Value
                    newItem.setValue(args[i]);
                }
            }

            if(newItem.getUserId() != null && newItem.getKey() != null)
            {
                System.out.println("Adding to Database: " + newItem.toJson());
                DatabaseHandler.addItem(newItem);
                Client.sendMessage(context.getMessage().getChannel(), context.getMessage().getAuthor() + ":\n" + newItem.getKey() + " successfully added.");
            }
        }
        
        if(args[0].equals("delete"))
        {
            String keyValue = args[1];
            if(DatabaseHandler.deleteItem(context.getMessage().getAuthor(), keyValue))
            {
                Client.sendMessage(context.getMessage().getChannel(), context.getMessage().getAuthor() + ":\nItem deleted!");
            }
            else
            {
                Client.sendMessage(context.getMessage().getChannel(), context.getMessage().getAuthor() + ":\nItems that do not exist cannot be deleted.");
            }
        }
        
        
        if(args[0].equals("list"))
        {
            String text = context.getMessage().getAuthor() + "\n\n";
            for(Item item : DatabaseHandler.getItems(context.getMessage().getAuthor()))
            {
                // Output the item
                if(item.getValue() == null)
                {
                    text += item.getKey() + "\n";
                }
                else
                {
                    text += item.getKey() + ": " + item.getValue() + "\n";
                }
            }
            
            Client.sendMessage(context.getMessage().getChannel(), text);
        }
    }


    /* 
    Used to register table as a command
     */
    public static Command register()
    {
        // Command: name
        Command sheet = new Command("sheet");
        sheet.onExecuted((context) ->
        {
            Sheet.Execute(context);
        });

        return sheet;
    }

    /*
    Sends a default message explaining the expression for rolling dice
     */
    private static void invalidExpression(CommandContext context)
    {
        Client.sendMessage(context.getMessage().getChannel(), context.getMessage().getAuthor() + ":\nInvalid Sheet command(s)");
    }
}
