package WayofTime.alchemicalWizardry.common;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

@Deprecated
public class AlchemicalWizardryTickHandler //implements ITickHandler
{
//    public void tickStart(EnumSet<TickType> type, Object... tickData)
//    {
//    }
//
//    public EnumSet<TickType> ticks()
//    {
//        return EnumSet.of(TickType.PLAYER);
//    }
//
//    public String getLabel()
//    {
//        return "BloodMagic";
//    }
//
//    public void tickEnd(EnumSet<TickType> type, Object... tickData)
//    {
//        String[] usernames = MinecraftServer.getServer().getAllUsernames();
//
//        if (usernames == null)
//        {
//            return;
//        }
//
//        for (String userName : usernames)
//        {
//            EntityPlayer entityPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(userName);
//
//            if (entityPlayer != null)
//            {
//                ObfuscationReflectionHelper.setPrivateValue(PlayerCapabilities.class, entityPlayer.capabilities, Float.valueOf(0.1f), new String[]{"walkSpeed", "g", "field_75097_g"});
//                //entityPlayer.sendPlayerAbilities();
//            }
//        }
//    }
}
