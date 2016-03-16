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

public class SubCommandNetwork extends SubCommandBase
{

    public SubCommandNetwork(ICommand parent)
    {
        super(parent, "network");
    }

    @Override
    public String getArgUsage(ICommandSender commandSender)
    {
        return StatCollector.translateToLocal("commands.network.usage");
    }

    @Override
    public String getHelpText()
    {
        return StatCollector.translateToLocal("commands.network.help");
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
                EntityPlayer player = getPlayer(commandSender, givenName);

                if (args.length > 1)
                {
                    givenName = args[1];
                    player = getPlayer(commandSender, givenName);
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);
                boolean displayHelp = isBounded(0, 2, args.length);

                try
                {
                    switch (ValidCommands.valueOf(args[0].toUpperCase(Locale.ENGLISH)))
                    {
                    case SYPHON:
                    {
                        if (displayHelp)
                        {
                            displayHelpString(commandSender, ValidCommands.SYPHON.help);
                            break;
                        }

                        if (args.length == 3)
                        {
                            if (Utils.isInteger(args[2]))
                            {
                                int amount = Integer.parseInt(args[2]);
                                NetworkHelper.syphonAndDamage(NetworkHelper.getSoulNetwork(player), player, amount);
                                displaySuccessString(commandSender, "commands.network.syphon.success", amount, givenName);
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
                    case ADD:
                    {
                        if (displayHelp)
                        {
                            displayHelpString(commandSender, ValidCommands.ADD.help);
                            break;
                        }

                        if (args.length == 3)
                        {
                            if (Utils.isInteger(args[2]))
                            {
                                int amount = Integer.parseInt(args[2]);
                                int maxOrb = NetworkHelper.getMaximumForTier(network.getOrbTier());
                                displaySuccessString(commandSender, "commands.network.add.success", network.addLifeEssence(amount, maxOrb), givenName);
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
                                network.setCurrentEssence(amount);
                                displaySuccessString(commandSender, "commands.network.set.success", givenName, amount);
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
                            commandSender.addChatMessage(new ChatComponentText(TextHelper.localizeEffect("message.divinationsigil.currentessence", network.getCurrentEssence())));

                        break;
                    }
                    case FILL:
                    {
                        if (displayHelp)
                        {
                            displayHelpString(commandSender, ValidCommands.FILL.help, Integer.MAX_VALUE);
                            break;
                        }

                        if (args.length > 1)
                        {
                            network.setCurrentEssence(Integer.MAX_VALUE);
                            displaySuccessString(commandSender, "commands.network.fill.success", givenName);
                        }

                        break;
                    }
                    case CAP:
                    {
                        if (displayHelp)
                        {
                            displayHelpString(commandSender, ValidCommands.CAP.help);
                            break;
                        }

                        if (args.length > 1)
                        {
                            int maxOrb = NetworkHelper.getMaximumForTier(network.getOrbTier());
                            network.setCurrentEssence(maxOrb);
                            displaySuccessString(commandSender, "commands.network.cap.success", givenName);
                        }

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
        SYPHON("commands.network.syphon.help"),
        ADD("commands.network.add.help"),
        SET("commands.network.set.help"),
        GET("commands.network.get.help"),
        FILL("commands.network.fill.help"),
        CAP("commands.network.cap.help");

        public String help;

        ValidCommands(String help)
        {
            this.help = help;
        }
    }
}
