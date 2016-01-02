package WayofTime.bloodmagic.api.util.helper;

import WayofTime.bloodmagic.api.Constants;

import com.google.common.base.Strings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;
import java.util.regex.Pattern;

public class PlayerHelper
{
    private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");

    public static String getUsernameFromPlayer(EntityPlayer player)
    {
        return UsernameCache.getLastKnownUsername(getUUIDFromPlayer(player));
    }

    public static EntityPlayer getPlayerFromUsername(String username)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            return null;

        return MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
    }

    public static EntityPlayer getPlayerFromUUID(String uuid)
    {
        return getPlayerFromUsername(getUsernameFromUUID(uuid));
    }

    public static EntityPlayer getPlayerFromUUID(UUID uuid)
    {
        return getPlayerFromUsername(getUsernameFromUUID(uuid));
    }

    public static UUID getUUIDFromPlayer(EntityPlayer player)
    {
        return player.getGameProfile().getId();
    }

    public static String getUsernameFromUUID(String uuid)
    {
        return UsernameCache.getLastKnownUsername(UUID.fromString(uuid));
    }

    public static String getUsernameFromUUID(UUID uuid)
    {
        return UsernameCache.getLastKnownUsername(uuid);
    }

    public static String getUsernameFromStack(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);

        return PlayerHelper.getUsernameFromUUID(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID));
    }

    public static boolean isFakePlayer(EntityPlayer player)
    {
        return player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(getUsernameFromPlayer(player)).matches();
    }

    public static void causeNauseaToPlayer(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);

        if (!Strings.isNullOrEmpty(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID)))
            causeNauseaToPlayer(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID));
    }

    public static void causeNauseaToPlayer(String ownerName)
    {
        EntityPlayer player = getPlayerFromUsername(ownerName);

        if (player == null)
            return;

        player.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
    }
}
