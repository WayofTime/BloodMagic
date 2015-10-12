package WayofTime.alchemicalWizardry.common.commands.sub;

import WayofTime.alchemicalWizardry.api.command.SubCommandBase;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.Locale;

public class SubCommandNetwork extends SubCommandBase {

    public SubCommandNetwork(ICommand parent) {
        super(parent, "network");
    }

    @Override
    public String getArgUsage(ICommandSender commandSender) {
        return StatCollector.translateToLocal("commands.network.usage");
    }

    @Override
    public String getHelpText() {
        return StatCollector.translateToLocal("commands.network.help");
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
                                displaySuccessString(commandSender, "commands.network.syphon.success", amount, givenName);
                            } else {
                                displayErrorString(commandSender, "commands.error.arg.invalid");
                            }
                        } else {
                            displayErrorString(commandSender, "commands.error.arg.missing");
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
                                displaySuccessString(commandSender, "commands.network.add.success", amount, givenName);
                            } else {
                                displayErrorString(commandSender, "commands.error.arg.invalid");
                            }
                        } else {
                            displayErrorString(commandSender, "commands.error.arg.missing");
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
                                displaySuccessString(commandSender, "commands.network.set.success", givenName, amount);
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
                            commandSender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("message.divinationsigil.currentessence") + " " + SoulNetworkHandler.getCurrentEssence(givenName) + "LP"));

                        break;
                    }
                    case FILL: {
                        if (displayHelp) {
                            displayHelpString(commandSender, ValidCommands.FILL.help, Integer.MAX_VALUE);
                            break;
                        }

                        if (args.length > 1) {
                            SoulNetworkHandler.setCurrentEssence(givenName, Integer.MAX_VALUE);
                            displaySuccessString(commandSender, "commands.network.fill.success", givenName);
                        }

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
                            displaySuccessString(commandSender, "commands.network.cap.success", givenName);
                        }

                        break;
                    }
                }
            } catch (IllegalArgumentException e) {
                displayErrorString(commandSender, "commands.error.404");
            }
        }
    }

    private enum ValidCommands {
        SYPHON("commands.network.syphon.help"),
        ADD("commands.network.add.help"),
        SET("commands.network.set.help"),
        GET("commands.network.get.help"),
        FILL("commands.network.fill.help"),
        CAP("commands.network.cap.help");

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
