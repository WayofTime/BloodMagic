package wayoftime.bloodmagic.common.alchemyarray;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.client.render.alchemyarray.BindingAlchemyCircleRenderer;
import wayoftime.bloodmagic.tile.TileAlchemyArray;

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
		if (tile.getWorld().isRemote)
		{
			return false;
		}

		if (ticksActive >= 50 && ticksActive <= 250)
		{
			this.spawnLightningOnCircle(tile.getWorld(), tile.getPos(), ticksActive);
		}

		if (ticksActive >= tickLimit)
		{
			BlockPos pos = tile.getPos();

			ItemStack output = outputStack.copy();

			ItemEntity outputEntity = new ItemEntity(tile.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, output);

			tile.getWorld().addEntity(outputEntity);

			return true;
		}

		return false;
	}

	public void spawnLightningOnCircle(World world, BlockPos pos, int ticksActive)
	{
		if (ticksActive % 50 == 0)
		{
			int circle = ticksActive / 50 - 1;
			float distance = BindingAlchemyCircleRenderer.getDistanceOfCircle(circle, ticksActive);
			float angle = BindingAlchemyCircleRenderer.getAngleOfCircle(circle, ticksActive);

			double dispX = distance * Math.sin(angle);
			double dispZ = -distance * Math.cos(angle);

			LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
//			LightningBoltEntity lightning = new LightningBoltEntity(world, pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
			lightningboltentity.setPosition(pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
			lightningboltentity.setEffectOnly(true);
			world.addEntity(lightningboltentity);
		}
	}

	@Override
	public AlchemyArrayEffect getNewCopy()
	{
		return new AlchemyArrayEffectBinding(outputStack, tickLimit);
	}
}
