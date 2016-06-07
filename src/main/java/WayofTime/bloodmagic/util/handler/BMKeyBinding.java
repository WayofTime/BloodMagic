package WayofTime.bloodmagic.util.handler;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.KeyProcessor;
import WayofTime.bloodmagic.util.handler.event.ClientHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BMKeyBinding extends KeyBinding
{
    private Key keyType;

    public BMKeyBinding(String name, int keyId, Key keyType)
    {
        super(Constants.Mod.MODID + ".keybind." + name, keyId, Constants.Mod.NAME);
        this.keyType = keyType;
        ClientRegistry.registerKeyBinding(this);
        ClientHandler.keyBindings.add(this);
    }

    public void handleKeyPress()
    {
        ItemStack itemStack = ClientHandler.minecraft.thePlayer.getHeldItemMainhand();
        if (itemStack != null && itemStack.getItem() instanceof IKeybindable)
        {
            BloodMagicPacketHandler.INSTANCE.sendToServer(new KeyProcessor(this.keyType, false));
        }
    }

    public enum Key
    {
        OPEN_SIGIL_HOLDING
    }
}