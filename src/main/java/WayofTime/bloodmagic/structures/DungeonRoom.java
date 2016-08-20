package WayofTime.bloodmagic.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;

public class DungeonRoom
{
    protected Map<DungeonStructure, BlockPos> structureMap = new HashMap<DungeonStructure, BlockPos>();

    public List<AreaDescriptor> getAreaDescriptors(PlacementSettings settings, BlockPos position)
    {
        return new ArrayList<AreaDescriptor>();
    }
}
