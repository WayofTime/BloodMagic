package WayofTime.bloodmagic.item.sigil.sigil;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IMeshProvider;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSigil extends Item implements IBindable, IMeshProvider {

    private final ISigil sigil;

    public ItemSigil(ISigil sigil, String name) {
        this.sigil = sigil;

        setCreativeTab(BloodMagic.TAB_BM);
        setUnlocalizedName(BloodMagic.MODID + ".sigil." + name);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));

        ItemStack sigilStack = getSigilStack(player, hand);
        if (getOwnerUUID(sigilStack) == null)
            return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));

        if (sigil instanceof ISigil.Toggle && player.isSneaking()) {
            boolean newState = toggleState(sigilStack);
            ((ISigil.Toggle) sigil).onToggle(newState, sigilStack, player, world, hand);
            return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }

        return ActionResult.newResult(sigil.onRightClick(sigilStack, player, world, hand), player.getHeldItem(hand));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (PlayerHelper.isFakePlayer(player))
            return EnumActionResult.FAIL;

        return sigil.onInteract(getSigilStack(player, hand), player, world, pos, facing, hand);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!(entity instanceof EntityPlayer) || PlayerHelper.isFakePlayer((EntityPlayer) entity))
            return;

        ItemStack sigilStack = stack;
        if (sigil instanceof ISigil.Holding) {
            ISigil.Holding holding = (ISigil.Holding) sigil;
            int current = holding.getEquippedSigil(stack);
            sigilStack = holding.getHeldSigils(stack).get(current);
        }

        if (sigil instanceof ISigil.Toggle && isActive(sigilStack)) {
            ((ISigil.Toggle) sigil).onUpdate(sigilStack, (EntityPlayer) entity, world, itemSlot, isSelected);
            if (entity.ticksExisted % 100 == 0)
                NetworkHelper.getSoulNetwork(getOwnerUUID(sigilStack)).syphonAndDamage((EntityPlayer) entity, sigil.getCost());
        }
    }

    @Override
    public String getOwnerName(ItemStack stack) {
        return sigil.getOwnerName(stack);
    }

    @Override
    public String getOwnerUUID(ItemStack stack) {
        return sigil.getOwnerUUID(stack);
    }

    public boolean toggleState(ItemStack stack) {
        if (!stack.hasTagCompound())
            return false;

        boolean newState = !isActive(stack);
        stack.getTagCompound().setBoolean("active", newState);
        return newState;
    }

    public boolean isActive(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("active");
    }

    // TODO - Need to get ISigil from holding stack
    public ItemStack getSigilStack(EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);

        if (sigil instanceof ISigil.Holding) {
            ISigil.Holding holding = (ISigil.Holding) sigil;
            int current = holding.getEquippedSigil(held);
            return holding.getHeldSigils(held).get(current);
        }

        return held;
    }

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack) {
        return sigil.onBind(player, stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemMeshDefinition getMeshDefinition() {
        return s -> {
            if (!(sigil instanceof ISigil.Toggle))
                return new ModelResourceLocation(s.getItem().getRegistryName(), "inventory");

            return new ModelResourceLocation(s.getItem().getRegistryName(), "active=" + isActive(s));
        };
    }

    @Override
    public List<String> getVariants() {
        if (sigil instanceof ISigil.Toggle) {
            return Lists.newArrayList(
                    "active=true",
                    "active=false"
            );
        } else return Lists.newArrayList("inventory");
    }

    @Nullable
    @Override
    public ResourceLocation getCustomLocation() {
        return null;
    }
}
