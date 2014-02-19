package WayofTime.alchemicalWizardry;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.block.Block;
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
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryEventHooks;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryFuelHandler;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryTickHandler;
import WayofTime.alchemicalWizardry.common.CommonProxy;
import WayofTime.alchemicalWizardry.common.EntityAirElemental;
import WayofTime.alchemicalWizardry.common.LifeBucketHandler;
import WayofTime.alchemicalWizardry.common.LifeEssence;
import WayofTime.alchemicalWizardry.common.ModLivingDropsEvent;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.PotionBoost;
import WayofTime.alchemicalWizardry.common.PotionDrowning;
import WayofTime.alchemicalWizardry.common.PotionFlameCloak;
import WayofTime.alchemicalWizardry.common.PotionFlight;
import WayofTime.alchemicalWizardry.common.PotionIceCloak;
import WayofTime.alchemicalWizardry.common.PotionInhibit;
import WayofTime.alchemicalWizardry.common.PotionProjectileProtect;
import WayofTime.alchemicalWizardry.common.PotionReciprocation;
import WayofTime.alchemicalWizardry.common.alchemy.AlchemicalPotionCreationHandler;
import WayofTime.alchemicalWizardry.common.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.common.altarRecipeRegistry.AltarRecipeRegistry;
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
import WayofTime.alchemicalWizardry.common.items.ItemSpellEffectBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellEnhancementBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellModifierBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellParadigmBlock;
import WayofTime.alchemicalWizardry.common.items.LifeBucket;
import WayofTime.alchemicalWizardry.common.items.forestry.ItemBloodFrame;
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
import WayofTime.alchemicalWizardry.common.tileEntity.TEOrientable;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPedestal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEffectBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEnhancementBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellModifierBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellParadigmBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import WayofTime.alchemicalWizardry.common.tileEntity.gui.GuiHandler;
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
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "AWWayofTime", name = "AlchemicalWizardry", version = "v0.8.0")
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

    public static Potion customPotionDrowning;
    public static Potion customPotionBoost;
    public static Potion customPotionProjProt;
    public static Potion customPotionInhibit;
    public static Potion customPotionFlight;
    public static Potion customPotionReciprocation;
    public static Potion customPotionFlameCloak;
    public static Potion customPotionIceCloak;

    public static int customPotionDrowningID;
    public static int customPotionBoostID;
    public static int customPotionProjProtID;
    public static int customPotionInhibitID;
    public static int customPotionFlightID;
    public static int customPotionReciprocationID;
    public static int customPotionFlameCloakID;
    public static int customPotionIceCloakID;

    public static boolean isThaumcraftLoaded;
    public static boolean isForestryLoaded;

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
        
        FMLCommonHandler.instance().bus().register(new AlchemicalWizardryEventHooks());
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
        //ItemStacks used for crafting go here
        ItemStack lavaBucketStack = new ItemStack(Items.lava_bucket);
        ItemStack cobblestoneStack = new ItemStack(Blocks.cobblestone);
        ItemStack glassStack = new ItemStack(Blocks.glass, 1, craftingConstant);
        ItemStack ironStack = new ItemStack(Items.iron_ingot);
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
        GameRegistry.addShapelessRecipe(new ItemStack(Items.skull, 1, 2), new ItemStack(Items.skull, 1, 1), new ItemStack(Items.rotten_flesh), new ItemStack(Items.iron_ingot), new ItemStack(Items.leather));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.skull, 1, 0), new ItemStack(Items.skull, 1, 1), new ItemStack(Items.bow, 1, 0), new ItemStack(Items.arrow, 1, 0), new ItemStack(Items.bone));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.skull, 1, 4), new ItemStack(Items.skull, 1, 1), new ItemStack(Items.gunpowder), new ItemStack(Blocks.dirt), new ItemStack(Blocks.sand));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockWritingTable), " s ", "ror", 's', new ItemStack(Items.brewing_stand), 'r', obsidianStack, 'o', weakBloodOrbStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockPedestal), "ooo", " c ", "ooo", 'o', obsidianStack, 'c', weakBloodShardStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockPlinth), "iii", " p ", "iii", 'i', ironBlockStack, 'p', new ItemStack(ModBlocks.blockPedestal));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.alchemyFlask, 1, 0), new ItemStack(ModItems.alchemyFlask, 1, craftingConstant), new ItemStack(Items.nether_wart), redstoneStack, glowstoneDustStack);
        GameRegistry.addRecipe(new ItemStack(ModItems.sigilOfHaste), "csc", "sts", "ror", 'c', new ItemStack(Items.cookie), 's', new ItemStack(Items.sugar), 't', ModItems.demonicSlate, 'r', obsidianStack, 'o', new ItemStack(ModItems.masterBloodOrb));
        GameRegistry.addRecipe(new ItemStack(ModItems.sigilOfWind), "faf", "grg", "fof", 'f', featherStack, 'g', ghastTearStack, 'a', new ItemStack(ModItems.airSigil), 'o', new ItemStack(ModItems.masterBloodOrb), 'r', ModItems.demonicSlate);
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.weakBloodShard, 5, 0), new ItemStack(ModItems.masterBloodOrb), new ItemStack(ModItems.weakBloodShard), imbuedSlateStack);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockTeleposer), "ggg", "efe", "ggg", 'g', goldIngotStack, 'f', new ItemStack(ModItems.telepositionFocus), 'e', new ItemStack(Items.ender_pearl));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.reinforcedTelepositionFocus), new ItemStack(ModItems.enhancedTelepositionFocus), new ItemStack(ModItems.weakBloodShard));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.demonicTelepositionFocus), new ItemStack(ModItems.reinforcedTelepositionFocus), new ItemStack(ModItems.demonBloodShard));
        GameRegistry.addRecipe(new ItemStack(ModItems.sigilOfTheBridge), "nnn", "nsn", "ror", 'n', stoneStack, 'r', new ItemStack(Blocks.soul_sand), 's', imbuedSlateStack, 'o', magicianBloodOrbStack);
        GameRegistry.addRecipe(new ItemStack(ModItems.armourInhibitor), " gg", "gsg", "gg ", 'g', goldIngotStack, 's', new ItemStack(ModItems.weakBloodShard));
        GameRegistry.addRecipe(new ItemStack(ModItems.itemRitualDiviner), "d1d", "2e3", "d4d", '1', new ItemStack(ModItems.airScribeTool), '2', new ItemStack(ModItems.waterScribeTool), '3', new ItemStack(ModItems.fireScribeTool), '4', new ItemStack(ModItems.earthScribeTool), 'd', diamondStack, 'e', new ItemStack(Items.emerald));
        GameRegistry.addRecipe(duskRitualDivinerStack, " d ", "srs", " d ", 'd', new ItemStack(ModItems.duskScribeTool), 's', new ItemStack(ModItems.demonicSlate), 'r', new ItemStack(ModItems.itemRitualDiviner));
        GameRegistry.addRecipe(new ItemStack(ModItems.sigilOfMagnetism), "bgb", "gsg", "bob", 'b', new ItemStack(Blocks.iron_block), 'g', goldIngotStack, 's', new ItemStack(ModItems.imbuedSlate), 'o', magicianBloodOrbStack);
        GameRegistry.addRecipe(new ItemStack(ModItems.energyBazooka), "Ocd", "cb ", "d w", 'O', archmageBloodOrbStack, 'c', crepitousStack, 'b', new ItemStack(ModItems.energyBlaster), 'd', diamondStack, 'w', new ItemStack(ModItems.weakBloodShard));
        GameRegistry.addRecipe(new ItemStack(ModItems.itemBloodLightSigil), "btb", "sss", "bob", 'o', magicianBloodOrbStack, 'b', glowstoneBlockStack, 't', new ItemStack(Blocks.torch), 's', imbuedSlateStack);
        GameRegistry.addRecipe(new ItemStack(ModItems.itemKeyOfDiablo), " gw", "gdg", "wg ", 'w', weakBloodShardStack, 'g', goldIngotStack, 'd', diamondStack);
        customPotionDrowning = (new PotionDrowning(customPotionDrowningID, true, 0)).setIconIndex(0, 0).setPotionName("Drowning");
        customPotionBoost = (new PotionBoost(customPotionBoostID, false, 0)).setIconIndex(0, 0).setPotionName("Boost");
        customPotionProjProt = (new PotionProjectileProtect(customPotionProjProtID, false, 0)).setIconIndex(0, 0).setPotionName("Whirlwind");
        customPotionInhibit = (new PotionInhibit(customPotionInhibitID, false, 0)).setIconIndex(0, 0).setPotionName("Inhibit");
        customPotionFlight = (new PotionFlight(customPotionFlightID, false, 0)).setIconIndex(0, 0).setPotionName("Flight");
        customPotionReciprocation = (new PotionReciprocation(customPotionReciprocationID, false, 0xFFFFFF)).setIconIndex(0, 0).setPotionName("Reciprocation");
        customPotionFlameCloak = (new PotionFlameCloak(customPotionFlameCloakID,false,0).setIconIndex(0,0).setPotionName("Flame Cloak"));
        customPotionIceCloak = (new PotionIceCloak(customPotionIceCloakID,false,0).setIconIndex(0,0).setPotionName("Ice Cloak"));

        //FluidStack lifeEssenceFluidStack = new FluidStack(lifeEssenceFluid, 1);
        //LiquidStack lifeEssence = new LiquidStack(lifeEssenceFlowing, 1);
        //LiquidDictionary.getOrCreateLiquid("Life Essence", lifeEssence);
        

