package WayofTime.bloodmagic.command;

import WayofTime.bloodmagic.command.sub.*;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

public class CommandBloodMagic extends CommandTreeBase {

    public CommandBloodMagic() {
        addSubcommand(new SubCommandBind());
        addSubcommand(new SubCommandNetwork());
        addSubcommand(new SubCommandOrb());
        addSubcommand(new SubCommandRitual());
        addSubcommand(new SubCommandTeleposer());
        addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public String getName() {
        return "bloodmagic";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/bloodmagic help";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

}
