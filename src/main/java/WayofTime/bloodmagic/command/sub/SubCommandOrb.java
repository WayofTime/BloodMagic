package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
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

public class SubCommandOrb extends CommandBase {
    @Override
    public String getName() {
        return "orb";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return TextHelper.localizeEffect("commands.bloodmagic.orb.usage");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException {
        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("help"))
                return;

            try {
                String givenName = commandSender.getName();

                if (args.length > 1)
                    givenName = args[1];

                EntityPlayer player = CommandBase.getPlayer(server, commandSender, givenName);
                String uuid = PlayerHelper.getUUIDFromPlayer(player).toString();
                SoulNetwork network = NetworkHelper.getSoulNetwork(uuid);

                boolean displayHelp = args.length > 0 && args.length < 2;

                try {
                    switch (ValidCommands.valueOf(args[0].toUpperCase(Locale.ENGLISH))) {
                        case SET: {
                            if (displayHelp) {
                                CommandBloodMagic.displayHelpString(commandSender, ValidCommands.SET.help);
                                break;
                            }

                            if (args.length == 3) {
                                if (Utils.isInteger(args[2])) {
                                    int amount = Integer.parseInt(args[2]);
                                    network.setOrbTier(amount);
                                    CommandBloodMagic.displaySuccessString(commandSender, "commands.bloodmagic.success");
                                } else {
                                    CommandBloodMagic.displayErrorString(commandSender, "commands.bloodmagic.error.arg.invalid");
                                }
                            } else {
                                CommandBloodMagic.displayErrorString(commandSender, "commands.bloodmagic.error.arg.missing");
                            }

                            break;
                        }
                        case GET: {
                            if (displayHelp) {
                                CommandBloodMagic.displayHelpString(commandSender, ValidCommands.GET.help);
                                break;
                            }

                            if (args.length > 1)
                                commandSender.sendMessage(new TextComponentString(TextHelper.localizeEffect("message.orb.currenttier", network.getOrbTier())));

                            break;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    CommandBloodMagic.displayErrorString(commandSender, "commands.bloodmagic.error.404");
                }
            } catch (PlayerNotFoundException e) {
                CommandBloodMagic.displayErrorString(commandSender, "commands.bloodmagic.error.404");
            }
        }
    }

    private enum ValidCommands {
        SET("commands.bloodmagic.orb.set.help"),
        GET("commands.bloodmagic.orb.get.help");

        public String help;

        ValidCommands(String help) {
            this.help = help;
        }
    }
}
