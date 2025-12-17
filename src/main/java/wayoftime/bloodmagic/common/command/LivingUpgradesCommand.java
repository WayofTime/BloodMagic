package wayoftime.bloodmagic.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.LivingStats;
import wayoftime.bloodmagic.common.datacomponent.UpgradeLimits;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.registry.BMRegistries;

import javax.swing.text.html.CSS;
import java.util.Optional;

public class LivingUpgradesCommand {
    private static final DynamicCommandExceptionType ERROR_NO_LIVING_HOLDER = new DynamicCommandExceptionType(playername -> Component.translatable("command.bloodmagic.upgrade.no_armour", playername));

    public static LiteralArgumentBuilder<CommandSourceStack> command(CommandBuildContext buildContext) {
                return Commands.literal("living-upgrade")
                        .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(
                                Commands.argument("target", EntityArgument.player())
                                        .then(
                                                Commands.literal("upgrade")
                                                        .then(
                                                                Commands.literal("set")
                                                                        .then(
                                                                                Commands.argument("id", ResourceArgument.resource(buildContext, BMRegistries.Keys.LIVING_UPGRADES))
                                                                                        .then(
                                                                                                Commands.argument("exp", IntegerArgumentType.integer(0))
                                                                                                        .executes(
                                                                                                                context -> setUpgrade(context.getSource(), EntityArgument.getPlayer(context, "target"), ResourceArgument.getResource(context, "id", BMRegistries.Keys.LIVING_UPGRADES), IntegerArgumentType.getInteger(context, "exp"))
                                                                                                        )
                                                                                        )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("get")
                                                                        .executes(
                                                                                context -> getUpgrades(context.getSource(), EntityArgument.getPlayer(context, "target"), Optional.empty())
                                                                        )
                                                                        .then(
                                                                                Commands.argument("id", ResourceArgument.resource(buildContext, BMRegistries.Keys.LIVING_UPGRADES))
                                                                                        .executes(
                                                                                                context -> getUpgrades(context.getSource(), EntityArgument.getPlayer(context, "target"), Optional.of(ResourceArgument.getResource(context, "id", BMRegistries.Keys.LIVING_UPGRADES)))
                                                                                        )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("limits")
                                                        .then(
                                                                Commands.literal("set")
                                                                        .then(
                                                                                Commands.argument("id", ResourceArgument.resource(buildContext, BMRegistries.Keys.LIVING_UPGRADES))
                                                                                        .then(
                                                                                                Commands.argument("exp", IntegerArgumentType.integer(0))
                                                                                                        .executes(
                                                                                                                context -> setLimit(context.getSource(), EntityArgument.getPlayer(context, "target"), ResourceArgument.getResource(context, "id", BMRegistries.Keys.LIVING_UPGRADES), IntegerArgumentType.getInteger(context, "exp"))
                                                                                                        )
                                                                                        )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("get")
                                                                        .executes(
                                                                                context -> getLimit(context.getSource(), EntityArgument.getPlayer(context, "target"), Optional.empty())
                                                                        )
                                                                        .then(
                                                                                Commands.argument("id", ResourceArgument.resource(buildContext, BMRegistries.Keys.LIVING_UPGRADES))
                                                                                        .executes(
                                                                                                context -> getLimit(context.getSource(), EntityArgument.getPlayer(context, "target"), Optional.of(ResourceArgument.getResource(context, "id", BMRegistries.Keys.LIVING_UPGRADES)))
                                                                                        )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("remove")
                                                                        .then(
                                                                                Commands.argument("id", ResourceArgument.resource(buildContext, BMRegistries.Keys.LIVING_UPGRADES))
                                                                                        .executes(context -> removeLimit(context.getSource(), EntityArgument.getPlayer(context, "target"), ResourceArgument.getResource(context, "id", BMRegistries.Keys.LIVING_UPGRADES)))
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("mode")
                                                                        .then(
                                                                                Commands.literal("allow")
                                                                                        .executes(context -> setMode(context.getSource(), EntityArgument.getPlayer(context, "target"), true))
                                                                        )
                                                                        .then(
                                                                                Commands.literal("deny")
                                                                                        .executes(context -> setMode(context.getSource(), EntityArgument.getPlayer(context, "target"), false))
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("points")
                                                        .then(
                                                                Commands.literal("recalc")
                                                                        .executes(context -> recalcPoints(context.getSource(), EntityArgument.getPlayer(context, "target")))
                                                        )
                                                        .then(
                                                                Commands.literal("set-evolved-state")
                                                                        .then(Commands.literal("evolved")
                                                                                .executes(context -> setEvolved(context.getSource(), EntityArgument.getPlayer(context, "target"), true))
                                                                        )
                                                                        .then(Commands.literal("not-evolved")
                                                                                .executes(context -> setEvolved(context.getSource(), EntityArgument.getPlayer(context, "target"), false))
                                                                        )
                                                        )
                                        )
                        );
    }

    private static int setMode(CommandSourceStack source, ServerPlayer target, boolean mode) throws CommandSyntaxException {
        if (LivingHelper.isNeverValid(target)) {
            throw ERROR_NO_LIVING_HOLDER.create(target.getName());
        }
        ItemStack chest = LivingHelper.getChest(target);
        UpgradeLimits limits = chest.getOrDefault(BMDataComponents.LIMITS, UpgradeLimits.EMPTY);
        chest.set(BMDataComponents.LIMITS, new UpgradeLimits(mode, limits.limits().clone()));

        return Command.SINGLE_SUCCESS;
    }

    private static int removeLimit(CommandSourceStack source, ServerPlayer target, Holder<LivingUpgrade> id) throws CommandSyntaxException {
        if (LivingHelper.isNeverValid(target)) {
            throw ERROR_NO_LIVING_HOLDER.create(target.getName());
        }
        ItemStack chest = LivingHelper.getChest(target);
        UpgradeLimits limits = chest.getOrDefault(BMDataComponents.LIMITS, UpgradeLimits.EMPTY);
        Object2FloatOpenHashMap<Holder<LivingUpgrade>> limitMap = limits.limits().clone();
        limitMap.removeFloat(id);
        chest.set(BMDataComponents.LIMITS, new UpgradeLimits(limits.allowOthers(), limitMap));

        return Command.SINGLE_SUCCESS;
    }

    private static int setEvolved(CommandSourceStack source, ServerPlayer target, boolean evolved) throws CommandSyntaxException {
        if (LivingHelper.isNeverValid(target)) {
            throw ERROR_NO_LIVING_HOLDER.create(target.getName());
        }
        ItemStack chest = LivingHelper.getChest(target);
        chest.set(BMDataComponents.IS_EVOLVED, evolved);
        source.sendSuccess(() -> Component.translatable("commands.bloodmagic.evolve.success", evolved), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int recalcPoints(CommandSourceStack source, ServerPlayer target) throws CommandSyntaxException {
        if (LivingHelper.isNeverValid(target)) {
            throw ERROR_NO_LIVING_HOLDER.create(target.getName());
        }
        int calculated = LivingHelper.recalcPoints(target);
        source.sendSuccess(() -> Component.translatable("commands.bloodmagic.recalc.success", calculated), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int setUpgrade(CommandSourceStack source, ServerPlayer target, Holder<LivingUpgrade> id, int exp) throws CommandSyntaxException {
        if (LivingHelper.isNeverValid(target)) {
            throw ERROR_NO_LIVING_HOLDER.create(target.getName());
        }
        ItemStack chest = LivingHelper.getChest(target);
        Object2FloatOpenHashMap<Holder<LivingUpgrade>> map = new Object2FloatOpenHashMap<>();
        map.putAll(chest.getOrDefault(BMDataComponents.UPGRADES, LivingStats.EMPTY).upgrades());
        map.put(id, exp);
        chest.set(BMDataComponents.UPGRADES, new LivingStats(map));

        source.sendSuccess(() -> Component.translatable("commands.bloodmagic.upgrade.set", Component.translatable(LivingUpgrade.descriptionId(id.getKey())), exp, target.getName()), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int getUpgrades(CommandSourceStack source, Player target, Optional<Holder<LivingUpgrade>> filter) throws CommandSyntaxException {
        if (LivingHelper.isNeverValid(target)) { // if this check fails the applied upgrades can never take effect
            throw ERROR_NO_LIVING_HOLDER.create(target.getName());
        }

        ItemStack chestStack = LivingHelper.getChest(target);
        LivingStats stats = chestStack.getOrDefault(BMDataComponents.UPGRADES, LivingStats.EMPTY);
        MutableComponent result = Component.empty();
        if (filter.isEmpty()) {
            stats.object2FloatEntrySet().forEach(entry -> {
                result.append(Component.translatable(LivingUpgrade.descriptionId(entry.getKey().getKey())));
                result.append(Component.literal(": " + entry.getFloatValue() + "exp\n"));
            });
        } else {
            result.append(Component.translatable(LivingUpgrade.descriptionId(filter.get().getKey())));
            result.append(Component.literal(": " + stats.upgrades().getFloat(filter.get()) + "exp"));
        }

        source.sendSuccess(() -> Component.translatable("commands.bloodmagic.upgrade.get", target.getName()).append(result), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int setLimit(CommandSourceStack source, ServerPlayer target, Holder<LivingUpgrade> id, int exp) throws CommandSyntaxException {
        if (LivingHelper.isNeverValid(target)) {
            throw ERROR_NO_LIVING_HOLDER.create(target.getName());
        }
        ItemStack chest = LivingHelper.getChest(target);
        Object2FloatOpenHashMap<Holder<LivingUpgrade>> map = new Object2FloatOpenHashMap<>();
        UpgradeLimits limits = chest.getOrDefault(BMDataComponents.LIMITS, UpgradeLimits.EMPTY);
        map.putAll(limits.limits());
        map.put(id, exp);
        chest.set(BMDataComponents.LIMITS, new UpgradeLimits(limits.allowOthers(), map));

        source.sendSuccess(() -> Component.translatable("commands.bloodmagic.limit.set", Component.translatable(LivingUpgrade.descriptionId(id.getKey())), exp, target.getName()), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int getLimit(CommandSourceStack source, Player target, Optional<Holder<LivingUpgrade>> filter) throws CommandSyntaxException {
        if (LivingHelper.isNeverValid(target)) { // if this check fails the applied upgrades can never take effect
            throw ERROR_NO_LIVING_HOLDER.create(target.getName());
        }

        ItemStack chestStack = LivingHelper.getChest(target);
        UpgradeLimits stats = chestStack.getOrDefault(BMDataComponents.LIMITS, UpgradeLimits.EMPTY);
        MutableComponent result = Component.empty();
        Component mode = Component.translatable("commands.bloodmagic.limit.mode." + (stats.allowOthers() ? "allow" : "deny"));
        if (filter.isEmpty()) {
            stats.limits().object2FloatEntrySet().forEach(entry -> {
                result.append(Component.translatable(LivingUpgrade.descriptionId(entry.getKey().getKey())));
                result.append(Component.literal(": " + entry.getFloatValue() + "exp\n"));
            });
        } else {
            result.append(Component.translatable(LivingUpgrade.descriptionId(filter.get().getKey())));
            result.append(Component.literal(": " + stats.getLimit(filter.get()) + "exp"));
        }

        source.sendSuccess(() -> Component.translatable("commands.bloodmagic.limit.get", target.getName(), mode).append(result), true);
        return Command.SINGLE_SUCCESS;
    }
}
