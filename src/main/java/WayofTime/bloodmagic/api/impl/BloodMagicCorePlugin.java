package WayofTime.bloodmagic.api.impl;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicPlugin;
import WayofTime.bloodmagic.api.IBloodMagicAPI;
import WayofTime.bloodmagic.api.IBloodMagicPlugin;
import WayofTime.bloodmagic.altar.ComponentType;
import WayofTime.bloodmagic.api.IBloodMagicRecipeRegistrar;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.block.BlockDecorative;
import WayofTime.bloodmagic.block.enums.BloodRuneType;
import WayofTime.bloodmagic.block.enums.EnumDecorative;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicRecipes;
import WayofTime.bloodmagic.incense.EnumTranquilityType;
import WayofTime.bloodmagic.incense.TranquilityStack;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@BloodMagicPlugin
public class BloodMagicCorePlugin implements IBloodMagicPlugin {

    @Override
    public void register(IBloodMagicAPI apiInterface) {
        BloodMagicAPI api = (BloodMagicAPI) apiInterface;
        // Add forced blacklistings
        api.getBlacklist().addTeleposer(RegistrarBloodMagicBlocks.INPUT_ROUTING_NODE);
        api.getBlacklist().addTransposition(RegistrarBloodMagicBlocks.INPUT_ROUTING_NODE);
        api.getBlacklist().addTeleposer(RegistrarBloodMagicBlocks.OUTPUT_ROUTING_NODE);
        api.getBlacklist().addTransposition(RegistrarBloodMagicBlocks.OUTPUT_ROUTING_NODE);
        api.getBlacklist().addTeleposer(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE);
        api.getBlacklist().addTransposition(RegistrarBloodMagicBlocks.ITEM_ROUTING_NODE);
        api.getBlacklist().addTeleposer(RegistrarBloodMagicBlocks.MASTER_ROUTING_NODE);
        api.getBlacklist().addTransposition(RegistrarBloodMagicBlocks.MASTER_ROUTING_NODE);
        api.getBlacklist().addTeleposer(RegistrarBloodMagicBlocks.DEMON_CRYSTAL);
        api.getBlacklist().addTransposition(RegistrarBloodMagicBlocks.DEMON_CRYSTAL);
        api.getBlacklist().addTeleposer(RegistrarBloodMagicBlocks.INVERSION_PILLAR);
        api.getBlacklist().addTransposition(RegistrarBloodMagicBlocks.INVERSION_PILLAR);
        api.getBlacklist().addWellOfSuffering(new ResourceLocation("armor_stand"));
        api.getBlacklist().addWellOfSuffering(new ResourceLocation(BloodMagic.MODID, "sentient_specter"));

        api.getValueManager().setSacrificialValue(new ResourceLocation("armor_stand"), 0);
        api.getValueManager().setSacrificialValue(new ResourceLocation(BloodMagic.MODID, "sentient_specter"), 0);

        api.getValueManager().setTranquility(Blocks.LAVA, new TranquilityStack(EnumTranquilityType.LAVA, 1.2D));
        api.getValueManager().setTranquility(Blocks.FLOWING_LAVA, new TranquilityStack(EnumTranquilityType.LAVA, 1.2D));
        api.getValueManager().setTranquility(Blocks.WATER, new TranquilityStack(EnumTranquilityType.WATER, 1.0D));
        api.getValueManager().setTranquility(Blocks.FLOWING_WATER, new TranquilityStack(EnumTranquilityType.WATER, 1.0D));
        api.getValueManager().setTranquility(RegistrarBloodMagicBlocks.LIFE_ESSENCE, new TranquilityStack(EnumTranquilityType.WATER, 1.5D));
        api.getValueManager().setTranquility(Blocks.NETHERRACK, new TranquilityStack(EnumTranquilityType.FIRE, 0.5D));
        api.getValueManager().setTranquility(Blocks.DIRT, new TranquilityStack(EnumTranquilityType.EARTHEN, 0.25D));
        api.getValueManager().setTranquility(Blocks.FARMLAND, new TranquilityStack(EnumTranquilityType.EARTHEN, 1.0D));
        api.getValueManager().setTranquility(Blocks.POTATOES, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
        api.getValueManager().setTranquility(Blocks.CARROTS, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
        api.getValueManager().setTranquility(Blocks.WHEAT, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
        api.getValueManager().setTranquility(Blocks.NETHER_WART, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));
        api.getValueManager().setTranquility(Blocks.BEETROOTS, new TranquilityStack(EnumTranquilityType.CROP, 1.0D));

        handleConfigValues(api);

        // Add standard blocks for altar components
        api.registerAltarComponent(Blocks.GLOWSTONE.getDefaultState(), ComponentType.GLOWSTONE.name());
        api.registerAltarComponent(Blocks.SEA_LANTERN.getDefaultState(), ComponentType.GLOWSTONE.name());
        api.registerAltarComponent(Blocks.BEACON.getDefaultState(), ComponentType.BEACON.name());

        BlockDecorative decorative = (BlockDecorative) RegistrarBloodMagicBlocks.DECORATIVE_BRICK;
        api.registerAltarComponent(decorative.getDefaultState().withProperty(decorative.getProperty(), EnumDecorative.BLOODSTONE_BRICK), ComponentType.BLOODSTONE.name());
        api.registerAltarComponent(decorative.getDefaultState().withProperty(decorative.getProperty(), EnumDecorative.BLOODSTONE_TILE), ComponentType.BLOODSTONE.name());
        api.registerAltarComponent(decorative.getDefaultState().withProperty(decorative.getProperty(), EnumDecorative.CRYSTAL_BRICK), ComponentType.CRYSTAL.name());
        api.registerAltarComponent(decorative.getDefaultState().withProperty(decorative.getProperty(), EnumDecorative.CRYSTAL_TILE), ComponentType.CRYSTAL.name());

        BlockBloodRune bloodRune = (BlockBloodRune) RegistrarBloodMagicBlocks.BLOOD_RUNE;
        for (BloodRuneType runeType : BloodRuneType.values())
            api.registerAltarComponent(bloodRune.getDefaultState().withProperty(bloodRune.getProperty(), runeType), ComponentType.BLOODRUNE.name());
    }

    @Override
    public void registerRecipes(IBloodMagicRecipeRegistrar recipeRegistrar) {
        RegistrarBloodMagicRecipes.registerAltarRecipes((BloodMagicRecipeRegistrar) recipeRegistrar);
        RegistrarBloodMagicRecipes.registerAlchemyTableRecipes((BloodMagicRecipeRegistrar) recipeRegistrar);
        RegistrarBloodMagicRecipes.registerTartaricForgeRecipes((BloodMagicRecipeRegistrar) recipeRegistrar);
        RegistrarBloodMagicRecipes.registerAlchemyArrayRecipes((BloodMagicRecipeRegistrar) recipeRegistrar);
    }

    private static void handleConfigValues(BloodMagicAPI api) {
        for (String value : ConfigHandler.values.sacrificialValues) {
            String[] split = value.split(";");
            if (split.length != 2) // Not valid format
                continue;

            api.getValueManager().setSacrificialValue(new ResourceLocation(split[0]), Integer.parseInt(split[1]));
        }

        for (String value : ConfigHandler.blacklist.teleposer) {
            EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(value));
            if (entityEntry == null) { // It's not an entity (or at least not a valid one), so let's try a block.
                String[] blockData = value.split("\\[");
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockData[0]));
                if (block == Blocks.AIR || block == null) // Not a valid block either
                    continue;

                if (blockData.length > 1) { // We have properties listed, so let's build a state.
                    api.getBlacklist().addTeleposer(parseState(value));
                    continue;
                }

                api.getBlacklist().addTeleposer(block);
                continue;
            }

            api.getBlacklist().addTeleposer(entityEntry.getRegistryName());
        }

