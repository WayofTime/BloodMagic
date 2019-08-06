package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@RitualRegister("gaia_transformation")
public class RitualGaiaTransformation extends Ritual {

    public int cooldown = 500;

    public RitualGaiaTransformation() {
        super("ritualGaiaTransformation", 0, 50000, "ritual." + BloodMagic.MODID + ".gaiaTransformationRitual");
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        /* Default Ritual Stuff */
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();
        BlockPos pos = masterRitualStone.getBlockPos();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        /* Default will augment stuff */
        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();
        DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(world, pos);

        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
        double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

        double rawDrained = 0;
        double corrosiveDrained = 0;
        double destructiveDrained = 0;
        double steadfastDrained = 0;
        double vengefulDrained = 0;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        /* Actual ritual stuff begins here */

        if (masterRitualStone.getCooldown() > 0) {
            masterRitualStone.setCooldown(masterRitualStone.getCooldown() - 1);

            if (world.rand.nextInt(15) == 0) {
                world.addWeatherEffect(new EntityLightningBolt(world, x - 1 + world.rand.nextInt(3), y + 1, z - 1 + world.rand.nextInt(3), false));
            }
            System.out.println("Cooldown remaining: " + masterRitualStone.getCooldown());
            return;
        }
        System.out.println("Init phase passed");

        int range = 10;

        boolean[][] boolList = new boolean[range * 2 + 1][range * 2 + 1];

        for (int i = 0; i < 2 * range + 1; i++) {
            for (int j = 0; j < 2 * range + 1; j++) {
                boolList[i][j] = false;
            }
        }

        boolList[range][range] = true;
        boolean isReady = false;

        while (!isReady) {
            isReady = true;

            for (int i = 0; i < 2 * range + 1; i++) {
                for (int j = 0; j < 2 * range + 1; j++) {
                    if (boolList[i][j]) {
                        if (i - 1 >= 0 && !boolList[i - 1][j]) {
                            Block block = world.getBlockState(new BlockPos(x - range + i - 1, y + 1, z - range + j)).getBlock();

                            if (!RegistrarBloodMagicBlocks.DECORATIVE_BRICK.equals(block)) {
                                System.out.println("Border Detected");
                                boolList[i - 1][j] = true;
                                isReady = false;
                            }
                        }

                        if (j - 1 >= 0 && !boolList[i][j - 1]) {
                            Block block = world.getBlockState(new BlockPos(x - range + i, y + 1, z - range + j - 1)).getBlock();

                            if (!RegistrarBloodMagicBlocks.DECORATIVE_BRICK.equals(block)) {
                                System.out.println("Border Detected");
                                boolList[i][j - 1] = true;
                                isReady = false;
                            }
                        }

                        if (i + 1 <= 2 * range && !boolList[i + 1][j]) {
                            Block block = world.getBlockState(new BlockPos(x - range + i + 1, y + 1, z - range + j)).getBlock();

                            if (!RegistrarBloodMagicBlocks.DECORATIVE_BRICK.equals(block)) {
                                System.out.println("Border Detected");
                                boolList[i + 1][j] = true;
                                isReady = false;
                            }
                        }

                        if (j + 1 <= 2 * range && !boolList[i][j + 1]) {
                            Block block = world.getBlockState(new BlockPos(x - range + i, y + 1, z - range + j + 1)).getBlock();

                            if (!RegistrarBloodMagicBlocks.DECORATIVE_BRICK.equals(block)) {
                                System.out.println("Border detected");
                                boolList[i][j + 1] = true;
                                isReady = false;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Area detected");

        float temperature = 0.5f;
        float humidity = 0.5f;
        float acceptableRange = 0.1f;
        int biomeSkip = 0;

        boolean isItemConsumed = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                BlockPos tilePos = new BlockPos(x + i, y, z + j);

                TileEntity tile = world.getTileEntity(tilePos);

                if (tile == null) {
                    continue;
                }
                IItemHandler inv = Utils.getInventory(tile, null);
                if (inv != null) {
                    ItemStack itemStack = inv.getStackInSlot(0);
                    System.out.println(itemStack);

                    if (itemStack != null && !itemStack.isEmpty()) {
                        if (!(masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(this.getRefreshCost())) == this.getRefreshCost())) {
                            BlockPos targetPos = world.getPlayerEntityByUUID(masterRitualStone.getOwner()).getPosition();
                            System.out.println(targetPos);
                            world.addWeatherEffect(new EntityLightningBolt(world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), false));
                            if (world.rand.nextInt(4) == 0)
                                break;
                        }
                        Item itemTest = itemStack.getItem();

                        if (itemTest instanceof ItemBlock) {
                            ItemBlock item = (ItemBlock) itemTest;

                            if (item.equals(Item.getItemFromBlock(Blocks.SAND))) {
                                humidity -= 0.1f * itemStack.getCount();
                                isItemConsumed = true;
                            } else if (item.equals(Item.getItemFromBlock(Blocks.LAPIS_BLOCK))) {
                                humidity += 0.4f * itemStack.getCount();
                                isItemConsumed = true;
                            } else if (item.equals(Item.getItemFromBlock(Blocks.SANDSTONE))) {
                                humidity -= 0.2f * itemStack.getCount();
                                isItemConsumed = true;
                            } else if (item.equals(Item.getItemFromBlock(Blocks.NETHERRACK))) {
                                humidity -= 0.4f * itemStack.getCount();
                                isItemConsumed = true;
                            } else if (item.equals(Item.getItemFromBlock(Blocks.COAL_BLOCK))) {
                                temperature += 0.2f * itemStack.getCount();
                                isItemConsumed = true;
                            } else if (item.equals(Item.getItemFromBlock(Blocks.ICE))) {
                                temperature -= 0.4f * itemStack.getCount();
                                isItemConsumed = true;
                            } else if (item.equals(Item.getItemFromBlock(Blocks.SNOW))) {
                                temperature -= 0.2f * itemStack.getCount();
                                isItemConsumed = true;
                            } else if (item.equals(Item.getItemFromBlock(Blocks.WOOL))) {
                                biomeSkip = (itemStack.getItemDamage() + 1) * itemStack.getCount();
                                isItemConsumed = true;
                            }
                        } else if (itemTest.equals(Items.DYE) && itemStack.getItemDamage() == 4) {
                            humidity += 0.1f * itemStack.getCount();
                            isItemConsumed = true;
                        } else if (itemTest.equals(Items.LAVA_BUCKET)) {
                            temperature += 0.4f * itemStack.getCount();
                            isItemConsumed = true;
                        } else if (itemTest.equals(Items.WATER_BUCKET)) {
                            humidity += 0.2f * itemStack.getCount();
                            isItemConsumed = true;
                        } else if (itemTest.equals(Items.COAL)) {
                            temperature += 0.1f * itemStack.getCount();
                            isItemConsumed = true;
                        } else if (itemTest.equals(Items.SNOWBALL)) {
                            temperature -= 0.1f * itemStack.getCount();
                            isItemConsumed = true;
                        }
                        if (isItemConsumed) {
                            inv.extractItem(0, inv.getSlotLimit(0), false);
                            IBlockState state = world.getBlockState(tilePos);
                            world.notifyBlockUpdate(tilePos, state, state, 3);
                            world.addWeatherEffect(new EntityLightningBolt(world, x + i, y + 1, z + j, false));
                        }
                    }
                }
            }
        }

        int biomeID = 1;
        int iteration = 0;

        for (Biome biome : Biome.REGISTRY) {
            if (biome == null) {
                continue;
            }

            float temp = biome.getDefaultTemperature();
            float rainfall = biome.getRainfall();
            temperature = Math.min(2.0f, Math.max(0.0f, temperature));
            humidity = Math.min(2.0f, Math.max(0.0f, humidity));

            if (Math.abs(rainfall - humidity) < acceptableRange && Math.abs(temperature - temp) < acceptableRange) {
                biomeID = iteration;
                if (biomeSkip == 0) {
                    break;
                } else {
                    biomeSkip--;
                }
            }

            iteration++;
        }

        // Default to Plains if too much biome skip is used
        if (biomeSkip != 0) {
            biomeID = 1;
        }

        for (int i = 0; i < 2 * range + 1; i++) {
            for (int j = 0; j < 2 * range + 1; j++) {
                if (boolList[i][j]) {
                    Chunk chunk = world.getChunk(new BlockPos(x - range + i, y, z - range + j));
                    byte[] byteArray = chunk.getBiomeArray();
                    int moduX = (x - range + i) % 16;
                    int moduZ = (z - range + j) % 16;

                    if (moduX < 0) {
                        moduX = moduX + 16;
                    }

                    if (moduZ < 0) {
                        moduZ = moduZ + 16;
                    }

                    byteArray[moduZ * 16 + moduX] = (byte) biomeID;
                    chunk.setBiomeArray(byteArray);
                }
            }
        }


        if (rawDrained > 0)
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawDrained, true);
        if (corrosiveDrained > 0)
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveDrained, true);
        if (destructiveDrained > 0)
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DESTRUCTIVE, destructiveDrained, true);
        if (steadfastDrained > 0)
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, steadfastDrained, true);
        if (vengefulDrained > 0)
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.VENGEFUL, vengefulDrained, true);

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
        masterRitualStone.setActive(false);
    }

    @Override
    public boolean activateRitual(IMasterRitualStone masterRitualStone, EntityPlayer player, UUID owner) {
        masterRitualStone.setCooldown(cooldown);
        return true;
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return 20000;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addOffsetRunes(components, 1, 2, 0, EnumRuneType.AIR);
        addOffsetRunes(components, 1, 3, 0, EnumRuneType.AIR);

        addCornerRunes(components, 3, 0, EnumRuneType.EARTH);
        addOffsetRunes(components, 3, 4, 0, EnumRuneType.EARTH);

        addOffsetRunes(components, 4, 5, 0, EnumRuneType.FIRE);

        addParallelRunes(components, 5, 0, EnumRuneType.WATER);

        addOffsetRunes(components, 1, 6, 0, EnumRuneType.WATER);

        addOffsetRunes(components, 1, 8, 0, EnumRuneType.BLANK);
        addParallelRunes(components, 8, 0, EnumRuneType.BLANK);

        addOffsetRunes(components, 10, 1, 0, EnumRuneType.DUSK);
        addParallelRunes(components, 10, 0, EnumRuneType.DUSK);

        addRune(components, 6, 0, -6, EnumRuneType.AIR);
        addRune(components, -6, 0, 6, EnumRuneType.AIR);
        addRune(components, -6, 0, -6, EnumRuneType.FIRE);
        addRune(components, 6, 0, 6, EnumRuneType.FIRE);

        addRune(components, 6, 0, -7, EnumRuneType.AIR);
        addRune(components, 7, 0, -6, EnumRuneType.AIR);
        addRune(components, -6, 0, 7, EnumRuneType.AIR);
        addRune(components, -7, 0, 6, EnumRuneType.AIR);
        addRune(components, 6, 0, 7, EnumRuneType.FIRE);
        addRune(components, 7, 0, 6, EnumRuneType.FIRE);
        addRune(components, -6, 0, -7, EnumRuneType.FIRE);
        addRune(components, -7, 0, -6, EnumRuneType.FIRE);

        addRune(components, 7, 0, -5, EnumRuneType.EARTH);
        addRune(components, 5, 0, -7, EnumRuneType.EARTH);
        addRune(components, -7, 0, 5, EnumRuneType.EARTH);
        addRune(components, -5, 0, 7, EnumRuneType.EARTH);
        addRune(components, 7, 0, 5, EnumRuneType.WATER);
        addRune(components, 5, 0, 7, EnumRuneType.WATER);
        addRune(components, -7, 0, -5, EnumRuneType.WATER);
        addRune(components, -5, 0, -7, EnumRuneType.WATER);

        addOffsetRunes(components, 8, 5, 0, EnumRuneType.DUSK);

        addRune(components, 4, 0, -8, EnumRuneType.EARTH);
        addRune(components, -4, 0, 8, EnumRuneType.EARTH);
        addRune(components, 8, 0, -4, EnumRuneType.EARTH);
        addRune(components, -8, 0, 4, EnumRuneType.EARTH);

        addRune(components, -4, 0, 9, EnumRuneType.EARTH);
        addRune(components, 4, 0, -9, EnumRuneType.EARTH);
        addRune(components, 9, 0, -4, EnumRuneType.EARTH);
        addRune(components, -9, 0, 4, EnumRuneType.EARTH);

        addRune(components, 8, 0, 4, EnumRuneType.WATER);
        addRune(components, 4, 0, 8, EnumRuneType.WATER);
        addRune(components, -8, 0, -4, EnumRuneType.WATER);
        addRune(components, -4, 0, -8, EnumRuneType.WATER);

        addRune(components, 9, 0, 4, EnumRuneType.WATER);
        addRune(components, 4, 0, 9, EnumRuneType.WATER);
        addRune(components, -9, 0, -4, EnumRuneType.WATER);
        addRune(components, -4, 0, -9, EnumRuneType.WATER);
    }


    @Override
    public Ritual getNewCopy() {
        return new RitualGaiaTransformation();
    }
}