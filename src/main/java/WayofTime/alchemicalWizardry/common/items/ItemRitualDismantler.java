package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.common.block.BlockRitualStone;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public class ItemRitualDismantler extends EnergyItems
{
    public ItemRitualDismantler()
    {
        super();
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List par3List, boolean x)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.dismatler.desc"));
        if(stack.hasTagCompound())
        	par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return EnergyItems.checkAndSetItemOwner(stack, player) && breakRitualStoneAtMasterStone(stack, player, world, pos);
    }

    public boolean breakRitualStoneAtMasterStone(ItemStack stack, EntityPlayer player, World world, BlockPos pos)
    {
        ItemStack[] playerInventory = player.inventory.mainInventory;
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TEMasterStone)
        {
            TEMasterStone masterStone = (TEMasterStone) tileEntity;
            int direction = masterStone.getDirection();
            int freeSpace = -1;

            List<RitualComponent> ritualList = Rituals.getRitualList(masterStone.getCurrentRitual());
            if (ritualList == null)
            {
                return false;
            }

            for (int i = 0; i < playerInventory.length; i++)
            {
                if (playerInventory[i] == null)
                {
                    freeSpace = i;
                    break;
                }
            }

            for (RitualComponent rc : ritualList)
            {
            	BlockPos newPos = pos.add(rc.getX(direction), rc.getY(), rc.getZ(direction));
                if (!world.isAirBlock(newPos) && world.getBlockState(newPos).getBlock() instanceof BlockRitualStone)
                {
                    if (freeSpace >= 0)
                    {
                        if (EnergyItems.syphonBatteries(stack, player, getEnergyUsed()) || player.capabilities.isCreativeMode)
                        {
                            world.setBlockToAir(newPos);
                            player.inventory.addItemStackToInventory(new ItemStack(ModBlocks.ritualStone));
                            if (world.isRemote)
                            {
                                world.playAuxSFX(2005, pos.offsetUp(), 0);

                                return true;
                            }
                        }

                        return true;
                    }
                }
            }
        }

        return false;
    }
}
