package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.client.render.alchemyarray.BindingAlchemyCircleRenderer;
import wayoftime.bloodmagic.common.tile.TileAlchemyArray;

public class AlchemyArrayEffectBinding extends AlchemyArrayEffectCrafting
{
	public AlchemyArrayEffectBinding(ItemStack outputStack, int tickLimit)
	{
		super(outputStack, tickLimit);
	}

	public AlchemyArrayEffectBinding(ItemStack outputStack)
	{
		this(outputStack, 300);
	}

	@Override
	public boolean update(TileAlchemyArray tile, int ticksActive)
	{
		// TODO: Add recipe rechecking to verify nothing screwy is going on.
		if (tile.getLevel().isClientSide)
		{
			return false;
		}

		if (ticksActive >= 50 && ticksActive <= 250)
		{
			this.spawnLightningOnCircle(tile.getLevel(), tile.getBlockPos(), ticksActive);
		}

		if (ticksActive >= tickLimit)
		{
			BlockPos pos = tile.getBlockPos();

			ItemStack output = outputStack.copy();

			ItemEntity outputEntity = new ItemEntity(tile.getLevel(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, output);

			tile.getLevel().addFreshEntity(outputEntity);

			return true;
		}

		return false;
	}

	public void spawnLightningOnCircle(Level world, BlockPos pos, int ticksActive)
	{
		if (ticksActive % 50 == 0)
		{
			int circle = ticksActive / 50 - 1;
			float distance = BindingAlchemyCircleRenderer.getDistanceOfCircle(circle, ticksActive);
			float angle = BindingAlchemyCircleRenderer.getAngleOfCircle(circle, ticksActive);

			double dispX = distance * Math.sin(angle);
			double dispZ = -distance * Math.cos(angle);

			LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
//			LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
			lightningboltentity.setPos(pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
			lightningboltentity.setVisualOnly(true);
			world.addFreshEntity(lightningboltentity);
		}
	}

	@Override
	public AlchemyArrayEffect getNewCopy()
	{
		return new AlchemyArrayEffectBinding(outputStack, tickLimit);
	}
}
