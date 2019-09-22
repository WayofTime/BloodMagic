package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.DamageSourceBloodMagic;
import WayofTime.bloodmagic.util.helper.PlayerSacrificeHelper;
import WayofTime.bloodmagic.util.helper.PurificationHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nonnull;

public class ItemDaggerOfSacrifice extends Item implements IVariantProvider {
    public ItemDaggerOfSacrifice() {
        super();
        setTranslationKey(BloodMagic.MODID + ".daggerOfSacrifice");
        setCreativeTab(BloodMagic.TAB_BM);
        setMaxStackSize(1);
        setFull3D();
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof FakePlayer)
            return false;

        if (target == null || attacker == null || attacker.getEntityWorld().isRemote || (attacker instanceof PlayerEntity && !(attacker instanceof ServerPlayerEntity)))
            return false;

        if (!target.isNonBoss())
            return false;

        if (target instanceof PlayerEntity)
            return false;

        if (target.isChild() && !(target instanceof IMob))
            return false;

        if (target.isDead || target.getHealth() < 0.5F)
            return false;

        EntityEntry entityEntry = EntityRegistry.getEntry(target.getClass());
        if (entityEntry == null)
            return false;
        int lifeEssenceRatio = BloodMagicAPI.INSTANCE.getValueManager().getSacrificial().getOrDefault(entityEntry.getRegistryName(), 25);

        if (lifeEssenceRatio <= 0)
            return false;

        int lifeEssence = (int) (lifeEssenceRatio * target.getHealth());
        if (target instanceof AnimalEntity) {
            lifeEssence = (int) (lifeEssence * (1 + PurificationHelper.getCurrentPurity((AnimalEntity) target)));
        }

        if (target.isChild()) {
            lifeEssence *= 0.5F;
        }

        if (PlayerSacrificeHelper.findAndFillAltar(attacker.getEntityWorld(), target, lifeEssence, true)) {
            target.getEntityWorld().playSound(null, target.posX, target.posY, target.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (target.getEntityWorld().rand.nextFloat() - target.getEntityWorld().rand.nextFloat()) * 0.8F);
            target.setHealth(-1);
            target.onDeath(DamageSourceBloodMagic.INSTANCE);
        }

        return false;
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=normal");
    }
}
