package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.BlockRitualStone;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.ritual.RitualComponent;
import WayofTime.bloodmagic.tile.TileMasterRitualStone;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.RitualHelper;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class ItemRitualDismantler extends Item implements IVariantProvider {
    public ItemRitualDismantler() {
        setTranslationKey(BloodMagic.MODID + ".ritualDismantler");
        setCreativeTab(BloodMagic.TAB_BM);
        setMaxStackSize(1);
    }

    @Override
    public ActionResultType onItemUse(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        Block block = world.getBlockState(pos).getBlock();
        TileEntity tileEntity = world.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(hand);

        if (tileEntity instanceof TileMasterRitualStone) {
            TileMasterRitualStone masterRitualStone = (TileMasterRitualStone) tileEntity;
            Direction direction = masterRitualStone.getDirection();

            String ritualName = RitualHelper.getValidRitual(world, pos);
            masterRitualStone.setActive(false);

            if (ritualName.equals("")) {
                world.setBlockToAir(pos);
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(RegistrarBloodMagicBlocks.RITUAL_CONTROLLER));
                NetworkHelper.getSoulNetwork(player).syphon(SoulTicket.item(stack, 100));
                return ActionResultType.SUCCESS;
            }

            Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(ritualName);
            List<RitualComponent> components = Lists.newArrayList();
            ritual.gatherComponents(components::add);
            for (RitualComponent component : components) {
                BlockPos newPos = pos.add(component.getOffset(direction));
                if (world.getBlockState(newPos).getBlock() instanceof BlockRitualStone) {
                    world.setBlockToAir(newPos);
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(RegistrarBloodMagicBlocks.RITUAL_STONE));
                }
            }

            NetworkHelper.getSoulNetwork(player).syphon(SoulTicket.item(stack, 200)); // smallest Ritual has 4 stones
            return ActionResultType.SUCCESS;

        } else if (player.isSneaking() && block instanceof BlockRitualStone) {
            block.removedByPlayer(world.getBlockState(pos), world, pos, player, false);
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(RegistrarBloodMagicBlocks.RITUAL_STONE));
            NetworkHelper.getSoulNetwork(player).syphon(SoulTicket.item(stack, 50));
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;
    }
}