package WayofTime.bloodmagic.livingArmour.downgrade;

import net.minecraft.block.BlockIce;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeSlippery extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { -50 };
    public static final int[] slipperyDuration = new int[] { 30 * 20 };

    public double prevMotionX = 0;
    public double prevMotionZ = 0;

    public LivingArmourUpgradeSlippery(int level)
    {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {
        double weight = 0.05;
        if (world.isRemote && player.onGround)
        {
            if (player.moveForward == 0)
            {
                player.motionX = (player.motionX - this.prevMotionX) * weight + this.prevMotionX;
                player.motionZ = (player.motionZ - this.prevMotionZ) * weight + this.prevMotionZ;

                player.velocityChanged = true;
            }
        }

        this.prevMotionX = player.motionX;
        this.prevMotionZ = player.motionZ;
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.slippery";
    }

    @Override
    public int getMaxTier()
    {
        return 1;
    }

    @Override
    public int getCostOfUpgrade()
    {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "slippery";
    }
}