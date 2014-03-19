package WayofTime.alchemicalWizardry.common.items.thaumcraft;

import java.util.List;

import javax.swing.Icon;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class FocusBase extends EnergyItems //implements IWandFocus
{
    protected IIcon ornament, depth;

    public FocusBase()
    {
        super();
        setMaxDamage(1);
        setNoRepair();
        setMaxStackSize(1);
    }

    boolean hasOrnament()
    {
        return false;
    }

    boolean hasDepth()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
//        if(hasOrnament())
//        {
//             ornament = iconRegister.registerIcon("AlchemicalWizardry:" + this.getUnlocalizedName() + "Orn");
//        }
//        if(hasDepth())
//        {
//            depth = iconRegister.registerIcon("AlchemicalWizardry:" + this.getUnlocalizedName() + "Depth");
//        }
    }

    @Override
    public boolean isItemTool(ItemStack par1ItemStack)
    {
        return true;
    }

/*erride
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        AspectList cost = getVisCost();

        if (cost != null && cost.size() > 0)
        {
            par3List.add(StatCollector.translateToLocal(isVisCostPerTick() ? "item.Focus.cost2" : "item.Focus.cost1"));

            for (Aspect aspect : cost.getAspectsSorted())
            {
                float amount = cost.getAmount(aspect) / 100.0F;
                par3List.add(" " + '\u00a7' + aspect.getChatcolor() + aspect.getName() + '\u00a7' + "r x " + amount);
            }
        }
    }

    @Override
    public int getItemEnchantability()
    {
        return 5;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack)
    {
        return EnumRarity.rare;
    }

    @Override
    public Icon getOrnament()
    {
        return ornament;
    }

    @Override
    public Icon getFocusDepthLayerIcon()
    {
        return depth;
    }

    @Override
    public WandFocusAnimation getAnimation()
    {
        return WandFocusAnimation.WAVE;
    }

    @Override
    public boolean isVisCostPerTick()
    {
        return false;
    }

    public boolean isUseItem()
    {
        return isVisCostPerTick();
    }

    @Override
    public ItemStack onFocusRightClick(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer, MovingObjectPosition paramMovingObjectPosition)
    {
        if (isUseItem())
        {
            paramEntityPlayer.setItemInUse(paramItemStack, Integer.MAX_VALUE);
        }

        return paramItemStack;
    }

    @Override
    public void onUsingFocusTick(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, int paramInt)
    {
        // NO-OP
    }

    @Override
    public void onPlayerStoppedUsingFocus(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer, int paramInt)
    {
        // NO-OP
    }

    @Override
    public String getSortingHelper(ItemStack paramItemStack)
    {
        return "00";
    }

    @Override
    public boolean onFocusBlockStartBreak(ItemStack paramItemStack, int paramInt1, int paramInt2, int paramInt3, EntityPlayer paramEntityPlayer)
    {
        return false;
    }

    @Override
    public boolean acceptsEnchant(int id)
    {
        if (id == ThaumcraftApi.enchantFrugal ||
                id == ThaumcraftApi.enchantPotency)
        {
            return true;
        }

        return false;
    }
    
    */
}
