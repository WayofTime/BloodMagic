package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.IFuelHandler;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.client.IVariantProvider;

import com.google.common.base.Strings;

public class ItemLavaCrystal extends ItemBindable implements IFuelHandler, IVariantProvider
{
    public ItemLavaCrystal()
    {
        super();
        setUnlocalizedName(Constants.Mod.MODID + ".lavaCrystal");
        setRegistryName(Constants.BloodMagicItem.LAVA_CRYSTAL.getRegName());
        setLPUsed(25);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        syphonNetwork(itemStack, getLPUsed());
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public int getBurnTime(ItemStack fuel)
    {
        if (fuel == null)
        {
            return 0;
        }

        Item fuelItem = fuel.getItem();

        if (fuelItem instanceof ItemLavaCrystal)
        {
//
//            if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
//            {
//                return 200;
//            }

            if (canSyphonFromNetwork(fuel, getLPUsed()))
            {
                return 200;
            } else
            {
                if (!Strings.isNullOrEmpty(getBindableOwner(fuel)))
                {
                    EntityPlayer player = PlayerHelper.getPlayerFromUUID(getBindableOwner(fuel));
                    if (player != null)
                    {
                        player.addPotionEffect(new PotionEffect(MobEffects.confusion, 99));
                    }
                }

                return 0;
            }
        }

        return 0;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=normal"));
        return ret;
    }
}
