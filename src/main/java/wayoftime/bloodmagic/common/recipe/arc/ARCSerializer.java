package wayoftime.bloodmagic.common.recipe.arc;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;

public class ARCSerializer implements RecipeSerializer<ARCRecipe> {

    private static final StreamCodec<RegistryFriendlyByteBuf, Pair<ItemStack, Double>> CHANCE_PAIR_STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, Pair::getFirst,
            ByteBufCodecs.DOUBLE, Pair::getSecond,
            Pair::new
    );

    public static final MapCodec<ARCRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("tool").forGetter(ARCRecipe::getTool),
            Ingredient.CODEC.fieldOf("input").forGetter(ARCRecipe::getInput),
            ItemStack.CODEC.listOf().fieldOf("guaranteed_outputs").forGetter(ARCRecipe::getGuaranteedOutput),
            Codec.pair(ItemStack.CODEC.fieldOf("item").codec(), Codec.DOUBLE.fieldOf("chance").codec()).listOf().fieldOf("chance_outputs").forGetter(ARCRecipe::getChanceOutput),
            FluidStack.CODEC.optionalFieldOf("input_fluid").forGetter(ARCRecipe::getInputFluid),
            FluidStack.CODEC.optionalFieldOf("output_fluid").forGetter(ARCRecipe::getOutputFluid)
    ).apply(inst, ARCRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ARCRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, ARCRecipe::getTool,
            Ingredient.CONTENTS_STREAM_CODEC, ARCRecipe::getInput,
            ItemStack.LIST_STREAM_CODEC, ARCRecipe::getGuaranteedOutput,
            CHANCE_PAIR_STREAM_CODEC.apply(ByteBufCodecs.list()), ARCRecipe::getChanceOutput,
            FluidStack.STREAM_CODEC.apply(ByteBufCodecs::optional), ARCRecipe::getInputFluid,
            FluidStack.STREAM_CODEC.apply(ByteBufCodecs::optional), ARCRecipe::getOutputFluid,
            ARCRecipe::new
    );

    @Override
    public MapCodec<ARCRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ARCRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
