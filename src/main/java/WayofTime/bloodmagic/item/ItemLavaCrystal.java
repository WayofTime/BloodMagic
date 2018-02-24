package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ItemLavaCrystal extends ItemBindableBase implements IVariantProvider {
    public ItemLavaCrystal() {
        super();
        setUnlocalizedName(BloodMagic.MODID + ".lavaCrystal");
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        String uuid = getOwnerUUID(itemStack);
        if (!Strings.isNullOrEmpty(uuid))
            NetworkHelper.getSoulNetwork(uuid).syphon(25);

        ItemStack returnStack = new ItemStack(this);
        returnStack.setTagCompound(itemStack.getTagCompound());
        return returnStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getItemBurnTime(ItemStack stack) {
        if (Strings.isNullOrEmpty(getOwnerUUID(stack)))
            return -1;

        if (NetworkHelper.canSyphonFromContainer(stack, 25))
            return 200;
        else {
            EntityPlayer player = PlayerHelper.getPlayerFromUUID(getOwnerUUID(stack));
            if (player != null)
                player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 99));
        }

        return -1;
    }

    @Override
    public String getOwnerName(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getString(Constants.NBT.OWNER_NAME) : null;
    }

    @Override
    public String getOwnerUUID(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getString(Constants.NBT.OWNER_UUID) : null;
    }

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> ret = Lists.newArrayList();
        ret.add(Pair.of(0, "type=normal"));
        return ret;
    }
}
