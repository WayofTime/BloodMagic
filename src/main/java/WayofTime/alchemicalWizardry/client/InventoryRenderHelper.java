package WayofTime.alchemicalWizardry.client;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.items.ItemAlchemyBase;
import WayofTime.alchemicalWizardry.common.items.ItemComponents;
import WayofTime.alchemicalWizardry.common.items.ItemIncense;

public class InventoryRenderHelper {

    private static final String resourceBase = "alchemicalwizardry:";
    
    /**
     * Registers a Model for the given Item and meta.
     *
     * @param item - Item to register Model for
     * @param meta - Meta of Item
     * @param name - Name of the model JSON
     */
    public static void itemRender(Item item, int meta, String name) {
        if (item instanceof ItemBlock && name.startsWith("ItemBlock"))
            name = name.replace("Item", "");

        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        String resName = resourceBase + name;

        ModelBakery.addVariantName(item, resName);
        renderItem.getItemModelMesher().register(item, meta, new ModelResourceLocation(resName, "inventory"));
    }
    
    public static void registerItems()
    {
    	registerSimpleItem(ModItems.weakBloodOrb);
    	registerSimpleItem(ModItems.apprenticeBloodOrb);
    	registerSimpleItem(ModItems.magicianBloodOrb);
    	registerSimpleItem(ModItems.masterBloodOrb);
    	registerSimpleItem(ModItems.archmageBloodOrb);
    	registerSimpleItem(ModItems.transcendentBloodOrb);
    	registerToggleItem(ModItems.energyBlaster);
    	registerToggleItem(ModItems.energySword);
    	registerSimpleItem(ModItems.lavaCrystal);
    	registerSimpleItem(ModItems.waterSigil);
    	registerSimpleItem(ModItems.lavaSigil);
    	registerSimpleItem(ModItems.voidSigil);
    	registerSimpleItem(ModItems.blankSlate);
    	registerSimpleItem(ModItems.reinforcedSlate);
    	registerSimpleItem(ModItems.sacrificialDagger);
    	registerSimpleItem(ModItems.daggerOfSacrifice);
    	registerSimpleItem(ModItems.airSigil);
    	registerToggleItem(ModItems.sigilOfTheFastMiner);
    	registerToggleItem(ModItems.sigilOfElementalAffinity);
    	registerToggleItem(ModItems.sigilOfHaste);
    	registerToggleItem(ModItems.boundPickaxe);
    	registerToggleItem(ModItems.boundAxe);
    	registerToggleItem(ModItems.boundShovel);
    	registerToggleItem(ModItems.growthSigil);
    	registerToggleItem(ModItems.sigilOfWind);
    	registerToggleItem(ModItems.sigilOfTheBridge);
    	registerToggleItem(ModItems.sigilOfMagnetism);
    	registerToggleItem(ModItems.energyBazooka);
    	registerToggleItem(ModItems.itemHarvestSigil);
    	registerToggleItem(ModItems.itemCompressionSigil);
    	registerSimpleItem(ModItems.divinationSigil);
    	registerSimpleItem(ModItems.waterScribeTool);
    	registerSimpleItem(ModItems.fireScribeTool);
    	registerSimpleItem(ModItems.earthScribeTool);
    	registerSimpleItem(ModItems.airScribeTool);
    	registerSimpleItem(ModItems.activationCrystal); 	//TODO
    	registerSimpleItem(ModItems.boundHelmet);
    	registerSimpleItem(ModItems.boundChestplate);
    	registerSimpleItem(ModItems.boundLeggings);
    	registerSimpleItem(ModItems.boundBoots);
    	registerSimpleItem(ModItems.weakBloodShard);
    	registerSimpleItem(ModItems.blankSpell);
    	registerSimpleItem(ModItems.alchemyFlask);
    	registerSimpleItem(ModItems.standardBindingAgent);
    	registerSimpleItem(ModItems.mundanePowerCatalyst);
    	registerSimpleItem(ModItems.averagePowerCatalyst);
    	registerSimpleItem(ModItems.greaterPowerCatalyst);
    	registerSimpleItem(ModItems.mundaneLengtheningCatalyst);
    	registerSimpleItem(ModItems.averageLengtheningCatalyst);
    	registerSimpleItem(ModItems.greaterLengtheningCatalyst);
    	registerSimpleItem(ModItems.incendium);
    	registerSimpleItem(ModItems.magicales);
    	registerSimpleItem(ModItems.sanctus);
    	registerSimpleItem(ModItems.aether);
    	registerSimpleItem(ModItems.simpleCatalyst);
    	registerSimpleItem(ModItems.crepitous);
    	registerSimpleItem(ModItems.crystallos);
    	registerSimpleItem(ModItems.terrae);
    	registerSimpleItem(ModItems.aquasalus);
    	registerSimpleItem(ModItems.tennebrae);
    	registerSimpleItem(ModItems.demonBloodShard);
    	registerSimpleItem(ModItems.telepositionFocus);
    	registerSimpleItem(ModItems.enhancedTelepositionFocus);
    	registerSimpleItem(ModItems.reinforcedTelepositionFocus);
    	registerSimpleItem(ModItems.demonicTelepositionFocus);
    	registerSimpleItem(ModItems.imbuedSlate);
    	registerSimpleItem(ModItems.demonicSlate);
    	registerSimpleItem(ModItems.duskScribeTool);
    	registerToggleItem(ModItems.armourInhibitor);
    	registerSimpleItem(ModItems.creativeFiller);
    	registerSimpleItem(ModItems.demonPlacer);
    	registerSimpleItem(ModItems.creativeDagger);
    	registerSimpleItem(ModItems.weakFillingAgent);
    	registerSimpleItem(ModItems.standardFillingAgent);
    	registerSimpleItem(ModItems.enhancedFillingAgent);
    	registerSimpleItem(ModItems.weakBindingAgent);
    	registerSimpleItem(ModItems.itemRitualDiviner); //TODO Make it work for all 3 metas
    	registerSimpleItem(ModItems.itemKeyOfDiablo);
    	registerSimpleItem(ModItems.itemBloodLightSigil);
    	registerSimpleItem(ModItems.itemComplexSpellCrystal);
    	registerSimpleItem(ModItems.bucketLife);
    	registerToggleItem(ModItems.itemSigilOfEnderSeverance);
    	registerSimpleItem(ModItems.itemSeerSigil);
    	registerSimpleItem(ModItems.customTool);
    	
    	int i = 0;
    	for(String name : ItemComponents.ITEM_NAMES)
    	{
    		String funName = ModItems.baseItems.getUnlocalizedName().substring(5) + "_" + name;
    		itemRender(ModItems.baseItems, i, funName);
//    		try {
//        		PrintWriter writer = new PrintWriter(funName + ".json");
//    			writer.println("{");
//    			writer.println("    " + '"' + "parent" + '"' + ":" + '"' + "alchemicalwizardry:item/standard_item" + '"' + ",");
//    			writer.println("    "+ '"' + "textures" + '"' + ": {");
//    			writer.println("        " + '"' + "layer0" + '"' + ":" + '"' + "alchemicalwizardry:items/" + "baseItem" + "_" + name + '"');
//    			writer.println("    }");
//    			writer.println("}");
//    			writer.close();
//    		} catch (FileNotFoundException e1) {
//    			// TODO Auto-generated catch block
//    			e1.printStackTrace();
//    		}
    		i++;
    	}
    	
    	i = 0;
    	for(String name : ItemAlchemyBase.ITEM_NAMES)
    	{
    		String funName = ModItems.baseAlchemyItems.getUnlocalizedName().substring(5) + "_" + name;
    		itemRender(ModItems.baseAlchemyItems, i, funName);
    		
    		i++;
    	}
    	
    	i = 0;
    	for(String name : ItemIncense.ITEM_NAMES)
    	{
    		String funName = ModItems.itemIncense.getUnlocalizedName().substring(5) + "_" + name;
    		itemRender(ModItems.itemIncense, i, funName);
    		
    		i++;
    	}
    	
    	registerSimpleItem(ModItems.itemCombinationalCatalyst);
    	registerSimpleItem(ModItems.itemAttunedCrystal);
    	registerSimpleItem(ModItems.itemTankSegmenter);
    	registerSimpleItem(ModItems.itemDestinationClearer);
    	registerSimpleItem(ModItems.dawnScribeTool);
    	registerSimpleItem(ModItems.itemBloodPack);
    	registerSimpleItem(ModItems.boundHelmetWater);
    	registerSimpleItem(ModItems.boundChestplateWater);
    	registerSimpleItem(ModItems.boundLeggingsWater);
    	registerSimpleItem(ModItems.boundBootsWater);
    	registerSimpleItem(ModItems.boundHelmetEarth);
    	registerSimpleItem(ModItems.boundChestplateEarth);
    	registerSimpleItem(ModItems.boundLeggingsEarth);
    	registerSimpleItem(ModItems.boundBootsEarth);
    	registerSimpleItem(ModItems.boundHelmetWind);
    	registerSimpleItem(ModItems.boundChestplateWind);
    	registerSimpleItem(ModItems.boundLeggingsWind);
    	registerSimpleItem(ModItems.boundBootsWind);
    	registerSimpleItem(ModItems.boundHelmetFire);
    	registerSimpleItem(ModItems.boundChestplateFire);
    	registerSimpleItem(ModItems.boundLeggingsFire);
    	registerSimpleItem(ModItems.boundBootsFire);
    	registerSimpleItem(ModItems.inputRoutingFocus);
    	registerSimpleItem(ModItems.outputRoutingFocus);
    	registerSimpleItem(ModItems.ritualDismantler);
    }
    
