package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.api.rituals.IRitualStone;
import WayofTime.alchemicalWizardry.common.items.ScribeTool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockRitualStone extends Block implements IRitualStone
{
    public BlockRitualStone()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public int damageDropped(IBlockState blockState)
    {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem == null)
        {
            return false;
        }

        Item item = playerItem.getItem();

        if (!(item instanceof ScribeTool))
        {
            return false;
        }

        if (playerItem.getMaxDamage() <= playerItem.getItemDamage() && !(playerItem.getMaxDamage() == 0))
        {
            return false;
        }

        ScribeTool scribeTool = (ScribeTool) item;

        if (!player.capabilities.isCreativeMode)
        {
            playerItem.setItemDamage(playerItem.getItemDamage() + 1);
        }

        world.setBlockState(blockPos, state.getBlock().getStateFromMeta(scribeTool.getType()), 3);
        world.markBlockForUpdate(blockPos);
        return true;
    }

	@Override
	public boolean isRuneType(World world, BlockPos blockPos, IBlockState blockState, int runeType)
	{
		return blockState.getBlock().getMetaFromState(blockState) == runeType;
	}
}
