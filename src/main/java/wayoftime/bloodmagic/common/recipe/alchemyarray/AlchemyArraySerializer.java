package wayoftime.bloodmagic.common.recipe.alchemyarray;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AlchemyArraySerializer implements RecipeSerializer<AlchemyArrayRecipe> {

    public static final MapCodec<AlchemyArrayRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(AlchemyArrayRecipe::getTexture),
            Ingredient.CODEC_NONEMPTY.fieldOf("baseinput").forGetter(AlchemyArrayRecipe::getBaseInput),
            Ingredient.CODEC_NONEMPTY.fieldOf("addedinput").forGetter(AlchemyArrayRecipe::getAddedInput),
            ItemStack.CODEC.fieldOf("output").forGetter(AlchemyArrayRecipe::getOutput)
    ).apply(instance, AlchemyArrayRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyArrayRecipe> STREAM_CODEC = StreamCodec.of(
            AlchemyArraySerializer::toNetwork,
            AlchemyArraySerializer::fromNetwork
    );

    private static AlchemyArrayRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        ResourceLocation texture = buffer.readResourceLocation();
        Ingredient baseInput = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient addedInput = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
        return new AlchemyArrayRecipe(texture, baseInput, addedInput, output);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, AlchemyArrayRecipe recipe) {
        buffer.writeResourceLocation(recipe.getTexture());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getBaseInput());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getAddedInput());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.getOutput());
    }

    @Override
    public MapCodec<AlchemyArrayRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AlchemyArrayRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
