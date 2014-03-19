package WayofTime.alchemicalWizardry.common.items.thaumcraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FocusGravityWell extends FocusBase
{
    //private static final AspectList visUsage = new AspectList().add(Aspect.AIR, 5).add(Aspect.ORDER, 5);

    private final int maxCooldown = 1;

    public static Map<String,Integer> playerCooldown = new HashMap();

    public FocusGravityWell()
    {
        super();
        this.setUnlocalizedName("focusGravityWell");
        this.setEnergyUsed(100);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        super.registerIcons(iconRegister);

        if (hasOrnament())
        {
            ornament = iconRegister.registerIcon("AlchemicalWizardry:" + "focusGravityWell" + "Orn");
        }

        if (hasDepth())
        {
            depth = iconRegister.registerIcon("AlchemicalWizardry:" + "focusGravityWell" + "Depth");
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
                //if(cooldown>=this.maxCooldown)
                {
                    Method consumeAllVis = clazz.getMethod("consumeAllVis", ItemStack.class, EntityPlayer.class, AspectList.class, boolean.class);

                    if ((Boolean) consumeAllVis.invoke(item, stack, par3EntityPlayer, getVisCost(), true))
                    {
                        playerCooldown.put(par3EntityPlayer.username, 0);
                        EnergyItems.checkAndSetItemOwner(focusStack, par3EntityPlayer);
                        Vec3 vector = par3EntityPlayer.getLookVec();
                        float distance = 2;
                        //if(par3EntityPlayer.worldObj.isRemote)
                        {
                            List<Entity> entities = par3EntityPlayer.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(par3EntityPlayer.posX + vector.xCoord * distance - 0.5f, par3EntityPlayer.posY + vector.yCoord * distance - 0.5f, par3EntityPlayer.posZ + vector.zCoord * distance - 0.5f, par3EntityPlayer.posX + vector.xCoord * distance + 0.5f, par3EntityPlayer.posY + vector.yCoord * distance + 0.5f, par3EntityPlayer.posZ + vector.zCoord * distance + 0.5f).expand(1, 1, 1));

                            for (Entity entity : entities)
                            {
                                if (entity.getEntityName() == par3EntityPlayer.username)
                                {
                                    continue;
                                }

                                entity.motionX = par3EntityPlayer.posX + vector.xCoord * distance - entity.posX;
                                entity.motionY = par3EntityPlayer.posY + vector.yCoord * distance - entity.posY;
                                entity.motionZ = par3EntityPlayer.posZ + vector.zCoord * distance - entity.posZ;
                                //entity.setVelocity(par3EntityPlayer.posX+vector.xCoord*distance-entity.posX, par3EntityPlayer.posY+vector.yCoord*distance-entity.posY, par3EntityPlayer.posZ+vector.zCoord*distance-entity.posZ);
                            }
                        }
                        World world = par3EntityPlayer.worldObj;

                        if (!par3EntityPlayer.capabilities.isCreativeMode)
                        {
                            this.syphonBatteriesWithoutParticles(focusStack, par3EntityPlayer, 10, false);
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
    public void onPlayerStoppedUsingFocus(ItemStack stack, World paramWorld, EntityPlayer par3EntityPlayer, int paramInt)
    {
        playerCooldown.put(par3EntityPlayer.username, 0);

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
                Method getFocusItem = clazz.getMethod("getFocusItem", ItemStack.class);
                ItemStack focusStack = (ItemStack) getFocusItem.invoke(item, stack);
                int potency = EnchantmentHelper.getEnchantmentLevel(ThaumcraftApi.enchantPotency, focusStack);

                if (potency > 0)
                {
                    EnergyItems.checkAndSetItemOwner(focusStack, par3EntityPlayer);
                    Vec3 vector = par3EntityPlayer.getLookVec();
                    float distance = 2;
                    //if(par3EntityPlayer.worldObj.isRemote)
                    {
                        List<Entity> entities = par3EntityPlayer.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(par3EntityPlayer.posX + vector.xCoord * distance - 0.5f, par3EntityPlayer.posY + vector.yCoord * distance - 0.5f, par3EntityPlayer.posZ + vector.zCoord * distance - 0.5f, par3EntityPlayer.posX + vector.xCoord * distance + 0.5f, par3EntityPlayer.posY + vector.yCoord * distance + 0.5f, par3EntityPlayer.posZ + vector.zCoord * distance + 0.5f).expand(1, 1, 1));

                        for (Entity entity : entities)
                        {
                            if (entity.getEntityName() == par3EntityPlayer.username)
                            {
                                continue;
                            }

                            float speed = 1.0F * potency;
                            entity.motionX = vector.xCoord * speed;
                            entity.motionY = vector.yCoord * speed;
                            entity.motionZ = vector.zCoord * speed;
                            //entity.setVelocity(par3EntityPlayer.posX+vector.xCoord*distance-entity.posX, par3EntityPlayer.posY+vector.yCoord*distance-entity.posY, par3EntityPlayer.posZ+vector.zCoord*distance-entity.posZ);
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
        return false;
    }
    */
}
