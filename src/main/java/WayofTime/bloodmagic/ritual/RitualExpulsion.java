package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;

import com.google.common.base.Strings;

public class RitualExpulsion extends Ritual
{
    public static final String EXPULSION_RANGE = "expulsionRange";

    public RitualExpulsion()
    {
        super("ritualExpulsion", 0, 10000, "ritual." + Constants.Mod.MODID + ".expulsionRitual");
        addBlockRange(EXPULSION_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-12, 0, -12), 25));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        if (masterRitualStone.getWorldObj().isRemote)
            return;

        AreaDescriptor expulsionRange = getBlockRange(EXPULSION_RANGE);

        List<String> allowedNames = new ArrayList<String>();

        if (world.getTileEntity(masterRitualStone.getBlockPos().up()) != null && world.getTileEntity(masterRitualStone.getBlockPos().up()) instanceof IInventory)
        {
            IInventory inventory = (IInventory) world.getTileEntity(masterRitualStone.getBlockPos().up());
            for (int i = 0; i < inventory.getSizeInventory(); i++)
            {
                ItemStack itemStack = inventory.getStackInSlot(i);
                if (itemStack != null && itemStack.getItem() instanceof IBindable)
                {
                    IBindable bindable = (IBindable) itemStack.getItem();
                    if (!Strings.isNullOrEmpty(bindable.getOwnerName(itemStack)) && !allowedNames.contains(bindable.getOwnerName(itemStack)))
                        allowedNames.add(bindable.getOwnerName(itemStack));
                }
            }
        }

        final int teleportDistance = 100;

        for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, expulsionRange.getAABB(masterRitualStone.getBlockPos())))
        {
            if (player.capabilities.isCreativeMode || PlayerHelper.getUUIDFromPlayer(player).toString().equals(masterRitualStone.getOwner()) || allowedNames.contains(PlayerHelper.getUUIDFromPlayer(player).toString()))
                continue;

            if (teleportRandomly(player, teleportDistance))
                network.syphon(getRefreshCost() * 1000);
        }

        allowedNames.clear();
    }

    public boolean teleportRandomly(EntityLivingBase entityLiving, double distance)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (player.capabilities.isCreativeMode)
                return false;
        }

        double x = entityLiving.posX;
        double y = entityLiving.posY;
        double z = entityLiving.posZ;
        Random rand = new Random();
        double randX = x + (rand.nextDouble() - 0.5D) * distance;
        double randY = y + (rand.nextInt((int) distance) - (distance) / 2);
        double randZ = z + (rand.nextDouble() - 0.5D) * distance;
        int i = 0;

        while (!teleportTo(entityLiving, randX, randY, randZ, x, y, z) && i < 100)
        {
            randX = x + (rand.nextDouble() - 0.5D) * distance;
            randY = y + (rand.nextInt((int) distance) - (distance) / 2);
            randZ = z + (rand.nextDouble() - 0.5D) * distance;
            i++;
        }

        return i >= 100;
    }

    public boolean teleportTo(EntityLivingBase entityLiving, double par1, double par3, double par5, double lastX, double lastY, double lastZ)
    {
        EnderTeleportEvent event = new EnderTeleportEvent(entityLiving, par1, par3, par5, 0);

        if (MinecraftForge.EVENT_BUS.post(event))
        {
            return false;
        }

        moveEntityViaTeleport(entityLiving, event.targetX, event.targetY, event.targetZ);
        boolean flag = false;
        int i = MathHelper.floor_double(entityLiving.posX);
        int j = MathHelper.floor_double(entityLiving.posY);
        int k = MathHelper.floor_double(entityLiving.posZ);
        int l;

        if (!entityLiving.worldObj.isAirBlock(new BlockPos(i, j, k)))
        {
            boolean flag1 = false;

            while (!flag1 && j > 0)
            {
                Block block = entityLiving.worldObj.getBlockState(new BlockPos(i, j - 1, k)).getBlock();

                if (block != null && block.getMaterial().blocksMovement())
                {
                    flag1 = true;
                } else
                {
                    --entityLiving.posY;
                    --j;
                }
            }

            if (flag1)
            {
                moveEntityViaTeleport(entityLiving, entityLiving.posX, entityLiving.posY, entityLiving.posZ);

                if (entityLiving.worldObj.getCollidingBoundingBoxes(entityLiving, entityLiving.getEntityBoundingBox()).isEmpty() && !entityLiving.worldObj.isAnyLiquid(entityLiving.getEntityBoundingBox()))
                {
                    flag = true;
                }
            }
        }

        if (!flag)
        {
            moveEntityViaTeleport(entityLiving, lastX, lastY, lastZ);
            return false;
        } else
        {
            for (l = 0; l < 128; ++l)
            {
                double lengthVal = (double) l / ((double) 128 - 1.0D);
                float randF1 = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float randF2 = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float randF3 = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                double lengthValX = lastX + (entityLiving.posX - lastX) * lengthVal + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width * 2.0D;
                double lengthValY = lastY + (entityLiving.posY - lastY) * lengthVal + entityLiving.worldObj.rand.nextDouble() * (double) entityLiving.height;
                double lengthValZ = lastZ + (entityLiving.posZ - lastZ) * lengthVal + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width * 2.0D;
                entityLiving.worldObj.spawnParticle(EnumParticleTypes.PORTAL, lengthValX, lengthValY, lengthValZ, (double) randF1, (double) randF2, (double) randF3);
            }

            return true;
        }
    }

    public void moveEntityViaTeleport(EntityLivingBase entityLiving, double x, double y, double z)
    {
        if (entityLiving != null && entityLiving instanceof EntityPlayer)
        {
            if (entityLiving instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP) entityLiving;

                if (entityplayermp.worldObj == entityLiving.worldObj)
                {
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, x, y, z, 5.0F);

                    if (!MinecraftForge.EVENT_BUS.post(event))
                    {
                        if (entityLiving.isRiding())
                        {
                            entityLiving.mountEntity(null);
                        }
                        entityLiving.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                    }
                }
            }
        } else if (entityLiving != null)
        {
            entityLiving.setPosition(x, y, z);
        }
    }

    @Override
    public int getRefreshTime()
    {
        return 1;
    }

    @Override
    public int getRefreshCost()
    {
        return 2;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 2, 0, EnumRuneType.EARTH);
        this.addRune(components, 2, 0, 1, EnumRuneType.EARTH);
        this.addRune(components, 1, 0, 2, EnumRuneType.EARTH);
        this.addRune(components, 2, 0, -1, EnumRuneType.EARTH);
        this.addRune(components, -1, 0, 2, EnumRuneType.EARTH);
        this.addRune(components, -2, 0, 1, EnumRuneType.EARTH);
        this.addRune(components, 1, 0, -2, EnumRuneType.EARTH);
        this.addRune(components, -2, 0, -1, EnumRuneType.EARTH);
        this.addRune(components, -1, 0, -2, EnumRuneType.EARTH);
        this.addRune(components, 4, 0, 2, EnumRuneType.AIR);
        this.addRune(components, 5, 0, 2, EnumRuneType.AIR);
        this.addRune(components, 4, 0, -2, EnumRuneType.AIR);
        this.addRune(components, 5, 0, -2, EnumRuneType.AIR);
        this.addRune(components, -4, 0, 2, EnumRuneType.AIR);
        this.addRune(components, -5, 0, 2, EnumRuneType.AIR);
        this.addRune(components, -4, 0, -2, EnumRuneType.AIR);
        this.addRune(components, -5, 0, -2, EnumRuneType.AIR);
        this.addRune(components, 2, 0, 4, EnumRuneType.AIR);
        this.addRune(components, 2, 0, 5, EnumRuneType.AIR);
        this.addRune(components, -2, 0, 4, EnumRuneType.AIR);
        this.addRune(components, -2, 0, 5, EnumRuneType.AIR);
        this.addRune(components, 2, 0, -4, EnumRuneType.AIR);
        this.addRune(components, 2, 0, -5, EnumRuneType.AIR);
        this.addRune(components, -2, 0, -4, EnumRuneType.AIR);
        this.addRune(components, -2, 0, -5, EnumRuneType.AIR);
        this.addParallelRunes(components, 5, 0, EnumRuneType.DUSK);
        this.addParallelRunes(components, 6, 0, EnumRuneType.EARTH);
        this.addRune(components, -6, 0, 1, EnumRuneType.DUSK);
        this.addRune(components, -6, 0, -1, EnumRuneType.DUSK);
        this.addRune(components, 6, 0, 1, EnumRuneType.DUSK);
        this.addRune(components, 6, 0, -1, EnumRuneType.DUSK);
        this.addRune(components, 1, 0, 6, EnumRuneType.DUSK);
        this.addRune(components, -1, 0, 6, EnumRuneType.DUSK);
        this.addRune(components, 1, 0, -6, EnumRuneType.DUSK);
        this.addRune(components, -1, 0, -6, EnumRuneType.DUSK);
        this.addCornerRunes(components, 4, 0, EnumRuneType.FIRE);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualExpulsion();
    }
}
