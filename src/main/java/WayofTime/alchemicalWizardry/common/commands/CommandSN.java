package WayofTime.alchemicalWizardry.common.commands;

import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CommandSN extends CommandBase
{
    public CommandSN() {}

    public String getCommandName()
    {
        return "soulnetwork";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "commands.soulnetwork.usage";
    }

    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        EntityPlayerMP targetPlayer = getPlayer(icommandsender, astring[0]);
        String owner = targetPlayer.getDisplayName();

        if (astring.length >= 2 && astring.length <= 3)
        {
            if ("add".equalsIgnoreCase(astring[1]))
            {
                int amount = parseIntBounded(icommandsender, astring[2], Integer.MIN_VALUE, Integer.MAX_VALUE);

                SoulNetworkHandler.addCurrentEssenceToMaximum(owner, amount, Integer.MAX_VALUE);
                func_152373_a(icommandsender, this, "commands.soulnetwork.add.success", amount, owner);
            }
            else if ("subtract".equalsIgnoreCase(astring[1]))
            {
                int amount = parseIntBounded(icommandsender, astring[2], Integer.MIN_VALUE, Integer.MAX_VALUE);

                if (amount > SoulNetworkHandler.getCurrentEssence(owner))
                {
                    int lp = SoulNetworkHandler.getCurrentEssence(owner);
                    SoulNetworkHandler.syphonFromNetwork(owner, lp);
                    func_152373_a(icommandsender, this, "commands.soulnetwork.subtract.success", SoulNetworkHandler.getCurrentEssence(owner), owner);
                }
                else
                {
                    SoulNetworkHandler.syphonFromNetwork(owner, amount);
                    func_152373_a(icommandsender, this, "commands.soulnetwork.subtract.success", amount, owner);
                }
            }
            else if ("fill".equalsIgnoreCase(astring[1]))
            {
                int amount = Integer.MAX_VALUE - SoulNetworkHandler.getCurrentEssence(owner);
                SoulNetworkHandler.addCurrentEssenceToMaximum(owner, amount, Integer.MAX_VALUE);
                func_152373_a(icommandsender, this, "commands.soulnetwork.fill.success", owner);
            }
            else if ("empty".equalsIgnoreCase(astring[1]))
            {
                SoulNetworkHandler.syphonFromNetwork(owner, SoulNetworkHandler.getCurrentEssence(owner));
                func_152373_a(icommandsender, this, "commands.soulnetwork.empty.success", owner);
            }
            else if ("get".equalsIgnoreCase(astring[1]))
            {
                int amount = SoulNetworkHandler.getCurrentEssence(owner);
                func_152373_a(icommandsender, this, "commands.soulnetwork.get.success", amount, owner);
            }
            else
            {
                throw new CommandException("commands.soulnetwork.notACommand");
            }
        }
        else if (astring.length == 0)
        {
            throw new CommandException("commands.soulnetwork.noPlayer");
        }
        else if (astring.length == 1)
        {
            throw new CommandException("commands.soulnetwork.noCommand");
        }
    }

    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] astring)
    {
        if (astring.length == 1)
        {
            return getListOfStringsMatchingLastWord(astring, this.getPlayer());
        }
        else if (astring.length == 2)
        {
            return getListOfStringsMatchingLastWord(astring, "add", "subtract", "fill", "empty", "get");
        }

        return null;
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
