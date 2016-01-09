package WayofTime.bloodmagic.item.soul;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;

public class ItemSoulBow extends ItemBow
{
    public ItemSoulBow()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".soulBow");
        this.setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
    {
        if (player.getItemInUse() == null)
        {
            return null;
        }

        int i = stack.getMaxItemUseDuration() - player.getItemInUseCount();

        if (i >= 18)
        {
            return new ModelResourceLocation("bloodmagic:ItemSoulBow_pulling_2", "inventory");
        } else if (i > 13)
        {
            return new ModelResourceLocation("bloodmagic:ItemSoulBow_pulling_1", "inventory");
        } else if (i > 0)
        {
            return new ModelResourceLocation("bloodmagic:ItemSoulBow_pulling_0", "inventory");
        }

        return null;
    }
}
