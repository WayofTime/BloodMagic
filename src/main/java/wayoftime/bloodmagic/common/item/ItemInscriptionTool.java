package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BlockRitualStone;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class ItemInscriptionTool extends Item
{
	private final EnumRuneType type;

	public ItemInscriptionTool(EnumRuneType type)
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB).maxDamage(40));

		this.type = type;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		ItemStack stack = context.getItem();
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof BlockRitualStone
				&& !((BlockRitualStone) state.getBlock()).isRuneType(world, pos, type))
		{
			((BlockRitualStone) state.getBlock()).setRuneType(world, pos, type);
			if (!player.isCreative())
			{
				stack.damageItem(1, player, (entity) -> {
					entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
				});
			}
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.FAIL;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent(TextHelper.localizeEffect("tooltip.bloodmagic.inscriber.desc")).mergeStyle(TextFormatting.GRAY));
	}
}
