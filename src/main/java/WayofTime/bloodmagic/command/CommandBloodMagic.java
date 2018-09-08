package WayofTime.bloodmagic.command;

import WayofTime.bloodmagic.command.sub.SubCommandBind;
import WayofTime.bloodmagic.command.sub.SubCommandNetwork;
import WayofTime.bloodmagic.command.sub.SubCommandOrb;
import WayofTime.bloodmagic.command.sub.SubCommandRitual;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

public class CommandBloodMagic extends CommandTreeBase {
    //TODO: TextComponentHelper for strings, remove the 1-liner identical helper classes
    public CommandBloodMagic() {
        addSubcommand(new SubCommandBind());
        addSubcommand(new SubCommandNetwork());
        addSubcommand(new SubCommandOrb());
        addSubcommand(new SubCommandRitual());
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
