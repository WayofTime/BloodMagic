package wayoftime.bloodmagic.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;

public class CommandBloodMagic
{
	public CommandBloodMagic(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal("bloodmagic"));
	}
}