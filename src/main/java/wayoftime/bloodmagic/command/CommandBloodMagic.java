package wayoftime.bloodmagic.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import wayoftime.bloodmagic.command.sub.SoulNetworkCommand;

public class CommandBloodMagic
{
	public static void register(RegisterCommandsEvent event) {
		event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("bloodmagic")
                        .then(SoulNetworkCommand.COMMAND)
        );
	}
}