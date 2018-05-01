package WayofTime.bloodmagic.item.soulBreath;

import java.util.function.Consumer;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.mesh.CustomMeshDefinitionActivatable;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.iface.IActivatable;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.NBTHelper;

public class ItemFlightScroll extends ItemSoulBreathContainer implements IMeshProvider, IActivatable
{
    //TODO: A lot of this stuff could be moved to a toggle-able variant
    public ItemFlightScroll()
    {
        super();
        setUnlocalizedName(BloodMagic.MODID + ".icarusScroll");
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    public boolean getActivated(ItemStack stack)
    {
        return !stack.isEmpty() && NBTHelper.checkNBT(stack).getTagCompound().getBoolean(Constants.NBT.ACTIVATED);
    }

    @Override
    public ItemStack setActivatedState(ItemStack stack, boolean activated)
    {
        if (!stack.isEmpty())
        {
            NBTHelper.checkNBT(stack).getTagCompound().setBoolean(Constants.NBT.ACTIVATED, activated);
            return stack;
        }

        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote)
        {
            if (player.isSneaking())
                setActivatedState(stack, !getActivated(stack));
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote && entityIn instanceof EntityPlayerMP && getActivated(stack))
        {
            if (entityIn.ticksExisted % 100 == 0)
            {
                double drainNeeded = getBreathCostPerSecond(stack);
                if (this.drainBreath(stack, drainNeeded, false) >= drainNeeded)
                {
                    this.drainBreath(stack, drainNeeded, true);
                } else
                {
                    this.setActivatedState(stack, false);
                }
            }

            onEffectUpdate(stack, worldIn, (EntityPlayer) entityIn, itemSlot, isSelected);
        }
    }

    public void onEffectUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        player.addPotionEffect(new PotionEffect(RegistrarBloodMagic.FLIGHT, 2, 0));
    }

    @Override
    public int getMaxBreath(ItemStack stack)
    {
        return 20;
    }

    public double getBreathCostPerSecond(ItemStack stack)
    {
        return 0.01;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition()
    {
        return new CustomMeshDefinitionActivatable("icarus_scroll");
    }

    @Override
    public void gatherVariants(Consumer<String> variants)
    {
        variants.accept("active=false");
        variants.accept("active=true");
    }
}
