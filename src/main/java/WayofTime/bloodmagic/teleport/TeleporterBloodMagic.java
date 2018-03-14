package WayofTime.bloodmagic.teleport;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterBloodMagic extends Teleporter {
    public TeleporterBloodMagic(WorldServer worldServer) {
        super(worldServer);
    }

    @Override
    public boolean makePortal(Entity entity) {
        return true;
    }

    @Override
    public void removeStalePortalLocations(long worldTime) {

    }

    @Override
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
        return true;
    }

    @Override
    public void placeInPortal(Entity entity, float rotationYaw) {
        entity.setLocationAndAngles(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY) + 2, MathHelper.floor(entity.posZ), entity.rotationYaw, entity.rotationPitch);
    }
}
