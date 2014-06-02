package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningRegistry;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityDemon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DemonPlacer extends Item
{
    @SideOnly(Side.CLIENT)
    private IIcon theIcon;

    public DemonPlacer()
    {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.maxStackSize = 1;
    }

    public String getItemDisplayName(ItemStack par1ItemStack)
    {
//        String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
//        String s1 = EntityList.getStringFromID(par1ItemStack.getItemDamage());
//
//        if (s1 != null)
//        {
//            s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
//        }
//
//        return s;
        return "Demon Crystal";
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        EntityEggInfo entityegginfo = (EntityEggInfo) EntityList.entityEggs.get(Integer.valueOf(par1ItemStack.getItemDamage()));
        return entityegginfo != null ? (par2 == 0 ? entityegginfo.primaryColor : entityegginfo.secondaryColor) : 16777215;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par3World.isRemote)
        {
            return true;
        } else
        {
            Block i1 = par3World.getBlock(par4, par5, par6);
            par4 += Facing.offsetsXForSide[par7];
            par5 += Facing.offsetsYForSide[par7];
            par6 += Facing.offsetsZForSide[par7];
            double d0 = 0.0D;

            if (par7 == 1 && i1 != null && i1.getRenderType() == 11)
            {
                d0 = 0.5D;
            }

            Entity entity = spawnCreature(par3World, par1ItemStack.getItemDamage(), (double) par4 + 0.5D, (double) par5 + d0, (double) par6 + 0.5D, par1ItemStack);

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName())
                {
                    ((EntityLiving) entity).setCustomNameTag(par1ItemStack.getDisplayName());
                }

                if (!par2EntityPlayer.capabilities.isCreativeMode)
                {
                    --par1ItemStack.stackSize;
                }
            }

            return true;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par2World.isRemote)
        {
            return par1ItemStack;
        } else
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

            if (movingobjectposition == null)
            {
                return par1ItemStack;
            } else
            {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!par2World.canMineBlock(par3EntityPlayer, i, j, k))
                    {
                        return par1ItemStack;
                    }

                    if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack))
                    {
                        return par1ItemStack;
                    }

                    if (par2World.getBlock(i, j, k).getMaterial() == Material.water)
                    {
                        Entity entity = spawnCreature(par2World, par1ItemStack.getItemDamage(), (double) i, (double) j, (double) k, par1ItemStack);

                        if (entity != null)
                        {
                            if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName())
                            {
                                ((EntityLiving) entity).setCustomNameTag(par1ItemStack.getDisplayName());
                            }

                            if (!par3EntityPlayer.capabilities.isCreativeMode)
                            {
                                --par1ItemStack.stackSize;
                            }
                        }
                    }
                }

                return par1ItemStack;
            }
        }
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    public static Entity spawnCreature(World par0World, int par1, double par2, double par4, double par6, ItemStack itemStack)
    {
//        if (!EntityList.entityEggs.containsKey(Integer.valueOf(par1)))
//        {
//            return null;
//        }
//        else
        {
            Entity entity = null;

            for (int j = 0; j < 1; ++j)
            {
                entity = SummoningRegistry.getEntityWithID(par0World, par1);

                if (entity != null && entity instanceof EntityLivingBase)
                {
                    EntityLiving entityliving = (EntityLiving) entity;
                    entity.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;

                    //entityliving.onSpawnWithEgg((EntityLivingData)null);
                    if (entityliving instanceof EntityDemon)
                    {
                        ((EntityDemon) entityliving).setOwner(DemonPlacer.getOwnerName(itemStack));

                        if (!DemonPlacer.getOwnerName(itemStack).equals(""))
                        {
                            ((EntityDemon) entityliving).setTamed(true);
                        }
                    }

                    par0World.spawnEntityInWorld(entity);
                    entityliving.playLivingSound();
                }
            }

            return entity;
        }
    }

//    @SideOnly(Side.CLIENT)
//    public boolean requiresMultipleRenderPasses()
//    {
//        return true;
//    }

//    @SideOnly(Side.CLIENT)
//
//    /**
//     * Gets an icon index based on an item's damage value and the given render pass
//     */
//    public Icon getIconFromDamageForRenderPass(int par1, int par2)
//    {
//        return par2 > 0 ? this.theIcon : super.getIconFromDamageForRenderPass(par1, par2);
//    }

//    @SideOnly(Side.CLIENT)
//
//    /**
//     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
//     */
//    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
//    {
//        Iterator iterator = EntityList.entityEggs.values().iterator();
//
//        while (iterator.hasNext())
//        {
//            EntityEggInfo entityegginfo = (EntityEggInfo)iterator.next();
//            par3List.add(new ItemStack(par1, 1, entityegginfo.spawnedID));
//        }
//    }

    public static void setOwnerName(ItemStack par1ItemStack, String ownerName)
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.stackTagCompound.setString("ownerName", ownerName);
    }

    public static String getOwnerName(ItemStack par1ItemStack)
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        return par1ItemStack.stackTagCompound.getString("ownerName");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Used to spawn demons.");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            if (!par1ItemStack.stackTagCompound.getString("ownerName").equals(""))
            {
                par3List.add("Demon's Owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DemonPlacer");
    }
}
