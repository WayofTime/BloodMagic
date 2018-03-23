package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.ritual.EnumRitualReaderState;
import WayofTime.bloodmagic.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.IDiscreteDemonWill;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemRitualReader extends Item implements IVariantProvider {
    public static final String tooltipBase = "tooltip.bloodmagic.ritualReader.";

    public ItemRitualReader() {
        super();
        setUnlocalizedName(BloodMagic.MODID + ".ritualReader");
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        if (!stack.hasTagCompound())
            return;

        EnumRitualReaderState state = this.getState(stack);
        tooltip.add(TextHelper.localizeEffect(tooltipBase + "currentState", TextHelper.localizeEffect(tooltipBase + state.toString().toLowerCase())));

        tooltip.add("");

        boolean sneaking = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        if (sneaking) {
            tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect(tooltipBase + "desc." + state.toString().toLowerCase()))));
        } else {
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.extraInfo"));
        }

        super.addInformation(stack, world, tooltip, tooltipFlag);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        RayTraceResult ray = this.rayTrace(world, player, false);
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }

        if (player.isSneaking()) {
            if (!world.isRemote) {
                cycleReader(stack, player);
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            EnumRitualReaderState state = this.getState(stack);
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof IMasterRitualStone) {
                IMasterRitualStone master = (IMasterRitualStone) tile;
                this.setMasterBlockPos(stack, pos);
                this.setBlockPos(stack, BlockPos.ORIGIN);

                switch (state) {
                    case INFORMATION:
                        master.provideInformationOfRitualToPlayer(player);
                        break;
                    case SET_AREA:
                        String range = this.getCurrentBlockRange(stack);
                        if (player.isSneaking()) {
                            String newRange = master.getNextBlockRange(range);
                            range = newRange;
                            this.setCurrentBlockRange(stack, newRange);
                        }

                        master.provideInformationOfRangeToPlayer(player, range);
                        break;
                    case SET_WILL_TYPES:
                        List<EnumDemonWillType> typeList = new ArrayList<>();
                        NonNullList<ItemStack> inv = player.inventory.mainInventory;
                        for (int i = 0; i < 9; i++) {
                            ItemStack testStack = inv.get(i);
                            if (testStack.isEmpty()) {
                                continue;
                            }

                            if (testStack.getItem() instanceof IDiscreteDemonWill) {
                                EnumDemonWillType type = ((IDiscreteDemonWill) testStack.getItem()).getType(testStack);
                                if (!typeList.contains(type)) {
                                    typeList.add(type);
                                }
                            }
                        }

                        master.setActiveWillConfig(player, typeList);
                        master.provideInformationOfWillConfigToPlayer(player, typeList);
                        break;
                }

                return EnumActionResult.FAIL;
            } else {
                if (state == EnumRitualReaderState.SET_AREA) {
                    BlockPos masterPos = this.getMasterBlockPos(stack);
                    if (!masterPos.equals(BlockPos.ORIGIN)) {
                        BlockPos containedPos = getBlockPos(stack);
                        if (containedPos.equals(BlockPos.ORIGIN)) {
                            this.setBlockPos(stack, pos.subtract(masterPos));
                            player.sendStatusMessage(new TextComponentTranslation("ritual.bloodmagic.blockRange.firstBlock"), true);
                            //TODO: Notify player.
                        } else {
                            tile = world.getTileEntity(masterPos);
                            if (tile instanceof IMasterRitualStone) {
                                IMasterRitualStone master = (IMasterRitualStone) tile;
                                master.setBlockRangeByBounds(player, this.getCurrentBlockRange(stack), containedPos, pos.subtract(masterPos));
                            }

                            this.setBlockPos(stack, BlockPos.ORIGIN);
                        }
                    }
                }
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    public BlockPos getBlockPos(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);
        return new BlockPos(stack.getTagCompound().getInteger(Constants.NBT.X_COORD), stack.getTagCompound().getInteger(Constants.NBT.Y_COORD), stack.getTagCompound().getInteger(Constants.NBT.Z_COORD));
    }

    public ItemStack setBlockPos(ItemStack stack, BlockPos pos) {
        stack = NBTHelper.checkNBT(stack);
        NBTTagCompound itemTag = stack.getTagCompound();
        itemTag.setInteger(Constants.NBT.X_COORD, pos.getX());
        itemTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
        itemTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
        return stack;
    }

    public BlockPos getMasterBlockPos(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);
        return new BlockPos(stack.getTagCompound().getInteger(Constants.NBT.X_COORD + "master"), stack.getTagCompound().getInteger(Constants.NBT.Y_COORD + "master"), stack.getTagCompound().getInteger(Constants.NBT.Z_COORD + "master"));
    }

    public ItemStack setMasterBlockPos(ItemStack stack, BlockPos pos) {
        stack = NBTHelper.checkNBT(stack);
        NBTTagCompound itemTag = stack.getTagCompound();
        itemTag.setInteger(Constants.NBT.X_COORD + "master", pos.getX());
        itemTag.setInteger(Constants.NBT.Y_COORD + "master", pos.getY());
        itemTag.setInteger(Constants.NBT.Z_COORD + "master", pos.getZ());
        return stack;
    }

    public String getCurrentBlockRange(ItemStack stack) {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        return tag.getString("range");
    }

    public void setCurrentBlockRange(ItemStack stack, String range) {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setString("range", range);
    }

    public void cycleReader(ItemStack stack, EntityPlayer player) {
        EnumRitualReaderState prevState = getState(stack);
        int val = prevState.ordinal();
        int nextVal = val + 1 >= EnumRitualReaderState.values().length ? 0 : val + 1;
        EnumRitualReaderState nextState = EnumRitualReaderState.values()[nextVal];

        setState(stack, nextState);
        notifyPlayerOfStateChange(nextState, player);
    }

    public void notifyPlayerOfStateChange(EnumRitualReaderState state, EntityPlayer player) {
        ChatUtil.sendNoSpam(player, new TextComponentTranslation(tooltipBase + "currentState", new TextComponentTranslation(tooltipBase + state.toString().toLowerCase())));
    }

    public void setState(ItemStack stack, EnumRitualReaderState state) {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setInteger(Constants.NBT.RITUAL_READER, state.ordinal());
    }

    public EnumRitualReaderState getState(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            return EnumRitualReaderState.INFORMATION;
        }

        NBTTagCompound tag = stack.getTagCompound();

        return EnumRitualReaderState.values()[tag.getInteger(Constants.NBT.RITUAL_READER)];
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=normal");
    }
}
