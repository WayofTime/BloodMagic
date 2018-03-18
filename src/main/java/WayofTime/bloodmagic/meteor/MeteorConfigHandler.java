package WayofTime.bloodmagic.meteor;

import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.gson.Serializers;
import WayofTime.bloodmagic.util.BMLog;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MeteorConfigHandler {
    private static final Map<String, Meteor> DEFAULT_METEORS = Maps.newHashMap();

    private static File meteorDir;

    public static void init(File meteorDirectory) {
        meteorDir = meteorDirectory;
        handleMeteors(true);
    }

    public static void handleMeteors(boolean checkNewVersion) {
        if (meteorDir == null) {
            BMLog.DEFAULT.error("Attempted to handle meteor config but the folder has not been initialized. Was this run too early?");
            return;
        }

        // Clear the meteors so that reloading in-game can be done
        MeteorRegistry.meteorMap.clear();
        List<Pair<String, Meteor>> defaultMeteors = getDefaultMeteors();

        try {
            // Create defaults if the folder doesn't exist
            if (!meteorDir.exists() && meteorDir.mkdir()) {
                for (Pair<String, Meteor> meteor : defaultMeteors) {
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

            List<Pair<String, Meteor>> meteors = Lists.newArrayList();

            // Filter names so we can compare to defaults
            for (File meteorFile : meteorFiles) {
                FileReader reader = new FileReader(meteorFile);
                Meteor meteor = Serializers.GSON.fromJson(reader, Meteor.class);
                meteors.add(Pair.of(FilenameUtils.removeExtension(meteorFile.getName()), meteor));
                reader.close();
            }

            if (checkNewVersion && ConfigHandler.values.shouldResyncMeteors) {
                Set<String> discoveredDefaults = Sets.newHashSet();

                // Check existing defaults for new version
                for (Pair<String, Meteor> meteor : meteors) {
                    Meteor defaultMeteor = DEFAULT_METEORS.get(meteor.getLeft());
                    if (defaultMeteor != null) {
                        discoveredDefaults.add(meteor.getLeft());
                        if (defaultMeteor.version > meteor.getRight().version) {
                            writeMeteor(meteor.getLeft(), defaultMeteor);
                            meteors.set(meteors.indexOf(meteor), Pair.of(meteor.getLeft(), defaultMeteor));
                        }
                    }
                }

                // Generate new defaults
                for (Map.Entry<String, Meteor> entry : DEFAULT_METEORS.entrySet()) {
                    if (discoveredDefaults.contains(entry.getKey()))
                        continue;

                    writeMeteor(entry.getKey(), entry.getValue());
                }
            }

            // Finally, register all of our meteors
            for (Pair<String, Meteor> meteor : meteors)
                MeteorRegistry.registerMeteor(meteor.getRight().getCatalystStack(), meteor.getRight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Pair<String, Meteor>> getDefaultMeteors() {
        List<Pair<String, Meteor>> holders = Lists.newArrayList();

        // Iron
        List<MeteorComponent> ironMeteorList = Lists.newArrayList();
        ironMeteorList.add(new MeteorComponent(400, "oreIron"));
        ironMeteorList.add(new MeteorComponent(200, "oreCopper"));
        ironMeteorList.add(new MeteorComponent(140, "oreTin"));
        ironMeteorList.add(new MeteorComponent(70, "oreSilver"));
        ironMeteorList.add(new MeteorComponent(80, "oreLead"));
        ironMeteorList.add(new MeteorComponent(30, "oreGold"));
        ironMeteorList.add(new MeteorComponent(60, "oreLapis"));
        ironMeteorList.add(new MeteorComponent(100, "oreRedstone"));
        Meteor ironMeteor = new Meteor(new ItemStack(Blocks.IRON_BLOCK), ironMeteorList, 15, 5);
        ironMeteor.setVersion(2);

        // Gold
        List<MeteorComponent> goldMeteorList = Lists.newArrayList();
        goldMeteorList.add(new MeteorComponent(200, "oreIron"));
        goldMeteorList.add(new MeteorComponent(100, "oreCopper"));
        goldMeteorList.add(new MeteorComponent(60, "oreTin"));
        goldMeteorList.add(new MeteorComponent(100, "oreGold"));
        goldMeteorList.add(new MeteorComponent(120, "oreLapis"));
        goldMeteorList.add(new MeteorComponent(30, "oreDiamond"));
        goldMeteorList.add(new MeteorComponent(20, "oreEmerald"));
        goldMeteorList.add(new MeteorComponent(20, "oreCoal"));
        Meteor goldMeteor = new Meteor(new ItemStack(Blocks.GOLD_BLOCK), goldMeteorList, 18, 6);
        goldMeteor.setVersion(3);

        // Diamond
        List<MeteorComponent> diamondMeteorList = Lists.newArrayList();
        diamondMeteorList.add(new MeteorComponent(50, "oreIron"));
        diamondMeteorList.add(new MeteorComponent(100, "oreGold"));
        diamondMeteorList.add(new MeteorComponent(10, "oreLapis"));
        diamondMeteorList.add(new MeteorComponent(250, "oreDiamond"));
        diamondMeteorList.add(new MeteorComponent(180, "oreEmerald"));
        diamondMeteorList.add(new MeteorComponent(50, "oreRedstone"));
        diamondMeteorList.add(new MeteorComponent(400, "oreDiamond"));
        Meteor diamondMeteor = new Meteor(new ItemStack(Blocks.DIAMOND_BLOCK), diamondMeteorList, 10, 3);
        diamondMeteor.setVersion(3);

        holders.add(Pair.of("iron", ironMeteor));
        DEFAULT_METEORS.put("iron", ironMeteor);

        holders.add(Pair.of("gold", goldMeteor));
        DEFAULT_METEORS.put("gold", goldMeteor);

        holders.add(Pair.of("diamond", diamondMeteor));
        DEFAULT_METEORS.put("diamond", diamondMeteor);
        return holders;
    }

    private static void writeMeteor(String name, Meteor meteor) {
        try {
            String json = Serializers.GSON.toJson(meteor);
            File meteorFile = new File(meteorDir, name + ".json");
            new PrintWriter(meteorFile).close(); // Clear the file
            FileWriter fileWriter = new FileWriter(meteorFile);
            fileWriter.write(json); // Write the new contents
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
