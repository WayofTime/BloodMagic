package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.Int3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

public class ItemFluidSigil extends Item implements IFluidContainerItem
{
    private int capacity = 128 * 1000;
    private static final int STATE_SYPHON = 0;
    private static final int STATE_FORCE_SYPHON = 1;
    private static final int STATE_PLACE = 2;
    private static final int STATE_INPUT_TANK = 3;
    private static final int STATE_DRAIN_TANK = 4;
    private static final int STATE_BEAST_DRAIN = 5;
    private static final int maxNumOfStates = 6;

    public ItemFluidSigil()
    {
        super();
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("A sigil with a lovely affinity for fluids");

        if (!(par1ItemStack.getTagCompound() == null))
        {
            switch (this.getActionState(par1ItemStack))
            {
                case STATE_SYPHON:
                    par3List.add("Syphoning Mode");
                    break;
                case STATE_FORCE_SYPHON:
                    par3List.add("Force-syphon Mode");
                    break;
                case STATE_PLACE:
                    par3List.add("Fluid Placement Mode");
                    break;
                case STATE_INPUT_TANK:
                    par3List.add("Fill Tank Mode");
                    break;
                case STATE_DRAIN_TANK:
                    par3List.add("Drain Tank Mode");
                    break;
                case STATE_BEAST_DRAIN:
                    par3List.add("Beast Mode");
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WaterSigil");
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

        float f = 1.0F;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f + 1.62D - (double) player.yOffset;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        boolean flag = true;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null)
        {
            return container;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;

                if (!world.canMineBlock(player, x, y, z))
                {
                    return container;
                }

                if (!player.canPlayerEdit(x, y, z, movingobjectposition.sideHit, container))
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

                List<Int3> positionList = new ArrayList();

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
                                        Block block = world.getBlock(x - range + i - 1, y - range + j, z - range + k);
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
                                        Block block = world.getBlock(x - range + i, y - range + j - 1, z - range + k);
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
                                        Block block = world.getBlock(x - range + i, y - range + j, z - range + k - 1);
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
                                        Block block = world.getBlock(x - range + i + 1, y - range + j, z - range + k);
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
                                        Block block = world.getBlock(x - range + i, y - range + j + 1, z - range + k);
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
                                        Block block = world.getBlock(x - range + i, y - range + j, z - range + k + 1);
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
                    if (world.getBlock(x + i - range, y + j - range, z + k - range) != null && world.getBlock(x + i - range, y + j - range, z + k - range).getMaterial() instanceof MaterialLiquid)
                    {
                        //world.setBlockToAir(x+i-range, y+j-range, z+k-range);
                        Block block = world.getBlock(x + i - range, y + j - range, z + k - range);
                        if (block == null)
                        {
                            continue;
                        }
                        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

                        AlchemicalWizardry.logger.info("x: " + (i - range) + " y: " + (j - range) + " z: " + (k - range));


                        if (fluid == null || world.getBlockMetadata(x + i - range, y + j - range, z + k - range) != 0)
                        {
                            continue;
                        }


                        FluidStack fillStack = new FluidStack(fluid, 1000);

                        int amount = this.fill(container, fillStack, false);

                        if ((amount > 0 && forceFill) || (amount >= 1000 && !forceFill))
                        {
                            {
                                world.setBlockToAir(x + i - range, y + j - range, z + k - range);

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
        float f = 1.0F;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f + 1.62D - (double) player.yOffset;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        boolean flag = true;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null)
        {
            return container;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!world.canMineBlock(player, i, j, k))
                {
                    return container;
                }

                if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, container))
                {
                    return container;
                }

                if (world.getBlock(i, j, k) != null && world.getBlock(i, j, k).getMaterial() instanceof MaterialLiquid)
                {
                    Block block = world.getBlock(i, j, k);
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
                            world.setBlockToAir(i, j, k);
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
            Block fluidBlock = simStack.getFluid().getBlock();

            float f = 1.0F;
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f + 1.62D - (double) player.yOffset;
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
            boolean flag = false;
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

            if (movingobjectposition == null)
            {
                return container;
            } else
            {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!world.canMineBlock(player, i, j, k))
                    {
                        return container;
                    }

                    if (movingobjectposition.sideHit == 0)
                    {
                        --j;
                    }

                    if (movingobjectposition.sideHit == 1)
                    {
                        ++j;
                    }

                    if (movingobjectposition.sideHit == 2)
                    {
                        --k;
                    }

                    if (movingobjectposition.sideHit == 3)
                    {
                        ++k;
                    }

                    if (movingobjectposition.sideHit == 4)
                    {
                        --i;
                    }

                    if (movingobjectposition.sideHit == 5)
                    {
                        ++i;
                    }

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, container))
                    {
                        return container;
                    }

                    if (this.tryPlaceContainedLiquid(world, fluidBlock, d0, d1, d2, i, j, k) && !player.capabilities.isCreativeMode)
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

    public boolean tryPlaceContainedLiquid(World par1World, Block block, double par2, double par4, double par6, int par8, int par9, int par10)
    {
        if (!par1World.isAirBlock(par8, par9, par10) && par1World.getBlock(par8, par9, par10).func_149730_j())
        {
            return false;
        } else if ((par1World.getBlock(par8, par9, par10).getMaterial() instanceof MaterialLiquid && (par1World.getBlockMetadata(par8, par9, par10) == 0)))
        {
            return false;
        } else
        {
            if ((block == Blocks.water || block == Blocks.flowing_water) && par1World.provider.isHellWorld)
            {
                par1World.playSoundEffect(par2 + 0.5D, par4 + 0.5D, par6 + 0.5D, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l)
                {
                    par1World.spawnParticle("largesmoke", (double) par8 + Math.random(), (double) par9 + Math.random(), (double) par10 + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else
            {
                par1World.setBlock(par8, par9, par10, block, 0, 3);
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
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f + 1.62D - (double) player.yOffset;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        boolean flag = false;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null)
        {
            return container;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                TileEntity tile = world.getTileEntity(i, j, k);

                if (tile instanceof IFluidHandler)
                {
                    int amount = ((IFluidHandler) tile).fill(ForgeDirection.getOrientation(movingobjectposition.sideHit), fluid, true);

                    this.drain(container, amount, true);
                }
            }
        }

        return container;
    }

    public ItemStack drainSelectedTank(ItemStack container, World world, EntityPlayer player)
    {
        float f = 1.0F;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f + 1.62D - (double) player.yOffset;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        boolean flag = false;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null)
        {
            return container;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                TileEntity tile = world.getTileEntity(i, j, k);

                if (tile instanceof IFluidHandler)
                {
                    FluidStack fluidAmount = ((IFluidHandler) tile).drain(ForgeDirection.getOrientation(movingobjectposition.sideHit), this.getCapacity(container), false);

                    int amount = this.fill(container, fluidAmount, false);

                    if (amount > 0)
                    {
                        ((IFluidHandler) tile).drain(ForgeDirection.getOrientation(movingobjectposition.sideHit), this.getCapacity(container), true);

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
