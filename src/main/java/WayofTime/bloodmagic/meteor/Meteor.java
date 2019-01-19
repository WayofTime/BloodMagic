package WayofTime.bloodmagic.meteor;

import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class Meteor {
    private static final Random RAND = new Random();

    private final ItemStack catalystStack;
    private final List<MeteorComponent> components;
    private final float explosionStrength;
    private final int radius;
    private final int maxWeight;
    public int version;
    public int cost = 1000000;

    public Meteor(ItemStack catalystStack, List<MeteorComponent> components, float explosionStrength, int radius) {
        this.catalystStack = catalystStack;
        this.components = components;
        this.explosionStrength = explosionStrength;
        this.radius = radius;

        int weight = 0;
        for (MeteorComponent component : components)
            weight += component.getWeight();
        this.maxWeight = weight;
    }

    public void generateMeteor(World world, BlockPos pos, IBlockState fillerBlock, double radiusModifier, double explosionModifier, double fillerChance) {
        world.newExplosion(null, pos.getX(), pos.getY(), pos.getZ(), (float) (explosionStrength * explosionModifier), true, true);
        int radius = (int) Math.ceil(getRadius() * radiusModifier);
        double floatingRadius = getRadius() * radiusModifier;

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    if (i * i + j * j + k * k > (floatingRadius + 0.5) * (floatingRadius + 0.5)) {
                        continue;
                    }

                    BlockPos newPos = pos.add(i, j, k);
                    IBlockState state = world.getBlockState(newPos);

                    if (world.isAirBlock(newPos) || Utils.isBlockLiquid(state)) {
                        IBlockState placedState = getRandomOreFromComponents(fillerBlock, fillerChance);
                        if (placedState != null) {
                            world.setBlockState(newPos, placedState);
                        }
                    }
                }
            }
        }
    }

    //fillerChance is the chance that the filler block will NOT be placed
    public IBlockState getRandomOreFromComponents(IBlockState fillerBlock, double fillerChance) {
        int goal = RAND.nextInt(getMaxWeight());

        for (MeteorComponent component : getComponents()) {
            goal -= component.getWeight();
            if (goal < 0) {
                IBlockState state = component.getStateFromOre();
                if (state != null) {
                    return state;
                } else {
                    return RAND.nextDouble() > fillerChance ? fillerBlock : null;
                }
            }
        }

        return RAND.nextDouble() > fillerChance ? fillerBlock : null;
    }

    public ItemStack getCatalystStack() {
        return catalystStack;
    }

    public List<MeteorComponent> getComponents() {
        return components;
    }

    public float getExplosionStrength() {
        return explosionStrength;
    }

    public int getRadius() {
        return radius;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setCost(int performCost) {
        this.cost = performCost;
    }

    public int getCost() {
        return cost;
    }
}