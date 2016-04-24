package WayofTime.bloodmagic.item.sigil;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.util.helper.NumeralHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.iface.IAltarReader;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.tile.TileIncenseAltar;
import WayofTime.bloodmagic.util.ChatUtil;

public class ItemSigilSeer extends ItemSigilBase implements IAltarReader
{
    public ItemSigilSeer()
    {
        super("seer");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote)
        {
            super.onItemRightClick(stack, world, player, hand);
            RayTraceResult rayTrace = rayTrace(world, player, false);

            if (rayTrace == null)
            {
                int currentEssence = NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).getCurrentEssence();

                List<ITextComponent> toSend = new ArrayList<ITextComponent>();
                if (!getOwnerName(stack).equals(PlayerHelper.getUsernameFromPlayer(player)))
                    toSend.add(new TextComponentTranslation(tooltipBase + "otherNetwork", getOwnerName(stack)));
                toSend.add(new TextComponentTranslation(tooltipBase + "currentEssence", currentEssence));
                ChatUtil.sendNoSpam(player, toSend.toArray(new ITextComponent[toSend.size()]));
            } else
            {
                if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
                {

                    TileEntity tile = world.getTileEntity(rayTrace.getBlockPos());

                    if (tile != null && tile instanceof IBloodAltar)
                    {
                        IBloodAltar altar = (IBloodAltar) tile;
                        int tier = altar.getTier().ordinal() + 1;
                        int currentEssence = altar.getCurrentBlood();
                        int capacity = altar.getCapacity();
                        int charge = altar.getTotalCharge();
                        altar.checkTier();
                        if (tile instanceof IInventory)
                        {
                            if (((IInventory) tile).getStackInSlot(0) != null)
                            {
                                int progress = altar.getProgress();
                                int totalLiquidRequired = altar.getLiquidRequired() * ((IInventory) tile).getStackInSlot(0).stackSize;
                                int consumptionRate = (int) (altar.getConsumptionRate() * (altar.getConsumptionMultiplier() + 1));
                                ChatUtil.sendNoSpam(player, new TextComponentTranslation(tooltipBase + "currentAltarProgress", progress, totalLiquidRequired), new TextComponentTranslation(tooltipBase + "currentAltarConsumptionRate", consumptionRate), new TextComponentTranslation(tooltipBase + "currentAltarTier", tier), new TextComponentTranslation(tooltipBase + "currentEssence", currentEssence), new TextComponentTranslation(tooltipBase + "currentAltarCapacity", capacity), new TextComponentTranslation(tooltipBase + "currentCharge", charge));
                            } else
                            {
                                ChatUtil.sendNoSpam(player, new TextComponentTranslation(tooltipBase + "currentAltarTier", NumeralHelper.toRoman(tier)), new TextComponentTranslation(tooltipBase + "currentEssence", currentEssence), new TextComponentTranslation(tooltipBase + "currentAltarCapacity", capacity), new TextComponentTranslation(tooltipBase + "currentCharge", charge));
                            }
                        }
                    } else if (tile != null && tile instanceof TileIncenseAltar)
                    {
                        TileIncenseAltar altar = (TileIncenseAltar) tile;
                        altar.recheckConstruction();
                        double tranquility = altar.tranquility;
                        ChatUtil.sendNoSpam(player, new TextComponentTranslation(tooltipBase + "currentTranquility", (int) ((100D * (int) (100 * tranquility)) / 100d)), new TextComponentTranslation(tooltipBase + "currentBonus", (int) (100 * altar.incenseAddition)));
                    } else
                    {
                        int currentEssence = NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).getCurrentEssence();
                        ChatUtil.sendNoSpam(player, new TextComponentTranslation(tooltipBase + "currentEssence", currentEssence));
                    }
                }
            }
        }

        return super.onItemRightClick(stack, world, player, hand);
    }
}