//        ModBlocks.blockLifeEssence.setUnlocalizedName("lifeEssenceBlock");
        FluidContainerRegistry.registerFluidContainer(lifeEssenceFluid, new ItemStack(ModItems.bucketLife), FluidContainerRegistry.EMPTY_BUCKET);
        
        //lifeEssenceFluid.setUnlocalizedName("lifeEssence");
        //LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Life Essence", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(AlchemicalWizardry.bucketLife), new ItemStack(Item.bucketEmpty)));
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
        proxy.registerEntityTrackers();
        //Gui registration
        // NetworkRegistry.instance().registerGuiHandler(this, new GuiHandlerAltar());
        Rituals.loadRituals();
        UpgradedAltars.loadAltars();
        SigilOfHolding.initiateSigilOfHolding();
        ArmourForge.initializeRecipes();
        TEPlinth.initialize();
        AlchemicalPotionCreationHandler.initializePotions();
        AltarRecipeRegistry.initRecipes();
        //MinecraftForge.setToolClass(ModItems.boundPickaxe, "pickaxe", 5);
        //MinecraftForge.setToolClass(ModItems.boundAxe, "axe", 5);
        //MinecraftForge.setToolClass(ModItems.boundShovel, "shovel", 5);
        FMLCommonHandler.instance().bus().register(new ModLivingDropsEvent());
        proxy.InitRendering();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
