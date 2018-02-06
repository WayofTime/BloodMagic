package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.apibutnotreally.util.helper.NetworkHelper;
import WayofTime.bloodmagic.apibutnotreally.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
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
        NetworkHelper.getSoulNetwork(this.getOwnerUUID(itemStack)).syphon(25);
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage());
        copiedStack.setCount(1);
        return copiedStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getItemBurnTime(ItemStack stack) {
        if (!stack.hasTagCompound())
            return -1;

        if (getOwnerUUID(stack) == null)
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
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> ret = Lists.newArrayList();
        ret.add(Pair.of(0, "type=normal"));
        return ret;
    }
}
