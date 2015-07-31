package WayofTime.alchemicalWizardry.common.thread;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class GAPIChecker
{
    public static boolean doneChecking = false;
    public static String onlineVersion = "@VERSION@";
    public static boolean triedToWarnPlayer = false;

    public static boolean startedDownload = false;
    public static boolean downloadedFile = false;

    private File modsDir;

    public void init()
    {
        FMLCommonHandler.instance().bus().register(this);
        File mcDir = (File)cpw.mods.fml.relauncher.FMLInjectionData.data()[6];

        this.modsDir = new File(mcDir, "mods");

        scanDepInfos();
    }

    private void scanDepInfos()
    {
        for (File file : modFiles())
        {
            if ((file.getName().endsWith(".jar")) && file.getName().contains("BloodMagic"))
            {
                scanDepInfo(file);
            }
        }
    }

    private void scanDepInfo(File file)
    {
        try
        {
            ZipFile zip = new ZipFile(file);
            ZipEntry e = zip.getEntry("Guide-API-Version.info");
            if (e == null)
            {
                e = zip.getEntry("Guide-API-Version.info");
            }
            if (e != null)
            {
                readConfigFile(zip.getInputStream(e));
            }
            zip.close();
        }
        catch (Exception e)
        {
            System.err.println("Failed to load dependencies.info from " + file.getName() + " as JSON");
            e.printStackTrace();
        }
    }

    private List<File> modFiles()
    {
        List<File> list = new LinkedList();
        list.addAll(Arrays.asList(this.modsDir.listFiles()));
        return list;
    }

    private void readConfigFile(InputStream inputStream) throws IOException
    {
        if (inputStream != null)
        {
            try
            {
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                onlineVersion = r.readLine();
                doneChecking = true;
                r.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (!Loader.isModLoaded("guideapi"))
        {
            if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null && !triedToWarnPlayer)
            {
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                IChatComponent component = IChatComponent.Serializer.func_150699_a(StatCollector.translateToLocal("bm.versioning.getGAPI"));
                player.addChatComponentMessage(component);
            }

            triedToWarnPlayer = true;
        }
    }
}
