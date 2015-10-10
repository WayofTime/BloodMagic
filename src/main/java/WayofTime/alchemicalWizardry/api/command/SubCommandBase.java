package WayofTime.alchemicalWizardry.api.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.Locale;

public abstract class SubCommandBase implements ISubCommand {

    private ICommand parent;
    private String name;

    public SubCommandBase(ICommand parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    @Override
    public String getSubCommandName() {
        return name;
    }

    @Override
    public ICommand getParentCommand() {
        return parent;
    }

    @Override
    public boolean canSenderUseSubCommand(ICommandSender commandSender) {
        return commandSender.canCommandSenderUseCommand(getRequiredPermissionLevel(), "op");
    }

    @Override
    public void processSubCommand(ICommandSender commandSender, String[] args) {

        if (args.length == 0 && !getSubCommandName().equals("help"))
            displayErrorString(commandSender, String.format("%s - %s", capitalizeFirstLetter(getSubCommandName()), getArgUsage(commandSender)));

        if (isBounded(0, 2, args.length) && args[0].equals("help"))
            displayHelpString(commandSender, String.format("%s - %s", capitalizeFirstLetter(getSubCommandName()), getHelpText()));
    }

    protected EntityPlayerMP getCommandSenderAsPlayer(ICommandSender commandSender) {
        if (commandSender instanceof EntityPlayerMP)
            return (EntityPlayerMP)commandSender;
        else
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.");
    }

    protected EntityPlayerMP getPlayer(ICommandSender commandSender, String playerName) {
        EntityPlayerMP entityplayermp = PlayerSelector.matchOnePlayer(commandSender, playerName);

        if (entityplayermp != null)
            return entityplayermp;
        else {
            entityplayermp = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);

            if (entityplayermp == null)
                throw new PlayerNotFoundException();
            else
                return entityplayermp;
        }
    }

    protected String capitalizeFirstLetter(String toCapital) {
        return String.valueOf(toCapital.charAt(0)).toUpperCase(Locale.ENGLISH) + toCapital.substring(1);
    }

    protected boolean isBounded(int low, int high, int given) {
        return given > low && given < high;
    }

    protected void displayHelpString(ICommandSender commandSender, String display) {
        commandSender.addChatMessage(new ChatComponentText(display).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
    }

    protected void displayErrorString(ICommandSender commandSender, String display) {
        commandSender.addChatMessage(new ChatComponentText(display).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }
}
