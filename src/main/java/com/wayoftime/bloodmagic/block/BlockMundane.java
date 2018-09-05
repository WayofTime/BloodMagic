package com.wayoftime.bloodmagic.block;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.util.register.IItemProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import javax.annotation.Nullable;

// QoL default block
public class BlockMundane extends Block implements IItemProvider {

    private final boolean hasItem;

    public BlockMundane(Material material, String name, boolean withItem) {
        super(material);

        this.hasItem = withItem;

        setTranslationKey(BloodMagic.MODID + ":" + name);
        setCreativeTab(BloodMagic.TAB_BM);
        setRegistryName(name);
        setDefaultState(getBlockState().getBaseState());
    }

    public BlockMundane(Material material, String name) {
        this(material, name, true);
    }

    @Nullable
    @Override
    public Item getItem() {
        return hasItem ? new ItemBlock(this) : null;
    }
}
