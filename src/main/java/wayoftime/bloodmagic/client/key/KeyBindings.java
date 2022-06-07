package wayoftime.bloodmagic.client.key;

import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.ClientEvents;
import wayoftime.bloodmagic.common.item.sigil.ItemSigilHolding;
import wayoftime.bloodmagic.network.KeyProcessorPacket;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;

public enum KeyBindings
{
	// @formatter:off
	OPEN_HOLDING(KeyConflictContext.IN_GAME, KeyModifier.NONE, -1)
	{
		@OnlyIn(Dist.CLIENT)
		@Override
		public void handleKeybind()
		{
			ItemStack itemStack = ClientHandler.minecraft.player.getHeldItemMainhand();
			if (itemStack.getItem() instanceof IKeybindable)
				BloodMagic.packetHandler.sendToServer(new KeyProcessorPacket(this.ordinal(), false));
//			System.out.println("I is on the client.");
//			
//			
//				BloodMagicPacketHandler.INSTANCE.sendToServer(new KeyProcessorPacket(this, false));
		}
	},
	CYCLE_HOLDING_POS(KeyConflictContext.IN_GAME, KeyModifier.SHIFT, -1)
	{
		@OnlyIn(Dist.CLIENT)
		@Override
		public void handleKeybind()
		{
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if (player.getHeldItemMainhand().getItem() instanceof ItemSigilHolding)
				ClientEvents.cycleSigil(player.getHeldItemMainhand(), player, -1);
		}
	},
	CYCLE_HOLDING_NEG(KeyConflictContext.IN_GAME, KeyModifier.SHIFT, -1)
	{
		@OnlyIn(Dist.CLIENT)
		@Override
		public void handleKeybind()
		{
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if (player.getHeldItemMainhand().getItem() instanceof ItemSigilHolding)
				ClientEvents.cycleSigil(player.getHeldItemMainhand(), player, 1);
		}
	},;
	// @formatter:on

	private final IKeyConflictContext keyConflictContext;
	private final KeyModifier keyModifier;
	private final int keyCode;

	@OnlyIn(Dist.CLIENT)
	private KeyBinding key;

	KeyBindings(IKeyConflictContext keyConflictContext, KeyModifier keyModifier, int keyCode)
	{
		this.keyConflictContext = keyConflictContext;
		this.keyModifier = keyModifier;
		this.keyCode = keyCode;
	}

	@OnlyIn(Dist.CLIENT)
	public abstract void handleKeybind();

	public IKeyConflictContext getKeyConflictContext()
	{
		return keyConflictContext;
	}

	public KeyModifier getKeyModifier()
	{
		return keyModifier;
	}

	public int getKeyCode()
	{
		return keyCode;
	}

	@OnlyIn(Dist.CLIENT)
	public KeyBinding getKey()
	{
		if (key == null)
			key = new KeyBindingBloodMagic(this);

		return key;
	}

	@OnlyIn(Dist.CLIENT)
	public void setKey(KeyBinding key)
	{
		this.key = key;
	}

	public String getDescription()
	{
		return BloodMagic.MODID + ".keybind." + name().toLowerCase(Locale.ROOT);
	}

	public static void initializeKeys()
	{
		OPEN_HOLDING.getKey();
		CYCLE_HOLDING_POS.getKey();
		CYCLE_HOLDING_NEG.getKey();
	}
}