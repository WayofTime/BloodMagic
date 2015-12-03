package WayofTime.bloodmagic.compat.jei.binding;

import WayofTime.bloodmagic.compat.jei.BloodMagicRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class BindingRecipeJEI extends BloodMagicRecipeWrapper {

    @Nonnull
    private final ItemStack input;

    @Nonnull
    private final ItemStack output;

    public BindingRecipeJEI(@Nonnull ItemStack input, @Nonnull ItemStack output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean usesOreDictionaryComparison() {
        return false;
    }

    @Override
    public List getInputs() {
        return Collections.singletonList(input);
    }

    @Override
    public List getOutputs() {
        return Collections.singletonList(output);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft) {

    }
}
