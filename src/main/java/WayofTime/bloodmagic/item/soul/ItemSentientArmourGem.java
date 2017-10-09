package WayofTime.bloodmagic.item.soul;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.item.armour.ItemSentientArmour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSentientArmourGem extends Item implements IMeshProvider {
    public ItemSentientArmourGem() {
        super();

        setCreativeTab(BloodMagic.TAB_BM);
        setUnlocalizedName(BloodMagic.MODID + ".sentientArmourGem");
        setMaxStackSize(1);
    }

    public EnumDemonWillType getCurrentType(ItemStack stack) {
        return EnumDemonWillType.DEFAULT;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        boolean hasSentientArmour = false;
        NonNullList<ItemStack> armourInventory = player.inventory.armorInventory;
        for (ItemStack armourStack : armourInventory) {
            if (armourStack != null && armourStack.getItem() instanceof ItemSentientArmour) {
                hasSentientArmour = true;
            }
        }

        if (hasSentientArmour) {
            ItemSentientArmour.revertAllArmour(player);
        } else {
            EnumDemonWillType type = PlayerDemonWillHandler.getLargestWillType(player);
            double will = PlayerDemonWillHandler.getTotalDemonWill(type, player);

//                PlayerDemonWillHandler.consumeDemonWill(player, willBracket[bracket]);
            ItemSentientArmour.convertPlayerArmour(type, will, player);
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemMeshDefinition getMeshDefinition() {
        return stack -> {
            boolean flag = false;
            NonNullList<ItemStack> armourInventory = Minecraft.getMinecraft().player.inventory.armorInventory;
            for (ItemStack armourStack : armourInventory)
                if (!armourStack.isEmpty() && armourStack.getItem() instanceof ItemSentientArmour)
                    flag = true;

            return new ModelResourceLocation(stack.getItem().getRegistryName(), "type=" + (flag ? "" : "de") + "activated");
        };
    }

    @Override
    public void populateVariants(List<String> variants) {
        variants.add("type=activated");
        variants.add("type=deactivated");
    }
}
