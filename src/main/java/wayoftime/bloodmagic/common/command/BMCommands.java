package wayoftime.bloodmagic.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class BMCommands {
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext buildContext = event.getBuildContext();

        dispatcher.register(
                Commands.literal("bloodmagic")
                        .then(LivingUpgradesCommand.command(buildContext))
                        .then(SoulNetworkCommand.COMMAND)
        );
    }
}
