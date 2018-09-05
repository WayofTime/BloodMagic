package com.wayoftime.bloodmagic.core;

import com.google.common.base.Stopwatch;
import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.util.BMLog;
import com.wayoftime.bloodmagic.core.util.PluginUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class RegistrarBloodMagic {

    public static final Fluid FLUID_LIFE_ESSENCE = new Fluid(BloodMagic.MODID + ":life_essence", new ResourceLocation(BloodMagic.MODID, "blocks/life_essence_flowing"), new ResourceLocation(BloodMagic.MODID, "blocks/life_essence_still"), 0x8C150C);

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        RegistrarBloodMagicBlocks.register(event.getRegistry());
        BMLog.DEBUG.info("Registered blocks in {}.", stopwatch.stop());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        RegistrarBloodMagicItems.register(event.getRegistry());
        BMLog.DEBUG.info("Registered items in {}.", stopwatch.stop());
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent<IRecipe> event) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        PluginUtil.handlePluginStep(PluginUtil.RegistrationStep.RECIPE_REGISTER);
        BMLog.DEBUG.info("Registered recipes in {}.", stopwatch.stop());
    }
}
