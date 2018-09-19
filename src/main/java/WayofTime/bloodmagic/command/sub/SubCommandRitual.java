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

public class SubCommandRitual extends CommandTreeBase {
    public SubCommandRitual() {
        addSubcommand(new RitualCreate());
        addSubcommand(new RitualRemove());
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
        public RitualCreate() {
            for (Ritual i : BloodMagic.RITUAL_MANAGER.getRituals()) {
                addSubcommand(new CommandTreeBase() {
                    public Ritual ritual = i;

                    @Override
                    public String getName() {
                        return this.ritual.getName();
                    }

                    @Override
                    public String getUsage(ICommandSender sender) {
                        return new TextComponentTranslation(this.ritual.getUnlocalizedName() + ".info").getFormattedText();
                    }

                    @Override
                    public void execute(MinecraftServer server, ICommandSender sender, String... args) throws CommandException {
                        EntityPlayerMP player = args.length < 2 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
                        boolean safe = false;
                        if (args.length > 0 && args.length < 3) {
                            int k = args.length - 1;
                            if (args[k].equals("true") || args[k].equals("false")) {
                                safe = Boolean.parseBoolean(args[k]);
                            } else if (args[0].equals("safe"))
                                safe = true;
                        }

                        BlockPos pos = player.getPosition().down();
                        World world = player.getEntityWorld();
                        EnumFacing direction = EnumFacing.NORTH;

                        if (RitualHelper.createRitual(world, pos, direction, this.ritual, safe))
                            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.success"));
                        else
                            sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.ritual.create.error.outOfWorldBoundaries"));

                    }
                });
                addSubcommand(new CommandTreeHelp(this));
            }
        }

        @Override
        public String getName() {
            return "create";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return new TextComponentTranslation("commands.bloodmagic.ritual.create.help").getFormattedText();
        }
    }

    class RitualRemove extends CommandTreeBase {

        @Override
        public String getName() {
            return "remove";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return new TextComponentTranslation("commands.bloodmagic.ritual.remove.help").getFormattedText();
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String... args) throws CommandException {
            EntityPlayerMP player = args.length < 2 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
            TileMasterRitualStone tile = getMRS(player);
            if (tile != null)
                RitualHelper.removeRitualFromRuins(tile);
            else
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.ritual.error.noMRS"));
        }
    }

    class RitualRepair extends CommandTreeBase {

        @Override
        public String getName() {
            return "repair";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return null;
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
                else
                    sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.ritual.create.error.outOfWorldBoundaries"));
            else
                sender.sendMessage(new TextComponentTranslation("commands.bloodmagic.ritual.error.noMRS"));
        }

    }
}
