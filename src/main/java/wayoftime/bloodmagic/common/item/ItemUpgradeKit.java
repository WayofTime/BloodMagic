package wayoftime.bloodmagic.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.block.enums.BloodRuneType;
import wayoftime.bloodmagic.common.block.BlockBloodRune;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;

public class ItemUpgradeKit extends Item
{

    private final BloodRuneType type;

    public ItemUpgradeKit(BloodRuneType type)
    {
        super(new Properties().stacksTo(64).tab(BloodMagic.TAB));
        this.type = type;
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        Player player = context.getPlayer();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockBloodRune)
        {
            BlockBloodRune rune = (BlockBloodRune) state.getBlock();
            if (canUpgrade(rune))
            {
                upgrade(type, pos, world);
                player.getItemInHand(context.getHand()).shrink(1);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }

    public void upgrade(BloodRuneType rune, BlockPos pos, Level world)
    {
        Block runeBlock = BloodMagicBlocks.BLANK_RUNE.get();
        switch (rune)
        {
        case ACCELERATION:
            runeBlock = BloodMagicBlocks.ACCELERATION_RUNE.get();
            break;
        case AUGMENTED_CAPACITY:
            runeBlock = BloodMagicBlocks.AUGMENTED_CAPACITY_RUNE.get();
            break;
        case CAPACITY:
            runeBlock = BloodMagicBlocks.CAPACITY_RUNE.get();
            break;
        case CHARGING:
            runeBlock = BloodMagicBlocks.CHARGING_RUNE.get();
            break;
        case DISPLACEMENT:
            runeBlock = BloodMagicBlocks.DISPLACEMENT_RUNE.get();
            break;
        case ORB:
            runeBlock = BloodMagicBlocks.ORB_RUNE.get();
            break;
        case SACRIFICE:
            runeBlock = BloodMagicBlocks.SACRIFICE_RUNE.get();
            break;
        case SELF_SACRIFICE:
            runeBlock = BloodMagicBlocks.SELF_SACRIFICE_RUNE.get();
            break;
        case SPEED:
            runeBlock = BloodMagicBlocks.SPEED_RUNE.get();
            break;
        case BLANK:
            // blank is not an upgrade
            break;
        case EFFICIENCY:
            // efficiency does not have a registered block
            break;
        }

        BlockState newState = runeBlock.defaultBlockState();
        world.setBlockAndUpdate(pos, newState);
    }

    public boolean canUpgrade(BlockBloodRune rune)
    {
        switch (rune.getBloodRune(null, null))
        {
        case SPEED:
            return type == BloodRuneType.ACCELERATION;
        case CAPACITY:
            return type == BloodRuneType.AUGMENTED_CAPACITY;
        case BLANK:
            return type != BloodRuneType.ACCELERATION && type != BloodRuneType.AUGMENTED_CAPACITY;
        default:
            return false;
        }
    }
}
