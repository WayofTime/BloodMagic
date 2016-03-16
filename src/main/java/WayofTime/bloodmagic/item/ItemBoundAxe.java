package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.mesh.CustomMeshDefinitionActivatable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ItemStackWrapper;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBoundAxe extends ItemBoundTool implements IMeshProvider
{
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder);

    public ItemBoundAxe()
    {
        super("axe", 7, EFFECTIVE_ON);
        setRegistryName(Constants.BloodMagicItem.BOUND_AXE.getRegName());
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player)
    {
        return true;
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
                        continue;

                    BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, blockPos, blockStack.getState(), player);
                    if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
                        continue;

                    if (blockStack.getBlock().getBlockHardness(world, blockPos) != -1)
                    {
                        float strengthVsBlock = getStrVsBlock(stack, blockStack.getBlock());

                        if (strengthVsBlock > 1.1F || blockStack.getBlock() instanceof BlockLeavesBase && world.canMineBlockBody(player, blockPos))
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

        NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, (int) (charge * charge * charge / 2.7));
        world.createExplosion(player, playerPos.getX(), playerPos.getY(), playerPos.getZ(), 0.1F, false);
        dropStacks(drops, world, playerPos.add(0, 1, 0));
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", 7, 0));
        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition()
    {
        return new CustomMeshDefinitionActivatable("ItemBoundAxe");
    }

    @Override
    public List<String> getVariants()
    {
        List<String> ret = new ArrayList<String>();
        ret.add("active=true");
        ret.add("active=false");
        return ret;
    }
}
