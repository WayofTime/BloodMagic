package WayofTime.bloodmagic.item.soul;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.item.armour.ItemSentientArmour;

public class ItemSentientArmourGem extends Item
{
    public static double[] willBracket = new double[] { 30, 200, 600, 1500, 4000, 6000, 8000, 16000 };
    public static double[] consumptionPerHit = new double[] { 0.1, 0.12, 0.15, 0.2, 0.3, 0.35, 0.4, 0.5 };
    public static double[] extraProtectionLevel = new double[] { 0, 0.25, 0.5, 0.6, 0.7, 0.75, 0.85, 0.9 };

    public ItemSentientArmourGem()
    {
        super();

        setCreativeTab(BloodMagic.tabBloodMagic);
        setUnlocalizedName(Constants.Mod.MODID + ".sentientArmourGem");
        setRegistryName(Constants.BloodMagicItem.SENTIENT_ARMOR_GEM.getRegName());
        setMaxStackSize(1);
    }

    public EnumDemonWillType getCurrentType(ItemStack stack)
    {
        return EnumDemonWillType.DEFAULT;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        boolean hasSentientArmour = false;
        ItemStack[] armourInventory = player.inventory.armorInventory;
        for (ItemStack armourStack : armourInventory)
        {
            if (armourStack != null && armourStack.getItem() instanceof ItemSentientArmour)
            {
                hasSentientArmour = true;
            }
        }

        if (hasSentientArmour)
        {
            ItemSentientArmour.revertAllArmour(player);
        } else
        {
            double will = PlayerDemonWillHandler.getTotalDemonWill(getCurrentType(stack), player);

            int bracket = getWillBracket(will);

            if (bracket >= 0)
            {
//                PlayerDemonWillHandler.consumeDemonWill(player, willBracket[bracket]);
                ItemSentientArmour.convertPlayerArmour(player, consumptionPerHit[bracket], extraProtectionLevel[bracket]);
            }
        }

        return stack;
    }

    public int getWillBracket(double will)
    {
        int bracket = -1;

        for (int i = 0; i < willBracket.length; i++)
        {
            if (will >= willBracket[i])
            {
                bracket = i;
            }
        }

        return bracket;
    }

    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
    {
        boolean hasSentientArmour = false;
        ItemStack[] armourInventory = player.inventory.armorInventory;
        for (ItemStack armourStack : armourInventory)
        {
            if (armourStack != null && armourStack.getItem() instanceof ItemSentientArmour)
            {
                hasSentientArmour = true;
            }
        }

        if (hasSentientArmour)
        {
            return new ModelResourceLocation("bloodmagic:ItemSentientArmourGem1", "inventory");
        }

        return null;
    }
}
