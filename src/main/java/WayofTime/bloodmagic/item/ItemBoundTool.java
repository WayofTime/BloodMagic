package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.event.BoundToolEvent;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.Multiset;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

@Getter
public class ItemBoundTool extends ItemBindable {

    private Set<Block> effectiveBlocks;
    protected final String tooltipBase;
    private final String name;

    public boolean beingHeldDown;
    private int heldDownCount;

    public final int chargeTime = 30;

    public ItemBoundTool(String name, int lpUsed, Set<Block> effectiveBlocks) {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".bound." + name);
        setHasSubtypes(true);
        setLPUsed(lpUsed);
        setFull3D();

        this.name = name;
        this.tooltipBase = "tooltip.BloodMagic.bound." + name + ".";
        this.effectiveBlocks = effectiveBlocks;
    }

    public ItemBoundTool(String name, int lpUsed) {
        this(name, lpUsed, null);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        return this.effectiveBlocks.contains(block) ? 8.0F : 1.0F;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer && getActivated(stack) && isSelected && isBeingHeldDown() && stack == ((EntityPlayer) entityIn).getCurrentEquippedItem()) {
            EntityPlayer player = (EntityPlayer) entityIn;
            heldDownCount = Math.min(player.getItemInUseDuration(), chargeTime);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        BindableHelper.checkAndSetItemOwner(stack, player);

//        if (!world.isRemote)
        {
            if (player.isSneaking())
                setActivated(stack, !getActivated(stack));
//            if (getActivated(stack) && ItemBindable.syphonBatteries(stack, player, getLPUsed()))
//                return stack;

            if (!player.isSneaking() && getActivated(stack)) {
                BoundToolEvent.Charge event = new BoundToolEvent.Charge(player, stack);
                if (MinecraftForge.EVENT_BUS.post(event))
                    return event.result;

                player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
                beingHeldDown = true;
            }
        }

        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (BindableHelper.checkAndSetItemOwner(stack, player) && ItemBindable.syphonBatteries(stack, player, getLPUsed()))
            return false;

        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        this.onItemRightClick(stack, world, player);

        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
        if (!playerIn.isSneaking() && getActivated(stack)) {
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            BoundToolEvent.Release event = new BoundToolEvent.Release(playerIn, stack, i);
            if (MinecraftForge.EVENT_BUS.post(event))
                return;

            i = event.charge;

            onBoundRelease(stack, worldIn, playerIn, Math.min(i, chargeTime));
            beingHeldDown = false;
        }
    }

    protected void onBoundRelease(ItemStack stack, World world, EntityPlayer player, int charge) {

    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getItemEnchantability() {
        return 50;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {

        if (StatCollector.canTranslate(tooltipBase + "desc"))
            tooltip.add(TextHelper.localizeEffect(tooltipBase + "desc"));
        if (getActivated(stack))
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.activated"));
        else
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.deactivated"));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getActivated(stack) && beingHeldDown;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return ((double) -Math.min(getHeldDownCount(), chargeTime) / chargeTime) + 1;
    }

    protected static void dropStacks(Multiset<ItemStackWrapper> drops, World world, BlockPos posToDrop) {
        for (Multiset.Entry<ItemStackWrapper> entry : drops.entrySet()) {
            int count = entry.getCount();
            ItemStackWrapper stack = entry.getElement();
            int maxStackSize = stack.item.getItemStackLimit(stack.toStack(1));

            while (count >= maxStackSize) {
                world.spawnEntityInWorld(new EntityItem(world, posToDrop.getX(), posToDrop.getY(), posToDrop.getZ(), stack.toStack(maxStackSize)));
                count -= maxStackSize;
            }

            if (count > 0)
                world.spawnEntityInWorld(new EntityItem(world, posToDrop.getX(), posToDrop.getY(), posToDrop.getZ(), stack.toStack(count)));
        }
    }

    public boolean getActivated(ItemStack stack) {
        return stack.getItemDamage() > 0;
    }

    public ItemStack setActivated(ItemStack stack, boolean activated) {
        stack.setItemDamage(activated ? 1 : 0);

        return stack;
    }
}
