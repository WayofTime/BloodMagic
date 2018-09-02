package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.command.CommandBloodMagic;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
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

public class SubCommandNetwork extends CommandTreeBase {
    public SubCommandNetwork() {
        addSubcommand(new Syphon());
        addSubcommand(new Add());
        addSubcommand(new Set());
        addSubcommand(new Get());
        addSubcommand(new Cap());
        addSubcommand(new Fill());
        addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public String getName() {
        return "network";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return TextHelper.localizeEffect("commands.bloodmagic.network.usage");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    public Integer commandHelperAmount(ICommandSender sender, String[] args) {
        int amount;
        if (args.length == 0)
            amount = 1000;
        else if (Utils.isInteger(args[0]))
            amount = Integer.parseInt(args[0]);
        else if (args.length > 1 && Utils.isInteger(args[1]))
            amount = Integer.parseInt(args[1]);
        else {
            CommandBloodMagic.displayErrorString(sender, "commands.bloodmagic.error.arg.invalid");
            CommandBloodMagic.displayHelpString(sender, this.getUsage(sender));
            return null;
        }
        if (amount < 0) {
            CommandBloodMagic.displayErrorString(sender, "commands.bloodmagic.error.negative");
            return null;
        }
        return amount;
    }

    abstract class NetworkCommand extends CommandTreeBase {
        public String help = TextHelper.localizeEffect("commands.bloodmagic.network." + getName() + ".help", getInfo());

        public EntityPlayerMP player;
        public SoulNetwork network;
        public String uuid;

        @Override
        public String getUsage(ICommandSender sender) {
            return TextHelper.localizeEffect("commands.bloodmagic.network." + getName() + ".usage") + "\n" + help;
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

    class Syphon extends NetworkCommand {
        @Override
        public String getName() {
            return "syphon";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            super.execute(server, sender, args);
            Integer amount = commandHelperAmount(sender, args);
            if (amount == null)
                return;
            int currE = network.getCurrentEssence();
            if (amount > currE) {
                CommandBloodMagic.displayErrorString(sender, "commands.bloodmagic.network.syphon.amountTooHigh");
                if (currE == 0)
                    return;
                amount = Math.min(amount, currE);
            }
            network.syphonAndDamage(player, SoulTicket.command(sender, this.getName(), amount));
            int newE = network.getCurrentEssence();
            CommandBloodMagic.displaySuccessString(sender, "commands.bloodmagic.network.syphon.success", currE - newE, player.getDisplayName().getFormattedText());
        }
    }

    class Add extends NetworkCommand {
        @Override
        public String getName() {
            return "add";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            super.execute(server, sender, args);
            Integer amount = commandHelperAmount(sender, args);
            if (amount == null)
                return;
            CommandBloodMagic.displaySuccessString(sender, "commands.bloodmagic.network.add.success", network.add(SoulTicket.command(sender, getName(), amount), NetworkHelper.getMaximumForTier(network.getOrbTier())), player.getDisplayName().getFormattedText());
        }
    }

    class Set extends NetworkCommand {
        @Override
        public String getName() {
            return "set";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            super.execute(server, sender, args);
            Integer amount = commandHelperAmount(sender, args);
            if (amount == null)
                return;
            network.setCurrentEssence(amount);
            CommandBloodMagic.displaySuccessString(sender, "commands.bloodmagic.network.set.success", player.getDisplayName().getFormattedText(), amount);
        }
    }

    class Get extends NetworkCommand {

        @Override
        public String getName() {
            return "get";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            super.execute(server, sender, args);
            sender.sendMessage(new TextComponentString((player != sender ? player.getDisplayName().getFormattedText() + " " : "") + TextHelper.localizeEffect("tooltip.bloodmagic.sigil.divination.currentEssence", network.getCurrentEssence())));
        }
    }

    class Cap extends NetworkCommand {

        @Override
        public String getName() {
            return "cap";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            super.execute(server, sender, args);
            network.setCurrentEssence(NetworkHelper.getMaximumForTier(network.getOrbTier()));
            CommandBloodMagic.displaySuccessString(sender, "commands.bloodmagic.network.cap.success", player.getDisplayName().getFormattedText());
        }
    }

    class Fill extends NetworkCommand {

        @Override
        public Integer getInfo() {
            return Integer.MAX_VALUE;
        }

        @Override
        public String getName() {
            return "fill";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            super.execute(server, sender, args);
            network.setCurrentEssence(Integer.MAX_VALUE);
            CommandBloodMagic.displaySuccessString(sender, "commands.bloodmagic.network.fill.success", player.getDisplayName().getFormattedText());
        }
    }
}