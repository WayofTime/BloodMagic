package WayofTime.alchemicalWizardry.item.sigil;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.NBTHolder;
import WayofTime.alchemicalWizardry.api.iface.ISigil;
import WayofTime.alchemicalWizardry.util.helper.TextHelper;
import WayofTime.alchemicalWizardry.item.ItemBindable;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Getter
public class ItemSigilBase extends ItemBindable implements ISigil {

    private final String name;
    private boolean toggleable;
    protected final String tooltipBase;

    public ItemSigilBase(String name, int energyUsed) {
        super();

        setUnlocalizedName(AlchemicalWizardry.MODID + ".sigil." + name);
        setEnergyUsed(energyUsed);

        this.name = name;
        this.tooltipBase = "tooltip.AlchemicalWizardry.sigil." + name + ".";
    }

    public ItemSigilBase(String name) {
        this(name, 0);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        super.onItemRightClick(stack, world, player);

        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        super.onItemRightClick(stack, world, player);

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {

        if (StatCollector.canTranslate(tooltipBase + "desc"))
            tooltip.add(TextHelper.localize(tooltipBase + "desc"));

        super.addInformation(stack, player, tooltip, advanced);
    }

    public void setToggleable() {
        this.toggleable = true;
    }

    public boolean isUnusable(ItemStack stack) {
        NBTHolder.checkNBT(stack);

        return stack.getTagCompound().getBoolean(NBTHolder.NBT_UNUSABLE);
    }

    public ItemStack setUnusable(ItemStack stack, boolean unusable) {
        NBTHolder.checkNBT(stack);

        stack.getTagCompound().setBoolean(NBTHolder.NBT_UNUSABLE, unusable);
        return stack;
    }

    public boolean getActivated(ItemStack stack) {
        return stack.getItemDamage() > 0;
    }

    public ItemStack setActivated(ItemStack stack, boolean activated) {
        if (this.toggleable)
            stack.setItemDamage(activated ? 1 : 0);

        return stack;
    }
}
