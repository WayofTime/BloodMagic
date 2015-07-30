package WayofTime.alchemicalWizardry.common.items.energy;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ISegmentedReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.api.items.interfaces.IReagentManipulator;

public class AlchemicalSegmenter extends Item implements IReagentManipulator
{
    public AlchemicalSegmenter()
    {
        super();
        this.hasSubtypes = true;
        setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        Reagent reagent = this.getReagent(stack);
        String name = super.getItemStackDisplayName(stack);
        if (reagent != null)
        {
            name = name + " (" + reagent.name + ")";
        }
        return name;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.tanksegmenter.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.tanksegmenter.desc2"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            Reagent reagent = this.getReagent(par1ItemStack);
            if (reagent != null)
            {
                par3List.add(StatCollector.translateToLocal("tooltip.reagent.selectedreagent") + " " + reagent.name);
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return itemStack;
        }
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (movingobjectposition == null)
        {
            return itemStack;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                BlockPos pos = movingobjectposition.func_178782_a();
                TileEntity tile = world.getTileEntity(pos);
                if (!(tile instanceof ISegmentedReagentHandler))
                {
                    return itemStack;
                }
                ISegmentedReagentHandler reagentHandler = (ISegmentedReagentHandler) tile;

                if (player.isSneaking())
                {
                    ReagentContainerInfo[] infos = reagentHandler.getContainerInfo(EnumFacing.UP);
                    if (infos != null)
                    {
                        List<Reagent> reagentList = new LinkedList();
                        for (ReagentContainerInfo info : infos)
                        {
                            if (info != null)
                            {
                                ReagentStack reagentStack = info.reagent;
                                if (reagentStack != null)
                                {
                                    Reagent reagent = reagentStack.reagent;
                                    if (reagent != null)
                                    {
                                        reagentList.add(reagent);
                                    }
                                }
                            }
                        }
                        Reagent pastReagent = this.getReagent(itemStack);
                        boolean goForNext = false;
                        boolean hasFound = false;
                        for (Reagent reagent : reagentList)
                        {
                            if (goForNext)
                            {
                                goForNext = false;
                                break;
                            }

                            if (reagent == pastReagent)
                            {
                                goForNext = true;
                                hasFound = true;
                            }
                        }
                        if (hasFound)
                        {
                            if (goForNext)
                            {
                                this.setReagentWithNotification(itemStack, reagentList.get(0), player);
                            }
                        } else
                        {
                            if (reagentList.size() >= 1)
                            {
                                this.setReagentWithNotification(itemStack, reagentList.get(0), player);
                            }
                        }
                    }
                } else
                {
                    Reagent reagent = this.getReagent(itemStack);
                    if (reagent == null)
                    {
                        //TODO: Send message that "All are wiped"
                        reagentHandler.getAttunedTankMap().clear();
                        return itemStack;
                    }
                    int totalTankSize = reagentHandler.getNumberOfTanks();
                    int numberAssigned = reagentHandler.getTanksTunedToReagent(reagent) + 1;

                    if (numberAssigned > totalTankSize)
                    {
                        numberAssigned = 0;
                    }

                    player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.tanksegmenter.nowhas") + " " + numberAssigned + " " + StatCollector.translateToLocal("message.tanksegmenter.tankssetto") + " " + reagent.name));

                    reagentHandler.setTanksTunedToReagent(reagent, numberAssigned);
                }
            } else if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.MISS)
            {
                this.setReagent(itemStack, null);
            }
        }

        return itemStack;
    }

    public Reagent getReagent(ItemStack stack)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        return ReagentRegistry.getReagentForKey(tag.getString("reagent"));
    }

    public void setReagent(ItemStack stack, Reagent reagent)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setString("reagent", ReagentRegistry.getKeyForReagent(reagent));
    }

    public void setReagentWithNotification(ItemStack stack, Reagent reagent, EntityPlayer player)
    {
        this.setReagent(stack, reagent);

        if (reagent != null)
        {
            player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.tanksegmenter.setto") + " " + reagent.name));
        }
    }
}
