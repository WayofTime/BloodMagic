package WayofTime.bloodmagic.command.sub;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.command.SubCommandBase;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Strings;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class SubCommandBind extends SubCommandBase
{

    public SubCommandBind(ICommand parent)
    {
        super(parent, "bind");
    }

    @Override
    public String getArgUsage(ICommandSender commandSender)
    {
        return TextHelper.localizeEffect("commands.bind.usage");
    }

    @Override
    public String getHelpText()
    {
        return TextHelper.localizeEffect("commands.bind.help");
    }

    @Override
    public void processSubCommand(ICommandSender commandSender, String[] args)
    {
        super.processSubCommand(commandSender, args);

        if (commandSender.getEntityWorld().isRemote)
            return;

        try
        {
            EntityPlayer player = getCommandSenderAsPlayer(commandSender);
            String playerName = player.getName();
            String uuid = PlayerHelper.getUUIDFromPlayer(player).toString();
            ItemStack held = player.getHeldItem();
            boolean bind = true;

            if (held != null && held.getItem() instanceof IBindable)
            {
                if (args.length > 0)
                {

                    if (args[0].equalsIgnoreCase("help"))
                        return;

                    if (isBoolean(args[0]))
                    {
                        bind = Boolean.parseBoolean(args[0]);

                        if (args.length > 2)
                            playerName = args[1];
                    } else
                    {
                        playerName = args[0];
                        uuid = PlayerHelper.getUUIDFromPlayer(getPlayer(commandSender, playerName)).toString();
                    }
                }

                if (bind)
                {
                    BindableHelper.setItemOwnerName(held, playerName);
                    BindableHelper.setItemOwnerUUID(held, uuid);
                    commandSender.addChatMessage(new ChatComponentTranslation("commands.bind.success"));
                } else
                {
                    if (!Strings.isNullOrEmpty(((IBindable) held.getItem()).getOwnerUUID(held)))
                    {
                        held.getTagCompound().removeTag(Constants.NBT.OWNER_UUID);
                        held.getTagCompound().removeTag(Constants.NBT.OWNER_NAME);
                        commandSender.addChatMessage(new ChatComponentTranslation("commands.bind.remove.success"));
                    }
                }
            }
        } catch (PlayerNotFoundException e)
        {
            commandSender.addChatMessage(new ChatComponentText(TextHelper.localizeEffect("commands.error.404")));
        }
    }

    private boolean isBoolean(String string)
    {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false");
    }
}
