package WayofTime.alchemicalWizardry.altar;

import WayofTime.alchemicalWizardry.api.BlockStack;
import WayofTime.alchemicalWizardry.api.altar.AltarComponent;
import WayofTime.alchemicalWizardry.api.altar.IAltarComponent;
import WayofTime.alchemicalWizardry.api.altar.EnumAltarComponent;
import WayofTime.alchemicalWizardry.api.altar.EnumAltarTier;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockGlowstone;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BloodAltar {

    public static EnumAltarTier getAltarTier(World world, BlockPos pos) {
        for (int i = EnumAltarTier.MAXTIERS; i >= 2; i--)
            if (checkAltarIsValid(world, pos, i))
                return EnumAltarTier.values()[i];

        return EnumAltarTier.ONE;
    }

    public static boolean checkAltarIsValid(World world, BlockPos worldPos, int altarTier) {
        for (AltarComponent altarComponent : EnumAltarTier.values()[altarTier].getAltarComponents()) {

            BlockPos componentPos = worldPos.add(altarComponent.getOffset());
            BlockStack worldBlock = new BlockStack(world.getBlockState(componentPos).getBlock(), world.getBlockState(componentPos).getBlock().getMetaFromState(world.getBlockState(componentPos)));

            if (altarComponent.isBloodRune()) {
                if (!checkRune(altarComponent, worldBlock))
                    return false;
            } else {
                if (((altarComponent.getBlockStack().getBlock() != worldBlock.getBlock()) || (altarComponent.getBlockStack().getMeta() != worldBlock.getMeta())) && (altarComponent.getBlockStack().getBlock() == Blocks.air && !world.isAirBlock(componentPos)))
                    if (!checkSpecials(altarComponent, worldBlock))
                        return false;
            }
        }

        return true;
    }

//    public static AltarUpgrade getUpgrades(World world, BlockPos pos, int altarTier) {
//        if(world.isRemote)
//            return null;
//
//        AltarUpgrade upgrades = new AltarUpgrade();
//        List<AltarComponent> list = EnumAltarTier.values()[altarTier].getAltarComponents();
//
//        for (AltarComponent altarComponent : list) {
//            BlockPos componentPos = pos.add(altarComponent.getOffset());
//
//            if (altarComponent.isUpgradeSlot()) {
//                BlockStack worldBlock = new BlockStack(world.getBlockState(componentPos).getBlock(), world.getBlockState(componentPos).getBlock().getMetaFromState(world.getBlockState(componentPos)));
//
//                if (worldBlock.getBlock() instanceof BlockBloodRune) {
//                    if (worldBlock.getBlock() instanceof IFadedRune && altarTier > ((IFadedRune)worldBlock.getBlock()).getAltarTierLimit(worldBlock.getMeta()))
//                        return getUpgrades(world, pos, ((IFadedRune) worldBlock.getBlock()).getAltarTierLimit(worldBlock.getMeta()));
//
//                    switch (((BlockBloodRune) worldBlock.getBlock()).getRuneEffect(worldBlock.getMeta())) {
//                        case 1:
//                            upgrades.addSpeed();
//                            break;
//
//                        case 2:
//                            upgrades.addEfficiencyUpgrade();
//                            break;
//
//                        case 3:
//                            upgrades.addSacrificeUpgrade();
//                            break;
//
//                        case 4:
//                            upgrades.addSelfSacrificeUpgrade();
//                            break;
//
//                        case 5:
//                            upgrades.addaltarCapacitiveUpgrade();
//                            break;
//
//                        case 6:
//                            upgrades.addDisplacementUpgrade();
//                            break;
//
//                        case 7:
//                            upgrades.addorbCapacitiveUpgrade();
//                            break;
//
//                        case 8:
//                            upgrades.addBetterCapacitiveUpgrade();
//                            break;
//
//                        case 9:
//                            upgrades.addAccelerationUpgrade();
//                            break;
//                    }
//                }
//            }
//        }
//
//        return upgrades;
//    }

    private static boolean checkRune(AltarComponent altarComponent, BlockStack blockStack) {
//        if (altarComponent.getBlockStack().getBlock() == ModBlocks.rune)
//            if (blockStack.getBlock() instanceof BlockBloodRune || (blockStack.getBlock() instanceof IAltarComponent && (((IAltarComponent) blockStack.getBlock()).getType(blockStack.getMeta()) == EnumAltarComponent.BLOODRUNE)))
//                return true;

        return false;
    }

    private static boolean checkSpecials(AltarComponent altarComponent, BlockStack blockStack) {
//        if (altarComponent.getBlockStack().getBlock() == ModBlocks.bloodStone)
//            if (blockStack.getBlock() instanceof BlockBloodStone || (blockStack.getBlock() instanceof IAltarComponent && (((IAltarComponent) blockStack.getBlock()).getType(blockStack.getMeta()) == EnumAltarComponent.BLOODSTONE)))
//                return true;

//        if (altarComponent.getBlockStack().getBlock() == ModBlocks.crystal)
//            if (blockStack.getBlock() instanceof BlockCrystal || (blockStack.getBlock() instanceof IAltarComponent && (((IAltarComponent) blockStack.getBlock()).getType(blockStack.getMeta()) == EnumAltarComponent.CRYSTAL)))
//                return true;

        if (altarComponent.getBlockStack().getBlock() == Blocks.glowstone)
            if (blockStack.getBlock() instanceof BlockGlowstone || (blockStack.getBlock() instanceof IAltarComponent && (((IAltarComponent) blockStack.getBlock()).getType(blockStack.getMeta()) == EnumAltarComponent.GLOWSTONE)))
                return true;

        if (altarComponent.getBlockStack().getBlock() == Blocks.beacon)
            if (blockStack.getBlock() instanceof BlockBeacon || (blockStack.getBlock() instanceof IAltarComponent && (((IAltarComponent) blockStack.getBlock()).getType(blockStack.getMeta()) == EnumAltarComponent.BEACON)))
                return true;

        return false;
    }
}
