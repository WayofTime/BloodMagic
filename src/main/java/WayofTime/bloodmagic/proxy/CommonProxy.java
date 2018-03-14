package WayofTime.bloodmagic.proxy;

import WayofTime.bloodmagic.ritual.CapabilityRuneType;
import WayofTime.bloodmagic.ritual.IRitualStone;
import WayofTime.bloodmagic.teleport.TeleportQueue;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;

public class CommonProxy {
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(TeleportQueue.getInstance());
        registerRenderers();
    }

    public void init() {
        CapabilityManager.INSTANCE.register(IRitualStone.Tile.class, new CapabilityRuneType.RuneTypeStorage(), new CapabilityRuneType.Factory());
    }

    public void postInit() {

    }

    public void registerRenderers() {

    }

    public Object beamCont(World worldObj, double xi, double yi, double zi, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact) {
        // TODO Auto-generated method stub
        return null;
    }

    public IAnimationStateMachine load(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters) {
        return null;
    }
}
