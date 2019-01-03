package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;
import WayofTime.bloodmagic.teleport.Teleports;
import WayofTime.bloodmagic.tile.TileTeleposer;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

import java.util.HashSet;
import java.util.Set;

public class SubCommandTeleposer extends CommandTreeBase {
    public static final Set<TileTeleposer> teleposerSet = new HashSet<>(); //contains "valid" teleposers (teleposers with focus), teleposers check themselves every 100 ticks.
    public static TileTeleposer[] teleposerArray;

    public SubCommandTeleposer() {
        addSubcommand(new OutputTeleposerList());
        addSubcommand(new TeleportToTeleposer());
        addSubcommand(new TeleportToTeleposerFocus());
        addSubcommand(new RemoveTeleposer());
        addSubcommand(new RecursiveRemoveTeleposer());
        addSubcommand(new RemoveAllOfPlayer());
        addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }


    @Override
    public String getName() {
        return "teleposer";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.bloodmagic.teleposer.usage";
    }

    abstract class TeleposeHelper extends CommandTreeBase {
        public EntityPlayer player;

        @Override
        public String getUsage(ICommandSender sender) {
            return "commands.bloodmagic.teleposer." + getName() + ".usage";
        }

        public String getHelp() {
            return "commands.bloodmagic.teleposer." + getName() + ".help";
        }

        public String getInfo() {
            return "";
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 2;
        }

        public TileTeleposer[] cleanUpAndCreateArrayFromTeleposerList(EntityPlayer player) {
            if (player == null)
                for (TileTeleposer i : teleposerSet) {
                    if (i == null || i.isInvalid() || i.isEmpty()) {
                        teleposerSet.remove(i);
                    }
                }
            else {
                for (TileTeleposer i : teleposerSet) {
                    if (i == null || i.isInvalid() || i.isEmpty()) {
                        teleposerSet.remove(i);
                        continue;
                    }
                    ItemStack stack = i.getStackInSlot(0);
                    ItemTelepositionFocus focus = (ItemTelepositionFocus) stack.getItem();
                    Binding binding = focus.getBinding(stack);
                    if (binding != null && !binding.getOwnerName().equals(player.getName())) {
                        teleposerSet.remove(i);
                    }
                }
            }
            teleposerArray = teleposerSet.toArray(new TileTeleposer[0]);
            return teleposerArray;

        }

        public void sendOwnedTeleposerList(ICommandSender sender, EntityPlayer player) {
            teleposerArray = cleanUpAndCreateArrayFromTeleposerList(player);
            for (int i = 0; i < teleposerArray.length; i++) {
                ItemStack stack = teleposerArray[i].getStackInSlot(0);
                ItemTelepositionFocus focus = (ItemTelepositionFocus) stack.getItem();
                Binding binding = focus.getBinding(stack);
                if (binding != null) {
                    String name = binding.getOwnerName();
                    if (player != null) {
                        if (name.equals(player.getName()))
                            sender.sendMessage(new TextComponentString(i + new TextComponentTranslation("commands.bloodmagic.teleposer.anddimension").getFormattedText() + teleposerArray[i].getWorld().provider.getDimension() + " " + teleposerArray[i].getPos() + " " + new TextComponentTranslation("commands.bloodmagic.teleposer.focusanddim").getFormattedText() + " " + focus.getWorld(stack).provider.getDimension() + " " + focus.getBlockPos(stack) + " " + new TextComponentTranslation("commands.bloodmagic.teleposer.owner").getFormattedText() + " " + name));
                    } else
                        sender.sendMessage(new TextComponentString(i + new TextComponentTranslation("commands.bloodmagic.teleposer.anddimension").getFormattedText() + teleposerArray[i].getWorld().provider.getDimension() + " " + teleposerArray[i].getPos() + " " + new TextComponentTranslation("commands.bloodmagic.teleposer.focusanddim").getFormattedText() + " " + focus.getWorld(stack).provider.getDimension() + " " + focus.getBlockPos(stack) + " " + new TextComponentTranslation("commands.bloodmagic.teleposer.owner").getFormattedText() + " " + name));
                }
            }
        }

        public Integer getIDFromArgs(ICommandSender sender, String[] args) {
            int teleposerID;
            if (args.length == 0) {
                return null;
            }

            if (Utils.isInteger(args[0]))
                teleposerID = Integer.parseInt(args[0]);
            else if (args.length > 1 && Utils.isInteger(args[1]))
                teleposerID = Integer.parseInt(args[1]);
            else {
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.error.arg.invalid"));
                sender.sendMessage(new TextComponentTranslation(this.getUsage(sender)));
                return null;
            }
            if (teleposerID < 0) {
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.error.negative"));
                return null;
            }
            return teleposerID;
        }

        @Override
        public final void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (args.length == 1 && args[0].equals("?") || args[0].equals("help")) {
                sender.sendMessage(new TextComponentTranslation(getHelp()));
                return;
            }
            if (!(getName().equals("rmrf") || getName().equals("remove"))) {
                this.player = args.length < 2 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
            }
            subExecute(server, sender, args);
        }

