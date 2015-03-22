package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

public class CreativeDagger extends Item
{
    public CreativeDagger()
    {
        super();
        setTextureName("AlchemicalWizardry:SacrificialDagger");
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setFull3D();
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
        	SacrificeKnifeUsedEvent evt = new SacrificeKnifeUsedEvent(par3EntityPlayer, true, true, 2);
        	if(MinecraftForge.EVENT_BUS.post(evt))
        	{
        		return par1ItemStack;
        	}
        	
        	if(evt.shouldDrainHealth)
        	{
                par3EntityPlayer.setHealth(par3EntityPlayer.getHealth() - 2);
        	}
        	
        	if(!evt.shouldFillAltar)
        	{
        		return par1ItemStack;
        	}
        }

        if (par3EntityPlayer instanceof FakePlayer)
        {
            return par1ItemStack;
        }

        double posX = par3EntityPlayer.posX;
        double posY = par3EntityPlayer.posY;
        double posZ = par3EntityPlayer.posZ;
        par2World.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (par2World.rand.nextFloat() - par2World.rand.nextFloat()) * 0.8F);
        float f = (float) 1.0F;
        float f1 = f * 0.6F + 0.4F;
        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;

        for (int l = 0; l < 8; ++l)
        {
            par2World.spawnParticle("reddust", posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), f1, f2, f3);
        }

        if (!par2World.isRemote && SpellHelper.isFakePlayer(par2World, par3EntityPlayer))
        {
            return par1ItemStack;
        }

        if (par3EntityPlayer.isPotionActive(AlchemicalWizardry.customPotionSoulFray))
        {
            findAndFillAltar(par2World, par3EntityPlayer, Integer.MAX_VALUE);
        } else
        {
            findAndFillAltar(par2World, par3EntityPlayer, Integer.MAX_VALUE);
        }

        if (par3EntityPlayer.getHealth() <= 0.001f)
        {
            par3EntityPlayer.onDeath(DamageSource.generic);
        }

        return par1ItemStack;
    }

    public void findAndFillAltar(World world, EntityPlayer player, int amount)
    {
        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);
        IBloodAltar altarEntity = getAltar(world, posX, posY, posZ);

        if (altarEntity == null)
        {
            return;
        }

        altarEntity.sacrificialDaggerCall(amount, false);
        altarEntity.startCycle();
    }

    public IBloodAltar getAltar(World world, int x, int y, int z)
    {
        TileEntity tileEntity = null;

        for (int i = -2; i <= 2; i++)
        {
            for (int j = -2; j <= 2; j++)
            {
                for (int k = -2; k <= 1; k++)
                {
                    tileEntity = world.getTileEntity(i + x, k + y, j + z);

                    if ((tileEntity instanceof IBloodAltar))
                    {
                        return (IBloodAltar) tileEntity;
                    }
                }

                if ((tileEntity instanceof IBloodAltar))
                {
                    return (IBloodAltar) tileEntity;
                }
            }

            if ((tileEntity instanceof IBloodAltar))
            {
                return (IBloodAltar) tileEntity;
            }
        }

        if ((tileEntity instanceof IBloodAltar))
        {
            return (IBloodAltar) tileEntity;
        }

        return null;
    }
