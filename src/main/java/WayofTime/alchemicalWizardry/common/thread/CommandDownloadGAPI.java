/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 31, 2014, 10:22:44 PM (GMT)]
 */
package WayofTime.alchemicalWizardry.common.thread;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class CommandDownloadGAPI extends CommandBase
{
    private static final boolean ENABLED = true;

    @Override
    public String getCommandName()
    {
        return "bloodmagic-download-g-api";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/bloodmagic-download-g-api";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2)
    {
        if (!ENABLED)
            var1.addChatMessage(new ChatComponentTranslation("bm.versioning.disabled").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));

        else
            if (GAPIChecker.downloadedFile)
                var1.addChatMessage(new ChatComponentTranslation("bm.versioning.downloadedAlready").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            else if (GAPIChecker.startedDownload)
                var1.addChatMessage(new ChatComponentTranslation("bm.versioning.downloadingAlready").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            else new ThreadDownloadGAPI("Guide-API-1.7.10-" + GAPIChecker.onlineVersion + ".jar");
    }
}
