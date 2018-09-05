package com.wayoftime.bloodmagic.core;

import com.google.common.collect.Lists;
import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.block.BlockBloodAltar;
import com.wayoftime.bloodmagic.block.BlockBloodRune;
import com.wayoftime.bloodmagic.block.BlockMundane;
import com.wayoftime.bloodmagic.client.render.TESRBloodAltar;
import com.wayoftime.bloodmagic.core.type.BloodRunes;
import com.wayoftime.bloodmagic.tile.TileBloodAltar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

@GameRegistry.ObjectHolder(BloodMagic.MODID)
@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class RegistrarBloodMagicBlocks {

    public static final Block LIFE_ESSENCE = Blocks.AIR;
    public static final Block BLOOD_ALTAR = Blocks.AIR;
    public static final Block BLOODSTONE_BRICK= Blocks.AIR;
    public static final Block BLOODSTONE_TILE = Blocks.AIR;

    public static final Block BLOOD_RUNE_BLANK = Blocks.AIR;
    public static final Block BLOOD_RUNE_SPEED = Blocks.AIR;
    public static final Block BLOOD_RUNE_EFFICIENCY = Blocks.AIR;
    public static final Block BLOOD_RUNE_SACRIFICE = Blocks.AIR;
    public static final Block BLOOD_RUNE_SELF_SACRIFICE = Blocks.AIR;
    public static final Block BLOOD_RUNE_DISPLACEMENT = Blocks.AIR;
    public static final Block BLOOD_RUNE_CAPACITY = Blocks.AIR;
    public static final Block BLOOD_RUNE_AUGMENTED_CAPACITY = Blocks.AIR;
    public static final Block BLOOD_RUNE_ORB = Blocks.AIR;
    public static final Block BLOOD_RUNE_ACCELERATION = Blocks.AIR;
    public static final Block BLOOD_RUNE_CHARGING = Blocks.AIR;

    static List<Block> blocks;

    public static void register(IForgeRegistry<Block> registry) {
        GameRegistry.registerTileEntity(TileBloodAltar.class, new ResourceLocation(BloodMagic.MODID, "blood_altar"));
        FluidRegistry.addBucketForFluid(RegistrarBloodMagic.FLUID_LIFE_ESSENCE);

        blocks = Lists.newArrayList(
                new BlockFluidClassic(RegistrarBloodMagic.FLUID_LIFE_ESSENCE, Material.WATER).setTranslationKey(BloodMagic.MODID + ".life_essence").setRegistryName("life_essence"),
                new BlockBloodAltar().setRegistryName("blood_altar"),
                new BlockMundane(Material.ROCK, "bloodstone_brick"),
                new BlockMundane(Material.ROCK, "bloodstone_tile")
        );

        for (BloodRunes rune : BloodRunes.values())
            blocks.add(new BlockBloodRune(rune));

        // TODO - Re-enable whenever I feel like it
//        for (DemonWillType type : DemonWillType.VALUES) {
//            blocks.add(new BlockDemonDecor(Material.ROCK, "demon_stone", type));
//            blocks.add(new BlockDemonDecor(Material.ROCK, "demon_stone_polished", type));
//            Block brickBlock;
//            blocks.add(brickBlock = new BlockDemonDecor(Material.ROCK, "demon_brick", type));
//            blocks.add(new BlockDemonDecor(Material.ROCK, "demon_brick_small", type));
//            blocks.add(new BlockDemonDecor(Material.ROCK, "demon_tile", type));
//            blocks.add(new BlockDemonDecor(Material.ROCK, "demon_tile_special", type));
//            blocks.add(new BlockDemonDecor(Material.IRON, "demon_metal", type));
//            blocks.add(new BlockStairsExtended(brickBlock.getDefaultState()) {
//                @SideOnly(Side.CLIENT)
//                @Override
//                public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
//                    tooltip.add(I18n.format("tooltip.bloodmagic:demon_will_" + type.getName()));
//                }
//            }.setTranslationKey(BloodMagic.MODID + ":demon_stairs"));
//        }

        registry.registerAll(blocks.toArray(new Block[0]));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileBloodAltar.class, new TESRBloodAltar());

        ModelLoader.setCustomStateMapper(LIFE_ESSENCE, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(state.getBlock().getRegistryName(), "fluid");
            }
        });

        for (Block block : blocks) {
            Item item = Item.getItemFromBlock(block);
            if (item == Items.AIR)
                continue;

            boolean flag = RegistrarBloodMagicItems.handleModel(item);


            if (!flag) // If we haven't registered a model by now, we don't need any special handling so we'll just use the default model.
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "normal"));
        }
    }
}
