package WayofTime.bloodmagic.item.gear;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.altar.IAltarManipulator;
import WayofTime.bloodmagic.altar.IBloodAltar;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.iface.IItemLPContainer;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.ItemHelper.LPContainer;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemPackSelfSacrifice extends ItemArmor implements IAltarManipulator, IItemLPContainer, IVariantProvider {
    /**
     * How much LP per half heart
     */
    public final int CONVERSION = 100;
    /**
     * Max LP storage
     */
    public final int CAPACITY = 10000;
    /**
     * How often the pack syphons
     */
    public final int INTERVAL = 20;
    /**
     * How much health is required for the pack to syphon (0 - 1)
     */
    public final float HEALTHREQ = 0.5f;

    public ItemPackSelfSacrifice() {
        super(ArmorMaterial.CHAIN, 0, EntityEquipmentSlot.CHEST);

        setUnlocalizedName(BloodMagic.MODID + ".pack.selfSacrifice");
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote)
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        RayTraceResult position = this.rayTrace(world, player, false);

        if (position == null) {
            return super.onItemRightClick(world, player, EnumHand.MAIN_HAND);
        } else {
            if (position.typeOfHit == RayTraceResult.Type.BLOCK) {
                TileEntity tile = world.getTileEntity(position.getBlockPos());

                if (!(tile instanceof IBloodAltar))
                    return super.onItemRightClick(world, player, EnumHand.MAIN_HAND);

                LPContainer.tryAndFillAltar((IBloodAltar) tile, stack, world, position.getBlockPos());
            }
        }

        return ActionResult.newResult(EnumActionResult.FAIL, stack);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (world.isRemote || player.capabilities.isCreativeMode)
            return;

        boolean shouldSyphon = player.getHealth() / player.getMaxHealth() > HEALTHREQ && getStoredLP(stack) < CAPACITY;

        if (shouldSyphon & world.getTotalWorldTime() % INTERVAL == 0) {
            NetworkHelper.getSoulNetwork(player).hurtPlayer(player, 1.0F);
            LPContainer.addLPToItem(stack, CONVERSION, CAPACITY);
        }

        if (getStoredLP(stack) > CAPACITY)
            setStoredLP(stack, CAPACITY);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;
        list.add(TextHelper.localize("tooltip.bloodmagic.pack.selfSacrifice.desc"));
        list.add(TextHelper.localize("tooltip.bloodmagic.pack.stored", getStoredLP(stack)));
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=normal");
    }

    // IFillable

    @Override
    public int getCapacity() {
        return this.CAPACITY;
    }

    @Override
    public int getStoredLP(ItemStack stack) {
        return stack != null ? NBTHelper.checkNBT(stack).getTagCompound().getInteger(Constants.NBT.STORED_LP) : 0;
    }

    @Override
    public void setStoredLP(ItemStack stack, int lp) {
        if (stack != null) {
            NBTHelper.checkNBT(stack).getTagCompound().setInteger(Constants.NBT.STORED_LP, lp);
        }
    }
}
