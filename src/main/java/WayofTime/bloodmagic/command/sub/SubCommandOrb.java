package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.registry.OrbRegistry;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

public class SubCommandOrb extends CommandTreeBase {
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
        return "commands.bloodmagic.orb.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    abstract class OrbCommand extends CommandTreeBase {

        public EntityPlayerMP player;
        public String uuid;
        public SoulNetwork network;
        public Object info;

        @Override
        public String getUsage(ICommandSender sender) {
            return "commands.bloodmagic.orb." + getName() + ".usage";
        }

        public String getHelp() {
            return "commands.bloodmagic.orb." + getName() + ".help";
        }

        public String getInfo() {
            return "";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (args.length == 1 && (args[0].equals("?") || args[0].equals("help"))) {
                sender.sendMessage(new TextComponentTranslation(getHelp()));
                return;
            }
            player = args.length < 2 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
            uuid = PlayerHelper.getUUIDFromPlayer(player).toString();
            network = NetworkHelper.getSoulNetwork(uuid);

            subExecute(server, sender, args);
        }

        protected abstract void subExecute(MinecraftServer server, ICommandSender sender, String... args) throws CommandException;
    }

    class Get extends OrbCommand {

        @Override
        public String getName() {
            return "get";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            super.execute(server, sender, args);
            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.orb.currenttier", network.getOrbTier()));
        }
    }

    class Set extends OrbCommand {
        //TODO: check whether maxTier check works with custom Blood Orbs
        int maxTier = OrbRegistry.getTierMap().size() - 1;

        @Override
        public String getInfo() {
            return "" + maxTier;
        }

        @Override
        public String getName() {
            return "set";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            super.execute(server, sender, args);

            int targetTier;
            if (args.length == 1 && Utils.isInteger(args[0]))
                targetTier = Integer.parseInt(args[0]);
            else if (args.length == 2 && Utils.isInteger(args[1]))
                targetTier = Integer.parseInt(args[1]);
            else {
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.error.arg.invalid"));
                sender.sendMessage(new TextComponentTranslation(this.getUsage(sender)));
                return;
            }
            if (targetTier < 0) {
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.error.negative"));
                return;
            } else if (targetTier > maxTier) {
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.orb.error.tierTooHigh", getInfo()));
                return;
            }
            network.setOrbTier(targetTier);
            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.success"));
        }
    }
}
