package pneumaticCraft.api.client.pneumaticHelmet;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
Use this interface to specify any hackable entity. When it's your entity, you can simply implement this interface in the 
entity's class. If you don't have access to the class (vanilla entities), you can implement this interface in a separate class
and register it using PneumaticRegistry.registry.addHackable(entityClass, IHackableEntityClass). In both ways there will be an IHackableEntity instance for every entity.
*/
public interface IHackableEntity{

    /**
     * Should return a unique id to represent this hackable. Used in NBT saving to be able to trigger the afterHackTime after a server restart.
     * Null is a valid return: afterHackTick will not be triggered at all in that case.
     * @return
     */
    public String getId();

    /**
    Returning true will allow the player to hack this entity. This can be used to only allow hacking on certain conditions.
    */
    public boolean canHack(Entity entity, EntityPlayer player);

    /**
        Add info that is displayed on the tracker tooltip here. Text like "Hack to explode" can be added.
        This method is only called when canHack(Entity) returned true.
        The added lines automatically will be tried to get localized.
    */
    public void addInfo(Entity entity, List<String> curInfo, EntityPlayer player);

    /**
     * Add info that is being displayed after hacking, as long as 'afterHackTick' is returning true.
     * Things like "Neutralized".
     * The added lines automatically will be tried to get localized.
     * @param entity
     * @param curInfo
     * @param player
     */
    public void addPostHackInfo(Entity entity, List<String> curInfo, EntityPlayer player);

    /**
        Return the time it takes to hack this entity in ticks. For more powerful hacks, a longer required hacking time is adviced.
    */
    public int getHackTime(Entity entity, EntityPlayer player);

    /**
        When the player hacked the entity for getHackTime(Entity) ticks this will be called on both client and server side.
    */
    public void onHackFinished(Entity entity, EntityPlayer player);

    /**
     * Called every tick after the hacking finished. Returning true will keep this going (for mob spawners, to keep them neutralized),
     * or false to stop ticking (for door/lever hacking).
     */
    public boolean afterHackTick(Entity entity);
}
