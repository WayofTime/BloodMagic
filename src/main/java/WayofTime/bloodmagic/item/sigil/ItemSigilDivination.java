package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.iface.IAltarReader;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

public class ItemSigilDivination extends ItemSigilBase implements IAltarReader {

    public ItemSigilDivination(boolean simple) {
        super(simple ? "divination" : "seer");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        if (!world.isRemote) {
            super.onItemRightClick(world, player, hand);

            Binding binding = getBinding(stack);
            if (binding != null) {
                int currentEssence = NetworkHelper.getSoulNetwork(binding).getCurrentEssence();
                List<ITextComponent> toSend = Lists.newArrayList();
                if (!binding.getOwnerId().equals(player.getGameProfile().getId()))
                    toSend.add(new TextComponentTranslation(tooltipBase + "otherNetwork", binding.getOwnerName()));
                toSend.add(new TextComponentTranslation(tooltipBase + "currentEssence", currentEssence));
                ChatUtil.sendNoSpam(player, toSend.toArray(new ITextComponent[toSend.size()]));
            }
        }

        return super.onItemRightClick(world, player, hand);
    }
}
