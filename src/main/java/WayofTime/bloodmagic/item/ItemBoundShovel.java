package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.client.mesh.CustomMeshDefinitionActivatable;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.ItemStackWrapper;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;
import java.util.function.Consumer;

public class ItemBoundShovel extends ItemBoundTool implements IMeshProvider {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND);

    public ItemBoundShovel() {
        super("shovel", 1, EFFECTIVE_ON);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState block, BlockPos pos, EntityLivingBase entityLiving) {
        return true;
    }

    @Override
    protected void onBoundRelease(ItemStack stack, World world, EntityPlayer player, int charge) {
        if (world.isRemote)
            return;
        int fortuneLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
        boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;

        int range = charge / 6; //Charge is a max of 30 - want 5 to be the max

        HashMultiset<ItemStackWrapper> drops = HashMultiset.create();

        BlockPos playerPos = player.getPosition();


        for (int i = -range; i <= range; i++) {
            for (int j = 0; j <= 2 * range; j++) {
                for (int k = -range; k <= range; k++) {
                    BlockPos blockPos = playerPos.add(i, j, k);
                    IBlockState blockState = world.getBlockState(blockPos);

                    if (world.isAirBlock(blockPos))
                        continue;

                    Material material = blockState.getMaterial();
                    if (material != Material.GROUND && material != Material.CLAY && material != Material.GRASS && !EFFECTIVE_ON.contains(blockState.getBlock()))
                        continue;

                    BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, blockPos, blockState, player);
                    if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
                        continue;

                    sharedHarvest(stack, world, player, blockPos, blockState, silkTouch, fortuneLvl);
                }
            }
        }

        NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, SoulTicket.item(stack, world, player, (int) (charge * charge * charge / 2.7)));
        world.createExplosion(player, playerPos.getX(), playerPos.getY(), playerPos.getZ(), 0.5F, false);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 5, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -2.5, 0));
        }
        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition() {
        return new CustomMeshDefinitionActivatable("bound_shovel");
    }

    @Override
    public void gatherVariants(Consumer<String> variants) {
        variants.accept("active=true");
        variants.accept("active=false");
    }
}
