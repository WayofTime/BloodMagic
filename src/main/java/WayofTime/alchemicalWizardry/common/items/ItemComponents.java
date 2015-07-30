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
import WayofTime.alchemicalWizardry.AlchemicalWizardry;

public class ItemComponents extends Item
{
    private static final String[] ITEM_NAMES = new String[]{"QuartzRod", "EmptyCore", "MagicalesCable", "WoodBrace", "StoneBrace", "ProjectileCore", "SelfCore", "MeleeCore", "ParadigmBackPlate", "OutputCable", "FlameCore", "IcyCore", "GustCore", "EarthenCore", "InputCable", "CrackedRunicPlate", "RunicPlate", "ScribedRunicPlate", "DefaultCore", "OffensiveCore", "DefensiveCore", "EnvironmentalCore", "PowerCore", "CostCore", "PotencyCore", "ObsidianBrace", "ToolCore", "EtherealSlate", "LifeShard", "SoulShard", "SoulRunicPlate", "LifeBrace", "EnderShard"};

    public ItemComponents()
    {
        super();
        this.maxStackSize = 64;
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
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
        return ("" + "item.bloodMagicBaseItem." + ITEM_NAMES[meta]);
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
