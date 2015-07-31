package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.BindableItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

public class SigilFluid extends BindableItems implements IFluidContainerItem, ISigil
{
    private int capacity = 128 * 1000;
    private static final int STATE_SYPHON = 0;
    private static final int STATE_FORCE_SYPHON = 1;
    private static final int STATE_PLACE = 2;
    private static final int STATE_INPUT_TANK = 3;
    private static final int STATE_DRAIN_TANK = 4;
    private static final int STATE_BEAST_DRAIN = 5;
    private static final int maxNumOfStates = 6;

    public SigilFluid()
    {
        super();
        this.setMaxDamage(0);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.fluidsigil.desc"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            switch (this.getActionState(par1ItemStack))
            {
                case STATE_SYPHON:
                    par3List.add(StatCollector.translateToLocal("tooltip.fluidsigil.syphoningmode"));
                    break;
                case STATE_FORCE_SYPHON:
                    par3List.add(StatCollector.translateToLocal("tooltip.fluidsigil.forcesyphonmode"));
                    break;
                case STATE_PLACE:
                    par3List.add(StatCollector.translateToLocal("tooltip.fluidsigil.fluidplacementmode"));
                    break;
                case STATE_INPUT_TANK:
                    par3List.add(StatCollector.translateToLocal("tooltip.fluidsigil.filltankmode"));
                    break;
                case STATE_DRAIN_TANK:
                    par3List.add(StatCollector.translateToLocal("tooltip.fluidsigil.draintankmode"));
                    break;
                case STATE_BEAST_DRAIN:
                    par3List.add(StatCollector.translateToLocal("tooltip.fluidsigil.beastmode"));
                    break;
            }

            FluidStack fluid = this.getFluid(par1ItemStack);
            if (fluid != null && fluid.amount > 0)
            {
                String str = fluid.getFluid().getName();
                int amount = fluid.amount;

                par3List.add("" + amount + "mB of " + str);
            } else
            {
                par3List.add("Empty");
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par3EntityPlayer.isSneaking())
        {
            int curState = this.cycleActionState(par1ItemStack);
            this.sendMessageViaState(curState, par3EntityPlayer);
            return par1ItemStack;
        }

        switch (this.getActionState(par1ItemStack))
        {
            case STATE_SYPHON:
                return this.fillItemFromWorld(par1ItemStack, par2World, par3EntityPlayer, false);
            case STATE_FORCE_SYPHON:
                return this.fillItemFromWorld(par1ItemStack, par2World, par3EntityPlayer, true);
            case STATE_PLACE:
                return this.emptyItemToWorld(par1ItemStack, par2World, par3EntityPlayer);
            case STATE_INPUT_TANK:
                return this.fillSelectedTank(par1ItemStack, par2World, par3EntityPlayer);
            case STATE_DRAIN_TANK:
                return this.drainSelectedTank(par1ItemStack, par2World, par3EntityPlayer);
            case STATE_BEAST_DRAIN:
                return this.fillItemFromBeastWorld(par1ItemStack, par2World, par3EntityPlayer, true);
        }

        return par1ItemStack;
    }

    public int getActionState(ItemStack item)
    {
        if (item.getTagCompound() == null)
        {
            item.setTagCompound(new NBTTagCompound());
        }

        return item.getTagCompound().getInteger("actionState");
    }

    public void setActionState(ItemStack item, int actionState)
    {
        if (item.getTagCompound() == null)
        {
            item.setTagCompound(new NBTTagCompound());
        }

        item.getTagCompound().setInteger("actionState", actionState);
    }

    public int cycleActionState(ItemStack item)
    {
        int state = this.getActionState(item);

        state++;

        if (state >= maxNumOfStates)
        {
            state = 0;
        }

        this.setActionState(item, state);

        return state;
    }

    public void sendMessageViaState(int state, EntityPlayer player)
    {
        if (player.worldObj.isRemote)
        {
            ChatComponentText cmc = new ChatComponentText("");
            switch (state)
            {
                case STATE_SYPHON:
                    cmc.appendText("Now in Syphoning Mode");
                    break;
                case STATE_FORCE_SYPHON:
                    cmc.appendText("Now in Force-syphon Mode");
                    break;
                case STATE_PLACE:
                    cmc.appendText("Now in Fluid Placement Mode");
                    break;
                case STATE_INPUT_TANK:
                    cmc.appendText("Now in Fill Tank Mode");
                    break;
                case STATE_DRAIN_TANK:
                    cmc.appendText("Now in Drain Tank Mode");
                    break;
                case STATE_BEAST_DRAIN:
                    cmc.appendText("Now in Beast Mode");
                    break;
            }
            player.addChatComponentMessage(cmc);
        }
    }

