package pneumaticCraft.api.block;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockSupplier{
    // private static Class blockClass;

    /**
     * @param blockName
     * @return
     */
    public static Block getBlock(String blockName){
        return GameRegistry.findBlock("PneumaticCraft", blockName);
        /*try {
            if(blockClass == null) blockClass = Class.forName("pneumaticCraft.common.block.Blockss");
            return (Block)blockClass.getField(blockName).get(null);
        } catch(Exception e) {
            System.err.println("[PneumaticCraft API] Block supply failed for block: " + blockName);
            return null;
        }*/
    }

    /*
     The following is a list of all the block names that can be passed as argument in getBlock(String) to get a PneumaticCraft block.
     
     
     pressureTube               meta = tube type
     airCompressor
     airCannon
     pressureChamberWall        meta < 6 ? wall : window
     pressureChamberValve
     pressureChamberInterface
     squidPlant
     fireFlower
     creeperPlant
     slimePlant
     rainPlant
     enderPlant
     lightningPlant
     adrenalinePlant
     burstPlant
     potionPlant
     repulsionPlant
     heliumPlant
     flyingFlower
     musicPlant
     propulsionPlant
     chopperPlant
     chargingStation
     elevatorBase
     elevatorFrame
     vacuumPump
     pneumaticDoorBase
     pneumaticDoor
     assemblyPlatform
     assemblyIOUnit
     assemblyDrill
     assemblyLaser
     assemblyController
     advancedPressureTube       meta = tube type (like 'pressureTube')
     compressedIron
     uvLightBox
     etchingAcid
     securityStation
     universalSensor
     pneumaticGenerator
     electricCompressor
     pneumaticEngine
     kineticCompressor
     aerialInterface
     electrostaticCompressor
     aphorismTile
     omnidirectionalHopper

     */
}
