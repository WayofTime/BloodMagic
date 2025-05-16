package wayoftime.bloodmagic.common.recipe.tiered;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class FluidTieredSerializer implements RecipeSerializer<FluidTieredRecipe> {
    public static final MapCodec<FluidTieredRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            CraftingBookCategory.CODEC.fieldOf("category").forGetter(FluidTieredRecipe::category),
            ShapedRecipePattern.MAP_CODEC.fieldOf("pattern").forGetter(FluidTieredRecipe::getPattern),
            Codec.INT.fieldOf("primary").forGetter(FluidTieredRecipe::getPrimary),
            Codec.INT.fieldOf("secondary").forGetter(FluidTieredRecipe::getSecondary),
            ItemStack.CODEC.fieldOf("result").forGetter(FluidTieredRecipe::getOutput)
    ).apply(builder, FluidTieredRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidTieredRecipe> STREAM_CODEC = StreamCodec.of(FluidTieredSerializer::toNetwork, FluidTieredSerializer::fromNetwork);

    private static FluidTieredRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
        CraftingBookCategory category = CraftingBookCategory.STREAM_CODEC.decode(buf);
        ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);
        int primary = buf.readInt();
        int secondary = buf.readInt();
        ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
        return new FluidTieredRecipe(category, pattern, primary, secondary, output);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buf, FluidTieredRecipe recipe) {
        CraftingBookCategory.STREAM_CODEC.encode(buf, recipe.category());
        ShapedRecipePattern.STREAM_CODEC.encode(buf, recipe.getPattern());
        ByteBufCodecs.INT.encode(buf, recipe.getPrimary());
        ByteBufCodecs.INT.encode(buf, recipe.getSecondary());
        ItemStack.STREAM_CODEC.encode(buf, recipe.getOutput());
    }

    @Override
    public MapCodec<FluidTieredRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FluidTieredRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
