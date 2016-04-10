package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import com.google.common.base.Strings;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.EnumRitualReaderState;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemRitualReader extends Item implements IVariantProvider
{
    public static final String tooltipBase = "tooltip.BloodMagic.ritualReader.";

    public ItemRitualReader()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".ritualReader");
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        NBTHelper.checkNBT(stack);

        EnumRitualReaderState state = this.getState(stack);
        tooltip.add(TextHelper.localizeEffect(tooltipBase + "currentState", TextHelper.localizeEffect(tooltipBase + state.toString().toLowerCase())));

        tooltip.add("");

        boolean sneaking = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        if (sneaking)
        {
            tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect(tooltipBase + "desc." + state.toString().toLowerCase()))));
        } else
        {
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.extraInfo"));
        }

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (player.isSneaking() && !world.isRemote)
        {
            cycleReader(stack, player);

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }

    public void cycleReader(ItemStack stack, EntityPlayer player)
    {
        EnumRitualReaderState prevState = getState(stack);
        int val = prevState.ordinal();
        int nextVal = val + 1 >= EnumRitualReaderState.values().length ? 0 : val + 1;
        EnumRitualReaderState nextState = EnumRitualReaderState.values()[nextVal];

        setState(stack, nextState);
        notifyPlayerOfStateChange(nextState, player);
    }

    public void notifyPlayerOfStateChange(EnumRitualReaderState state, EntityPlayer player)
    {
        ChatUtil.sendNoSpam(player, new TextComponentTranslation(tooltipBase + "currentState", new TextComponentTranslation(tooltipBase + state.toString().toLowerCase())));
    }

    public void setState(ItemStack stack, EnumRitualReaderState state)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setInteger(Constants.NBT.RITUAL_READER, state.ordinal());
    }

    public EnumRitualReaderState getState(ItemStack stack)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
            return EnumRitualReaderState.INFORMATION;
        }

        NBTTagCompound tag = stack.getTagCompound();

        return EnumRitualReaderState.values()[tag.getInteger(Constants.NBT.RITUAL_READER)];
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=normal"));
        return ret;
    }
}
