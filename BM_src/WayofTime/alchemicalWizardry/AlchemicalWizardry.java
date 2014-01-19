package WayofTime.alchemicalWizardry;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ItemApi;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryEventHooks;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryFuelHandler;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryTickHandler;
import WayofTime.alchemicalWizardry.common.BloodMagicConfiguration;
import WayofTime.alchemicalWizardry.common.CommonProxy;
import WayofTime.alchemicalWizardry.common.EntityAirElemental;
import WayofTime.alchemicalWizardry.common.LifeBucketHandler;
import WayofTime.alchemicalWizardry.common.ModLivingDropsEvent;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.PotionBoost;
import WayofTime.alchemicalWizardry.common.PotionDrowning;
import WayofTime.alchemicalWizardry.common.PotionFlight;
import WayofTime.alchemicalWizardry.common.PotionInhibit;
import WayofTime.alchemicalWizardry.common.PotionProjectileProtect;
import WayofTime.alchemicalWizardry.common.PotionReciprocation;
import WayofTime.alchemicalWizardry.common.alchemy.AlchemicalPotionCreationHandler;
import WayofTime.alchemicalWizardry.common.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.common.block.ArmourForge;
import WayofTime.alchemicalWizardry.common.block.LifeEssenceBlock;
import WayofTime.alchemicalWizardry.common.bloodAltarUpgrade.UpgradedAltars;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityBileDemon;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityBoulderFist;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityEarthElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFallenAngel;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFireElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityHolyElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityIceDemon;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityLowerGuardian;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityShade;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityShadeElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntitySmallEarthGolem;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityWaterElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityWingedFireDemon;
import WayofTime.alchemicalWizardry.common.items.ItemBloodRuneBlock;
import WayofTime.alchemicalWizardry.common.items.ItemRitualDiviner;
import WayofTime.alchemicalWizardry.common.items.LifeBucket;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHolding;
import WayofTime.alchemicalWizardry.common.items.thaumcraft.ItemSanguineArmour;
import WayofTime.alchemicalWizardry.common.rituals.Rituals;
import WayofTime.alchemicalWizardry.common.spell.simple.HomSpellRegistry;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellEarthBender;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellExplosions;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellFireBurst;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellFrozenWater;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellHolyBlast;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellLightningBolt;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellTeleport;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellWateryGrave;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellWindGust;
import WayofTime.alchemicalWizardry.common.summoning.SummoningHelper;
import WayofTime.alchemicalWizardry.common.summoning.SummoningRegistry;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEConduit;
import WayofTime.alchemicalWizardry.common.tileEntity.TEHomHeart;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPedestal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import WayofTime.alchemicalWizardry.common.tileEntity.gui.GuiHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "AWWayofTime", name = "AlchemicalWizardry", version = "v0.7.2")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"BloodAltar", "particle", "SetLifeEssence", "GetLifeEssence", "Ritual", "GetAltarEssence", "TESocket", "TEWritingTable", "CustomParticle", "SetPlayerVel", "SetPlayerPos", "TEPedestal", "TEPlinth", "TETeleposer", "InfiniteLPPath", "TEOrientor"}, packetHandler = PacketHandler.class)

public class AlchemicalWizardry
{
    public static boolean doMeteorsDestroyBlocks = true;
    public static String[] diamondMeteorArray;
    public static int diamondMeteorRadius;
    public static String[] stoneMeteorArray;
    public static int stoneMeteorRadius;
    public static String[] ironBlockMeteorArray;
    public static int ironBlockMeteorRadius;
    public static String[] netherStarMeteorArray;
    public static int netherStarMeteorRadius;

    public static Potion customPotionDrowning;
    public static Potion customPotionBoost;
    public static Potion customPotionProjProt;
    public static Potion customPotionInhibit;
    public static Potion customPotionFlight;
    public static Potion customPotionReciprocation;

    public static int customPotionDrowningID;
    public static int customPotionBoostID;
    public static int customPotionProjProtID;
    public static int customPotionInhibitID;
    public static int customPotionFlightID;
    public static int customPotionReciprocationID;

    public static boolean isThaumcraftLoaded;

    public static CreativeTabs tabBloodMagic = new CreativeTabs("tabBloodMagic")
    {
        public ItemStack getIconItemStack()
        {
            return new ItemStack(ModItems.weakBloodOrb, 1, 0);
        }
    };

    public static EnumToolMaterial bloodBoundToolMaterial = EnumHelper.addToolMaterial("BoundBlood", 4, 1000, 12.0f, 8.0f, 50);
    public static EnumArmorMaterial sanguineArmourArmourMaterial = EnumHelper.addArmorMaterial("SanguineArmour", 1000, new int[]{3, 6, 5, 2}, 30);

    //Dungeon loot chances
    public static int standardBindingAgentDungeonChance;
    public static int mundanePowerCatalystDungeonChance;
    public static int averagePowerCatalystDungeonChance;
    public static int greaterPowerCatalystDungeonChance;
    public static int mundaneLengtheningCatalystDungeonChance;
    public static int averageLengtheningCatalystDungeonChance;
    public static int greaterLengtheningCatalystDungeonChance;

    //Mob IDs
    public static int entityFallenAngelID = 20;
    public static int entityLowerGuardianID = 21;
    public static int entityBileDemonID = 22;
    public static int entityWingedFireDemonID = 23;
    public static int entitySmallEarthGolemID = 24;
    public static int entityIceDemonID = 25;
    public static int entityBoulderFistID = 26;
    public static int entityShadeID = 27;
    public static int entityAirElementalID = 28;
    public static int entityWaterElementalID = 29;
    public static int entityEarthElementalID = 30;
    public static int entityFireElementalID = 31;
    public static int entityShadeElementalID = 32;
    public static int entityHolyElementalID = 33;

    public static Item bucketLife;
    public static Fluid lifeEssenceFluid;

    public static int weakBloodOrbItemID;
    public static int energyBlasterItemID;
    public static int energySwordItemID;
    public static int lavaCrystalItemID;
    public static int waterSigilItemID;
    public static int lavaSigilItemID;
    public static int voidSigilItemID;
    public static int sigilOfTheFastMinerItemID;
    public static int sigilOfElementalAffinityItemID;
    public static int sigilOfHasteItemID;
    public static int blankSlateItemID;
    public static int reinforcedSlateItemID;
    public static int sacrificialDaggerItemID;
    public static int daggerOfSacrificeItemID;
    public static int bucketLifeItemID;
    public static int apprenticeBloodOrbItemID;
    public static int magicianBloodOrbItemID;
    public static int airSigilItemID;
    public static int sigilOfHoldingItemID;
    public static int divinationSigilItemID;
    public static int elementalInkWaterItemID;
    public static int elementalInkFireItemID;
    public static int elementalInkEarthItemID;
    public static int elementalInkAirItemID;
    public static int waterScribeToolItemID;
    public static int fireScribeToolItemID;
    public static int earthScribeToolItemID;
    public static int airScribeToolItemID;
    public static int weakActivationCrystalItemID;
    public static int boundPickaxeItemID;
    public static int boundAxeItemID;
    public static int boundShovelItemID;
    public static int boundHelmetItemID;
    public static int boundPlateItemID;
    public static int boundLeggingsItemID;
    public static int boundBootsItemID;
    public static int weakBloodShardItemID;
    public static int growthSigilItemID;
    //public static int fireSpellItemID;
    public static int blankSpellItemID;
    public static int masterBloodOrbItemID;
    public static int alchemyFlaskItemID;
    public static int standardBindingAgentItemID;
    public static int mundanePowerCatalystItemID;
    public static int averagePowerCatalystItemID;
    public static int greaterPowerCatalystItemID;
    public static int mundaneLengtheningCatalystItemID;
    public static int averageLengtheningCatalystItemID;
    public static int greaterLengtheningCatalystItemID;
    public static int incendiumItemID;
    public static int magicalesItemID;
    public static int sanctusItemID;
    public static int aetherItemID;
    public static int simpleCatalystItemID;
    public static int crepitousItemID;
    public static int crystallosItemID;
    public static int terraeItemID;
    public static int aquasalusItemID;
    public static int tennebraeItemID;
    public static int demonBloodShardItemID;
    public static int archmageBloodOrbItemID;
    public static int sigilOfWindItemID;
    public static int telepositionFocusItemID;
    public static int enhancedTelepositionFocusItemID;
    public static int reinforcedTelepositionFocusItemID;
    public static int demonicTelepositionFocusItemID;
    public static int imbuedSlateItemID;
    public static int demonicSlateItemID;
    public static int duskScribeToolItemID;
    public static int sigilOfTheBridgeItemID;
    public static int armourInhibitorItemID;
    public static int creativeFillerItemID;
    public static int demonPlacerItemID;
    public static int itemBloodRuneBlockItemID;
    public static int weakFillingAgentItemID;
    public static int standardFillingAgentItemID;
    public static int enhancedFillingAgentItemID;
    public static int weakBindingAgentItemID;
    public static int itemRitualDivinerItemID;
    public static int sanguineHelmetItemID;
    public static int focusBloodBlastItemID;
    public static int focusGravityWellItemID;
    public static int sigilOfMagnetismItemID;
    public static int itemKeyOfDiabloItemID;
    public static int energyBazookaItemID;
    public static int itemBloodLightSigilItemID;
    public static int itemComplexSpellCrystalItemID;

