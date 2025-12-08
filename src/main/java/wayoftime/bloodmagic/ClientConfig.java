package wayoftime.bloodmagic;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    public final ModConfigSpec.BooleanValue ALWAYS_RENDER_NODE_LINES;

    protected ClientConfig(ModConfigSpec.Builder builder) {
        ALWAYS_RENDER_NODE_LINES = builder.define("always_render_lines", false);
    }
}
