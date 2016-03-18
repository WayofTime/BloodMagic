package WayofTime.bloodmagic.item.gear;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.IAltarManipulator;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ItemPackSacrifice extends ItemArmor implements IAltarManipulator, IVariantProvider
{
    public final int CAPACITY = 10000; // Max LP storage

    public ItemPackSacrifice()
    {
        super(ArmorMaterial.CHAIN, 0, EntityEquipmentSlot.CHEST);

        setUnlocalizedName(Constants.Mod.MODID + ".pack.sacrifice");
        setRegistryName(Constants.BloodMagicItem.SACRIFICE_PACK.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (world.isRemote)
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        RayTraceResult position = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (position == null)
        {
            return super.onItemRightClick(stack, world, player, EnumHand.MAIN_HAND);
        } else
        {
            if (position.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                TileEntity tile = world.getTileEntity(position.getBlockPos());

                if (!(tile instanceof TileAltar))
                    return super.onItemRightClick(stack, world, player, EnumHand.MAIN_HAND);

                TileAltar altar = (TileAltar) tile;

                if (!altar.isActive())
                {
                    int amount = this.getStoredLP(stack);

                    if (amount > 0)
                    {
                        int filledAmount = altar.fillMainTank(amount);
                        amount -= filledAmount;
                        setStoredLP(stack, amount);
                        world.notifyBlockUpdate(position.getBlockPos(), world.getBlockState(position.getBlockPos()), world.getBlockState(position.getBlockPos()), 3);
                    }
                }
            }
        }

        return ActionResult.newResult(EnumActionResult.FAIL, stack);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
    {
        if (getStoredLP(stack) > CAPACITY)
            setStoredLP(stack, CAPACITY);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced)
    {
        stack = NBTHelper.checkNBT(stack);
        list.add(TextHelper.localize("tooltip.BloodMagic.pack.sacrifice.desc"));
        list.add(TextHelper.localize("tooltip.BloodMagic.pack.stored", getStoredLP(stack)));
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=normal"));
        return ret;
    }

    public void addLP(ItemStack stack, int toAdd)
    {
        stack = NBTHelper.checkNBT(stack);

        if (toAdd < 0)
            toAdd = 0;

        if (toAdd > CAPACITY)
            toAdd = CAPACITY;

        setStoredLP(stack, Math.min(getStoredLP(stack) + toAdd, CAPACITY));
    }

    public void setStoredLP(ItemStack stack, int lp)
    {
        stack = NBTHelper.checkNBT(stack);
        stack.getTagCompound().setInteger(Constants.NBT.STORED_LP, lp);
    }

    public int getStoredLP(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);
        return stack.getTagCompound().getInteger(Constants.NBT.STORED_LP);
    }
}
