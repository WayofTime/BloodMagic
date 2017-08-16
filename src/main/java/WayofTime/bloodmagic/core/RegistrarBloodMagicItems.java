package WayofTime.bloodmagic.core;

import WayofTime.bloodmagic.block.IBMBlock;
import WayofTime.bloodmagic.client.IVariantProvider;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
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
import WayofTime.bloodmagic.item.soul.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Set;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
@GameRegistry.ObjectHolder(BloodMagic.MODID)
@SuppressWarnings("unchecked")
public class RegistrarBloodMagicItems
{
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
    public static final Item ITEM_COMPONENT = Items.AIR;
    public static final Item ITEM_DEMON_CRYSTAL = Items.AIR;
    public static final Item TELEPOSITION_FOCUS = Items.AIR;
    public static final Item EXPERIENCE_TOME = Items.AIR;
    public static final Item BLOOD_SHARD = Items.AIR;
    public static final Item LIVING_ARMOUR_HELMET = Items.AIR;
    public static final Item LIVING_ARMOUR_CHEST = Items.AIR;
    public static final Item LIVING_ARMOUR_LEGS = Items.AIR;
    public static final Item LIVING_ARMOUR_BOOTS = Items.AIR;
    public static final Item SENTIENT_ARMOUR_HELMET = Items.AIR;
    public static final Item SENTIENT_ARMOUR_CHEST = Items.AIR;
    public static final Item SENTIENT_ARMOUR_LEGS = Items.AIR;
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
    public static final Item ITEM_POINTS_UPGRADE = Items.AIR;
    public static final Item DEMON_WILL_GAUGE = Items.AIR;
    public static final Item POTION_FLASK = Items.AIR;

    public static Item.ToolMaterial BOUND_TOOL_MATERIAL = EnumHelper.addToolMaterial("bound", 4, 1, 10, 8, 50);
    public static Item.ToolMaterial SOUL_TOOL_MATERIAL = EnumHelper.addToolMaterial("demonic", 4, 520, 7, 8, 50);

