package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEHomHeart;

public class BlankSpell extends EnergyItems
{
    public BlankSpell()
    {
        super();
        this.setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add(StatCollector.translateToLocal("tooltip.blankspell.desc"));

        if (!(stack.getTagCompound() == null))
        {
            NBTTagCompound itemTag = stack.getTagCompound();

            if (!stack.getTagCompound().getString("ownerName").equals(""))
            {
                list.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
            }

            list.add(StatCollector.translateToLocal("tooltip.alchemy.coords") + " " + itemTag.getInteger("xCoord") + ", " + itemTag.getInteger("yCoord") + ", " + itemTag.getInteger("zCoord"));
            list.add(StatCollector.translateToLocal("tooltip.alchemy.dimension") + " " + getDimensionID(stack));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!EnergyItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return stack;
        }

        if (!world.isRemote)
        {
            World newWorld = DimensionManager.getWorld(getDimensionID(stack));

            if (newWorld != null)
            {
                NBTTagCompound itemTag = stack.getTagCompound();
                TileEntity tileEntity = newWorld.getTileEntity(new BlockPos(itemTag.getInteger("xCoord"), itemTag.getInteger("yCoord"), itemTag.getInteger("zCoord")));

                if (tileEntity instanceof TEHomHeart)
                {
                    TEHomHeart homHeart = (TEHomHeart) tileEntity;

                    if (homHeart.canCastSpell(stack, world, player))
                    {
                    	if(EnergyItems.syphonBatteries(stack, player, homHeart.getCostForSpell()))
                    	{
                            EnergyItems.syphonBatteries(stack, player, homHeart.castSpell(stack, world, player));
                    	}
                    } else
                    {
                        return stack;
                    }
                } else
                {
                    return stack;
                }
            } else
            {
                return stack;
            }
        } else
        {
            return stack;
        }
        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        return stack;
    }

    public int getDimensionID(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.getTagCompound().getInteger("dimensionId");
    }
}
