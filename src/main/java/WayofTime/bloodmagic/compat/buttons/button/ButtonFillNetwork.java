package WayofTime.bloodmagic.compat.buttons.button;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.compat.buttons.BloodMagicPlugin;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import tehnut.buttons.api.button.utility.Button;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ButtonFillNetwork extends Button {

    public ButtonFillNetwork() {
        super(BloodMagicPlugin.FILL_BUTTON);

        setServerRequired();
    }

    @Nullable
    @Override
    public List<? extends ITextComponent> getTooltip() {
        return Collections.singletonList(new TextComponentTranslation("button.bloodmagic.tooltip.fill"));
    }

    @Override
    public void onServerClick(EntityPlayerMP player) {
        SoulNetwork network = NetworkHelper.getSoulNetwork(player);
        network.setCurrentEssence(Integer.MAX_VALUE);
    }

    @Override
    public ResourceLocation getButtonId() {
        return new ResourceLocation(Constants.Mod.MODID, "button_fillnetwork");
    }
}
