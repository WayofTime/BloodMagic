package WayofTime.alchemicalWizardry.api.util.helper;

import WayofTime.alchemicalWizardry.api.NBTHolder;
import com.google.common.base.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.UUID;
import java.util.regex.Pattern;

public class PlayerHelper {

    private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");

    public static String getUsernameFromPlayer(EntityPlayer player) {
        return player.getGameProfile().getName();
    }

    public static EntityPlayer getPlayerFromUsername(String username) {
        if (MinecraftServer.getServer() == null)
            return null;

        return MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
    }

    public static UUID getUUIDFromPlayer(EntityPlayer player) {
        return player.getGameProfile().getId();
    }

    public static boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(getUsernameFromPlayer(player)).matches();
    }

    public static void causeNauseaToPlayer(ItemStack stack) {
        stack = NBTHolder.checkNBT(stack);

        if (!Strings.isNullOrEmpty(stack.getTagCompound().getString(NBTHolder.NBT_OWNER)))
            causeNauseaToPlayer(stack.getTagCompound().getString(NBTHolder.NBT_OWNER));
    }

    public static void causeNauseaToPlayer(String ownerName) {
        EntityPlayer player = getPlayerFromUsername(ownerName);

        if (player == null)
            return;

        player.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
    }
}
