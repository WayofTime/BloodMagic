package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.client.IVariantProvider;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.registry.ModItems;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ItemComponent extends Item implements IVariantProvider
{
    @Getter
    private static ArrayList<String> names = new ArrayList<String>();

    public static final String REAGENT_WATER = "reagentWater";
    public static final String REAGENT_LAVA = "reagentLava";
    public static final String REAGENT_AIR = "reagentAir";
    public static final String REAGENT_FASTMINER = "reagentFastMiner";
    public static final String REAGENT_VOID = "reagentVoid";
    public static final String REAGENT_GROWTH = "reagentGrowth";
    public static final String REAGENT_AFFINITY = "reagentAffinity";
    public static final String REAGENT_SIGHT = "reagentSight";
    public static final String REAGENT_BINDING = "reagentBinding";
    public static final String REAGENT_SUPPRESSION = "reagentSuppression";
    public static final String COMPONENT_FRAME_PART = "frameParts";
    public static final String REAGENT_BLOODLIGHT = "reagentBloodLight";
    public static final String REAGENT_MAGNETISM = "reagentMagnetism";
    public static final String REAGENT_HASTE = "reagentHaste";
    public static final String REAGENT_COMPRESSION = "reagentCompression";
    public static final String REAGENT_BRIDGE = "reagentBridge";
    public static final String REAGENT_SEVERANCE = "reagentSeverance";
    public static final String REAGENT_TELEPOSITION = "reagentTeleposition";
    public static final String REAGENT_TRANSPOSITION = "reagentTransposition";

    public ItemComponent()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".baseComponent.");
        setRegistryName(Constants.BloodMagicItem.COMPONENT.getRegName());
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.tabBloodMagic);

        buildItemList();
    }

    private void buildItemList()
    {
        names.add(0, REAGENT_WATER);
        names.add(1, REAGENT_LAVA);
        names.add(2, REAGENT_AIR);
        names.add(3, REAGENT_FASTMINER);
        names.add(4, REAGENT_VOID);
        names.add(5, REAGENT_GROWTH);
        names.add(6, REAGENT_AFFINITY);
        names.add(7, REAGENT_SIGHT);
        names.add(8, REAGENT_BINDING);
        names.add(9, REAGENT_SUPPRESSION);
        names.add(10, COMPONENT_FRAME_PART);
        names.add(11, REAGENT_BLOODLIGHT);
        names.add(12, REAGENT_MAGNETISM);
        names.add(13, REAGENT_HASTE);
        names.add(14, REAGENT_COMPRESSION);
        names.add(15, REAGENT_BRIDGE);
        names.add(16, REAGENT_SEVERANCE);
        names.add(17, REAGENT_TELEPOSITION);
        names.add(18, REAGENT_TRANSPOSITION);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names.get(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(id, 1, i));
    }

    public static ItemStack getStack(String name)
    {
        return new ItemStack(ModItems.itemComponent, 1, names.indexOf(name));
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (String name : names)
            ret.add(new ImmutablePair<Integer, String>(names.indexOf(name), "type=" + name));
        return ret;
    }
}
