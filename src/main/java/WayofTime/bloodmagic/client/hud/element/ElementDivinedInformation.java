package WayofTime.bloodmagic.client.hud.element;

import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;

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
            if (sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_DIVINATION)
                flag = true;

            if (!flag) {
                sigilStack = player.getHeldItem(EnumHand.OFF_HAND);
                if (sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_DIVINATION)
                    flag = true;
            }
        } else {
            if (sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
                flag = true;

            if (!flag) {
                sigilStack = player.getHeldItem(EnumHand.OFF_HAND);
                if (sigilStack.getItem() == RegistrarBloodMagicItems.SIGIL_SEER)
                    flag = true;
            }
        }

        return super.shouldRender(minecraft) && flag;
    }
}
