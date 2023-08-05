package wayoftime.bloodmagic.client.hud.element;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import wayoftime.bloodmagic.client.Sprite;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;
import wayoftime.bloodmagic.common.tile.TileIncenseAltar;
import wayoftime.bloodmagic.util.helper.InventoryHelper;

public abstract class ElementDivinedInformation<T extends BlockEntity> extends ElementTileInformation<T>
{

	private final boolean simple;

	public ElementDivinedInformation(int lines, boolean simple, Class<T> tileClass)
	{
		super(100, lines, tileClass);
		this.simple = simple;
	}

	public abstract void gatherInformation(Consumer<Pair<Sprite, Function<T, String>>> information);

	@Override
	public boolean shouldRender(Minecraft minecraft)
	{
		HitResult trace = Minecraft.getInstance().hitResult;
		if (trace == null || trace.getType() != HitResult.Type.BLOCK)
			return false;

		BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(((BlockHitResult) trace).getBlockPos());
		if (tile == null || !tileClass.isAssignableFrom(tile.getClass()))
			return false;

		Player player = Minecraft.getInstance().player;

		NonNullList<ItemStack> inventory = InventoryHelper.getActiveInventories(player);

		boolean hasDivination = false;
		boolean hasSeer = false;
		for (ItemStack sigilStack : inventory)
		{
			if (sigilStack.getItem() instanceof ItemSigilHolding)
			{
				List<ItemStack> internalInv = ItemSigilHolding.getInternalInventory(sigilStack);
				int currentSlot = ItemSigilHolding.getCurrentItemOrdinal(sigilStack);
				if (internalInv != null && !internalInv.get(currentSlot).isEmpty())
				{
					hasDivination = hasDivination || internalInv.get(currentSlot).getItem() == BloodMagicItems.DIVINATION_SIGIL.get();
					hasSeer = hasSeer || internalInv.get(currentSlot).getItem() == BloodMagicItems.SEER_SIGIL.get();
					continue;
				}
			}

			hasDivination = hasDivination || sigilStack.getItem() == BloodMagicItems.DIVINATION_SIGIL.get();
			hasSeer = hasSeer || sigilStack.getItem() == BloodMagicItems.SEER_SIGIL.get();

			if (hasSeer)
				break;
		}

		if (tile instanceof TileIncenseAltar)
			return hasDivination || hasSeer;

		return (simple && hasDivination && !hasSeer) || (hasSeer && !simple);
	}
}