    public ItemStack fillItemFromBeastWorld(ItemStack container, World world, EntityPlayer player, boolean forceFill)
    {
        if (world.isRemote)
        {
            return container;
        }
        int range = 5;

        boolean flag = true;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null)
        {
            return container;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                if (!world.canMineBlockBody(player, movingobjectposition.func_178782_a()))
                {
                    return container;
                }

                if (!player.func_175151_a(movingobjectposition.func_178782_a(), movingobjectposition.field_178784_b, container))
                {
                    return container;
                }

                boolean[][][] boolList = new boolean[range * 2 + 1][range * 2 + 1][range * 2 + 1];

                for (int i = 0; i < 2 * range + 1; i++)
                {
                    for (int j = 0; j < 2 * range + 1; j++)
                    {
                        for (int k = 0; k < 2 * range + 1; k++)
                        {
                            boolList[i][j][k] = false;
                        }
                    }
                }

                List<Int3> positionList = new ArrayList<Int3>();

                int x = movingobjectposition.func_178782_a().getX();
                int y = movingobjectposition.func_178782_a().getY();
                int z = movingobjectposition.func_178782_a().getZ();

                boolList[range][range][range] = true;
                positionList.add(new Int3(range, range, range));
                boolean isReady = false;

                while (!isReady)
                {
                    isReady = true;

                    for (int i = 0; i < 2 * range + 1; i++)
                    {
                        for (int j = 0; j < 2 * range + 1; j++)
                        {
                            for (int k = 0; k < 2 * range + 1; k++)
                            {
                                if (boolList[i][j][k])
                                {
                                    if (i - 1 >= 0 && !boolList[i - 1][j][k])
                                    {
                                        Block block = world.getBlockState(new BlockPos(x - range + i - 1, y - range + j, z - range + k)).getBlock();
                                        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

                                        if (fluid != null)
                                        {
                                            boolList[i - 1][j][k] = true;
                                            positionList.add(new Int3(i - 1, j, k));
                                            isReady = false;
                                        }
                                    }

                                    if (j - 1 >= 0 && !boolList[i][j - 1][k])
                                    {
                                        Block block = world.getBlockState(new BlockPos(x - range + i, y - range + j - 1, z - range + k)).getBlock();
                                        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

                                        if (fluid != null)
                                        {
                                            boolList[i][j - 1][k] = true;
                                            positionList.add(new Int3(i, j - 1, k));
                                            isReady = false;
                                        }
                                    }

                                    if (k - 1 >= 0 && !boolList[i][j][k - 1])
                                    {
                                        Block block = world.getBlockState(new BlockPos(x - range + i, y - range + j, z - range + k - 1)).getBlock();
                                        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

                                        if (fluid != null)
                                        {
                                            boolList[i][j][k - 1] = true;
                                            positionList.add(new Int3(i, j, k - 1));
                                            isReady = false;
                                        }
                                    }

                                    if (i + 1 <= 2 * range && !boolList[i + 1][j][k])
                                    {
                                        Block block = world.getBlockState(new BlockPos(x - range + i + 1, y - range + j, z - range + k)).getBlock();
                                        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

                                        if (fluid != null)
                                        {
                                            boolList[i + 1][j][k] = true;
                                            positionList.add(new Int3(i + 1, j, k));
                                            isReady = false;
                                        }
                                    }

                                    if (j + 1 <= 2 * range && !boolList[i][j + 1][k])
                                    {
                                        Block block = world.getBlockState(new BlockPos(x - range + i, y - range + j + 1, z - range + k)).getBlock();
                                        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

                                        if (fluid != null)
                                        {
                                            boolList[i][j + 1][k] = true;
                                            positionList.add(new Int3(i, j + 1, k));
                                            isReady = false;
                                        }
                                    }

                                    if (k + 1 <= 2 * range && !boolList[i][j][k + 1])
                                    {
                                        Block block = world.getBlockState(new BlockPos(x - range + i, y - range + j, z - range + k + 1)).getBlock();
                                        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

                                        if (fluid != null)
                                        {
                                            boolList[i][j][k + 1] = true;
                                            positionList.add(new Int3(i, j, k + 1));
                                            isReady = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                for (Int3 pos : positionList)
                {
                    int i = pos.xCoord;
                    int j = pos.yCoord;
                    int k = pos.zCoord;

                    if (!boolList[i][j][k])
                    {
                        continue;
                    }
                    if (world.getBlockState(new BlockPos(x + i - range, y + j - range, z + k - range)).getBlock() != null && world.getBlockState(new BlockPos(x + i - range, y + j - range, z + k - range)).getBlock().getMaterial() instanceof MaterialLiquid)
                    {
                        //world.setBlockToAir(x+i-range, y+j-range, z+k-range);
                        Block block = world.getBlockState(new BlockPos(x + i - range, y + j - range, z + k - range)).getBlock();
                        if (block == null)
                        {
                            continue;
                        }
                        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

                        AlchemicalWizardry.logger.info("x: " + (i - range) + " y: " + (j - range) + " z: " + (k - range));


                        if (fluid == null || world.getBlockState(new BlockPos(x + i - range, y + j - range, z + k - range)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x + i - range, y + j - range, z + k - range))) != 0)
                        {
                            continue;
                        }


                        FluidStack fillStack = new FluidStack(fluid, 1000);

                        int amount = this.fill(container, fillStack, false);

                        if ((amount > 0 && forceFill) || (amount >= 1000 && !forceFill))
                        {
                            {
                                world.setBlockToAir(new BlockPos(x + i - range, y + j - range, z + k - range));
                            }

                            this.fill(container, new FluidStack(fluid, 1000), true);
                        }
                    }
                }
            }

            return container;
        }
    }

    public ItemStack fillItemFromWorld(ItemStack container, World world, EntityPlayer player, boolean forceFill)
    {
        boolean flag = true;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null)
        {
            return container;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                if (!world.canMineBlockBody(player, movingobjectposition.func_178782_a()))
                {
                    return container;
                }

                if (!player.func_175151_a(movingobjectposition.func_178782_a(), movingobjectposition.field_178784_b, container))
                {
                    return container;
                }

                if (world.getBlockState(movingobjectposition.func_178782_a()).getBlock() != null && world.getBlockState(movingobjectposition.func_178782_a()).getBlock().getMaterial() instanceof MaterialLiquid)
                {
                    Block block = world.getBlockState(movingobjectposition.func_178782_a()).getBlock();
                    Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

                    if (fluid == null)
                    {
                        return container;
                    }

                    FluidStack fillStack = new FluidStack(fluid, 1000);

                    int amount = this.fill(container, fillStack, false);

                    if ((amount > 0 && forceFill) || (amount >= 1000 && !forceFill))
                    {
                        if (!player.capabilities.isCreativeMode)
                        {
                            world.setBlockToAir(movingobjectposition.func_178782_a());
                        }

                        this.fill(container, new FluidStack(fluid, 1000), true);

                        if (!player.capabilities.isCreativeMode)
                        {
                        } else
                        {
                            return container;
                        }
                    }
                }
            }

            return container;
        }
    }

    public ItemStack emptyItemToWorld(ItemStack container, World world, EntityPlayer player)
    {
        FluidStack simStack = this.drain(container, 1000, false);

        if (simStack != null && simStack.amount >= 1000)
        {
            boolean flag = false;
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

            if (movingobjectposition == null)
            {
                return container;
            } else
            {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    if (!world.canMineBlockBody(player, movingobjectposition.func_178782_a()))
                    {
                        return container;
                    }
                    
                    int i = movingobjectposition.func_178782_a().getX();
                    int j = movingobjectposition.func_178782_a().getY();
                    int k = movingobjectposition.func_178782_a().getZ();

                    if (movingobjectposition.field_178784_b.getIndex() == 0)
                    {
                        --j;
                    }

                    if (movingobjectposition.field_178784_b.getIndex() == 1)
                    {
                        ++j;
                    }

                    if (movingobjectposition.field_178784_b.getIndex() == 2)
                    {
                        --k;
                    }

                    if (movingobjectposition.field_178784_b.getIndex() == 3)
                    {
                        ++k;
                    }

                    if (movingobjectposition.field_178784_b.getIndex() == 4)
                    {
                        --i;
                    }

                    if (movingobjectposition.field_178784_b.getIndex() == 5)
                    {
                        ++i;
                    }

                    if (!player.func_175151_a(new BlockPos(i, j, k), movingobjectposition.field_178784_b, container))
                    {
                        return container;
                    }

                    if (this.tryPlaceContainedLiquid(world, new BlockPos(i, j, k)) && !player.capabilities.isCreativeMode)
                    {
                        this.drain(container, 1000, true);

                        return container;
                    }
                }

                return container;
            }
        }

        return container;
    }

