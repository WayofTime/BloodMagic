package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.registry.RegistrarBloodMagicItems;

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
    public static final String SAND_IRON = "ironSand";
    public static final String SAND_GOLD = "goldSand";
    public static final String SAND_COAL = "coalSand";
    public static final String PLANT_OIL = "plantOil";
    public static final String SULFUR = "sulfur";
    public static final String SALTPETER = "saltpeter";
    public static final String NEURO_TOXIN = "neurotoxin";
    public static final String ANTISEPTIC = "antiseptic";
    public static final String REAGENT_HOLDING = "reagentHolding";
    public static final String CATALYST_LENGTH_1 = "mundaneLength";
    public static final String CATALYST_POWER_1 = "mundanePower";
    public static final String REAGENT_CLAW = "reagentClaw";
    public static final String REAGENT_BOUNCE = "reagentBounce";
    public static final String REAGENT_FROST = "reagentFrost";

    public ItemComponent()
    {
        super();

        setUnlocalizedName(BloodMagic.MODID + ".baseComponent.");
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.TAB_BM);

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
        names.add(19, SAND_IRON);
        names.add(20, SAND_GOLD);
        names.add(21, SAND_COAL);
        names.add(22, PLANT_OIL);
        names.add(23, SULFUR);
        names.add(24, SALTPETER);
        names.add(25, NEURO_TOXIN);
        names.add(26, ANTISEPTIC);
        names.add(27, REAGENT_HOLDING);
        names.add(28, CATALYST_LENGTH_1);
        names.add(29, CATALYST_POWER_1);
        names.add(30, REAGENT_CLAW);
        names.add(31, REAGENT_BOUNCE);
        names.add(32, REAGENT_FROST);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names.get(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(id, 1, i));
    }

    public static ItemStack getStack(String name)
    {
        return new ItemStack(RegistrarBloodMagicItems.ITEM_COMPONENT, 1, names.indexOf(name));
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (String name : names)
            ret.add(new ImmutablePair<Integer, String>(names.indexOf(name), "type=" + name));
        return ret;
    }

    public static ItemStack getStack(String key, int stackSize)
    {
        ItemStack stack = getStack(key);
        stack.setCount(stackSize);

        return stack;
    }
}
