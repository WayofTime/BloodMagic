package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.BoundToolEvent;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.base.Strings;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBoundSword extends ItemSword
{
    private int lpUsed;

    public ItemBoundSword()
    {
        super(ModItems.boundToolMaterial);

        setUnlocalizedName(Constants.Mod.MODID + ".bound.sword");
        setHasSubtypes(true);
        setNoRepair();
        setCreativeTab(BloodMagic.tabBloodMagic);
        this.lpUsed = 50;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
    {

        if (!playerIn.isSneaking() && getActivated(stack))
        {
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            BoundToolEvent.Release event = new BoundToolEvent.Release(playerIn, stack, i);
            if (MinecraftForge.EVENT_BUS.post(event))
                return;

            i = event.charge;

            // TODO Make conical charge blast
            Explosion explosion = new Explosion(worldIn, playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, (float) (i * 0.5), true, true);

            ItemBindable.syphonNetwork(stack, playerIn, (int) (i * i * i / 2.7));

            if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(worldIn, explosion))
                return;
            explosion.doExplosionA();
            explosion.doExplosionB(true);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {

        BindableHelper.checkAndSetItemOwner(stack, player);

        // if (!world.isRemote)
        {
            if (player.isSneaking())
                setActivated(stack, !getActivated(stack));
            if (getActivated(stack) && ItemBindable.syphonNetwork(stack, player, lpUsed))
                return stack;

            if (!player.isSneaking() && getActivated(stack))
            {
                BoundToolEvent.Charge event = new BoundToolEvent.Charge(player, stack);
                if (MinecraftForge.EVENT_BUS.post(event))
                    return event.result;

                player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
            }
        }

        return stack;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (getActivated(stack))
        {
            if (target != null || attacker != null)
            {
                if (target instanceof EntityLiving)
                {
                    if (!target.isPotionActive(Potion.weakness))
                        target.addPotionEffect(new PotionEffect(Potion.weakness.id, 60, 2));
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        return stack;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        NBTHelper.checkNBT(stack);

        if (StatCollector.canTranslate("tooltip.BloodMagic.bound.sword.desc"))
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.bound.sword.desc"));
        if (getActivated(stack))
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.activated"));
        else
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.deactivated"));

        if (!Strings.isNullOrEmpty(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID)))
            tooltip.add(TextHelper.getFormattedText(String.format(StatCollector.translateToLocal("tooltip.BloodMagic.currentOwner"), stack.getTagCompound().getString(Constants.NBT.OWNER_UUID))));
    }

    private boolean getActivated(ItemStack stack)
    {
        return stack.getItemDamage() > 0;
    }

    private ItemStack setActivated(ItemStack stack, boolean activated)
    {
        stack.setItemDamage(activated ? 1 : 0);

        return stack;
    }
}
