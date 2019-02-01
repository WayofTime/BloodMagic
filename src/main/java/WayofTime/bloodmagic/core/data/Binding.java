package WayofTime.bloodmagic.core.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.UUID;

public class Binding implements INBTSerializable<NBTTagCompound> {

    private UUID uuid;
    private String name;

    public Binding(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    private Binding() {
        // No-op
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("id", NBTUtil.createUUIDTag(uuid));
        tag.setString("name", name);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.uuid = NBTUtil.getUUIDFromTag(nbt.getCompoundTag("id"));
        this.name = nbt.getString("name");
    }

    public UUID getOwnerId() {
        return uuid;
    }

    public Binding setOwnerId(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getOwnerName() {
        return name;
    }

    public Binding setOwnerName(String name) {
        this.name = name;
        return this;
    }

    @Nullable
    public static Binding fromStack(ItemStack stack) {
        if (!stack.hasTagCompound()) // Definitely hasn't been bound yet.
            return null;

        NBTBase bindingTag = stack.getTagCompound().getTag("binding");
        if (bindingTag == null || bindingTag.getId() != 10 || bindingTag.isEmpty()) // Make sure it's both a tag compound and that it has actual data.
            return null;

        Binding binding = new Binding();
        binding.deserializeNBT((NBTTagCompound) bindingTag);
        return binding;
    }

    @Override
    public String toString() {
        return "Binding{" + "uuid=" + uuid + ", name='" + name + '\'' + '}';
    }
}
