package rpbot.command;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandContext;
import java.util.Random;
import rpbot.client.Client;
import sx.blah.discord.handle.obj.IUser;

/**
 * Roll - For rolling dice (e.g. /roll 2d6 will generate two dice with 6-sides)
 *
 * @author ivanskodje
 */
public class Roll
{

	// Limitations
	private static final int MAX_ROLLS = 20;
	private static final int MAX_DICE_SIDES = 1000;

	/**
	 * Executes the command
	 *
	 * @param context
	 */
	public static void Execute(CommandContext context)
	{
		// If we have no arguments, send a warning message and return
		if (context.getArgs().length < 1)
		{
			invalidExpression(context);
			return;
		}

		// Username (@<name>#id)
		IUser userName = context.getMessage().getAuthor();

		// Get our dice argument
		String diceRoll = context.getArgs()[0].toLowerCase(); // Get dice roll

		// Split text into dice count and dice side numbers
		String[] diceCountAndSides = diceRoll.split("d");

		// Error check: Make sure we have two numbers, a number before and after the 'd' (eg. 2d7 -> {2, 7})
		if (diceCountAndSides.length < 2)
		{
			invalidExpression(context);
			return;
		}

		try
		{
			// Parse dice count and sides into integer
			int diceCount = Integer.parseInt(diceCountAndSides[0]);
			int diceSides = Integer.parseInt(diceCountAndSides[1]);

			// Setup empty dice string and start value
			String diceString = "";
			int totalValue = 0;

			// Error handling : Make sure we dont try to roll an excessive amount of dice
			if (diceCount > MAX_ROLLS)
			{
				Client.sendMessage(context.getMessage().getChannel(), userName + ":\nYou cannot roll more than " + MAX_ROLLS + " dices at once!");
				return;
			}
			else if (diceSides > MAX_DICE_SIDES)
			{
				Client.sendMessage(context.getMessage().getChannel(), userName + ":\nYou cannot roll dice over " + MAX_DICE_SIDES + " sides!");
				return;
			}

			// Roll Dice
			for (int i = 0; i < diceCount; i++)
			{
				// Roll dice
				Random random = new Random();

				boolean idiotic_roll_safeguard = true;

				int dice = random.nextInt(diceSides) + 1; // Adding +1 since we dont want 0 to be an option

				// Build string
				if (i == 0)
				{
					diceString += dice;
				}
				else
				{
					diceString += ", " + dice;
				}

				// Add dice value to total
				totalValue += dice;
			}

			// Send result
			Client.sendMessage(context.getMessage().getChannel(), "[v2.0] " + userName + ":\n(" + diceString + ") Total value: " + totalValue);
		}
		catch (NumberFormatException ex)
		{
			invalidExpression(context);
		}
	}

	/*
	Sends a default message explaining the expression for rolling dice
	 */
	private static void invalidExpression(CommandContext context)
	{
		Client.sendMessage(context.getMessage().getChannel(), context.getMessage().getAuthor() + ":\nDice expressions contain the standard representations of dice in text form (e.g. 2d6 is two 6-sided dice).\nTry typing in '!roll 2d6'.");
	}

	/* 
	Used to register table as a command
	 */
	public static Command register()
	{
		// Command: roll
		Command roll = new Command("roll");
		roll.withAliases("dice", "rolling", "r");
		roll.onExecuted((context) ->
		{
			Roll.Execute(context);
		});

		return roll;
	}
}
