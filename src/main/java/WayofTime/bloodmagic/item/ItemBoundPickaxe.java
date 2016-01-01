package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.api.ItemStackWrapper;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class ItemBoundPickaxe extends ItemBoundTool
{
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone, Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore, Blocks.double_stone_slab, Blocks.golden_rail, Blocks.gold_block, Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.mossy_cobblestone, Blocks.netherrack, Blocks.packed_ice, Blocks.rail, Blocks.redstone_ore, Blocks.sandstone, Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab);

    public ItemBoundPickaxe()
    {
        super("pickaxe", 5, EFFECTIVE_ON);
    }

    @Override
    public boolean canHarvestBlock(Block blockIn)
    {
        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block)
    {
        return block.getMaterial() != Material.iron && block.getMaterial() != Material.anvil && block.getMaterial() != Material.rock ? super.getStrVsBlock(stack, block) : 12F;
    }

    @Override
    protected void onBoundRelease(ItemStack stack, World world, EntityPlayer player, int charge)
    {
        boolean silkTouch = EnchantmentHelper.getSilkTouchModifier(player);
        int fortuneLvl = EnchantmentHelper.getFortuneModifier(player);
        int range = (int) (charge * 0.25);

        HashMultiset<ItemStackWrapper> drops = HashMultiset.create();

        BlockPos playerPos = player.getPosition().add(0, -1, 0);

        for (int i = -range; i <= range; i++)
        {
            for (int j = -range; j <= range; j++)
            {
                for (int k = -range; k <= range; k++)
                {
                    BlockPos blockPos = playerPos.add(i, j, k);
                    Block block = world.getBlockState(blockPos).getBlock();
                    int blockMeta = block.getMetaFromState(world.getBlockState(blockPos));

                    if (block != null && block.getBlockHardness(world, blockPos) != -1)
                    {
                        float strengthVsBlock = getStrVsBlock(stack, block);

                        if (strengthVsBlock > 1.1F && world.canMineBlockBody(player, blockPos))
                        {
                            if (silkTouch && block.canSilkHarvest(world, blockPos, world.getBlockState(blockPos), player))
                                drops.add(new ItemStackWrapper(block, 1, blockMeta));
                            else
                            {
                                List<ItemStack> itemDrops = block.getDrops(world, blockPos, world.getBlockState(blockPos), fortuneLvl);

                                if (itemDrops != null)
                                    for (ItemStack stacks : itemDrops)
                                        drops.add(ItemStackWrapper.getHolder(stacks));
                            }

                            world.setBlockToAir(blockPos);
                        }
                    }
                }
            }
        }

        world.createExplosion(player, playerPos.getX(), playerPos.getY(), playerPos.getZ(), 0.5F, false);
        dropStacks(drops, world, playerPos.add(0, 1, 0));
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", 5, 0));
        return multimap;
    }
}
