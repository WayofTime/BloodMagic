package wayoftime.bloodmagic.command.sub;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class SoulNetworkCommand {
    public static final ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> COMMAND = Commands.literal("network")
            .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
            .then(
                    Commands.argument("target", EntityArgument.player())
                            .then(
                                    Commands.literal("query")
                                            .executes(context -> showNetwork(context, EntityArgument.getPlayer(context, "target")))
                            )
                            .then(
                                    Commands.literal("reset")
                                            .executes(context -> setNetwork(context, EntityArgument.getPlayer(context, "target"), 0))
                            )
                            .then(
                                    Commands.literal("set")
                                            .then(
                                                    Commands.argument("amount", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                                            .executes(context -> setNetwork(context, EntityArgument.getPlayer(context, "target"), IntegerArgumentType.getInteger(context, "amount")))
                                            )
                            )
                            .then(
                                    Commands.literal("add")
                                            .then(
                                                    Commands.argument("amount", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                                            .executes(context -> addNetwork(context, EntityArgument.getPlayer(context, "target"), IntegerArgumentType.getInteger(context, "amount")))
                                            )
                            )
            );

    private static int setNetwork(CommandContext<CommandSourceStack> context, ServerPlayer target, int amount) {
        SoulNetwork network = NetworkHelper.getSoulNetwork(target);
        SoulNetwork setAmount = network.setCurrentEssence(amount);
        context.getSource().sendSuccess(() -> Component.literal("Successfully set " + target.getGameProfile().getName() + "'s Essence to " + setAmount.getCurrentEssence()), true);
        return 1;
    }

    private static int addNetwork(CommandContext<CommandSourceStack> context, ServerPlayer target, int amount) {
        SoulNetwork network = NetworkHelper.getSoulNetwork(target);
        int added = network.add(SoulTicket.command(context.getSource().source, context.getInput(), amount), Integer.MAX_VALUE);
        context.getSource().sendSuccess(() -> Component.literal("Successfully added " + added + " LP to " + target.getGameProfile().getName() + "'s Soul Network"), true);
        return 1;
    }

    private static int showNetwork(CommandContext<CommandSourceStack> context, ServerPlayer target) {
        SoulNetwork network = NetworkHelper.getSoulNetwork(target);
        int amount = network.getCurrentEssence();
        context.getSource().sendSuccess(() -> Component.literal(target.getGameProfile().getName() + " has " + amount + " LP in their Soul Network"), true);
        return 1;
    }
}
