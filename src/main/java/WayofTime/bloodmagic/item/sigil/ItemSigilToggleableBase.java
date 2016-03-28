package WayofTime.bloodmagic.item.sigil;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.impl.ItemSigilToggleable;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import com.google.common.base.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemSigilToggleableBase extends ItemSigilToggleable implements IVariantProvider
{
    protected final String tooltipBase;
    private final String name;

    public ItemSigilToggleableBase(String name, int lpUsed)
    {
        super(lpUsed);
        setToggleable();

        setUnlocalizedName(Constants.Mod.MODID + ".sigil." + name);
        setCreativeTab(BloodMagic.tabBloodMagic);

        this.name = name;
        this.tooltipBase = "tooltip.BloodMagic.sigil." + name + ".";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        NBTHelper.checkNBT(stack);
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic." + (getActivated(stack) ? "activated" : "deactivated")));

        if (!Strings.isNullOrEmpty(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID)))
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.currentOwner", PlayerHelper.getUsernameFromStack(stack)));
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "active=false"));
        ret.add(new ImmutablePair<Integer, String>(1, "active=true"));
        return ret;
    }
}
