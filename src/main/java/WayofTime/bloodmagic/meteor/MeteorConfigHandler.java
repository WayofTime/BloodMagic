package WayofTime.bloodmagic.meteor;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.gson.Serializers;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeteorConfigHandler
{
    private static final Map<String, Meteor> DEFAULT_METEORS = new HashMap<String, Meteor>();

    private static File meteorDir;

    public static void init(File meteorDirectory)
    {
        meteorDir = meteorDirectory;
        handleMeteors(true);
    }

    public static void handleMeteors(boolean checkNewVersion)
    {
        // Clear the meteors so that reloading in-game can be done
        MeteorRegistry.meteorMap.clear();
        List<Pair<String, Meteor>> defaultMeteors = getDefaultMeteors();

        try
        {
            // Create defaults if the folder doesn't exist
            if (!meteorDir.exists() && meteorDir.mkdir())
            {
                for (Pair<String, Meteor> meteor : defaultMeteors)
                {
                    String json = Serializers.GSON.toJson(meteor.getRight());
                    FileWriter writer = new FileWriter(new File(meteorDir, meteor.getLeft() + ".json"));
                    writer.write(json);
                    writer.close();
                }
            }

            // Collect all meteors from the files
            File[] meteorFiles = meteorDir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
            if (meteorFiles == null)
                return;

            List<Pair<String, Meteor>> meteors = new ArrayList<Pair<String, Meteor>>();

            // Filter names so we can compare to defaults
            for (File meteorFile : meteorFiles)
            {
                Meteor meteor = Serializers.GSON.fromJson(new FileReader(meteorFile), Meteor.class);
                meteors.add(Pair.of(FilenameUtils.removeExtension(meteorFile.getName()), meteor));
            }

            if (checkNewVersion && ConfigHandler.getConfig().getBoolean("resyncOnVersionChange", "Meteors", true, "Should the default meteors be regenerated if the mod has updated them"))
            {
                // Check defaults for new version
                for (Pair<String, Meteor> meteor : meteors)
                {
                    Meteor defaultMeteor = DEFAULT_METEORS.get(meteor.getLeft());
                    if (defaultMeteor != null && defaultMeteor.version > meteor.getRight().version)
                    {
                        String json = Serializers.GSON.toJson(defaultMeteor);
                        File meteorFile = new File(meteorDir, meteor.getLeft() + ".json");
                        new PrintWriter(meteorFile).close(); // Clear the file
                        FileWriter fileWriter = new FileWriter(meteorFile);
                        fileWriter.write(json); // Write the new contents
                        fileWriter.close();

                        meteors.set(meteors.indexOf(meteor), Pair.of(meteor.getLeft(), defaultMeteor));
                    }
                }
            }

            // Finally, register all of our meteors
            for (Pair<String, Meteor> meteor : meteors)
                MeteorRegistry.registerMeteor(meteor.getRight().getCatalystStack(), meteor.getRight());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        ConfigHandler.getConfig().save();
    }

    private static List<Pair<String, Meteor>> getDefaultMeteors()
    {
        List<Pair<String, Meteor>> holders = new ArrayList<Pair<String, Meteor>>();

        // Iron
        List<MeteorComponent> ironMeteorList = new ArrayList<MeteorComponent>();
        ironMeteorList.add(new MeteorComponent(400, "oreIron"));
        ironMeteorList.add(new MeteorComponent(200, "oreCopper"));
        ironMeteorList.add(new MeteorComponent(140, "oreTin"));
        ironMeteorList.add(new MeteorComponent(70, "oreSilver"));
        ironMeteorList.add(new MeteorComponent(80, "oreLead"));
        ironMeteorList.add(new MeteorComponent(30, "oreGold"));
        ironMeteorList.add(new MeteorComponent(60, "oreLapis"));
        ironMeteorList.add(new MeteorComponent(100, "oreRedstone"));
        Meteor ironMeteor = new Meteor(new ItemStack(Blocks.IRON_BLOCK), ironMeteorList, 15, 5, 1000);
        ironMeteor.setVersion(2);
        // Gold
        List<MeteorComponent> goldMeteorList = new ArrayList<MeteorComponent>();
        goldMeteorList.add(new MeteorComponent(200, "oreIron"));
        goldMeteorList.add(new MeteorComponent(100, "oreCopper"));
        goldMeteorList.add(new MeteorComponent(60, "oreTin"));
        goldMeteorList.add(new MeteorComponent(100, "oreGold"));
        goldMeteorList.add(new MeteorComponent(120, "oreLapis"));
        goldMeteorList.add(new MeteorComponent(30, "oreDiamond"));
        goldMeteorList.add(new MeteorComponent(20, "oreEmerald"));
        goldMeteorList.add(new MeteorComponent(20, "oreCoal"));

        Meteor goldMeteor = new Meteor(new ItemStack(Blocks.GOLD_BLOCK), goldMeteorList, 18, 6, 1000);
        goldMeteor.setVersion(3);

        List<MeteorComponent> diamondMeteorList = new ArrayList<MeteorComponent>();
        diamondMeteorList.add(new MeteorComponent(50, "oreIron"));
        diamondMeteorList.add(new MeteorComponent(100, "oreGold"));
        diamondMeteorList.add(new MeteorComponent(10, "oreLapis"));
        diamondMeteorList.add(new MeteorComponent(250, "oreDiamond"));
        diamondMeteorList.add(new MeteorComponent(180, "oreEmerald"));
        diamondMeteorList.add(new MeteorComponent(50, "oreRedstone"));
        diamondMeteorList.add(new MeteorComponent(400, "minecraft:diamond_block"));

        Meteor diamondMeteor = new Meteor(new ItemStack(Blocks.DIAMOND_BLOCK), diamondMeteorList, 10, 3, 1000);
        goldMeteor.setVersion(3);

        holders.add(Pair.of("IronMeteor", ironMeteor));
        DEFAULT_METEORS.put("IronMeteor", ironMeteor);

        holders.add(Pair.of("GoldMeteor", goldMeteor));
        DEFAULT_METEORS.put("GoldMeteor", goldMeteor);

        holders.add(Pair.of("DiamondMeteor", diamondMeteor));
        DEFAULT_METEORS.put("DiamondMeteor", diamondMeteor);
        return holders;
    }
}
