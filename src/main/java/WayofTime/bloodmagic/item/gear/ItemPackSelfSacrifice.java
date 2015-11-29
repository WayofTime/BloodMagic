package WayofTime.bloodmagic.item.gear;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.IAltarManipulator;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class ItemPackSelfSacrifice extends ItemArmor implements IAltarManipulator {

    public final int CONVERSION = 100; // How much LP per half heart
    public final int CAPACITY = 10000; // Max LP storage
    public final int INTERVAL = 20; // How often the pack syphons
    public final float HEALTHREQ = 0.5f; // How much health is required for the pack to syphon (0 - 1)

    public ItemPackSelfSacrifice() {
        super(ArmorMaterial.CHAIN, 0, 1);

        setUnlocalizedName(Constants.Mod.MODID + ".pack.selfSacrifice");
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote)
            return stack;

        MovingObjectPosition position = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (position == null) {
            return super.onItemRightClick(stack, world, player);
        } else {
            if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                TileEntity tile = world.getTileEntity(position.getBlockPos());

                if (!(tile instanceof TileAltar))
                    return super.onItemRightClick(stack, world, player);

                TileAltar altar = (TileAltar) tile;

                if (!altar.isActive()) {
                    int amount = this.getStoredLP(stack);

                    if (amount > 0) {
                        int filledAmount = altar.fillMainTank(amount);
                        amount -= filledAmount;
                        setStoredLP(stack, amount);
                        world.markBlockForUpdate(position.getBlockPos());
                    }
                }
            }
        }

        return stack;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (world.isRemote || player.capabilities.isCreativeMode)
            return;

        boolean shouldSyphon = player.getHealth() / player.getMaxHealth() > HEALTHREQ && getStoredLP(stack) < CAPACITY;

        if (shouldSyphon & world.getTotalWorldTime() % INTERVAL == 0) {
            NetworkHelper.hurtPlayer(player, 1.0F);
            addLP(stack, CONVERSION);
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return Constants.Mod.DOMAIN + "models/armor/bloodPack_layer_1.png";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        stack = NBTHelper.checkNBT(stack);
        list.add(TextHelper.localize("tooltip.BloodMagic.pack.selfSacrifice.desc"));
        list.add(TextHelper.localize("tooltip.BloodMagic.pack.stored", getStoredLP(stack)));
    }

    public void addLP(ItemStack stack, int toAdd) {
        stack = NBTHelper.checkNBT(stack);

        if (toAdd < 0)
            toAdd = 0;

        if (toAdd > CAPACITY)
            toAdd = CAPACITY;

        setStoredLP(stack, getStoredLP(stack) + toAdd);
    }

    public void setStoredLP(ItemStack stack, int lp) {
        stack = NBTHelper.checkNBT(stack);
        stack.getTagCompound().setInteger(Constants.NBT.STORED_LP, lp);
    }

    public int getStoredLP(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);
        return stack.getTagCompound().getInteger(Constants.NBT.STORED_LP);
    }
}
