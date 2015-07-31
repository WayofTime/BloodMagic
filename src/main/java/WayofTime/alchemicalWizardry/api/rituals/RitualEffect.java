package WayofTime.alchemicalWizardry.api.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;

public abstract class RitualEffect
{
    public abstract void performEffect(IMasterRitualStone ritualStone);

    public boolean startRitual(IMasterRitualStone ritualStone, EntityPlayer player)
    {
        return true;
    }

    public void onRitualBroken(IMasterRitualStone ritualStone, RitualBreakMethod method)
    {

    }

    public abstract int getCostPerRefresh();

    public int getInitialCooldown()
    {
        return 0;
    }

    public abstract List<RitualComponent> getRitualComponentList();

    public boolean canDrainReagent(IMasterRitualStone ritualStone, Reagent reagent, int amount, boolean doDrain)
    {
        if (ritualStone == null || reagent == null || amount == 0)
        {
            return false;
        }

        ReagentStack reagentStack = new ReagentStack(reagent, amount);

        ReagentStack stack = ritualStone.drain(ForgeDirection.UNKNOWN, reagentStack, false);

        if (stack != null && stack.amount >= amount)
        {
            if (doDrain)
            {
                ritualStone.drain(ForgeDirection.UNKNOWN, reagentStack, true);
            }

            return true;
        }

        return false;
    }
    
    public LocalRitualStorage getNewLocalStorage()
    {
    	return new LocalRitualStorage();
    }
    
    public void addOffsetRunes(ArrayList<RitualComponent> ritualList, int off1, int off2, int y, int rune)
    {
    	ritualList.add(new RitualComponent(off1, y, off2, rune));
    	ritualList.add(new RitualComponent(off2, y, off1, rune));
    	ritualList.add(new RitualComponent(off1, y, -off2, rune));
    	ritualList.add(new RitualComponent(-off2, y, off1, rune));
    	ritualList.add(new RitualComponent(-off1, y, off2, rune));
    	ritualList.add(new RitualComponent(off2, y, -off1, rune));
    	ritualList.add(new RitualComponent(-off1, y, -off2, rune));
    	ritualList.add(new RitualComponent(-off2, y, -off1, rune));
    }
    
    public void addCornerRunes(ArrayList<RitualComponent> ritualList, int off1, int y, int rune)
    {
    	ritualList.add(new RitualComponent(off1, y, off1, rune));
    	ritualList.add(new RitualComponent(off1, y, -off1, rune));
    	ritualList.add(new RitualComponent(-off1, y, -off1, rune));
    	ritualList.add(new RitualComponent(-off1, y, off1, rune));
    }
    
    public void addParallelRunes(ArrayList<RitualComponent> ritualList, int off1, int y, int rune)
    {
    	ritualList.add(new RitualComponent(off1, y, 0, rune));
    	ritualList.add(new RitualComponent(-off1, y, 0, rune));
    	ritualList.add(new RitualComponent(0, y, -off1, rune));
    	ritualList.add(new RitualComponent(0, y, off1, rune));
    }
}
