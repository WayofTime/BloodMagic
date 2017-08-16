package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.impl.ItemSigil;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Strings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemSigilBase extends ItemSigil implements IVariantProvider {
    protected final String tooltipBase;
    private final String name;

    public ItemSigilBase(String name, int lpUsed) {
        super(lpUsed);

        setUnlocalizedName(BloodMagic.MODID + ".sigil." + name);
        setCreativeTab(BloodMagic.TAB_BM);

        this.name = name;
        this.tooltipBase = "tooltip.bloodmagic.sigil." + name + ".";
    }

    public ItemSigilBase(String name) {
        this(name, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (TextHelper.canTranslate(tooltipBase + "desc"))
            tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect(tooltipBase + "desc"))));

        if (!stack.hasTagCompound())
            return;

        if (!Strings.isNullOrEmpty(getOwnerName(stack)))
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentOwner", PlayerHelper.getUsernameFromStack(stack)));

        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(Pair.of(0, "type=normal"));
        return ret;
    }

    public String getName() {
        return name;
    }
}
