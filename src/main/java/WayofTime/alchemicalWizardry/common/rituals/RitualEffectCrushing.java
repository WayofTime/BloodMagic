package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

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
        World world = ritualStone.getWorld();

        if (world.getWorldTime() % 10 != 5)
        {
            return;
        }

        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();
        TileEntity tile = world.getTileEntity(x, y + 1, z);
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

        boolean isSilkTouch = hasCrystallos;
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
                        Block block = world.getBlock(x + i, y + j, z + k);
                        int meta = world.getBlockMetadata(x + i, y + j, z + k);
                        if(block.getBlockHardness(world, x + i, y + j, z + k) == -1)
                        {
                        	continue;
                        }

                        if (block != null && !world.isAirBlock(x + i, y + j, z + k))
                        {
                            if ((block.equals(ModBlocks.ritualStone) || block.equals(ModBlocks.blockMasterStone)) || SpellHelper.isBlockFluid(block))
                            {
                                continue;
                            }

                            if (isSilkTouch && block.canSilkHarvest(world, null, x + i, y + j, z + k, meta))
                            {
                                ItemStack item = new ItemStack(block, 1, meta);
                                ItemStack copyStack = item.copyItemStack(item);

                                SpellHelper.insertStackIntoInventory(copyStack, tileEntity);

                                if (copyStack.stackSize > 0)
                                {
                                    world.spawnEntityInWorld(new EntityItem(world, x + 0.4, y + 2, z + 0.5, copyStack));
                                }

                                if (hasCrystallos)
                                {
                                    this.canDrainReagent(ritualStone, ReagentRegistry.crystallosReagent, crystallosDrain, true);
                                }
                            } else
                            {
                                ArrayList<ItemStack> itemDropList = block.getDrops(world, x + i, y + j, z + k, meta, fortuneLevel);

                                if (itemDropList != null)
                                {
                                    int invSize = tileEntity.getSizeInventory();

                                    for (ItemStack item : itemDropList)
                                    {
                                        hasIncendium = hasIncendium && this.canDrainReagent(ritualStone, ReagentRegistry.incendiumReagent, incendiumDrain, false);
                                        ItemStack copyStack = item.copyItemStack(item);

                                        if (this.usesIncendium(copyStack))
                                        {
                                            copyStack = this.transformToNewItem(copyStack, hasIncendium, false);
                                            this.canDrainReagent(ritualStone, ReagentRegistry.incendiumReagent, incendiumDrain, true);
                                        }

                                        SpellHelper.insertStackIntoInventory(copyStack, tileEntity);
                                        if (copyStack.stackSize > 0)
                                        {
                                            world.spawnEntityInWorld(new EntityItem(world, x + 0.4, y + 2, z + 0.5, copyStack));
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
                            }
                            world.setBlockToAir(x + i, y + j, z + k);
                            world.playSoundEffect(x + i, y + j, z + k, "mob.endermen.portal", 1.0F, 1.0F);

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
                Block block = ((ItemBlock) item).field_150939_a;

                if (block == Blocks.cobblestone || block == Blocks.stone)
                {
                    return true;
                }
            } else
            {

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
                Block block = ((ItemBlock) item).field_150939_a;

                if (hasIncendium)
                {
                    if (block == Blocks.cobblestone || block == Blocks.stone)
                    {
                        copyStack = new ItemStack(Blocks.netherrack, stackSize, 0);
                    }
                }
            } else
            {

            }

            return copyStack;
        }
        return stack;
    }

    public boolean isSilkTouch(World world, int x, int y, int z)
    {
        int index = 0;
        for (int i = -2; i <= 2; i++)
        {
            for (int j = -2; j <= 2; j++)
            {
                int index1 = Math.abs(i);
                int index2 = Math.abs(j);

                if ((index1 == 2 && (index2 == 2 || index2 == 1)) || (index1 == 1 && index2 == 2))
                {
                    Block block = world.getBlock(x + i, y + 1, z + j);
                    if (block == Blocks.gold_block)
                    {
                        index++;
                    }
                }
            }
        }

        return index >= 12;
    }

    public int getFortuneLevel(World world, int x, int y, int z)
    {
        int index = 0;
        for (int i = -2; i <= 2; i++)
        {
            for (int j = -2; j <= 2; j++)
            {
                int index1 = Math.abs(i);
                int index2 = Math.abs(j);

                if ((index1 == 2 && (index2 == 2 || index2 == 1)) || (index1 == 1 && index2 == 2))
                {
                    Block block = world.getBlock(x + i, y + 1, z + j);
                    if (block == Blocks.emerald_block || block == Blocks.diamond_block)
                    {
                        index++;
                    }
                }
            }
        }

        if (index >= 12)
        {
            return 3;
        } else if (index >= 8)
        {
            return 2;
        } else if (index >= 4)
        {
            return 1;
        }

        return 0;
    }

    @Override
    public int getCostPerRefresh()
    {
        return 7;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> crushingRitual = new ArrayList();
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
