package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class RitualWater extends Ritual {

	public RitualWater() {
		super("ritualWater", 0, 1000, "ritual." + Constants.Mod.MODID + ".waterRitual");
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone) {
		World world = masterRitualStone.getWorld();
		SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner(), world);
		int currentEssence = network.getCurrentEssence();
		
		if(currentEssence < getRefreshCost())
			return;
		
		BlockPos pos = masterRitualStone.getPos().up();
		if(world.isAirBlock(pos)) {
			world.setBlockState(pos, Blocks.water.getDefaultState());
			network.syphon(getRefreshCost());
		}
	}

	@Override
	public int getRefreshTime() {
		return 1;
	}

	@Override
	public int getRefreshCost() {
		return 50;
	}

	@Override
	public ArrayList<RitualComponent> getComponents() {
		ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 1, 0, EnumRuneType.WATER);
        
        return components;
	}
}
