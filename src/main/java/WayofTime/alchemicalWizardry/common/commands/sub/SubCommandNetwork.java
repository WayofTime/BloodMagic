package WayofTime.alchemicalWizardry.common.commands.sub;

import WayofTime.alchemicalWizardry.api.command.SubCommandBase;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.Locale;

public class SubCommandNetwork extends SubCommandBase {

    public SubCommandNetwork(ICommand parent) {
        super(parent, "network");
    }

    @Override
    public String getArgUsage(ICommandSender commandSender) {
        return "/bloodmagic network [syphon|add|get|fill|cap] player [amount]";
    }

    @Override
    public String getHelpText() {
        return "LP network utilities";
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
            EntityPlayer player = getPlayer(commandSender, givenName);

            if (args.length > 1) {
                givenName = args[1];
                player = getPlayer(commandSender, givenName);
            }

            boolean displayHelp = isBounded(0, 2, args.length);

            try {
                switch (ValidCommands.valueOf(args[0].toUpperCase(Locale.ENGLISH))) {
                    case SYPHON: {
                        if (displayHelp) {
                            displayHelpString(commandSender, ValidCommands.SYPHON.help);
                            break;
                        }

                        if (args.length == 3) {
                            if (isInteger(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                SoulNetworkHandler.syphonAndDamageFromNetwork(givenName, player, amount);
                            } else {
                                displayErrorString(commandSender, "Invalid arguments");
                            }
                        } else {
                            displayErrorString(commandSender, "Not enough arguments");
                        }

                        break;
                    }
                    case ADD: {
                        if (displayHelp) {
                            displayHelpString(commandSender, ValidCommands.ADD.help);
                            break;
                        }

                        if (args.length == 3) {
                            if (isInteger(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                int maxOrb = SoulNetworkHandler.getMaximumForOrbTier(SoulNetworkHandler.getCurrentMaxOrb(givenName));
                                SoulNetworkHandler.addCurrentEssenceToMaximum(givenName, amount, maxOrb);
                            } else {
                                displayErrorString(commandSender, "Invalid arguments");
                            }
                        } else {
                            displayErrorString(commandSender, "Not enough arguments");
                        }

                        break;
                    }
                    case SET: {
                        if (displayHelp) {
                            displayHelpString(commandSender, ValidCommands.SET.help);
                            break;
                        }

                        if (args.length == 3) {
                            if (isInteger(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                SoulNetworkHandler.setCurrentEssence(givenName, amount);
                            } else {
                                displayErrorString(commandSender, "Invalid arguments");
                            }
                        } else {
                            displayErrorString(commandSender, "Not enough arguments");
                        }
                    }
                    case GET: {
                        if (displayHelp) {
                            displayHelpString(commandSender, ValidCommands.GET.help);
                            break;
                        }

                        if (args.length > 1)
                            commandSender.addChatMessage(new ChatComponentText("Current Essence: " + SoulNetworkHandler.getCurrentEssence(givenName)));

                        break;
                    }
                    case FILL: {
                        if (displayHelp) {
                            displayHelpString(commandSender, ValidCommands.FILL.help);
                            break;
                        }

                        if (args.length > 1)
                            SoulNetworkHandler.setCurrentEssence(givenName, Integer.MAX_VALUE);

                        break;
                    }
                    case CAP: {
                        if (displayHelp) {
                            displayHelpString(commandSender, ValidCommands.CAP.help);
                            break;
                        }

                        if (args.length > 1) {
                            int maxOrb = SoulNetworkHandler.getMaximumForOrbTier(SoulNetworkHandler.getCurrentMaxOrb(givenName));
                            SoulNetworkHandler.setCurrentEssence(givenName, maxOrb);
                        }

                        break;
                    }
                }
            } catch (IllegalArgumentException e) {
                displayErrorString(commandSender, "Command not found!");
            }
        }
    }

    private enum ValidCommands {
        SYPHON("Removes the given amount of LP from the given player's LP network."),
        ADD("Adds the given amount of LP to the given player's LP network."),
        SET("Sets the given player's LP to the given amount"),
        GET("Returns the amount of LP in the given player's LP network."),
        FILL(String.format("Fills the given player's LP network to %d", Integer.MAX_VALUE)),
        CAP("Fills the given player's LP network to the max that their highest Blood Orb can store.");

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
