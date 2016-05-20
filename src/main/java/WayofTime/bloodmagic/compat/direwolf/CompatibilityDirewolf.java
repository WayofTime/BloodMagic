package WayofTime.bloodmagic.compat.direwolf;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.event.AltarCraftedEvent;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.compat.ICompatibility;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CompatibilityDirewolf implements ICompatibility
{
    @Override
    public void loadCompatibility(InitializationPhase phase)
    {
        if (phase == InitializationPhase.POST_INIT)
        {
            AltarRecipeRegistry.registerRecipe(new AltarRecipeRegistry.AltarRecipe(new ItemStack(Items.BREAD), new ItemStack(Items.BREAD), EnumAltarTier.ONE, 10, 1, 0));
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @Override
    public String getModId()
    {
        return Constants.Mod.MODID;
    }

    @Override
    public boolean enableCompat()
    {
        return true;
    }

    @SubscribeEvent
    public void onAltarCrafted(AltarCraftedEvent event)
    {
        if (event.getOutput().getItem() == Items.BREAD)
        {
            event.getOutput().setTagCompound(new NBTTagCompound());
            event.getOutput().setStackDisplayName(TextHelper.localizeEffect("secret.BloodMagic.bread.bloody"));
            event.getOutput().getTagCompound().setBoolean("bloody", true);
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event)
    {
        if (event.getItemStack().getItem() == Items.BREAD)
            if (event.getItemStack().hasTagCompound())
                if (event.getItemStack().getTagCompound().hasKey("bloody"))
                    event.getToolTip().add(TextHelper.localizeEffect("secret.BloodMagic.bread.bloody.desc"));
    }
}
