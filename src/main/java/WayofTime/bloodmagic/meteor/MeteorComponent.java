package WayofTime.bloodmagic.meteor;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@Getter
@Setter
@AllArgsConstructor
public class MeteorComponent
{
    public int weight;
    public String oreName;

    public IBlockState getStateFromOre()
    {
        List<ItemStack> list = OreDictionary.getOres(oreName);
        if (list != null && !list.isEmpty())
        {
            for (ItemStack stack : list)
            {
                if (stack != null && stack.getItem() instanceof ItemBlock)
                {
                    Block block = ((ItemBlock) stack.getItem()).getBlock();
                    IBlockState state = block.getStateFromMeta(stack.getItemDamage());

                    return state;
                }
            }
        }

        return null;
    }
}
