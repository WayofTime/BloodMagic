package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.iface.IAltarReader;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import WayofTime.bloodmagic.util.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemSigilDivination extends ItemSigilBase implements IAltarReader {
    public ItemSigilDivination() {
        super("divination");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
//        if (world instanceof WorldServer)
//        {
//            System.out.println("Testing...");
////            BuildTestStructure s = new BuildTestStructure();
////            s.placeStructureAtPosition(new Random(), Rotation.CLOCKWISE_180, (WorldServer) world, player.getPosition(), 0);
//            DungeonTester.testDungeonElementWithOutput((WorldServer) world, player.getPosition());
//        }

//        if (!world.isRemote)
//        {
//            EntityCorruptedSheep fred = new EntityCorruptedSheep(world, EnumDemonWillType.DESTRUCTIVE);
//            fred.setPosition(player.posX, player.posY, player.posZ);
//            world.spawnEntityInWorld(fred);
//        }
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);

        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        if (!world.isRemote) {
            super.onItemRightClick(world, player, hand);

            RayTraceResult position = rayTrace(world, player, false);

            if (position == null) {
                int currentEssence = NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).getCurrentEssence();
                List<ITextComponent> toSend = new ArrayList<ITextComponent>();
                if (!getOwnerName(stack).equals(PlayerHelper.getUsernameFromPlayer(player)))
                    toSend.add(new TextComponentTranslation(tooltipBase + "otherNetwork", getOwnerName(stack)));
                toSend.add(new TextComponentTranslation(tooltipBase + "currentEssence", currentEssence));
                ChatUtil.sendNoSpam(player, toSend.toArray(new ITextComponent[toSend.size()]));
            }
        }

        return super.onItemRightClick(world, player, hand);
    }
}
