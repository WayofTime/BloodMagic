package WayofTime.bloodmagic.alchemyArray;

import lombok.Getter;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.client.render.alchemyArray.BindingAlchemyCircleRenderer;

public class AlchemyArrayEffectBinding extends AlchemyArrayEffect
{
    @Getter
    public final ItemStack outputStack;

    public AlchemyArrayEffectBinding(ItemStack outputStack)
    {
        this.outputStack = outputStack;
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive)
    {
        if (ticksActive >= 50 && ticksActive <= 250)
        {
            // TODO: Find a way to spawn lightning from only the server side -
            // does not render when just spawned on server, not client.
            this.spawnLightningOnCircle(tile.getWorld(), tile.getPos(), ticksActive);
        }

        if (tile.getWorld().isRemote)
        {
            return false;
        }

        if (ticksActive >= 300)
        {
            BlockPos pos = tile.getPos();

            ItemStack output = outputStack.copy();
            EntityItem outputEntity = new EntityItem(tile.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, output);

            tile.getWorld().spawnEntityInWorld(outputEntity);

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

            EntityLightningBolt lightning = new EntityLightningBolt(world, pos.getX() + dispX, pos.getY(), pos.getZ() + dispZ);
            world.spawnEntityInWorld(lightning);
        }
    }
}
