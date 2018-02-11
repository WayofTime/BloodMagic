package WayofTime.bloodmagic.api.impl.recipe;

import WayofTime.bloodmagic.BloodMagic;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeAlchemyArray {

    @Nonnull
    private final Ingredient input;
    @Nonnull
    private final Ingredient catalyst;
    @Nonnull
    private final ItemStack output;
    @Nonnull
    private final ResourceLocation circleTexture;

    public RecipeAlchemyArray(@Nonnull Ingredient input, @Nonnull Ingredient catalyst, @Nonnull ItemStack output, @Nullable ResourceLocation circleTexture) {
        Preconditions.checkNotNull(input, "input cannot be null.");
        Preconditions.checkNotNull(catalyst, "catalyst cannot be null.");
        Preconditions.checkNotNull(output, "output cannot be null.");

        this.input = input;
        this.catalyst = catalyst;
        this.output = output;
        this.circleTexture = circleTexture == null ? new ResourceLocation(BloodMagic.MODID, "textures/models/AlchemyArrays/WIPArray.png") : circleTexture;
    }

    @Nonnull
    public Ingredient getInput() {
        return input;
    }

    @Nonnull
    public Ingredient getCatalyst() {
        return catalyst;
    }

    @Nonnull
    public ItemStack getOutput() {
        return output;
    }

    @Nonnull
    public ResourceLocation getCircleTexture() {
        return circleTexture;
    }
}
