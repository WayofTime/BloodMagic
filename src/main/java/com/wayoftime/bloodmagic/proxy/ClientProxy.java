package com.wayoftime.bloodmagic.proxy;

import com.wayoftime.bloodmagic.BloodMagic;
import net.minecraftforge.client.model.obj.OBJLoader;
import org.lwjgl.opengl.Display;

public class ClientProxy implements IProxy {

    @Override
    public void preInit() {
        OBJLoader.INSTANCE.addDomain(BloodMagic.MODID);

        Display.setTitle("Basically 1.13"); // TODO - :BigThink:
    }
}