    public static int testingBlockBlockID;
    public static int lifeEssenceFlowingBlockID;
    public static int lifeEssenceStillBlockID;
    public static int blockAltarBlockID;
    public static int bloodRuneBlockID;
    public static int speedRuneBlockID;
    public static int runeOfSacrificeBlockID;
    public static int runeOfSelfSacrificeBlockID;
    public static int efficiencyRuneBlockID;
    public static int lifeEssenceFluidID;
    public static int ritualStoneBlockID;
    public static int blockMasterStoneBlockID;
    public static int imperfectRitualStoneBlockID;
    public static int bloodSocketBlockID;
    public static int armourForgeBlockID;
    public static int emptySocketBlockID;
    public static int largeBloodStoneBrickBlockID;
    public static int bloodStoneBrickBlockID;
    public static int blockWritingTableBlockID;
    public static int blockHomHeartBlockID;
    public static int blockPedestalBlockID;
    public static int blockPlinthBlockID;
    public static int blockTeleposerBlockID;
    public static int spectralBlockBlockID;
    public static int blockConduitBlockID;
    public static int blockBloodLightBlockID;

    public static void registerRenderInformation()
    {
    }

    ;

    public static void registerRenderThings()
    {
    }

    ;

    // The instance of your mod that Forge uses.
    @Instance("AWWayofTime")
    public static AlchemicalWizardry instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "WayofTime.alchemicalWizardry.client.ClientProxy", serverSide = "WayofTime.alchemicalWizardry.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new LifeBucketHandler());
        BloodMagicConfiguration.init(new File(event.getModConfigurationDirectory(), "AWWayofTime.cfg"));

        //Custom config stuff goes here


        Potion[] potionTypes = null;

        for (Field f : Potion.class.getDeclaredFields())
        {
            f.setAccessible(true);

            try
            {
                if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a"))
                {
                    Field modfield = Field.class.getDeclaredField("modifiers");
                    modfield.setAccessible(true);
                    modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                    potionTypes = (Potion[]) f.get(null);
                    final Potion[] newPotionTypes = new Potion[256];
                    System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
                    f.set(null, newPotionTypes);
                }
            } catch (Exception e)
            {
                System.err.println("Severe error, please report this to the mod author:");
                System.err.println(e);
            }
        }

        MinecraftForge.EVENT_BUS.register(new AlchemicalWizardryEventHooks());
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        int craftingConstant = OreDictionary.WILDCARD_VALUE;
        TickRegistry.registerTickHandler(new AlchemicalWizardryTickHandler(), Side.SERVER);
        //orbOfTesting = new OrbOfTesting(17000);

        //public final static Item glassShard = new GlassShard(17009).setUnlocalizedName("glassShard");
        //public final static Item bloodiedShard = new BloodiedShard(17010).setUnlocalizedName("bloodiedShard");

//        elementalInkWater = new WaterInk(elementalInkWaterItemID).setUnlocalizedName("waterInk");
//        elementalInkFire = new FireInk(elementalInkFireItemID).setUnlocalizedName("fireInk");
//        elementalInkEarth = new EarthInk(elementalInkEarthItemID).setUnlocalizedName("earthInk");
//        elementalInkAir = new AirInk(elementalInkAirItemID).setUnlocalizedName("airInk");

        //fireSpell = new SpellFireBurst(fireSpellItemID).setUnlocalizedName("fireSpell");

        ModItems.init();
        ModBlocks.init();

        //blocks

