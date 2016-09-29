package WayofTime.bloodmagic.livingArmour.downgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeDisoriented extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { -10, -20 };
    public static final double[] chance = new double[] { 0.001, 0.002 };

    public LivingArmourUpgradeDisoriented(int level)
    {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {
        if (world.isRemote && player.ticksExisted % 20 == 0 && world.rand.nextDouble() <= chance[this.level])
        {
            player.rotationYaw = (float) (world.rand.nextFloat() * 360);
            player.rotationPitch = (float) (world.rand.nextFloat() * 180 - 90);
        }
    }

    @Override
    public boolean runOnClient()
    {
        return true;
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.disoriented";
    }

    @Override
    public int getMaxTier()
    {
        return 2;
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
        return tooltipBase + "disoriented";
    }
}