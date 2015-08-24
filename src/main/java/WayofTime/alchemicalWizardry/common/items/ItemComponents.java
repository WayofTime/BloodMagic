package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemComponents extends Item
{
    public static final String[] ITEM_NAMES = new String[]{"quartz_rod", "empty_core", "magicales_cable", "wood_brace", "stone_brace", "projectile_core", "self_core", "melee_core", "paradigm_back_plate", "output_cable", "flame_core", "icy_core", "gust_core", "earthen_core", "input_cable", "cracked_runic_plate", "runic_plate", "scribed_runic_plate", "default_core", "offensive_core", "defensive_core", "environmental_core", "power_core", "cost_core", "potency_core", "obsidian_brace", "tool_core", "ethereal_slate", "life_shard", "soul_shard", "soul_runic_plate", "life_brace", "ender_shard"};

    public ItemComponents()
    {
        super();
        this.hasSubtypes = true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1ItemStack;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        //This is what will do all the localisation things on the alchemy components so you dont have to set it :D
        int meta = MathHelper.clamp_int(itemStack.getItemDamage(), 0, ITEM_NAMES.length - 1);
        return getUnlocalizedName() + "." + ITEM_NAMES[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List list)
    {
        for (int meta = 0; meta < ITEM_NAMES.length; ++meta)
        {
            list.add(new ItemStack(id, 1, meta));
        }
    }
}
