package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

public class EnergyBattery extends Item implements ArmourUpgrade, IBindable, IBloodOrb
{
    private int maxEssence;
    protected int orbLevel;

    public EnergyBattery(int damage)
    {
        super();
        DamageSource damageSource = DamageSource.generic;
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        maxEssence = damage;
        orbLevel = 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergyBattery");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Stores raw Life Essence");
        if (!(par1ItemStack.stackTagCompound == null))
        {
            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
        }
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);
        World world = par3EntityPlayer.worldObj;
        if (world != null)
        {
            double posX = par3EntityPlayer.posX;
            double posY = par3EntityPlayer.posY;
            double posZ = par3EntityPlayer.posZ;
            world.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            SpellHelper.sendIndexedParticleToAllAround(world, posX, posY, posZ, 20, world.provider.dimensionId, 4, posX, posY, posZ);
        }
        NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

        if (itemTag == null || itemTag.getString("ownerName").equals(""))
        {
            return par1ItemStack;
        }

        if (world.isRemote)
        {
            return par1ItemStack;
        }
        
        if(itemTag.getString("ownerName").equals(SpellHelper.getUsername(par3EntityPlayer)))
        {
        	SoulNetworkHandler.setMaxOrbToMax(itemTag.getString("ownerName"), this.orbLevel);
        }

        SoulNetworkHandler.addCurrentEssenceToMaximum(itemTag.getString("ownerName"), 200, this.getMaxEssence());
        EnergyItems.hurtPlayer(par3EntityPlayer, 200);
        return par1ItemStack;
    }

    /*
     * @return the damage that was not deducted
     */
    public int damageItem(ItemStack par1ItemStack, int par2int)
    {
        if (par2int == 0)
        {
            return 0;
        }

        int before = this.getDamage(par1ItemStack);
        this.setDamage(par1ItemStack, this.getDamage(par1ItemStack) + par2int);
        return par2int - (this.getDamage(par1ItemStack) - before);
    }

    protected void damagePlayer(World world, EntityPlayer player, int damage)
    {
        if (world != null)
        {
            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;
            world.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            float f = (float) 1.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;

            for (int l = 0; l < 8; ++l)
            {
                world.spawnParticle("reddust", posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), f1, f2, f3);
            }
        }

        if (!player.capabilities.isCreativeMode)
        {
            for (int i = 0; i < damage; i++)
            {
                player.setHealth((player.getHealth() - 1));
            }
        }

        if (player.getHealth() <= 0)
        {
            player.inventory.dropAllItems();
        }
    }

    public int getMaxEssence()
    {
        return this.maxEssence;
    }

    public int getOrbLevel()
    {
        return orbLevel;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
    }

    @Override
    public boolean isUpgrade()
    {
        return false;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 0;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return itemStack;
    }

    @Override
    public boolean hasContainerItem()
    {
        return true;
    }

    //@SideOnly(Side.SERVER)
    public int getCurrentEssence(ItemStack par1ItemStack)
    {
        if (par1ItemStack == null)
        {
            return 0;
        }

        NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

        if (itemTag == null || itemTag.getString("ownerName").equals(""))
        {
            return 0;
        }

        String owner = itemTag.getString("ownerName");
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;
        return (currentEssence);
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack)
    {
        return false;
    }
}
