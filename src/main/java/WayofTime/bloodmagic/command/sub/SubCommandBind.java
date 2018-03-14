package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.util.helper.BindableHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class SubCommandBind extends CommandBase {
    @Override
    public String getName() {
        return "bind";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return TextHelper.localizeEffect("commands.bind.usage");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) throws CommandException {
        if (commandSender.getEntityWorld().isRemote)
            return;

        try {
            EntityPlayer player = CommandBase.getCommandSenderAsPlayer(commandSender);
            ItemStack held = player.getHeldItemMainhand();
            boolean bind = true;

            if (held.getItem() instanceof IBindable) {
                if (args.length > 0) {

                    if (args[0].equalsIgnoreCase("help"))
                        return;

                    if (isBoolean(args[0])) {
                        bind = Boolean.parseBoolean(args[0]);

                        if (args.length > 2)
                            player = CommandBase.getPlayer(server, commandSender, args[1]);
                    } else {
                        player = CommandBase.getPlayer(server, commandSender, args[0]);
                    }
                }

                if (bind) {
                    Binding binding = new Binding(player.getGameProfile().getId(), player.getGameProfile().getName());
                    BindableHelper.applyBinding(held, binding);
                    commandSender.sendMessage(new TextComponentTranslation("commands.bind.success"));
                } else {
                    Binding binding = ((IBindable) held.getItem()).getBinding(held);
                    if (binding != null) {
                        held.getTagCompound().removeTag("binding");
                        commandSender.sendMessage(new TextComponentTranslation("commands.bind.remove.success"));
                    }
                }
            }
        } catch (PlayerNotFoundException e) {
            commandSender.sendMessage(new TextComponentTranslation(TextHelper.localizeEffect("commands.error.404")));
        }
    }

    private boolean isBoolean(String string) {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
    }
}
