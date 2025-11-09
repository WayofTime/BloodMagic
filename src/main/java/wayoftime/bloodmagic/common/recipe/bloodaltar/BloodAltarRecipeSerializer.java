package wayoftime.bloodmagic.common.recipe.bloodaltar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BloodAltarRecipeSerializer implements RecipeSerializer<BloodAltarRecipe> {

    public static final MapCodec<BloodAltarRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(BloodAltarRecipe::getInput),
            ItemStack.CODEC.fieldOf("output").forGetter(BloodAltarRecipe::getResult),
            Codec.INT.fieldOf("minTier").forGetter(BloodAltarRecipe::getMinTier),
            Codec.INT.fieldOf("bloodNeeded").forGetter(BloodAltarRecipe::getTotalBlood),
            Codec.INT.fieldOf("craftSpeed").forGetter(BloodAltarRecipe::getCraftSpeed),
            Codec.INT.fieldOf("drainSpeed").forGetter(BloodAltarRecipe::getDrainSpeed)
    ).apply(instance, BloodAltarRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BloodAltarRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, BloodAltarRecipe::getInput,
            ItemStack.STREAM_CODEC, BloodAltarRecipe::getResult,
            ByteBufCodecs.INT, BloodAltarRecipe::getMinTier,
            ByteBufCodecs.INT, BloodAltarRecipe::getTotalBlood,
            ByteBufCodecs.INT, BloodAltarRecipe::getCraftSpeed,
            ByteBufCodecs.INT, BloodAltarRecipe::getDrainSpeed,
            BloodAltarRecipe::new
    );

    @Override
    public MapCodec<BloodAltarRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BloodAltarRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
