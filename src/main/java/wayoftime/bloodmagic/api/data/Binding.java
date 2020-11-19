package wayoftime.bloodmagic.api.data;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.INBTSerializable;

public class Binding implements INBTSerializable<CompoundNBT>
{
	private UUID uuid;
	private String name;

	public Binding(UUID uuid, String name)
	{
		this.uuid = uuid;
		this.name = name;
	}

	private Binding()
	{
		// No-op
	}

	@Override
	public CompoundNBT serializeNBT()
	{
		CompoundNBT tag = new CompoundNBT();
//		tag.put("id", NBTUtil.writeUniqueId(uuid));
		tag.put("id", NBTUtil.func_240626_a_(uuid));
		tag.putString("name", name);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt)
	{
		this.uuid = NBTUtil.readUniqueId(nbt.get("id"));
		this.name = nbt.getString("name");
	}

	public UUID getOwnerId()
	{
		return uuid;
	}

	public Binding setOwnerId(UUID uuid)
	{
		this.uuid = uuid;
		return this;
	}

	public String getOwnerName()
	{
		return name;
	}

	public Binding setOwnerName(String name)
	{
		this.name = name;
		return this;
	}

	@Nullable
	public static Binding fromStack(ItemStack stack)
	{
		if (!stack.hasTag()) // Definitely hasn't been bound yet.
			return null;

		INBT bindingTag = stack.getTag().get("binding");
		if (bindingTag == null || bindingTag.getId() != 10) // Make sure it's both a tag compound and that it has actual
															// data.
			return null;

		Binding binding = new Binding();
		binding.deserializeNBT((CompoundNBT) bindingTag);
		return binding;
	}

	@Override
	public String toString()
	{
		return "Binding{" + "uuid=" + uuid + ", name='" + name + '\'' + '}';
	}
}