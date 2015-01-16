package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.Int3;

public class RitualEffectItemRouting extends RitualEffect
{
	Random rand = new Random();
	
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (world.getWorldTime() % 20 != 0)
        {
            return;
        }
                
        int xBufOffset = 0;
        int yBufOffset = 1;
        int zBufOffset = 0;
        
        TileEntity bufferTile = world.getTileEntity(x + xBufOffset, y + yBufOffset, z + zBufOffset);
        
        if(!(bufferTile instanceof IInventory))
        {
        	return;
        }
        
        IInventory bufferInventory = (IInventory)bufferTile;
        
        for(int i=0; i<4; i++)
        {
        	Int3 inputChest = this.getInputBufferChestLocation(i);
        	TileEntity inputFocusInv = world.getTileEntity(x + inputChest.xCoord, y + inputChest.yCoord, z + inputChest.zCoord);
        	if(inputFocusInv instanceof IInventory)
        	{
        		
        	}
        }
    }
    
    public Int3 getInputBufferChestLocation(int number)
    {
    	switch(number)
    	{
    	case 0:
    		return new Int3(1, 0, 0);
    	case 1:
    		return new Int3(-1, 0, 0);
    	case 2:
    		return new Int3(0, 0, 1);
    	case 3:
    		return new Int3(0, 0, -1);
    	}
    	return new Int3(0, 0, 0);
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> omegaRitual = new ArrayList();
        
        this.addCornerRunes(omegaRitual, 1, 0, RitualComponent.BLANK);
        this.addOffsetRunes(omegaRitual, 2, 1, 0, RitualComponent.FIRE);
        this.addParallelRunes(omegaRitual, 4, 0, RitualComponent.WATER);
        this.addParallelRunes(omegaRitual, 5, 0, RitualComponent.EARTH);
        this.addCornerRunes(omegaRitual, 4, 0, RitualComponent.WATER);

        return omegaRitual;
    }
}
