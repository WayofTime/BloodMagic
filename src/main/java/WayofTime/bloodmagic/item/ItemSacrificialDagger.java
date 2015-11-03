package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.DamageSourceBloodMagic;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.event.SacrificeKnifeUsedEvent;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerSacrificeHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSacrificialDagger extends Item
{
    public static String[] names = { "normal", "creative" };

    public ItemSacrificialDagger()
    {
        super();

        setUnlocalizedName(BloodMagic.MODID + ".sacrificialDagger.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHasSubtypes(true);
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item id, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
//        if (AlchemicalWizardry.wimpySettings)
        {
//            par3List.add(StatCollector.translateToLocal("tooltip.sacrificialdagger.desc1"));
        }
//        else
        {
            par3List.add(StatCollector.translateToLocal("tooltip.sacrificialdagger.desc2"));
            par3List.add(StatCollector.translateToLocal("tooltip.sacrificialdagger.desc3"));
        }
    }

    /**
     * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int itemInUseCount)
    {
//        if(itemInUseCount < 32)
//        {
//        	return;
//        }

        PlayerSacrificeHelper.sacrificePlayerHealth(player);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
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

        if (PlayerHelper.isFakePlayer(player))
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

        if (!world.isRemote && PlayerHelper.isFakePlayer(player))
        {
            return stack;
        }

//        if (player.isPotionActive(AlchemicalWizardry.customPotionSoulFray))
        {
//            findAndFillAltar(world, player, 20);
        }
//        else
        {
            findAndFillAltar(world, player, 200);
        }

        if (player.getHealth() <= 0.001f)
        {
            player.onDeath(new DamageSourceBloodMagic());
        }

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

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
//        if (AlchemicalWizardry.wimpySettings)
        {
//            return "Sacrificial Orb";
        }
        return super.getItemStackDisplayName(stack);
    }

    public boolean isPlayerPreparedForSacrifice(World world, EntityPlayer player)
    {
        return !world.isRemote && (PlayerSacrificeHelper.getPlayerIncense(player) > 0);
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

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return this.canUseForSacrifice(stack) || super.hasEffect(stack);
    }
}
