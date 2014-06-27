package WayofTime.alchemicalWizardry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import joshie.alchemicalWizardy.ShapedBloodOrbRecipe;
import joshie.alchemicalWizardy.ShapelessBloodOrbRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
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
import thaumcraft.api.ItemApi;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemicalPotionCreationHandler;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningRegistry;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryEventHooks;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryFuelHandler;
import WayofTime.alchemicalWizardry.common.CommonProxy;
import WayofTime.alchemicalWizardry.common.EntityAirElemental;
import WayofTime.alchemicalWizardry.common.LifeBucketHandler;
import WayofTime.alchemicalWizardry.common.LifeEssence;
import WayofTime.alchemicalWizardry.common.ModLivingDropsEvent;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.block.ArmourForge;
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
import WayofTime.alchemicalWizardry.common.items.ItemRitualDiviner;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHolding;
import WayofTime.alchemicalWizardry.common.items.thaumcraft.ItemSanguineArmour;
import WayofTime.alchemicalWizardry.common.potion.PotionBoost;
import WayofTime.alchemicalWizardry.common.potion.PotionDrowning;
import WayofTime.alchemicalWizardry.common.potion.PotionFireFuse;
import WayofTime.alchemicalWizardry.common.potion.PotionFlameCloak;
import WayofTime.alchemicalWizardry.common.potion.PotionFlight;
import WayofTime.alchemicalWizardry.common.potion.PotionHeavyHeart;
import WayofTime.alchemicalWizardry.common.potion.PotionIceCloak;
import WayofTime.alchemicalWizardry.common.potion.PotionInhibit;
import WayofTime.alchemicalWizardry.common.potion.PotionPlanarBinding;
import WayofTime.alchemicalWizardry.common.potion.PotionProjectileProtect;
import WayofTime.alchemicalWizardry.common.potion.PotionReciprocation;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectAnimalGrowth;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectAutoAlchemy;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectBiomeChanger;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectContainment;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectCrushing;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectExpulsion;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectFeatheredEarth;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectFeatheredKnife;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectFlight;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectGrowth;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectHealing;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectInterdiction;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectItemSuction;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectJumping;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectLava;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectLeap;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectMagnetic;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectSoulBound;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectSummonMeteor;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectSupression;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectUnbinding;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectWater;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectWellOfSuffering;
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
import WayofTime.alchemicalWizardry.common.summoning.SummoningHelperAW;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEConduit;
import WayofTime.alchemicalWizardry.common.tileEntity.TEDemonPortal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEHomHeart;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import WayofTime.alchemicalWizardry.common.tileEntity.TEOrientable;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPedestal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TESchematicSaver;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralContainer;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEffectBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEnhancementBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellModifierBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellParadigmBlock;
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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "AWWayofTime", name = "AlchemicalWizardry", version = "v1.0.1g")
//@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"BloodAltar", "particle", "SetLifeEssence", "GetLifeEssence", "Ritual", "GetAltarEssence", "TESocket", "TEWritingTable", "CustomParticle", "SetPlayerVel", "SetPlayerPos", "TEPedestal", "TEPlinth", "TETeleposer", "InfiniteLPPath", "TEOrientor"}, packetHandler = PacketHandler.class)

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

    public static boolean isThaumcraftLoaded;
    public static boolean isForestryLoaded;
    
    public static boolean wimpySettings;

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
    public static ArmorMaterial sanguineArmourArmourMaterial = EnumHelper.addArmorMaterial("SanguineArmour", 1000, new int[]{3, 6, 5, 2}, 30);

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
    	
    	if(!bmDirectory.exists() && bmDirectory.mkdirs())
    	{
    		try
    		{
    			InputStream in = AlchemicalWizardry.class.getResourceAsStream("/assets/alchemicalwizardry/schematics/building/buildings.zip");
    			System.out.println("none yet!");
    			if(in != null)
    			{
    				System.out.println("I have found a zip!");
    				ZipInputStream zipStream = new ZipInputStream(in);
    				ZipEntry entry = null;
    				
    				int extractCount = 0;
    				
    				while((entry = zipStream.getNextEntry()) != null)
    				{
    					File file = new File(bmDirectory, entry.getName());
    					if(file.exists() && file.length() > 3L)
    					{
    						continue;
    					}
    					FileOutputStream out = new FileOutputStream(file);
    					
    					byte[] buffer = new byte[8192];
    					int len;
    					while((len = zipStream.read(buffer)) != -1)
    					{
    						out.write(buffer, 0, len);
    					}
    					out.close();
    					
    					extractCount++;
    				}
    			}
    		}
    		catch(Exception e)
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
        
        //FMLCommonHandler.instance().bus().register(new AlchemicalWizardryEventHooks());
        MinecraftForge.EVENT_BUS.register(new AlchemicalWizardryEventHooks());
        NewPacketHandler.INSTANCE.ordinal();
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        int craftingConstant = OreDictionary.WILDCARD_VALUE;
        //TickRegistry.registerTickHandler(new AlchemicalWizardryTickHandler(), Side.SERVER);
        
        ModBlocks.registerBlocksInInit();
        //blocks
        
        proxy.registerRenderers();
        proxy.registerEntities();
        proxy.registerEntityTrackers();
        //ItemStacks used for crafting go here
        ItemStack lapisStack = new ItemStack(Items.dye,1,4);
        ItemStack lavaBucketStack = new ItemStack(Items.lava_bucket);
        ItemStack cobblestoneStack = new ItemStack(Blocks.cobblestone);
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
        ItemStack bloodAltarStack = new ItemStack(ModBlocks.blockAltar,1,0);
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
        ItemStack redWoolStack = new ItemStack(Blocks.wool, 1, 14);
        ItemStack zombieHead = new ItemStack(Items.skull, 1, 2);
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
        //GameRegistry.addRecipe(glassShardStack, " x", "y ", 'x', ironIngotStack, 'y', glassStack);
        //GameRegistry.addRecipe(weakBloodOrbStackCrafted, "xxx", "xdx", "www", 'x', bloodiedShardStack, 'd', diamondStack, 'w', woolStack);
        GameRegistry.addRecipe(sacrificialDaggerStack, "ggg", " dg", "i g", 'g', glassStack, 'd', goldIngotStack, 'i', ironIngotStack);
        //GameRegistry.addRecipe(blankSlateStack, "sgs", "gig", "sgs", 's', stoneStack, 'g', goldNuggetStack, 'i', ironIngotStack);
        //GameRegistry.addRecipe(reinforcedSlateStack, "rir", "ibi", "gig", 'r', redstoneStack, 'i', ironIngotStack, 'b', blankSlateStack, 'g', glowstoneBlockStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(lavaCrystalStackCrafted, "glg", "lbl", "odo", 'g', glassStack, 'l', lavaBucketStack, 'b', weakBloodOrbStack, 'd', diamondStack, 'o', obsidianStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(waterSigilStackCrafted, "www", "wbw", "wow", 'w', waterBucketStack, 'b', blankSlateStack, 'o', weakBloodOrbStack));
        GameRegistry.addRecipe(lavaSigilStackCrafted, "lml", "lbl", "lcl", 'l', lavaBucketStack, 'b', blankSlateStack, 'm', magmaCreamStack, 'c', lavaCrystalStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(voidSigilStackCrafted, "ese", "ere", "eoe", 'e', emptyBucketStack, 'r', reinforcedSlateStack, 'o', apprenticeBloodOrbStack, 's', stringStack));
        GameRegistry.addRecipe(bloodAltarStack, "s s", "scs", "gdg", 's', stoneStack, 'c', furnaceStack, 'd', diamondStack, 'g', goldIngotStack);
        //GameRegistry.addRecipe(energySwordStack, " o ", " o ", " s ", 'o', weakBloodOrbStack, 's', diamondSwordStack);
        //GameRegistry.addRecipe(energyBlasterStack, "oi ", "gdi", " rd", 'o', weakBloodOrbStack, 'i', ironIngotStack, 'd', diamondStack, 'r', reinforcedSlateStack, 'g', goldIngotStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(bloodRuneCraftedStack, "sss", "ror", "sss", 's', stoneStack, 'o', weakBloodOrbStack, 'r', blankSlateStack));
        GameRegistry.addRecipe(speedRuneStack, "sbs", "uru", "sbs", 'u', sugarStack, 's', stoneStack, 'r', bloodRuneStack, 'b', blankSlateStack);
        //GameRegistry.addRecipe(efficiencyRuneStack, "sbs", "rur", "sbs", 'r', redstoneStack, 's', stoneStack, 'u', bloodRuneStack,'b',blankSlateStack);
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 1), "sbs", "bob", "srs", 's', stoneStack, 'o', magicianBloodOrbStack, 'b', emptyBucketStack, 'r', new ItemStack(ModItems.imbuedSlate)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 2), "sbs", "bob", "srs", 's', stoneStack, 'o', magicianBloodOrbStack, 'b', waterBucketStack, 'r', new ItemStack(ModItems.imbuedSlate)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModBlocks.bloodRune, 1, 3), "sws", "ror", "sws", 's', stoneStack, 'o', new ItemStack(ModItems.masterBloodOrb), 'w', weakBloodOrbStack, 'r', new ItemStack(ModItems.demonicSlate)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(airSigilStack, "fgf", "fsf", "fof", 'f', featherStack, 'g', ghastTearStack, 's', reinforcedSlateStack, 'o', apprenticeBloodOrbStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(miningSigilStackCrafted, "sps", "hra", "sos", 'o', apprenticeBloodOrbStack, 's', stoneStack, 'p', ironPickaxeStack, 'h', ironShovelStack, 'a', ironAxeStack, 'r', reinforcedSlateStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(runeOfSacrificeStack, "srs", "gog", "srs", 's', stoneStack, 'g', goldIngotStack, 'o', apprenticeBloodOrbStack, 'r', reinforcedSlateStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(runeOfSelfSacrificeStack, "srs", "gog", "srs", 's', stoneStack, 'g', glowstoneDustStack, 'o', apprenticeBloodOrbStack, 'r', reinforcedSlateStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(divinationSigilStackCrafted, "ggg", "gsg", "gog", 'g', glassStack, 's', blankSlateStack, 'o', weakBloodOrbStack));
//        GameRegistry.addRecipe(waterScribeToolStack, "f", "i", 'f', featherStack, 'i', elementalInkWaterStack);
//        GameRegistry.addRecipe(fireScribeToolStack, "f", "i", 'f', featherStack, 'i', elementalInkFireStack);
//        GameRegistry.addRecipe(earthScribeToolStack, "f", "i", 'f', featherStack, 'i', elementalInkEarthStack);
//        GameRegistry.addRecipe(airScribeToolStack, "f", "i", 'f', featherStack, 'i', elementalInkAirStack);
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
        customPotionFlameCloak = (new PotionFlameCloak(customPotionFlameCloakID,false,0).setIconIndex(0,0).setPotionName("Flame Cloak"));
        customPotionIceCloak = (new PotionIceCloak(customPotionIceCloakID,false,0).setIconIndex(0,0).setPotionName("Ice Cloak"));
        customPotionHeavyHeart = (new PotionHeavyHeart(customPotionHeavyHeartID,true,0).setIconIndex(0, 0).setPotionName("Heavy Heart"));
        customPotionFireFuse = (new PotionFireFuse(customPotionFireFuseID,true,0).setIconIndex(0, 0).setPotionName("Fire Fuse"));
        customPotionPlanarBinding = (new PotionPlanarBinding(customPotionPlanarBindingID,true,0).setIconIndex(0,0).setPotionName("Planar Binding"));
        
        ItemStack masterBloodOrbStack = new ItemStack(ModItems.masterBloodOrb);
        
        //FluidStack lifeEssenceFluidStack = new FluidStack(lifeEssenceFluid, 1);
        //LiquidStack lifeEssence = new LiquidStack(lifeEssenceFlowing, 1);
        //LiquidDictionary.getOrCreateLiquid("Life Essence", lifeEssence);
        

//        ModBlocks.blockLifeEssence.setUnlocalizedName("lifeEssenceBlock");
        FluidContainerRegistry.registerFluidContainer(lifeEssenceFluid, new ItemStack(ModItems.bucketLife), FluidContainerRegistry.EMPTY_BUCKET);
        
        //lifeEssenceFluid.setUnlocalizedName("lifeEssence");
        //LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Life Essence", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(AlchemicalWizardry.bucketLife), new ItemStack(Items.bucketEmpty)));
        //GameRegistry.registerBlock(testingBlock, "testingBlock");
        //LanguageRegistry.addName(testingBlock, "Testing Block");
        //(testingBlock, "pickaxe", 0);
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
        GameRegistry.registerTileEntity(TESpectralContainer.class,"spectralContainerTileEntity");
        GameRegistry.registerTileEntity(TEDemonPortal.class, "containerDemonPortal");
        GameRegistry.registerTileEntity(TESchematicSaver.class, "containerSchematicSaver");
        //GameRegistry.registerBlock(ModBlocks.blockSpellEffect,"blockSpellEffect");
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
        //EntityRegistry.registerModEntity(EnergyBlastProjectile.class, "BlasterProj", 0, this, 128, 5, true);
        
        //Gui registration
        // NetworkRegistry.instance().registerGuiHandler(this, new GuiHandlerAltar());
        UpgradedAltars.loadAltars();
        SigilOfHolding.initiateSigilOfHolding();
        ArmourForge.initializeRecipes();
        TEPlinth.initialize();
        
        this.initAlchemyPotionRecipes();
        this.initAltarRecipes();
        this.initRituals();
        this.initBindingRecipes(); 
        
        //MinecraftForge.setToolClass(ModItems.boundPickaxe, "pickaxe", 5);
        //MinecraftForge.setToolClass(ModItems.boundAxe, "axe", 5);
        //MinecraftForge.setToolClass(ModItems.boundShovel, "shovel", 5);
        MinecraftForge.EVENT_BUS.register(new ModLivingDropsEvent());
        proxy.InitRendering();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
//        ItemStack[] comp = new ItemStack[5];
//        for(int i=0;i<5;i++)
//        {
//        	comp[i] = redstoneStack;
//        }
//        AlchemyRecipeRegistry.registerRecipe(glowstoneDustStack, 2, comp, 2);
        
        ItemStack gunpowderStack = new ItemStack(Items.gunpowder);
        
        ItemStack offensaStack = new ItemStack(ModItems.baseAlchemyItems,1,0);
        ItemStack praesidiumStack = new ItemStack(ModItems.baseAlchemyItems,1,1);
        ItemStack orbisTerraeStack = new ItemStack(ModItems.baseAlchemyItems,1,2);
        ItemStack strengthenedCatalystStack = new ItemStack(ModItems.baseAlchemyItems,1,3);
        ItemStack concentratedCatalystStack = new ItemStack(ModItems.baseAlchemyItems,1,4);
        ItemStack fracturedBoneStack = new ItemStack(ModItems.baseAlchemyItems,1,5);
        ItemStack virtusStack = new ItemStack(ModItems.baseAlchemyItems,1,6);
        ItemStack reductusStack = new ItemStack(ModItems.baseAlchemyItems,1,7);
        ItemStack potentiaStack = new ItemStack(ModItems.baseAlchemyItems,1,8);
        
        ItemStack strengthenedCatalystStackCrafted = new ItemStack(ModItems.baseAlchemyItems,2,3);
        ItemStack fracturedBoneStackCrafted = new ItemStack(ModItems.baseAlchemyItems,4,5);
        
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
        
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.web),2,new ItemStack[]{new ItemStack(Items.string),new ItemStack(Items.string),new ItemStack(Items.string),new ItemStack(Items.string),new ItemStack(Items.string)},1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.gunpowder,2,0), 2, new ItemStack[]{gunpowderStack, new ItemStack(Items.coal), new ItemStack(Blocks.sand)}, 1);
        
        AlchemyRecipeRegistry.registerRecipe(strengthenedCatalystStackCrafted, 10, new ItemStack[]{simpleCatalystStack, simpleCatalystStack, new ItemStack(Items.dye,1,15), new ItemStack(Items.nether_wart)}, 3);
        AlchemyRecipeRegistry.registerRecipe(offensaStack,10, new ItemStack[]{strengthenedCatalystStack,incendiumStack, new ItemStack(Items.arrow), new ItemStack(Items.flint), new ItemStack(Items.arrow)},3);
        AlchemyRecipeRegistry.registerRecipe(praesidiumStack, 10, new ItemStack[]{strengthenedCatalystStack,tennebraeStack,ironIngotStack,new ItemStack(Blocks.web),redstoneStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(orbisTerraeStack, 10, new ItemStack[]{strengthenedCatalystStack,terraeStack, gunpowderStack, new ItemStack(Blocks.netherrack), new ItemStack(Blocks.sand)}, 3);
        AlchemyRecipeRegistry.registerRecipe(concentratedCatalystStack,10,new ItemStack[]{strengthenedCatalystStack,fracturedBoneStack,goldNuggetStack},4);
        AlchemyRecipeRegistry.registerRecipe(fracturedBoneStackCrafted, 2, new ItemStack[]{new ItemStack(Items.bone), new ItemStack(Items.bone),new ItemStack(Items.bone),new ItemStack(Items.bone), gunpowderStack},1); 
        AlchemyRecipeRegistry.registerRecipe(virtusStack,20, new ItemStack[]{redstoneStack, new ItemStack(Items.coal),strengthenedCatalystStack,redstoneStack,gunpowderStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(reductusStack,20,new ItemStack[]{redstoneStack, goldIngotStack, strengthenedCatalystStack,new ItemStack(Blocks.soul_sand), new ItemStack(Items.carrot)},3);
        AlchemyRecipeRegistry.registerRecipe(potentiaStack,20, new ItemStack[]{glowstoneDustStack,strengthenedCatalystStack,lapisStack,lapisStack,new ItemStack(Items.quartz)}, 3);
        
        
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
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityShadeElementalID), new ItemStack[]{tennebraeStack,tennebraeStack,tennebraeStack,tennebraeStack,tennebraeStack,tennebraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelperAW(this.entityHolyElementalID), new ItemStack[]{sanctusStack, sanctusStack, sanctusStack, sanctusStack, sanctusStack, sanctusStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
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
        ItemStack quartzRodStack = new ItemStack(ModItems.baseItems,1,0);
        ItemStack emptyCoreStack = new ItemStack(ModItems.baseItems,1,1);
        ItemStack magicalesCableStack = new ItemStack(ModItems.baseItems,1,2);
        ItemStack woodBraceStack = new ItemStack(ModItems.baseItems,1,3);
        ItemStack stoneBraceStack = new ItemStack(ModItems.baseItems,1,4);
        ItemStack projectileCoreStack = new ItemStack(ModItems.baseItems,1,5);
        ItemStack selfCoreStack = new ItemStack(ModItems.baseItems,1,6);
        ItemStack meleeCoreStack = new ItemStack(ModItems.baseItems,1,7);
        ItemStack paradigmBackPlateStack = new ItemStack(ModItems.baseItems,1,8);
        ItemStack outputCableStack = new ItemStack(ModItems.baseItems,1,9);
        ItemStack flameCoreStack = new ItemStack(ModItems.baseItems,1,10);
        ItemStack iceCoreStack = new ItemStack(ModItems.baseItems,1,11);
        ItemStack windCoreStack = new ItemStack(ModItems.baseItems,1,12);
        ItemStack earthCoreStack = new ItemStack(ModItems.baseItems,1,13);
        ItemStack inputCableStack = new ItemStack(ModItems.baseItems,1,14);
        ItemStack crackedRunicPlateStack = new ItemStack(ModItems.baseItems,1,15);
        ItemStack runicPlateStack = new ItemStack(ModItems.baseItems,1,16);
        ItemStack imbuedRunicPlateStack = new ItemStack(ModItems.baseItems,1,17);
        ItemStack defaultCoreStack = new ItemStack(ModItems.baseItems,1,18);
        ItemStack offenseCoreStack = new ItemStack(ModItems.baseItems,1,19);
        ItemStack defensiveCoreStack = new ItemStack(ModItems.baseItems,1,20);
        ItemStack environmentalCoreStack = new ItemStack(ModItems.baseItems,1,21);
        ItemStack powerCoreStack = new ItemStack(ModItems.baseItems,1,22);
        ItemStack costCoreStack = new ItemStack(ModItems.baseItems,1,23);
        ItemStack potencyCoreStack = new ItemStack(ModItems.baseItems,1,24);
        ItemStack obsidianBraceStack = new ItemStack(ModItems.baseItems,1,25);
        
        ItemStack magicalesCraftedCableStack = new ItemStack(ModItems.baseItems,5,2);
        ItemStack crackedRunicPlateStackCrafted = new ItemStack(ModItems.baseItems,2,15);
        ItemStack runicPlateStackCrafted = new ItemStack(ModItems.baseItems,2,16);
        
        GameRegistry.addRecipe(quartzRodStack, "qqq", 'q', new ItemStack(Items.quartz));
        GameRegistry.addRecipe(emptyCoreStack,"gig","nrn","gig",'n',goldIngotStack,'i',ironIngotStack,'g',glassStack,'r',simpleCatalystStack);
        GameRegistry.addRecipe(magicalesCraftedCableStack,"sss","mmm","sss",'s',new ItemStack(Items.string),'m',magicalesStack);
        GameRegistry.addRecipe(woodBraceStack," il","ili","li ",'l', new ItemStack(Blocks.log,1,craftingConstant),'i',new ItemStack(Items.string));
        GameRegistry.addRecipe(stoneBraceStack," is","isi","si ",'i', ironIngotStack,'s',reinforcedSlateStack);
        GameRegistry.addRecipe(obsidianBraceStack," is","ibi","si ",'i', obsidianStack,'s',reinforcedSlateStack,'b',stoneBraceStack);
        
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(projectileCoreStack, "mbm","aca","mom",'c', emptyCoreStack,'b',weakBloodShardStack,'m', magicalesStack,'o', magicianBloodOrbStack,'a',new ItemStack(Items.arrow)));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(selfCoreStack,"sbs","ncn","sos",'c', emptyCoreStack, 's',sanctusStack,'b', weakBloodShardStack,'o', magicianBloodOrbStack,'n',glowstoneDustStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(meleeCoreStack,"sbs","ncn","sos",'c', emptyCoreStack, 's',incendiumStack,'b', weakBloodShardStack,'o', magicianBloodOrbStack,'n',new ItemStack(Items.fire_charge)));
        GameRegistry.addRecipe(paradigmBackPlateStack,"isi","rgr","isi",'i',ironIngotStack,'r',stoneStack,'g',goldIngotStack,'s',reinforcedSlateStack);
        GameRegistry.addRecipe(outputCableStack, " si","s c"," si",'s',stoneStack,'i',ironIngotStack,'c',simpleCatalystStack); 
        
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(flameCoreStack,"mdm","scs","mom",'m',incendiumStack,'c',emptyCoreStack,'o',magicianBloodOrbStack,'d',diamondStack,'s',weakBloodShardStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(iceCoreStack,"mdm","scs","mom",'m',crystallosStack,'c',emptyCoreStack,'o',magicianBloodOrbStack,'d',diamondStack,'s',weakBloodShardStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(windCoreStack,"mdm","scs","mom",'m',aetherStack,'c',emptyCoreStack,'o',magicianBloodOrbStack,'d',diamondStack,'s',weakBloodShardStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(earthCoreStack,"mdm","scs","mom",'m',terraeStack,'c',emptyCoreStack,'o',magicianBloodOrbStack,'d',diamondStack,'s',weakBloodShardStack));

        GameRegistry.addRecipe(inputCableStack, "ws ","rcs","ws ",'w',blankSlateStack,'s',stoneStack,'r',imbuedSlateStack,'c',simpleCatalystStack);
        
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(defaultCoreStack,"msm","geg","mom",'m', strengthenedCatalystStack,'e', emptyCoreStack, 'o', magicianBloodOrbStack, 's',weakBloodShardStack, 'g', goldIngotStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(offenseCoreStack,"msm","geg","mom",'m', offensaStack,'e', emptyCoreStack, 'o', magicianBloodOrbStack, 's',weakBloodShardStack, 'g', goldIngotStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(defensiveCoreStack,"msm","geg","mom",'m', praesidiumStack,'e', emptyCoreStack, 'o', magicianBloodOrbStack, 's',weakBloodShardStack, 'g', goldIngotStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(environmentalCoreStack,"msm","geg","mom",'m', orbisTerraeStack,'e', emptyCoreStack, 'o', magicianBloodOrbStack, 's',weakBloodShardStack, 'g', goldIngotStack));
        
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(powerCoreStack,"msm","geg","mom",'m', virtusStack,'e', emptyCoreStack, 'o', masterBloodOrbStack, 's',weakBloodShardStack, 'g', goldIngotStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(costCoreStack,"msm","geg","mom",'m', reductusStack,'e', emptyCoreStack, 'o', masterBloodOrbStack, 's',weakBloodShardStack, 'g', goldIngotStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(potencyCoreStack,"msm","geg","mom",'m', potentiaStack,'e', emptyCoreStack, 'o', masterBloodOrbStack, 's',weakBloodShardStack, 'g', goldIngotStack));
        
        AlchemyRecipeRegistry.registerRecipe(crackedRunicPlateStackCrafted, 10, new ItemStack[]{imbuedSlateStack,imbuedSlateStack,concentratedCatalystStack}, 4);
        AlchemyRecipeRegistry.registerRecipe(runicPlateStack, 30, new ItemStack[]{crackedRunicPlateStack,terraeStack}, 5);
        AlchemyRecipeRegistry.registerRecipe(imbuedRunicPlateStack, 100, new ItemStack[]{magicalesStack,incendiumStack,runicPlateStack, runicPlateStack,aquasalusStack}, 5);
        AlchemyRecipeRegistry.registerRecipe(complexSpellCrystalStack,50,new ItemStack[]{new ItemStack(ModItems.blankSpell), weakBloodShardStack, weakBloodShardStack, diamondStack,goldIngotStack},3);
        
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockConduit,1,0),"q q","ccc","q q",'q', quartzRodStack,'c', magicalesCableStack);
        
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellParadigm,1,0),"gb ","pcw","gb ",'p',paradigmBackPlateStack,'c', projectileCoreStack,'g',goldIngotStack,'b',stoneBraceStack,'w',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellParadigm,1,1),"gb ","pcw","gb ",'p',paradigmBackPlateStack,'c', selfCoreStack,'g',goldIngotStack,'b',stoneBraceStack,'w',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellParadigm,1,2),"gb ","pcw","gb ",'p',paradigmBackPlateStack,'c', meleeCoreStack,'g',goldIngotStack,'b',stoneBraceStack,'w',outputCableStack);
        
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEffect,1,0),"bgb","ico","bgb",'c',flameCoreStack,'b',stoneBraceStack,'g',goldIngotStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEffect,1,1),"bgb","ico","bgb",'c',iceCoreStack,'b',stoneBraceStack,'g',goldIngotStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEffect,1,2),"bgb","ico","bgb",'c',windCoreStack,'b',stoneBraceStack,'g',goldIngotStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEffect,1,3),"bgb","ico","bgb",'c',earthCoreStack,'b',stoneBraceStack,'g',goldIngotStack,'i',inputCableStack,'o',outputCableStack);
        
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellModifier,1,0),"bgb","ico","bgb",'c',defaultCoreStack,'i',inputCableStack,'o',outputCableStack,'b',stoneBraceStack,'g',ironIngotStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellModifier,1,1),"bgb","ico","bgb",'c',offenseCoreStack,'i',inputCableStack,'o',outputCableStack,'b',stoneBraceStack,'g',ironIngotStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellModifier,1,2),"bgb","ico","bgb",'c',defensiveCoreStack,'i',inputCableStack,'o',outputCableStack,'b',stoneBraceStack,'g',ironIngotStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellModifier,1,3),"bgb","ico","bgb",'c',environmentalCoreStack,'i',inputCableStack,'o',outputCableStack,'b',stoneBraceStack,'g',ironIngotStack);
        
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement,1,0),"bpb","ico","bpb",'c', powerCoreStack,'b',woodBraceStack,'p',crackedRunicPlateStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement,1,1),"bpb","ico","bpb",'c', powerCoreStack,'b',stoneBraceStack,'p',runicPlateStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement,1,2),"bpb","ico","bpb",'c', powerCoreStack,'b',obsidianBraceStack,'p',imbuedRunicPlateStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement,1,5),"bpb","ico","bpb",'c', costCoreStack,'b',woodBraceStack,'p',crackedRunicPlateStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement,1,6),"bpb","ico","bpb",'c', costCoreStack,'b',stoneBraceStack,'p',runicPlateStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement,1,7),"bpb","ico","bpb",'c', costCoreStack,'b',obsidianBraceStack,'p',imbuedRunicPlateStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement,1,10),"bpb","ico","bpb",'c', potencyCoreStack,'b',woodBraceStack,'p',crackedRunicPlateStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement,1,11),"bpb","ico","bpb",'c', potencyCoreStack,'b',stoneBraceStack,'p',runicPlateStack,'i',inputCableStack,'o',outputCableStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSpellEnhancement,1,12),"bpb","ico","bpb",'c', potencyCoreStack,'b',obsidianBraceStack,'p',imbuedRunicPlateStack,'i',inputCableStack,'o',outputCableStack);
        
        GameRegistry.addShapelessRecipe(new ItemStack(Items.dye,5,15),fracturedBoneStack);
        
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.itemSigilOfSupression),"wtl","wvl","wol",'v',new ItemStack(ModItems.voidSigil),'t',new ItemStack(ModBlocks.blockTeleposer),'o',masterBloodOrbStack,'l',lavaBucketStack,'w',waterBucketStack));
        GameRegistry.addRecipe(new ShapedBloodOrbRecipe(new ItemStack(ModItems.itemSigilOfEnderSeverance),"ptp","ese","pop",'s',new ItemStack(ModItems.demonicSlate),'t',weakBloodShardStack,'o',masterBloodOrbStack,'e',new ItemStack(Items.ender_eye),'p', new ItemStack(Items.ender_pearl)));
        
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.flint,2,0), 1, new ItemStack[]{new ItemStack(Blocks.gravel),new ItemStack(Items.flint)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.grass), 2, new ItemStack[]{new ItemStack(Blocks.dirt),new ItemStack(Items.dye,1,15),new ItemStack(Items.wheat_seeds),new ItemStack(Items.wheat_seeds)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.leather,3,0), 2, new ItemStack[]{new ItemStack(Items.rotten_flesh),new ItemStack(Items.rotten_flesh),new ItemStack(Items.rotten_flesh),waterBucketStack,new ItemStack(Items.flint)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.bread), 1, new ItemStack[]{new ItemStack(Items.wheat),new ItemStack(Items.sugar)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.fire_charge,5,0), 3, new ItemStack[]{new ItemStack(Items.coal),new ItemStack(Items.blaze_powder),gunpowderStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.sand,2,0), 1, new ItemStack[]{new ItemStack(Blocks.cobblestone),gunpowderStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.clay,4,0), 2, new ItemStack[]{new ItemStack(Blocks.hardened_clay,1,craftingConstant),new ItemStack(Blocks.hardened_clay,1,craftingConstant),new ItemStack(Blocks.hardened_clay,1,craftingConstant),new ItemStack(Blocks.hardened_clay,1,craftingConstant),waterBucketStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.string,4,0), 1, new ItemStack[]{new ItemStack(Blocks.wool,1,craftingConstant),new ItemStack(Items.flint)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.gravel,2,0), 1, new ItemStack[]{new ItemStack(Blocks.stone),gunpowderStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.obsidian), 1, new ItemStack[]{waterBucketStack,lavaBucketStack}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Items.paper), 1, new ItemStack[]{new ItemStack(Items.reeds)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.soul_sand,3,0), 3, new ItemStack[]{new ItemStack(Blocks.sand),new ItemStack(Blocks.sand),new ItemStack(Blocks.sand),waterBucketStack,weakBloodShardStack}, 3);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.mycelium,1,0), 5, new ItemStack[]{new ItemStack(Blocks.grass),new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.red_mushroom)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(Blocks.ice), 2, new ItemStack[]{waterBucketStack,new ItemStack(Items.snowball)}, 1);
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
                ModItems.sanguineHelmet = new ItemSanguineArmour().setUnlocalizedName("sanguineHelmet");
                GameRegistry.registerItem(ModItems.sanguineHelmet, "sanguineHelmet");

                ItemStack itemGoggles = ItemApi.getItem("itemGoggles", 0);

                if (itemGoggles != null)
                {
                	BindingRegistry.registerRecipe(new ItemStack(ModItems.sanguineHelmet), itemGoggles);
                	
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
        
        if(Loader.isModLoaded("Forestry"))
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
        }else
        {
        	this.isForestryLoaded = false;
        }
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
    	AlchemicalPotionCreationHandler.addPotion(new ItemStack(Items.ender_pearl),AlchemicalWizardry.customPotionPlanarBinding.id,1*60*20);
    }
    
    public static void initAltarRecipes()
    {
    	AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.weakBloodOrb), new ItemStack(Items.diamond),1,2000,2,1,false);
    	AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.apprenticeBloodOrb), new ItemStack(Items.emerald),2,5000,5,5,false);
    	AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.magicianBloodOrb), new ItemStack(Blocks.gold_block),3,25000,20,20,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.masterBloodOrb), new ItemStack(ModItems.weakBloodShard),4,40000,30,50,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.archmageBloodOrb), new ItemStack(ModItems.demonBloodShard),5,75000,50,100,false);

		AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(ModItems.weakBloodOrb),1,2);
		AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(ModItems.apprenticeBloodOrb),2,5);
		AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(ModItems.magicianBloodOrb),3,15);
		AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(ModItems.masterBloodOrb),4,25);
		AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(ModItems.archmageBloodOrb),5,50);
		
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.telepositionFocus), new ItemStack(Items.ender_pearl),4,2000,10,10,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.enhancedTelepositionFocus), new ItemStack(ModItems.telepositionFocus),4,10000,25,15,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.demonicSlate), new ItemStack(ModItems.imbuedSlate),4,15000,20,20,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.duskScribeTool), new ItemStack(Blocks.coal_block),4,2000,20,10,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModBlocks.bloodSocket), new ItemStack(ModBlocks.emptySocket),3,30000,40,10,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.earthScribeTool), new ItemStack(Blocks.obsidian),3,1000,5,5,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.waterScribeTool), new ItemStack(Blocks.lapis_block),3,1000,5,5,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.blankSpell), new ItemStack(Blocks.glass),2,1000,5,5,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.blankSlate), new ItemStack(Blocks.stone),1,1000,5,5,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.activationCrystal), new ItemStack(ModItems.lavaCrystal),3,10000,20,10,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.fireScribeTool), new ItemStack(Items.magma_cream),3,1000,5,5,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.airScribeTool), new ItemStack(Items.ghast_tear),3,1000,5,5,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.imbuedSlate), new ItemStack(ModItems.reinforcedSlate),3,5000,15,10,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.daggerOfSacrifice), new ItemStack(Items.iron_sword),2,3000,5,5,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.alchemyFlask), new ItemStack(Items.glass_bottle),2,2000,5,5,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.reinforcedSlate), new ItemStack(ModItems.blankSlate),2,2000,5,5,false);
		AltarRecipeRegistry.registerAltarRecipe(new ItemStack(ModItems.bucketLife), new ItemStack(Items.bucket),1,1000,5,0,false);
    }
    
    public static void initRituals()
    {
    	Rituals.registerRitual("AW001Water", 1, 500, new RitualEffectWater(), "Ritual of the Full Spring");
    	Rituals.registerRitual("AW002Lava", 1, 10000, new RitualEffectLava(), "Serenade of the Nether");
    	Rituals.registerRitual("AW003GreenGrove", 1, 1000, new RitualEffectGrowth(), "Ritual of the Green Grove");
    	Rituals.registerRitual("AW004Interdiction", 1, 1000, new RitualEffectInterdiction(), "Interdiction Ritual");
    	Rituals.registerRitual("AW005Containment", 1, 2000, new RitualEffectContainment(), "Ritual of Containment");
    	Rituals.registerRitual("AW006Binding", 1, 5000, new RitualEffectSoulBound(), "Ritual of Binding");
        Rituals.registerRitual("AW007Unbinding", 1, 30000, new RitualEffectUnbinding(), "Ritual of Unbinding");
        Rituals.registerRitual("AW008HighJump", 1, 1000, new RitualEffectJumping(), "Ritual of the High Jump");
        Rituals.registerRitual("AW009Magnetism", 1, 5000, new RitualEffectMagnetic(), "Ritual of Magnetism");
        Rituals.registerRitual("AW010Crusher", 1, 2500, new RitualEffectCrushing(), "Ritual of the Crusher");
        Rituals.registerRitual("AW011Speed", 1, 1000, new RitualEffectLeap(), "Ritual of Speed");
        Rituals.registerRitual("AW012AnimalGrowth", 1, 10000, new RitualEffectAnimalGrowth(), "Ritual of the Shepherd");
        Rituals.registerRitual("AW013Suffering", 1, 50000, new RitualEffectWellOfSuffering(), "Well of Suffering");
        Rituals.registerRitual("AW014Regen", 1, 25000, new RitualEffectHealing(), "Ritual of Regeneration");
        Rituals.registerRitual("AW015FeatheredKnife", 1, 50000, new RitualEffectFeatheredKnife(), "Ritual of the Feathered Knife");
        Rituals.registerRitual("AW016FeatheredEarth", 2, 100000, new RitualEffectFeatheredEarth(), "Ritual of the Feathered Earth");
        Rituals.registerRitual("AW017Gaia", 2, 1000000, new RitualEffectBiomeChanger(), "Ritual of Gaia's Transformation");
        Rituals.registerRitual("AW018Condor", 2, 1000000, new RitualEffectFlight(), "Reverence of the Condor");
        Rituals.registerRitual("AW019FallingTower", 2, 1000000, new RitualEffectSummonMeteor(), "Mark of the Falling Tower");
        Rituals.registerRitual("AW020BalladOfAlchemy", 1, 20000, new RitualEffectAutoAlchemy(), "Ballad of Alchemy");
        Rituals.registerRitual("AW021Expulsion", 1, 1000000, new RitualEffectExpulsion(), "Aura of Expulsion");
        Rituals.registerRitual("AW022Supression", 1, 10000, new RitualEffectSupression(), "Dome of Supression");
        Rituals.registerRitual("AW023Zephyr", 1, 25000, new RitualEffectItemSuction(),"Call of the Zephyr");
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
}
