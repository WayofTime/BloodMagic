package WayofTime.bloodmagic.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameData;

@Getter
@EqualsAndHashCode
public class BlockStack
{
    private final Block block;
    private final int meta;

    public BlockStack(Block block, int meta)
    {
        this.block = block;
        this.meta = meta;
    }

    public BlockStack(Block block)
    {
        this(block, 0);
    }

    @Override
    public String toString()
    {
        return GameData.getBlockRegistry().getNameForObject(block) + ":" + meta;
    }
}
