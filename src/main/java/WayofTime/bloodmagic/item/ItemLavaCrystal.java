package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLavaCrystal extends ItemBindableBase implements IVariantProvider {

    public ItemLavaCrystal() {
        super();
        setUnlocalizedName(BloodMagic.MODID + ".lavaCrystal");
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        Binding binding = getBinding(stack);
        if (binding != null)
            NetworkHelper.getSoulNetwork(binding.getOwnerId()).syphon(25);

        ItemStack returnStack = new ItemStack(this);
        returnStack.setTagCompound(stack.getTagCompound());
        return returnStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBurnTime(ItemStack stack) {
        Binding binding = getBinding(stack);
        if (binding == null)
            return -1;

        if (NetworkHelper.canSyphonFromContainer(stack, 25))
            return 200;
        else {
            EntityPlayer player = PlayerHelper.getPlayerFromUUID(binding.getOwnerId());
            if (player != null)
                player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 99));
        }

        return -1;
    }

    @Nullable
    @Override
    public Binding getBinding(ItemStack stack) {
        return Binding.fromStack(stack);
    }

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> ret = Lists.newArrayList();
        ret.add(Pair.of(0, "type=normal"));
        return ret;
    }
}