        //blockConduit = new BlockConduit(blockConduitBlockID);
        proxy.registerRenderers();
        proxy.registerEntities();
        //ItemStacks used for crafting go here
        ItemStack lavaBucketStack = new ItemStack(Item.bucketLava);
        ItemStack cobblestoneStack = new ItemStack(Block.cobblestone);
        ItemStack glassStack = new ItemStack(Block.glass, 1, craftingConstant);
        ItemStack ironStack = new ItemStack(Item.ingotIron);
        ItemStack diamondStack = new ItemStack(Item.diamond, 1, craftingConstant);
        ItemStack woolStack = new ItemStack(Block.cloth);
        ItemStack goldNuggetStack = new ItemStack(Item.goldNugget);
        ItemStack stoneStack = new ItemStack(Block.stone, 1, craftingConstant);
        ItemStack redstoneStack = new ItemStack(Item.redstone);
        ItemStack glowstoneBlockStack = new ItemStack(Block.glowStone);
        ItemStack ironBlockStack = new ItemStack(Block.blockIron);
        ItemStack waterBucketStack = new ItemStack(Item.bucketWater);
        ItemStack emptyBucketStack = new ItemStack(Item.bucketEmpty);
        ItemStack magmaCreamStack = new ItemStack(Item.magmaCream);
        ItemStack stringStack = new ItemStack(Item.silk);
        ItemStack obsidianStack = new ItemStack(Block.obsidian);
        ItemStack diamondSwordStack = new ItemStack(Item.swordDiamond);
        ItemStack goldIngotStack = new ItemStack(Item.ingotGold);
        ItemStack cauldronStack = new ItemStack(Block.cauldron);
        ItemStack furnaceStack = new ItemStack(Block.furnaceIdle);
        ItemStack sugarStack = new ItemStack(Item.sugar);
        ItemStack featherStack = new ItemStack(Item.feather);
        ItemStack ghastTearStack = new ItemStack(Item.ghastTear);
        ItemStack ironPickaxeStack = new ItemStack(Item.pickaxeIron);
        ItemStack ironAxeStack = new ItemStack(Item.axeIron);
        ItemStack ironShovelStack = new ItemStack(Item.shovelIron);
        ItemStack glowstoneDustStack = new ItemStack(Item.glowstone);
        ItemStack saplingStack = new ItemStack(Block.sapling);
        ItemStack reedStack = new ItemStack(Item.reed);
        ItemStack blankSlateStack = new ItemStack(ModItems.blankSlate, 1, craftingConstant);
        //ItemStack glassShardStack = new ItemStack(glassShard);
        ItemStack weakBloodOrbStackCrafted = new ItemStack(ModItems.weakBloodOrb);
        //ItemStack bloodiedShardStack = new ItemStack(bloodiedShard);
        ItemStack reinforcedSlateStack = new ItemStack(ModItems.reinforcedSlate, 1, craftingConstant);
        ItemStack weakBloodOrbStack = new ItemStack(ModItems.weakBloodOrb, 1, craftingConstant);
        ItemStack imbuedSlateStack = new ItemStack(ModItems.imbuedSlate, 1, craftingConstant);
        ItemStack demonSlateStack = new ItemStack(ModItems.demonicSlate, 1, craftingConstant);
        ItemStack apprenticeBloodOrbStack = new ItemStack(ModItems.apprenticeBloodOrb, 1, craftingConstant);
        ItemStack magicianBloodOrbStack = new ItemStack(ModItems.magicianBloodOrb, 1, craftingConstant);
        ItemStack waterSigilStackCrafted = new ItemStack(ModItems.waterSigil);
        ItemStack lavaSigilStackCrafted = new ItemStack(ModItems.lavaSigil);
        ItemStack voidSigilStackCrafted = new ItemStack(ModItems.voidSigil);
        ItemStack airSigilStack = new ItemStack(ModItems.airSigil);
        ItemStack lavaCrystalStackCrafted = new ItemStack(ModItems.lavaCrystal);
        ItemStack lavaCrystalStack = new ItemStack(ModItems.lavaCrystal);
        ItemStack energySwordStack = new ItemStack(ModItems.energySword);
        ItemStack energyBlasterStack = new ItemStack(ModItems.energyBlaster);
        ItemStack sacrificialDaggerStack = new ItemStack(ModItems.sacrificialDagger);
        ItemStack bloodAltarStack = new ItemStack(ModBlocks.blockAltar);
        ItemStack bloodRuneCraftedStack = new ItemStack(ModBlocks.bloodRune, 1);
        ItemStack bloodRuneStack = new ItemStack(ModBlocks.bloodRune);
        ItemStack speedRuneStack = new ItemStack(ModBlocks.speedRune);
        ItemStack efficiencyRuneStack = new ItemStack(ModBlocks.efficiencyRune);
        ItemStack runeOfSacrificeStack = new ItemStack(ModBlocks.runeOfSacrifice);
        ItemStack runeOfSelfSacrificeStack = new ItemStack(ModBlocks.runeOfSelfSacrifice);
        ItemStack miningSigilStackCrafted = new ItemStack(ModItems.sigilOfTheFastMiner);
        ItemStack divinationSigilStackCrafted = new ItemStack(ModItems.divinationSigil);
//        ItemStack elementalInkWaterStack = new ItemStack(elementalInkWater);
//        ItemStack elementalInkFireStack = new ItemStack(elementalInkFire);
//        ItemStack elementalInkEarthStack = new ItemStack(elementalInkEarth);
//        ItemStack elementalInkAirStack = new ItemStack(elementalInkAir);
        ItemStack waterScribeToolStack = new ItemStack(ModItems.waterScribeTool);
        ItemStack fireScribeToolStack = new ItemStack(ModItems.fireScribeTool);
        ItemStack earthScribeToolStack = new ItemStack(ModItems.earthScribeTool);
        ItemStack airScribeToolStack = new ItemStack(ModItems.airScribeTool);
        ItemStack ritualStoneStackCrafted = new ItemStack(ModBlocks.ritualStone, 4);
        ItemStack ritualStoneStack = new ItemStack(ModBlocks.ritualStone);
        ItemStack masterRitualStoneStack = new ItemStack(ModBlocks.blockMasterStone);
        ItemStack imperfectRitualStoneStack = new ItemStack(ModBlocks.imperfectRitualStone);
        ItemStack sigilOfElementalAffinityStackCrafted = new ItemStack(ModItems.sigilOfElementalAffinity);
        ItemStack lavaSigilStack = new ItemStack(ModItems.lavaSigil);
        ItemStack waterSigilStack = new ItemStack(ModItems.waterSigil);
        ItemStack sigilOfHoldingStack = new ItemStack(ModItems.sigilOfHolding);
        ItemStack weakBloodShardStack = new ItemStack(ModItems.weakBloodShard);
        ItemStack emptySocketStack = new ItemStack(ModBlocks.emptySocket);
        ItemStack bloodSocketStack = new ItemStack(ModBlocks.bloodSocket);
        ItemStack armourForgeStack = new ItemStack(ModBlocks.armourForge);
        ItemStack largeBloodStoneBrickStackCrafted = new ItemStack(ModBlocks.largeBloodStoneBrick, 32);
        ItemStack largeBloodStoneBrickStack = new ItemStack(ModBlocks.largeBloodStoneBrick);
        ItemStack bloodStoneBrickStackCrafted = new ItemStack(ModBlocks.bloodStoneBrick, 4);
        ItemStack growthSigilStack = new ItemStack(ModItems.growthSigil);
        ItemStack blockHomHeartStack = new ItemStack(ModBlocks.blockHomHeart);
        ItemStack redWoolStack = new ItemStack(Block.cloth, 1, 14);
        ItemStack zombieHead = new ItemStack(Item.skull, 1, 2);
        ItemStack simpleCatalystStack = new ItemStack(ModItems.simpleCatalyst);
        ItemStack duskRitualDivinerStack = new ItemStack(ModItems.itemRitualDiviner);
        ((ItemRitualDiviner) duskRitualDivinerStack.getItem()).setMaxRuneDisplacement(duskRitualDivinerStack, 1);
        //weakBloodOrbStackCrafted.setItemDamage(weakBloodOrbStackCrafted.getMaxDamage());
        waterSigilStackCrafted.setItemDamage(waterSigilStackCrafted.getMaxDamage());
        lavaSigilStackCrafted.setItemDamage(lavaSigilStackCrafted.getMaxDamage());
        voidSigilStackCrafted.setItemDamage(voidSigilStackCrafted.getMaxDamage());
        lavaCrystalStackCrafted.setItemDamage(lavaCrystalStackCrafted.getMaxDamage());
        miningSigilStackCrafted.setItemDamage(miningSigilStackCrafted.getMaxDamage());
        sigilOfElementalAffinityStackCrafted.setItemDamage(sigilOfElementalAffinityStackCrafted.getMaxDamage());
        ItemStack archmageBloodOrbStack = new ItemStack(ModItems.archmageBloodOrb);
        ItemStack sanctusStack = new ItemStack(ModItems.sanctus);
        ItemStack aetherStack = new ItemStack(ModItems.aether);
        ItemStack terraeStack = new ItemStack(ModItems.terrae);
        ItemStack incendiumStack = new ItemStack(ModItems.incendium);
        ItemStack tennebraeStack = new ItemStack(ModItems.tennebrae);
        ItemStack aquasalusStack = new ItemStack(ModItems.aquasalus);
        ItemStack crystallosStack = new ItemStack(ModItems.crystallos);
        ItemStack crepitousStack = new ItemStack(ModItems.crepitous);
        ItemStack magicalesStack = new ItemStack(ModItems.magicales);
        //All crafting goes here
        // GameRegistry.addRecipe(orbOfTestingStack, "x x", "   ", "x x", 'x', cobblestoneStack);
        //GameRegistry.addRecipe(glassShardStack, " x", "y ", 'x', ironStack, 'y', glassStack);
        //GameRegistry.addRecipe(weakBloodOrbStackCrafted, "xxx", "xdx", "www", 'x', bloodiedShardStack, 'd', diamondStack, 'w', woolStack);
        GameRegistry.addRecipe(sacrificialDaggerStack, "ggg", " dg", "i g", 'g', glassStack, 'd', goldIngotStack, 'i', ironStack);
        //GameRegistry.addRecipe(blankSlateStack, "sgs", "gig", "sgs", 's', stoneStack, 'g', goldNuggetStack, 'i', ironStack);
        //GameRegistry.addRecipe(reinforcedSlateStack, "rir", "ibi", "gig", 'r', redstoneStack, 'i', ironStack, 'b', blankSlateStack, 'g', glowstoneBlockStack);
        GameRegistry.addRecipe(lavaCrystalStackCrafted, "glg", "lbl", "odo", 'g', glassStack, 'l', lavaBucketStack, 'b', weakBloodOrbStack, 'd', diamondStack, 'o', obsidianStack);
        GameRegistry.addRecipe(waterSigilStackCrafted, "www", "wbw", "wow", 'w', waterBucketStack, 'b', blankSlateStack, 'o', weakBloodOrbStack);
        GameRegistry.addRecipe(lavaSigilStackCrafted, "lml", "lbl", "lcl", 'l', lavaBucketStack, 'b', blankSlateStack, 'm', magmaCreamStack, 'c', lavaCrystalStack);
        GameRegistry.addRecipe(voidSigilStackCrafted, "ese", "ere", "eoe", 'e', emptyBucketStack, 'r', reinforcedSlateStack, 'o', apprenticeBloodOrbStack, 's', stringStack);
        GameRegistry.addRecipe(bloodAltarStack, "s s", "scs", "gdg", 's', stoneStack, 'c', furnaceStack, 'd', diamondStack, 'g', goldIngotStack);
        //GameRegistry.addRecipe(energySwordStack, " o ", " o ", " s ", 'o', weakBloodOrbStack, 's', diamondSwordStack);
        //GameRegistry.addRecipe(energyBlasterStack, "oi ", "gdi", " rd", 'o', weakBloodOrbStack, 'i', ironStack, 'd', diamondStack, 'r', reinforcedSlateStack, 'g', goldIngotStack);
        GameRegistry.addRecipe(bloodRuneCraftedStack, "sss", "ror", "sss", 's', stoneStack, 'o', weakBloodOrbStack, 'r', blankSlateStack);
        GameRegistry.addRecipe(speedRuneStack, "sbs", "uru", "sbs", 'u', sugarStack, 's', stoneStack, 'r', bloodRuneStack, 'b', blankSlateStack);
        //GameRegistry.addRecipe(efficiencyRuneStack, "sbs", "rur", "sbs", 'r', redstoneStack, 's', stoneStack, 'u', bloodRuneStack,'b',blankSlateStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.bloodRune, 1, 1), "sbs", "bob", "srs", 's', stoneStack, 'o', magicianBloodOrbStack, 'b', emptyBucketStack, 'r', new ItemStack(ModItems.imbuedSlate));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.bloodRune, 1, 2), "sbs", "bob", "srs", 's', stoneStack, 'o', magicianBloodOrbStack, 'b', waterBucketStack, 'r', new ItemStack(ModItems.imbuedSlate));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.bloodRune, 1, 3), "sws", "ror", "sws", 's', stoneStack, 'o', new ItemStack(ModItems.masterBloodOrb), 'w', weakBloodOrbStack, 'r', new ItemStack(ModItems.demonicSlate));
        GameRegistry.addRecipe(airSigilStack, "fgf", "fsf", "fof", 'f', featherStack, 'g', ghastTearStack, 's', reinforcedSlateStack, 'o', apprenticeBloodOrbStack);
        GameRegistry.addRecipe(miningSigilStackCrafted, "sps", "hra", "sos", 'o', apprenticeBloodOrbStack, 's', stoneStack, 'p', ironPickaxeStack, 'h', ironShovelStack, 'a', ironAxeStack, 'r', reinforcedSlateStack);
        GameRegistry.addRecipe(runeOfSacrificeStack, "srs", "gog", "srs", 's', stoneStack, 'g', goldIngotStack, 'o', apprenticeBloodOrbStack, 'r', reinforcedSlateStack);
        GameRegistry.addRecipe(runeOfSelfSacrificeStack, "srs", "gog", "srs", 's', stoneStack, 'g', glowstoneDustStack, 'o', apprenticeBloodOrbStack, 'r', reinforcedSlateStack);
        GameRegistry.addRecipe(divinationSigilStackCrafted, "ggg", "gsg", "gog", 'g', glassStack, 's', blankSlateStack, 'o', weakBloodOrbStack);
//        GameRegistry.addRecipe(waterScribeToolStack, "f", "i", 'f', featherStack, 'i', elementalInkWaterStack);
//        GameRegistry.addRecipe(fireScribeToolStack, "f", "i", 'f', featherStack, 'i', elementalInkFireStack);
//        GameRegistry.addRecipe(earthScribeToolStack, "f", "i", 'f', featherStack, 'i', elementalInkEarthStack);
//        GameRegistry.addRecipe(airScribeToolStack, "f", "i", 'f', featherStack, 'i', elementalInkAirStack);
        GameRegistry.addRecipe(ritualStoneStackCrafted, "srs", "ror", "srs", 's', obsidianStack, 'o', apprenticeBloodOrbStack, 'r', reinforcedSlateStack);
        GameRegistry.addRecipe(masterRitualStoneStack, "brb", "ror", "brb", 'b', obsidianStack, 'o', magicianBloodOrbStack, 'r', ritualStoneStack);
        GameRegistry.addRecipe(imperfectRitualStoneStack, "bsb", "sos", "bsb", 's', stoneStack, 'b', obsidianStack, 'o', weakBloodOrbStack);
        GameRegistry.addRecipe(sigilOfElementalAffinityStackCrafted, "oao", "wsl", "oro", 'o', obsidianStack, 'a', airSigilStack, 'w', waterSigilStack, 'l', lavaSigilStack, 'r', magicianBloodOrbStack, 's', imbuedSlateStack);
        GameRegistry.addRecipe(sigilOfHoldingStack, "asa", "srs", "aoa", 'a', blankSlateStack, 's', stoneStack, 'r', imbuedSlateStack, 'o', magicianBloodOrbStack);
        GameRegistry.addRecipe(emptySocketStack, "bgb", "gdg", "bgb", 'b', weakBloodShardStack, 'g', glassStack, 'd', diamondStack);
        GameRegistry.addRecipe(armourForgeStack, "sfs", "fof", "sfs", 'f', bloodSocketStack, 's', stoneStack, 'o', magicianBloodOrbStack);
        GameRegistry.addShapelessRecipe(largeBloodStoneBrickStackCrafted, weakBloodShardStack, stoneStack);
        GameRegistry.addRecipe(bloodStoneBrickStackCrafted, "bb", "bb", 'b', largeBloodStoneBrickStack);
        GameRegistry.addRecipe(growthSigilStack, "srs", "rer", "sos", 's', saplingStack, 'r', reedStack, 'o', apprenticeBloodOrbStack, 'e', reinforcedSlateStack);
        GameRegistry.addRecipe(blockHomHeartStack, "www", "srs", "sos", 'w', redWoolStack, 's', stoneStack, 'r', bloodRuneStack, 'o', apprenticeBloodOrbStack);
        GameRegistry.addShapelessRecipe(new ItemStack(Item.skull, 1, 2), new ItemStack(Item.skull, 1, 1), new ItemStack(Item.rottenFlesh), new ItemStack(Item.ingotIron), new ItemStack(Item.leather));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.skull, 1, 0), new ItemStack(Item.skull, 1, 1), new ItemStack(Item.bow, 1, 0), new ItemStack(Item.arrow, 1, 0), new ItemStack(Item.bone));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.skull, 1, 4), new ItemStack(Item.skull, 1, 1), new ItemStack(Item.gunpowder), new ItemStack(Block.dirt), new ItemStack(Block.sand));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockWritingTable), " s ", "ror", 's', new ItemStack(Item.brewingStand), 'r', obsidianStack, 'o', weakBloodOrbStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockPedestal), "ooo", " c ", "ooo", 'o', obsidianStack, 'c', weakBloodShardStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockPlinth), "iii", " p ", "iii", 'i', ironBlockStack, 'p', new ItemStack(ModBlocks.blockPedestal));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.alchemyFlask, 1, 0), new ItemStack(ModItems.alchemyFlask, 1, craftingConstant), new ItemStack(Item.netherStalkSeeds), redstoneStack, glowstoneDustStack);
        GameRegistry.addRecipe(new ItemStack(ModItems.sigilOfHaste), "csc", "sts", "ror", 'c', new ItemStack(Item.cookie), 's', new ItemStack(Item.sugar), 't', ModItems.demonicSlate, 'r', obsidianStack, 'o', new ItemStack(ModItems.masterBloodOrb));
        GameRegistry.addRecipe(new ItemStack(ModItems.sigilOfWind), "faf", "grg", "fof", 'f', featherStack, 'g', ghastTearStack, 'a', new ItemStack(ModItems.airSigil), 'o', new ItemStack(ModItems.masterBloodOrb), 'r', ModItems.demonicSlate);
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.weakBloodShard, 5, 0), new ItemStack(ModItems.masterBloodOrb), new ItemStack(ModItems.weakBloodShard), imbuedSlateStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockTeleposer), "ggg", "efe", "ggg", 'g', goldIngotStack, 'f', new ItemStack(ModItems.telepositionFocus), 'e', new ItemStack(Item.enderPearl));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.reinforcedTelepositionFocus), new ItemStack(ModItems.enhancedTelepositionFocus), new ItemStack(ModItems.weakBloodShard));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.demonicTelepositionFocus), new ItemStack(ModItems.reinforcedTelepositionFocus), new ItemStack(ModItems.demonBloodShard));
        GameRegistry.addRecipe(new ItemStack(ModItems.sigilOfTheBridge), "nnn", "nsn", "ror", 'n', stoneStack, 'r', new ItemStack(Block.slowSand), 's', imbuedSlateStack, 'o', magicianBloodOrbStack);
        GameRegistry.addRecipe(new ItemStack(ModItems.armourInhibitor), " gg", "gsg", "gg ", 'g', goldIngotStack, 's', new ItemStack(ModItems.weakBloodShard));
        GameRegistry.addRecipe(new ItemStack(ModItems.itemRitualDiviner), "d1d", "2e3", "d4d", '1', new ItemStack(ModItems.airScribeTool), '2', new ItemStack(ModItems.waterScribeTool), '3', new ItemStack(ModItems.fireScribeTool), '4', new ItemStack(ModItems.earthScribeTool), 'd', diamondStack, 'e', new ItemStack(Item.emerald));
        GameRegistry.addRecipe(duskRitualDivinerStack, " d ", "srs", " d ", 'd', new ItemStack(ModItems.duskScribeTool), 's', new ItemStack(ModItems.demonicSlate), 'r', new ItemStack(ModItems.itemRitualDiviner));
        GameRegistry.addRecipe(new ItemStack(ModItems.sigilOfMagnetism), "bgb", "gsg", "bob", 'b', new ItemStack(Block.blockIron), 'g', goldIngotStack, 's', new ItemStack(ModItems.imbuedSlate), 'o', magicianBloodOrbStack);
        GameRegistry.addRecipe(new ItemStack(ModItems.energyBazooka), "Ocd", "cb ", "d w", 'O', archmageBloodOrbStack, 'c', crepitousStack, 'b', new ItemStack(ModItems.energyBlaster), 'd', diamondStack, 'w', new ItemStack(ModItems.weakBloodShard));
        GameRegistry.addRecipe(new ItemStack(ModItems.itemBloodLightSigil), "btb", "sss", "bob", 'o', magicianBloodOrbStack, 'b', glowstoneBlockStack, 't', new ItemStack(Block.torchWood), 's', imbuedSlateStack);
        GameRegistry.addRecipe(new ItemStack(ModItems.itemKeyOfDiablo), " gw", "gdg", "wg ", 'w', weakBloodShardStack, 'g', goldIngotStack, 'd', diamondStack);
        customPotionDrowning = (new PotionDrowning(customPotionDrowningID, true, 0)).setIconIndex(0, 0).setPotionName("Drowning");
        customPotionBoost = (new PotionBoost(customPotionBoostID, false, 0)).setIconIndex(0, 0).setPotionName("Boost");
        customPotionProjProt = (new PotionProjectileProtect(customPotionProjProtID, false, 0)).setIconIndex(0, 0).setPotionName("Whirlwind");
        customPotionInhibit = (new PotionInhibit(customPotionInhibitID, false, 0)).setIconIndex(0, 0).setPotionName("Inhibit");
        customPotionFlight = (new PotionFlight(customPotionFlightID, false, 0)).setIconIndex(0, 0).setPotionName("Flight");
        customPotionReciprocation = (new PotionReciprocation(customPotionReciprocationID, false, 0xFFFFFF)).setIconIndex(0, 0).setPotionName("Reciprocation");
        //All items registered go here
        //LanguageRegistry.addName(orbOfTesting, "Orb of Testing");
        LanguageRegistry.addName(ModItems.weakBloodOrb, "Weak Blood Orb");
        LanguageRegistry.addName(ModItems.apprenticeBloodOrb, "Apprentice Blood Orb");
        LanguageRegistry.addName(ModItems.magicianBloodOrb, "Magician's Blood Orb");
        LanguageRegistry.addName(ModItems.archmageBloodOrb, "Archmage's Blood Orb");
        LanguageRegistry.addName(ModItems.energyBlaster, "Energy Blaster");
        LanguageRegistry.addName(ModItems.energySword, "Bound Blade");
        LanguageRegistry.addName(ModItems.lavaCrystal, "Lava Crystal");
        LanguageRegistry.addName(ModItems.waterSigil, "Water Sigil");
        LanguageRegistry.addName(ModItems.lavaSigil, "Lava Sigil");
        LanguageRegistry.addName(ModItems.voidSigil, "Void Sigil");
        //LanguageRegistry.addName(glassShard, "Glass Shard");
        //LanguageRegistry.addName(bloodiedShard, "Bloodied Shard");
        LanguageRegistry.addName(ModItems.blankSlate, "Blank Slate");
        LanguageRegistry.addName(ModItems.reinforcedSlate, "Reinforced Slate");
        LanguageRegistry.addName(ModItems.sacrificialDagger, "Sacrificial Knife");
        LanguageRegistry.addName(ModItems.daggerOfSacrifice, "Dagger of Sacrifice");
        LanguageRegistry.addName(ModItems.airSigil, "Air Sigil");
        LanguageRegistry.addName(ModItems.sigilOfTheFastMiner, "Sigil of the Fast Miner");
        LanguageRegistry.addName(ModItems.sigilOfElementalAffinity, "Sigil of Elemental Affinity");
        LanguageRegistry.addName(ModItems.sigilOfHaste, "Sigil of Haste");
        LanguageRegistry.addName(ModItems.sigilOfHolding, "Sigil of Holding");
        LanguageRegistry.addName(ModItems.growthSigil, "Sigil of the Green Grove");
