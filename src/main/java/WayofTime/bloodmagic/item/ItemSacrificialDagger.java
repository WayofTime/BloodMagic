package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import WayofTime.bloodmagic.client.IMeshProvider;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.SacrificeKnifeUsedEvent;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerSacrificeHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.helper.TextHelper;

import javax.annotation.Nullable;

public class ItemSacrificialDagger extends Item implements IMeshProvider
{
    public static String[] names = { "normal", "creative" };

    public ItemSacrificialDagger()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".sacrificialDagger.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHasSubtypes(true);
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced)
    {
        list.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.BloodMagic.sacrificialDagger.desc"))));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer)
            PlayerSacrificeHelper.sacrificePlayerHealth((EntityPlayer) entityLiving);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (PlayerHelper.isFakePlayer(player))
            return super.onItemRightClick(stack, world, player, hand);

        if (this.canUseForSacrifice(stack))
        {
            player.setActiveHand(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }

        int lpAdded = 200;

        RayTraceResult rayTrace = rayTrace(world, player, false);
        if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            TileEntity tile = world.getTileEntity(rayTrace.getBlockPos());

            if (tile != null && tile instanceof TileAltar && stack.getItemDamage() == 1)
                lpAdded = ((TileAltar) tile).getCapacity();
        }

        if (!player.capabilities.isCreativeMode)
        {
            SacrificeKnifeUsedEvent evt = new SacrificeKnifeUsedEvent(player, true, true, 2, lpAdded);
            if (MinecraftForge.EVENT_BUS.post(evt))
                return super.onItemRightClick(stack, world, player, hand);

            if (evt.shouldDrainHealth)
            {
                player.hurtResistantTime = 0;
                player.attackEntityFrom(BloodMagicAPI.getDamageSource(), 0.001F);
                player.setHealth(Math.max(player.getHealth() - 2, 0.0001f));
                if (player.getHealth() <= 0.001f)
                {
                    player.onDeath(BloodMagicAPI.getDamageSource());
                    player.setHealth(0);
                }
//                player.attackEntityFrom(BloodMagicAPI.getDamageSource(), 2.0F);
            }

            if (!evt.shouldFillAltar)
                return super.onItemRightClick(stack, world, player, hand);

            lpAdded = evt.lpAdded;
        }

        double posX = player.posX;
        double posY = player.posY;
        double posZ = player.posZ;
        world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        for (int l = 0; l < 8; ++l)
            world.spawnParticle(EnumParticleTypes.REDSTONE, posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), 0, 0, 0);

        if (!world.isRemote && PlayerHelper.isFakePlayer(player))
            return super.onItemRightClick(stack, world, player, hand);

        // TODO - Check if SoulFray is active
        PlayerSacrificeHelper.findAndFillAltar(world, player, lpAdded, false);

        return super.onItemRightClick(stack, world, player, hand);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
    {
        if (!world.isRemote && entity instanceof EntityPlayer)
            this.setUseForSacrifice(stack, this.isPlayerPreparedForSacrifice(world, (EntityPlayer) entity));
    }

    public boolean isPlayerPreparedForSacrifice(World world, EntityPlayer player)
    {
        return !world.isRemote && (PlayerSacrificeHelper.getPlayerIncense(player) > 0);
    }

    public boolean canUseForSacrifice(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);
        return stack.getTagCompound().getBoolean(Constants.NBT.SACRIFICE);
    }

    public void setUseForSacrifice(ItemStack stack, boolean sacrifice)
    {
        stack = NBTHelper.checkNBT(stack);
        stack.getTagCompound().setBoolean(Constants.NBT.SACRIFICE, sacrifice);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition()
    {
        return new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                String variant = "type=normal";
                if (stack.getItemDamage() != 0)
                    variant = "type=creative";

                if (canUseForSacrifice(stack))
                    variant = "type=ceremonial";

                return new ModelResourceLocation(new ResourceLocation(Constants.Mod.MODID, "item/ItemSacrificialDagger"), variant);
            }
        };
    }

    @Override
    public List<String> getVariants()
    {
        List<String> variants = new ArrayList<String>();
        variants.add("type=normal");
        variants.add("type=creative");
        variants.add("type=ceremonial");
        return variants;
    }

    @Nullable
    @Override
    public ResourceLocation getCustomLocation()
    {
        return null;
    }
}