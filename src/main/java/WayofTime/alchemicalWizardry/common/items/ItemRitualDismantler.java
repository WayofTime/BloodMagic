package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.common.block.RitualStone;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemRitualDismantler extends EnergyItems
{
    public ItemRitualDismantler()
    {
        super();
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ritual_dismantler");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List par3List, boolean x)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.dismatler.desc"));
        if(stack.hasTagCompound())
        	par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        return EnergyItems.checkAndSetItemOwner(stack, player) && breakRitualStoneAtMasterStone(stack, player, world, x, y, z);
    }

    public boolean breakRitualStoneAtMasterStone(ItemStack stack, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TEMasterStone)
        {
            TEMasterStone masterStone = (TEMasterStone) tileEntity;
            int direction = masterStone.getDirection();

            String ritualName = Rituals.checkValidRitual(world, x, y, z);
            List<RitualComponent> ritualList = Rituals.getRitualList(ritualName);
            if (ritualList == null)
            {
                return false;
            }

            for (RitualComponent rc : ritualList)
            {
                if (!world.isAirBlock(x + rc.getX(direction), y + rc.getY(), z + rc.getZ(direction)) && world.getBlock(x + rc.getX(direction), y + rc.getY(), z + rc.getZ(direction)) instanceof RitualStone)
                {
                    if (EnergyItems.syphonBatteries(stack, player, getEnergyUsed()) || player.capabilities.isCreativeMode)
                    {
                        world.setBlockToAir(x + rc.getX(direction), y + rc.getY(), z + rc.getZ(direction));
                        EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(ModBlocks.ritualStone));
                        if (world.isRemote)
                        {
                            world.playAuxSFX(2005, x, y + 1, z, 0);
                        }
                        else
                        {
                            world.spawnEntityInWorld(entityItem);
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }
}
