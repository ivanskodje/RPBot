package rpbot.command;

import com.darichey.discord.api.CommandContext;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import rpbot.client.Client;
import sx.blah.discord.handle.obj.IUser;

/**
 * Calc - For calculating mathematical expressions
 * @author ivanskodje
 */
public class Calc
{	
	public static void Execute(CommandContext context)
	{
		// Username (@<name>#id)
		IUser userName = context.getMessage().getAuthor();
		
		// If we have no arguments, send a warning message and return
		if(context.getArgs().length < 1)
		{
			Client.sendMessage(context.getMessage().getChannel(), userName + ": Missing a mathematical expression\nTry /calc 1+1");
			return;
		}
		
		// If the argument is "help", display help text
		if(context.getArgs()[0].equals("help"))
		{
			Client.sendMessage(context.getMessage().getChannel(), userName + ":\n!calc *<mathematical expression>*");
			return;
		}
			
		// Put together a string with remaining arguments (math)
		String mathString = Stream.of(context.getArgs()).collect(Collectors.joining());
		
		try
		{
			// Setup ScriptEngine for calculating more complex mathematical expressions
			ScriptEngineManager mgr = new javax.script.ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			
			// Send message
			Client.sendMessage(context.getMessage().getChannel(), userName + ": " + mathString + " = " + engine.eval(mathString));
			
		}
		catch(ScriptException ex)
		{
			Client.sendMessage(context.getMessage().getChannel(), userName + ": '" + mathString + "' is not a valid mathematical expression");
			ex.printStackTrace();
		}
	}
}
