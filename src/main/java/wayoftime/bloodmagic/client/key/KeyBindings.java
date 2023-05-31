package wayoftime.bloodmagic.client.key;

import java.util.Locale;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.ItemStack;
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
			ItemStack itemStack = ClientHandler.minecraft.player.getMainHandItem();
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
			LocalPlayer player = Minecraft.getInstance().player;
			if (player.getMainHandItem().getItem() instanceof ItemSigilHolding)
				ClientEvents.cycleSigil(player.getMainHandItem(), player, -1);
		}
	},
	CYCLE_HOLDING_NEG(KeyConflictContext.IN_GAME, KeyModifier.SHIFT, -1)
	{
		@OnlyIn(Dist.CLIENT)
		@Override
		public void handleKeybind()
		{
			LocalPlayer player = Minecraft.getInstance().player;
			if (player.getMainHandItem().getItem() instanceof ItemSigilHolding)
				ClientEvents.cycleSigil(player.getMainHandItem(), player, 1);
		}
	},;
	// @formatter:on

	private final IKeyConflictContext keyConflictContext;
	private final KeyModifier keyModifier;
	private final int keyCode;

	@OnlyIn(Dist.CLIENT)
	private KeyMapping key;

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
	public KeyMapping getKey()
	{
		if (key == null)
			key = toKeyMapping();
		return key;
	}

	@OnlyIn(Dist.CLIENT)
	public void setKey(KeyMapping key)
	{
		this.key = key;
	}

	public String getDescription()
	{
		return BloodMagic.MODID + ".keybind." + name().toLowerCase(Locale.ROOT);
	}

	public KeyMapping toKeyMapping() {
		return new KeyMapping(getDescription(), keyConflictContext, keyModifier,InputConstants.Type.KEYSYM, getKeyCode(), "key.bloodmagic.category");
	}


}