    public boolean tryPlaceContainedLiquid(World world, BlockPos blockPos)
    {
        if (!world.isAirBlock(blockPos) && world.getBlockState(blockPos).getBlock().getMaterial().isSolid()) //TODO Was func_149730_j() so check this!
        {
            return false;
        } else if ((world.getBlockState(blockPos).getBlock().getMaterial() instanceof MaterialLiquid && world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos)) == 0))
        {
            return false;
        } else
        {
            Block block = world.getBlockState(blockPos).getBlock();
            if ((block == Blocks.water || block == Blocks.flowing_water) && world.provider.func_177500_n())
            {
                world.playSoundEffect(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l)
                {
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) blockPos.getX() + Math.random(), (double) blockPos.getY() + Math.random(), (double) blockPos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else
            {
                world.setBlockState(blockPos, block.getBlockState().getBaseState());
            }

            return true;
        }
    }

    public ItemStack fillSelectedTank(ItemStack container, World world, EntityPlayer player)
    {
        FluidStack fluid = this.getFluid(container);

        if (fluid == null)
        {
            return container;
        }

        float f = 1.0F;
        boolean flag = false;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null)
        {
            return container;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                TileEntity tile = world.getTileEntity(movingobjectposition.func_178782_a());

                if (tile instanceof IFluidHandler)
                {
                    int amount = ((IFluidHandler) tile).fill(movingobjectposition.field_178784_b, fluid, true);

                    this.drain(container, amount, true);
                }
            }
        }

        return container;
    }

    public ItemStack drainSelectedTank(ItemStack container, World world, EntityPlayer player)
    {
        float f = 1.0F;
        boolean flag = false;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null)
        {
            return container;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                TileEntity tile = world.getTileEntity(movingobjectposition.func_178782_a());

                if (tile instanceof IFluidHandler)
                {
                    FluidStack fluidAmount = ((IFluidHandler) tile).drain(movingobjectposition.field_178784_b, this.getCapacity(container), false);

                    int amount = this.fill(container, fluidAmount, false);

                    if (amount > 0)
                    {
                        ((IFluidHandler) tile).drain(movingobjectposition.field_178784_b, this.getCapacity(container), true);

                        this.fill(container, fluidAmount, true);
                    }
                }
            }
        }

        return container;
    }

    /* IFluidContainerItem */
    @Override
    public FluidStack getFluid(ItemStack container)
    {
        if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Fluid"))
        {
            return null;
        }
        return FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));
    }

    @Override
    public int getCapacity(ItemStack container)
    {
        return capacity;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill)
    {
        if (resource == null)
        {
            return 0;
        }

        if (!doFill)
        {
            if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Fluid"))
            {
                return Math.min(capacity, resource.amount);
            }

            FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));

            if (stack == null || stack.amount <= 0)
            {
                return Math.min(capacity, resource.amount);
            }

            if (!stack.isFluidEqual(resource))
            {
                return 0;
            }

            return Math.min(capacity - stack.amount, resource.amount);
        }

        if (container.getTagCompound() == null)
        {
            container.setTagCompound(new NBTTagCompound());
        }

        if (!container.getTagCompound().hasKey("Fluid"))
        {
            NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());

            if (capacity < resource.amount)
            {
                fluidTag.setInteger("Amount", capacity);
                container.getTagCompound().setTag("Fluid", fluidTag);
                return capacity;
            }

            container.getTagCompound().setTag("Fluid", fluidTag);
            return resource.amount;
        }

        NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag("Fluid");
        FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

        if (stack == null || stack.amount <= 0)
        {
            NBTTagCompound fluidTag1 = resource.writeToNBT(new NBTTagCompound());

            if (capacity < resource.amount)
            {
                fluidTag1.setInteger("Amount", capacity);
                container.getTagCompound().setTag("Fluid", fluidTag1);
                return capacity;
            }

            container.getTagCompound().setTag("Fluid", fluidTag1);
            return resource.amount;
        }

        if (!stack.isFluidEqual(resource))
        {
            return 0;
        }

        int filled = capacity - stack.amount;
        if (resource.amount < filled)
        {
            stack.amount += resource.amount;
            filled = resource.amount;
        } else
        {
            stack.amount = capacity;
        }

        container.getTagCompound().setTag("Fluid", stack.writeToNBT(fluidTag));
        return filled;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
        if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Fluid"))
        {
            return null;
        }

        FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));
        if (stack == null)
        {
            return null;
        }

        stack.amount = Math.min(stack.amount, maxDrain);
        if (doDrain)
        {
            if (maxDrain >= capacity)
            {
                container.getTagCompound().removeTag("Fluid");

                if (container.getTagCompound().hasNoTags())
                {
                    container.setTagCompound(null);
                }
                return stack;
            }

            NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag("Fluid");
            fluidTag.setInteger("Amount", fluidTag.getInteger("Amount") - maxDrain);
            container.getTagCompound().setTag("Fluid", fluidTag);
        }
        return stack;
    }
}
