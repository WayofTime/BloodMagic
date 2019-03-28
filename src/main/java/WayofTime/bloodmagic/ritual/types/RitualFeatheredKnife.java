package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.altar.IBloodAltar;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.util.helper.PlayerSacrificeHelper;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSelfSacrifice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("feathered_knife")
public class RitualFeatheredKnife extends Ritual {
    public static final String ALTAR_RANGE = "altar";
    public static final String DAMAGE_RANGE = "damage";

    public static double rawWillDrain = 0.05;
    public static double destructiveWillDrain = 0.05;
    public static double corrosiveWillThreshold = 10;
    public static double steadfastWillThreshold = 10;
    public static double vengefulWillThreshold = 10;
    public static int defaultRefreshTime = 20;
    public int refreshTime = 20;
    public BlockPos altarOffsetPos = new BlockPos(0, 0, 0); //TODO: Save!

    public RitualFeatheredKnife() {
        super("ritualFeatheredKnife", 0, 25000, "ritual." + BloodMagic.MODID + ".featheredKnifeRitual");
        addBlockRange(ALTAR_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-5, -10, -5), 11, 21, 11));
        addBlockRange(DAMAGE_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-15, -20, -15), 31, 41, 31));

        setMaximumVolumeAndDistanceOfRange(ALTAR_RANGE, 0, 10, 15);
        setMaximumVolumeAndDistanceOfRange(DAMAGE_RANGE, 0, 25, 15);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

        double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);
        double destructiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DESTRUCTIVE, willConfig);
        double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
        double steadfastWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.STEADFAST, willConfig);
        double vengefulWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.VENGEFUL, willConfig);

        refreshTime = getRefreshTimeForRawWill(rawWill);

        boolean consumeRawWill = rawWill >= rawWillDrain && refreshTime != defaultRefreshTime;

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        BlockPos altarPos = pos.add(altarOffsetPos);

        TileEntity tile = world.getTileEntity(altarPos);

        AreaDescriptor altarRange = masterRitualStone.getBlockRange(ALTAR_RANGE);

        if (!altarRange.isWithinArea(altarOffsetPos) || !(tile instanceof IBloodAltar)) {
            for (BlockPos newPos : altarRange.getContainedPositions(pos)) {
                TileEntity nextTile = world.getTileEntity(newPos);
                if (nextTile instanceof IBloodAltar) {
                    tile = nextTile;
                    altarOffsetPos = newPos.subtract(pos);

                    altarRange.resetCache();
                    break;
                }
            }
        }

        boolean useIncense = corrosiveWill >= corrosiveWillThreshold;

        if (tile instanceof IBloodAltar) {
            IBloodAltar tileAltar = (IBloodAltar) tile;

            AreaDescriptor damageRange = masterRitualStone.getBlockRange(DAMAGE_RANGE);
            AxisAlignedBB range = damageRange.getAABB(pos);

            double destructiveDrain = 0;

            List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, range);

            for (EntityPlayer player : entities) {
                float healthThreshold = steadfastWill >= steadfastWillThreshold ? 0.7f : 0.3f;

                if (vengefulWill >= vengefulWillThreshold && !player.getGameProfile().getId().equals(masterRitualStone.getOwner())) {
                    healthThreshold = 0.1f;
                }

                float health = player.getHealth();
                float maxHealth = player.getMaxHealth();

                float sacrificedHealth = 1;
                double lpModifier = 1;

                if ((health / player.getMaxHealth() > healthThreshold) && (!useIncense || !player.isPotionActive(RegistrarBloodMagic.SOUL_FRAY))) {
                    if (useIncense) {
                        double incenseAmount = PlayerSacrificeHelper.getPlayerIncense(player);

                        sacrificedHealth = health - maxHealth * healthThreshold;
                        lpModifier *= PlayerSacrificeHelper.getModifier(incenseAmount);

                        PlayerSacrificeHelper.setPlayerIncense(player, 0);
                        player.addPotionEffect(new PotionEffect(RegistrarBloodMagic.SOUL_FRAY, PlayerSacrificeHelper.soulFrayDuration));
                    }

                    if (destructiveWill >= destructiveWillDrain * sacrificedHealth) {
                        lpModifier *= getLPModifierForWill(destructiveWill);
                        destructiveWill -= destructiveWillDrain * sacrificedHealth;
                        destructiveDrain += destructiveWillDrain * sacrificedHealth;
                    }

                    if (LivingArmour.hasFullSet(player)) {
                        ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                        LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                        if (armour != null) {
                            LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.selfSacrifice", chestStack);

                            if (upgrade instanceof LivingArmourUpgradeSelfSacrifice) {
                                double modifier = ((LivingArmourUpgradeSelfSacrifice) upgrade).getSacrificeModifier();

                                lpModifier *= (1 + modifier);
                            }
                        }
                    }

                    player.setHealth(health - sacrificedHealth);

                    tileAltar.sacrificialDaggerCall((int) (ConfigHandler.values.sacrificialDaggerConversion * lpModifier * sacrificedHealth), false);

                    totalEffects++;

                    if (totalEffects >= maxEffects) {
                        break;
                    }

                }
            }

            if (destructiveDrain > 0) {
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.STEADFAST, destructiveDrain, true);
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
        if (totalEffects > 0 && consumeRawWill) {
            WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawWillDrain, true);
        }
    }

    @Override
    public int getRefreshTime() {
        return refreshTime;
    }

    @Override
    public int getRefreshCost() {
        return 20;
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        addParallelRunes(components, 1, 0, EnumRuneType.DUSK);
        addParallelRunes(components, 2, -1, EnumRuneType.WATER);
        addCornerRunes(components, 1, -1, EnumRuneType.AIR);
        addOffsetRunes(components, 2, 4, -1, EnumRuneType.FIRE);
        addOffsetRunes(components, 2, 4, 0, EnumRuneType.EARTH);
        addOffsetRunes(components, 4, 3, 0, EnumRuneType.EARTH);
        addCornerRunes(components, 3, 0, EnumRuneType.AIR);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualFeatheredKnife();
    }

    @Override
    public ITextComponent[] provideInformationOfRitualToPlayer(EntityPlayer player) {
        return new ITextComponent[]{new TextComponentTranslation(this.getTranslationKey() + ".info"), new TextComponentTranslation(this.getTranslationKey() + ".default.info"), new TextComponentTranslation(this.getTranslationKey() + ".corrosive.info"), new TextComponentTranslation(this.getTranslationKey() + ".steadfast.info"), new TextComponentTranslation(this.getTranslationKey() + ".destructive.info"), new TextComponentTranslation(this.getTranslationKey() + ".vengeful.info")};
    }

    public double getLPModifierForWill(double destructiveWill) {
        return 1 + destructiveWill * 0.2 / 100;
    }

    public int getRefreshTimeForRawWill(double rawWill) {
        if (rawWill >= rawWillDrain) {
            return 10;
        }

        return defaultRefreshTime;
    }
}
