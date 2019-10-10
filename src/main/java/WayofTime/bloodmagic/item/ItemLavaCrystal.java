package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO: Make some hook somewhere that attaches the pos to the ticket otherwise the tickets are basically useless lmao
public class ItemLavaCrystal extends ItemBindableBase implements IVariantProvider {

    public ItemLavaCrystal() {
        super();
        setTranslationKey(BloodMagic.MODID + ".lavaCrystal");
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
        if (bindingTag == null || bindingTag.getId() != 10 || bindingTag.isEmpty()) // Make sure it's both a tag compound and that it has actual data.
            return null;

        NBTTagCompound nbt = (NBTTagCompound) bindingTag;
        return new Binding(NBTUtil.getUUIDFromTag(nbt.getCompoundTag("id")), nbt.getString("name"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        pos = pos.offset(facing);
        ItemStack itemstack = player.getHeldItem(hand);

        Binding binding = getBinding(player.getHeldItem(hand));
        
        if (binding == null)
            return EnumActionResult.FAIL;
        
        if (!player.canPlayerEdit(pos, facing, itemstack))
            return EnumActionResult.FAIL;

        if (worldIn.isAirBlock(pos) && NetworkHelper.getSoulNetwork(binding).syphonAndDamage(player, SoulTicket.item(player.getHeldItem(hand), 100)).isSuccess()) {
            worldIn.playSound(player, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
            worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
        } else
            return EnumActionResult.FAIL;

        if (player instanceof EntityPlayerMP)
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, itemstack);

        return EnumActionResult.SUCCESS;
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=normal");
    }
}
