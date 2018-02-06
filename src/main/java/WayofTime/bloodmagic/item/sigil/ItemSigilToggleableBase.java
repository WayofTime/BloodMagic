package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.apibutnotreally.impl.ItemSigilToggleable;
import WayofTime.bloodmagic.apibutnotreally.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.mesh.CustomMeshDefinitionActivatable;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Strings;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemSigilToggleableBase extends ItemSigilToggleable implements IMeshProvider {
    protected final String tooltipBase;
    private final String name;

    public ItemSigilToggleableBase(String name, int lpUsed) {
        super(lpUsed);

        setUnlocalizedName(BloodMagic.MODID + ".sigil." + name);
        setCreativeTab(BloodMagic.TAB_BM);

        this.name = name;
        this.tooltipBase = "tooltip.bloodmagic.sigil." + name + ".";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        if (!stack.hasTagCompound())
            return;
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic." + (getActivated(stack) ? "activated" : "deactivated")));

        if (!Strings.isNullOrEmpty(getOwnerName(stack)))
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.currentOwner", PlayerHelper.getUsernameFromStack(stack)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition() {
        return new CustomMeshDefinitionActivatable("sigil_" + name.toLowerCase(Locale.ROOT));
    }

    @Nullable
    @Override
    public ResourceLocation getCustomLocation() {
        return null;
    }

    @Override
    public List<String> getVariants() {
        List<String> ret = new ArrayList<String>();
        ret.add("active=false");
        ret.add("active=true");

        return ret;
    }
}
