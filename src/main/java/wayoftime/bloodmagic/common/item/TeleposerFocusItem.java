package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import wayoftime.bloodmagic.common.blockentity.TeleposerTile;
import wayoftime.bloodmagic.common.datacomponent.Binding;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.event.ItemBindEvent;

import java.util.ArrayList;
import java.util.List;

public class TeleposerFocusItem extends Item implements ITeleposerFocus {
    public final int range;

    public TeleposerFocusItem(int range) {
        super(new Item.Properties().stacksTo(1).component(BMDataComponents.BINDING, Binding.EMPTY));
        this.range = range;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        Player player = context.getPlayer();

        if (world.getBlockEntity(pos) instanceof TeleposerTile) {
            setStoredPos(stack, pos);
            setWorld(stack, world);

            Binding binding = getBinding(stack);
            if (binding == null || binding.isEmpty()) {
                ItemBindEvent toPost = new ItemBindEvent(player, stack);
                if (!NeoForge.EVENT_BUS.post(toPost).isCanceled()) {
                    stack.set(BMDataComponents.BINDING, new Binding(player.getUUID(), player.getName().getString()));
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    public void setStoredPos(ItemStack stack, BlockPos pos) {
        stack.set(BMDataComponents.TELEPOSER_POS, pos);
    }

    @Override
    public BlockPos getStoredPos(ItemStack stack) {
        BlockPos pos = stack.get(BMDataComponents.TELEPOSER_POS);
        return pos != null ? pos : BlockPos.ZERO;
    }

    public void setWorld(ItemStack stack, Level world) {
        String worldKey = world.dimension().location().toString();
        stack.set(BMDataComponents.TELEPOSER_DIMENSION, worldKey);
    }

    public ResourceKey<Level> getStoredKey(ItemStack stack) {
        String worldKey = stack.get(BMDataComponents.TELEPOSER_DIMENSION);
        if (worldKey == null || worldKey.isEmpty()) {
            return null;
        }
        return ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(worldKey));
    }

    @Override
    public Level getStoredWorld(ItemStack stack, Level world) {
        ResourceKey<Level> registryKey = getStoredKey(stack);
        if (registryKey == null || world.getServer() == null) {
            return null;
        }
        return world.getServer().getLevel(registryKey);
    }

    @Override
    public Binding getBinding(ItemStack stack) {
        return stack.get(BMDataComponents.BINDING);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        ResourceKey<Level> storedKey = getStoredKey(stack);
        if (storedKey != null) {
            BlockPos storedPos = getStoredPos(stack);
            tooltip.add(Component.translatable("tooltip.bloodmagic.telepositionfocus.coords", storedPos.getX(), storedPos.getY(), storedPos.getZ()).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.bloodmagic.telepositionfocus.world", Component.translatable(storedKey.location().toString())).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public AABB getEntityRangeOffset(Level world, BlockPos teleposerPos) {
        return new AABB(-range, 1, -range, range + 1, 2 * range + 2, range + 1);
    }

    @Override
    public List<BlockPos> getBlockListOffset(Level world) {
        List<BlockPos> posList = new ArrayList<>();
        for (int i = -range; i <= range; i++) {
            for (int j = 1; j <= 2 * range + 1; j++) {
                for (int k = -range; k <= range; k++) {
                    posList.add(new BlockPos(i, j, k));
                }
            }
        }
        return posList;
    }
}
