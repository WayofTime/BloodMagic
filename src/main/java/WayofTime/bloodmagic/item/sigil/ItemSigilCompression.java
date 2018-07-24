package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.compress.CompressionRegistry;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSigilCompression extends ItemSigilToggleableBase {
    public ItemSigilCompression() {
        super("compression", 200);
    }

    // TODO REWRITE all compression stuff if someone has time
    // TODO for now, there is a semi-working system in place

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected) {

        if (PlayerHelper.isFakePlayer(player))
            return;

        ItemStack compressedStack = CompressionRegistry.compressInventory(player.inventory.mainInventory.toArray(new ItemStack[player.inventory.mainInventory.size()]), world);

        if (compressedStack != null) {
            EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, compressedStack);
            world.spawnEntity(entityItem);
        }
    }
}
