package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.IReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IReagentManipulator;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class DivinationSigil extends Item implements ArmourUpgrade, IReagentManipulator, IBindable
{
    public DivinationSigil()
    {
        super();
        this.maxStackSize = 1;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DivinationSigil");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.divinationsigil.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.divinationsigil.desc2"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (!EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.worldObj.isRemote)
        {
            return par1ItemStack;
        }
        
        if(!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, 0))
        {
        	return par1ItemStack;
        }

        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        if (itemTag == null || itemTag.getString("ownerName").equals(""))
        {
            return par1ItemStack;
        }

        String ownerName = itemTag.getString("ownerName");
        int currentEssence = EnergyItems.getCurrentEssence(ownerName);

        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

        if (movingobjectposition == null)
        {
            par3EntityPlayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("message.divinationsigil.currentessence") + " " + EnergyItems.getCurrentEssence(ownerName) + "LP"));

            return par1ItemStack;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;

                TileEntity tile = par2World.getTileEntity(x, y, z);

                if (!(tile instanceof IReagentHandler))
                {
                    par3EntityPlayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("message.divinationsigil.currentessence") + " " + EnergyItems.getCurrentEssence(ownerName) + "LP"));

                    return par1ItemStack;
                }

                IReagentHandler relay = (IReagentHandler) tile;

                ReagentContainerInfo[] infoList = relay.getContainerInfo(ForgeDirection.UNKNOWN);
                if (infoList != null)
                {
                    for (ReagentContainerInfo info : infoList)
                    {
                        if (info != null && info.reagent != null && info.reagent.reagent != null)
                        {
                            par3EntityPlayer.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.divinationsigil.reagent") + " " + ReagentRegistry.getKeyForReagent(info.reagent.reagent) + "," + StatCollector.translateToLocal("message.divinationsigil.amount") + " " + info.reagent.amount));
                        }
                    }
                }
            }
        }

        return par1ItemStack;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 25;
    }
}
