package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.command.CommandBloodMagic;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Locale;

public class SubCommandNetwork extends CommandBase {
    @Override
    public String getName() {
        return "network";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return TextHelper.localizeEffect("commands.network.usage");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("help"))
                return;

            try {
                EntityPlayer player = CommandBase.getPlayer(server, commandSender, args[1]);

                try {
                    ValidCommands command = ValidCommands.valueOf(args[0].toUpperCase(Locale.ENGLISH));
                    command.run(player, commandSender, args.length > 0 && args.length < 2, args);
                } catch (IllegalArgumentException e) {

                }
            } catch (PlayerNotFoundException e) {
                CommandBloodMagic.displayErrorString(commandSender, e.getLocalizedMessage());
            }
        } else {
            CommandBloodMagic.displayErrorString(commandSender, "commands.error.arg.missing");
        }
    }

    private enum ValidCommands {
        SYPHON("commands.network.syphon.help") {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args) {
                if (displayHelp) {
                    CommandBloodMagic.displayHelpString(sender, this.help);
                    return;
                }

                if (args.length == 3) {
                    if (Utils.isInteger(args[2])) {
                        int amount = Integer.parseInt(args[2]);
                        NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, amount);
                        CommandBloodMagic.displaySuccessString(sender, "commands.network.syphon.success", amount, player.getDisplayName().getFormattedText());
                    } else {
                        CommandBloodMagic.displayErrorString(sender, "commands.error.arg.invalid");
                    }
                } else {
                    CommandBloodMagic.displayErrorString(sender, "commands.error.arg.missing");
                }
            }
        },
        ADD("commands.network.add.help") {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args) {
                if (displayHelp) {
                    CommandBloodMagic.displayHelpString(sender, this.help);
                    return;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);

                if (args.length == 3) {
                    if (Utils.isInteger(args[2])) {
                        int amount = Integer.parseInt(args[2]);
                        int maxOrb = NetworkHelper.getMaximumForTier(network.getOrbTier());
                        CommandBloodMagic.displaySuccessString(sender, "commands.network.add.success", network.add(amount, maxOrb), player.getDisplayName().getFormattedText());
                    } else {
                        CommandBloodMagic.displayErrorString(sender, "commands.error.arg.invalid");
                    }
                } else {
                    CommandBloodMagic.displayErrorString(sender, "commands.error.arg.missing");
                }
            }
        },
        SET("commands.network.set.help") {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args) {
                if (displayHelp) {
                    CommandBloodMagic.displayHelpString(sender, this.help);
                    return;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);

                if (args.length == 3) {
                    if (Utils.isInteger(args[2])) {
                        int amount = Integer.parseInt(args[2]);
                        network.setCurrentEssence(amount);
                        CommandBloodMagic.displaySuccessString(sender, "commands.network.set.success", player.getDisplayName().getFormattedText(), amount);
                    } else {
                        CommandBloodMagic.displayErrorString(sender, "commands.error.arg.invalid");
                    }
                } else {
                    CommandBloodMagic.displayErrorString(sender, "commands.error.arg.missing");
                }
            }
        },
        GET("commands.network.get.help") {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args) {
                if (displayHelp) {
                    CommandBloodMagic.displayHelpString(sender, this.help);
                    return;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);

                if (args.length > 1)
                    sender.sendMessage(new TextComponentString(TextHelper.localizeEffect("tooltip.bloodmagic.sigil.divination.currentEssence", network.getCurrentEssence())));

            }
        },
        FILL("commands.network.fill.help") {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args) {
                if (displayHelp) {
                    CommandBloodMagic.displayHelpString(sender, this.help, Integer.MAX_VALUE);
                    return;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);

                if (args.length > 1) {
                    network.setCurrentEssence(Integer.MAX_VALUE);
                    CommandBloodMagic.displaySuccessString(sender, "commands.network.fill.success", player.getDisplayName().getFormattedText());
                }
            }
        },
        CAP("commands.network.cap.help") {
            @Override
            public void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args) {
                if (displayHelp) {
                    CommandBloodMagic.displayHelpString(sender, this.help);
                    return;
                }

                SoulNetwork network = NetworkHelper.getSoulNetwork(player);

                if (args.length > 1) {
                    int maxOrb = NetworkHelper.getMaximumForTier(network.getOrbTier());
                    network.setCurrentEssence(maxOrb);
                    CommandBloodMagic.displaySuccessString(sender, "commands.network.cap.success", player.getDisplayName().getFormattedText());
                }
            }
        },;

        public String help;

        ValidCommands(String help) {
            this.help = help;
        }

        public abstract void run(EntityPlayer player, ICommandSender sender, boolean displayHelp, String... args);
    }
}
