package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.helper.RitualHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SubCommandRitual extends CommandTreeBase {
    public SubCommandRitual() {
        addSubcommand(new RitualCreate());
        addSubcommand(new RitualRepair());
        addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public String getName() {
        return "ritual";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    public TileMasterRitualStone getMRS(ICommandSender sender) {
        BlockPos pos = sender.getPosition().down();
        World world = sender.getEntityWorld();
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileMasterRitualStone) {
            return (TileMasterRitualStone) tile;
        }
        return null;
    }

    class RitualCreate extends CommandTreeBase {
        public List<String> ritualList = new ArrayList<>();

        public RitualCreate() {
            for (Ritual ritual : BloodMagic.RITUAL_MANAGER.getRituals()) {
                ritualList.add(BloodMagic.RITUAL_MANAGER.getId(ritual));
            }
        }

        @Override
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
            return ritualList;
        }

        @Override
        public String getName() {
            return "create";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String... args) throws CommandException {
            if (args.length == 0) {
                sender.sendMessage(new TextComponentTranslation("commands.blooodmagic.ritual.create.noRitual"));
                return;
            } else if (args.length == 2 && (args[1].equals("help") || args[1].equals("?"))) {
                sender.sendMessage(new TextComponentTranslation(BloodMagic.RITUAL_MANAGER.getRitual(args[0]).getTranslationKey() + ".info"));
                return;
            }
            EntityPlayerMP player = args.length < 3 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[1]);
            boolean safe = false;
            if (args.length > 1 && args.length < 4) {
                int k = args.length - 1;
                if (args[k].equals("true") || args[k].equals("false")) {
                    safe = Boolean.parseBoolean(args[k]);
                } else if (args[1].equals("safe"))
                    safe = true;
                else
                    player = getPlayer(server, sender, args[1]);
            }

            BlockPos pos = player.getPosition().down();
            World world = player.getEntityWorld();
            EnumFacing direction = player.getHorizontalFacing();

            if (RitualHelper.createRitual(world, pos, direction, BloodMagic.RITUAL_MANAGER.getRitual(args[0]), safe))
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.success"));
            else if (!safe)
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.ritual.create.error.outOfWorldBoundaries"));
            else
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.ritaul.create.error.unsafe"));

        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "commands.bloodmagic.ritual.create.help";
        }
    }

    class RitualRepair extends CommandTreeBase {

        @Override
        public String getName() {
            return "repair";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "commands.bloodmagic.ritual.repair.usage";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String... args) throws CommandException {
            EntityPlayerMP player = args.length < 2 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
            TileMasterRitualStone tile = getMRS(player);
            boolean safe = false;
            if (args.length > 0 && args.length < 3) {
                int k = args.length - 1;
                if (args[k].equals("true") || args[k].equals("false")) {
                    safe = Boolean.parseBoolean(args[k]);
                } else if (args[0].equals("safe"))
                    safe = true;
            }
            if (tile != null)
                if (RitualHelper.repairRitualFromRuins(tile, safe))
                    sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.success"));
                else if (!safe)
                    sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.ritual.create.error.outOfWorldBoundaries"));
                else
                    sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.ritaul.create.error.unsafe"));
            else
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.ritual.error.noMRS"));
        }

    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