//        ItemStack[] comp = new ItemStack[5];
//        for(int i=0;i<5;i++)
//        {
//        	comp[i] = redstoneStack;
//        }
//        AlchemyRecipeRegistry.registerRecipe(glowstoneDustStack, 2, comp, 2);
        //TODO NEW RECIPES!
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.weakBindingAgent), 10, new ItemStack[]{simpleCatalystStack, simpleCatalystStack, new ItemStack(Items.clay_ball)}, 2);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.standardBindingAgent), 15, new ItemStack[]{new ItemStack(ModItems.weakBindingAgent), sanctusStack, new ItemStack(ModItems.crystallos)}, 3);
        AlchemyRecipeRegistry.registerRecipe(simpleCatalystStack, 2, new ItemStack[]{sugarStack, redstoneStack, redstoneStack, glowstoneDustStack, new ItemStack(Items.gunpowder)}, 1);
        AlchemyRecipeRegistry.registerRecipe(new ItemStack(ModItems.incendium), 5, new ItemStack[]{lavaBucketStack, new ItemStack(Items.blaze_powder), new ItemStack(Items.blaze_powder), new ItemStack(Blocks.netherrack), simpleCatalystStack}, 1);
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
        HomSpellRegistry.registerBasicSpell(new ItemStack(Items.flint_and_steel), new SpellFireBurst());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Blocks.ice), new SpellFrozenWater());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Blocks.tnt), new SpellExplosions());
        HomSpellRegistry.registerBasicSpell(new ItemStack(ModItems.apprenticeBloodOrb), new SpellHolyBlast());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Items.ghast_tear), new SpellWindGust());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Items.glowstone_dust), new SpellLightningBolt());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Items.water_bucket), new SpellWateryGrave());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Blocks.obsidian), new SpellEarthBender());
        HomSpellRegistry.registerBasicSpell(new ItemStack(Items.ender_pearl), new SpellTeleport());
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityFallenAngelID), new ItemStack[]{sanctusStack, sanctusStack, sanctusStack, aetherStack, tennebraeStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityLowerGuardianID), new ItemStack[]{cobblestoneStack, cobblestoneStack, terraeStack, tennebraeStack, new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_nugget)}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityBileDemonID), new ItemStack[]{new ItemStack(Items.poisonous_potato), tennebraeStack, terraeStack, new ItemStack(Items.porkchop), new ItemStack(Items.egg), new ItemStack(Items.beef)}, new ItemStack[]{crepitousStack, crepitousStack, terraeStack, ironBlockStack, ironBlockStack, diamondStack}, new ItemStack[]{}, 0, 5);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityWingedFireDemonID), new ItemStack[]{aetherStack, incendiumStack, incendiumStack, incendiumStack, tennebraeStack, new ItemStack(Blocks.netherrack)}, new ItemStack[]{diamondStack, new ItemStack(Blocks.gold_block), magicalesStack, magicalesStack, new ItemStack(Items.fire_charge), new ItemStack(Blocks.coal_block)}, new ItemStack[]{}, 0, 5);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entitySmallEarthGolemID), new ItemStack[]{new ItemStack(Items.clay_ball), terraeStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityIceDemonID), new ItemStack[]{crystallosStack, crystallosStack, aquasalusStack, crystallosStack, sanctusStack, terraeStack}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityBoulderFistID), new ItemStack[]{terraeStack, sanctusStack, tennebraeStack, new ItemStack(Items.bone), new ItemStack(Items.cooked_beef), new ItemStack(Items.cooked_beef)}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
        SummoningRegistry.registerSummon(new SummoningHelper(this.entityShadeID), new ItemStack[]{tennebraeStack, tennebraeStack, tennebraeStack, aetherStack, glassStack, new ItemStack(Items.glass_bottle)}, new ItemStack[]{}, new ItemStack[]{}, 0, 4);
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

//            try
//            {
//                //do stuff
//                ModItems.sanguineHelmet = new ItemSanguineArmour(sanguineHelmetItemID).setUnlocalizedName("sanguineHelmet");
//                LanguageRegistry.addName(ModItems.sanguineHelmet, "Sanguine Helmet");
////                focusBloodBlast = new FocusBloodBlast(focusBloodBlastItemID);
////                LanguageRegistry.addName(focusBloodBlast,"Wand Focus: Blood Blast");
////
////                focusGravityWell = new FocusGravityWell(focusGravityWellItemID);
////                LanguageRegistry.addName(focusGravityWell,"Wand Focus: Gravity Well");
//                ItemStack itemGoggles = ItemApi.getItem("itemGoggles", 0);
//
//                if (itemGoggles != null)
//                {
//                    //GameRegistry.addShapelessRecipe(new ItemStack(this.sanguineHelmet), itemGoggles);
//                }
//
//                //LogHelper.log(Level.INFO, "Loaded RP2 World addon");
//            } catch (Exception e)
//            {
//                //LogHelper.log(Level.SEVERE, "Could not load RP2 World addon");
//                e.printStackTrace(System.err);
//            }
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
}
