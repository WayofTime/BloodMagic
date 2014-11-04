package WayofTime.alchemicalWizardry;

import WayofTime.alchemicalWizardry.api.alchemy.AlchemicalPotionCreationHandler;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import WayofTime.alchemicalWizardry.api.harvest.HarvestRegistry;
import WayofTime.alchemicalWizardry.api.items.ShapedBloodOrbRecipe;
import WayofTime.alchemicalWizardry.api.items.ShapelessBloodOrbRecipe;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningRegistry;
import WayofTime.alchemicalWizardry.common.*;
import WayofTime.alchemicalWizardry.common.alchemy.CombinedPotionRegistry;
import WayofTime.alchemicalWizardry.common.block.ArmourForge;
import WayofTime.alchemicalWizardry.common.bloodAltarUpgrade.UpgradedAltars;
import WayofTime.alchemicalWizardry.common.book.BUEntries;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.DemonPacketAngel;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.DemonPacketRegistry;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;
import WayofTime.alchemicalWizardry.common.entity.mob.*;
import WayofTime.alchemicalWizardry.common.harvest.BloodMagicHarvestHandler;
import WayofTime.alchemicalWizardry.common.harvest.CactusReedHarvestHandler;
import WayofTime.alchemicalWizardry.common.harvest.GourdHarvestHandler;
import WayofTime.alchemicalWizardry.common.harvest.PamHarvestCompatRegistry;
import WayofTime.alchemicalWizardry.common.items.ItemRitualDiviner;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHolding;
import WayofTime.alchemicalWizardry.common.items.thaumcraft.ItemSanguineArmour;
import WayofTime.alchemicalWizardry.common.potion.*;
import WayofTime.alchemicalWizardry.common.renderer.AlchemyCircleRenderer;
import WayofTime.alchemicalWizardry.common.rituals.*;
import WayofTime.alchemicalWizardry.common.spell.simple.*;
import WayofTime.alchemicalWizardry.common.summoning.SummoningHelperAW;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import WayofTime.alchemicalWizardry.common.tileEntity.*;
import WayofTime.alchemicalWizardry.common.tileEntity.gui.GuiHandler;
import WayofTime.alchemicalWizardry.common.tweaker.MineTweakerIntegration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Mod(modid = "AWWayofTime", name = "AlchemicalWizardry", version = "v1.2.0b (Beta1)", guiFactory = "WayofTime.alchemicalWizardry.client.gui.ConfigGuiFactory")

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

    public static String[] allowedCrushedOresArray;

    public static Potion customPotionDrowning;
    public static Potion customPotionBoost;
    public static Potion customPotionProjProt;
    public static Potion customPotionInhibit;
    public static Potion customPotionFlight;
    public static Potion customPotionReciprocation;
    public static Potion customPotionFlameCloak;
    public static Potion customPotionIceCloak;
    public static Potion customPotionHeavyHeart;
    public static Potion customPotionFireFuse;
    public static Potion customPotionPlanarBinding;
    public static Potion customPotionSoulFray;
    public static Potion customPotionSoulHarden;
    public static Potion customPotionDeaf;
    public static Potion customPotionFeatherFall;

    public static int customPotionDrowningID;
    public static int customPotionBoostID;
    public static int customPotionProjProtID;
    public static int customPotionInhibitID;
    public static int customPotionFlightID;
    public static int customPotionReciprocationID;
    public static int customPotionFlameCloakID;
    public static int customPotionIceCloakID;
    public static int customPotionHeavyHeartID;
    public static int customPotionFireFuseID;
    public static int customPotionPlanarBindingID;
    public static int customPotionSoulFrayID;
    public static int customPotionSoulHardenID;
    public static int customPotionDeafID;
    public static int customPotionFeatherFallID;

    public static boolean isThaumcraftLoaded;
    public static boolean isForestryLoaded;
    public static boolean isBotaniaLoaded;

    public static boolean wimpySettings;
    public static boolean respawnWithDebuff;
    public static boolean lockdownAltar;
    public static boolean causeHungerWithRegen;

    public static List<Class> wellBlacklist;

	public static Logger logger = LogManager.getLogger("BloodMagic");
    public static CreativeTabs tabBloodMagic = new CreativeTabs("tabBloodMagic")
    {
        @Override
        public ItemStack getIconItemStack()
        {
            return new ItemStack(ModItems.weakBloodOrb, 1, 0);
        }

        @Override
        public Item getTabIconItem()
        {
            return ModItems.weakBloodOrb;
        }
    };

    public static ToolMaterial bloodBoundToolMaterial = EnumHelper.addToolMaterial("BoundBlood", 4, 1000, 12.0f, 8.0f, 50);
    public static ArmorMaterial sanguineArmourArmourMaterial = EnumHelper.addArmorMaterial("SanguineArmour", 33, new int[]{3, 8, 6, 3}, 30);

    //Dungeon loot chances
    public static int standardBindingAgentDungeonChance;
    public static int mundanePowerCatalystDungeonChance;
    public static int averagePowerCatalystDungeonChance;
    public static int greaterPowerCatalystDungeonChance;
    public static int mundaneLengtheningCatalystDungeonChance;
    public static int averageLengtheningCatalystDungeonChance;
    public static int greaterLengtheningCatalystDungeonChance;

    //Mob IDs
    public static String entityFallenAngelID = "AW001FallenAngel";
    public static String entityLowerGuardianID = "AW002";
    public static String entityBileDemonID = "AW003";
    public static String entityWingedFireDemonID = "AW004";
    public static String entitySmallEarthGolemID = "AW005";
    public static String entityIceDemonID = "AW006";
    public static String entityBoulderFistID = "AW007";
    public static String entityShadeID = "AW008";
    public static String entityAirElementalID = "AW009";
    public static String entityWaterElementalID = "AW010";
    public static String entityEarthElementalID = "AW011";
    public static String entityFireElementalID = "AW012";
    public static String entityShadeElementalID = "AW013";
    public static String entityHolyElementalID = "AW014";


    public static Fluid lifeEssenceFluid;

    // The instance of your mod that Forge uses.
    @Instance("AWWayofTime")
    public static AlchemicalWizardry instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "WayofTime.alchemicalWizardry.client.ClientProxy", serverSide = "WayofTime.alchemicalWizardry.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        File bmDirectory = new File("config/BloodMagic/schematics");

        if (!bmDirectory.exists() && bmDirectory.mkdirs())
        {
            try
            {
                InputStream in = AlchemicalWizardry.class.getResourceAsStream("/assets/alchemicalwizardry/schematics/building/buildings.zip");
                logger.info("none yet!");
                if (in != null)
                {
                    logger.info("I have found a zip!");
                    ZipInputStream zipStream = new ZipInputStream(in);
                    ZipEntry entry = null;

                    int extractCount = 0;

                    while ((entry = zipStream.getNextEntry()) != null)
                    {
                        File file = new File(bmDirectory, entry.getName());
                        if (file.exists() && file.length() > 3L)
                        {
                            continue;
                        }
                        FileOutputStream out = new FileOutputStream(file);

                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = zipStream.read(buffer)) != -1)
                        {
                            out.write(buffer, 0, len);
                        }
                        out.close();

                        extractCount++;
                    }
                }
            } catch (Exception e)
            {
            }
        }

        TEDemonPortal.loadBuildingList();

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
        AlchemicalWizardry.lifeEssenceFluid = new LifeEssence("Life Essence");
        FluidRegistry.registerFluid(lifeEssenceFluid);

        ModBlocks.init();
        ModBlocks.registerBlocksInPre();
        ModItems.init();
        ModItems.registerItems();

        RecipeSorter.INSTANCE.register("AWWayofTime:shapedorb", ShapedBloodOrbRecipe.class, Category.SHAPED, "before:minecraft:shapeless");
        RecipeSorter.INSTANCE.register("AWWayofTime:shapelessorb", ShapelessBloodOrbRecipe.class, Category.SHAPELESS, "after:minecraft:shapeless");

        Object eventHook = new AlchemicalWizardryEventHooks();
        FMLCommonHandler.instance().bus().register(eventHook);
        MinecraftForge.EVENT_BUS.register(eventHook);
        NewPacketHandler.INSTANCE.ordinal();
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        int craftingConstant = OreDictionary.WILDCARD_VALUE;

        ModBlocks.registerBlocksInInit();
        //blocks

        proxy.registerRenderers();
        proxy.registerEntities();
        proxy.registerEntityTrackers();
        proxy.registerEvents();

        //ItemStacks used for crafting go here
        ItemStack lapisStack = new ItemStack(Items.dye, 1, 4);
        ItemStack lavaBucketStack = new ItemStack(Items.lava_bucket);
        ItemStack cobblestoneStack = new ItemStack(Blocks.cobblestone, 1, craftingConstant);
        ItemStack glassStack = new ItemStack(Blocks.glass, 1, craftingConstant);
        ItemStack ironIngotStack = new ItemStack(Items.iron_ingot);
        ItemStack diamondStack = new ItemStack(Items.diamond, 1, craftingConstant);
        ItemStack woolStack = new ItemStack(Blocks.wool);
        ItemStack goldNuggetStack = new ItemStack(Items.gold_nugget);
        ItemStack stoneStack = new ItemStack(Blocks.stone, 1, craftingConstant);
        ItemStack redstoneStack = new ItemStack(Items.redstone);
        ItemStack glowstoneBlockStack = new ItemStack(Blocks.glowstone);
        ItemStack ironBlockStack = new ItemStack(Blocks.iron_block);
        ItemStack waterBucketStack = new ItemStack(Items.water_bucket);
        ItemStack emptyBucketStack = new ItemStack(Items.bucket);
        ItemStack magmaCreamStack = new ItemStack(Items.magma_cream);
        ItemStack stringStack = new ItemStack(Items.string);
        ItemStack obsidianStack = new ItemStack(Blocks.obsidian);
        ItemStack diamondSwordStack = new ItemStack(Items.diamond_sword);
        ItemStack goldIngotStack = new ItemStack(Items.gold_ingot);
        ItemStack cauldronStack = new ItemStack(Blocks.cauldron);
        ItemStack furnaceStack = new ItemStack(Blocks.furnace);
        ItemStack sugarStack = new ItemStack(Items.sugar);
        ItemStack featherStack = new ItemStack(Items.feather);
        ItemStack ghastTearStack = new ItemStack(Items.ghast_tear);
        ItemStack ironPickaxeStack = new ItemStack(Items.iron_pickaxe);
        ItemStack ironAxeStack = new ItemStack(Items.iron_axe);
        ItemStack ironShovelStack = new ItemStack(Items.iron_shovel);
        ItemStack glowstoneDustStack = new ItemStack(Items.glowstone_dust);
        ItemStack saplingStack = new ItemStack(Blocks.sapling);
        ItemStack reedStack = new ItemStack(Items.reeds);
        ItemStack blankSlateStack = new ItemStack(ModItems.blankSlate, 1, craftingConstant);
        ItemStack weakBloodOrbStackCrafted = new ItemStack(ModItems.weakBloodOrb);
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
        ItemStack bloodAltarStack = new ItemStack(ModBlocks.blockAltar, 1, 0);
        ItemStack bloodRuneCraftedStack = new ItemStack(ModBlocks.bloodRune, 1);
        ItemStack bloodRuneStack = new ItemStack(ModBlocks.bloodRune);
        ItemStack speedRuneStack = new ItemStack(ModBlocks.speedRune);
        ItemStack efficiencyRuneStack = new ItemStack(ModBlocks.efficiencyRune);
        ItemStack runeOfSacrificeStack = new ItemStack(ModBlocks.runeOfSacrifice);
        ItemStack runeOfSelfSacrificeStack = new ItemStack(ModBlocks.runeOfSelfSacrifice);
        ItemStack miningSigilStackCrafted = new ItemStack(ModItems.sigilOfTheFastMiner);
        ItemStack divinationSigilStackCrafted = new ItemStack(ModItems.divinationSigil);
        ItemStack seerSigilStackCrafted = new ItemStack(ModItems.itemSeerSigil);
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
        ItemStack redWoolStack = new ItemStack(Blocks.wool, 1, 14);
        ItemStack zombieHead = new ItemStack(Items.skull, 1, 2);
        ItemStack simpleCatalystStack = new ItemStack(ModItems.simpleCatalyst);
        ItemStack duskRitualDivinerStack = new ItemStack(ModItems.itemRitualDiviner);
        ((ItemRitualDiviner) duskRitualDivinerStack.getItem()).setMaxRuneDisplacement(duskRitualDivinerStack, 1);
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
        GameRegistry.addRecipe(sacrificialDaggerStack, "ggg", " dg", "i g", 'g', glassStack, 'd', goldIngotStack, 'i', ironIngotStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(lavaCrystalStackCrafted, "glg", "lbl", "odo", 'g', glassStack, 'l', lavaBucketStack, 'b', weakBloodOrbStack, 'd', diamondStack, 'o', obsidianStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(waterSigilStackCrafted, "www", "wbw", "wow", 'w', waterBucketStack, 'b', blankSlateStack, 'o', weakBloodOrbStack));
        GameRegistry.addRecipe(lavaSigilStackCrafted, "lml", "lbl", "lcl", 'l', lavaBucketStack, 'b', blankSlateStack, 'm', magmaCreamStack, 'c', lavaCrystalStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(voidSigilStackCrafted, "ese", "ere", "eoe", 'e', emptyBucketStack, 'r', reinforcedSlateStack, 'o', apprenticeBloodOrbStack, 's', stringStack));
        GameRegistry.addRecipe(bloodAltarStack, "s s", "scs", "gdg", 's', stoneStack, 'c', furnaceStack, 'd', diamondStack, 'g', goldIngotStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(bloodRuneCraftedStack, "sss", "ror", "sss", 's', stoneStack, 'o', weakBloodOrbStack, 'r', blankSlateStack));
        GameRegistry.addRecipe(speedRuneStack, "sbs", "uru", "sbs", 'u', sugarStack, 's', stoneStack, 'r', bloodRuneStack, 'b', blankSlateStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 1), "sbs", "bob", "srs", 's', stoneStack, 'o', magicianBloodOrbStack, 'b', emptyBucketStack, 'r', new ItemStack(ModItems.imbuedSlate)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 2), "sbs", "bob", "srs", 's', stoneStack, 'o', magicianBloodOrbStack, 'b', waterBucketStack, 'r', new ItemStack(ModItems.imbuedSlate)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 3), "sws", "ror", "sws", 's', stoneStack, 'o', new ItemStack(ModItems.masterBloodOrb), 'w', weakBloodOrbStack, 'r', new ItemStack(ModItems.demonicSlate)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 4), "srs", "beb", "sos", 's', obsidianStack, 'o', new ItemStack(ModItems.masterBloodOrb), 'r', new ItemStack(ModItems.demonicSlate), 'b', emptyBucketStack, 'e', new ItemStack(ModBlocks.bloodRune, 1, 1)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(airSigilStack, "fgf", "fsf", "fof", 'f', featherStack, 'g', ghastTearStack, 's', reinforcedSlateStack, 'o', apprenticeBloodOrbStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(miningSigilStackCrafted, "sps", "hra", "sos", 'o', apprenticeBloodOrbStack, 's', stoneStack, 'p', ironPickaxeStack, 'h', ironShovelStack, 'a', ironAxeStack, 'r', reinforcedSlateStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(runeOfSacrificeStack, "srs", "gog", "srs", 's', stoneStack, 'g', goldIngotStack, 'o', apprenticeBloodOrbStack, 'r', reinforcedSlateStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(runeOfSelfSacrificeStack, "srs", "gog", "srs", 's', stoneStack, 'g', glowstoneDustStack, 'o', apprenticeBloodOrbStack, 'r', reinforcedSlateStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(divinationSigilStackCrafted, "ggg", "gsg", "gog", 'g', glassStack, 's', blankSlateStack, 'o', weakBloodOrbStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(seerSigilStackCrafted, "gbg", "gsg", "gog", 'g', glassStack, 's', divinationSigilStackCrafted, 'o', apprenticeBloodOrbStack, 'b', new ItemStack(ModItems.bucketLife)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(ritualStoneStackCrafted, "srs", "ror", "srs", 's', obsidianStack, 'o', apprenticeBloodOrbStack, 'r', reinforcedSlateStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(masterRitualStoneStack, "brb", "ror", "brb", 'b', obsidianStack, 'o', magicianBloodOrbStack, 'r', ritualStoneStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(imperfectRitualStoneStack, "bsb", "sos", "bsb", 's', stoneStack, 'b', obsidianStack, 'o', weakBloodOrbStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(sigilOfElementalAffinityStackCrafted, "oao", "wsl", "oro", 'o', obsidianStack, 'a', airSigilStack, 'w', waterSigilStack, 'l', lavaSigilStack, 'r', magicianBloodOrbStack, 's', imbuedSlateStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(sigilOfHoldingStack, "asa", "srs", "aoa", 'a', blankSlateStack, 's', stoneStack, 'r', imbuedSlateStack, 'o', magicianBloodOrbStack));
        GameRegistry.addRecipe(emptySocketStack, "bgb", "gdg", "bgb", 'b', weakBloodShardStack, 'g', glassStack, 'd', diamondStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(armourForgeStack, "sfs", "fof", "sfs", 'f', bloodSocketStack, 's', stoneStack, 'o', magicianBloodOrbStack));
        GameRegistry.addShapelessRecipe(largeBloodStoneBrickStackCrafted, weakBloodShardStack, stoneStack);
        GameRegistry.addRecipe(bloodStoneBrickStackCrafted, "bb", "bb", 'b', largeBloodStoneBrickStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(growthSigilStack, "srs", "rer", "sos", 's', saplingStack, 'r', reedStack, 'o', apprenticeBloodOrbStack, 'e', reinforcedSlateStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(blockHomHeartStack, "www", "srs", "sos", 'w', redWoolStack, 's', stoneStack, 'r', bloodRuneStack, 'o', apprenticeBloodOrbStack));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.skull, 1, 2), new ItemStack(Items.skull, 1, 1), new ItemStack(Items.rotten_flesh), new ItemStack(Items.iron_ingot), new ItemStack(Items.leather));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.skull, 1, 0), new ItemStack(Items.skull, 1, 1), new ItemStack(Items.bow, 1, 0), new ItemStack(Items.arrow, 1, 0), new ItemStack(Items.bone));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.skull, 1, 4), new ItemStack(Items.skull, 1, 1), new ItemStack(Items.gunpowder), new ItemStack(Blocks.dirt), new ItemStack(Blocks.sand));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.blockWritingTable), " s ", "ror", 's', new ItemStack(Items.brewing_stand), 'r', obsidianStack, 'o', weakBloodOrbStack));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockPedestal), "ooo", " c ", "ooo", 'o', obsidianStack, 'c', weakBloodShardStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockPlinth), "iii", " p ", "iii", 'i', ironBlockStack, 'p', new ItemStack(ModBlocks.blockPedestal));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.alchemyFlask, 1, 0), new ItemStack(ModItems.alchemyFlask, 1, craftingConstant), new ItemStack(Items.nether_wart), redstoneStack, glowstoneDustStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.sigilOfHaste), "csc", "sts", "ror", 'c', new ItemStack(Items.cookie), 's', new ItemStack(Items.sugar), 't', ModItems.demonicSlate, 'r', obsidianStack, 'o', new ItemStack(ModItems.masterBloodOrb)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.sigilOfWind), "faf", "grg", "fof", 'f', featherStack, 'g', ghastTearStack, 'a', new ItemStack(ModItems.airSigil), 'o', new ItemStack(ModItems.masterBloodOrb), 'r', ModItems.demonicSlate));
        GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModItems.weakBloodShard, 5, 0), new ItemStack(ModItems.masterBloodOrb), new ItemStack(ModItems.weakBloodShard), imbuedSlateStack));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockTeleposer), "ggg", "efe", "ggg", 'g', goldIngotStack, 'f', new ItemStack(ModItems.telepositionFocus), 'e', new ItemStack(Items.ender_pearl));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.reinforcedTelepositionFocus), new ItemStack(ModItems.enhancedTelepositionFocus), new ItemStack(ModItems.weakBloodShard));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.demonicTelepositionFocus), new ItemStack(ModItems.reinforcedTelepositionFocus), new ItemStack(ModItems.demonBloodShard));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.sigilOfTheBridge), "nnn", "nsn", "ror", 'n', stoneStack, 'r', new ItemStack(Blocks.soul_sand), 's', imbuedSlateStack, 'o', magicianBloodOrbStack));
        GameRegistry.addRecipe(new ItemStack(ModItems.armourInhibitor), " gg", "gsg", "gg ", 'g', goldIngotStack, 's', new ItemStack(ModItems.weakBloodShard));
        GameRegistry.addRecipe(new ItemStack(ModItems.itemRitualDiviner), "d1d", "2e3", "d4d", '1', new ItemStack(ModItems.airScribeTool), '2', new ItemStack(ModItems.waterScribeTool), '3', new ItemStack(ModItems.fireScribeTool), '4', new ItemStack(ModItems.earthScribeTool), 'd', diamondStack, 'e', new ItemStack(Items.emerald));
        GameRegistry.addRecipe(duskRitualDivinerStack, " d ", "srs", " d ", 'd', new ItemStack(ModItems.duskScribeTool), 's', new ItemStack(ModItems.demonicSlate), 'r', new ItemStack(ModItems.itemRitualDiviner));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.sigilOfMagnetism), "bgb", "gsg", "bob", 'b', new ItemStack(Blocks.iron_block), 'g', goldIngotStack, 's', new ItemStack(ModItems.imbuedSlate), 'o', magicianBloodOrbStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.energyBazooka), "Ocd", "cb ", "d w", 'O', archmageBloodOrbStack, 'c', crepitousStack, 'b', new ItemStack(ModItems.energyBlaster), 'd', diamondStack, 'w', new ItemStack(ModItems.weakBloodShard)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.itemBloodLightSigil), "btb", "sss", "bob", 'o', magicianBloodOrbStack, 'b', glowstoneBlockStack, 't', new ItemStack(Blocks.torch), 's', imbuedSlateStack));
        GameRegistry.addRecipe(new ItemStack(ModItems.itemKeyOfDiablo), " gw", "gdg", "wg ", 'w', weakBloodShardStack, 'g', goldIngotStack, 'd', diamondStack);
        customPotionDrowning = (new PotionDrowning(customPotionDrowningID, true, 0)).setIconIndex(0, 0).setPotionName("Drowning");
        customPotionBoost = (new PotionBoost(customPotionBoostID, false, 0)).setIconIndex(0, 0).setPotionName("Boost");
        customPotionProjProt = (new PotionProjectileProtect(customPotionProjProtID, false, 0)).setIconIndex(0, 0).setPotionName("Whirlwind");
        customPotionInhibit = (new PotionInhibit(customPotionInhibitID, false, 0)).setIconIndex(0, 0).setPotionName("Inhibit");
        customPotionFlight = (new PotionFlight(customPotionFlightID, false, 0)).setIconIndex(0, 0).setPotionName("Flight");
        customPotionReciprocation = (new PotionReciprocation(customPotionReciprocationID, false, 0xFFFFFF)).setIconIndex(0, 0).setPotionName("Reciprocation");
        customPotionFlameCloak = (new PotionFlameCloak(customPotionFlameCloakID, false, 0).setIconIndex(0, 0).setPotionName("Flame Cloak"));
        customPotionIceCloak = (new PotionIceCloak(customPotionIceCloakID, false, 0).setIconIndex(0, 0).setPotionName("Ice Cloak"));
        customPotionHeavyHeart = (new PotionHeavyHeart(customPotionHeavyHeartID, true, 0).setIconIndex(0, 0).setPotionName("Heavy Heart"));
        customPotionFireFuse = (new PotionFireFuse(customPotionFireFuseID, true, 0).setIconIndex(0, 0).setPotionName("Fire Fuse"));
        customPotionPlanarBinding = (new PotionPlanarBinding(customPotionPlanarBindingID, true, 0).setIconIndex(0, 0).setPotionName("Planar Binding"));
        customPotionSoulFray = (new PotionSoulFray(customPotionSoulFrayID, true, 0).setIconIndex(0, 0).setPotionName("Soul Fray"));
        customPotionSoulHarden = (new PotionSoulHarden(customPotionSoulHardenID, false, 0).setIconIndex(0, 0).setPotionName("Soul Harden"));
        customPotionDeaf = (new PotionDeaf(customPotionDeafID, true, 0).setIconIndex(0, 0).setPotionName("Deafness"));
        customPotionFeatherFall = (new PotionFeatherFall(customPotionFeatherFallID, false, 0).setIconIndex(0, 0).setPotionName("Feather Fall"));

        ItemStack masterBloodOrbStack = new ItemStack(ModItems.masterBloodOrb);

