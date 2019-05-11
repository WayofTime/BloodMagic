package WayofTime.bloodmagic.client.hud.element;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;

import java.util.List;

public abstract class ElementDivinedInformation<T extends TileEntity> extends ElementTileInformation<T> {

    private final boolean simple;

    public ElementDivinedInformation(int lines, boolean simple, Class<T> tileClass) {
        super(100, lines, tileClass);
        this.simple = simple;
    }

    @Override
    public boolean shouldRender(Minecraft minecraft) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        ItemStack sigilStack = player.getHeldItem(EnumHand.MAIN_HAND);
        boolean flag = false;
        if (simple) {
            if (sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_DIVINATION || sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
                flag = true;
            else flag = isFlagSigilHolding(sigilStack, true);

            if (!flag) {
                sigilStack = player.getHeldItem(EnumHand.OFF_HAND);
                if (sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_DIVINATION || sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
                    flag = true;
                else flag = isFlagSigilHolding(sigilStack, true);
            }

        } else {
            if (sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
                flag = true;
            else flag = isFlagSigilHolding(sigilStack, false);

            if (!flag) {
                sigilStack = player.getHeldItem(EnumHand.OFF_HAND);
                if (sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
                    flag = true;
                else flag = isFlagSigilHolding(sigilStack, false);
            }
        }
        return super.shouldRender(minecraft) && flag;
    }

    private boolean isFlagSigilHolding(ItemStack sigilStack, boolean simple) {
        if (sigilStack.getItem() instanceof ItemSigilHolding) {
            List<ItemStack> internalInv = ItemSigilHolding.getInternalInventory(sigilStack);
            int currentSlot = ItemSigilHolding.getCurrentItemOrdinal(sigilStack);
            if (internalInv != null && !internalInv.get(currentSlot).isEmpty()) {
                return (internalInv.get(currentSlot).getItem() == RegistrarBloodMagicItems.SIGIL_SEER && !simple) || (internalInv.get(currentSlot).getItem() == RegistrarBloodMagicItems.SIGIL_DIVINATION && simple);
            }
        }
        return false;
    }
}
