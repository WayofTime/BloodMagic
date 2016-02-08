package WayofTime.bloodmagic.item;

import java.util.List;
import java.util.Set;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.ItemStackWrapper;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ItemBoundPickaxe extends ItemBoundTool
{
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone, Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore, Blocks.double_stone_slab, Blocks.golden_rail, Blocks.gold_block, Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.mossy_cobblestone, Blocks.netherrack, Blocks.packed_ice, Blocks.rail, Blocks.redstone_ore, Blocks.sandstone, Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab);

    public ItemBoundPickaxe()
    {
        super("pickaxe", 5, 5, EFFECTIVE_ON);
        setRegistryName(Constants.BloodMagicItem.BOUND_PICKAXE.getRegName());
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
        if (world.isRemote)
            return;

        boolean silkTouch = EnchantmentHelper.getSilkTouchModifier(player);
        int fortuneLvl = EnchantmentHelper.getFortuneModifier(player);
        int range = (int) (charge / 6); //Charge is a max of 30 - want 5 to be the max

        HashMultiset<ItemStackWrapper> drops = HashMultiset.create();

        BlockPos playerPos = player.getPosition();

        for (int i = -range; i <= range; i++)
        {
            for (int j = 0; j <= 2 * range; j++)
            {
                for (int k = -range; k <= range; k++)
                {
                    BlockPos blockPos = playerPos.add(i, j, k);
                    BlockStack blockStack = BlockStack.getStackFromPos(world, blockPos);

                    if (blockStack.getBlock().isAir(world, blockPos))
                        break;

                    BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, blockPos, blockStack.getState(), player);
                    if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
                        break;

                    if (blockStack.getBlock() != null && blockStack.getBlock().getBlockHardness(world, blockPos) != -1)
                    {
                        float strengthVsBlock = getStrVsBlock(stack, blockStack.getBlock());

                        if (strengthVsBlock > 1.1F && world.canMineBlockBody(player, blockPos))
                        {
                            if (silkTouch && blockStack.getBlock().canSilkHarvest(world, blockPos, world.getBlockState(blockPos), player))
                                drops.add(new ItemStackWrapper(blockStack));
                            else
                            {
                                List<ItemStack> itemDrops = blockStack.getBlock().getDrops(world, blockPos, world.getBlockState(blockPos), fortuneLvl);

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

        ItemBindable.syphonNetwork(stack, player, (int) (charge * charge * charge / 2.7));

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
