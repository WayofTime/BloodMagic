package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.registry.ModPotions;

public class RitualGreenGrove extends Ritual
{
    public static final String GROW_RANGE = "growing";
    public static final String LEECH_RANGE = "leech";

    public static double corrosiveWillDrain = 0.2;

    public RitualGreenGrove()
    {
        super("ritualGreenGrove", 0, 5000, "ritual." + Constants.Mod.MODID + ".greenGroveRitual");
        addBlockRange(GROW_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 2, -1), 3, 1, 3));
        addBlockRange(LEECH_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 1));
        setMaximumVolumeAndDistanceOfRange(GROW_RANGE, 81, 4, 4);
        setMaximumVolumeAndDistanceOfRange(LEECH_RANGE, 0, 15, 15);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        BlockPos pos = masterRitualStone.getBlockPos();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNausea();
            return;
        }

        int maxGrowths = currentEssence / getRefreshCost();
        int totalGrowths = 0;

        List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

        double corrosiveWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.CORROSIVE, willConfig);

        AreaDescriptor growingRange = getBlockRange(GROW_RANGE);

        for (BlockPos newPos : growingRange.getContainedPositions(masterRitualStone.getBlockPos()))
        {
            IBlockState state = world.getBlockState(newPos);
            Block block = state.getBlock();

            if (!BloodMagicAPI.getGreenGroveBlacklist().contains(block))
            {
                if (block instanceof IPlantable || block instanceof IGrowable)
                {
                    block.updateTick(world, newPos, state, new Random());
                    totalGrowths++;
                }
            }

            if (totalGrowths >= maxGrowths)
            {
                break;
            }
        }

        double corrosiveDrain = 0;
        if (corrosiveWill > corrosiveWillDrain)
        {
            AreaDescriptor leechRange = getBlockRange(LEECH_RANGE);
            AxisAlignedBB mobArea = leechRange.getAABB(pos);
            List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, mobArea);
            for (EntityLivingBase entityLiving : entityList)
            {
                if (corrosiveWill < corrosiveWillDrain)
                {
                    break;
                }

                if (entityLiving instanceof EntityPlayer)
                {
                    continue;
                }

                if (entityLiving.isPotionActive(ModPotions.plantLeech) || !entityLiving.isPotionApplicable(new PotionEffect(ModPotions.plantLeech)))
                {
                    continue;
                }

                entityLiving.addPotionEffect(new PotionEffect(ModPotions.plantLeech, 200, 0));

                corrosiveWill -= corrosiveWillDrain;
                corrosiveDrain += corrosiveWillDrain;
            }

            if (corrosiveWillDrain > 0)
            {
                WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.CORROSIVE, corrosiveDrain, true);
            }
        }

        network.syphon(totalGrowths * getRefreshCost());
    }

    @Override
    public int getRefreshTime()
    {
        return 20;
    }

    @Override
    public int getRefreshCost()
    {
        return 5; //TODO: Need to find a way to balance this
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 1, 0, EnumRuneType.EARTH);
        this.addParallelRunes(components, 1, 0, EnumRuneType.WATER);

        return components;
    }

    @Override
    public ITextComponent[] provideInformationOfRitualToPlayer(EntityPlayer player)
    {
        return new ITextComponent[] { new TextComponentTranslation(this.getUnlocalizedName() + ".info"), new TextComponentTranslation(this.getUnlocalizedName() + ".corrosive.info") };
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualGreenGrove();
    }
}
