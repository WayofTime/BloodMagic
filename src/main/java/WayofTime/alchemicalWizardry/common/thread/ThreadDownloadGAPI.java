package WayofTime.alchemicalWizardry.common.thread;

import net.minecraft.client.Minecraft;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ThreadDownloadGAPI extends Thread
{
    String fileName;

    byte[] buffer = new byte[10240];

    int totalBytesDownloaded;
    int bytesJustDownloaded;

    InputStream webReader;

    public ThreadDownloadGAPI(String fileName)
    {
        setName("Blood Magic Download Guide-API Thread");
        this.fileName = fileName;
        setDaemon(true);
        start();
    }

    @Override
    public void run()
    {
        try
        {
            IChatComponent component = IChatComponent.Serializer.func_150699_a(String.format(StatCollector.translateToLocal("bm.versioning.startingDownload"), fileName));

            if (Minecraft.getMinecraft().thePlayer != null)
            {
                Minecraft.getMinecraft().thePlayer.addChatMessage(component);
            }

            GAPIChecker.startedDownload = true;

            String base = "http://minecraft.curseforge.com/mc-mods/228832-guide-api";
            URL url = new URL(base + "/files/latest");

            try
            {
                url.openStream().close(); // Add to DL Counter
            }
            catch(IOException e) {}

            webReader = url.openStream();

            File dir = new File(".", "mods");
            File f = new File(dir, "Guide-API-" + MinecraftForge.MC_VERSION + "-" + GAPIChecker.onlineVersion + ".jar");
            f.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(f.getAbsolutePath());

            while ((bytesJustDownloaded = webReader.read(buffer)) > 0)
            {
                outputStream.write(buffer, 0, bytesJustDownloaded);
                buffer = new byte[10240];
                totalBytesDownloaded += bytesJustDownloaded;
            }

            if (Minecraft.getMinecraft().thePlayer != null)
            {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("bm.versioning.doneDownloading", fileName).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
            }

            Desktop.getDesktop().open(dir);
            GAPIChecker.downloadedFile = true;

            outputStream.close();
            webReader.close();
            finalize();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    private void sendError()
    {
        if (Minecraft.getMinecraft().thePlayer != null)
        {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentTranslation("bm.versioning.error").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
    }
}
