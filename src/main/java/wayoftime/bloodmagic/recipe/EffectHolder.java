package wayoftime.bloodmagic.recipe;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class EffectHolder
{
	private final Effect potion;
	private int baseDuration;
	private int amplifier;
	private double ampDurationMod;
	private double lengthDurationMod;

	public EffectHolder(Effect potion, int baseDuration, int amplifier, double ampDurationMod, double lengthDurationMod)
	{
		this.potion = potion;
		this.baseDuration = baseDuration;
		this.amplifier = amplifier;
		this.ampDurationMod = ampDurationMod;
		this.lengthDurationMod = lengthDurationMod;
	}

	public EffectInstance getEffectInstance(boolean ambientIn, boolean showParticlesIn)
	{
		return getEffectInstance(1, ambientIn, showParticlesIn);
	}

	public EffectInstance getEffectInstance(double durationModifier, boolean ambientIn, boolean showParticlesIn)
	{
		return new EffectInstance(potion, (int) (baseDuration * ampDurationMod * lengthDurationMod * durationModifier), amplifier, ambientIn, showParticlesIn);
	}

	public Effect getPotion()
	{
		return potion;
	}

	public int getAmplifier()
	{
		return amplifier;
	}

	public int getBaseDuration()
	{
		return baseDuration;
	}

	public double getAmpDurationMod()
	{
		return ampDurationMod;
	}

	public double getLengthDurationMod()
	{
		return lengthDurationMod;
	}

	public void setBaseDuration(int baseDuration)
	{
		this.baseDuration = baseDuration;
	}

	public void setAmplifier(int amplifier)
	{
		this.amplifier = amplifier;
	}

	public void setAmpDurationMod(double ampDurationMod)
	{
		this.ampDurationMod = ampDurationMod;
	}

	public void setLengthDurationMod(double lengthDurationMod)
	{
		this.lengthDurationMod = lengthDurationMod;
	}

	public CompoundNBT write(CompoundNBT nbt)
	{
		nbt.putInt("Id", Effect.getId(this.getPotion()));
		this.writeInternal(nbt);
		return nbt;
	}

	private void writeInternal(CompoundNBT nbt)
	{
		nbt.putByte("Amplifier", (byte) getAmplifier());
		nbt.putInt("Duration", getBaseDuration());
		nbt.putDouble("AmpDurationMod", getAmpDurationMod());
		nbt.putDouble("LengthDurationMod", getLengthDurationMod());
	}

	public static EffectHolder read(CompoundNBT nbt)
	{
		int i = nbt.getInt("Id");
		Effect effect = Effect.get(i);
		return effect == null ? null : readInternal(effect, nbt);
	}

	private static EffectHolder readInternal(Effect effect, CompoundNBT nbt)
	{
		int amplifier = nbt.getByte("Amplifier");
		int baseDuration = nbt.getInt("Duration");
		double ampDurationMod = nbt.getDouble("AmpDurationMod");
		double lengthDurationMod = nbt.getDouble("LengthDurationMod");

		return new EffectHolder(effect, baseDuration, amplifier, ampDurationMod, lengthDurationMod);
	}

//	public final void write(PacketBuffer buffer)
//	{
//		buffer.writeInt(Effect.getId(getPotion()));
//		buffer.writeInt(getBaseDuration());
//		buffer.writeInt(getAmplifier());
//		buffer.writeDouble(getAmpDurationMod());
//		buffer.writeDouble(getLengthDurationMod());
//	}
}
