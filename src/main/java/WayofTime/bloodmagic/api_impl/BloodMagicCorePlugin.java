package WayofTime.bloodmagic.api_impl;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.apiv2.BloodMagicPlugin;
import WayofTime.bloodmagic.apiv2.IBloodMagicAPI;
import WayofTime.bloodmagic.apiv2.IBloodMagicPlugin;
import WayofTime.bloodmagic.block.BlockBloodRune;
import WayofTime.bloodmagic.block.BlockDecorative;
import WayofTime.bloodmagic.block.enums.EnumBloodRune;
import WayofTime.bloodmagic.block.enums.EnumDecorative;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.RegistrarBloodMagicRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

@BloodMagicPlugin
public class BloodMagicCorePlugin implements IBloodMagicPlugin {

    @Override
    public void register(IBloodMagicAPI api) {
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
        api.getBlacklist().addSacrifice(new ResourceLocation("armor_stand"));
        api.getBlacklist().addSacrifice(new ResourceLocation(BloodMagic.MODID, "sentient_specter"));

        // TODO - Register things from config

        // Add standard blocks for altar components
        api.registerAltarComponent(Blocks.GLOWSTONE.getDefaultState(), EnumAltarComponent.GLOWSTONE.name());
        api.registerAltarComponent(Blocks.SEA_LANTERN.getDefaultState(), EnumAltarComponent.GLOWSTONE.name());
        api.registerAltarComponent(Blocks.BEACON.getDefaultState(), EnumAltarComponent.BEACON.name());

        BlockDecorative decorative = (BlockDecorative) RegistrarBloodMagicBlocks.DECORATIVE_BRICK;
        api.registerAltarComponent(decorative.getDefaultState().withProperty(decorative.getProperty(), EnumDecorative.BLOODSTONE_BRICK), EnumAltarComponent.BLOODSTONE.name());
        api.registerAltarComponent(decorative.getDefaultState().withProperty(decorative.getProperty(), EnumDecorative.BLOODSTONE_TILE), EnumAltarComponent.BLOODSTONE.name());
        api.registerAltarComponent(decorative.getDefaultState().withProperty(decorative.getProperty(), EnumDecorative.CRYSTAL_BRICK), EnumAltarComponent.CRYSTAL.name());
        api.registerAltarComponent(decorative.getDefaultState().withProperty(decorative.getProperty(), EnumDecorative.CRYSTAL_TILE), EnumAltarComponent.CRYSTAL.name());

        BlockBloodRune bloodRune = (BlockBloodRune) RegistrarBloodMagicBlocks.BLOOD_RUNE;
        for (EnumBloodRune runeType : EnumBloodRune.values())
            api.registerAltarComponent(bloodRune.getDefaultState().withProperty(bloodRune.getProperty(), runeType), EnumAltarComponent.BLOODRUNE.name());

        RegistrarBloodMagicRecipes.registerAltarRecipes(api.getRecipeRegistrar());
        RegistrarBloodMagicRecipes.registerAlchemyTableRecipes(api.getRecipeRegistrar());
        RegistrarBloodMagicRecipes.registerTartaricForgeRecipes(((BloodMagicAPI) api).getRecipeRegistrar());
    }
}
