package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("full_stomach")
public class RitualFullStomach extends Ritual {
    public static final String FILL_RANGE = "fillRange";
    public static final String CHEST_RANGE = "chest";

    public RitualFullStomach() {
        super("ritualFullStomach", 0, 100000, "ritual." + BloodMagic.MODID + ".fullStomachRitual");
        addBlockRange(FILL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-25, -25, -25), 51));
        addBlockRange(CHEST_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

        setMaximumVolumeAndDistanceOfRange(FILL_RANGE, 0, 25, 25);
        setMaximumVolumeAndDistanceOfRange(CHEST_RANGE, 1, 3, 3);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        BlockPos pos = masterRitualStone.getBlockPos();

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        AreaDescriptor chestRange = masterRitualStone.getBlockRange(CHEST_RANGE);
        TileEntity tile = world.getTileEntity(chestRange.getContainedPositions(pos).get(0));
        if (tile == null || !tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
            return;

        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int lastSlot = 0;
        AreaDescriptor fillingRange = masterRitualStone.getBlockRange(FILL_RANGE);
        List<EntityPlayer> playerList = world.getEntitiesWithinAABB(EntityPlayer.class, fillingRange.getAABB(pos));

        for (EntityPlayer player : playerList) {
            FoodStats foodStats = player.getFoodStats();
            float satLevel = foodStats.getSaturationLevel();

            for (int i = lastSlot; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.extractItem(i, 1, true);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemFood) {
                    ItemFood foodItem = (ItemFood) stack.getItem();

                    int healAmount = foodItem.getHealAmount(stack);
                    float saturationAmount = foodItem.getSaturationModifier(stack) * healAmount * 2.0f;

                    // Checks to make sure we're being efficient with the food and not wasting high value foods
                    // If the food provides more than the max saturation, we just accept it no matter what if the player is low
                    // Pam please stop being weird. Fix your mod.
                    if (saturationAmount + satLevel <= 20 || satLevel < 5) {
                        foodStats.addStats(foodItem, stack);
                        inventory.extractItem(i, 1, false);
                        totalEffects++;
                        lastSlot = i;
                        break;
                    }
                }
            }

            if (totalEffects >= maxEffects) {
                masterRitualStone.getOwnerNetwork().causeNausea();
                break;
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
    }

    @Override
    public int getRefreshTime() {
        return 20;
    }

    @Override
    public int getRefreshCost() {
        return 100;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addParallelRunes(components, 3, 0, EnumRuneType.FIRE);
        addCornerRunes(components, 1, 0, EnumRuneType.AIR);
        addOffsetRunes(components, 1, 2, 0, EnumRuneType.AIR);
        addCornerRunes(components, 4, 0, EnumRuneType.WATER);
        addOffsetRunes(components, 4, 3, 0, EnumRuneType.EARTH);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualFullStomach();
    }
}
