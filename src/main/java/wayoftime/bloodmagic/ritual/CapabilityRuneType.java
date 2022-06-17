package wayoftime.bloodmagic.ritual;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.Constants;

public final class CapabilityRuneType implements ICapabilitySerializable<CompoundTag>, IRitualStoneTile
{
	// So... the Tile would need to implement this?
	public static final Capability<CapabilityRuneType> INSTANCE = CapabilityManager.get(new CapabilityToken<>()
	{
	});
	public static final ResourceLocation ID = new ResourceLocation(BloodMagic.MODID, Constants.NAMES.RUNE_CAPABILITY_NAME);

	private IRitualStoneTile instance;

	public CapabilityRuneType()
	{
		this.instance = new RuneTypeWrapper();
	}

	public CapabilityRuneType(IRitualStoneTile rune)
	{
		this.instance = rune;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		// TODO Auto-generated method stub
		return LazyOptional.of(() -> this).cast();
	}

	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag tag = new CompoundTag();
		tag.putByte("rune", (byte) instance.getRuneType().ordinal());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag)
	{
		instance.setRuneType(EnumRuneType.byMetadata(tag.getByte("rune")));
	}

	@Override
	public boolean isRuneType(EnumRuneType runeType)
	{
		return instance.isRuneType(runeType);
	}

	@Override
	public EnumRuneType getRuneType()
	{
		return instance.getRuneType();
	}

	@Override
	public void setRuneType(EnumRuneType runeType)
	{
		instance.setRuneType(runeType);
	}

	public static class RuneTypeWrapper implements IRitualStoneTile
	{
		private EnumRuneType type = EnumRuneType.BLANK;

		@Override
		public boolean isRuneType(EnumRuneType runeType)
		{
			return type == runeType;
		}

		@Override
		public EnumRuneType getRuneType()
		{
			return type;
		}

		public void setRuneType(EnumRuneType runeType)
		{
			type = runeType;
		}
	}
}