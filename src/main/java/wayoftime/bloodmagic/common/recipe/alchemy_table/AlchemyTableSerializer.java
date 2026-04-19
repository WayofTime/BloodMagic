package wayoftime.bloodmagic.common.recipe.alchemy_table;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AlchemyTableSerializer implements RecipeSerializer<AlchemyTableRecipe> {

    public static final MapCodec<AlchemyTableRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Ingredient.CODEC_NONEMPTY.listOf().fieldOf("inputs").forGetter(AlchemyTableRecipe::inputs),
            Codec.INT.fieldOf("essence").forGetter(AlchemyTableRecipe::essence),
            Codec.INT.fieldOf("tier").forGetter(AlchemyTableRecipe::tier),
            Codec.INT.fieldOf("duration").forGetter(AlchemyTableRecipe::duration),
            ItemStack.CODEC.fieldOf("output").forGetter(AlchemyTableRecipe::output)
    ).apply(builder, AlchemyTableRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyTableRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), AlchemyTableRecipe::inputs,
            ByteBufCodecs.INT, AlchemyTableRecipe::essence,
            ByteBufCodecs.INT, AlchemyTableRecipe::tier,
            ByteBufCodecs.INT, AlchemyTableRecipe::duration,
            ItemStack.STREAM_CODEC, AlchemyTableRecipe::output,
            AlchemyTableRecipe::new
    );

    @Override
    public MapCodec<AlchemyTableRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AlchemyTableRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
