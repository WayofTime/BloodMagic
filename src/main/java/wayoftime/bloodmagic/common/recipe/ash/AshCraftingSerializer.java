package wayoftime.bloodmagic.common.recipe.ash;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AshCraftingSerializer implements RecipeSerializer<AshCraftingRecipe> {

    public static final MapCodec<AshCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Ingredient.CODEC.fieldOf("first").forGetter(AshCraftingRecipe::first),
            Ingredient.CODEC.fieldOf("second").forGetter(AshCraftingRecipe::second),
            ItemStack.CODEC.fieldOf("result").forGetter(AshCraftingRecipe::output)
    ).apply(builder, AshCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AshCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, AshCraftingRecipe::first,
            Ingredient.CONTENTS_STREAM_CODEC, AshCraftingRecipe::second,
            ItemStack.STREAM_CODEC, AshCraftingRecipe::output,
            AshCraftingRecipe::new
    );

    public static final String NAME = "ash_crafting";

    @Override
    public MapCodec<AshCraftingRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AshCraftingRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
