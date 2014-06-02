package WayofTime.alchemicalWizardry.common.items.thaumcraft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FocusBloodBlast extends FocusBase
{
    //private static final AspectList visUsage = new AspectList().add(Aspect.AIR, 15).add(Aspect.ENTROPY, 45);

    private final int maxCooldown = 7;

    public static Map<String,Integer> playerCooldown = new HashMap();

    public FocusBloodBlast()
    {
        super();
        this.setUnlocalizedName("focusBloodBlast");
        this.setEnergyUsed(100);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        super.registerIcons(iconRegister);

        if (hasOrnament())
        {
            ornament = iconRegister.registerIcon("AlchemicalWizardry:" + "focusBloodBlast" + "Orn");
        }

        if (hasDepth())
        {
            depth = iconRegister.registerIcon("AlchemicalWizardry:" + "focusBloodBlast" + "Depth");
        }
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        //super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        if (!(par1ItemStack.stackTagCompound == null))
        {
            if (!par1ItemStack.stackTagCompound.getString("ownerName").equals(""))
            {
                par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
            }
        }
    }

    /*
    @Override
    public void onUsingFocusTick(ItemStack stack, EntityPlayer par3EntityPlayer, int ticks)
    {
        if (AlchemicalWizardry.isThaumcraftLoaded)
        {
            Item item = stack.getItem();
            Class clazz = item.getClass();

            while (!clazz.getName().equals("thaumcraft.common.items.wands.ItemWandCasting"))
            {
                if (clazz == Object.class)
                {
                    return;
                }

                clazz = clazz.getSuperclass();
            }

            //Item testItem = item.set

            //Method consumeAllVis = null;
            try
            {
                if (!playerCooldown.containsKey(par3EntityPlayer.username))
                {
                    playerCooldown.put(par3EntityPlayer.username, 0);
                }

                Method getFocusItem = clazz.getMethod("getFocusItem", ItemStack.class);
                ItemStack focusStack = (ItemStack) getFocusItem.invoke(item, stack);
                //int potency = EnchantmentHelper.getEnchantmentLevel(ThaumcraftApi.enchantPotency, focusStack);
                int cooldown = playerCooldown.get(par3EntityPlayer.username) + 1;
                playerCooldown.put(par3EntityPlayer.username, cooldown);

                if (cooldown >= this.maxCooldown)
                {
                    Method consumeAllVis = clazz.getMethod("consumeAllVis", ItemStack.class, EntityPlayer.class, AspectList.class, boolean.class);

                    if ((Boolean) consumeAllVis.invoke(item, stack, par3EntityPlayer, getVisCost(), true))
                    {
                        playerCooldown.put(par3EntityPlayer.username, 0);
                        EnergyItems.checkAndSetItemOwner(focusStack, par3EntityPlayer);
                        World world = par3EntityPlayer.worldObj;

                        if (!par3EntityPlayer.capabilities.isCreativeMode)
                        {
                            this.syphonBatteries(focusStack, par3EntityPlayer, 100);
                        }

                        //world.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                        world.playSoundAtEntity(par3EntityPlayer, "thaumcraft:wand", 0.5F, 1F);

                        if (!world.isRemote)
                        {
                            //par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, damage));
                            world.spawnEntityInWorld(new EnergyBlastProjectile(world, par3EntityPlayer, (int) (5)));
                            //this.setDelay(par1ItemStack, maxDelay);
                        }
                    }
                }
            } catch (NoSuchMethodException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPlayerStoppedUsingFocus(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer, int paramInt)
    {
        playerCooldown.put(paramEntityPlayer.username, 0);
    }

    @Override
    public String getSortingHelper(ItemStack itemstack)
    {
        return "BLOODBLAST";
    }

    @Override
    public int getFocusColor()
    {
        return 0x8A0707;
    }

    @Override
    public AspectList getVisCost()
    {
        return visUsage;
    }

    @Override
    public boolean isVisCostPerTick()
    {
        return true;
    }

    @Override
    public WandFocusAnimation getAnimation()
    {
        return WandFocusAnimation.WAVE;
    }

    boolean hasOrnament()
    {
        return true;
    }
    */
}
