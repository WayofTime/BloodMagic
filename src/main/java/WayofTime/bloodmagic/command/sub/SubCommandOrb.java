package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.command.SubCommandBase;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.Locale;

public class SubCommandOrb extends SubCommandBase
{

    public SubCommandOrb(ICommand parent)
    {
        super(parent, "orb");
    }

    @Override
    public String getArgUsage(ICommandSender commandSender)
    {
        return StatCollector.translateToLocal("commands.orb.usage");
    }

    @Override
    public String getHelpText()
    {
        return StatCollector.translateToLocal("commands.orb.help");
    }

    @Override
    public void processSubCommand(ICommandSender commandSender, String[] args)
    {
        super.processSubCommand(commandSender, args);

        if (args.length > 0)
        {

            if (args[0].equalsIgnoreCase("help"))
                return;

            try
            {
                String givenName = commandSender.getName();

                if (args.length > 1)
                    givenName = args[1];

                EntityPlayer player = getPlayer(commandSender, givenName);
                String uuid = PlayerHelper.getUUIDFromPlayer(player).toString();
                SoulNetwork network = NetworkHelper.getSoulNetwork(uuid);

                boolean displayHelp = isBounded(0, 2, args.length);

                try
                {
                    switch (ValidCommands.valueOf(args[0].toUpperCase(Locale.ENGLISH)))
                    {
                    case SET:
                    {
                        if (displayHelp)
                        {
                            displayHelpString(commandSender, ValidCommands.SET.help);
                            break;
                        }

                        if (args.length == 3)
                        {
                            if (Utils.isInteger(args[2]))
                            {
                                int amount = Integer.parseInt(args[2]);
                                network.setOrbTier(amount);
                                displaySuccessString(commandSender, "commands.success");
                            } else
                            {
                                displayErrorString(commandSender, "commands.error.arg.invalid");
                            }
                        } else
                        {
                            displayErrorString(commandSender, "commands.error.arg.missing");
                        }

                        break;
                    }
                    case GET:
                    {
                        if (displayHelp)
                        {
                            displayHelpString(commandSender, ValidCommands.GET.help);
                            break;
                        }

                        if (args.length > 1)
                            commandSender.addChatMessage(new ChatComponentText(TextHelper.localizeEffect("message.orb.currenttier", network.getOrbTier())));

                        break;
                    }
                    }
                } catch (IllegalArgumentException e)
                {
                    displayErrorString(commandSender, "commands.error.404");
                }
            } catch (PlayerNotFoundException e)
            {
                displayErrorString(commandSender, "commands.error.404");
            }
        }
    }

    private enum ValidCommands
    {
        SET("commands.orb.set.help"),
        GET("commands.orb.get.help");

        public String help;

        ValidCommands(String help)
        {
            this.help = help;
        }
    }
}
