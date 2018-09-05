package com.wayoftime.bloodmagic.item;

import com.wayoftime.bloodmagic.BloodMagic;
import com.wayoftime.bloodmagic.core.RegistrarBloodMagic;
import com.wayoftime.bloodmagic.core.altar.AltarUtil;
import com.wayoftime.bloodmagic.core.network.SoulNetwork;
import com.wayoftime.bloodmagic.core.util.BooleanResult;
import com.wayoftime.bloodmagic.tile.TileBloodAltar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class ItemDaggerSelfSacrifice extends ItemMundane {

    private final Type type;

    public ItemDaggerSelfSacrifice(Type sacrifice) {
        super("dagger_self_sacrifice" + (sacrifice == Type.CREATIVE ? "_creative" : ""));

        setMaxStackSize(1);

        this.type = sacrifice;

        if (sacrifice != Type.CREATIVE)
            addPropertyOverride(new ResourceLocation(BloodMagic.MODID, "charged"), (stack, worldIn, entityIn) -> stack.hasTagCompound() && stack.getTagCompound().hasKey("charge") ? 1.0F : 0.0F);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.attackEntityFrom(SoulNetwork.WEAK_SOUL, 2.0F);
        BooleanResult<Integer> fillResult = AltarUtil.handleSacrifice(player, type.amount);
        if (!fillResult.isSuccess())
            return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));

        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    public enum Type {
        NORMAL(200),
        CREATIVE(Integer.MAX_VALUE),
        ;

        private final int amount;

        Type(int amount) {
            this.amount = amount;
        }
    }
}
