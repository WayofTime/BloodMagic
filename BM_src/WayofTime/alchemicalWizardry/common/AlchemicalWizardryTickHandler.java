package WayofTime.alchemicalWizardry.common;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.TickType;

public class AlchemicalWizardryTickHandler implements ITickHandler
{
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel()
	{
		return "BloodMagic";
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		String[] usernames = MinecraftServer.getServer().getAllUsernames();

		if (usernames == null)
		{
			return;
		}

		for (String userName : usernames)
		{
			EntityPlayer entityPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(userName);

			if (entityPlayer != null)
			{
				ObfuscationReflectionHelper.setPrivateValue(PlayerCapabilities.class, entityPlayer.capabilities, Float.valueOf(0.1f), new String[] { "walkSpeed", "g", "field_75097_g" });
				//entityPlayer.sendPlayerAbilities();
			}
		}
	}
}
