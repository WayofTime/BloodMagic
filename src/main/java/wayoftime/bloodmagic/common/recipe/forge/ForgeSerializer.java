package wayoftime.bloodmagic.common.recipe.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;

public class ForgeSerializer implements RecipeSerializer<ForgeRecipe> {

    public static final MapCodec<ForgeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.fieldOf("minDrain").forGetter(ForgeRecipe::getMinWill),
            Codec.DOUBLE.fieldOf("drain").forGetter(ForgeRecipe::getDrain),
            Codec.list(Ingredient.CODEC_NONEMPTY).fieldOf("inputs").forGetter(ForgeRecipe::getCraftingIngredients),
            ItemStack.CODEC.fieldOf("output").forGetter(ForgeRecipe::getOutput),
            EnumWillType.CODEC.optionalFieldOf("willType").forGetter(ForgeRecipe::getWillType)
    ).apply(instance, ForgeRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ForgeRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, ForgeRecipe::getMinWill,
            ByteBufCodecs.DOUBLE, ForgeRecipe::getDrain,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), ForgeRecipe::getCraftingIngredients,
            ItemStack.STREAM_CODEC, ForgeRecipe::getOutput,
            EnumWillType.STREAM_CODEC.apply(ByteBufCodecs::optional), ForgeRecipe::getWillType,
            ForgeRecipe::new
    );

    @Override
    public MapCodec<ForgeRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ForgeRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
