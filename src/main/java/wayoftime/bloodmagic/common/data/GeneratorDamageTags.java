package wayoftime.bloodmagic.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.registries.BloodMagicDamageTypes;

import java.util.concurrent.CompletableFuture;

public class GeneratorDamageTags extends TagsProvider<DamageType> {
    public GeneratorDamageTags(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.DAMAGE_TYPE, future, BloodMagic.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(BloodMagicDamageTypes.SACRIFICE).add(BloodMagicDamageTypes.RITUAL);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(BloodMagicDamageTypes.SACRIFICE);
        this.tag(DamageTypeTags.NO_IMPACT).add(BloodMagicDamageTypes.SACRIFICE);
    }
}
