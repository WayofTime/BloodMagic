package wayoftime.bloodmagic.common.recipe.tiered;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class EnergyTieredSerializer implements RecipeSerializer<EnergyTieredRecipe> {
    public static final MapCodec<EnergyTieredRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            CraftingBookCategory.CODEC.fieldOf("category").forGetter(EnergyTieredRecipe::category),
            ShapedRecipePattern.MAP_CODEC.fieldOf("pattern").forGetter(EnergyTieredRecipe::getPattern),
            Codec.INT.fieldOf("primary").forGetter(EnergyTieredRecipe::getPrimary),
            Codec.INT.fieldOf("secondary").forGetter(EnergyTieredRecipe::getSecondary),
            ItemStack.CODEC.fieldOf("result").forGetter(EnergyTieredRecipe::getOutput)
    ).apply(builder, EnergyTieredRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, EnergyTieredRecipe> STREAM_CODEC = StreamCodec.of(EnergyTieredSerializer::toNetwork, EnergyTieredSerializer::fromNetwork);

    private static EnergyTieredRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
        return null;
    }

    private static void toNetwork(RegistryFriendlyByteBuf buf, EnergyTieredRecipe recipe) {}

    @Override
    public MapCodec<EnergyTieredRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, EnergyTieredRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
