package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.ISigil;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.item.ItemBindable;
import WayofTime.bloodmagic.util.helper.TextHelper;
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
public class ItemSigilBase extends ItemBindable implements ISigil
{
    protected final String tooltipBase;
    private final String name;
    private boolean toggleable;

    public ItemSigilBase(String name, int lpUsed)
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".sigil." + name);
        setLPUsed(lpUsed);

        this.name = name;
        this.tooltipBase = "tooltip.BloodMagic.sigil." + name + ".";
    }

    public ItemSigilBase(String name)
    {
        this(name, 0);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        super.onItemRightClick(stack, world, player);

        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        super.onItemRightClick(stack, world, player);

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        if (StatCollector.canTranslate(tooltipBase + "desc"))
            tooltip.add(TextHelper.localizeEffect(tooltipBase + "desc"));

        super.addInformation(stack, player, tooltip, advanced);
    }

    public void setToggleable()
    {
        this.toggleable = true;
    }

    public boolean isUnusable(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        return stack.getTagCompound().getBoolean(Constants.NBT.UNUSABLE);
    }

    public ItemStack setUnusable(ItemStack stack, boolean unusable)
    {
        NBTHelper.checkNBT(stack);

        stack.getTagCompound().setBoolean(Constants.NBT.UNUSABLE, unusable);
        return stack;
    }

    public boolean getActivated(ItemStack stack)
    {
        return stack.getItemDamage() > 0;
    }

    public ItemStack setActivated(ItemStack stack, boolean activated)
    {
        if (this.toggleable)
            stack.setItemDamage(activated ? 1 : 0);

        return stack;
    }
}
