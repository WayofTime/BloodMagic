package WayofTime.alchemicalWizardry.common.commands.sub;

import WayofTime.alchemicalWizardry.api.command.SubCommandBase;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.Locale;

public class SubCommandOrb extends SubCommandBase {

    public SubCommandOrb(ICommand parent) {
        super(parent, "orb");
    }

    @Override
    public String getArgUsage(ICommandSender commandSender) {
        return StatCollector.translateToLocal("commands.orb.usage");
    }

    @Override
    public String getHelpText() {
        return StatCollector.translateToLocal("commands.orb.help");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processSubCommand(ICommandSender commandSender, String[] args) {
        super.processSubCommand(commandSender, args);

        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("help"))
                return;

            String givenName = commandSender.getCommandSenderName();

            if (args.length > 1)
                givenName = args[1];

            boolean displayHelp = isBounded(0, 2, args.length);

            try {
                switch (ValidCommands.valueOf(args[0].toUpperCase(Locale.ENGLISH))) {
                    case SET: {
                        if (displayHelp) {
                            displayHelpString(commandSender, ValidCommands.SET.help);
                            break;
                        }

                        if (args.length == 3) {
                            if (isInteger(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                SoulNetworkHandler.setMaxOrbToMax(givenName, amount);
                                displaySuccessString(commandSender, "commands.success");
                            } else {
                                displayErrorString(commandSender, "commands.error.arg.invalid");
                            }
                        } else {
                            displayErrorString(commandSender, "commands.error.arg.missing");
                        }

                        break;
                    }
                    case GET: {
                        if (displayHelp) {
                            displayHelpString(commandSender, ValidCommands.GET.help);
                            break;
                        }

                        if (args.length > 1)
                            commandSender.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("message.orb.currenttier", SoulNetworkHandler.getCurrentMaxOrb(givenName))));

                        break;
                    }
                }
            } catch (IllegalArgumentException e) {
                displayErrorString(commandSender, "commands.error.404");
            }
        }
    }

    private enum ValidCommands {
        SET("commands.orb.set.help"),
        GET("commands.orb.get.help");

        public String help;

        ValidCommands(String help) {
            this.help = help;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