//        LanguageRegistry.addName(elementalInkWater, "Elemental Ink: Water");
//        LanguageRegistry.addName(elementalInkFire, "Elemental Ink: Fire");
//        LanguageRegistry.addName(elementalInkEarth, "Elemental Ink: Earth");
//        LanguageRegistry.addName(elementalInkAir, "Elemental Ink: Air");
        LanguageRegistry.addName(ModItems.divinationSigil, "Divination Sigil");
        LanguageRegistry.addName(new ItemStack(ModItems.activationCrystal, 1, 0), "Weak Activation Crystal");
        LanguageRegistry.addName(new ItemStack(ModItems.activationCrystal, 1, 1), "Awakened Activation Crystal");
        LanguageRegistry.addName(ModItems.waterScribeTool, "Elemental Inscription Tool: Water");
        LanguageRegistry.addName(ModItems.fireScribeTool, "Elemental Inscription Tool: Fire");
        LanguageRegistry.addName(ModItems.earthScribeTool, "Elemental Inscription Tool: Earth");
        LanguageRegistry.addName(ModItems.airScribeTool, "Elemental Inscription Tool: Air");
        LanguageRegistry.addName(ModItems.boundPickaxe, "Bound Pickaxe");
        LanguageRegistry.addName(ModItems.boundAxe, "Bound Axe");
        LanguageRegistry.addName(ModItems.boundShovel, "Bound Shovel");
        LanguageRegistry.addName(ModItems.boundHelmet, "Bound Helmet");
        LanguageRegistry.addName(ModItems.boundPlate, "Bound Chestplate");
        LanguageRegistry.addName(ModItems.boundLeggings, "Bound Leggings");
        LanguageRegistry.addName(ModItems.boundBoots, "Bound Boots");
        LanguageRegistry.addName(ModItems.weakBloodShard, "Weak Blood Shard");
        LanguageRegistry.addName(ModItems.blankSpell, "Unbound Crystal");
        LanguageRegistry.addName(ModItems.masterBloodOrb, "Master Blood Orb");
        LanguageRegistry.addName(ModItems.alchemyFlask, "Potion Flask");
        LanguageRegistry.addName(ModItems.mundanePowerCatalyst, "Mundane Power Catalyst");
        LanguageRegistry.addName(ModItems.averagePowerCatalyst, "Average Power Catalyst");
        LanguageRegistry.addName(ModItems.greaterPowerCatalyst, "Greater Power Catalyst");
        LanguageRegistry.addName(ModItems.mundaneLengtheningCatalyst, "Mundane Lengthening Catalyst");
        LanguageRegistry.addName(ModItems.averageLengtheningCatalyst, "Average Lengthening Catalyst");
        LanguageRegistry.addName(ModItems.greaterLengtheningCatalyst, "Greater Lengthening Catalyst");
        LanguageRegistry.addName(ModItems.standardBindingAgent, "Standard Binding Agent");
        LanguageRegistry.addName(ModItems.incendium, "Incendium");
        LanguageRegistry.addName(ModItems.magicales, "Magicales");
        LanguageRegistry.addName(ModItems.sanctus, "Sanctus");
        LanguageRegistry.addName(ModItems.aether, "Aether");
        LanguageRegistry.addName(ModItems.simpleCatalyst, "Simple Catalyst");
        LanguageRegistry.addName(ModItems.crepitous, "Crepitous");
        LanguageRegistry.addName(ModItems.crystallos, "Crystallos");
        LanguageRegistry.addName(ModItems.terrae, "Terrae");
        LanguageRegistry.addName(ModItems.aquasalus, "Aquasalus");
        LanguageRegistry.addName(ModItems.tennebrae, "Tennebrae");
        LanguageRegistry.addName(ModItems.sigilOfWind, "Sigil of the Whirlwind");
        LanguageRegistry.addName(ModItems.telepositionFocus, "Teleposition Focus");
        LanguageRegistry.addName(ModItems.enhancedTelepositionFocus, "Enhanced Teleposition Focus");
        LanguageRegistry.addName(ModItems.reinforcedTelepositionFocus, "Reinforced Teleposition Focus");
        LanguageRegistry.addName(ModItems.demonicTelepositionFocus, "Demonic Teleposition Focus");
        LanguageRegistry.addName(ModItems.imbuedSlate, "Imbued Slate");
        LanguageRegistry.addName(ModItems.demonicSlate, "Demonic Slate");
        LanguageRegistry.addName(ModItems.duskScribeTool, "Elemental Inscription Tool: Dusk");
        LanguageRegistry.addName(ModItems.sigilOfTheBridge, "Sigil of the Phantom Bridge");
        LanguageRegistry.addName(ModItems.armourInhibitor, "Armour Inhibitor");
        LanguageRegistry.addName(ModItems.creativeFiller, "Orb of Testing");
        LanguageRegistry.addName(ModItems.weakFillingAgent, "Weak Filling Agent");
        LanguageRegistry.addName(ModItems.standardFillingAgent, "Standard Filling Agent");
        LanguageRegistry.addName(ModItems.enhancedFillingAgent, "Enhanced Filling Agent");
        LanguageRegistry.addName(ModItems.weakBindingAgent, "Weak Binding Agent");
        LanguageRegistry.addName(ModItems.itemRitualDiviner, "Ritual Diviner");
        LanguageRegistry.addName(ModItems.sigilOfMagnetism, "Sigil of Magnetism");
        LanguageRegistry.addName(ModItems.itemKeyOfDiablo, "Key of Binding");
        LanguageRegistry.addName(ModItems.energyBazooka, "Energy Bazooka");
        LanguageRegistry.addName(ModItems.itemBloodLightSigil, "Sigil of the Blood Lamp");
        LanguageRegistry.addName(ModItems.demonBloodShard, "Demon Blood Shard");
        //FluidStack lifeEssenceFluidStack = new FluidStack(lifeEssenceFluid, 1);
        //LiquidStack lifeEssence = new LiquidStack(lifeEssenceFlowing, 1);
        //LiquidDictionary.getOrCreateLiquid("Life Essence", lifeEssence);
        FluidRegistry.registerFluid(lifeEssenceFluid);
        ModBlocks.blockLifeEssence = new LifeEssenceBlock(lifeEssenceFluidID);
        ModBlocks.blockLifeEssence.setUnlocalizedName("lifeEssenceBlock");
        bucketLife = (new LifeBucket(bucketLifeItemID, ModBlocks.blockLifeEssence.blockID)).setUnlocalizedName("bucketLife").setContainerItem(Item.bucketEmpty).setCreativeTab(CreativeTabs.tabMisc);
        FluidContainerRegistry.registerFluidContainer(lifeEssenceFluid, new ItemStack(bucketLife), FluidContainerRegistry.EMPTY_BUCKET);
        //lifeEssenceFluid.setUnlocalizedName("lifeEssence");
        //LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Life Essence", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(AlchemicalWizardry.bucketLife), new ItemStack(Item.bucketEmpty)));
        //GameRegistry.registerBlock(testingBlock, "testingBlock");
        //LanguageRegistry.addName(testingBlock, "Testing Block");
        //MinecraftForge.setBlockHarvestLevel(testingBlock, "pickaxe", 0);
        GameRegistry.registerBlock(ModBlocks.blockAltar, "bloodAltar");
        LanguageRegistry.addName(ModBlocks.blockAltar, "Blood Altar");
        GameRegistry.registerBlock(ModBlocks.blockLifeEssence, "lifeEssence");
        LanguageRegistry.addName(ModBlocks.blockLifeEssence, "Life Essence");
        MinecraftForge.setBlockHarvestLevel(ModBlocks.blockAltar, "pickaxe", 1);
        //Register Tile Entity
        GameRegistry.registerTileEntity(TEAltar.class, "containerAltar");
        GameRegistry.registerTileEntity(TEMasterStone.class, "containerMasterStone");
        GameRegistry.registerTileEntity(TESocket.class, "containerSocket");
        GameRegistry.registerTileEntity(TEWritingTable.class, "containerWritingTable");
        GameRegistry.registerTileEntity(TEHomHeart.class, "containerHomHeart");
        GameRegistry.registerTileEntity(TEPedestal.class, "containerPedestal");
        GameRegistry.registerTileEntity(TEPlinth.class, "containerPlinth");
        GameRegistry.registerTileEntity(TETeleposer.class, "containerTeleposer");
        GameRegistry.registerTileEntity(TEConduit.class, "containerConduit");
        //
        GameRegistry.registerBlock(ModBlocks.bloodRune, ItemBloodRuneBlock.class, "AlchemicalWizardry" + (ModBlocks.bloodRune.getUnlocalizedName().substring(5)));
        LanguageRegistry.addName(new ItemStack(ModBlocks.bloodRune, 1, 0), "Blood Rune");
        LanguageRegistry.addName(new ItemStack(ModBlocks.bloodRune, 1, 1), "Rune of Augmented Capacity");
        LanguageRegistry.addName(new ItemStack(ModBlocks.bloodRune, 1, 2), "Rune of Dislocation");
        LanguageRegistry.addName(new ItemStack(ModBlocks.bloodRune, 1, 3), "Rune of the Orb");
        GameRegistry.registerBlock(ModBlocks.speedRune, "speedRune");
        LanguageRegistry.addName(ModBlocks.speedRune, "Speed Rune");
        GameRegistry.registerBlock(ModBlocks.efficiencyRune, "efficiencyRune");
        LanguageRegistry.addName(ModBlocks.efficiencyRune, "Efficiency Rune");
        GameRegistry.registerBlock(ModBlocks.runeOfSacrifice, "runeOfSacrifice");
        LanguageRegistry.addName(ModBlocks.runeOfSacrifice, "Rune of Sacrifice");
        GameRegistry.registerBlock(ModBlocks.runeOfSelfSacrifice, "runeOfSelfSacrifice");
        LanguageRegistry.addName(ModBlocks.runeOfSelfSacrifice, "Rune of Self-sacrifice");
