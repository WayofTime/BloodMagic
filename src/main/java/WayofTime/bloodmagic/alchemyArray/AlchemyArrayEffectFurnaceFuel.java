package WayofTime.bloodmagic.alchemyArray;

import java.util.List;

import WayofTime.bloodmagic.util.DamageSourceBloodMagic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AlchemyArrayEffectFurnaceFuel extends AlchemyArrayEffect
{
    static double radius = 10;
    static int burnTicksAdded = 401; //Set to +1 more than it needs to be due to a hacky method - basically done so that the array doesn't double dip your health if you only add one item.

    public AlchemyArrayEffectFurnaceFuel(String key)
    {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive)
    {
        BlockPos pos = tile.getPos();
        World world = tile.getWorld();
        EntityPlayer sacrifice = null;

        for (EnumFacing face : EnumFacing.VALUES)
        {
            BlockPos furnacePos = pos.offset(face);
            Block block = world.getBlockState(furnacePos).getBlock();
            if (block != Blocks.FURNACE) //This will only work vanilla furnaces. No others!
            {
                continue;
            }

            TileEntity bottomTile = world.getTileEntity(furnacePos);
            if (bottomTile instanceof TileEntityFurnace)
            {
                TileEntityFurnace furnaceTile = (TileEntityFurnace) bottomTile;
                if (canFurnaceSmelt(furnaceTile) && !furnaceTile.isBurning())
                {
                    if (sacrifice == null || sacrifice.isDead)
                    {
                        AxisAlignedBB bb = new AxisAlignedBB(pos).grow(radius);
                        List<EntityPlayer> playerList = world.getEntitiesWithinAABB(EntityPlayer.class, bb);
                        for (EntityPlayer player : playerList)
                        {
                            if (!player.isDead)
                            {
                                sacrifice = player;
                            }
                        }
                    }

                    if (sacrifice == null || sacrifice.isDead)
                    {
                        return false;
                    }

                    if (addFuelTime(furnaceTile, world, furnacePos, burnTicksAdded))
                    {
                        if (!sacrifice.capabilities.isCreativeMode)
                        {
                            sacrifice.hurtResistantTime = 0;
                            sacrifice.attackEntityFrom(DamageSourceBloodMagic.INSTANCE, 1.0F); //No.
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean addFuelTime(TileEntityFurnace furnaceTile, World world, BlockPos furnacePos, int cookTime)
    {
        furnaceTile.setField(0, cookTime);
        BlockFurnace.setState(true, world, furnacePos);
        return true;
    }

    public static boolean canFurnaceSmelt(TileEntityFurnace furnaceTile)
    {
        ItemStack burnStack = furnaceTile.getStackInSlot(0);
        if (burnStack.isEmpty())
        {
            return false;
        } else
        {
            ItemStack resultStack = FurnaceRecipes.instance().getSmeltingResult(burnStack);

            if (resultStack.isEmpty())
            {
                return false;
            } else
            {
                ItemStack finishStack = furnaceTile.getStackInSlot(2);

                if (finishStack.isEmpty())
                {
                    return true;
                } else if (!finishStack.isItemEqual(resultStack))
                {
                    return false;
                } else if (finishStack.getCount() + resultStack.getCount() <= furnaceTile.getInventoryStackLimit() && finishStack.getCount() + resultStack.getCount() <= finishStack.getMaxStackSize()) // Forge fix: make furnace respect stack sizes in furnace recipes
                {
                    return true;
                } else
                {
                    return finishStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {

    }

    @Override
    public AlchemyArrayEffect getNewCopy()
    {
        return new AlchemyArrayEffectFurnaceFuel(key);
    }
}