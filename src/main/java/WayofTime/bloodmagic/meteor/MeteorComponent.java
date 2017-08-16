package WayofTime.bloodmagic.meteor;

import WayofTime.bloodmagic.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class MeteorComponent {
    public int weight;
    public String oreName;

    public MeteorComponent(int weight, String oreName) {
        this.weight = weight;
        this.oreName = oreName;
    }

    public IBlockState getStateFromOre() {
        if (oreName.contains(":")) {
            String[] stringList = oreName.split(":");
            String domain = stringList[0];
            String block = stringList[1];
            int meta = 0;
            if (stringList.length > 2 && Utils.isInteger(stringList[2])) {
                meta = Integer.parseInt(stringList[2]);
            }

            Block ore = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(domain, block));
            if (ore != Blocks.AIR) {
                return ore.getStateFromMeta(meta);
            }
        }

        List<ItemStack> list = OreDictionary.getOres(oreName);
        if (list != null && !list.isEmpty()) {
            for (ItemStack stack : list) {
                if (stack != null && stack.getItem() instanceof ItemBlock) {
                    Block block = ((ItemBlock) stack.getItem()).getBlock();
                    IBlockState state = block.getStateFromMeta(stack.getItemDamage());

                    return state;
                }
            }
        }

        return null;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getOreName() {
        return oreName;
    }

    public void setOreName(String oreName) {
        this.oreName = oreName;
    }
}
