package com.wayoftime.bloodmagic.core;

import com.google.common.collect.Lists;
import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.network.BloodOrb;
import com.wayoftime.bloodmagic.core.type.DemonWillType;
import com.wayoftime.bloodmagic.core.type.SlateType;
import com.wayoftime.bloodmagic.core.util.register.IItemProvider;
import com.wayoftime.bloodmagic.core.util.register.IVariantProvider;
import com.wayoftime.bloodmagic.item.*;
import com.wayoftime.bloodmagic.item.sigil.ItemSigil;
import com.wayoftime.bloodmagic.item.sigil.SigilAir;
import com.wayoftime.bloodmagic.item.sigil.SigilDivination;
import com.wayoftime.bloodmagic.item.sigil.SigilFastMiner;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

@GameRegistry.ObjectHolder(BloodMagic.MODID)
@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class RegistrarBloodMagicItems {

    public static final Item BLOOD_ORB_WEAK = Items.AIR;
    public static final Item BLOOD_ORB_APPRENTICE = Items.AIR;
    public static final Item BLOOD_ORB_MAGICIAN = Items.AIR;
    public static final Item BLOOD_ORB_MASTER = Items.AIR;
    public static final Item BLOOD_ORB_ARCHMAGE = Items.AIR;
    public static final Item BLOOD_ORB_TRANSCENDENT = Items.AIR;

    public static final Item DAGGER_SELF_SACRIFICE = Items.AIR;
    public static final Item DAGGER_SELF_SACRIFICE_CREATIVE = Items.AIR;

    public static final Item SLATE_BLANK = Items.AIR;
    public static final Item SLATE_REINFORCED = Items.AIR;
    public static final Item SLATE_IMBUED = Items.AIR;
    public static final Item SLATE_DEMONIC = Items.AIR;
    public static final Item SLATE_ETHEREAL = Items.AIR;

    public static final Item SIGIL_DIVINATION = Items.AIR;
    public static final Item SIGIL_AIR = Items.AIR;
    public static final Item SIGIL_FAST_MINER = Items.AIR;

    public static final Item LIVING_ARMOR_HEAD = Items.AIR;
    public static final Item LIVING_ARMOR_CHEST = Items.AIR;
    public static final Item LIVING_ARMOR_LEGS = Items.AIR;
    public static final Item LIVING_ARMOR_FEET = Items.AIR;
    public static final Item LIVING_TOME = Items.AIR;

    public static final Item DEMON_WILL_CRYSTAL_RAW = Items.AIR;
    public static final Item DEMON_WILL_CRYSTAL_CORROSIVE = Items.AIR;
    public static final Item DEMON_WILL_CRYSTAL_DESTRUCTIVE = Items.AIR;
    public static final Item DEMON_WILL_CRYSTAL_VENGEFUL = Items.AIR;
    public static final Item DEMON_WILL_CRYSTAL_STEADFAST = Items.AIR;

    static List<Item> items = Lists.newArrayList();

    public static void register(IForgeRegistry<Item> registry) {
        for (Block block : RegistrarBloodMagicBlocks.blocks) {
            if (block instanceof IItemProvider) {
                Item item = ((IItemProvider) block).getItem();
                if (item != null)
                    items.add(item.setRegistryName(block.getRegistryName()));
            }
        }

        items.addAll(Lists.newArrayList(
                new ItemBloodOrb(new BloodOrb(new ResourceLocation(BloodMagic.MODID, "weak"), 1, 5000, 2)),
                new ItemBloodOrb(new BloodOrb(new ResourceLocation(BloodMagic.MODID, "apprentice"), 2, 25000, 5)),
                new ItemBloodOrb(new BloodOrb(new ResourceLocation(BloodMagic.MODID, "magician"), 3, 150000, 15)),
                new ItemBloodOrb(new BloodOrb(new ResourceLocation(BloodMagic.MODID, "master"), 4, 1000000, 25)),
                new ItemBloodOrb(new BloodOrb(new ResourceLocation(BloodMagic.MODID, "archmage"), 5, 10000000, 50)),
                new ItemBloodOrb(new BloodOrb(new ResourceLocation(BloodMagic.MODID, "transcendent"), 6, 30000000, 50)),
                new ItemDaggerSelfSacrifice(ItemDaggerSelfSacrifice.Type.NORMAL),
                new ItemDaggerSelfSacrifice(ItemDaggerSelfSacrifice.Type.CREATIVE),
                new ItemMundane("slate_" + SlateType.BLANK.getName()),
                new ItemMundane("slate_" + SlateType.REINFORCED.getName()),
                new ItemMundane("slate_" + SlateType.IMBUED.getName()),
                new ItemMundane("slate_" + SlateType.DEMONIC.getName()),
                new ItemMundane("slate_" + SlateType.ETHEREAL.getName()),
                new ItemSigil(new SigilDivination(), "divination"),
                new ItemSigil(new SigilAir(), "air"),
                new ItemSigil(new SigilFastMiner(), "fast_miner"),
                new ItemLivingArmor(EntityEquipmentSlot.HEAD),
                new ItemLivingArmor(EntityEquipmentSlot.CHEST),
                new ItemLivingArmor(EntityEquipmentSlot.LEGS),
                new ItemLivingArmor(EntityEquipmentSlot.FEET),
                new ItemLivingTome(),
                new ItemAltarBuilder()
        ));

        for (DemonWillType type : DemonWillType.VALUES)
            items.add(new ItemMundane("demon_will_crystal_" + type.getName()));

        registry.registerAll(items.toArray(new Item[0]));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (Item item : items) {
            boolean flag = handleModel(item);

            if (!flag) // If we haven't registered a model by now, we don't need any special handling so we'll just use the default model.
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), item instanceof ItemBlock ? "normal" : "inventory"));
        }
    }

    static boolean handleModel(Item item) {
        if (item instanceof IVariantProvider) {
            Int2ObjectMap<String> variants = new Int2ObjectOpenHashMap<>();
            ((IVariantProvider) item).collectVariants(variants);
            for (Int2ObjectMap.Entry<String> entry : variants.int2ObjectEntrySet())
                ModelLoader.setCustomModelResourceLocation(item, entry.getIntKey(), new ModelResourceLocation(item.getRegistryName(), entry.getValue()));

            return true;
        }

        return false;
    }
}
