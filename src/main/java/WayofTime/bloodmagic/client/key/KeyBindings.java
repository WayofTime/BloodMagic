package WayofTime.bloodmagic.client.key;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.KeyProcessor;
import WayofTime.bloodmagic.util.handler.event.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

public enum KeyBindings {
    // @formatter:off
    OPEN_HOLDING(KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_H) {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleKeybind() {
            ItemStack itemStack = ClientHandler.minecraft.player.getHeldItemMainhand();
            if (itemStack.getItem() instanceof IKeybindable)
                BloodMagicPacketHandler.INSTANCE.sendToServer(new KeyProcessor(this, false));
        }
    },
    CYCLE_HOLDING_POS(KeyConflictContext.IN_GAME, KeyModifier.SHIFT, Keyboard.KEY_EQUALS) {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleKeybind() {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player.getHeldItemMainhand().getItem() instanceof ItemSigilHolding)
                ClientHandler.cycleSigil(player.getHeldItemMainhand(), player, -1);
        }
    },
    CYCLE_HOLDING_NEG(KeyConflictContext.IN_GAME, KeyModifier.SHIFT, Keyboard.KEY_MINUS) {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleKeybind() {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player.getHeldItemMainhand().getItem() instanceof ItemSigilHolding)
                ClientHandler.cycleSigil(player.getHeldItemMainhand(), player, 1);
        }
    },
    ;
    // @formatter:on

    private final IKeyConflictContext keyConflictContext;
    private final KeyModifier keyModifier;
    private final int keyCode;

    @SideOnly(Side.CLIENT)
    private KeyBinding key;

    KeyBindings(IKeyConflictContext keyConflictContext, KeyModifier keyModifier, int keyCode) {
        this.keyConflictContext = keyConflictContext;
        this.keyModifier = keyModifier;
        this.keyCode = keyCode;
    }

    @SideOnly(Side.CLIENT)
    public abstract void handleKeybind();

    public IKeyConflictContext getKeyConflictContext() {
        return keyConflictContext;
    }

    public KeyModifier getKeyModifier() {
        return keyModifier;
    }

    public int getKeyCode() {
        return keyCode;
    }

    @SideOnly(Side.CLIENT)
    public KeyBinding getKey() {
        if (key == null)
            key = new KeyBindingBloodMagic(this);

        return key;
    }

    @SideOnly(Side.CLIENT)
    public void setKey(KeyBinding key) {
        this.key = key;
    }

    public String getDescription() {
        return BloodMagic.MODID + ".keybind." + name().toLowerCase(Locale.ENGLISH);
    }
}