//        ModBlocks.blockLifeEssence.setUnlocalizedName("lifeEssenceBlock");
        FluidContainerRegistry.registerFluidContainer(lifeEssenceFluid, new ItemStack(ModItems.bucketLife), FluidContainerRegistry.EMPTY_BUCKET);
        ModBlocks.blockAltar.setHarvestLevel("pickaxe", 1);

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
        GameRegistry.registerTileEntity(TEOrientable.class, "containerOrientable");
        GameRegistry.registerTileEntity(TESpellParadigmBlock.class, "containerSpellParadigmBlock");
        GameRegistry.registerTileEntity(TESpellEffectBlock.class, "containerSpellEffectBlock");
        GameRegistry.registerTileEntity(TESpellModifierBlock.class, "containerSpellModifierBlock");
        GameRegistry.registerTileEntity(TESpellEnhancementBlock.class, "containerSpellEnhancementBlock");
        GameRegistry.registerTileEntity(TESpectralContainer.class, "spectralContainerTileEntity");
        GameRegistry.registerTileEntity(TEDemonPortal.class, "containerDemonPortal");
        GameRegistry.registerTileEntity(TESchematicSaver.class, "containerSchematicSaver");
        GameRegistry.registerTileEntity(TESpectralBlock.class, "containerSpectralBlock");
        GameRegistry.registerTileEntity(TEReagentConduit.class, "containerReagentConduit");
        GameRegistry.registerTileEntity(TEBellJar.class, "containerBellJar");
        GameRegistry.registerTileEntity(TEAlchemicCalcinator.class, "containerAlchemicCalcinator");
        ModBlocks.bloodRune.setHarvestLevel("pickaxe", 2);
        ModBlocks.speedRune.setHarvestLevel("pickaxe", 2);
        ModBlocks.efficiencyRune.setHarvestLevel("pickaxe", 2);
        ModBlocks.runeOfSacrifice.setHarvestLevel("pickaxe", 2);
        ModBlocks.runeOfSelfSacrifice.setHarvestLevel("pickaxe", 2);
        ModBlocks.ritualStone.setHarvestLevel("pickaxe", 2);
        ModBlocks.bloodSocket.setHarvestLevel("pickaxe", 2);
        ModBlocks.ritualStone.setHarvestLevel("pickaxe", 2);
        ModBlocks.imperfectRitualStone.setHarvestLevel("pickaxe", 2);
        ModBlocks.blockMasterStone.setHarvestLevel("pickaxe", 2);
        ModBlocks.emptySocket.setHarvestLevel("pickaxe", 2);
        ModBlocks.bloodStoneBrick.setHarvestLevel("pickaxe", 0);
        ModBlocks.largeBloodStoneBrick.setHarvestLevel("pickaxe", 0);
        ModBlocks.blockWritingTable.setHarvestLevel("pickaxe", 1);
        ModBlocks.blockHomHeart.setHarvestLevel("pickaxe", 1);
        ModBlocks.blockPedestal.setHarvestLevel("pickaxe", 2);
        ModBlocks.blockPlinth.setHarvestLevel("pickaxe", 2);
        ModBlocks.blockTeleposer.setHarvestLevel("pickaxe", 2);

        //Fuel handler
        GameRegistry.registerFuelHandler(new AlchemicalWizardryFuelHandler());

        //Gui registration
        UpgradedAltars.loadAltars();
        SigilOfHolding.initiateSigilOfHolding();
        ArmourForge.initializeRecipes();
        TEPlinth.initialize();

        this.initAlchemyPotionRecipes();
        this.initAltarRecipes();
        this.initRituals();
        this.initBindingRecipes();
        this.initHarvestRegistry();
        this.initCombinedAlchemyPotionRecipes();
        
        ReagentRegistry.initReagents();
        this.initReagentRegistries();
        
        this.initDemonPacketRegistiry();

        MinecraftForge.EVENT_BUS.register(new ModLivingDropsEvent());
        proxy.InitRendering();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        ItemStack gunpowderStack = new ItemStack(Items.gunpowder);
        ItemStack offensaStack = new ItemStack(ModItems.baseAlchemyItems, 1, 0);
        ItemStack praesidiumStack = new ItemStack(ModItems.baseAlchemyItems, 1, 1);
        ItemStack orbisTerraeStack = new ItemStack(ModItems.baseAlchemyItems, 1, 2);
        ItemStack strengthenedCatalystStack = new ItemStack(ModItems.baseAlchemyItems, 1, 3);
        ItemStack concentratedCatalystStack = new ItemStack(ModItems.baseAlchemyItems, 1, 4);
        ItemStack fracturedBoneStack = new ItemStack(ModItems.baseAlchemyItems, 1, 5);
        ItemStack virtusStack = new ItemStack(ModItems.baseAlchemyItems, 1, 6);
        ItemStack reductusStack = new ItemStack(ModItems.baseAlchemyItems, 1, 7);
        ItemStack potentiaStack = new ItemStack(ModItems.baseAlchemyItems, 1, 8);

        ItemStack strengthenedCatalystStackCrafted = new ItemStack(ModItems.baseAlchemyItems, 2, 3);
        ItemStack fracturedBoneStackCrafted = new ItemStack(ModItems.baseAlchemyItems, 4, 5);

        //TODO NEW RECIPES!
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.weakBindingAgent), 10, new ItemStack[]{simpleCatalystStack, simpleCatalystStack, new ItemStack(Items.clay_ball)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.standardBindingAgent), 15, new ItemStack[]{new ItemStack(ModItems.weakBindingAgent), sanctusStack, new ItemStack(ModItems.crystallos)}, 3);
        AlchemyRecipeRegistry.registerRecipe(simpleCatalystStack, 2, new ItemStack[]{sugarStack, redstoneStack, redstoneStack, glowstoneDustStack, new ItemStack(Items.gunpowder)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.incendium), 5, new ItemStack[]{lavaBucketStack, new ItemStack(Items.blaze_powder), new ItemStack(Items.blaze_powder), new ItemStack(Blocks.netherrack), simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.aether), 5, new ItemStack[]{featherStack, featherStack, glowstoneDustStack, ghastTearStack, simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.sanctus), 5, new ItemStack[]{glowstoneDustStack, new ItemStack(Items.gold_nugget), glowstoneDustStack, glassStack, simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.crepitous), 5, new ItemStack[]{new ItemStack(Items.gunpowder), new ItemStack(Items.gunpowder), cobblestoneStack, cobblestoneStack, simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.crystallos), 5, new ItemStack[]{new ItemStack(Blocks.ice), new ItemStack(Blocks.ice), new ItemStack(Blocks.snow), new ItemStack(Blocks.snow), simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.terrae), 5, new ItemStack[]{new ItemStack(Blocks.dirt), new ItemStack(Blocks.sand), obsidianStack, obsidianStack, simpleCatalystStack}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.aquasalus), 5, new ItemStack[]{simpleCatalystStack, new ItemStack(Items.dye, 1, 0), new ItemStack(Items.potionitem, 1, 0), new ItemStack(Items.potionitem, 1, 0), new ItemStack(Items.potionitem, 1, 0)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.tennebrae), 5, new ItemStack[]{simpleCatalystStack, new ItemStack(Items.coal), new ItemStack(Items.coal), new ItemStack(Blocks.obsidian), new ItemStack(Items.clay_ball)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.magicales), 5, new ItemStack[]{redstoneStack, simpleCatalystStack, new ItemStack(Items.gunpowder), new ItemStack(Items.glowstone_dust), new ItemStack(Items.glowstone_dust)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.mundanePowerCatalyst), 10, new ItemStack[]{glowstoneDustStack, glowstoneDustStack, glowstoneDustStack, new ItemStack(ModItems.weakBindingAgent), simpleCatalystStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.mundaneLengtheningCatalyst), 10, new ItemStack[]{redstoneStack, redstoneStack, redstoneStack, new ItemStack(ModItems.weakBindingAgent), simpleCatalystStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.averagePowerCatalyst), 20, new ItemStack[]{new ItemStack(ModItems.mundanePowerCatalyst), new ItemStack(ModItems.mundanePowerCatalyst), new ItemStack(ModItems.standardBindingAgent)}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.averageLengtheningCatalyst), 20, new ItemStack[]{new ItemStack(ModItems.mundaneLengtheningCatalyst), new ItemStack(ModItems.mundaneLengtheningCatalyst), new ItemStack(ModItems.standardBindingAgent)}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.greaterPowerCatalyst), 30, new ItemStack[]{new ItemStack(ModItems.averagePowerCatalyst), new ItemStack(ModItems.averagePowerCatalyst), new ItemStack(ModItems.incendium)}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.greaterLengtheningCatalyst), 30, new ItemStack[]{new ItemStack(ModItems.averageLengtheningCatalyst), new ItemStack(ModItems.averageLengtheningCatalyst), new ItemStack(ModItems.aquasalus)}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.weakFillingAgent), 5, new ItemStack[]{simpleCatalystStack, new ItemStack(Items.nether_wart), redstoneStack, glowstoneDustStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.standardFillingAgent), 10, new ItemStack[]{new ItemStack(ModItems.weakFillingAgent), new ItemStack(ModItems.terrae)}, 3);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.enhancedFillingAgent), 25, new ItemStack[]{new ItemStack(ModItems.standardFillingAgent), new ItemStack(ModItems.aquasalus), new ItemStack(ModItems.magicales)}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.activationCrystal, 1, 1), 100, new ItemStack[]{new ItemStack(ModItems.activationCrystal, 1, 0), new ItemStack(ModItems.demonBloodShard), incendiumStack, aquasalusStack, aetherStack}, 4);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.activationCrystal, 1, 1), 100, new ItemStack[]{new ItemStack(ModItems.activationCrystal, 1, 0), new ItemStack(Items.nether_star), incendiumStack, aquasalusStack, aetherStack}, 4);

        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.web), 2, new ItemStack[]{new ItemStack(Items.string), new ItemStack(Items.string), new ItemStack(Items.string), new ItemStack(Items.string), new ItemStack(Items.string)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.gunpowder, 2, 0), 2, new ItemStack[]{gunpowderStack, new ItemStack(Items.coal), new ItemStack(Blocks.sand)}, 1);

        AlchemyRecipeRegistry.registerRecipe(strengthenedCatalystStackCrafted, 10, new ItemStack[]{simpleCatalystStack, simpleCatalystStack, new ItemStack(Items.dye, 1, 15), new ItemStack(Items.nether_wart)}, 3);
        AlchemyRecipeRegistry.registerRecipe(offensaStack, 10, new ItemStack[]{strengthenedCatalystStack, incendiumStack, new ItemStack(Items.arrow), new ItemStack(Items.flint), new ItemStack(Items.arrow)}, 3);
        AlchemyRecipeRegistry.registerRecipe(praesidiumStack, 10, new ItemStack[]{strengthenedCatalystStack, tennebraeStack, ironIngotStack, new ItemStack(Blocks.web), redstoneStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(orbisTerraeStack, 10, new ItemStack[]{strengthenedCatalystStack, terraeStack, gunpowderStack, new ItemStack(Blocks.netherrack), new ItemStack(Blocks.sand)}, 3);
        AlchemyRecipeRegistry.registerRecipe(concentratedCatalystStack, 10, new ItemStack[]{strengthenedCatalystStack, fracturedBoneStack, goldNuggetStack}, 4);
        AlchemyRecipeRegistry.registerRecipe(fracturedBoneStackCrafted, 2, new ItemStack[]{new ItemStack(Items.bone), new ItemStack(Items.bone), new ItemStack(Items.bone), new ItemStack(Items.bone), gunpowderStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(virtusStack, 20, new ItemStack[]{redstoneStack, new ItemStack(Items.coal), strengthenedCatalystStack, redstoneStack, gunpowderStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(reductusStack, 20, new ItemStack[]{redstoneStack, goldIngotStack, strengthenedCatalystStack, new ItemStack(Blocks.soul_sand), new ItemStack(Items.carrot)}, 3);
        AlchemyRecipeRegistry.registerRecipe(potentiaStack, 20, new ItemStack[]{glowstoneDustStack, strengthenedCatalystStack, lapisStack, lapisStack, new ItemStack(Items.quartz)}, 3);


        HomSpellRegistry.registerBasicSpell(new ItemStack(Items.flint_and_steel), new SpellFireBurst());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Blocks.ice), new SpellFrozenWater());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Blocks.tnt), new SpellExplosions());
        HomSpellRegistry.registerBasicSpell(new ItemStack(ModItems.apprenticeBloodOrb), new SpellHolyBlast());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Items.ghast_tear), new SpellWindGust());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Items.glowstone_dust), new SpellLightningBolt());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Items.water_bucket), new SpellWateryGrave());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Blocks.obsidian), new SpellEarthBender());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Items.ender_pearl), new SpellTeleport());
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityFallenAngelID), new ItemStack[]{sanctusStack, sanctusStack, sanctusStack, aetherStack, tennebraeStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityLowerGuardianID), new ItemStack[]{cobblestoneStack, cobblestoneStack, terraeStack, tennebraeStack, new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_nugget)}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityBileDemonID), new ItemStack[]{new ItemStack(Items.poisonous_potato), tennebraeStack, terraeStack, new ItemStack(Items.porkchop), new ItemStack(Items.egg), new ItemStack(Items.beef)}, new ItemStack[]{crepitousStack, crepitousStack, terraeStack, ironBlockStack, ironBlockStack, diamondStack}, new ItemStack[]{}, 0, 5);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityWingedFireDemonID), new ItemStack[]{aetherStack, incendiumStack, incendiumStack, incendiumStack, tennebraeStack, new ItemStack(Blocks.netherrack)}, new ItemStack[]{diamondStack, new ItemStack(Blocks.gold_block), magicalesStack, magicalesStack, new ItemStack(Items.fire_charge), new ItemStack(Blocks.coal_block)}, new ItemStack[]{}, 0, 5);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entitySmallEarthGolemID), new ItemStack[]{new ItemStack(Items.clay_ball), terraeStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityIceDemonID), new ItemStack[]{crystallosStack, crystallosStack, aquasalusStack, crystallosStack, sanctusStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityBoulderFistID), new ItemStack[]{terraeStack, sanctusStack, tennebraeStack, new ItemStack(Items.bone), new ItemStack(Items.cooked_beef), new ItemStack(Items.cooked_beef)}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityShadeID), new ItemStack[]{tennebraeStack, tennebraeStack, tennebraeStack, aetherStack, glassStack, new ItemStack(Items.glass_bottle)}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityAirElementalID), new ItemStack[]{aetherStack, aetherStack, aetherStack, aetherStack, aetherStack, aetherStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityWaterElementalID), new ItemStack[]{aquasalusStack, aquasalusStack, aquasalusStack, aquasalusStack, aquasalusStack, aquasalusStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityEarthElementalID), new ItemStack[]{terraeStack, terraeStack, terraeStack, terraeStack, terraeStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityFireElementalID), new ItemStack[]{incendiumStack, incendiumStack, incendiumStack, incendiumStack, incendiumStack, incendiumStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityShadeElementalID), new ItemStack[]{tennebraeStack, tennebraeStack, tennebraeStack, tennebraeStack, tennebraeStack, tennebraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityHolyElementalID), new ItemStack[]{sanctusStack, sanctusStack, sanctusStack, sanctusStack, sanctusStack, sanctusStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);

        //Custom mobs
        EntityRegistry.registerModEntity(EntityFallenAngel.class, "FallenAngel", 20, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityLowerGuardian.class, "LowerGuardian", 21, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityBileDemon.class, "BileDemon", 22, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityWingedFireDemon.class, "WingedFireDemon", 23, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntitySmallEarthGolem.class, "SmallEarthGolem", 24, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityIceDemon.class, "IceDemon", 25, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityBoulderFist.class, "BoulderFist", 26, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityShade.class, "Shade", 27, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityAirElemental.class, "AirElemental", 28, this, 120, 3, true);
        EntityRegistry.registerModEntity(EntityWaterElemental.class, "WaterElemental", 29, this, 120, 3, true);
        EntityRegistry.registerModEntity(EntityEarthElemental.class, "EarthElemental", 30, this, 120, 3, true);
        EntityRegistry.registerModEntity(EntityFireElemental.class, "FireElemental", 31, this, 120, 3, true);
        EntityRegistry.registerModEntity(EntityShadeElemental.class, "ShadeElemental", 32, this, 120, 3, true);
        EntityRegistry.registerModEntity(EntityHolyElemental.class, "HolyElemental", 33, this, 120, 3, true);

        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.standardBindingAgent), 1, 3, this.standardBindingAgentDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.mundanePowerCatalyst), 1, 1, this.mundanePowerCatalystDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.mundaneLengtheningCatalyst), 1, 1, this.mundaneLengtheningCatalystDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.averagePowerCatalyst), 1, 1, this.averagePowerCatalystDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.averageLengtheningCatalyst), 1, 1, this.averageLengtheningCatalystDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.greaterPowerCatalyst), 1, 1, this.greaterPowerCatalystDungeonChance));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.greaterLengtheningCatalyst), 1, 1, this.greaterLengtheningCatalystDungeonChance));

        //Ore Dictionary Registration
        OreDictionary.registerOre("oreCoal", Blocks.coal_ore);
        MeteorRegistry.registerMeteorParadigm(diamondStack, diamondMeteorArray, diamondMeteorRadius);
        MeteorRegistry.registerMeteorParadigm(stoneStack, this.stoneMeteorArray, this.stoneMeteorRadius);
        MeteorRegistry.registerMeteorParadigm(ironBlockStack, this.ironBlockMeteorArray, this.ironBlockMeteorRadius);
        MeteorRegistry.registerMeteorParadigm(new ItemStack(Items.nether_star), this.netherStarMeteorArray, this.netherStarMeteorRadius);

        //Register spell component recipes
        ItemStack complexSpellCrystalStack = new ItemStack(ModItems.itemComplexSpellCrystal);
        ItemStack quartzRodStack = new ItemStack(ModItems.baseItems, 1, 0);
        ItemStack emptyCoreStack = new ItemStack(ModItems.baseItems, 1, 1);
        ItemStack magicalesCableStack = new ItemStack(ModItems.baseItems, 1, 2);
        ItemStack woodBraceStack = new ItemStack(ModItems.baseItems, 1, 3);
        ItemStack stoneBraceStack = new ItemStack(ModItems.baseItems, 1, 4);
        ItemStack projectileCoreStack = new ItemStack(ModItems.baseItems, 1, 5);
        ItemStack selfCoreStack = new ItemStack(ModItems.baseItems, 1, 6);
        ItemStack meleeCoreStack = new ItemStack(ModItems.baseItems, 1, 7);
        ItemStack toolCoreStack = new ItemStack(ModItems.baseItems, 1, 26);
        ItemStack paradigmBackPlateStack = new ItemStack(ModItems.baseItems, 1, 8);
        ItemStack outputCableStack = new ItemStack(ModItems.baseItems, 1, 9);
        ItemStack flameCoreStack = new ItemStack(ModItems.baseItems, 1, 10);
        ItemStack iceCoreStack = new ItemStack(ModItems.baseItems, 1, 11);
        ItemStack windCoreStack = new ItemStack(ModItems.baseItems, 1, 12);
        ItemStack earthCoreStack = new ItemStack(ModItems.baseItems, 1, 13);
        ItemStack inputCableStack = new ItemStack(ModItems.baseItems, 1, 14);
        ItemStack crackedRunicPlateStack = new ItemStack(ModItems.baseItems, 1, 15);
        ItemStack runicPlateStack = new ItemStack(ModItems.baseItems, 1, 16);
        ItemStack imbuedRunicPlateStack = new ItemStack(ModItems.baseItems, 1, 17);
        ItemStack defaultCoreStack = new ItemStack(ModItems.baseItems, 1, 18);
        ItemStack offenseCoreStack = new ItemStack(ModItems.baseItems, 1, 19);
        ItemStack defensiveCoreStack = new ItemStack(ModItems.baseItems, 1, 20);
        ItemStack environmentalCoreStack = new ItemStack(ModItems.baseItems, 1, 21);
        ItemStack powerCoreStack = new ItemStack(ModItems.baseItems, 1, 22);
        ItemStack costCoreStack = new ItemStack(ModItems.baseItems, 1, 23);
        ItemStack potencyCoreStack = new ItemStack(ModItems.baseItems, 1, 24);
        ItemStack obsidianBraceStack = new ItemStack(ModItems.baseItems, 1, 25);

        ItemStack magicalesCraftedCableStack = new ItemStack(ModItems.baseItems, 5, 2);
        ItemStack crackedRunicPlateStackCrafted = new ItemStack(ModItems.baseItems, 2, 15);
        ItemStack runicPlateStackCrafted = new ItemStack(ModItems.baseItems, 2, 16);

        GameRegistry.addRecipe(quartzRodStack, "qqq", 'q', new ItemStack(Items.quartz));
        GameRegistry.addRecipe(emptyCoreStack, "gig", "nrn", "gig", 'n', goldIngotStack, 'i', ironIngotStack, 'g', glassStack, 'r', simpleCatalystStack);
        GameRegistry.addRecipe(magicalesCraftedCableStack, "sss", "mmm", "sss", 's', new ItemStack(Items.string), 'm', magicalesStack);
        GameRegistry.addRecipe(woodBraceStack, " il", "ili", "li ", 'l', new ItemStack(Blocks.log, 1, craftingConstant), 'i', new ItemStack(Items.string));
        GameRegistry.addRecipe(stoneBraceStack, " is", "isi", "si ", 'i', ironIngotStack, 's', reinforcedSlateStack);
        GameRegistry.addRecipe(obsidianBraceStack, " is", "ibi", "si ", 'i', obsidianStack, 's', reinforcedSlateStack, 'b', stoneBraceStack);

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(projectileCoreStack, "mbm", "aca", "mom", 'c', emptyCoreStack, 'b', weakBloodShardStack, 'm', magicalesStack, 'o', magicianBloodOrbStack, 'a', new ItemStack(Items.arrow)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(selfCoreStack, "sbs", "ncn", "sos", 'c', emptyCoreStack, 's', sanctusStack, 'b', weakBloodShardStack, 'o', magicianBloodOrbStack, 'n', glowstoneDustStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(meleeCoreStack, "sbs", "ncn", "sos", 'c', emptyCoreStack, 's', incendiumStack, 'b', weakBloodShardStack, 'o', magicianBloodOrbStack, 'n', new ItemStack(Items.fire_charge)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(toolCoreStack, "sbs", "ncn", "sos", 'c', emptyCoreStack, 's', terraeStack, 'b', weakBloodShardStack, 'o', magicianBloodOrbStack, 'n', new ItemStack(Blocks.obsidian)));

        GameRegistry.addRecipe(paradigmBackPlateStack, "isi", "rgr", "isi", 'i', ironIngotStack, 'r', stoneStack, 'g', goldIngotStack, 's', reinforcedSlateStack);
        GameRegistry.addRecipe(outputCableStack, " si", "s c", " si", 's', stoneStack, 'i', ironIngotStack, 'c', simpleCatalystStack);

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(flameCoreStack, "mdm", "scs", "mom", 'm', incendiumStack, 'c', emptyCoreStack, 'o', magicianBloodOrbStack, 'd', diamondStack, 's', weakBloodShardStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(iceCoreStack, "mdm", "scs", "mom", 'm', crystallosStack, 'c', emptyCoreStack, 'o', magicianBloodOrbStack, 'd', diamondStack, 's', weakBloodShardStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(windCoreStack, "mdm", "scs", "mom", 'm', aetherStack, 'c', emptyCoreStack, 'o', magicianBloodOrbStack, 'd', diamondStack, 's', weakBloodShardStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(earthCoreStack, "mdm", "scs", "mom", 'm', terraeStack, 'c', emptyCoreStack, 'o', magicianBloodOrbStack, 'd', diamondStack, 's', weakBloodShardStack));

        GameRegistry.addRecipe(inputCableStack, "ws ", "rcs", "ws ", 'w', blankSlateStack, 's', stoneStack, 'r', imbuedSlateStack, 'c', simpleCatalystStack);

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(defaultCoreStack, "msm", "geg", "mom", 'm', strengthenedCatalystStack, 'e', emptyCoreStack, 'o', magicianBloodOrbStack, 's', weakBloodShardStack, 'g', goldIngotStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(offenseCoreStack, "msm", "geg", "mom", 'm', offensaStack, 'e', emptyCoreStack, 'o', magicianBloodOrbStack, 's', weakBloodShardStack, 'g', goldIngotStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(defensiveCoreStack, "msm", "geg", "mom", 'm', praesidiumStack, 'e', emptyCoreStack, 'o', magicianBloodOrbStack, 's', weakBloodShardStack, 'g', goldIngotStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(environmentalCoreStack, "msm", "geg", "mom", 'm', orbisTerraeStack, 'e', emptyCoreStack, 'o', magicianBloodOrbStack, 's', weakBloodShardStack, 'g', goldIngotStack));

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(powerCoreStack, "msm", "geg", "mom", 'm', virtusStack, 'e', emptyCoreStack, 'o', masterBloodOrbStack, 's', weakBloodShardStack, 'g', goldIngotStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(costCoreStack, "msm", "geg", "mom", 'm', reductusStack, 'e', emptyCoreStack, 'o', masterBloodOrbStack, 's', weakBloodShardStack, 'g', goldIngotStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(potencyCoreStack, "msm", "geg", "mom", 'm', potentiaStack, 'e', emptyCoreStack, 'o', masterBloodOrbStack, 's', weakBloodShardStack, 'g', goldIngotStack));

        AlchemyRecipeRegistry.registerRecipe(crackedRunicPlateStackCrafted, 10, new ItemStack[]{imbuedSlateStack, imbuedSlateStack, concentratedCatalystStack}, 4);
        AlchemyRecipeRegistry.registerRecipe(runicPlateStack, 30, new ItemStack[]{crackedRunicPlateStack, terraeStack}, 5);
        AlchemyRecipeRegistry.registerRecipe(imbuedRunicPlateStack, 100, new ItemStack[]{magicalesStack, incendiumStack, runicPlateStack, runicPlateStack, aquasalusStack}, 5);
        AlchemyRecipeRegistry.registerRecipe(complexSpellCrystalStack, 50, new ItemStack[]{new ItemStack(ModItems.blankSpell), weakBloodShardStack, weakBloodShardStack, diamondStack, goldIngotStack}, 3);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockConduit, 1, 0), "q q", "ccc", "q q", 'q', quartzRodStack, 'c', magicalesCableStack);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellParadigm, 1, 0), "gb ", "pcw", "gb ", 'p', paradigmBackPlateStack, 'c', projectileCoreStack, 'g', goldIngotStack, 'b', stoneBraceStack, 'w', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellParadigm, 1, 1), "gb ", "pcw", "gb ", 'p', paradigmBackPlateStack, 'c', selfCoreStack, 'g', goldIngotStack, 'b', stoneBraceStack, 'w', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellParadigm, 1, 2), "gb ", "pcw", "gb ", 'p', paradigmBackPlateStack, 'c', meleeCoreStack, 'g', goldIngotStack, 'b', stoneBraceStack, 'w', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellParadigm, 1, 3), "gb ", "pcw", "gb ", 'p', paradigmBackPlateStack, 'c', toolCoreStack, 'g', goldIngotStack, 'b', stoneBraceStack, 'w', outputCableStack);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEffect, 1, 0), "bgb", "ico", "bgb", 'c', flameCoreStack, 'b', stoneBraceStack, 'g', goldIngotStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEffect, 1, 1), "bgb", "ico", "bgb", 'c', iceCoreStack, 'b', stoneBraceStack, 'g', goldIngotStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEffect, 1, 2), "bgb", "ico", "bgb", 'c', windCoreStack, 'b', stoneBraceStack, 'g', goldIngotStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEffect, 1, 3), "bgb", "ico", "bgb", 'c', earthCoreStack, 'b', stoneBraceStack, 'g', goldIngotStack, 'i', inputCableStack, 'o', outputCableStack);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellModifier, 1, 0), "bgb", "ico", "bgb", 'c', defaultCoreStack, 'i', inputCableStack, 'o', outputCableStack, 'b', stoneBraceStack, 'g', ironIngotStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellModifier, 1, 1), "bgb", "ico", "bgb", 'c', offenseCoreStack, 'i', inputCableStack, 'o', outputCableStack, 'b', stoneBraceStack, 'g', ironIngotStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellModifier, 1, 2), "bgb", "ico", "bgb", 'c', defensiveCoreStack, 'i', inputCableStack, 'o', outputCableStack, 'b', stoneBraceStack, 'g', ironIngotStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellModifier, 1, 3), "bgb", "ico", "bgb", 'c', environmentalCoreStack, 'i', inputCableStack, 'o', outputCableStack, 'b', stoneBraceStack, 'g', ironIngotStack);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement, 1, 0), "bpb", "ico", "bpb", 'c', powerCoreStack, 'b', woodBraceStack, 'p', crackedRunicPlateStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement, 1, 1), "bpb", "ico", "bpb", 'c', powerCoreStack, 'b', stoneBraceStack, 'p', runicPlateStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement, 1, 2), "bpb", "ico", "bpb", 'c', powerCoreStack, 'b', obsidianBraceStack, 'p', imbuedRunicPlateStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement, 1, 5), "bpb", "ico", "bpb", 'c', costCoreStack, 'b', woodBraceStack, 'p', crackedRunicPlateStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement, 1, 6), "bpb", "ico", "bpb", 'c', costCoreStack, 'b', stoneBraceStack, 'p', runicPlateStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement, 1, 7), "bpb", "ico", "bpb", 'c', costCoreStack, 'b', obsidianBraceStack, 'p', imbuedRunicPlateStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement, 1, 10), "bpb", "ico", "bpb", 'c', potencyCoreStack, 'b', woodBraceStack, 'p', crackedRunicPlateStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement, 1, 11), "bpb", "ico", "bpb", 'c', potencyCoreStack, 'b', stoneBraceStack, 'p', runicPlateStack, 'i', inputCableStack, 'o', outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement, 1, 12), "bpb", "ico", "bpb", 'c', potencyCoreStack, 'b', obsidianBraceStack, 'p', imbuedRunicPlateStack, 'i', inputCableStack, 'o', outputCableStack);

        GameRegistry.addRecipe(new ItemStack(ModItems.itemAttunedCrystal), "Sr ", " ar", "s S", 'r', quartzRodStack, 's', new ItemStack(Items.stick, 1, craftingConstant), 'a', strengthenedCatalystStack, 'S', stoneStack);
        GameRegistry.addRecipe(new ItemStack(ModItems.itemTankSegmenter), "gqi", " rq", "q g", 'q', quartzRodStack, 'i', ironIngotStack, 'r', strengthenedCatalystStack, 'g', goldIngotStack);
        GameRegistry.addRecipe(new ItemStack(ModItems.itemDestinationClearer), "qcq", "c c", "qcq", 'q', quartzRodStack, 'c', simpleCatalystStack);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockAlchemicCalcinator), "pgp", "gsg", "ccc", 'p', crackedRunicPlateStack, 'g', glassStack, 's', strengthenedCatalystStack, 'c', cobblestoneStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockCrystalBelljar), "GGG", "GcG", "www", 'G', glassStack, 'c', concentratedCatalystStack, 'w', new ItemStack(Blocks.wooden_slab, 1, craftingConstant));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockReagentConduit), "isi", "scs", "isi", 'c', concentratedCatalystStack, 's', stringStack, 'i', ironIngotStack);

        GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 5, 15), fracturedBoneStack);

        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.itemSigilOfSupression), "wtl", "wvl", "wol", 'v', new ItemStack(ModItems.voidSigil), 't', new ItemStack(ModBlocks.blockTeleposer), 'o', masterBloodOrbStack, 'l', lavaBucketStack, 'w', waterBucketStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.itemSigilOfEnderSeverance), "ptp", "ese", "pop", 's', new ItemStack(ModItems.demonicSlate), 't', weakBloodShardStack, 'o', masterBloodOrbStack, 'e', new ItemStack(Items.ender_eye), 'p', new ItemStack(Items.ender_pearl)));

        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.flint, 2, 0), 1, new ItemStack[]{new ItemStack(Blocks.gravel), new ItemStack(Items.flint)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.grass), 2, new ItemStack[]{new ItemStack(Blocks.dirt), new ItemStack(Items.dye, 1, 15), new ItemStack(Items.wheat_seeds), new ItemStack(Items.wheat_seeds)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.leather, 3, 0), 2, new ItemStack[]{new ItemStack(Items.rotten_flesh), new ItemStack(Items.rotten_flesh), new ItemStack(Items.rotten_flesh), waterBucketStack, new ItemStack(Items.flint)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.bread), 1, new ItemStack[]{new ItemStack(Items.wheat), new ItemStack(Items.sugar)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.fire_charge, 5, 0), 3, new ItemStack[]{new ItemStack(Items.coal), new ItemStack(Items.blaze_powder), gunpowderStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.sand, 2, 0), 1, new ItemStack[]{new ItemStack(Blocks.cobblestone), gunpowderStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.clay, 4, 0), 2, new ItemStack[]{new ItemStack(Blocks.hardened_clay, 1, craftingConstant), new ItemStack(Blocks.hardened_clay, 1, craftingConstant), new ItemStack(Blocks.hardened_clay, 1, craftingConstant), new ItemStack(Blocks.hardened_clay, 1, craftingConstant), waterBucketStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.string, 4, 0), 1, new ItemStack[]{new ItemStack(Blocks.wool, 1, craftingConstant), new ItemStack(Items.flint)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.gravel, 2, 0), 1, new ItemStack[]{new ItemStack(Blocks.stone), gunpowderStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.obsidian), 1, new ItemStack[]{waterBucketStack, lavaBucketStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.paper), 1, new ItemStack[]{new ItemStack(Items.reeds)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.soul_sand, 3, 0), 3, new ItemStack[]{new ItemStack(Blocks.sand), new ItemStack(Blocks.sand), new ItemStack(Blocks.sand), waterBucketStack, weakBloodShardStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.mycelium, 1, 0), 5, new ItemStack[]{new ItemStack(Blocks.grass), new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.red_mushroom)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.ice), 2, new ItemStack[]{waterBucketStack, new ItemStack(Items.snowball)}, 1);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	BUEntries entries = new BUEntries();
    	entries.postInit();
        //TODO Thaumcraft Integration
        if (Loader.isModLoaded("Thaumcraft"))
        {
            this.isThaumcraftLoaded = true;

            try
            {
                //do stuff
                ModItems.sanguineHelmet = new ItemSanguineArmour(0).setUnlocalizedName("sanguineHelmet");
                ModItems.sanguineRobe = new ItemSanguineArmour(1).setUnlocalizedName("sanguineRobe");
                ModItems.sanguinePants = new ItemSanguineArmour(2).setUnlocalizedName("sanguinePants");
                ModItems.sanguineBoots = new ItemSanguineArmour(3).setUnlocalizedName("sanguineBoots");
                GameRegistry.registerItem(ModItems.sanguineHelmet, "sanguineHelmet");
                GameRegistry.registerItem(ModItems.sanguineRobe, "sanguineRobe");
                GameRegistry.registerItem(ModItems.sanguinePants, "sanguinePants");
                GameRegistry.registerItem(ModItems.sanguineBoots, "sanguineBoots");

                ItemStack itemGoggles = ItemApi.getItem("itemGoggles", 0);
                Item itemThaumChest = GameRegistry.findItem("Thaumcraft", "ItemChestplateThaumium");
                Item itemThaumLeggings = GameRegistry.findItem("Thaumcraft", "ItemLeggingsThaumium");
                Item itemThaumBoots = GameRegistry.findItem("Thaumcraft", "ItemBootsThaumium");

                AspectList aspectList = new AspectList();
                aspectList.add(Aspect.ARMOR, 5).add(Aspect.MAGIC, 5);

                ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.sanguineHelmet), aspectList);
                ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.sanguineRobe), aspectList);
                ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.sanguinePants), aspectList);
                ThaumcraftApi.registerObjectTag(new ItemStack(ModItems.sanguineBoots), aspectList);


                if (itemGoggles != null)
                {
                    BindingRegistry.registerRecipe(new ItemStack(ModItems.sanguineHelmet), itemGoggles);
                }

                if (itemThaumChest != null)
                {
                    BindingRegistry.registerRecipe(new ItemStack(ModItems.sanguineRobe), new ItemStack(itemThaumChest));
                }

                if (itemThaumLeggings != null)
                {
                    BindingRegistry.registerRecipe(new ItemStack(ModItems.sanguinePants), new ItemStack(itemThaumLeggings));
                }

                if (itemThaumBoots != null)
                {
                    BindingRegistry.registerRecipe(new ItemStack(ModItems.sanguineBoots), new ItemStack(itemThaumBoots));
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

        if (Loader.isModLoaded("Forestry"))
        {
            this.isForestryLoaded = true;

//        	ModItems.itemBloodFrame = new ItemBloodFrame(this.itemBloodFrameItemID).setUnlocalizedName("bloodFrame");
//        	
//        	ItemStack provenFrame = GameRegistry.findItemStack("Forestry", "frameImpregnated", 1);
//        	
//        	if(provenFrame !=null)
//        	{
//        		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.itemBloodFrame), provenFrame, 3, 30000, 20, 20, false);
//        	}
        } else
        {
            this.isForestryLoaded = false;
        }

        if (Loader.isModLoaded("harvestcraft"))
        {
            PamHarvestCompatRegistry.registerPamHandlers();
            AlchemicalWizardry.logger.info("Loaded Harvestcraft Handlers!");
        }
        
        if(Loader.isModLoaded("MineTweaker3")) {
            MineTweakerIntegration.register();
            AlchemicalWizardry.logger.info("Loaded MineTweaker 3 Integration");
        }
        
        this.isBotaniaLoaded = Loader.isModLoaded("Botania");

        BloodMagicConfiguration.loadBlacklist();
    }

    public static void initAlchemyPotionRecipes()
    {
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.ghast_tear), Potion.regeneration.id, 450);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.golden_carrot), Potion.nightVision.id, 2 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.magma_cream), Potion.fireResistance.id, 2 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.water_bucket), Potion.waterBreathing.id, 2 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.sugar), Potion.moveSpeed.id, 2 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.speckled_melon), Potion.heal.id, 2 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.spider_eye), Potion.poison.id, 450);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.dye, 1, 0), Potion.blindness.id, 450);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.fermented_spider_eye), Potion.weakness.id, 450);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.blaze_powder), Potion.damageBoost.id, 2 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(ModItems.aether), Potion.jump.id, 2 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.clay_ball), Potion.moveSlowdown.id, 450);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.redstone), Potion.digSpeed.id, 2 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.potionitem, 1, 0), AlchemicalWizardry.customPotionDrowning.id, 450);
        //AlchemicalPotionCreationHandler.addPotion(new ItemStack(Item.goldenCarrot),Potion.nightVision.id,2*60*20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.glass_bottle), Potion.invisibility.id, 2 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.diamond), Potion.resistance.id, 2 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.poisonous_potato), Potion.field_76443_y.id, 2); //saturation
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(ModItems.demonBloodShard), Potion.field_76434_w.id, 4 * 60 * 20); //health boost
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(ModItems.weakBloodShard), Potion.field_76444_x.id, 4 * 60 * 20); //Absorption
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(ModItems.terrae), AlchemicalWizardry.customPotionBoost.id, 1 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.feather), AlchemicalWizardry.customPotionFlight.id, 1 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.arrow), AlchemicalWizardry.customPotionReciprocation.id, 1 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.ender_pearl), AlchemicalWizardry.customPotionPlanarBinding.id, 1 * 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Blocks.soul_sand), AlchemicalWizardry.customPotionSoulFray.id, 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(ModItems.baseItems, 1, 16), AlchemicalWizardry.customPotionSoulHarden.id, 60 * 20);
        AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.slime_ball), AlchemicalWizardry.customPotionDeaf.id, 60 * 20);
    }

    public static void initAltarRecipes()
    {
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.weakBloodOrb), new ItemStack(Items.diamond), 1, 2000, 2, 1, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.apprenticeBloodOrb), new ItemStack(Items.emerald), 2, 5000, 5, 5, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.magicianBloodOrb), new ItemStack(Blocks.gold_block), 3, 25000, 20, 20, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.masterBloodOrb), new ItemStack(ModItems.weakBloodShard), 4, 40000, 30, 50, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.archmageBloodOrb), new ItemStack(ModItems.demonBloodShard), 5, 75000, 50, 100, false);

        AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(ModItems.weakBloodOrb), 1, 2);
        AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(ModItems.apprenticeBloodOrb), 2, 5);
        AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(ModItems.magicianBloodOrb), 3, 15);
        AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(ModItems.masterBloodOrb), 4, 25);
        AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(ModItems.archmageBloodOrb), 5, 50);

        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.telepositionFocus), new ItemStack(Items.ender_pearl), 4, 2000, 10, 10, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.enhancedTelepositionFocus), new ItemStack(ModItems.telepositionFocus), 4, 10000, 25, 15, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.demonicSlate), new ItemStack(ModItems.imbuedSlate), 4, 15000, 20, 20, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.duskScribeTool), new ItemStack(Blocks.coal_block), 4, 2000, 20, 10, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModBlocks.bloodSocket), new ItemStack(ModBlocks.emptySocket), 3, 30000, 40, 10, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.earthScribeTool), new ItemStack(Blocks.obsidian), 3, 1000, 5, 5, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.waterScribeTool), new ItemStack(Blocks.lapis_block), 3, 1000, 5, 5, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.blankSpell), new ItemStack(Blocks.glass), 2, 1000, 5, 5, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.blankSlate), new ItemStack(Blocks.stone), 1, 1000, 5, 5, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.activationCrystal), new ItemStack(ModItems.lavaCrystal), 3, 10000, 20, 10, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.fireScribeTool), new ItemStack(Items.magma_cream), 3, 1000, 5, 5, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.airScribeTool), new ItemStack(Items.ghast_tear), 3, 1000, 5, 5, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.imbuedSlate), new ItemStack(ModItems.reinforcedSlate), 3, 5000, 15, 10, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.daggerOfSacrifice), new ItemStack(Items.iron_sword), 2, 3000, 5, 5, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.alchemyFlask), new ItemStack(Items.glass_bottle), 2, 2000, 5, 5, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.reinforcedSlate), new ItemStack(ModItems.blankSlate), 2, 2000, 5, 5, false);
        AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.bucketLife), new ItemStack(Items.bucket), 1, 1000, 5, 0, false);
    }

    public static void initRituals()
    {
        Rituals.registerRitual("AW001Water", 1, 500, new RitualEffectWater(), "Ritual of the Full Spring", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 30, 255, 255, 0, 0.501, 0.8, 0, 1.5, false));
        Rituals.registerRitual("AW002Lava", 1, 10000, new RitualEffectLava(), "Serenade of the Nether", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 255, 0, 0, 255, 0, 0.501, 0.8, 0, 1.5, false));
        Rituals.registerRitual("AW003GreenGrove", 1, 1000, new RitualEffectGrowth(), "Ritual of the Green Grove", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 244, 164, 96, 255, 0, 1.0, 1.6, 0, 1.5, false));
        Rituals.registerRitual("AW004Interdiction", 1, 1000, new RitualEffectInterdiction(), "Interdiction Ritual", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 27, 227, 206, 255, 0, 0.501, 0.8, 0, 1.5, false));
        Rituals.registerRitual("AW005Containment", 1, 2000, new RitualEffectContainment(), "Ritual of Containment", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 186, 21, 21, 255, 0, 2.5, 2.5, 0, 2.5, false));
        Rituals.registerRitual("AW006Binding", 1, 5000, new RitualEffectSoulBound(), "Ritual of Binding", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/TransCircleBinding.png"), 193, 7, 7, 255, 0, 0.501, 1.0, 0, 2.5, true));
        Rituals.registerRitual("AW007Unbinding", 1, 30000, new RitualEffectUnbinding(), "Ritual of Unbinding", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 193, 7, 7, 255, 0, 0.5, 0.8, 0, 2.5, false));
        Rituals.registerRitual("AW008HighJump", 1, 1000, new RitualEffectJumping(), "Ritual of the High Jump", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 10, 183, 173, 255, 0, 0.501, 1.501, 0, 1.5, false));
        Rituals.registerRitual("AW009Magnetism", 1, 5000, new RitualEffectMagnetic(), "Ritual of Magnetism", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 126, 39, 0, 255, 0, 0.501, 2.0, 0, 1.5, false));
        Rituals.registerRitual("AW010Crusher", 1, 2500, new RitualEffectCrushing(), "Ritual of the Crusher", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW011Speed", 1, 1000, new RitualEffectLeap(), "Ritual of Speed", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW012AnimalGrowth", 1, 10000, new RitualEffectAnimalGrowth(), "Ritual of the Shepherd", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW013Suffering", 1, 50000, new RitualEffectWellOfSuffering(), "Well of Suffering", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/TransCircleSuffering.png"), 0, 0, 0, 255, 0, 0.501, 0.8, 0, 2.5, true));
        Rituals.registerRitual("AW014Regen", 1, 25000, new RitualEffectHealing(), "Ritual of Regeneration", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW015FeatheredKnife", 1, 50000, new RitualEffectFeatheredKnife(), "Ritual of the Feathered Knife", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW016FeatheredEarth", 2, 100000, new RitualEffectFeatheredEarth(), "Ritual of the Feathered Earth", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW017Gaia", 2, 1000000, new RitualEffectBiomeChanger(), "Ritual of Gaia's Transformation", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW018Condor", 2, 1000000, new RitualEffectFlight(), "Reverence of the Condor", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW019FallingTower", 2, 1000000, new RitualEffectSummonMeteor(), "Mark of the Falling Tower", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW020BalladOfAlchemy", 1, 20000, new RitualEffectAutoAlchemy(), "Ballad of Alchemy", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW021Expulsion", 1, 1000000, new RitualEffectExpulsion(), "Aura of Expulsion", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW022Supression", 1, 10000, new RitualEffectSupression(), "Dome of Supression", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW023Zephyr", 1, 25000, new RitualEffectItemSuction(), "Call of the Zephyr", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW024Harvest", 1, 20000, new RitualEffectHarvest(), "Reap of the Harvest Moon", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW025Conduit", 2, 2000000, new RitualEffectLifeConduit(), "Cry of the Eternal Soul", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW026Ellipsoid", 1, 25000, new RitualEffectEllipsoid(), "Focus of the Ellipsoid", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW027Evaporation", 1, 20000, new RitualEffectEvaporation(), "Song of Evaporation", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW028SpawnWard", 1, 150000, new RitualEffectSpawnWard(), "Ward of Sacrosanctity", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW029VeilOfEvil", 1, 150000, new RitualEffectVeilOfEvil(), "Veil of Evil", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        Rituals.registerRitual("AW030FullStomach", 1, 100000, new RitualEffectFullStomach(), "Requiem of the Satiated Stomach", new AlchemyCircleRenderer(new ResourceLocation("alchemicalwizardry:textures/models/SimpleTransCircle.png"), 0, 0, 0, 255, 0, 0.501, 0.501, 0, 1.5, false));
        //Rituals.registerRitual(1,100,new RitualEffectApiaryOverclock(),"Apiary Overclock"));
    }

    public static void initBindingRecipes()
    {
        BindingRegistry.registerRecipe(new ItemStack(ModItems.boundPickaxe), new ItemStack(Items.diamond_pickaxe));
        BindingRegistry.registerRecipe(new ItemStack(ModItems.boundAxe), new ItemStack(Items.diamond_axe));
        BindingRegistry.registerRecipe(new ItemStack(ModItems.boundShovel), new ItemStack(Items.diamond_shovel));
        BindingRegistry.registerRecipe(new ItemStack(ModItems.energySword), new ItemStack(Items.diamond_sword));
        BindingRegistry.registerRecipe(new ItemStack(ModItems.energyBlaster), new ItemStack(ModItems.apprenticeBloodOrb));
    }

    public static void initHarvestRegistry()
    {
        HarvestRegistry.registerHarvestHandler(new BloodMagicHarvestHandler());
        HarvestRegistry.registerHarvestHandler(new GourdHarvestHandler());
        HarvestRegistry.registerHarvestHandler(new CactusReedHarvestHandler());
    }

    public static void initCombinedAlchemyPotionRecipes()
    {
        CombinedPotionRegistry.registerCombinedPotionRecipe(customPotionFlameCloak, Potion.moveSpeed, Potion.regeneration);
    }
    
    public static void initReagentRegistries()
    {
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.sanctus), new ReagentStack(ReagentRegistry.sanctusReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.incendium), new ReagentStack(ReagentRegistry.incendiumReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.aquasalus), new ReagentStack(ReagentRegistry.aquasalusReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.magicales), new ReagentStack(ReagentRegistry.magicalesReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.aether), new ReagentStack(ReagentRegistry.aetherReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.crepitous), new ReagentStack(ReagentRegistry.crepitousReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.crystallos), new ReagentStack(ReagentRegistry.crystallosReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.terrae), new ReagentStack(ReagentRegistry.terraeReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.tennebrae), new ReagentStack(ReagentRegistry.tenebraeReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems, 1, 0), new ReagentStack(ReagentRegistry.offensaReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems, 1, 1), new ReagentStack(ReagentRegistry.praesidiumReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems, 1, 2), new ReagentStack(ReagentRegistry.orbisTerraeReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems, 1, 6), new ReagentStack(ReagentRegistry.virtusReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems, 1, 7), new ReagentStack(ReagentRegistry.reductusReagent, 1000));
        ReagentRegistry.registerItemAndReagent(new ItemStack(ModItems.baseAlchemyItems, 1, 8), new ReagentStack(ReagentRegistry.potentiaReagent, 1000));
    }
    
    public static void initDemonPacketRegistiry()
    {
    	DemonPacketRegistry.registerDemonPacket("angel", new DemonPacketAngel());
    }
}
