package WayofTime.bloodmagic.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.meteor.MeteorComponent;
import WayofTime.bloodmagic.meteor.MeteorHolder;
import WayofTime.bloodmagic.meteor.MeteorRegistry;
import WayofTime.bloodmagic.util.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ModMeteors
{
    public static String[] meteors = new String[] {};

    public static void init()
    {
        Gson gson = new GsonBuilder().create();

        List<String> properStrings = new ArrayList<String>();
        String currentString = "";
        int leftParenths = 0;
        int rightParenths = 0;

        for (String str : meteors)
        {
            currentString += str;
            for (char c : str.toCharArray())
            {
                if (c == '{')
                {
                    leftParenths++;
                } else if (c == '}')
                {
                    rightParenths++;
                }
            }

            if (leftParenths == rightParenths)
            {
                properStrings.add(currentString);
                currentString = "";
                leftParenths = 0;
                rightParenths = 0;
            }
        }

        for (String properString : properStrings)
        {
            MeteorHolder holder = gson.fromJson(properString, MeteorHolder.class);
            if (holder != null)
            {
                MeteorRegistry.registerMeteor(holder.getKeyStack(holder.metaKey), holder);
            }
        }
    }

    public static String[] getDefaultMeteors()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<MeteorHolder> holders = new ArrayList<MeteorHolder>();

        List<MeteorComponent> ironMeteorList = new ArrayList<MeteorComponent>();
        ironMeteorList.add(new MeteorComponent(400, "oreIron"));
        ironMeteorList.add(new MeteorComponent(200, "oreCopper"));
        ironMeteorList.add(new MeteorComponent(140, "oreTin"));
        ironMeteorList.add(new MeteorComponent(70, "oreSilver"));
        ironMeteorList.add(new MeteorComponent(80, "oreLead"));
        ironMeteorList.add(new MeteorComponent(30, "oreGold"));
        ironMeteorList.add(new MeteorComponent(60, "oreLapis"));
        ironMeteorList.add(new MeteorComponent(100, "oreRedstone"));
        MeteorHolder ironMeteorHolder = new MeteorHolder(Utils.getResourceForItem(new ItemStack(Blocks.IRON_BLOCK)), 0, ironMeteorList, 15, 5, 1000);

        holders.add(ironMeteorHolder);

        String[] meteors = new String[holders.size()];
        for (int i = 0; i < holders.size(); i++)
        {
            meteors[i] = gson.toJson(holders.get(i), MeteorHolder.class);
        }

        return meteors;
    }
}
