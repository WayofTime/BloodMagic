package WayofTime.alchemicalWizardry.client;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import org.w3c.dom.Document;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.client.book.pages.BlankPage;
import WayofTime.alchemicalWizardry.client.book.pages.BookPage;
import WayofTime.alchemicalWizardry.client.book.pages.ContentsTablePage;
import WayofTime.alchemicalWizardry.client.book.pages.CraftingPage;
import WayofTime.alchemicalWizardry.client.book.pages.FurnacePage;
import WayofTime.alchemicalWizardry.client.book.pages.PicturePage;
import WayofTime.alchemicalWizardry.client.book.pages.SectionPage;
import WayofTime.alchemicalWizardry.client.book.pages.SidebarPage;
import WayofTime.alchemicalWizardry.client.book.pages.TextPage;
import WayofTime.alchemicalWizardry.client.book.pages.TitlePage;
import WayofTime.alchemicalWizardry.common.CommonProxy;
import WayofTime.alchemicalWizardry.common.EntityAirElemental;
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
import WayofTime.alchemicalWizardry.common.entity.mob.EntityTestDemon;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityWaterElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityWingedFireDemon;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityEnergyBazookaMainProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityMeteor;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderConduit;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderPedestal;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderPlinth;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderSpellEffectBlock;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderSpellEnhancementBlock;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderSpellModifierBlock;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderSpellParadigmBlock;
import WayofTime.alchemicalWizardry.common.renderer.block.RenderWritingTable;
import WayofTime.alchemicalWizardry.common.renderer.block.TEAltarRenderer;
import WayofTime.alchemicalWizardry.common.renderer.block.itemRender.TEAltarItemRenderer;
import WayofTime.alchemicalWizardry.common.renderer.block.itemRender.TEConduitItemRenderer;
import WayofTime.alchemicalWizardry.common.renderer.block.itemRender.TESpellEffectBlockItemRenderer;
import WayofTime.alchemicalWizardry.common.renderer.block.itemRender.TESpellEnhancementBlockItemRenderer;
import WayofTime.alchemicalWizardry.common.renderer.block.itemRender.TESpellModifierBlockItemRenderer;
import WayofTime.alchemicalWizardry.common.renderer.block.itemRender.TESpellParadigmBlockItemRenderer;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderBileDemon;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderBoulderFist;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderElemental;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderFallenAngel;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderIceDemon;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderLowerGuardian;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderShade;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderSmallEarthGolem;
import WayofTime.alchemicalWizardry.common.renderer.mob.RenderWingedFireDemon;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelBileDemon;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelBoulderFist;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelElemental;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelFallenAngel;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelIceDemon;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelLowerGuardian;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelShade;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelSmallEarthGolem;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelWingedFireDemon;
import WayofTime.alchemicalWizardry.common.renderer.projectile.RenderEnergyBazookaMainProjectile;
import WayofTime.alchemicalWizardry.common.renderer.projectile.RenderEnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.renderer.projectile.RenderMeteor;
import WayofTime.alchemicalWizardry.common.spell.complex.EntitySpellProjectile;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEConduit;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPedestal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEffectBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEnhancementBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellModifierBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellParadigmBlock;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	public static SmallFontRenderer smallFontRenderer;
	public static Minecraft mc;
    public static int renderPass;
    public static int altarRenderType;

    @Override
    public void registerRenderers()
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	smallFontRenderer = new SmallFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
    	readManuals();
        //altarRenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerEntityRenderingHandler(EnergyBlastProjectile.class, new RenderEnergyBlastProjectile());
        RenderingRegistry.registerEntityRenderingHandler(EntityEnergyBazookaMainProjectile.class, new RenderEnergyBazookaMainProjectile());
        RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderEnergyBlastProjectile());
        RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, new RenderMeteor());
        //EntityRegistry.registerGlobalEntityID(EntityFallenAngel.class, "AlchemicalWizardry.FallenAngel", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityFallenAngel.class, new RenderFallenAngel(new ModelFallenAngel(), 0.5F));
        //EntityRegistry.registerGlobalEntityID(EntityLowerGuardian.class, "AlchemicalWizardry.LowerGuardian", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityLowerGuardian.class, new RenderLowerGuardian(new ModelLowerGuardian(), 0.5F));
        //EntityRegistry.registerGlobalEntityID(EntityBileDemon.class, "AlchemicalWizardry.BileDemon", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityBileDemon.class, new RenderBileDemon(new ModelBileDemon(), 1.5F));
        //EntityRegistry.registerGlobalEntityID(EntityWingedFireDemon.class, "AlchemicalWizardry.WingedFireDemon", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityWingedFireDemon.class, new RenderWingedFireDemon(new ModelWingedFireDemon(), 1.0F));
        //EntityRegistry.registerGlobalEntityID(EntitySmallEarthGolem.class, "AlchemicalWizardry.SmallEarthGolem", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntitySmallEarthGolem.class, new RenderSmallEarthGolem(new ModelSmallEarthGolem(), 0.5F));
        //EntityRegistry.registerGlobalEntityID(EntityIceDemon.class, "AlchemicalWizardry.IceDemon", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityIceDemon.class, new RenderIceDemon(new ModelIceDemon(), 0.5F));
        // EntityRegistry.registerGlobalEntityID(EntityBoulderFist.class, "AlchemicalWizardry.BoulderFist", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityBoulderFist.class, new RenderBoulderFist(new ModelBoulderFist(), 0.5F));
        //EntityRegistry.registerGlobalEntityID(EntityShade.class, "AlchemicalWizardry.Shade", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityShade.class, new RenderShade(new ModelShade(), 0.5F));
        //EntityRegistry.registerGlobalEntityID(EntityAirElemental.class, "AlchemicalWizardry.AirElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityAirElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        //EntityRegistry.registerGlobalEntityID(EntityWaterElemental.class, "AlchemicalWizardry.WaterElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityWaterElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        //EntityRegistry.registerGlobalEntityID(EntityEarthElemental.class, "AlchemicalWizardry.EarthElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityEarthElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        //EntityRegistry.registerGlobalEntityID(EntityFireElemental.class, "AlchemicalWizardry.FireElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityFireElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        //EntityRegistry.registerGlobalEntityID(EntityShadeElemental.class, "AlchemicalWizardry.ShadeElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityShadeElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        //EntityRegistry.registerGlobalEntityID(EntityHolyElemental.class, "AlchemicalWizardry.HolyElemental", EntityRegistry.findGlobalUniqueEntityId(),0x40FF00, 0x0B610B);
        RenderingRegistry.registerEntityRenderingHandler(EntityHolyElemental.class, new RenderElemental(new ModelElemental(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTestDemon.class, new RenderFallenAngel(new ModelFallenAngel(), 0.5F));

        ClientRegistry.bindTileEntitySpecialRenderer(TEPedestal.class, new RenderPedestal());
        ClientRegistry.bindTileEntitySpecialRenderer(TEPlinth.class, new RenderPlinth());
        ClientRegistry.bindTileEntitySpecialRenderer(TEWritingTable.class, new RenderWritingTable());
        ClientRegistry.bindTileEntitySpecialRenderer(TEConduit.class, new RenderConduit());
        ClientRegistry.bindTileEntitySpecialRenderer(TESpellEffectBlock.class, new RenderSpellEffectBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TESpellEnhancementBlock.class, new RenderSpellEnhancementBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TESpellParadigmBlock.class, new RenderSpellParadigmBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TESpellModifierBlock.class, new RenderSpellModifierBlock());
        //Item Renderer stuff
        MinecraftForgeClient.registerItemRenderer(ModBlocks.blockConduit.blockID, new TEConduitItemRenderer());
        MinecraftForgeClient.registerItemRenderer(ModBlocks.blockSpellEffect.blockID, new TESpellEffectBlockItemRenderer());
        MinecraftForgeClient.registerItemRenderer(ModBlocks.blockSpellEnhancement.blockID, new TESpellEnhancementBlockItemRenderer());
        MinecraftForgeClient.registerItemRenderer(ModBlocks.blockSpellParadigm.blockID, new TESpellParadigmBlockItemRenderer());
        MinecraftForgeClient.registerItemRenderer(ModBlocks.blockSpellModifier.blockID, new TESpellModifierBlockItemRenderer());
        //RenderingRegistry.registerEntityRenderingHandler(FireProjectile.class, new RenderFireProjectile());
        //RenderingRegistry.registerBlockHandler(new AltarRenderer());
    }

    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void InitRendering()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TEAltar.class, new TEAltarRenderer());
        MinecraftForgeClient.registerItemRenderer(ModBlocks.blockAltar.blockID, new TEAltarItemRenderer());
        //MinecraftForgeClient.registerItemRenderer(AlchemicalWizardry.blockWritingTable.blockID, new TEWritingTableItemRenderer());
    }
    
    public static Map<String, Class<? extends BookPage>> pageClasses = new HashMap<String, Class<? extends BookPage>>();
    
    public static void registerManualPage (String type, Class<? extends BookPage> clazz)
    {
        pageClasses.put(type, clazz);
    }
    
    public static Class<? extends BookPage> getPageClass (String type)
    {
        return pageClasses.get(type);
    }
    
    void initManualPages ()
    {
        ClientProxy.registerManualPage("crafting", CraftingPage.class);
        ClientProxy.registerManualPage("picture", PicturePage.class);
        ClientProxy.registerManualPage("text", TextPage.class);
        ClientProxy.registerManualPage("intro", TextPage.class);
        ClientProxy.registerManualPage("sectionpage", SectionPage.class);
        ClientProxy.registerManualPage("intro", TitlePage.class);
        ClientProxy.registerManualPage("contents", ContentsTablePage.class);
        ClientProxy.registerManualPage("furnace", FurnacePage.class);
        ClientProxy.registerManualPage("sidebar", SidebarPage.class);
//        ClientProxy.registerManualPage("materialstats", MaterialPage.class);
//        ClientProxy.registerManualPage("toolpage", ToolPage.class);
//        ClientProxy.registerManualPage("modifier", ModifierPage.class);
//        ClientProxy.registerManualPage("blockcast", BlockCastPage.class);


        ClientProxy.registerManualPage("blank", BlankPage.class);
    }
    
    public static Document diary;
    public static Document volume1;
    public static Document volume2;
    public static Document smelter;


    public void readManuals ()
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        diary = readManual("/assets/alchemicalwizardry/books/architect.xml", dbFactory);
//        volume1 = readManual("/assets/tinker/manuals/firstday.xml", dbFactory);
//        volume2 = readManual("/assets/tinker/manuals/materials.xml", dbFactory);
//        smelter = readManual("/assets/tinker/manuals/smeltery.xml", dbFactory);
        initManualIcons();
        initManualRecipes();
        initManualPages();
    }

    public void initManualIcons ()
    {

        BMClientRegistry.registerManualIcon("torch", new ItemStack(Block.torchWood));
        BMClientRegistry.registerManualIcon("sapling", new ItemStack(Block.sapling));
        BMClientRegistry.registerManualIcon("workbench", new ItemStack(Block.workbench));
        BMClientRegistry.registerManualIcon("coal", new ItemStack(Item.coal));


//        BMClientRegistry.registerManualIcon("obsidianingot", new ItemStack(TContent.materials, 1, 18));
//        BMClientRegistry.registerManualIcon("lavacrystal", new ItemStack(TContent.materials, 1, 7));


        // Tool Materials
//        BMClientRegistry.registerManualIcon("woodplanks", new ItemStack(Block.planks));
//        BMClientRegistry.registerManualIcon("stoneblock", new ItemStack(Block.stone));
//        BMClientRegistry.registerManualIcon("ironingot", new ItemStack(Item.ingotIron));
//        BMClientRegistry.registerManualIcon("flint", new ItemStack(Item.flint));
//        BMClientRegistry.registerManualIcon("cactus", new ItemStack(Block.cactus));
//        BMClientRegistry.registerManualIcon("bone", new ItemStack(Item.bone));
//        BMClientRegistry.registerManualIcon("obsidian", new ItemStack(Block.obsidian));
//        BMClientRegistry.registerManualIcon("netherrack", new ItemStack(Block.netherrack));
//        BMClientRegistry.registerManualIcon("blueslimecrystal", new ItemStack(TContent.materials, 1, 17));
//        BMClientRegistry.registerManualIcon("slimecrystal", new ItemStack(TContent.materials, 1, 1));
//        BMClientRegistry.registerManualIcon("paperstack", new ItemStack(TContent.materials, 1, 0));
//        BMClientRegistry.registerManualIcon("cobaltingot", new ItemStack(TContent.materials, 1, 3));
//        BMClientRegistry.registerManualIcon("arditeingot", new ItemStack(TContent.materials, 1, 4));
//        BMClientRegistry.registerManualIcon("copperingot", new ItemStack(TContent.materials, 1, 9));
//        BMClientRegistry.registerManualIcon("steelingot", new ItemStack(TContent.materials, 1, 16));
//        BMClientRegistry.registerManualIcon("pigironingot", new ItemStack(TContent.materials, 1, 34));


        // Tool parts
//        BMClientRegistry.registerManualIcon("pickhead", new ItemStack(TContent.pickaxeHead, 1, 2));
//        BMClientRegistry.registerManualIcon("shovelhead", new ItemStack(TContent.shovelHead, 1, 2));
//        BMClientRegistry.registerManualIcon("axehead", new ItemStack(TContent.hatchetHead, 1, 2));
//        BMClientRegistry.registerManualIcon("swordblade", new ItemStack(TContent.swordBlade, 1, 2));
//        BMClientRegistry.registerManualIcon("pan", new ItemStack(TContent.frypanHead, 1, 2));
//        BMClientRegistry.registerManualIcon("board", new ItemStack(TContent.signHead, 1, 2));
//        BMClientRegistry.registerManualIcon("knifeblade", new ItemStack(TContent.knifeBlade, 1, 2));
//        BMClientRegistry.registerManualIcon("chiselhead", new ItemStack(TContent.chiselHead, 1, 2));
//
//
//        BMClientRegistry.registerManualIcon("hammerhead", new ItemStack(TContent.hammerHead, 1, 2));
//        BMClientRegistry.registerManualIcon("excavatorhead", new ItemStack(TContent.excavatorHead, 1, 2));
//        BMClientRegistry.registerManualIcon("scythehead", new ItemStack(TContent.scytheBlade, 1, 2));
//        BMClientRegistry.registerManualIcon("broadaxehead", new ItemStack(TContent.broadAxeHead, 1, 2));
//        BMClientRegistry.registerManualIcon("largeswordblade", new ItemStack(TContent.largeSwordBlade, 1, 2));
//
//
//        BMClientRegistry.registerManualIcon("toolrod", new ItemStack(Item.stick));
//
//
//        BMClientRegistry.registerManualIcon("binding", new ItemStack(TContent.binding, 1, 4));
//        BMClientRegistry.registerManualIcon("wideguard", new ItemStack(TContent.wideGuard, 1, 4));
//        BMClientRegistry.registerManualIcon("handguard", new ItemStack(TContent.handGuard, 1, 4));
//        BMClientRegistry.registerManualIcon("crossbar", new ItemStack(TContent.crossbar, 1, 4));
//
//
//        BMClientRegistry.registerManualIcon("toughrod", new ItemStack(TContent.toughRod, 1, 0));
//        BMClientRegistry.registerManualIcon("toughbinding", new ItemStack(TContent.toughBinding, 1, 17));
//        BMClientRegistry.registerManualIcon("largeplate", new ItemStack(TContent.largePlate, 1, 17));
//
//
//        BMClientRegistry.registerManualIcon("bowstring", new ItemStack(TContent.bowstring, 1, 0));
//        BMClientRegistry.registerManualIcon("arrowhead", new ItemStack(TContent.arrowhead, 1, 2));
//        BMClientRegistry.registerManualIcon("fletching", new ItemStack(TContent.fletching, 1, 0));
//        
//        BMClientRegistry.registerManualIcon("bloodbucket", new ItemStack(TContent.buckets, 1, 16));
//        BMClientRegistry.registerManualIcon("emeraldbucket", new ItemStack(TContent.buckets, 1, 15));
//        BMClientRegistry.registerManualIcon("gluebucket", new ItemStack(TContent.buckets, 1, 25));
//        BMClientRegistry.registerManualIcon("slimebucket", new ItemStack(TContent.buckets, 1, 24));
//        BMClientRegistry.registerManualIcon("enderbucket", new ItemStack(TContent.buckets, 1, 23));


        // ToolIcons
//        BMClientRegistry.registerManualIcon("pickicon",
//                ToolBuilder.instance.buildTool(new ItemStack(TContent.pickaxeHead, 1, 10), new ItemStack(TContent.toolRod, 1, 11), new ItemStack(TContent.binding, 1, 12), ""));
//        BMClientRegistry.registerManualIcon("shovelicon", ToolBuilder.instance.buildTool(new ItemStack(TContent.shovelHead, 1, 10), new ItemStack(TContent.toolRod, 1, 11), null, ""));
//        BMClientRegistry.registerManualIcon("axeicon", ToolBuilder.instance.buildTool(new ItemStack(TContent.hatchetHead, 1, 10), new ItemStack(TContent.toolRod, 1, 11), null, ""));
//        BMClientRegistry.registerManualIcon("mattockicon",
//                ToolBuilder.instance.buildTool(new ItemStack(TContent.hatchetHead, 1, 10), new ItemStack(TContent.toolRod, 1, 11), new ItemStack(TContent.shovelHead, 1, 12), ""));
//        BMClientRegistry.registerManualIcon("swordicon",
//                ToolBuilder.instance.buildTool(new ItemStack(TContent.swordBlade, 1, 10), new ItemStack(TContent.toolRod, 1, 11), new ItemStack(TContent.wideGuard, 1, 12), ""));
//        BMClientRegistry.registerManualIcon("longswordicon",
//                ToolBuilder.instance.buildTool(new ItemStack(TContent.swordBlade, 1, 10), new ItemStack(TContent.toolRod, 1, 11), new ItemStack(TContent.handGuard, 1, 12), ""));
//        BMClientRegistry.registerManualIcon("rapiericon",
//                ToolBuilder.instance.buildTool(new ItemStack(TContent.swordBlade, 1, 10), new ItemStack(TContent.toolRod, 1, 11), new ItemStack(TContent.crossbar, 1, 12), ""));
//        BMClientRegistry.registerManualIcon("daggerIcon",
//                ToolBuilder.instance.buildTool(new ItemStack(TContent.knifeBlade, 1, 10), new ItemStack(TContent.toolRod, 1, 11), new ItemStack(TContent.crossbar, 1, 12), ""));
//        BMClientRegistry.registerManualIcon("frypanicon", ToolBuilder.instance.buildTool(new ItemStack(TContent.frypanHead, 1, 10), new ItemStack(TContent.toolRod, 1, 11), null, ""));
//        BMClientRegistry.registerManualIcon("battlesignicon", ToolBuilder.instance.buildTool(new ItemStack(TContent.signHead, 1, 10), new ItemStack(TContent.toolRod, 1, 11), null, ""));
//        BMClientRegistry.registerManualIcon("chiselicon", ToolBuilder.instance.buildTool(new ItemStack(TContent.chiselHead, 1, 10), new ItemStack(TContent.toolRod, 1, 11), null, ""));
//        BMClientRegistry.registerManualIcon("shortbowIcon",
//                ToolBuilder.instance.buildTool(new ItemStack(TContent.toolRod, 1, 10), new ItemStack(TContent.bowstring, 1, 0), new ItemStack(TContent.toolRod, 1, 12), ""));
//        BMClientRegistry.registerManualIcon("arrowIcon",
//                ToolBuilder.instance.buildTool(new ItemStack(TContent.arrowhead, 1, 10), new ItemStack(TContent.toolRod, 1, 11), new ItemStack(TContent.fletching, 1, 0), ""));


//        BMClientRegistry.registerManualIcon("hammericon", ToolBuilder.instance.buildTool(new ItemStack(TContent.hammerHead, 1, 10), new ItemStack(TContent.toughRod, 1, 11), new ItemStack(
//                TContent.largePlate, 1, 12), new ItemStack(TContent.largePlate, 8), ""));
//        BMClientRegistry.registerManualIcon("lumbericon", ToolBuilder.instance.buildTool(new ItemStack(TContent.broadAxeHead, 1, 10), new ItemStack(TContent.toughRod, 1, 11), new ItemStack(
//                TContent.largePlate, 1, 12), new ItemStack(TContent.toughBinding, 8), ""));
//        BMClientRegistry.registerManualIcon("excavatoricon", ToolBuilder.instance.buildTool(new ItemStack(TContent.excavatorHead, 1, 10), new ItemStack(TContent.toughRod, 1, 11),
//                new ItemStack(TContent.largePlate, 1, 12), new ItemStack(TContent.toughBinding, 8), ""));
//        BMClientRegistry.registerManualIcon("scytheicon", ToolBuilder.instance.buildTool(new ItemStack(TContent.scytheBlade, 1, 10), new ItemStack(TContent.toughRod, 1, 11), new ItemStack(
//                TContent.toughBinding, 1, 12), new ItemStack(TContent.toughRod, 8), ""));
//        BMClientRegistry.registerManualIcon("cleavericon", ToolBuilder.instance.buildTool(new ItemStack(TContent.largeSwordBlade, 1, 10), new ItemStack(TContent.toughRod, 1, 11),
//                new ItemStack(TContent.largePlate, 1, 12), new ItemStack(TContent.toughRod, 8), ""));
//        BMClientRegistry.registerManualIcon("battleaxeicon", ToolBuilder.instance.buildTool(new ItemStack(TContent.broadAxeHead, 1, 10), new ItemStack(TContent.toughRod, 1, 11),
//                new ItemStack(TContent.broadAxeHead, 1, 12), new ItemStack(TContent.toughBinding, 8), ""));
    }

    public void initManualRecipes ()
    {
        ItemStack goldIngot = new ItemStack(Item.ingotGold);
        ItemStack ironIngot = new ItemStack(Item.ingotIron);
        ItemStack diamond = new ItemStack(Item.diamond);
        ItemStack glassBlock = new ItemStack(Block.glass);
        ItemStack smoothStone = new ItemStack(Block.stone);
        ItemStack furnace = new ItemStack(Block.furnaceIdle);
        
        ItemStack weakBloodOrb = new ItemStack(ModItems.weakBloodOrb);
        
        ItemStack blankSlate = new ItemStack(ModItems.blankSlate);


        // TConstruct recipes
        BMClientRegistry.registerManualLargeRecipe("sacrificialKnife", new ItemStack(ModItems.sacrificialDagger), glassBlock, glassBlock, glassBlock, null, ironIngot, glassBlock, goldIngot, null, glassBlock);
        BMClientRegistry.registerManualLargeRecipe("bloodAltar", new ItemStack(ModBlocks.blockAltar), smoothStone,null,smoothStone,smoothStone,furnace,smoothStone,goldIngot, diamond,goldIngot);
        BMClientRegistry.registerManualLargeRecipe("divinationSigil", new ItemStack(ModItems.divinationSigil), glassBlock,glassBlock,glassBlock,glassBlock,blankSlate,glassBlock,glassBlock,weakBloodOrb,glassBlock);


//        BMClientRegistry.registerManualLargeRecipe("slimymud", slimyMud, null, slimeball, slimeball, null, slimeball, slimeball, null, dirt, sand);
//        BMClientRegistry.registerManualFurnaceRecipe("slimecrystal", new ItemStack(TContent.materials, 1, 1), slimyMud);
//        BMClientRegistry.registerManualSmallRecipe("paperstack", new ItemStack(TContent.materials, 1, 0), paper, paper, paper, paper);
//        BMClientRegistry.registerManualLargeRecipe("mossball", new ItemStack(TContent.materials, 1, 6), mossycobble, mossycobble, mossycobble, mossycobble, mossycobble, mossycobble,
//                mossycobble, mossycobble, mossycobble);
//        BMClientRegistry.registerManualLargeRecipe("lavacrystal", new ItemStack(TContent.materials, 1, 7), blazerod, firecharge, blazerod, firecharge, new ItemStack(Item.bucketLava),
//                firecharge, blazerod, firecharge, blazerod);
//        BMClientRegistry.registerManualLargeRecipe("silkycloth", silkyCloth, string, string, string, string, new ItemStack(TContent.materials, 1, 24), string, string, string, string);
//        BMClientRegistry.registerManualLargeRecipe("silkyjewel", new ItemStack(TContent.materials, 1, 26), null, silkyCloth, null, silkyCloth, new ItemStack(Item.emerald), silkyCloth, null,
//                silkyCloth, null);
//
//
//        BMClientRegistry.registerManualSmallRecipe("graveyardsoil", graveyardsoil, new ItemStack(Block.dirt), new ItemStack(Item.rottenFlesh), new ItemStack(Item.dyePowder, 1, 15), null);
//        BMClientRegistry.registerManualFurnaceRecipe("consecratedsoil", consecratedsoil, graveyardsoil);
//
//
//        BMClientRegistry.registerManualSmallRecipe("grout", grout, sand, gravel, null, clay);
//        BMClientRegistry.registerManualFurnaceRecipe("searedbrick", searedbrick, grout);
//        BMClientRegistry.registerManualSmallRecipe("searedbricks", new ItemStack(TContent.smeltery, 1, 2), searedbrick, searedbrick, searedbrick, searedbrick);
//        BMClientRegistry.registerManualLargeRecipe("smelterycontroller", new ItemStack(TContent.smeltery, 1, 0), searedbrick, searedbrick, searedbrick, searedbrick, null, searedbrick,
//                searedbrick, searedbrick, searedbrick);
//        BMClientRegistry.registerManualLargeRecipe("dryingrack", new ItemStack(TContent.dryingRack, 1, 0), null, null, null, plankSlab, plankSlab, plankSlab, null, null,
//                null);
//        BMClientRegistry.registerManualLargeRecipe("smelterydrain", new ItemStack(TContent.smeltery, 1, 1), searedbrick, null, searedbrick, searedbrick, null, searedbrick, searedbrick, null,
//                searedbrick);
//
//
//        BMClientRegistry.registerManualLargeRecipe("smelterytank1", new ItemStack(TContent.lavaTank, 1, 0), searedbrick, searedbrick, searedbrick, searedbrick, glass, searedbrick,
//                searedbrick, searedbrick, searedbrick);
//        BMClientRegistry.registerManualLargeRecipe("smelterytank2", new ItemStack(TContent.lavaTank, 1, 1), searedbrick, glass, searedbrick, glass, glass, glass, searedbrick, glass,
//                searedbrick);
//        BMClientRegistry.registerManualLargeRecipe("smelterytank3", new ItemStack(TContent.lavaTank, 1, 2), searedbrick, glass, searedbrick, searedbrick, glass, searedbrick, searedbrick,
//                glass, searedbrick);
//
//
//        BMClientRegistry.registerManualLargeRecipe("smelterytable", new ItemStack(TContent.searedBlock, 1, 0), searedbrick, searedbrick, searedbrick, searedbrick, null, searedbrick,
//                searedbrick, null, searedbrick);
//        BMClientRegistry.registerManualLargeRecipe("smelteryfaucet", new ItemStack(TContent.searedBlock, 1, 1), searedbrick, null, searedbrick, null, searedbrick, null, null, null, null);
//        BMClientRegistry.registerManualLargeRecipe("castingchannel", new ItemStack(TContent.castingChannel), null, null, null, searedbrick, null, searedbrick, searedbrick, searedbrick,
//                searedbrick);
//        BMClientRegistry.registerManualLargeRecipe("smelterybasin", new ItemStack(TContent.searedBlock, 1, 2), searedbrick, null, searedbrick, searedbrick, null, searedbrick, searedbrick,
//                searedbrick, searedbrick);
//
//
//        //Traps
//        ItemStack reed = new ItemStack(Item.reed);
//        BMClientRegistry.registerManualLargeRecipe("punji", new ItemStack(TContent.punji), reed, null, reed, null, reed, null, reed, null, reed);
//        BMClientRegistry.registerManualSmallRecipe("barricade", new ItemStack(TContent.barricadeOak), null, log, null, log);
//
//
//        //Machines
//        ItemStack alubrassIngot = new ItemStack(TContent.materials, 1, 14);
//        ItemStack bronzeIngot = new ItemStack(TContent.materials, 1, 13);
//        ItemStack blankCast = new ItemStack(TContent.blankPattern, 1, 1);
//        ItemStack redstone = new ItemStack(Item.redstone);
//
//
//        // Modifier recipes
//        ItemStack ironpick = ToolBuilder.instance.buildTool(new ItemStack(TContent.pickaxeHead, 1, 6), new ItemStack(TContent.toolRod, 1, 2), new ItemStack(TContent.binding, 1, 6), "");
//        BMClientRegistry.registerManualIcon("ironpick", ironpick);
//        ItemStack ironlongsword = ToolBuilder.instance.buildTool(new ItemStack(TContent.swordBlade, 1, 6), new ItemStack(TContent.toolRod, 1, 2), new ItemStack(TContent.handGuard, 1, 10), "");
//        BMClientRegistry.registerManualIcon("ironlongsword", ironlongsword);
//

//        BMClientRegistry.registerManualModifier("diamondmod", ironpick.copy(), new ItemStack(Item.diamond));
//        BMClientRegistry.registerManualModifier("emeraldmod", ironpick.copy(), new ItemStack(Item.emerald));
//        BMClientRegistry.registerManualModifier("redstonemod", ironpick.copy(), new ItemStack(Item.redstone), new ItemStack(Block.blockRedstone));
//        BMClientRegistry.registerManualModifier("lavacrystalmod", ironpick.copy(), new ItemStack(TContent.materials, 1, 7));
//        BMClientRegistry.registerManualModifier("lapismod", ironpick.copy(), new ItemStack(Item.dyePowder, 1, 4), new ItemStack(Block.blockLapis));
//        BMClientRegistry.registerManualModifier("mossmod", ironpick.copy(), new ItemStack(TContent.materials, 1, 6));
//        BMClientRegistry.registerManualModifier("quartzmod", ironlongsword.copy(), new ItemStack(Item.netherQuartz), new ItemStack(Block.blockNetherQuartz));
//        BMClientRegistry.registerManualModifier("blazemod", ironlongsword.copy(), new ItemStack(Item.blazePowder));
//        BMClientRegistry.registerManualModifier("necroticmod", ironlongsword.copy(), new ItemStack(TContent.materials, 1, 8));
//        BMClientRegistry.registerManualModifier("silkymod", ironpick.copy(), new ItemStack(TContent.materials, 1, 26));
//        BMClientRegistry.registerManualModifier("reinforcedmod", ironpick.copy(), new ItemStack(TContent.largePlate, 1, 6));
//
//
//        BMClientRegistry.registerManualModifier("pistonmod", ironlongsword.copy(), new ItemStack(Block.pistonBase));
//        BMClientRegistry.registerManualModifier("beheadingmod", ironlongsword.copy(), new ItemStack(Item.enderPearl), new ItemStack(Block.obsidian));
//        BMClientRegistry.registerManualModifier("spidermod", ironlongsword.copy(), new ItemStack(Item.fermentedSpiderEye));
//        BMClientRegistry.registerManualModifier("smitemod", ironlongsword.copy(), new ItemStack(TContent.craftedSoil, 1, 4));
//
//
//        BMClientRegistry.registerManualModifier("electricmod", ironpick.copy(), new ItemStack(Block.dirt), new ItemStack(Block.dirt));
//        BMClientRegistry.registerManualModifier("fluxmod", ironpick.copy(), new ItemStack(Block.dirt));
//		BMClientRegistry.registerManualModifier("fluxmod2", ironpick.copy(), new ItemStack(Block.dirt));
//
//
//        BMClientRegistry.registerManualModifier("tier1free", ironpick.copy(), new ItemStack(Item.diamond), new ItemStack(Block.blockGold));
//        BMClientRegistry.registerManualModifier("tier1.5free", ironpick.copy(), new ItemStack(Item.appleGold, 1, 1), new ItemStack(Block.blockDiamond));
//        BMClientRegistry.registerManualModifier("tier2free", ironpick.copy(), new ItemStack(Item.netherStar));
//        BMClientRegistry.registerManualModifier("creativefree", ironpick.copy(), new ItemStack(TContent.creativeModifier));
//
//
//        BMClientRegistry.registerManualSmeltery("brownstone", new ItemStack(TContent.speedBlock), new ItemStack(TContent.moltenTin, 1), new ItemStack(Block.gravel));
//        BMClientRegistry.registerManualSmeltery("clearglass", new ItemStack(TContent.clearGlass), new ItemStack(TContent.moltenGlass, 1), null);
//        BMClientRegistry.registerManualSmeltery("searedstone", new ItemStack(TContent.smeltery, 1, 4), new ItemStack(TContent.moltenStone, 1), null);
//        BMClientRegistry.registerManualSmeltery("endstone", new ItemStack(Block.whiteStone), new ItemStack(TContent.moltenEnder, 1), new ItemStack(Block.obsidian));
//        BMClientRegistry.registerManualSmeltery("glueball", new ItemStack(TContent.materials, 1, 36), new ItemStack(TContent.glueFluidBlock, 1), null);


    }

    Document readManual (String location, DocumentBuilderFactory dbFactory)
    {
        try
        {
            InputStream stream = AlchemicalWizardry.class.getResourceAsStream(location);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    
    public static Document getManualFromStack (ItemStack stack)
    {
        switch (stack.getItemDamage())
        {
        case 0:
            return diary;
//        case 1:
//            return volume2;
//        case 2:
//            return smelter;
//        case 3:
//            return diary;
        }


        return null;
    }
}
