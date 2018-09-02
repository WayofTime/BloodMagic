package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.command.CommandBloodMagic;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

public class SubCommandOrb extends CommandTreeBase {
    //TODO: localized strings
    public SubCommandOrb() {
        addSubcommand(new Get());
        addSubcommand(new Set());
        addSubcommand(new CommandTreeHelp(this));
    }

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

    abstract class OrbCommand extends CommandTreeBase {
        public String help = TextHelper.localizeEffect("commands.bloodmagic.orb." + getName() + ".help", getInfo());

        public EntityPlayerMP player;
        public String uuid;
        public SoulNetwork network;

        @Override
        public String getUsage(ICommandSender sender) {
            return TextHelper.localizeEffect("commands.bloodmagic.orb." + getName() + ".usage") + "\n" + help;
        }

        public Object getInfo() {
            return null;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            player = args.length < 2 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
            uuid = PlayerHelper.getUUIDFromPlayer(player).toString();
            network = NetworkHelper.getSoulNetwork(uuid);
        }
    }

    class Get extends OrbCommand {

        @Override
        public String getName() {
            return "get";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            super.execute(server, sender, args);
            sender.sendMessage(new TextComponentString(TextHelper.localizeEffect("message.orb.currenttier", network.getOrbTier())));
        }
    }

    class Set extends OrbCommand {

        @Override
        public String getName() {
            return "set";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            super.execute(server, sender, args);
            if (args.length == 1 && Utils.isInteger(args[0]))
                network.setOrbTier(Integer.parseInt(args[0]));
            else if (args.length == 2 && Utils.isInteger(args[1]))
                network.setOrbTier(Integer.parseInt(args[1]));
            else {
                CommandBloodMagic.displayErrorString(sender, "commands.bloodmagic.error.arg.invalid");
                CommandBloodMagic.displayHelpString(sender, this.getUsage(sender));
                return;
            }
            CommandBloodMagic.displaySuccessString(sender, "commands.bloodmagic.success");
        }
    }
}
