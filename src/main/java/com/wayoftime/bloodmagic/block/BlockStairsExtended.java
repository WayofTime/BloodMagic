package com.wayoftime.bloodmagic.block;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.util.register.IItemProvider;
import com.wayoftime.bloodmagic.core.util.register.IVariantProvider;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import javax.annotation.Nullable;

// Because Mojang doesn't want us making our own stairs apparently
public class BlockStairsExtended extends BlockStairs implements IItemProvider, IVariantProvider {

    public BlockStairsExtended(IBlockState modelState) {
        super(modelState);

        String name = modelState.getBlock().getRegistryName().getPath() + "_stairs";
        setRegistryName(name);
        setTranslationKey(BloodMagic.MODID + ":" + name);
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Nullable
    @Override
    public Item getItem() {
        return new ItemBlock(this);
    }

    @Override
    public void collectVariants(Int2ObjectMap<String> variants) {
        variants.put(0, "facing=south,half=bottom,shape=straight");
    }
}
