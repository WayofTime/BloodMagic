package com.wayoftime.bloodmagic.guide.test;

import com.google.common.collect.Lists;
import com.wayoftime.bloodmagic.guide.Entry;
import com.wayoftime.bloodmagic.guide.Guide;
import com.wayoftime.bloodmagic.guide.page.PageComponentFiller;
import com.wayoftime.bloodmagic.guide.page.PageComponentItem;
import com.wayoftime.bloodmagic.guide.page.PageComponentText;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod(modid = "test_guide")
@Mod.EventBusSubscriber(modid = "test_guide")
public class GuideTest {

    public static final List<Guide> GUIDES = Lists.newArrayList();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GUIDES.add(new Guide(new ResourceLocation("guide_test", "test"), guide -> {
            guide.addCategory("test_cat_1", category -> {
                category.addEntry(new Entry("test_entry_1", test1 -> {
                    test1.appendComponent(new PageComponentText(new TextComponentString("doot")));
                }));
            });

            guide.addCategory("test_cat_2", category -> {
                category.addEntry(new Entry("test_entry_2", test2 -> {
                    test2.appendComponent(new PageComponentText(new TextComponentString("what's up bud")));
                    test2.appendComponent(new PageComponentItem(new ItemStack(Items.DIAMOND)));
                    test2.appendComponent(new PageComponentFiller());
                }));

                category.addEntry(new Entry("test_entry_3", test3 -> {
                    test3.appendComponent(new PageComponentFiller());
                    test3.appendComponent(new PageComponentFiller());
                }));
            });
        }));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Guide guide : GUIDES)
            event.getRegistry().register(new ItemGuide(guide));
    }
}
