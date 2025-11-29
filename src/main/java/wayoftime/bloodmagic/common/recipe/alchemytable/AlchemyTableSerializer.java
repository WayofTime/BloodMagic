package wayoftime.bloodmagic.common.recipe.alchemytable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class AlchemyTableSerializer implements RecipeSerializer<AlchemyTableRecipe> {

    public static final MapCodec<AlchemyTableRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.listOf().fieldOf("input").forGetter(AlchemyTableRecipe::getInput),
            ItemStack.CODEC.fieldOf("output").forGetter(AlchemyTableRecipe::getOutput),
            Codec.INT.fieldOf("syphon").forGetter(AlchemyTableRecipe::getSyphon),
            Codec.INT.fieldOf("ticks").forGetter(AlchemyTableRecipe::getTicks),
            Codec.INT.optionalFieldOf("upgradeLevel", 0).forGetter(AlchemyTableRecipe::getMinimumTier)
    ).apply(instance, AlchemyTableRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyTableRecipe> STREAM_CODEC = StreamCodec.of(
            AlchemyTableSerializer::toNetwork,
            AlchemyTableSerializer::fromNetwork
    );

    private static AlchemyTableRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        int inputSize = buffer.readInt();
        List<Ingredient> inputs = new java.util.ArrayList<>();
        for (int i = 0; i < inputSize; i++) {
            inputs.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        }
        ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
        int syphon = buffer.readInt();
        int ticks = buffer.readInt();
        int minimumTier = buffer.readInt();
        return new AlchemyTableRecipe(inputs, output, syphon, ticks, minimumTier);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, AlchemyTableRecipe recipe) {
        buffer.writeInt(recipe.getInput().size());
        for (Ingredient ingredient : recipe.getInput()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
        ItemStack.STREAM_CODEC.encode(buffer, recipe.getOutput());
        buffer.writeInt(recipe.getSyphon());
        buffer.writeInt(recipe.getTicks());
        buffer.writeInt(recipe.getMinimumTier());
    }

    @Override
    public MapCodec<AlchemyTableRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AlchemyTableRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
