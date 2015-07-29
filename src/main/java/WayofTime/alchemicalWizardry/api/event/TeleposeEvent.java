package WayofTime.alchemicalWizardry.api.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;


/** Fired when a teleposer attempts to transpose two blocks. Use this to perform special cleanup or compensation,
or cancel it entirely to prevent the transposition. */
@Cancelable
public class TeleposeEvent extends Event 
{
  
  public final World initialWorld;
  public final BlockPos initialPos;
  
  public final Block initialBlock;
  public final IBlockState initialState;
  
  public final World finalWorld;
  public final BlockPos finalPos;
  
  public final Block finalBlock;
  public final IBlockState finalState;
  
  public TeleposeEvent(World wi, BlockPos posi, IBlockState statei, World wf, BlockPos posf, IBlockState statef) {
    initialWorld = wi;
    initialPos = posi;
    
    initialState = statei;
    initialBlock = initialState.getBlock();

    finalWorld = wf;
    finalPos = posf;
    
    finalState = statef;
    finalBlock = finalState.getBlock();
  }
  
  public TileEntity getInitialTile() {
    return initialWorld.getTileEntity(initialPos);
  }
  
  public TileEntity getFinalTile() {
    return finalWorld.getTileEntity(finalPos);
  }

}
