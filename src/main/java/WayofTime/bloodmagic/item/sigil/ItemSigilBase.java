package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ItemSigilBase extends ItemSigil implements IVariantProvider {

    protected final String tooltipBase;
    private final String name;

    public ItemSigilBase(String name, int lpUsed) {
        super(lpUsed);

        setTranslationKey(BloodMagic.MODID + ".sigil." + name);

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
            tooltip.addAll(Arrays.asList(WordUtils.wrap(TextHelper.localizeEffect(tooltipBase + "desc"), 30, "/cut", false).split("/cut")));

        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=normal");
    }

    public String getName() {
        return name;
    }
}
