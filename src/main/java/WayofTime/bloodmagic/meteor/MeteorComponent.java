package WayofTime.bloodmagic.meteor;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.bloodmagic.util.Utils;

@Getter
@Setter
@AllArgsConstructor
public class MeteorComponent
{
    public int weight;
    public String oreName;

    public IBlockState getStateFromOre()
    {
        if (oreName.contains(":"))
        {
            String[] stringList = oreName.split(":");
            String domain = stringList[0];
            String block = stringList[1];
            int meta = 0;
            if (stringList.length > 2 && Utils.isInteger(stringList[2]))
            {
                meta = Integer.parseInt(stringList[2]);
            }

            Block ore = Block.REGISTRY.getObject(new ResourceLocation(domain, block));
            if (ore != null)
            {
                return ore.getStateFromMeta(meta);
            }
        }

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
