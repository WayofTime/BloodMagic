package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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

    @Override
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
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
    	if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block)
        {
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
    }
}
