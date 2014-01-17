package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.*;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.BonemealEvent;

import java.util.List;

public class SigilOfGrowth extends EnergyItems implements ArmourUpgrade {
    private static Icon activeIcon;
    private static Icon passiveIcon;
    private int tickDelay = 100;

    public SigilOfGrowth(int id)
    {
        super(id);
        this.maxStackSize = 1;
        //setMaxDamage(1000);
        setEnergyUsed(150);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Who needs a green thumb when");
        par3List.add("you have a green slate?");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            if (par1ItemStack.stackTagCompound.getBoolean("isActive"))
            {
                par3List.add("Activated");
            } else
            {
                par3List.add("Deactivated");
            }

            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:GrowthSigil_deactivated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:GrowthSigil_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:GrowthSigil_deactivated");
    }

    @Override
    public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        if (stack.stackTagCompound == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.stackTagCompound;

        if (tag.getBoolean("isActive"))
        {
            return this.activeIcon;
        } else
        {
            return this.passiveIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        if (par1 == 1)
        {
            return this.activeIcon;
        } else
        {
            return this.passiveIcon;
        }
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par2EntityPlayer);

        if (applyBonemeal(par1ItemStack, par3World, par4, par5, par6, par2EntityPlayer))
        {
            EnergyItems.syphonBatteries(par1ItemStack, par2EntityPlayer, getEnergyUsed());

            if (par3World.isRemote)
            {
                par3World.playAuxSFX(2005, par4, par5, par6, 0);
                return true;
            }

            return true;
        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = par1ItemStack.stackTagCompound;
        tag.setBoolean("isActive", !(tag.getBoolean("isActive")));

        if (tag.getBoolean("isActive"))
        {
            par1ItemStack.setItemDamage(1);
            tag.setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % tickDelay);

            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed());
            }
        } else
        {
            par1ItemStack.setItemDamage(par1ItemStack.getMaxDamage());
        }

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!(par3Entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (par1ItemStack.stackTagCompound.getBoolean("isActive"))
        {
            if (par2World.getWorldTime() % tickDelay == par1ItemStack.stackTagCompound.getInteger("worldTimeDelay") && par3Entity instanceof EntityPlayer)
            {
                EnergyItems.syphonBatteries(par1ItemStack, (EntityPlayer) par3Entity, getEnergyUsed());
            }

            int range = 5;
            int verticalRange = 2;
            int posX = (int) Math.round(par3Entity.posX - 0.5f);
            int posY = (int) par3Entity.posY;
            int posZ = (int) Math.round(par3Entity.posZ - 0.5f);

            for (int ix = posX - range; ix <= posX + range; ix++)
            {
                for (int iz = posZ - range; iz <= posZ + range; iz++)
                {
                    for (int iy = posY - verticalRange; iy <= posY + verticalRange; iy++)
                    {
                        int id = par2World.getBlockId(ix, iy, iz);
                        Block block = Block.blocksList[id];

                        if (block instanceof IPlantable)
                        {
                            if (par2World.rand.nextInt(20) == 0)
                            {
                                block.updateTick(par2World, ix, iy, iz, par2World.rand);
                            }
                        }
                    }
                }
            }
        }

        return;
    }

    public static boolean applyBonemeal(ItemStack par0ItemStack, World par1World, int par2, int par3, int par4, EntityPlayer player)
    {
        int l = par1World.getBlockId(par2, par3, par4);
        BonemealEvent event = new BonemealEvent(player, par1World, l, par2, par3, par4);

        if (MinecraftForge.EVENT_BUS.post(event))
        {
            return false;
        }

        if (event.getResult() == Result.ALLOW)
        {
            return true;
        }

        if (l == Block.sapling.blockID)
        {
            if (!par1World.isRemote)
            {
                if ((double) par1World.rand.nextFloat() < 0.45D)
                {
                    ((BlockSapling) Block.sapling).markOrGrowMarked(par1World, par2, par3, par4, par1World.rand);
                }
            }

            return true;
        } else if (l != Block.mushroomBrown.blockID && l != Block.mushroomRed.blockID)
        {
            if (l != Block.melonStem.blockID && l != Block.pumpkinStem.blockID)
            {
                if (l > 0 && Block.blocksList[l] instanceof BlockCrops)
                {
                    if (par1World.getBlockMetadata(par2, par3, par4) == 7)
                    {
                        return false;
                    } else
                    {
                        if (!par1World.isRemote)
                        {
                            ((BlockCrops) Block.blocksList[l]).fertilize(par1World, par2, par3, par4);
                        }

                        return true;
                    }
                } else
                {
                    int i1;
                    int j1;
                    int k1;

                    if (l == Block.cocoaPlant.blockID)
                    {
                        i1 = par1World.getBlockMetadata(par2, par3, par4);
                        j1 = BlockDirectional.getDirection(i1);
                        k1 = BlockCocoa.func_72219_c(i1);

                        if (k1 >= 2)
                        {
                            return false;
                        } else
                        {
                            if (!par1World.isRemote)
                            {
                                ++k1;
                                par1World.setBlockMetadataWithNotify(par2, par3, par4, k1 << 2 | j1, 2);
                            }

                            return true;
                        }
                    } else if (l != Block.grass.blockID)
                    {
                        return false;
                    } else
                    {
                        if (!par1World.isRemote)
                        {
                            label102:

                            for (i1 = 0; i1 < 128; ++i1)
                            {
                                j1 = par2;
                                k1 = par3 + 1;
                                int l1 = par4;

                                for (int i2 = 0; i2 < i1 / 16; ++i2)
                                {
                                    j1 += itemRand.nextInt(3) - 1;
                                    k1 += (itemRand.nextInt(3) - 1) * itemRand.nextInt(3) / 2;
                                    l1 += itemRand.nextInt(3) - 1;

                                    if (par1World.getBlockId(j1, k1 - 1, l1) != Block.grass.blockID || par1World.isBlockNormalCube(j1, k1, l1))
                                    {
                                        continue label102;
                                    }
                                }

                                if (par1World.getBlockId(j1, k1, l1) == 0)
                                {
                                    if (itemRand.nextInt(10) != 0)
                                    {
                                        if (Block.tallGrass.canBlockStay(par1World, j1, k1, l1))
                                        {
                                            par1World.setBlock(j1, k1, l1, Block.tallGrass.blockID, 1, 3);
                                        }
                                    } else
                                    {
                                        ForgeHooks.plantGrass(par1World, j1, k1, l1);
                                    }
                                }
                            }
                        }

                        return true;
                    }
                }
            } else if (par1World.getBlockMetadata(par2, par3, par4) == 7)
            {
                return false;
            } else
            {
                if (!par1World.isRemote)
                {
                    ((BlockStem) Block.blocksList[l]).fertilizeStem(par1World, par2, par3, par4);
                }

                return true;
            }
        } else
        {
            if (!par1World.isRemote)
            {
                if ((double) par1World.rand.nextFloat() < 0.4D)
                {
                    ((BlockMushroom) Block.blocksList[l]).fertilizeMushroom(par1World, par2, par3, par4, par1World.rand);
                }
            }

            return true;
        }
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        int range = 5;
        int verticalRange = 2;
        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);

        for (int ix = posX - range; ix <= posX + range; ix++)
        {
            for (int iz = posZ - range; iz <= posZ + range; iz++)
            {
                for (int iy = posY - verticalRange; iy <= posY + verticalRange; iy++)
                {
                    int id = world.getBlockId(ix, iy, iz);
                    Block block = Block.blocksList[id];

                    if (block instanceof IPlantable)
                    {
                        if (world.rand.nextInt(10) == 0)
                        {
                            block.updateTick(world, ix, iy, iz, world.rand);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isUpgrade()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        // TODO Auto-generated method stub
        return 50;
    }
}
