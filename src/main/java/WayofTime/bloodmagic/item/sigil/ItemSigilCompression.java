package WayofTime.bloodmagic.item.sigil;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.compress.CompressionRegistry;

public class ItemSigilCompression extends ItemSigilToggleable
{
    public ItemSigilCompression()
    {
        super("compression", 200);
        setRegistryName(Constants.BloodMagicItem.SIGIL_COMPRESSION.getRegName());
    }

    // TODO REWRITE all compression stuff if someone has time
    // TODO for now, there is a semi-working system in place

    @Override
    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
        ItemStack compressedStack = CompressionRegistry.compressInventory(player.inventory.mainInventory, world);

        if (compressedStack != null)
        {
            EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, compressedStack);
            world.spawnEntityInWorld(entityItem);
        }
    }
}