        for (String value : ConfigHandler.blacklist.transposer) {
            String[] blockData = value.split("\\[");
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockData[0]));
            if (block == Blocks.AIR || block == null) // Not a valid block
                continue;

            if (blockData.length > 1) { // We have properties listed, so let's build a state.
                api.getBlacklist().addTeleposer(parseState(value));
                continue;
            }

            api.getBlacklist().addTeleposer(block);
        }

        for (String value : ConfigHandler.blacklist.wellOfSuffering) {
            EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(value));
            if (entityEntry == null) // Not a valid entity
                continue;

            api.getBlacklist().addWellOfSuffering(entityEntry.getRegistryName());
        }
    }

    private static IBlockState parseState(String blockInfo) {
        String[] split = blockInfo.split("\\[");
        split[1] = split[1].substring(0, split[1].lastIndexOf("]")); // Make sure brackets are removed from state

        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(split[0])); // Find the block
        if (block == Blocks.AIR)
            return Blocks.AIR.getDefaultState(); // The block is air, so we're looking at invalid data

        BlockStateContainer blockState = block.getBlockState();
        IBlockState returnState = blockState.getBaseState();

        // Force our values into the state
        String[] stateValues = split[1].split(","); // Splits up each value
        for (String value : stateValues) {
            String[] valueSplit = value.split("="); // Separates property and value
            IProperty property = blockState.getProperty(valueSplit[0]);
            if (property != null)
                returnState = returnState.withProperty(property, (Comparable) property.parseValue(valueSplit[1]).get()); // Force the property into the state
        }

        return returnState;
    }
}
