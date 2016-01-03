package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class RitualRegeneration extends Ritual
{
    public static final String HEAL_RANGE = "heal";

    public static final int SACRIFICE_AMOUNT = 100;

    public RitualRegeneration()
    {
        super("ritualRegeneration", 0, 25000, "ritual." + Constants.Mod.MODID + ".regenerationRitual");
        addBlockRange(HEAL_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-15, -15, -15), 31));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        BlockPos pos = masterRitualStone.getBlockPos();

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        AreaDescriptor damageRange = getBlockRange(HEAL_RANGE);
        AxisAlignedBB range = damageRange.getAABB(pos);

        List<EntityPlayer> entities = world.getEntitiesWithinAABB(EntityPlayer.class, range);

        for (EntityLivingBase player : entities)
        {
            float health = player.getHealth();
            if (health <= player.getMaxHealth() - 1)
            {
                player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 50, 0, false, false));

                totalEffects++;

                if (totalEffects >= maxEffects)
                {
                    break;
                }
            }
        }

        network.syphon(getRefreshCost() * totalEffects);
    }

    @Override
    public int getRefreshTime()
    {
        return 50;
    }

    @Override
    public int getRefreshCost()
    {
        return 200;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        components.add(new RitualComponent(new BlockPos(4, 0, 0), EnumRuneType.AIR));
        components.add(new RitualComponent(new BlockPos(5, 0, -1), EnumRuneType.AIR));
        components.add(new RitualComponent(new BlockPos(5, 0, 1), EnumRuneType.AIR));
        components.add(new RitualComponent(new BlockPos(-4, 0, 0), EnumRuneType.AIR));
        components.add(new RitualComponent(new BlockPos(-5, 0, -1), EnumRuneType.AIR));
        components.add(new RitualComponent(new BlockPos(-5, 0, 1), EnumRuneType.AIR));
        components.add(new RitualComponent(new BlockPos(0, 0, 4), EnumRuneType.FIRE));
        components.add(new RitualComponent(new BlockPos(1, 0, 5), EnumRuneType.FIRE));
        components.add(new RitualComponent(new BlockPos(-1, 0, 5), EnumRuneType.FIRE));
        components.add(new RitualComponent(new BlockPos(0, 0, -4), EnumRuneType.FIRE));
        components.add(new RitualComponent(new BlockPos(1, 0, -5), EnumRuneType.FIRE));
        components.add(new RitualComponent(new BlockPos(-1, 0, -5), EnumRuneType.FIRE));
        this.addOffsetRunes(components, 3, 5, 0, EnumRuneType.WATER);
        this.addCornerRunes(components, 3, 0, EnumRuneType.DUSK);
        this.addOffsetRunes(components, 4, 5, 0, EnumRuneType.EARTH);
        this.addOffsetRunes(components, 4, 5, -1, EnumRuneType.EARTH);
        this.addCornerRunes(components, 5, 0, EnumRuneType.EARTH);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualRegeneration();
    }
}
