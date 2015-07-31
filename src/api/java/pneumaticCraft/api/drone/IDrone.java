package pneumaticCraft.api.drone;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidTank;
import pneumaticCraft.api.item.IPressurizable;

public interface IDrone extends IPressurizable{
    /**
     * 
     * @param upgradeIndex metadata value of the upgrade item
     * @return amount of inserted upgrades in the drone
     */
    public int getUpgrades(int upgradeIndex);

    public World getWorld();

    public IFluidTank getTank();

    public IInventory getInventory();

    public Vec3 getPosition();

    public IPathNavigator getPathNavigator();

    public void sendWireframeToClient(int x, int y, int z);

    public EntityPlayerMP getFakePlayer();

    public boolean isBlockValidPathfindBlock(int x, int y, int z);

    public void dropItem(ItemStack stack);

    public void setDugBlock(int x, int y, int z);

    public EntityAITasks getTargetAI();

    public IExtendedEntityProperties getProperty(String key);

    public void setProperty(String key, IExtendedEntityProperties property);

    public void setEmittingRedstone(ForgeDirection orientation, int emittingRedstone);

    public void setName(String string);

    public void setCarryingEntity(Entity entity);

    public Entity getCarryingEntity();

    public boolean isAIOverriden();

    public void onItemPickupEvent(EntityItem curPickingUpEntity, int stackSize);
}
