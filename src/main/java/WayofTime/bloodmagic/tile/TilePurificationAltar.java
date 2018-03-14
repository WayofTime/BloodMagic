package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.iface.IPurificationAsh;
import WayofTime.bloodmagic.ritual.AreaDescriptor;
import WayofTime.bloodmagic.util.helper.PurificationHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.List;

public class TilePurificationAltar extends TileInventory implements ITickable {
    public AreaDescriptor purityArea = new AreaDescriptor.Rectangle(new BlockPos(-5, -5, -5), 11);

    public double totalPurity = 0;
    public double maxPurity = 0;
    public double purityRate = 0;

    public TilePurificationAltar() {
        super(1, "purificationAltar");
    }

    @Override
    public void update() {
        if (totalPurity <= 0) {
            ItemStack stack = this.getStackInSlot(0);
            if (!stack.isEmpty() && stack.getItem() instanceof IPurificationAsh) {
                totalPurity = ((IPurificationAsh) stack.getItem()).getTotalPurity(stack);
                maxPurity = ((IPurificationAsh) stack.getItem()).getMaxPurity(stack);
                purityRate = ((IPurificationAsh) stack.getItem()).getPurityRate(stack);
            }
        } else {
            return;
        }

        AxisAlignedBB aabb = purityArea.getAABB(getPos());
        List<EntityAnimal> animalList = getWorld().getEntitiesWithinAABB(EntityAnimal.class, aabb);
        if (animalList.isEmpty()) {
            return;
        }

        boolean hasPerformed = false;

        for (EntityAnimal animal : animalList) {
            double added = PurificationHelper.addPurity(animal, Math.min(purityRate, totalPurity), maxPurity);
            if (added > 0) {
                totalPurity -= purityRate;
                hasPerformed = true;
            }
        }

        if (hasPerformed) {
            if (getWorld().rand.nextInt(4) == 0 && getWorld() instanceof WorldServer) {
                WorldServer server = (WorldServer) getWorld();
                server.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 1, 0.02, 0.03, 0.02, 0);
            }
        }
    }

    @Override
    public void deserialize(NBTTagCompound tag) {
        super.deserialize(tag);
        totalPurity = tag.getDouble("totalPurity");
        maxPurity = tag.getDouble("maxPurity");
        purityRate = tag.getDouble("purityRate");
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag) {
        super.serialize(tag);

        tag.setDouble("totalPurity", totalPurity);
        tag.setDouble("maxPurity", maxPurity);
        tag.setDouble("purityRate", purityRate);

        return tag;
    }
}
