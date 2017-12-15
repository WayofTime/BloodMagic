package WayofTime.bloodmagic.core;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.IBMBlock;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.item.*;
import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import WayofTime.bloodmagic.item.alchemy.ItemLivingArmourPointsUpgrade;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.armour.ItemSentientArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.item.gear.ItemPackSelfSacrifice;
import WayofTime.bloodmagic.item.routing.ItemFluidRouterFilter;
import WayofTime.bloodmagic.item.routing.ItemNodeRouter;
import WayofTime.bloodmagic.item.routing.ItemRouterFilter;
import WayofTime.bloodmagic.item.sigil.*;
import WayofTime.bloodmagic.item.sigil.sigil.*;
import WayofTime.bloodmagic.item.sigil.sigil.ItemSigilTeleposition;
import WayofTime.bloodmagic.item.sigil.sigil.ItemSigilTransposition;
import WayofTime.bloodmagic.item.soul.*;
import WayofTime.bloodmagic.item.types.ComponentType;
import WayofTime.bloodmagic.item.types.ReagentType;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

@GameRegistry.ObjectHolder(BloodMagic.MODID)
@SuppressWarnings("unchecked")
public class RegistrarBloodMagicItems {
    public static final Item BLOOD_ORB = Items.AIR;
    public static final Item ACTIVATION_CRYSTAL = Items.AIR;
    public static final Item SLATE = Items.AIR;
    public static final Item INSCRIPTION_TOOL = Items.AIR;
    public static final Item SACRIFICIAL_DAGGER = Items.AIR;
    public static final Item PACK_SELF_SACRIFICE = Items.AIR;
    public static final Item PACK_SACRIFICE = Items.AIR;
    public static final Item DAGGER_OF_SACRIFICE = Items.AIR;
    public static final Item RITUAL_DIVINER = Items.AIR;
    public static final Item RITUAL_READER = Items.AIR;
    public static final Item LAVA_CRYSTAL = Items.AIR;
    public static final Item BOUND_SWORD = Items.AIR;
    public static final Item BOUND_PICKAXE = Items.AIR;
    public static final Item BOUND_AXE = Items.AIR;
    public static final Item BOUND_SHOVEL = Items.AIR;
    public static final Item SIGIL_DIVINATION = Items.AIR;
    public static final Item SIGIL_AIR = Items.AIR;
    public static final Item SIGIL_WATER = Items.AIR;
    public static final Item SIGIL_LAVA = Items.AIR;
    public static final Item SIGIL_VOID = Items.AIR;
    public static final Item SIGIL_GREEN_GROVE = Items.AIR;
    public static final Item SIGIL_BLOOD_LIGHT = Items.AIR;
    public static final Item SIGIL_ELEMENTAL_AFFINITY = Items.AIR;
    public static final Item SIGIL_HASTE = Items.AIR;
    public static final Item SIGIL_MAGNETISM = Items.AIR;
    public static final Item SIGIL_SUPPRESSION = Items.AIR;
    public static final Item SIGIL_FAST_MINER = Items.AIR;
    public static final Item SIGIL_SEER = Items.AIR;
    public static final Item SIGIL_ENDER_SEVERANCE = Items.AIR;
    public static final Item SIGIL_WHIRLWIND = Items.AIR;
    public static final Item SIGIL_PHANTOM_BRIDGE = Items.AIR;
    public static final Item SIGIL_COMPRESSION = Items.AIR;
    public static final Item SIGIL_HOLDING = Items.AIR;
    public static final Item SIGIL_TELEPOSITION = Items.AIR;
    public static final Item SIGIL_TRANSPOSITION = Items.AIR;
    public static final Item SIGIL_CLAW = Items.AIR;
    public static final Item SIGIL_BOUNCE = Items.AIR;
    public static final Item SIGIL_FROST = Items.AIR;
    public static final Item COMPONENT = Items.AIR;
    public static final Item REAGENT = Items.AIR;
    public static final Item ITEM_DEMON_CRYSTAL = Items.AIR;
    public static final Item TELEPOSITION_FOCUS = Items.AIR;
    public static final Item EXPERIENCE_TOME = Items.AIR;
    public static final Item BLOOD_SHARD = Items.AIR;
    public static final Item LIVING_ARMOUR_HELMET = Items.AIR;
    public static final Item LIVING_ARMOUR_CHEST = Items.AIR;
    public static final Item LIVING_ARMOUR_LEGGINGS = Items.AIR;
    public static final Item LIVING_ARMOUR_BOOTS = Items.AIR;
    public static final Item SENTIENT_ARMOUR_HELMET = Items.AIR;
    public static final Item SENTIENT_ARMOUR_CHEST = Items.AIR;
    public static final Item SENTIENT_ARMOUR_LEGGINGS = Items.AIR;
    public static final Item SENTIENT_ARMOUR_BOOTS = Items.AIR;
    public static final Item ALTAR_MAKER = Items.AIR;
    public static final Item UPGRADE_TOME = Items.AIR;
    public static final Item UPGRADE_TRAINER = Items.AIR;
    public static final Item ARCANE_ASHES = Items.AIR;
    public static final Item MONSTER_SOUL = Items.AIR;
    public static final Item SOUL_GEM = Items.AIR;
    public static final Item SOUL_SNARE = Items.AIR;
    public static final Item SENTIENT_SWORD = Items.AIR;
    public static final Item SENTIENT_BOW = Items.AIR;
    public static final Item SENTIENT_ARMOUR_GEM = Items.AIR;
    public static final Item SENTIENT_AXE = Items.AIR;
    public static final Item SENTIENT_PICKAXE = Items.AIR;
    public static final Item SENTIENT_SHOVEL = Items.AIR;
    public static final Item NODE_ROUTER = Items.AIR;
    public static final Item BASE_ITEM_FILTER = Items.AIR;
    public static final Item BASE_FLUID_FILTER = Items.AIR;
    public static final Item CUTTING_FLUID = Items.AIR;
    public static final Item SANGUINE_BOOK = Items.AIR;
    public static final Item POINTS_UPGRADE = Items.AIR;
    public static final Item DEMON_WILL_GAUGE = Items.AIR;
    public static final Item POTION_FLASK = Items.AIR;

