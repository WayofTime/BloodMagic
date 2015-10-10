package com.cricketcraft.chisel.api;

/**
 * Use the enum constants (using {@link #key} or calling {@link #toString()}) in this class as keys for IMC messages sent to chisel
 * <p>
 * It is also acceptable to copy the Strings in this class to avoid referencing this API.
 */
public enum IMC {

	/**
	 * Adds a variation to a group.
	 * 
	 * Use this to add a variation to a group. String syntax:
	 * <p>
	 * groupname|blockname|meta
	 * <p>
	 * An example would be {@code "mygroup|minecraft:dirt|1"} and this will add the vanilla dirt block with metadata 1 to the "mygroup" group, creating that group if need be.
	 */
	ADD_VARIATION("variation:add"),

	/**
	 * Removes a variation from a group.
	 * 
	 * Use this to remove a variation from a group. String syntax:
	 * <p>
	 * groupname|blockname|meta
	 * <p>
	 * An example would be {@code "mygroup|minecraft:dirt|1"} and this will add the vanilla dirt block with metadata 1 to the "mygroup" group, creating that group if need be.
	 */
	REMOVE_VARIATION("variation:remove"),

	/**
	 * Registers an oredict name to a group. This can be used to automatically add all blocks with this oredict name to a group. String syntax:
	 * <p>
	 * groupname|oredictname
	 * <p>
	 * An example would be {@code "mygroup|plankWood"} which will add all blocks registered in the oredict as "plankWood" to your group called "mygroup".
	 */
	REGISTER_GROUP_ORE("group:ore");

	/**
	 * The IMC message key for this message type.
	 */
	public final String key;

	IMC(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}

	/**
	 * The modid of Chisel so you can easily send IMC to this mod.
	 */
	@Deprecated
	public static final String CHISEL_MODID = "chisel";

	public static final String getModid() {
		return ChiselAPIProps.MOD_ID;
	}
}
