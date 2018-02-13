package WayofTime.bloodmagic.client.hud;

import WayofTime.bloodmagic.client.Sprite;
import WayofTime.bloodmagic.item.sigil.ItemSigilDivination;
import WayofTime.bloodmagic.item.sigil.ItemSigilSeer;
import WayofTime.bloodmagic.tile.TileAltar;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.Color;
import java.util.List;
import java.util.function.Function;

public abstract class HUDElementCornerTile<T extends TileEntity> extends HUDElement {

    protected final List<Pair<Sprite, Function<T, String>>> information;

    public HUDElementCornerTile() {
        super(5, 5, RenderGameOverlayEvent.ElementType.HOTBAR);

        this.information = Lists.newArrayList();
        addInformation(information);
    }

    protected abstract void addInformation(List<Pair<Sprite, Function<T, String>>> information);

    @SuppressWarnings("unchecked")
    @Override
    public void render(Minecraft minecraft, ScaledResolution resolution, float partialTicks) {
        T tile = (T) Minecraft.getMinecraft().world.getTileEntity(Minecraft.getMinecraft().objectMouseOver.getBlockPos());

        int yOffset = 0;
        for (Pair<Sprite, Function<T, String>> sprite : information) {
            Minecraft.getMinecraft().renderEngine.bindTexture(sprite.getLeft().getTextureLocation());
            sprite.getLeft().draw(getXOffset(), getYOffset() + yOffset);
            int textY = getYOffset() + yOffset + (sprite.getLeft().getTextureHeight() / 4);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(sprite.getRight().apply(tile), getXOffset() + sprite.getLeft().getTextureWidth() + 2, textY, Color.WHITE.getRGB());
            yOffset += sprite.getLeft().getTextureHeight() + 2;
        }
    }

    public static abstract class BloodAltar extends HUDElementCornerTile<TileAltar> {

        private final boolean simple;

        public BloodAltar(boolean simple) {
            this.simple = simple;
        }

        @Override
        public boolean shouldRender(Minecraft minecraft) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            ItemStack sigilStack = player.getHeldItem(EnumHand.MAIN_HAND);
            boolean flag = false;
            if (simple) {
                if (sigilStack.getItem() instanceof ItemSigilDivination)
                    flag = true;

                if (!flag) {
                    sigilStack = player.getHeldItem(EnumHand.OFF_HAND);
                    if (sigilStack.getItem() instanceof ItemSigilDivination)
                        flag = true;
                }
            } else {
                if (sigilStack.getItem() instanceof ItemSigilSeer)
                    flag = true;

                if (!flag) {
                    sigilStack = player.getHeldItem(EnumHand.OFF_HAND);
                    if (sigilStack.getItem() instanceof ItemSigilSeer)
                        flag = true;
                }
            }

            TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
            if (!(tile instanceof TileAltar))
                flag = false;

            return flag;
        }
    }
}
