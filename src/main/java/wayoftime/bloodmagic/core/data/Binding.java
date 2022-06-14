package wayoftime.bloodmagic.core.data;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraftforge.common.util.INBTSerializable;

public class Binding implements INBTSerializable<CompoundTag>
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
	public CompoundTag serializeNBT()
	{
		CompoundTag tag = new CompoundTag();
//		tag.put("id", NBTUtil.writeUniqueId(uuid));
		tag.put("id", NbtUtils.createUUID(uuid));
		tag.putString("name", name);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.uuid = NbtUtils.loadUUID(nbt.get("id"));
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

		Tag bindingTag = stack.getTag().get("binding");
		if (bindingTag == null || bindingTag.getId() != 10) // Make sure it's both a tag compound and that it has actual
															// data.
			return null;

		Binding binding = new Binding();
		binding.deserializeNBT((CompoundTag) bindingTag);
		return binding;
	}

	@Override
	public String toString()
	{
		return "Binding{" + "uuid=" + uuid + ", name='" + name + '\'' + '}';
	}
}