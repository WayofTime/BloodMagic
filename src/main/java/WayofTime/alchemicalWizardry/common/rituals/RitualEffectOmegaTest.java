package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.omega.OmegaParadigm;
import WayofTime.alchemicalWizardry.common.omega.OmegaRegistry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectOmegaTest extends RitualEffect
{
	public static final int tickDuration = 1 * 60 * 20;
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();
//
//        if (world.getWorldTime() % 200 != 0)
//        {
//            return;
//        }

        double range = 2;

        List<EntityPlayer> playerList = SpellHelper.getPlayersInRange(world, x + 0.5, y + 0.5, z + 0.5, range, range);
        
        for(EntityPlayer player : playerList)
        {        	
        	Reagent reagent = ReagentRegistry.aquasalusReagent;
        	
        	OmegaParadigm waterParadigm = OmegaRegistry.getParadigmForReagent(reagent);
        	waterParadigm.convertPlayerArmour(player);
        	
        	APISpellHelper.setPlayerCurrentReagentAmount(player, tickDuration);
        	APISpellHelper.setPlayerMaxReagentAmount(player, tickDuration);
        	APISpellHelper.setPlayerReagentType(player, reagent);
        	APISpellHelper.setCurrentAdditionalMaxHP(player, waterParadigm.getMaxAdditionalHealth());
			NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getReagentBarPacket(reagent, APISpellHelper.getPlayerCurrentReagentAmount(player), APISpellHelper.getPlayerMaxReagentAmount(player)), (EntityPlayerMP)player);
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> animalGrowthRitual = new ArrayList();
        animalGrowthRitual.add(new RitualComponent(0, 0, 2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(2, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(0, 0, -2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(0, 0, 1, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(1, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(0, 0, -1, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(1, 0, 2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(-1, 0, 2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(1, 0, -2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(-1, 0, -2, RitualComponent.WATER));
        animalGrowthRitual.add(new RitualComponent(2, 0, 1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(2, 0, -1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(-2, 0, 1, RitualComponent.AIR));
        animalGrowthRitual.add(new RitualComponent(-2, 0, -1, RitualComponent.AIR));
        return animalGrowthRitual;
    }
}
