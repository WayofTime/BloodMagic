package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.event.SacrificeKnifeUsedEvent;
import WayofTime.bloodmagic.item.types.ISubItem;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.DamageSourceBloodMagic;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import WayofTime.bloodmagic.util.helper.PlayerSacrificeHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class ItemSacrificialDagger extends ItemEnum<ItemSacrificialDagger.DaggerType> implements IMeshProvider {

    public ItemSacrificialDagger() {
        super(DaggerType.class, "sacrificial_dagger");

        setHasSubtypes(true);
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        list.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.sacrificialDagger.desc"))));

        if (stack.getItemDamage() == 1)
            list.add(TextHelper.localizeEffect("tooltip.bloodmagic.sacrificialDagger.creative"));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer && !entityLiving.getEntityWorld().isRemote)
            PlayerSacrificeHelper.sacrificePlayerHealth((EntityPlayer) entityLiving);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (PlayerHelper.isFakePlayer(player))
            return super.onItemRightClick(world, player, hand);

        if (this.canUseForSacrifice(stack)) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        int lpAdded = ConfigHandler.values.sacrificialDaggerConversion * 2;

        RayTraceResult rayTrace = rayTrace(world, player, false);
        if (rayTrace != null && rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
            TileEntity tile = world.getTileEntity(rayTrace.getBlockPos());

            if (tile != null && tile instanceof TileAltar && stack.getItemDamage() == 1)
                lpAdded = ((TileAltar) tile).getCapacity();
        }

        if (!player.capabilities.isCreativeMode) {
            SacrificeKnifeUsedEvent evt = new SacrificeKnifeUsedEvent(player, true, true, 2, lpAdded);
            if (MinecraftForge.EVENT_BUS.post(evt))
                return super.onItemRightClick(world, player, hand);

            if (evt.shouldDrainHealth) {
                player.hurtResistantTime = 0;
                player.attackEntityFrom(DamageSourceBloodMagic.INSTANCE, 0.001F);
                player.setHealth(Math.max(player.getHealth() - 2, 0.0001f));
                if (player.getHealth() <= 0.001f) {
                    player.onDeath(DamageSourceBloodMagic.INSTANCE);
                    player.setHealth(0);
                }
//                player.attackEntityFrom(BloodMagicAPI.getDamageSource(), 2.0F);
            }

            if (!evt.shouldFillAltar)
                return super.onItemRightClick(world, player, hand);

            lpAdded = evt.lpAdded;
        }

        double posX = player.posX;
        double posY = player.posY;
        double posZ = player.posZ;
        world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        for (int l = 0; l < 8; ++l)
            world.spawnParticle(EnumParticleTypes.REDSTONE, posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), 0, 0, 0);

        if (!world.isRemote && PlayerHelper.isFakePlayer(player))
            return super.onItemRightClick(world, player, hand);

        // TODO - Check if SoulFray is active
        PlayerSacrificeHelper.findAndFillAltar(world, player, lpAdded, false);

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
        if (!world.isRemote && entity instanceof EntityPlayer)
            this.setUseForSacrifice(stack, this.isPlayerPreparedForSacrifice(world, (EntityPlayer) entity));
    }

    public boolean isPlayerPreparedForSacrifice(World world, EntityPlayer player) {
        return !world.isRemote && (PlayerSacrificeHelper.getPlayerIncense(player) > 0);
    }

    public boolean canUseForSacrifice(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);
        return stack.getTagCompound().getBoolean(Constants.NBT.SACRIFICE);
    }

    public void setUseForSacrifice(ItemStack stack, boolean sacrifice) {
        stack = NBTHelper.checkNBT(stack);
        stack.getTagCompound().setBoolean(Constants.NBT.SACRIFICE, sacrifice);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition() {
        return stack -> {
            String variant = "type=normal";
            if (stack.getItemDamage() != 0)
                variant = "type=creative";

            if (canUseForSacrifice(stack))
                variant = "type=ceremonial";

            return new ModelResourceLocation(getRegistryName(), variant);
        };
    }

    @Override
    public void gatherVariants(Consumer<String> variants) {
        variants.accept("type=normal");
        variants.accept("type=creative");
        variants.accept("type=ceremonial");
    }

    public enum DaggerType implements ISubItem {

        NORMAL,
        CREATIVE,;

        @Nonnull
        @Override
        public String getInternalName() {
            return name().toLowerCase(Locale.ROOT);
        }

        @Nonnull
        @Override
        public ItemStack getStack(int count) {
            return new ItemStack(RegistrarBloodMagicItems.SACRIFICIAL_DAGGER, count, ordinal());
        }
    }
}