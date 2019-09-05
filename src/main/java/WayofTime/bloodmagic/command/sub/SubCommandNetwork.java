package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

import java.util.List;

public class SubCommandNetwork extends CommandTreeBase {

    public SubCommandNetwork() {
        addSubcommand(new Syphon());
        addSubcommand(new Add());
        addSubcommand(new Set());
        addSubcommand(new Get());
        addSubcommand(new Cap());
        addSubcommand(new Fill());
        addSubcommand(new Tickets());
        addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public String getName() {
        return "network";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return "commands.bloodmagic.network.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    abstract class NetworkCommand extends CommandTreeBase {

        public EntityPlayerMP player;
        public SoulNetwork network;
        public String uuid;

        @Override
        public int getRequiredPermissionLevel() {
            return 2;
        }

        public Integer commandHelperAmount(MinecraftServer server, ICommandSender sender, String[] args) {
            int amount;
            if (args.length == 0)
                amount = 1000;
            else if (Utils.isInteger(args[0]))
                amount = Integer.parseInt(args[0]);
            else if (args.length > 1 && Utils.isInteger(args[1]))
                amount = Integer.parseInt(args[1]);
            else {
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.error.arg.invalid"));
                sender.sendMessage(new TextComponentTranslation(this.getUsage(sender)));
                return null;
            }
            if (amount < 0) {
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.error.negative"));
                return null;
            }
            return amount;
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "commands.bloodmagic.network." + getName() + ".usage";
        }

        public String getHelp() {
            return "commands.bloodmagic.network." + getName() + ".help";
        }

        public String getInfo() {
            return "";
        }

        @Override
        public final void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (args.length == 1 && (args[0].equals("?") || args[0].equals("help"))) {
                sender.sendMessage(new TextComponentTranslation(getHelp()));
                return;
            }
            if (!getName().equals("get")) {
                this.player = args.length < 2 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
                this.uuid = PlayerHelper.getUUIDFromPlayer(player).toString();
                this.network = NetworkHelper.getSoulNetwork(uuid);
            }
            subExecute(server, sender, args);
        }

        protected abstract void subExecute(MinecraftServer server, ICommandSender sender, String... args) throws CommandException;
    }

    class Syphon extends NetworkCommand {
        @Override
        public String getName() {
            return "syphon";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            Integer amount = commandHelperAmount(server, sender, args);
            if (amount == null)
                return;
            int currE = network.getCurrentEssence();
            if (amount > currE) {
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.network.syphon.amountTooHigh"));
                if (currE == 0)
                    return;
                amount = Math.min(amount, currE);
            }
            network.syphonAndDamage(player, SoulTicket.command(sender, this.getName(), amount));
            int newE = network.getCurrentEssence();
            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.network.syphon.success", currE - newE, player.getDisplayName().getFormattedText()));
        }
    }

    class Add extends NetworkCommand {
        @Override
        public String getName() {
            return "add";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            Integer amount = commandHelperAmount(server, sender, args);
            if (amount == null)
                return;
            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.network.add.success", network.add(SoulTicket.command(sender, getName(), amount), NetworkHelper.getMaximumForTier(network.getOrbTier())), player.getDisplayName().getFormattedText()));
        }
    }

    class Set extends NetworkCommand {
        @Override
        public String getName() {
            return "set";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            Integer amount = commandHelperAmount(server, sender, args);
            if (amount == null)
                return;
            network.setCurrentEssence(amount);
            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.network.set.success", player.getDisplayName().getFormattedText(), amount));
        }
    }

    class Get extends NetworkCommand {

        @Override
        public String getName() {
            return "get";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            this.player = args.length < 1 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
            this.uuid = PlayerHelper.getUUIDFromPlayer(player).toString();
            this.network = NetworkHelper.getSoulNetwork(uuid);
            sender.sendMessage(new TextComponentString((player != sender ? player.getDisplayName().getFormattedText() + " " : "" + new TextComponentTranslation("tooltip.bloodmagic.sigil.divination.currentEssence", network.getCurrentEssence()).getFormattedText())));
        }
    }

    class Cap extends NetworkCommand {

        @Override
        public String getName() {
            return "cap";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            network.setCurrentEssence(NetworkHelper.getMaximumForTier(network.getOrbTier()));
            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.network.cap.success", player.getDisplayName().getFormattedText()));
        }
    }

    class Fill extends NetworkCommand {

        @Override
        public String getInfo() {
            return "" + Integer.MAX_VALUE;
        }

        @Override
        public String getName() {
            return "fill";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            network.setCurrentEssence(Integer.MAX_VALUE);
            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.network.fill.success", player.getDisplayName().getFormattedText()));
        }
    }

    class Tickets extends NetworkCommand {

        @Override
        public String getName() {
            return "tickethistory";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            List<SoulTicket> tickethistory = network.getTicketHistory();
            if (!tickethistory.isEmpty())
                for (SoulTicket i : network.getTicketHistory())
                    sender.sendMessage(i.getDescription());
            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.success", player.getDisplayName().getFormattedText()));
        }
    }
}