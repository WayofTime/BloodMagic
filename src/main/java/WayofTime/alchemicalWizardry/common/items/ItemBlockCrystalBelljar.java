package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class ItemBlockCrystalBelljar extends ItemBlock
{
    public ItemBlockCrystalBelljar(Block par1)
    {
        super(par1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(16);
    }

    public int getMetadata(int par1)
    {
        return par1;
    }

    public ReagentContainer[] getReagentContainers(ItemStack stack)
    {
        if (stack != null && stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound();

            NBTTagList tagList = tag.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);

            int size = tagList.tagCount();
            ReagentContainer[] tanks = new ReagentContainer[size];

            for (int i = 0; i < size; i++)
            {
                NBTTagCompound savedTag = tagList.getCompoundTagAt(i);
                tanks[i] = ReagentContainer.readFromNBT(savedTag);
            }

            return tanks;
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        ReagentContainer[] tanks = this.getReagentContainers(stack);

        if (tanks == null)
        {
            list.add(StatCollector.translateToLocal("tooltip.crystalbelljar.empty"));
        } else
        {
            list.add(StatCollector.translateToLocal("tooltip.crystalbelljar.contents"));
            for (int i = 0; i < tanks.length; i++)
            {
                if (tanks[i] == null || tanks[i].getReagent() == null || tanks[i].getReagent().reagent == null)
                {
                    list.add("- Empty");
                } else
                {
                    ReagentStack reagentStack = tanks[i].getReagent();
                    list.add("- " + reagentStack.reagent.name + ": " + reagentStack.amount + "/" + tanks[i].getCapacity() / 1000 + "k AR");
                }
            }
        }
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (!world.setBlock(x, y, z, field_150939_a, metadata, 3))
        {
            return false;
        }

        if (world.getBlock(x, y, z) == field_150939_a)
        {
            field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
            field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }
}
