package WayofTime.bloodmagic.compat.minecraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;

public class CrossVersionProxy110 implements ICrossVersionProxy {

    @Override
    public TileEntity createTileFromData(World world, NBTTagCompound tagCompound)
    {
        Method m = ReflectionHelper.findMethod(TileEntity.class, null, new String[] { "create", "func_190200_a", "a" }, World.class, NBTTagCompound.class);
        try
        {
            return (TileEntity) m.invoke(null, world, tagCompound);
        } catch (Exception e)
        {
            return null;
        }
    }
}
