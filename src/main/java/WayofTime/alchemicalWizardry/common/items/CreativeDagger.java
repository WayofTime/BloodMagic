package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.event.SacrificeKnifeUsedEvent;
import WayofTime.alchemicalWizardry.api.sacrifice.PlayerSacrificeHandler;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class CreativeDagger extends Item
{
    public CreativeDagger()
    {
        super();
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setFull3D();
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (this.canUseForSacrifice(stack))
        {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
            return stack;
        }

        if (!player.capabilities.isCreativeMode)
        {
            SacrificeKnifeUsedEvent evt = new SacrificeKnifeUsedEvent(player, true, true, 2);
            if(MinecraftForge.EVENT_BUS.post(evt))
            {
                return stack;
            }

            if(evt.shouldDrainHealth)
            {
                player.setHealth(player.getHealth() - 2);
            }

            if(!evt.shouldFillAltar)
            {
                return stack;
            }
        }

        if (player instanceof FakePlayer)
        {
            return stack;
        }

        double posX = player.posX;
        double posY = player.posY;
        double posZ = player.posZ;
        world.playSoundEffect((double) ((float) posX + 0.5F), (double) ((float) posY + 0.5F), (double) ((float) posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        float f = 1.0F;
        float f1 = f * 0.6F + 0.4F;
        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;

        for (int l = 0; l < 8; ++l)
        {
            world.spawnParticle(EnumParticleTypes.REDSTONE, posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), f1, f2, f3);
        }

        if (!world.isRemote && SpellHelper.isFakePlayer(world, player))
        {
            return stack;
        }

        findAndFillAltar(world, player, Integer.MAX_VALUE);
        return stack;
    }

    public void findAndFillAltar(World world, EntityPlayer player, int amount)
    {
        BlockPos pos = player.getPosition();
        IBloodAltar altarEntity = getAltar(world, pos);

        if (altarEntity == null)
        {
            return;
        }

        altarEntity.sacrificialDaggerCall(amount, false);
        altarEntity.startCycle();
    }

    public IBloodAltar getAltar(World world, BlockPos pos)
    {
        TileEntity tileEntity;

        for (int i = -2; i <= 2; i++)
        {
            for (int j = -2; j <= 2; j++)
            {
                for (int k = -2; k <= 1; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                    tileEntity = world.getTileEntity(newPos);

                    if(tileEntity instanceof IBloodAltar)
                    {
                        return (IBloodAltar)tileEntity;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
    {
        if(!world.isRemote && entity instanceof EntityPlayer)
        {
            this.setUseForSacrifice(stack, this.isPlayerPreparedForSacrifice(world, (EntityPlayer)entity));
        }
    }

    public boolean isPlayerPreparedForSacrifice(World world, EntityPlayer player)
    {
        return !world.isRemote && (PlayerSacrificeHandler.getPlayerIncense(player) > 0);
    }

    public boolean canUseForSacrifice(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();

        return tag != null && tag.getBoolean("sacrifice");
    }

    public void setUseForSacrifice(ItemStack stack, boolean sacrifice)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if(tag == null)
        {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        tag.setBoolean("sacrifice", sacrifice);
    }
}
