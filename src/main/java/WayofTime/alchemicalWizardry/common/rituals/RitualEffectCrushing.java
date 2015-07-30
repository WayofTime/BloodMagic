package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectCrushing extends RitualEffect
{
    public static final int crystallosDrain = 10;
    public static final int orbisTerraeDrain = 10;
    public static final int potentiaDrain = 10;
    public static final int virtusDrain = 10;
    public static final int incendiumDrain = 10;

    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorldObj();
        BlockPos pos = ritualStone.getPosition();

        if (world.getWorldTime() % 10 != 5)
        {
            return;
        }

        TileEntity tile = world.getTileEntity(pos.offsetUp());
        IInventory tileEntity;

        if (tile instanceof IInventory)
        {
            tileEntity = (IInventory) tile;
        } else
        {
            return;
        }

        if (tileEntity.getSizeInventory() <= 0)
        {
            return;
        }

        boolean hasRoom = false;
        for (int i = 0; i < tileEntity.getSizeInventory(); i++)
        {
            if (tileEntity.getStackInSlot(i) == null)
            {
                hasRoom = true;
                break;
            }
        }

        if (!hasRoom)
        {
            return; //Prevents overflow
        }

        boolean hasCrystallos = this.canDrainReagent(ritualStone, ReagentRegistry.crystallosReagent, crystallosDrain, false);
        boolean hasOrbisTerrae = this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, orbisTerraeDrain, false);
        boolean hasPotentia = this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);
        boolean hasVirtus = this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false);
        boolean hasIncendium = this.canDrainReagent(ritualStone, ReagentRegistry.incendiumReagent, incendiumDrain, false);

        int fortuneLevel = 0;
        if (hasOrbisTerrae)
        {
            fortuneLevel++;
        }
        if (hasPotentia)
        {
            fortuneLevel++;
        }
        if (hasVirtus)
        {
            fortuneLevel++;
        }

        if (currentEssence < this.getCostPerRefresh())
        {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else
        {
            for (int j = -3; j < 0; j++)
            {
                for (int i = -1; i <= 1; i++)
                {
                    for (int k = -1; k <= 1; k++)
                    {
                    	BlockPos newPos = pos.add(i, j, k);
                    	IBlockState state = world.getBlockState(newPos);
                        Block block = state.getBlock();
                        
                        if(block.getBlockHardness(world, newPos) == -1)
                        {
                        	continue;
                        }

                        if (!world.isAirBlock(newPos))
                        {
                            if ((block.equals(ModBlocks.ritualStone) || block.equals(ModBlocks.blockMasterStone)) || SpellHelper.isBlockFluid(block))
                            {
                                continue;
                            }

                            if (hasCrystallos && block.canSilkHarvest(world, newPos, state, null))
                            {
                                ItemStack item = new ItemStack(block, 1, block.getMetaFromState(state));
                                ItemStack copyStack = ItemStack.copyItemStack(item);

                                SpellHelper.insertStackIntoInventory(copyStack, tileEntity, EnumFacing.DOWN);

                                if (copyStack.stackSize > 0)
                                {
                                    world.spawnEntityInWorld(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5, copyStack));
                                }

                                if (hasCrystallos)
                                {
                                    this.canDrainReagent(ritualStone, ReagentRegistry.crystallosReagent, crystallosDrain, true);
                                }
                            } else
                            {
                                List<ItemStack> itemDropList = block.getDrops(world, newPos, state, fortuneLevel);

                                if (itemDropList != null)
                                {
                                    int invSize = tileEntity.getSizeInventory();

                                    for (ItemStack item : itemDropList)
                                    {
                                        hasIncendium = hasIncendium && this.canDrainReagent(ritualStone, ReagentRegistry.incendiumReagent, incendiumDrain, false);
                                        ItemStack copyStack = ItemStack.copyItemStack(item);

                                        if (this.usesIncendium(copyStack))
                                        {
                                            copyStack = this.transformToNewItem(copyStack, hasIncendium, false);
                                            this.canDrainReagent(ritualStone, ReagentRegistry.incendiumReagent, incendiumDrain, true);
                                        }

                                        SpellHelper.insertStackIntoInventory(copyStack, tileEntity, EnumFacing.DOWN);
                                        if (copyStack.stackSize > 0)
                                        {
                                            world.spawnEntityInWorld(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5, copyStack));
                                        }  
                                    }
                                    
                                    if (hasOrbisTerrae)
                                    {
                                        this.canDrainReagent(ritualStone, ReagentRegistry.orbisTerraeReagent, orbisTerraeDrain, true);
                                    }
                                    if (hasPotentia)
                                    {
                                        this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, true);
                                    }
                                    if (hasVirtus)
                                    {
                                        this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, true);
                                    }
                                }
                            }
                            world.setBlockToAir(newPos);
                            world.playSoundEffect(newPos.getX(), newPos.getY(), newPos.getZ(), "mob.endermen.portal", 1.0F, 1.0F);

                            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());

                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean usesIncendium(ItemStack stack)
    {
        if (stack != null)
        {
            Item item = stack.getItem();
            if (item instanceof ItemBlock)
            {
                Block block = ((ItemBlock) item).getBlock();

                if (block == Blocks.cobblestone || block == Blocks.stone)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private ItemStack transformToNewItem(ItemStack stack, boolean hasIncendium, boolean hasCrepitous)
    {
        if (stack != null)
        {
            ItemStack copyStack = ItemStack.copyItemStack(stack);
            int stackSize = copyStack.stackSize;

            Item item = stack.getItem();
            if (item instanceof ItemBlock)
            {
                Block block = ((ItemBlock) item).getBlock();

                if (hasIncendium)
                {
                    if (block == Blocks.cobblestone || block == Blocks.stone)
                    {
                        copyStack = new ItemStack(Blocks.netherrack, stackSize, 0);
                    }
                }
            }

            return copyStack;
        }
        return stack;
    }

    @Override
    public int getCostPerRefresh()
    {
        return 7;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> crushingRitual = new ArrayList<RitualComponent>();
        crushingRitual.add(new RitualComponent(0, 0, 1, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(1, 0, 0, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(0, 0, -1, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.EARTH));
        crushingRitual.add(new RitualComponent(2, 0, 0, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(0, 0, 2, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(0, 0, -2, RitualComponent.FIRE));
        crushingRitual.add(new RitualComponent(2, 0, 2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(2, 0, -2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.DUSK));
        crushingRitual.add(new RitualComponent(2, 1, 0, RitualComponent.AIR));
        crushingRitual.add(new RitualComponent(-2, 1, 0, RitualComponent.AIR));
        crushingRitual.add(new RitualComponent(0, 1, 2, RitualComponent.AIR));
        crushingRitual.add(new RitualComponent(0, 1, -2, RitualComponent.AIR));
        return crushingRitual;
    }
}
