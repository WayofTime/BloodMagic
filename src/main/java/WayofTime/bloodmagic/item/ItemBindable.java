package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBindable extends Item implements IBindable {

    private int energyUsed;

    public ItemBindable() {
        super();

        setCreativeTab(BloodMagic.tabBloodMagic);
        setMaxStackSize(1);
    }

    public static boolean syphonBatteries(ItemStack stack, EntityPlayer player, int damageToBeDone) {
        if (!player.worldObj.isRemote) {
            return NetworkHelper.syphonAndDamage(NetworkHelper.getSoulNetwork(BindableHelper.getOwnerName(stack), player.worldObj), damageToBeDone);
        } else {
            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;

//            SpellHelper.sendIndexedParticleToAllAround(player.worldObj, posX, posY, posZ, 20, player.worldObj.provider.getDimensionId(), 4, posX, posY, posZ);
            player.worldObj.playSoundEffect((double) ((float) player.posX + 0.5F), (double) ((float) player.posY + 0.5F), (double) ((float) player.posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.8F);
        }
        return true;
    }

    public static void hurtPlayer(EntityPlayer user, int energySyphoned) {
        if (energySyphoned < 100 && energySyphoned > 0) {
            if (!user.capabilities.isCreativeMode) {
                user.attackEntityFrom(BloodMagicAPI.getDamageSource(), 0F); // Emulate an attack
                user.setHealth(user.getHealth() - 1);

                if (user.getHealth() <= 0.0005f)
                    user.onDeath(BloodMagicAPI.getDamageSource());
            }
        } else if (energySyphoned >= 100) {
            if (!user.capabilities.isCreativeMode) {
                for (int i = 0; i < ((energySyphoned + 99) / 100); i++) {
                    user.attackEntityFrom(BloodMagicAPI.getDamageSource(), 0F); // Emulate an attack
                    user.setHealth(user.getHealth() - 1);

                    if (user.getHealth() <= 0.0005f) {
                        user.onDeath(BloodMagicAPI.getDamageSource());
                        break;
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        NBTHelper.checkNBT(stack);

        if (!Strings.isNullOrEmpty(stack.getTagCompound().getString(Constants.NBT.OWNER_NAME)))
            tooltip.add(TextHelper.getFormattedText(String.format(StatCollector.translateToLocal("tooltip.BloodMagic.currentOwner"), stack.getTagCompound().getString(Constants.NBT.OWNER_NAME))));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        BindableHelper.checkAndSetItemOwner(stack, player);

        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        BindableHelper.checkAndSetItemOwner(stack, player);

        return false;
    }

    protected void damagePlayer(World world, EntityPlayer player, int damage) {
        if (world != null) {
            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;
            world.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            float f = 1.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;
            for (int l = 0; l < 8; ++l)
                world.spawnParticle(EnumParticleTypes.REDSTONE, posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), f1, f2, f3);
        }
        for (int i = 0; i < damage; i++) {
            player.attackEntityFrom(BloodMagicAPI.getDamageSource(), 0F); // Emulate an attack
            player.setHealth(player.getHealth() - 1);

            if (player.getHealth() <= 0.0005) {
                player.inventory.dropAllItems();
                break;
            }
        }
    }

    public int getEnergyUsed() {
        return this.energyUsed;
    }

    protected void setEnergyUsed(int energyUsed) {
        this.energyUsed = energyUsed;
    }

    public String getBindableOwner(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);

        return stack.getTagCompound().getString(Constants.NBT.OWNER_NAME);
    }

    // IBindable

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack) {
        return true;
    }
}