    public static Item.ToolMaterial BOUND_TOOL_MATERIAL = EnumHelper.addToolMaterial("bound", 4, 1, 10, 8, 50);
    public static Item.ToolMaterial SOUL_TOOL_MATERIAL = EnumHelper.addToolMaterial("demonic", 4, 520, 7, 8, 50);

    public static List<Item> items;

    public static void registerItems(IForgeRegistry<Item> registry) {
        items = Lists.newArrayList();

        RegistrarBloodMagicBlocks.blocks.stream().filter(block -> block instanceof IBMBlock && ((IBMBlock) block).getItem() != null).forEach(block -> {
            IBMBlock bmBlock = (IBMBlock) block;
            items.add(bmBlock.getItem().setRegistryName(block.getRegistryName()));
        });

        items.addAll(Lists.newArrayList(
                new ItemBloodOrb().setRegistryName("blood_orb"),
                new ItemActivationCrystal().setRegistryName("activation_crystal"),
                new ItemSlate().setRegistryName("slate"),
                new ItemInscriptionTool().setRegistryName("inscription_tool"),
                new ItemSacrificialDagger().setRegistryName("sacrificial_dagger"),
                new ItemPackSacrifice().setRegistryName("pack_sacrifice"),
                new ItemPackSelfSacrifice().setRegistryName("pack_self_sacrifice"),
                new ItemDaggerOfSacrifice().setRegistryName("dagger_of_sacrifice"),
                new ItemRitualDiviner().setRegistryName("ritual_diviner"),
                new ItemRitualReader().setRegistryName("ritual_reader"),
                new ItemLavaCrystal().setRegistryName("lava_crystal"),
                new ItemBoundSword().setRegistryName("bound_sword"),
                new ItemBoundPickaxe().setRegistryName("bound_pickaxe"),
                new ItemBoundAxe().setRegistryName("bound_axe"),
                new ItemBoundShovel().setRegistryName("bound_shovel"),
                new ItemSigilDivination().setRegistryName("sigil_divination"),
                new ItemSigil(new SigilAir(), "air").setRegistryName("sigil_air"),
                new ItemSigil(new SigilFluid(FluidRegistry.WATER), "water").setRegistryName("sigil_water"),
                new ItemSigil(new SigilFluid(FluidRegistry.LAVA), "lava").setRegistryName("sigil_lava"),
                new ItemSigilVoid().setRegistryName("sigil_void"),
                new ItemSigil(new SigilGreenGrove(), "green_grove").setRegistryName("sigil_green_grove"),
                new ItemSigil(new SigilBloodLight(), "blood_light").setRegistryName("sigil_blood_light"),
                new ItemSigil(new SigilElementalAffinity(), "elemental_affinity").setRegistryName("sigil_elemental_affinity"),
                new ItemSigilMagnetism().setRegistryName("sigil_magnetism"),
                new ItemSigilSuppression().setRegistryName("sigil_suppression"),
                new ItemSigil(new SigilHaste(), "haste").setRegistryName("sigil_haste"),
                new ItemSigil(new SigilFastMiner(), "fast_miner").setRegistryName("sigil_fast_miner"),
                new ItemSigilSeer().setRegistryName("sigil_seer"),
                new ItemSigilPhantomBridge().setRegistryName("sigil_phantom_bridge"),
                new ItemSigilWhirlwind().setRegistryName("sigil_whirlwind"),
                new ItemSigilCompression().setRegistryName("sigil_compression"),
                new ItemSigil(new SigilEnderSeverance(), "ender_severance").setRegistryName("sigil_ender_severance"),
                new ItemSigilHolding().setRegistryName("sigil_holding"),
                new ItemSigilTeleposition().setRegistryName("sigil_teleposition"),
                new ItemSigilTransposition().setRegistryName("sigil_transposition"),
                new ItemSigil(new SigilClaw(), "claw").setRegistryName("sigil_claw"),
                new ItemSigil(new SigilBounce(), "bounce").setRegistryName("sigil_bounce"),
                new ItemSigil(new SigilFrost(), "frost").setRegistryName("sigil_frost"),
                new ItemEnum<>(ComponentType.class, "component").setRegistryName("component"),
                new ItemEnum<>(ReagentType.class, "reagent").setRegistryName("reagent"),
                new ItemDemonCrystal().setRegistryName("item_demon_crystal"),
                new ItemTelepositionFocus().setRegistryName("teleposition_focus"),
                new ItemExperienceBook().setRegistryName("experience_tome"),
                new ItemBloodShard().setRegistryName("blood_shard"),
                new ItemLivingArmour(EntityEquipmentSlot.HEAD).setRegistryName("living_armour_helmet"),
                new ItemLivingArmour(EntityEquipmentSlot.CHEST).setRegistryName("living_armour_chest"),
                new ItemLivingArmour(EntityEquipmentSlot.LEGS).setRegistryName("living_armour_leggings"),
                new ItemLivingArmour(EntityEquipmentSlot.FEET).setRegistryName("living_armour_boots"),
                new ItemSentientArmour(EntityEquipmentSlot.HEAD).setRegistryName("sentient_armour_helmet"),
                new ItemSentientArmour(EntityEquipmentSlot.CHEST).setRegistryName("sentient_armour_chest"),
                new ItemSentientArmour(EntityEquipmentSlot.LEGS).setRegistryName("sentient_armour_leggings"),
                new ItemSentientArmour(EntityEquipmentSlot.FEET).setRegistryName("sentient_armour_boots"),
                new ItemAltarMaker().setRegistryName("altar_maker"),
                new ItemUpgradeTome().setRegistryName("upgrade_tome"),
                new ItemUpgradeTrainer().setRegistryName("upgrade_trainer"),
                new ItemArcaneAshes().setRegistryName("arcane_ashes"),
                new ItemMonsterSoul().setRegistryName("monster_soul"),
                new ItemSoulGem().setRegistryName("soul_gem"),
                new ItemSoulSnare().setRegistryName("soul_snare"),
                new ItemSentientSword().setRegistryName("sentient_sword"),
                new ItemSentientBow().setRegistryName("sentient_bow"),
                new ItemSentientArmourGem().setRegistryName("sentient_armour_gem"),
                new ItemSentientAxe().setRegistryName("sentient_axe"),
                new ItemSentientPickaxe().setRegistryName("sentient_pickaxe"),
                new ItemSentientShovel().setRegistryName("sentient_shovel"),
                new ItemNodeRouter().setRegistryName("node_router"),
                new ItemRouterFilter().setRegistryName("base_item_filter"),
                new ItemFluidRouterFilter().setRegistryName("base_fluid_filter"),
                new ItemCuttingFluid().setRegistryName("cutting_fluid"),
                new ItemSanguineBook().setRegistryName("sanguine_book"),
                new ItemLivingArmourPointsUpgrade().setRegistryName("points_upgrade"),
                new ItemDemonWillGauge().setRegistryName("demon_will_gauge"),
                new ItemPotionFlask().setRegistryName("potion_flask")
        ));

        registry.registerAll(items.toArray(new Item[0]));
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        items.stream().filter(i -> i instanceof IVariantProvider).forEach(i -> {
            IVariantProvider variantProvider = (IVariantProvider) i;
            Int2ObjectMap<String> variants = new Int2ObjectOpenHashMap<>();
            variantProvider.populateVariants(variants);
            for (Int2ObjectMap.Entry<String> variant : variants.int2ObjectEntrySet())
                ModelLoader.setCustomModelResourceLocation(i, variant.getIntKey(), new ModelResourceLocation(i.getRegistryName(), variant.getValue()));
        });

        items.stream().filter(i -> i instanceof IMeshProvider).forEach(i -> {
            IMeshProvider mesh = (IMeshProvider) i;
            ResourceLocation loc = mesh.getCustomLocation();
            if (loc == null)
                loc = i.getRegistryName();

            List<String> variants = Lists.newArrayList();
            mesh.populateVariants(variants);
            for (String variant : variants)
                ModelLoader.registerItemVariants(i, new ModelResourceLocation(loc, variant));

            ModelLoader.setCustomMeshDefinition(i, mesh.getMeshDefinition());
        });

        RegistrarBloodMagicBlocks.blocks.stream().filter(b -> b instanceof IVariantProvider).forEach(b -> {
            IVariantProvider variantProvider = (IVariantProvider) b;
            Int2ObjectMap<String> variants = new Int2ObjectOpenHashMap<>();
            variantProvider.populateVariants(variants);
            for (Int2ObjectMap.Entry<String> variant : variants.int2ObjectEntrySet())
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), variant.getIntKey(), new ModelResourceLocation(b.getRegistryName(), variant.getValue()));
        });

        final ResourceLocation holdingLoc = SIGIL_HOLDING.getRegistryName();
        ModelLoader.setCustomMeshDefinition(SIGIL_HOLDING, stack -> {
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("color"))
                return new ModelResourceLocation(holdingLoc, "type=color");
            return new ModelResourceLocation(holdingLoc, "type=normal");
        });
        ModelLoader.registerItemVariants(SIGIL_HOLDING, new ModelResourceLocation(holdingLoc, "type=normal"));
        ModelLoader.registerItemVariants(SIGIL_HOLDING, new ModelResourceLocation(holdingLoc, "type=color"));
    }
}
