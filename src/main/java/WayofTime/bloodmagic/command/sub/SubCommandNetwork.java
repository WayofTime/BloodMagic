package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.command.SubCommandBase;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
        return TextHelper.localizeEffect("commands.network.usage");
    }

    @Override
    public String getHelpText()
    {
        return TextHelper.localizeEffect("commands.network.help");
    }

    @Override
    public void processSubCommand(MinecraftServer server, ICommandSender commandSender, String[] args)
    {
        super.processSubCommand(server, commandSender, args);

        if (args.length > 1)
        {
            if (args[0].equalsIgnoreCase("help"))
                return;

            try
            {
                EntityPlayer player = CommandBase.getPlayer(server, commandSender, args[1]);

                try
                {
                    ValidCommands command = ValidCommands.valueOf(args[0].toUpperCase(Locale.ENGLISH));
                    command.run(player, commandSender, isBounded(0, 2, args.length), args);
                } catch (IllegalArgumentException e)
                {

                }
            } catch (PlayerNotFoundException e)
            {
                displayErrorString(commandSender, e.getLocalizedMessage());
            }
        } else
        {
            displayErrorString(commandSender, "commands.error.arg.missing");
        }
    }

    private enum ValidCommands
    {
        SYPHON("commands.network.syphon.help")
        {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args)
            {
                if (displayHelp)
                {
                    displayHelpString(sender, this.help);
                    return;
                }

                if (args.length == 3)
                {
                    if (Utils.isInteger(args[2]))
                    {
                        int amount = Integer.parseInt(args[2]);
                        NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, amount);
                        displaySuccessString(sender, "commands.network.syphon.success", amount, player.getDisplayName().getFormattedText());
                    } else
                    {
                        displayErrorString(sender, "commands.error.arg.invalid");
                    }
                } else
                {
                    displayErrorString(sender, "commands.error.arg.missing");
                }
            }
        },
        ADD("commands.network.add.help")
        {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args)
            {
                if (displayHelp)
                {
                    displayHelpString(sender, this.help);
                    return;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);

                if (args.length == 3)
                {
                    if (Utils.isInteger(args[2]))
                    {
                        int amount = Integer.parseInt(args[2]);
                        int maxOrb = NetworkHelper.getMaximumForTier(network.getOrbTier());
                        displaySuccessString(sender, "commands.network.add.success", network.add(amount, maxOrb), player.getDisplayName().getFormattedText());
                    } else
                    {
                        displayErrorString(sender, "commands.error.arg.invalid");
                    }
                } else
                {
                    displayErrorString(sender, "commands.error.arg.missing");
                }
            }
        },
        SET("commands.network.set.help")
        {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args)
            {
                if (displayHelp)
                {
                    displayHelpString(sender, this.help);
                    return;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);

                if (args.length == 3)
                {
                    if (Utils.isInteger(args[2]))
                    {
                        int amount = Integer.parseInt(args[2]);
                        network.setCurrentEssence(amount);
                        displaySuccessString(sender, "commands.network.set.success", player.getDisplayName().getFormattedText(), amount);
                    } else
                    {
                        displayErrorString(sender, "commands.error.arg.invalid");
                    }
                } else
                {
                    displayErrorString(sender, "commands.error.arg.missing");
                }
            }
        },
        GET("commands.network.get.help")
        {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args)
            {
                if (displayHelp)
                {
                    displayHelpString(sender, this.help);
                    return;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);

                if (args.length > 1)
                    sender.addChatMessage(new TextComponentString(TextHelper.localizeEffect("tooltip.BloodMagic.sigil.divination.currentEssence", network.getCurrentEssence())));

            }
        },
        FILL("commands.network.fill.help")
        {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args)
            {
                if (displayHelp)
                {
                    displayHelpString(sender, this.help, Integer.MAX_VALUE);
                    return;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);

                if (args.length > 1)
                {
                    network.setCurrentEssence(Integer.MAX_VALUE);
                    displaySuccessString(sender, "commands.network.fill.success", player.getDisplayName().getFormattedText());
                }
            }
        },
        CAP("commands.network.cap.help")
        {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args)
            {
                if (displayHelp)
                {
                    displayHelpString(sender, this.help);
                    return;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);

                if (args.length > 1)
                {
                    int maxOrb = NetworkHelper.getMaximumForTier(network.getOrbTier());
                    network.setCurrentEssence(maxOrb);
                    displaySuccessString(sender, "commands.network.cap.success", player.getDisplayName().getFormattedText());
                }
            }
        },
        ;

        public String help;

        ValidCommands(String help)
        {
            this.help = help;
        }

        public abstract void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args);
    }
}
