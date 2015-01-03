package com.arc.bloodarsenal.commands;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class CommandUnbind extends CommandBase
{
    public CommandUnbind() {}

    public String getCommandName()
    {
        return "unbind";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "commands.unbind.usage";
    }

    public void processCommand(ICommandSender iCommandSender, String[] astring)
    {
        EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(iCommandSender);
        ItemStack item = entityplayermp.getCurrentEquippedItem();

        if (item != null && item.getItem() instanceof IBindable)
        {
            if (!EnergyItems.getOwnerName(item).isEmpty())
            {
                item.stackTagCompound.setString("ownerName", "");
                func_152373_a(iCommandSender, this, "commands.unbind.success", new Object[0]);
            }
            else
            {
                throw new CommandException("commands.unbind.failed.notBindable", new Object[0]);
            }
        }
        else if (!(item.getItem() instanceof IBindable))
        {
            throw new CommandException("commands.unbind.failed.notBindable", new Object[0]);
        }
    }
}
