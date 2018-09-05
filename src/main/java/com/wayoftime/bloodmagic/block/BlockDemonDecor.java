package com.wayoftime.bloodmagic.block;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.type.DemonWillType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockDemonDecor extends BlockMundane {

    private final DemonWillType type;

    public BlockDemonDecor(Material material, String decorType, DemonWillType type) {
        super(material, decorType + "_" + type.getName());

        setTranslationKey(BloodMagic.MODID + ":" + decorType);

        this.type = type;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.bloodmagic:demon_will_" + type.getName()));
    }
}
