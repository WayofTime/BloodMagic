package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.iface.IDemonWillViewer;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemDemonWillGauge extends Item implements IVariantProvider, IDemonWillViewer {
    public ItemDemonWillGauge() {
        setUnlocalizedName(BloodMagic.MODID + ".willGauge");
        setMaxStackSize(1);
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.willGauge"))));
    }

    @Override
    public void populateVariants(Int2ObjectMap<String> variants) {
        variants.put(0, "type=willgauge");
    }

    @Override
    public boolean canSeeDemonWillAura(World world, ItemStack stack, EntityPlayer player) {
        return true;
    }

    @Override
    public int getDemonWillAuraResolution(World world, ItemStack stack, EntityPlayer player) {
        return 100;
    }
}
