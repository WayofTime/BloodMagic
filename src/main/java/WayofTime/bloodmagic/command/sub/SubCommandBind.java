package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.util.helper.BindableHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

public class SubCommandBind extends CommandTreeBase {
    public ServerPlayerEntity player;

    public SubCommandBind() {
        addSubcommand(new CommandTreeHelp(this));
    }

    public String getInfo() {
        return player.getName();
    }

    @Override
    public String getName() {
        return "bind";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return "commands.bloodmagic.bind.usage";
    }

    public String getHelp() {
        return "commands.bloodmagic.bind.help";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && (args[0].equals("?") || args[0].equals("help"))) {
            sender.sendMessage(new TranslationTextComponent(getHelp()));
            return;
        }
        if (sender.getEntityWorld().isRemote)
            return;
        ServerPlayerEntity player = args.length < 2 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
        ItemStack held = player.getHeldItemMainhand();
        boolean bind = true;
        if (held.getItem() instanceof IBindable) {
            Binding binding = ((IBindable) held.getItem()).getBinding(held);
            if (binding != null)
                bind = false;
            if (args.length < 2)
                if (args.length == 1)
                    if (isBoolean(args[0]))
                        bind = Boolean.parseBoolean(args[0]);
                    else
                        player = getPlayer(server, sender, args[0]);
            if (bind) {
                if (binding.getOwnerName().equals(player.getName())) {
                    sender.sendMessage(new TranslationTextComponent("commands.bloodmagic.bind.error.ownerEqualsTarget"));
                    return;
                }
                binding = new Binding(player.getGameProfile().getId(), player.getGameProfile().getName());
                BindableHelper.applyBinding(held, binding);
                this.player = player;
                sender.sendMessage(new TranslationTextComponent("commands.bloodmagic.bind.success", getInfo()));
            } else {
                if (binding == null) {
                    sender.sendMessage(new TranslationTextComponent("commands.bloodmagic.bind.error.notBound"));
                }
                held.getTagCompound().removeTag("binding");
                sender.sendMessage(new TranslationTextComponent("commands.bloodmagic.bind.remove.success"));
            }
        } else
            sender.sendMessage(new TranslationTextComponent("commands.bloodmagic.bind.error.notBindable"));


    }

    private boolean isBoolean(String string) {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
    }
}