//        GameRegistry.registerBlock(lifeEssenceStill, "lifeEssenceStill");
//        GameRegistry.registerBlock(lifeEssenceFlowing, "lifeEssenceFlowing");
        //LanguageRegistry.addName(lifeEssenceStill, "Life Essence");
        LanguageRegistry.addName(bucketLife, "Bucket of Life");
        GameRegistry.registerBlock(ModBlocks.ritualStone, "ritualStone");
        GameRegistry.registerBlock(ModBlocks.blockMasterStone, "masterStone");
        GameRegistry.registerBlock(ModBlocks.bloodSocket, "bloodSocket");
        LanguageRegistry.addName(ModBlocks.blockMasterStone, "Master Ritual Stone");
        GameRegistry.registerBlock(ModBlocks.imperfectRitualStone, "imperfectRitualStone");
        LanguageRegistry.addName(ModBlocks.imperfectRitualStone, "Imperfect Ritual Stone");
        LanguageRegistry.addName(ModBlocks.ritualStone, "Ritual Stone");
        LanguageRegistry.addName(ModBlocks.armourForge, "Soul Armour Forge");
        LanguageRegistry.addName(ModBlocks.emptySocket, "Empty Socket");
        LanguageRegistry.addName(ModBlocks.bloodSocket, "Filled Socket");
        LanguageRegistry.addName(ModBlocks.bloodStoneBrick, "Bloodstone Brick");
        LanguageRegistry.addName(ModBlocks.largeBloodStoneBrick, "Large Bloodstone Brick");
        LanguageRegistry.addName(ModBlocks.blockHomHeart, "Spell Table");
        LanguageRegistry.addName(ModBlocks.blockPedestal, "Arcane Pedestal");
        LanguageRegistry.addName(ModBlocks.blockPlinth, "Arcane Plinth");
        LanguageRegistry.addName(ModBlocks.blockWritingTable, "Alchemic Chemistry Set");
        LanguageRegistry.addName(ModBlocks.blockTeleposer, "Teleposer");
        LanguageRegistry.addName(ModBlocks.spectralBlock, "Spectral Block");
        LanguageRegistry.addName(ModBlocks.blockBloodLight, "Blood Light");
        GameRegistry.registerBlock(ModBlocks.armourForge, "armourForge");
        GameRegistry.registerBlock(ModBlocks.emptySocket, "emptySocket");
        GameRegistry.registerBlock(ModBlocks.bloodStoneBrick, "bloodStoneBrick");
        GameRegistry.registerBlock(ModBlocks.largeBloodStoneBrick, "largeBloodStoneBrick");
        GameRegistry.registerBlock(ModBlocks.blockWritingTable, "blockWritingTable");
        GameRegistry.registerBlock(ModBlocks.blockHomHeart, "blockHomHeart");
        GameRegistry.registerBlock(ModBlocks.blockPedestal, "blockPedestal");
        GameRegistry.registerBlock(ModBlocks.blockPlinth, "blockPlinth");
        GameRegistry.registerBlock(ModBlocks.blockTeleposer, "blockTeleposer");
        GameRegistry.registerBlock(ModBlocks.spectralBlock, "spectralBlock");
        GameRegistry.registerBlock(ModBlocks.blockBloodLight, "bloodLight");
        //GameRegistry.registerBlock(blockConduit,"blockConduit");
        MinecraftForge.setBlockHarvestLevel(ModBlocks.bloodRune, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.speedRune, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.efficiencyRune, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.runeOfSacrifice, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.runeOfSelfSacrifice, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.ritualStone, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.bloodSocket, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.ritualStone, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.imperfectRitualStone, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.blockMasterStone, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.emptySocket, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.bloodStoneBrick, "pickaxe", 0);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.largeBloodStoneBrick, "pickaxe", 0);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.blockWritingTable, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.blockHomHeart, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.blockPedestal, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.blockPlinth, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(ModBlocks.blockTeleposer, "pickaxe", 2);
        //Fuel handler
        GameRegistry.registerFuelHandler(new AlchemicalWizardryFuelHandler());
        //EntityRegistry.registerModEntity(EnergyBlastProjectile.class, "BlasterProj", 0, this, 128, 5, true);
        proxy.registerEntityTrackers();
        //Gui registration
        // NetworkRegistry.instance().registerGuiHandler(this, new GuiHandlerAltar());
        Rituals.loadRituals();
        UpgradedAltars.loadAltars();
        SigilOfHolding.initiateSigilOfHolding();
        ArmourForge.initializeRecipes();
        TEPlinth.initialize();
        AlchemicalPotionCreationHandler.initializePotions();
        MinecraftForge.setToolClass(ModItems.boundPickaxe, "pickaxe", 5);
        MinecraftForge.setToolClass(ModItems.boundAxe, "axe", 5);
        MinecraftForge.setToolClass(ModItems.boundShovel, "shovel", 5);
        MinecraftForge.EVENT_BUS.register(new ModLivingDropsEvent());
        proxy.InitRendering();
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
//        ItemStack[] comp = new ItemStack[5];
//        for(int i=0;i<5;i++)
//        {
//        	comp[i] = redstoneStack;
//        }
//        AlchemyRecipeRegistry.registerRecipe(glowstoneDustStack, 2, comp, 2);
        //TODO NEW RECIPES!
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.weakBindingAgent), 10, new ItemStack[]{simpleCatalystStack, simpleCatalystStack, new ItemStack(Item.clay)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.standardBindingAgent), 15, new ItemStack[]{new ItemStack(ModItems.weakBindingAgent), sanctusStack, new ItemStack(ModItems.crystallos)}, 3);
        AlchemyRecipeRegistry.registerRecipe(simpleCatalystStack, 2, new ItemStack[]{sugarStack, redstoneStack, redstoneStack, glowstoneDustStack, new ItemStack(Item.gunpowder)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.incendium), 5, new ItemStack[]{lavaBucketStack, new ItemStack(Item.blazePowder), new ItemStack(Item.blazePowder), new ItemStack(Block.netherrack), simpleCatalystStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.aether), 5, new ItemStack[]{featherStack, featherStack, glowstoneDustStack, ghastTearStack, simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.sanctus), 5, new ItemStack[]{glowstoneDustStack, new ItemStack(Item.goldNugget), glowstoneDustStack, glassStack, simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.crepitous), 5, new ItemStack[]{new ItemStack(Item.gunpowder), new ItemStack(Item.gunpowder), cobblestoneStack, cobblestoneStack, simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.crystallos), 5, new ItemStack[]{new ItemStack(Block.ice), new ItemStack(Block.ice), new ItemStack(Block.blockSnow), new ItemStack(Block.blockSnow), simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.terrae), 5, new ItemStack[]{new ItemStack(Block.dirt), new ItemStack(Block.sand), obsidianStack, obsidianStack, simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.aquasalus), 5, new ItemStack[]{simpleCatalystStack, new ItemStack(Item.dyePowder, 1, 0), new ItemStack(Item.potion, 1, 0), new ItemStack(Item.potion, 1, 0), new ItemStack(Item.potion, 1, 0)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.tennebrae), 5, new ItemStack[]{simpleCatalystStack, new ItemStack(Item.coal), new ItemStack(Item.coal), new ItemStack(Block.obsidian), new ItemStack(Item.clay)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.magicales), 5, new ItemStack[]{redstoneStack, simpleCatalystStack, new ItemStack(Item.gunpowder), new ItemStack(Item.glowstone), new ItemStack(Item.glowstone)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.mundanePowerCatalyst), 10, new ItemStack[]{glowstoneDustStack, glowstoneDustStack, glowstoneDustStack, new ItemStack(ModItems.weakBindingAgent), simpleCatalystStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.mundaneLengtheningCatalyst), 10, new ItemStack[]{redstoneStack, redstoneStack, redstoneStack, new ItemStack(ModItems.weakBindingAgent), simpleCatalystStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.averagePowerCatalyst), 20, new ItemStack[]{new ItemStack(ModItems.mundanePowerCatalyst), new ItemStack(ModItems.mundanePowerCatalyst), new ItemStack(ModItems.standardBindingAgent)}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.averageLengtheningCatalyst), 20, new ItemStack[]{new ItemStack(ModItems.mundaneLengtheningCatalyst), new ItemStack(ModItems.mundaneLengtheningCatalyst), new ItemStack(ModItems.standardBindingAgent)}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.greaterPowerCatalyst), 30, new ItemStack[]{new ItemStack(ModItems.averagePowerCatalyst), new ItemStack(ModItems.averagePowerCatalyst), new ItemStack(ModItems.incendium)}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.greaterLengtheningCatalyst), 30, new ItemStack[]{new ItemStack(ModItems.averageLengtheningCatalyst), new ItemStack(ModItems.averageLengtheningCatalyst), new ItemStack(ModItems.aquasalus)}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.weakFillingAgent), 5, new ItemStack[]{simpleCatalystStack, new ItemStack(Item.netherStalkSeeds), redstoneStack, glowstoneDustStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.standardFillingAgent), 10, new ItemStack[]{new ItemStack(ModItems.weakFillingAgent), new ItemStack(ModItems.terrae)}, 3);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.enhancedFillingAgent), 25, new ItemStack[]{new ItemStack(ModItems.standardFillingAgent), new ItemStack(ModItems.aquasalus), new ItemStack(ModItems.magicales)}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.activationCrystal, 1, 1), 100, new ItemStack[]{new ItemStack(ModItems.activationCrystal, 1, 0), new ItemStack(ModItems.demonBloodShard), incendiumStack, aquasalusStack, aetherStack}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.activationCrystal, 1, 1), 100, new ItemStack[]{new ItemStack(ModItems.activationCrystal, 1, 0), new ItemStack(Item.netherStar), incendiumStack, aquasalusStack, aetherStack}, 4);
        HomSpellRegistry.registerBasicSpell(new ItemStack(Item.flintAndSteel), new SpellFireBurst());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Block.ice), new SpellFrozenWater());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Block.tnt), new SpellExplosions());
        HomSpellRegistry.registerBasicSpell(new ItemStack(ModItems.apprenticeBloodOrb), new SpellHolyBlast());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Item.ghastTear), new SpellWindGust());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Item.glowstone), new SpellLightningBolt());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Item.bucketWater), new SpellWateryGrave());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Block.obsidian), new SpellEarthBender());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Item.enderPearl), new SpellTeleport());
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityFallenAngelID), new ItemStack[]{sanctusStack, sanctusStack, sanctusStack, aetherStack, tennebraeStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityLowerGuardianID), new ItemStack[]{cobblestoneStack, cobblestoneStack, terraeStack, tennebraeStack, new ItemStack(Item.ingotIron), new ItemStack(Item.goldNugget)}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityBileDemonID), new ItemStack[]{new ItemStack(Item.poisonousPotato), tennebraeStack, terraeStack, new ItemStack(Item.porkRaw), new ItemStack(Item.egg), new ItemStack(Item.beefRaw)}, new ItemStack[]{crepitousStack, crepitousStack, terraeStack, ironBlockStack, ironBlockStack, diamondStack}, new ItemStack[]{}, 0, 5);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityWingedFireDemonID), new ItemStack[]{aetherStack, incendiumStack, incendiumStack, incendiumStack, tennebraeStack, new ItemStack(Block.netherrack)}, new ItemStack[]{diamondStack, new ItemStack(Block.blockGold), magicalesStack, magicalesStack, new ItemStack(Item.fireballCharge), new ItemStack(Block.coalBlock)}, new ItemStack[]{}, 0, 5);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entitySmallEarthGolemID), new ItemStack[]{new ItemStack(Item.clay), terraeStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityIceDemonID), new ItemStack[]{crystallosStack, crystallosStack, aquasalusStack, crystallosStack, sanctusStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityBoulderFistID), new ItemStack[]{terraeStack, sanctusStack, tennebraeStack, new ItemStack(Item.bone), new ItemStack(Item.beefCooked), new ItemStack(Item.beefCooked)}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityShadeID), new ItemStack[]{tennebraeStack, tennebraeStack, tennebraeStack, aetherStack, glassStack, new ItemStack(Item.glassBottle)}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityAirElementalID), new ItemStack[]{aetherStack, aetherStack, aetherStack, aetherStack, aetherStack, aetherStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityWaterElementalID), new ItemStack[]{aquasalusStack, aquasalusStack, aquasalusStack, aquasalusStack, aquasalusStack, aquasalusStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityEarthElementalID), new ItemStack[]{terraeStack, terraeStack, terraeStack, terraeStack, terraeStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityFireElementalID), new ItemStack[]{incendiumStack, incendiumStack, incendiumStack, incendiumStack, incendiumStack, incendiumStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        //TODO SummoningRegistry.registerSummon(new SummoningHelper(this.entityShadeElementalID), new ItemStack[]{tennebraeStack,tennebraeStack,tennebraeStack,tennebraeStack,tennebraeStack,tennebraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityHolyElementalID), new ItemStack[]{sanctusStack, sanctusStack, sanctusStack, sanctusStack, sanctusStack, sanctusStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        //Custom mobs
        EntityRegistry.registerModEntity(EntityFallenAngel.class, "FallenAngel", this.entityFallenAngelID, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityLowerGuardian.class, "LowerGuardian", this.entityLowerGuardianID, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityBileDemon.class, "BileDemon", this.entityBileDemonID, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityWingedFireDemon.class, "WingedFireDemon", this.entityWingedFireDemonID, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntitySmallEarthGolem.class, "SmallEarthGolem", this.entitySmallEarthGolemID, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityIceDemon.class, "IceDemon", this.entityIceDemonID, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityBoulderFist.class, "BoulderFist", this.entityBoulderFistID, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityShade.class, "Shade", this.entityShadeID, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityAirElemental.class, "AirElemental", this.entityAirElementalID, this, 120, 3, true);
        EntityRegistry.registerModEntity(EntityWaterElemental.class, "WaterElemental", this.entityWaterElementalID, this, 120, 3, true);
        EntityRegistry.registerModEntity(EntityEarthElemental.class, "EarthElemental", this.entityEarthElementalID, this, 120, 3, true);
        EntityRegistry.registerModEntity(EntityFireElemental.class, "FireElemental", this.entityFireElementalID, this, 120, 3, true);
        EntityRegistry.registerModEntity(EntityShadeElemental.class, "ShadeElemental", this.entityShadeElementalID, this, 120, 3, true);
        EntityRegistry.registerModEntity(EntityHolyElemental.class, "HolyElemental", this.entityHolyElementalID, this, 120, 3, true);
        //EntityRegistry.addSpawn(EntityFallenAngel.class, 5, 1, 5, EnumCreatureType.creature, BiomeGenBase.biomeList);
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.FallenAngel.name", "en_US", "Fallen Angel");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.LowerGuardian.name", "en_US", "Lower Stone Guardian");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.BileDemon.name", "en_US", "Bile Demon");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.WingedFireDemon.name", "en_US", "Winged Fire Demon");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.SmallEarthGolem.name", "en_US", "Small Earth Golem");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.IceDemon.name", "en_US", "Ice Demon");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.BoulderFist.name", "en_US", "Boulder Fist");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.Shade.name", "en_US", "Shade");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.AirElemental.name", "en_US", "Air Elemental");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.WaterElemental.name", "en_US", "Water Elemental");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.EarthElemental.name", "en_US", "Earth Elemental");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.FireElemental.name", "en_US", "Fire Elemental");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.ShadeElemental.name", "en_US", "Shade Elemental");
        LanguageRegistry.instance().addStringLocalization("entity.AlchemicalWizardry.HolyElemental.name", "en_US", "Holy Elemental");
        LanguageRegistry.instance().addStringLocalization("itemGroup.tabBloodMagic", "en_US", "Blood Magic");
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.standardBindingAgent), 1, 3, this.standardBindingAgentDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.mundanePowerCatalyst), 1, 1, this.mundanePowerCatalystDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.mundaneLengtheningCatalyst), 1, 1, this.mundaneLengtheningCatalystDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.averagePowerCatalyst), 1, 1, this.averagePowerCatalystDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.averageLengtheningCatalyst), 1, 1, this.averageLengtheningCatalystDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.greaterPowerCatalyst), 1, 1, this.greaterPowerCatalystDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.greaterLengtheningCatalyst), 1, 1, this.greaterLengtheningCatalystDungeonChance));
        //Ore Dictionary Registration
        OreDictionary.registerOre("oreCoal", Block.oreCoal);
        MeteorRegistry.registerMeteorParadigm(diamondStack, diamondMeteorArray, diamondMeteorRadius);
        MeteorRegistry.registerMeteorParadigm(stoneStack, this.stoneMeteorArray, this.stoneMeteorRadius);
        MeteorRegistry.registerMeteorParadigm(ironBlockStack, this.ironBlockMeteorArray, this.ironBlockMeteorRadius);
        MeteorRegistry.registerMeteorParadigm(new ItemStack(Item.netherStar), this.netherStarMeteorArray, this.netherStarMeteorRadius);
