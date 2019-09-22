package WayofTime.bloodmagic.alchemyArray;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.network.play.server.SUpdateHealthPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.iface.IAlchemyArray;

public class AlchemyArrayEffectTeleport extends AlchemyArrayEffect {
    public static final int MAX_SEARCH = 20;
    public static final int TELEPORT_DELAY = 40;

    public AlchemyArrayEffectTeleport(String key) {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive) {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(IAlchemyArray array, World world, BlockPos pos, BlockState state, Entity entity) {
        Direction direction = array.getRotation();

        teleportEntityInDirection(world, pos, entity, direction);
    }

    public void teleportEntityInDirection(World world, BlockPos currentPos, Entity entity, Direction direction) {
        if (entity != null && entity.timeUntilPortal <= 0) {
            for (int i = 1; i <= MAX_SEARCH; i++) {
                BlockPos offsetPos = currentPos.offset(direction, i);
                Block testBlock = world.getBlockState(offsetPos).getBlock();
                if (testBlock == RegistrarBloodMagicBlocks.ALCHEMY_ARRAY) {
                    int x = offsetPos.getX();
                    int y = offsetPos.getY();
                    int z = offsetPos.getZ();

                    entity.getEntityWorld().playSound(x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
                    entity.timeUntilPortal = TELEPORT_DELAY;
                    if (!world.isRemote) {
                        if (entity instanceof PlayerEntity) {
                            ServerPlayerEntity player = (ServerPlayerEntity) entity;

                            player.setPositionAndUpdate(x + 0.5, y + 0.5, z + 0.5);
                            player.getEntityWorld().updateEntityWithOptionalForce(player, false);
                            player.connection.sendPacket(new SUpdateHealthPacket(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));

                        } else {
                            ServerWorld worldServer = (ServerWorld) entity.getEntityWorld();

                            entity.setPosition(x + 0.5, y + 0.5, z + 0.5);
                            worldServer.resetUpdateEntityTick();
                        }
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void writeToNBT(CompoundNBT tag) {

    }

    @Override
    public void readFromNBT(CompoundNBT tag) {

    }

    @Override
    public AlchemyArrayEffect getNewCopy() {
        return new AlchemyArrayEffectTeleport(key);
    }
}
