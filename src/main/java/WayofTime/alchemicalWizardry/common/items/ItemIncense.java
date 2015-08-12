package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.ShapelessBloodOrbRecipe;
import WayofTime.alchemicalWizardry.api.sacrifice.IIncense;

public class ItemIncense extends Item implements IIncense
{
    public static final String[] ITEM_NAMES = new String[]{"wood_ash", "byrrus", "livens", "viridis", "purpura"};

    public ItemIncense()
    {
        super();
        this.hasSubtypes = true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add(StatCollector.translateToLocal("tooltip.alchemy.usedinincense"));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int meta = MathHelper.clamp_int(itemStack.getItemDamage(), 0, ITEM_NAMES.length - 1);
        return (getUnlocalizedName() + "." + ITEM_NAMES[meta]);
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
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
    	if(!this.doesIncenseHaveUniqueTexture(stack.getItemDamage()))
    	{
    		EnumIncense inc = EnumIncense.getEnumForIndex(stack.getItemDamage());
            return (int)((255*inc.redColour * 256 * 256 + 255*inc.greenColour * 256 + 255*inc.blueColour));
    	}

        return 256 * (256 * 255 + 255) + 255;
    }

	@Override
	public int getMinLevel(ItemStack stack) 
	{
		return EnumIncense.getEnumForIndex(stack.getItemDamage()).minValue;
	}

	@Override
	public int getMaxLevel(ItemStack stack) 
	{
		return EnumIncense.getEnumForIndex(stack.getItemDamage()).maxValue;
	}

	@Override
	public int getIncenseDuration(ItemStack stack) 
	{
		return EnumIncense.getEnumForIndex(stack.getItemDamage()).incenseDuration;
	}
	
	@Override
	public float getTickRate(ItemStack stack)
	{
		return EnumIncense.getEnumForIndex(stack.getItemDamage()).tickRate;
	}

	@Override
	public float getRedColour(ItemStack stack) 
	{
		return EnumIncense.getEnumForIndex(stack.getItemDamage()).redColour;
	}

	@Override
	public float getGreenColour(ItemStack stack) 
	{
		return EnumIncense.getEnumForIndex(stack.getItemDamage()).greenColour;
	}

	@Override
	public float getBlueColour(ItemStack stack) 
	{
		return EnumIncense.getEnumForIndex(stack.getItemDamage()).blueColour;
	}
	
	public boolean doesIncenseHaveUniqueTexture(int meta)
	{
		return false;
	}
	
	public static void registerIncenseRecipes()
	{
		int WILDCARD = OreDictionary.WILDCARD_VALUE;
		ItemStack woodStack = new ItemStack(Blocks.log, 1, WILDCARD);
		ItemStack charcoalStack = new ItemStack(Items.coal, 1, 1);
		ItemStack leavesStack = new ItemStack(Blocks.leaves, 1, WILDCARD);
		ItemStack goldNuggetStack = new ItemStack(Items.gold_nugget, 1, WILDCARD);
		ItemStack stringStack = new ItemStack(Items.string, 1, WILDCARD);
		ItemStack glowstoneStack = new ItemStack(Items.glowstone_dust, 1, WILDCARD);
		ItemStack soulSandStack = new ItemStack(Blocks.soul_sand);
		ItemStack gunpowderStack = new ItemStack(Items.gunpowder);
		ItemStack fermentedEyeStack = new ItemStack(Items.fermented_spider_eye);
		ItemStack quartzStack = new ItemStack(Items.quartz, 1, WILDCARD);
		ItemStack blazePowderStack = new ItemStack(Items.blaze_powder);
		ItemStack netherwartStack = new ItemStack(Items.nether_wart);
		ItemStack fracturedBoneStack = new ItemStack(ModItems.baseAlchemyItems, 1, 5);
		
		ItemStack woodashStack = new ItemStack(ModItems.itemIncense, 1, 0);
		
		GameRegistry.addRecipe(woodashStack, "WWW", "WCW", "WWW", 'W', woodStack, 'C', charcoalStack); //WOODASH
		GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModItems.itemIncense, 1, 1), woodashStack, "dyeRed", "dyeRed", new ItemStack(Items.redstone), leavesStack, leavesStack, new ItemStack(ModItems.apprenticeBloodOrb)));
		GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModItems.itemIncense, 1, 2), woodashStack, "dyeBlue", "dyeBlue", goldNuggetStack, goldNuggetStack, glowstoneStack, stringStack, stringStack, new ItemStack(ModItems.magicianBloodOrb)));
		GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModItems.itemIncense, 1, 3), woodashStack, "dyeGreen", "dyeGreen", soulSandStack, gunpowderStack, fermentedEyeStack, new ItemStack(ModItems.masterBloodOrb)));
		GameRegistry.addRecipe(new ShapelessBloodOrbRecipe(new ItemStack(ModItems.itemIncense, 1, 4), woodashStack, "dyePurple", "dyePurple", quartzStack, netherwartStack, blazePowderStack, fracturedBoneStack, goldNuggetStack, new ItemStack(ModItems.archmageBloodOrb)));
	}
	
	public enum EnumIncense
	{
		WOODASH(0, 200, 1.0f, 1000, 0.937f, 0.898f, 0.820f),
		RED(200, 600, 1.5f, 1000, 1.0f, 0, 0),
		BLUE(600, 1200, 3.0f, 1000, 0, 0, 1.0f),
		GREEN(1200, 2000, 4.0f, 1000, 0, 1.0f, 0),
		PURPLE(2000, 3000, 5.0f, 1000, 1.0f, 0, 1.0f);
		
		public final int minValue;
		public final int maxValue;
		public final float tickRate;
		public final int incenseDuration;
		
		public final float redColour;
		public final float greenColour;
		public final float blueColour;
		
		EnumIncense(int minValue, int maxValue, float tickRate, int dur, float red, float green, float blue)
		{
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.tickRate = tickRate;
			this.incenseDuration = dur;
			
			this.redColour = red;
			this.greenColour = green;
			this.blueColour = blue;
		}
		
		public static EnumIncense getEnumForIndex(int index)
		{
			if(index > EnumIncense.values().length || index < 0)
			{
				return WOODASH;
			}else
			{
				return EnumIncense.values()[index];
			}
		}
	}
}