//        sanguineHelmet = new ItemSanguineArmour(sanguineHelmetItemID).setUnlocalizedName("sanguineHelmet");
//
//        LanguageRegistry.addName(sanguineHelmet,"Sanguine Helmet");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        //TODO Thaumcraft Integration
        if (Loader.isModLoaded("Thaumcraft"))
        {
            this.isThaumcraftLoaded = true;

            try
            {
                //do stuff
                ModItems.sanguineHelmet = new ItemSanguineArmour(sanguineHelmetItemID).setUnlocalizedName("sanguineHelmet");
                LanguageRegistry.addName(ModItems.sanguineHelmet, "Sanguine Helmet");
//                focusBloodBlast = new FocusBloodBlast(focusBloodBlastItemID);
//                LanguageRegistry.addName(focusBloodBlast,"Wand Focus: Blood Blast");
//
//                focusGravityWell = new FocusGravityWell(focusGravityWellItemID);
//                LanguageRegistry.addName(focusGravityWell,"Wand Focus: Gravity Well");
                ItemStack itemGoggles = ItemApi.getItem("itemGoggles", 0);

                if (itemGoggles != null)
                {
                    //GameRegistry.addShapelessRecipe(new ItemStack(this.sanguineHelmet), itemGoggles);
                }

                //LogHelper.log(Level.INFO, "Loaded RP2 World addon");
            } catch (Exception e)
            {
                //LogHelper.log(Level.SEVERE, "Could not load RP2 World addon");
                e.printStackTrace(System.err);
            }
        } else
        {
            this.isThaumcraftLoaded = false;
        }
    }
}
