package WayofTime.bloodmagic.livingArmour.upgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class LivingArmourUpgradeGrimReaperSprint extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { 20, 50, 130, 270, 450, 580, 700, 800, 900, 1000 };
    public static final int[] rebirthDelay = new int[] { 20 * 60 * 60, 20 * 60 * 50, 20 * 60 * 45, 20 * 60 * 40, 20 * 60 * 30, 20 * 60 * 25, 20 * 60 * 15, 20 * 60 * 10, 20 * 60 * 5, 20 * 60 };

    public int deathTimer = 0;

    public LivingArmourUpgradeGrimReaperSprint(int level)
    {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {
        if (deathTimer > 0)
        {
            deathTimer--;
        }
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.grimReaper";
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
    public void readFromNBT(NBTTagCompound tag)
    {
        deathTimer = tag.getInteger(Constants.Mod.MODID + ".tracker.grimReaper");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger(Constants.Mod.MODID + ".tracker.grimReaper", deathTimer);
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "grimReaper";
    }

    public void applyEffectOnRebirth(EntityPlayer player)
    {
        player.setHealth(player.getMaxHealth());
        deathTimer = rebirthDelay[this.level];
        ChatUtil.sendNoSpam(player, TextHelper.localizeEffect(chatBase + "grimReaper"));
    }

    public boolean canSavePlayer(EntityPlayer player)
    {
        return deathTimer <= 0;
    }
}
