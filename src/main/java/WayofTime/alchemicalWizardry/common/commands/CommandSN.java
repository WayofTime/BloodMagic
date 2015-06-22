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

    @Override
    public String getCommandName()
    {
        return "soulnetwork";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "commands.soulnetwork.usage";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        EntityPlayerMP targetPlayer = getPlayer(icommandsender, astring[0]);
        String owner = targetPlayer.getDisplayName();
        EntityPlayerMP proxyPlayerName = getPlayer(icommandsender, astring[0]);

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
                int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
                func_152373_a(icommandsender, this, "commands.soulnetwork.get.success", currentEssence, owner);
            }
            else if ("fillMax".equalsIgnoreCase(astring[1]))
            {
                int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
                int orbTier = SoulNetworkHandler.getCurrentMaxOrb(owner);
                int maxForOrb = SoulNetworkHandler.getMaximumForOrbTier(orbTier);
                int fillAmount = maxForOrb - currentEssence;
                SoulNetworkHandler.addCurrentEssenceToMaximum(owner, fillAmount, fillAmount);
                func_152373_a(icommandsender, this, "commands.soulnetwork.fillMax.success", owner);
            }
            else if ("create".equalsIgnoreCase(astring[1]))
            {
                int orbTier = parseIntBounded(icommandsender, astring[2], 1, 6);
                SoulNetworkHandler.setMaxOrbToMax(proxyPlayerName.getDisplayName(), orbTier);
                func_152373_a(icommandsender, this, "commands.soulnetwork.create.success", owner, orbTier);
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

    @Override
    public List addTabCompletionOptions(ICommandSender iCommandSender, String[] astring)
    {
        if (astring.length == 1)
        {
            return getListOfStringsMatchingLastWord(astring, this.getPlayer());
        }
        else if (astring.length == 2)
        {
            return getListOfStringsMatchingLastWord(astring, "add", "subtract", "fill", "empty", "get", "fillMax", "create");
        }

        return null;
    }

    protected String[] getPlayer()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int par2)
    {
        return par2 == 0;
    }
}