    public static void registerSimpleItem(Item item)
    {
    	itemRender(item, 0, item.getUnlocalizedName().substring(5));
    }
    
    public static void registerToggleItem(Item item)
    {
    	itemRender(item, 0, item.getUnlocalizedName().substring(5) + "_deactivated");
    	itemRender(item, 1, item.getUnlocalizedName().substring(5) + "_activated");
    }

    /**
     * Shorthand of {@code itemRender(Item, int, String)}
     *
     * @param item - Item to register Model for
     * @param meta - Meta of Item
     */
    public static void itemRender(Item item, int meta) {
        itemRender(item, meta, item.getClass().getSimpleName() + meta);
    }

    /**
     * Shorthand of {@code itemRender(Item, int)}
     *
     * @param item - Item to register Model for
     */
    public static void itemRender(Item item) {
        itemRender(item, 0, item.getClass().getSimpleName());
    }

    /**
     * Registers a model for the item across all Meta's that get used for the item
     *
     * @param item - Item to register Model for
     */
    public static void itemRenderAll(Item item) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        final Item toRender = item;

        renderItem.getItemModelMesher().register(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(resourceBase + toRender.getClass().getSimpleName(), "inventory");
            }
        });
    }

    /**
     *
     * @param block - Block to get Item of
     * @return      - The ItemBlock that corresponds to the Block.
     */
    public static Item getItemFromBlock(Block block) {
        return Item.getItemFromBlock(block);
    }
}
