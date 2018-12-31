package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO: Make some hook somewhere that attaches the pos to the ticket otherwise the tickets are basically useless lmao
public class ItemLavaCrystal extends ItemBindableBase implements IVariantProvider {

    public ItemLavaCrystal() {
        super();
        setUnlocalizedName(BloodMagic.MODID + ".lavaCrystal");
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        Binding binding = getBinding(stack);
        if (binding != null)
            NetworkHelper.getSoulNetwork(binding.getOwnerId()).syphon(SoulTicket.item(stack, 25));

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

        if (NetworkHelper.syphonFromContainer(stack, SoulTicket.item(stack, 25)))
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
        if (stack.getTagCompound() == null) // hasTagCompound doesn't work on empty stacks with tags
            return null;

        NBTBase bindingTag = stack.getTagCompound().getTag("binding");
        if (bindingTag == null || bindingTag.getId() != 10 || bindingTag.hasNoTags()) // Make sure it's both a tag compound and that it has actual data.
            return null;

        NBTTagCompound nbt = (NBTTagCompound) bindingTag;
        return new Binding(NBTUtil.getUUIDFromTag(nbt.getCompoundTag("id")),nbt.getString("name"));
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=normal");
    }
}