    private static Set<Item> items;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        items = Sets.newHashSet(
                new ItemBloodOrb().setRegistryName("blood_orb"),
                new ItemActivationCrystal().setRegistryName("activation_crystal"),
                new ItemSlate().setRegistryName("slate"),
                new ItemInscriptionTool().setRegistryName("inscription_tool"),
                new ItemSacrificialDagger().setRegistryName("sacrificial_dagger"),
                new ItemPackSacrifice().setRegistryName("sacrificial_pack"),
                new ItemPackSelfSacrifice().setRegistryName("pack_of_sacrifice"),
                new ItemDaggerOfSacrifice().setRegistryName("dagger_of_sacrifice"),
                new ItemRitualDiviner().setRegistryName("ritual_diviner"),
                new ItemRitualReader().setRegistryName("ritual_reader"),
                new ItemLavaCrystal().setRegistryName("lava_crystal"),
                new ItemBoundSword().setRegistryName("bound_sword"),
                new ItemBoundPickaxe().setRegistryName("bound_pickaxe"),
                new ItemBoundAxe().setRegistryName("bound_axe"),
                new ItemBoundShovel().setRegistryName("bound_shovel"),
                new ItemSigilDivination().setRegistryName("sigil_divination"),
                new ItemSigilAir().setRegistryName("sigil_air"),
                new ItemSigilWater().setRegistryName("sigil_water"),
                new ItemSigilLava().setRegistryName("sigil_lava"),
                new ItemSigilVoid().setRegistryName("sigil_void"),
                new ItemSigilGreenGrove().setRegistryName("sigil_growth"),
                new ItemSigilBloodLight().setRegistryName("sigil_light"),
                new ItemSigilElementalAffinity().setRegistryName("sigil_elemental"),
                new ItemSigilMagnetism().setRegistryName("sigil_magnetism"),
                new ItemSigilSuppression().setRegistryName("sigil_suppression"),
                new ItemSigilHaste().setRegistryName("sigil_haste"),
                new ItemSigilFastMiner().setRegistryName("sigil_fast_miner"),
                new ItemSigilSeer().setRegistryName("sigil_seer"),
                new ItemSigilPhantomBridge().setRegistryName("sigil_bridge"),
                new ItemSigilWhirlwind().setRegistryName("sigil_whirlwind"),
                new ItemSigilCompression().setRegistryName("sigil_compression"),
                new ItemSigilEnderSeverance().setRegistryName("sigil_severance"),
                new ItemSigilHolding().setRegistryName("sigil_holding"),
                new ItemSigilTeleposition().setRegistryName("sigil_teleposition"),
                new ItemSigilTransposition().setRegistryName("sigil_transposition"),
                new ItemSigilClaw().setRegistryName("sigil_claw"),
                new ItemSigilBounce().setRegistryName("sigil_bounce"),
                new ItemSigilFrost().setRegistryName("sigil_frost"),
                new ItemComponent().setRegistryName("component"),
                new ItemDemonCrystal().setRegistryName("demon_crystal"),
                new ItemTelepositionFocus().setRegistryName("teleposition_focus"),
                new ItemExperienceBook().setRegistryName("experience_book"),
                new ItemBloodShard().setRegistryName("blood_shard"),
                new ItemLivingArmour(EntityEquipmentSlot.HEAD).setRegistryName("living_helm"),
                new ItemLivingArmour(EntityEquipmentSlot.CHEST).setRegistryName("living_chest"),
                new ItemLivingArmour(EntityEquipmentSlot.LEGS).setRegistryName("living_leggings"),
                new ItemLivingArmour(EntityEquipmentSlot.FEET).setRegistryName("living_boots"),
                new ItemSentientArmour(EntityEquipmentSlot.HEAD).setRegistryName("sentient_helm"),
                new ItemSentientArmour(EntityEquipmentSlot.CHEST).setRegistryName("sentient_chest"),
                new ItemSentientArmour(EntityEquipmentSlot.LEGS).setRegistryName("sentient_leggings"),
                new ItemSentientArmour(EntityEquipmentSlot.FEET).setRegistryName("sentient_boots"),
                new ItemAltarMaker().setRegistryName("altar_maker"),
                new ItemUpgradeTome().setRegistryName("upgrade_tome"),
                new ItemUpgradeTrainer().setRegistryName("upgrade_trainer"),
                new ItemArcaneAshes().setRegistryName("arcane_ashes"),
                new ItemMonsterSoul().setRegistryName("monster_soul"),
                new ItemSoulGem().setRegistryName("soul_gem"),
                new ItemSoulSnare().setRegistryName("soul_snare"),
                new ItemSentientSword().setRegistryName("sentient_sword"),
                new ItemSentientBow().setRegistryName("sentient_bow"),
                new ItemSentientArmourGem().setRegistryName("armour_gem"),
                new ItemSentientAxe().setRegistryName("sentient_axe"),
                new ItemSentientPickaxe().setRegistryName("sentient_pickaxe"),
                new ItemSentientShovel().setRegistryName("sentient_shovel"),
                new ItemNodeRouter().setRegistryName("node_router"),
                new ItemRouterFilter().setRegistryName("router_filter"),
                new ItemFluidRouterFilter().setRegistryName("fluid_router_filter"),
                new ItemCuttingFluid().setRegistryName("cutting_fluid"),
                new ItemSanguineBook().setRegistryName("sanguine_book"),
                new ItemLivingArmourPointsUpgrade().setRegistryName("living_armor_point_upgrade"),
                new ItemDemonWillGauge().setRegistryName("demon_will_gauge"),
                new ItemPotionFlask().setRegistryName("potion_flask")
        );

        RegistrarBloodMagicBlocks.blocks.stream().filter(block -> block instanceof IBMBlock && ((IBMBlock) block).getItem() != null).forEach(block -> {
            IBMBlock bmBlock = (IBMBlock) block;
            items.add(bmBlock.getItem().setRegistryName(block.getRegistryName()));
        });

        event.getRegistry().registerAll(items.toArray(new Item[0]));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event)
    {
        items.stream().filter(i -> i instanceof IVariantProvider).forEach(item -> {
            IVariantProvider variantProvider = (IVariantProvider) item;
            for (Pair<Integer, String> variant : variantProvider.getVariants())
                ModelLoader.setCustomModelResourceLocation(item, variant.getLeft(), new ModelResourceLocation(item.getRegistryName(), variant.getRight()));
        });

        RegistrarBloodMagicBlocks.blocks.stream().filter(i -> i instanceof IVariantProvider).forEach(block -> {
            IVariantProvider variantProvider = (IVariantProvider) block;
            for (Pair<Integer, String> variant : variantProvider.getVariants())
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), variant.getLeft(), new ModelResourceLocation(block.getRegistryName(), variant.getRight()));
        });

        final ResourceLocation holdingLoc = new ResourceLocation("bloodmagic", "item/ItemSigilHolding");
        ModelLoader.setCustomMeshDefinition(SIGIL_HOLDING, stack -> {
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("color"))
                return new ModelResourceLocation(holdingLoc, "type=color");
            return new ModelResourceLocation(holdingLoc, "type=normal");
        });
        ModelLoader.registerItemVariants(SIGIL_HOLDING, new ModelResourceLocation(holdingLoc, "type=normal"));
        ModelLoader.registerItemVariants(SIGIL_HOLDING, new ModelResourceLocation(holdingLoc, "type=color"));
    }
}