        protected abstract void subExecute(MinecraftServer server, ICommandSender sender, String... args) throws CommandException;
    }

    class OutputTeleposerList extends TeleposeHelper {

        @Override
        public String getName() {
            return "list";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            sendOwnedTeleposerList(sender, player);
        }
    }

    class TeleportToTeleposer extends TeleposeHelper {

        @Override
        public String getName() {
            return "teleport";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            Integer teleposerID = getIDFromArgs(sender, args);
            if (teleposerID == null)
                sendOwnedTeleposerList(sender, null);
            else if (!sender.getEntityWorld().isRemote) {
                if (teleposerID > teleposerArray.length) {
                    sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.error.outofbounds"));
                    return;
                }
                TileTeleposer brunhilde = teleposerArray[teleposerID]; // every teleposer is a brunhilde!
                BlockPos brunhildePos = brunhilde.getPos();
                World brunhildeWorld = brunhilde.getWorld();
                if (player.getEntityWorld().equals(brunhildeWorld))
                    new Teleports.TeleportSameDim(brunhildePos, player, player.getUniqueID(), false).teleport();
                else
                    new Teleports.TeleportToDim(brunhildePos, player, player.getUniqueID(), player.getEntityWorld(), brunhildeWorld.provider.getDimension(), false).teleport();
            } else
                return;

            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.success"));
        }
    }

    class TeleportToTeleposerFocus extends TeleposeHelper {

        @Override
        public String getName() {
            return "teleportfocus";
        }

        @Override
        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            Integer teleposerID = getIDFromArgs(sender, args);
            if (teleposerID == null)
                sendOwnedTeleposerList(sender, null);
            else if (!sender.getEntityWorld().isRemote) {
                if (teleposerID > teleposerArray.length) {
                    sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.outofbounds"));
                    return;
                }
                TileTeleposer brunhilde = teleposerArray[teleposerID]; // every teleposer is a brunhilde!
                ItemStack stack = brunhilde.getStackInSlot(0);
                ItemTelepositionFocus focus = (ItemTelepositionFocus) stack.getItem();
                BlockPos brunhildeFocusPos = focus.getBlockPos(stack);
                World brunhildeFocusWorld = focus.getWorld(stack);
                if (player.getEntityWorld().equals(brunhildeFocusWorld))
                    new Teleports.TeleportSameDim(brunhildeFocusPos, player, player.getUniqueID(), false).teleport();
                else
                    new Teleports.TeleportToDim(brunhildeFocusPos, player, player.getUniqueID(), player.getEntityWorld(), brunhildeFocusWorld.provider.getDimension(), false).teleport();
            } else
                return;

            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.success"));
        }
    }

    class RemoveTeleposer extends TeleposeHelper {

        @Override
        public String getName() {
            return "remove";
        }

        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            Integer teleposerID = getIDFromArgs(sender, args);
            if (teleposerID == null)
                sendOwnedTeleposerList(sender, null);
            else if (!sender.getEntityWorld().isRemote) {
                if (teleposerID > teleposerArray.length) {
                    sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.outofbounds"));
                    return;
                }
                TileTeleposer brunhilde = teleposerArray[teleposerID]; // every teleposer is a brunhilde!
                BlockPos brunhildePos = brunhilde.getPos();
                World brunhildeWorld = brunhilde.getWorld();
                brunhildeWorld.setBlockToAir(brunhildePos);
                cleanUpAndCreateArrayFromTeleposerList(null);
            } else
                return;

            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.success"));
        }
    }

    class RecursiveRemoveTeleposer extends TeleposeHelper {

        @Override
        public String getName() {
            return "rmrf";
        }

        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            Integer teleposerID = getIDFromArgs(sender, args);
            if (teleposerID == null)
                sendOwnedTeleposerList(sender, null);
            else if (!sender.getEntityWorld().isRemote) {
                if (teleposerID > teleposerArray.length) {
                    sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.outofbounds"));
                    return;
                }
                TileTeleposer brunhilde = teleposerArray[teleposerID]; // every teleposer is a brunhilde!
                do {
                    BlockPos brunhildePos = brunhilde.getPos();
                    World brunhildeWorld = brunhilde.getWorld();
                    ItemStack stack = brunhilde.getStackInSlot(0);
                    ItemTelepositionFocus focus = (ItemTelepositionFocus) stack.getItem();
                    BlockPos brunhildeFocusPos = focus.getBlockPos(stack);
                    World brunhildeFocusWorld = focus.getWorld(stack);
                    brunhilde.setInventorySlotContents(0, ItemStack.EMPTY);
                    brunhildeWorld.setBlockToAir(brunhildePos);
                    TileEntity testTile = brunhildeFocusWorld.getTileEntity(brunhildeFocusPos);
                    if (!(testTile instanceof TileTeleposer) || ((TileTeleposer) testTile).getStackInSlot(0).isEmpty())
                        break;
                    for (TileTeleposer i : teleposerArray) {
                        if (i.getPos().equals(brunhildeFocusPos)) {
                            brunhilde = i;
                            break;
                        }

                    }
                } while (true);
                cleanUpAndCreateArrayFromTeleposerList(null);
            } else
                return;

            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.success"));
        }

    }

    class RemoveAllOfPlayer extends TeleposeHelper {

        @Override
        public String getName() {
            return "removeall";
        }

        public void subExecute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            cleanUpAndCreateArrayFromTeleposerList(player);

            if (!sender.getEntityWorld().isRemote) {
                for (TileTeleposer i : teleposerArray) {
                    i.setInventorySlotContents(0, ItemStack.EMPTY);
                    i.getWorld().setBlockToAir(i.getPos());
                }

            }
            cleanUpAndCreateArrayFromTeleposerList(null);

            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.success"));

        }
    }
}
