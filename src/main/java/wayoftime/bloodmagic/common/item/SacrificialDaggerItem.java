package wayoftime.bloodmagic.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.attribute.BMAttributes;
import wayoftime.bloodmagic.common.blockentity.BloodAltarTile;
import wayoftime.bloodmagic.common.dataattachment.BMDataAttachments;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.event.SacrificialDaggerEvent;
import wayoftime.bloodmagic.util.AltarUtil;


public class SacrificialDaggerItem extends Item {
    public SacrificialDaggerItem() {
        super(new Properties().stacksTo(1).component(BMDataComponents.INCENSE, false));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player instanceof FakePlayer) {
            return super.use(level, player, hand);
        }

        boolean isCeremonial = player.getMainHandItem().getOrDefault(BMDataComponents.INCENSE, false);
        double conversion = BloodMagic.SERVER_CONFIG.SELF_SACRIFICE_CONVERSION.get();
        BlockPos altarPos = AltarUtil.findAltar(level, player.blockPosition(), 2);
        int healthSacrificed = 2;
        AttributeInstance attribute = player.getAttribute(BMAttributes.SELF_SACRIFICE_MULTIPLIER);
        int lpAdded = (int) (healthSacrificed * conversion * attribute.getValue()); // TODO probably want to put this in a helper/util class since theres a ritual for this

        if (!player.getAbilities().instabuild) {
            if (isCeremonial) {
                healthSacrificed = (int) (player.getHealth() - player.getMaxHealth() / 10F);
                conversion *= (1 + player.getData(BMDataAttachments.INCENSE));
            }
            SacrificialDaggerEvent event = NeoForge.EVENT_BUS.post(new SacrificialDaggerEvent(player, true, true, healthSacrificed, (int) (healthSacrificed * conversion)));
            if (event.isCanceled()) {
                return super.use(level, player, hand);
            }
            if (event.shouldDrainHealth) {
                player.invulnerableTime = 0;
                player.hurt(AltarUtil.sacrificeDamage(player), event.hpLost);
            }
            lpAdded = event.lpAdded;
            if (!event.shouldFillAltar) {
                return super.use(level, player, hand);
            }
        } else if (player.isShiftKeyDown()) {
            lpAdded = Integer.MAX_VALUE;
        }

        double posX = player.getX();
        double posY = player.getY();
        double posZ = player.getZ();

        level.playSound(player, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat() * 0.8F));
        for (int i = 0; i < 8; i++) {
            level.addParticle(DustParticleOptions.REDSTONE, posX + level.random.nextDouble() - level.random.nextDouble(), posY + level.random.nextDouble() - level.random.nextDouble(), posZ + level.random.nextDouble() - level.random.nextDouble(), 0, 0, 0);
        }

        // TODO soul fray?

        if (altarPos == null) {
            return super.use(level, player, hand);
        }
        BlockEntity be = level.getBlockEntity(altarPos);
        if (be instanceof BloodAltarTile altar) {
            altar.sacrificialDaggerCall(lpAdded, false);
        }

        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player) {
            boolean state = stack.getOrDefault(BMDataComponents.INCENSE, false);
            boolean playerState = player.getData(BMDataAttachments.INCENSE) > 0;
            if (playerState && !state) {
                stack.set(BMDataComponents.INCENSE, true);
            } else if (!playerState && state) {
                stack.set(BMDataComponents.INCENSE, false);
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrDefault(BMDataComponents.INCENSE, false);
    }
}
