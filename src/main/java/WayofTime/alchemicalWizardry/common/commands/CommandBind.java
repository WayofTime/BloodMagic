package WayofTime.alchemicalWizardry.common.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;

public class CommandBind extends CommandBase
{
    public CommandBind() {}

    public String getCommandName()
    {
        return "bind";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "commands.bind.usage";
    }

    public void processCommand(ICommandSender iCommandSender, String[] astring)
    {
        EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(iCommandSender);
        ItemStack item = entityplayermp.getCurrentEquippedItem();
        EntityPlayerMP targetPlayer = getPlayer(iCommandSender, astring[0]);

        if (targetPlayer == null)
        {
            throw new CommandException("commands.bind.failed.noPlayer");
        }

        if (item != null && item.getItem() instanceof IBindable)
        {
            if (EnergyItems.getOwnerName(item).isEmpty())
            {
                EnergyItems.checkAndSetItemOwner(item, targetPlayer);
                func_152373_a(iCommandSender, this, "commands.bind.success");
            }
            else
            {
                throw new CommandException("commands.bind.failed.alreadyBound");
            }
        }
        else if (!(item.getItem() instanceof IBindable))
        {
            throw new CommandException("commands.bind.failed.notBindable");
        }
    }

    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] astring)
    {
        return getListOfStringsMatchingLastWord(astring, this.getPlayer());
    }

    protected String[] getPlayer()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    public boolean isUsernameIndex(String[] astring, int par2)
    {
        return par2 == 0;
    }
}
