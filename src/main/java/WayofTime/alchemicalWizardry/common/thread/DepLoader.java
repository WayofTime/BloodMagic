package WayofTime.alchemicalWizardry.common.thread;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.versioning.ComparableVersion;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.awt.*;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import net.minecraft.launchwrapper.LaunchClassLoader;
import sun.misc.URLClassPath;
import sun.net.util.URLUtil;

public class DepLoader implements IFMLLoadingPlugin, IFMLCallHook
{
    private static ByteBuffer downloadBuffer = ByteBuffer.allocateDirect(8388608);
    private static DepLoadInst inst;

    public interface IDownloadDisplay
    {
        void resetProgress(int paramInt);

        void setPokeThread(Thread paramThread);

        void updateProgress(int paramInt);

        boolean shouldStopIt();

        void updateProgressString(String paramString, Object... paramVarArgs);

        Object makeDialog();

        void showErrorDialog(String paramString1, String paramString2);
    }

    public static class Downloader extends JOptionPane implements DepLoader.IDownloadDisplay
    {
        private JDialog container;
        private JLabel currentActivity;
        private JProgressBar progress;
        boolean stopIt;
        Thread pokeThread;

        public JDialog makeDialog()
        {
            return this.container;
        }

        public void updateProgressString(String progressUpdate, Object... data)
        {
            if (this.currentActivity != null) {
                this.currentActivity.setText(String.format(progressUpdate, data));
            }
        }

        public void resetProgress(int sizeGuess)
        {
            if (this.progress != null) {
                this.progress.getModel().setRangeProperties(0, 0, 0, sizeGuess, false);
            }
        }

        public void updateProgress(int fullLength)
        {
            if (this.progress != null) {
                this.progress.getModel().setValue(fullLength);
            }
        }

        public void setPokeThread(Thread currentThread)
        {
            this.pokeThread = currentThread;
        }

        public boolean shouldStopIt()
        {
            return this.stopIt;
        }

        public void showErrorDialog(String name, String url)
        {
            JEditorPane ep = new JEditorPane("text/html", "<html>CB's DepLoader was unable to download required library " + name + "<br>Check your internet connection and try restarting or download it manually from" + "<br><a href=\"" + url + "\">" + url + "</a> and put it in your mods folder" + "</html>");

            ep.setEditable(false);
            ep.setOpaque(false);
            ep.addHyperlinkListener(new HyperlinkListener()
            {
                public void hyperlinkUpdate(HyperlinkEvent event)
                {
                    try
                    {
                        if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                            Desktop.getDesktop().browse(event.getURL().toURI());
                        }
                    }
                    catch (Exception e) {}
                }
            });
            JOptionPane.showMessageDialog(null, ep, "A download error has occured", 0);
        }
    }

    public static class DummyDownloader implements DepLoader.IDownloadDisplay
    {
        public void resetProgress(int sizeGuess) {}

        public void setPokeThread(Thread currentThread) {}

        public void updateProgress(int fullLength) {}

        public boolean shouldStopIt()
        {
            return false;
        }

        public void updateProgressString(String string, Object... data) {}

        public Object makeDialog()
        {
            return null;
        }

        public void showErrorDialog(String name, String url) {}
    }

    public static class VersionedFile
    {
        public final Pattern pattern;
        public final String filename;
        public final ComparableVersion version;
        public final String name;

        public VersionedFile(String filename, Pattern pattern)
        {
            this.pattern = pattern;
            this.filename = filename;
            Matcher m = pattern.matcher(filename);
            if (m.matches())
            {
                this.name = m.group(1);
                this.version = new ComparableVersion(m.group(2));
            }
            else
            {
                this.name = null;
                this.version = null;
            }
        }

        public boolean matches()
        {
            return this.name != null;
        }
    }

    public static class Dependency
    {
        public String url;
        public DepLoader.VersionedFile file;
        public String existing;
        public boolean coreLib;

        public Dependency(String url, DepLoader.VersionedFile file, boolean coreLib)
        {
            this.url = url;
            this.file = file;
            this.coreLib = coreLib;
        }
    }

    public static class DepLoadInst
    {
        private File modsDir;
        private File v_modsDir;
        private DepLoader.IDownloadDisplay downloadMonitor;
        private JDialog popupWindow;
        private Map<String, DepLoader.Dependency> depMap = new HashMap();
        private HashSet<String> depSet = new HashSet();

        public DepLoadInst()
        {
            String mcVer = (String)cpw.mods.fml.relauncher.FMLInjectionData.data()[4];
            File mcDir = (File)cpw.mods.fml.relauncher.FMLInjectionData.data()[6];

            this.modsDir = new File(mcDir, "mods");
            this.v_modsDir = new File(mcDir, "mods/" + mcVer);
            if (!this.v_modsDir.exists()) {
                this.v_modsDir.mkdirs();
            }
        }

        private void addClasspath(String name)
        {
            try
            {
                ((LaunchClassLoader)DepLoader.class.getClassLoader()).addURL(new File(this.v_modsDir, name).toURI().toURL());
            }
            catch (MalformedURLException e)
            {
                throw new RuntimeException(e);
            }
        }

        private void deleteMod(File mod)
        {
            if (mod.delete()) {
                return;
            }
            try
            {
                ClassLoader cl = DepLoader.class.getClassLoader();
                URL url = mod.toURI().toURL();
                Field f_ucp = URLClassLoader.class.getDeclaredField("ucp");
                Field f_loaders = URLClassPath.class.getDeclaredField("loaders");
                Field f_lmap = URLClassPath.class.getDeclaredField("lmap");
                f_ucp.setAccessible(true);
                f_loaders.setAccessible(true);
                f_lmap.setAccessible(true);

                URLClassPath ucp = (URLClassPath)f_ucp.get(cl);
                Closeable loader = (Closeable)((Map)f_lmap.get(ucp)).remove(URLUtil.urlNoFragString(url));
                if (loader != null)
                {
                    loader.close();
                    ((List)f_loaders.get(ucp)).remove(loader);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            if (!mod.delete())
            {
                mod.deleteOnExit();
                String msg = "CB's DepLoader was unable to delete file " + mod.getPath() + " the game will now try to delete it on exit. If this dialog appears again, delete it manually.";
                System.err.println(msg);
                if (!GraphicsEnvironment.isHeadless()) {
                    JOptionPane.showMessageDialog(null, msg, "An update error has occured", 0);
                }
                System.exit(1);
            }
        }

        private void download(DepLoader.Dependency dep)
        {
            this.popupWindow = ((JDialog)this.downloadMonitor.makeDialog());
            File libFile = new File(this.v_modsDir, dep.file.filename);
            try
            {
                URL libDownload = new URL(dep.url + '/' + dep.file.filename);
                this.downloadMonitor.updateProgressString("Downloading file %s", libDownload.toString());
                System.out.format("Downloading file %s\n", libDownload.toString());
                URLConnection connection = libDownload.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("User-Agent", "CB's DepLoader Downloader");
                int sizeGuess = connection.getContentLength();
                download(connection.getInputStream(), sizeGuess, libFile);
                this.downloadMonitor.updateProgressString("Download complete", 0);
                System.out.println("Download complete");

                scanDepInfo(libFile);
            }
            catch (Exception e)
            {
                libFile.delete();
                if (this.downloadMonitor.shouldStopIt())
                {
                    System.err.println("You have stopped the downloading operation before it could complete");
                    System.exit(1);
                    return;
                }
                this.downloadMonitor.showErrorDialog(dep.file.filename, dep.url + '/' + dep.file.filename);
                throw new RuntimeException("A download error occured", e);
            }
        }

        private void download(InputStream is, int sizeGuess, File target) throws Exception
        {
            if (sizeGuess > DepLoader.downloadBuffer.capacity()) {
                throw new Exception(String.format("The file %s is too large to be downloaded by CB's DepLoader - the download is invalid", new Object[] { target.getName() }));
            }
            DepLoader.downloadBuffer.clear();

            int fullLength = 0;

            this.downloadMonitor.resetProgress(sizeGuess);
            try
            {
                this.downloadMonitor.setPokeThread(Thread.currentThread());
                byte[] smallBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(smallBuffer)) >= 0)
                {
                    DepLoader.downloadBuffer.put(smallBuffer, 0, bytesRead);
                    fullLength += bytesRead;
                    if (this.downloadMonitor.shouldStopIt()) {
                        break;
                    }
                    this.downloadMonitor.updateProgress(fullLength);
                }
                is.close();
                this.downloadMonitor.setPokeThread(null);
                DepLoader.downloadBuffer.limit(fullLength);
                DepLoader.downloadBuffer.position(0);
            }
            catch (InterruptedIOException e)
            {
                Thread.interrupted();
                throw new Exception("Stop");
            }
            catch (IOException e)
            {
                throw e;
            }
            try
            {
                if (!target.exists()) {
                    target.createNewFile();
                }
                DepLoader.downloadBuffer.position(0);
                FileOutputStream fos = new FileOutputStream(target);
                fos.getChannel().write(DepLoader.downloadBuffer);
                fos.close();
            }
            catch (Exception e)
            {
                throw e;
            }
        }

        private String checkExisting(DepLoader.Dependency dep)
        {
            for (File f : this.modsDir.listFiles())
            {
                DepLoader.VersionedFile vfile = new DepLoader.VersionedFile(f.getName(), dep.file.pattern);
                if ((vfile.matches()) && (vfile.name.equals(dep.file.name))) {
                    if (!f.renameTo(new File(this.v_modsDir, f.getName()))) {
                        deleteMod(f);
                    }
                }
            }
            for (File f : this.v_modsDir.listFiles())
            {
                DepLoader.VersionedFile vfile = new DepLoader.VersionedFile(f.getName(), dep.file.pattern);
                if ((vfile.matches()) && (vfile.name.equals(dep.file.name)))
                {
                    int cmp = vfile.version.compareTo(dep.file.version);
                    if (cmp < 0)
                    {
                        System.out.println("Deleted old version " + f.getName());
                        deleteMod(f);
                        return null;
                    }
                    if (cmp > 0)
                    {
                        System.err.println("Warning: version of " + dep.file.name + ", " + vfile.version + " is newer than request " + dep.file.version);
                        return f.getName();
                    }
                    return f.getName();
                }
            }
            return null;
        }

        public void load()
        {
            scanDepInfos();
            if (this.depMap.isEmpty()) {
                return;
            }
            loadDeps();
            activateDeps();
        }

        private void activateDeps()
        {
            for (DepLoader.Dependency dep : this.depMap.values()) {
                if (dep.coreLib) {
                    addClasspath(dep.existing);
                }
            }
        }

        private void loadDeps()
        {
            this.downloadMonitor = (FMLLaunchHandler.side().isClient() ? new DepLoader.Downloader() : new DepLoader.DummyDownloader());
            try
            {
                while (!this.depSet.isEmpty())
                {
                    Iterator<String> it = this.depSet.iterator();
                    DepLoader.Dependency dep = this.depMap.get(it.next());
                    it.remove();
                    load(dep);
                }
            }
            finally
            {
                if (this.popupWindow != null)
                {
                    this.popupWindow.setVisible(false);
                    this.popupWindow.dispose();
                }
            }
        }

        private void load(DepLoader.Dependency dep)
        {
            dep.existing = checkExisting(dep);
            if ((dep.existing == null) && (dep.file.name.equalsIgnoreCase("guide-api")))
            {
                download(dep);
                dep.existing = dep.file.filename;
            }
        }

        private List<File> modFiles()
        {
            List<File> list = new LinkedList();
            list.addAll(Arrays.asList(this.modsDir.listFiles()));
            list.addAll(Arrays.asList(this.v_modsDir.listFiles()));
            return list;
        }

        private void scanDepInfos()
        {
            for (File file : modFiles()) {
                if ((file.getName().endsWith(".jar")) || (file.getName().endsWith(".zip"))) {
                    scanDepInfo(file);
                }
            }
        }

        private void scanDepInfo(File file)
        {
            try
            {
                ZipFile zip = new ZipFile(file);
                ZipEntry e = zip.getEntry("dependancies.info");
                if (e == null) {
                    e = zip.getEntry("dependencies.info");
                }
                if (e != null) {
                    loadJSon(zip.getInputStream(e));
                }
                zip.close();
            }
            catch (Exception e)
            {
                System.err.println("Failed to load dependencies.info from " + file.getName() + " as JSON");
                e.printStackTrace();
            }
        }

        private void loadJSon(InputStream input) throws IOException
        {
            InputStreamReader reader = new InputStreamReader(input);
            JsonElement root = new JsonParser().parse(reader);
            if (root.isJsonArray()) {
                loadJSonArr(root);
            } else {
                loadJson(root.getAsJsonObject());
            }
            reader.close();
        }

        private void loadJSonArr(JsonElement root) throws IOException
        {
            for (JsonElement node : root.getAsJsonArray()) {
                loadJson(node.getAsJsonObject());
            }
        }

        private void loadJson(JsonObject node) throws IOException
        {
            boolean obfuscated = ((LaunchClassLoader)DepLoader.class.getClassLoader()).getClassBytes("net.minecraft.world.World") == null;



            String testClass = node.get("class").getAsString();
            if (DepLoader.class.getResource("/" + testClass.replace('.', '/') + ".class") != null) {
                return;
            }
            String repo = node.get("repo").getAsString();
            String filename = node.get("file").getAsString();
            if ((!obfuscated) && (node.has("dev"))) {
                filename = node.get("dev").getAsString();
            }
            boolean coreLib = (node.has("coreLib")) && (node.get("coreLib").getAsBoolean());


            Pattern pattern = null;
            try
            {
                if (node.has("pattern")) {
                    pattern = Pattern.compile(node.get("pattern").getAsString());
                }
            }
            catch (PatternSyntaxException e)
            {
                System.err.println("Invalid filename pattern: " + node.get("pattern"));
                e.printStackTrace();
            }
            if (pattern == null) {
                pattern = Pattern.compile("(\\w+).*?([\\d\\.]+)[-\\w]*\\.[^\\d]+");
            }
            DepLoader.VersionedFile file = new DepLoader.VersionedFile(filename, pattern);
            if (!file.matches()) {
                throw new RuntimeException("Invalid filename format for dependency: " + filename);
            }
            addDep(new DepLoader.Dependency(repo, file, coreLib));
        }

        private void addDep(DepLoader.Dependency newDep)
        {
            if (mergeNew(this.depMap.get(newDep.file.name), newDep))
            {
                this.depMap.put(newDep.file.name, newDep);
                this.depSet.add(newDep.file.name);
            }
        }

        private boolean mergeNew(DepLoader.Dependency oldDep, DepLoader.Dependency newDep)
        {
            if (oldDep == null) {
                return true;
            }
            DepLoader.Dependency newest = newDep.file.version.compareTo(oldDep.file.version) > 0 ? newDep : oldDep;
            newest.coreLib = ((newDep.coreLib) || (oldDep.coreLib));

            return newest == newDep;
        }
    }

    public static void load()
    {
        if (inst == null)
        {
            inst = new DepLoadInst();
            inst.load();
        }
    }

    public String[] getASMTransformerClass()
    {
        return null;
    }

    public String getModContainerClass()
    {
        return null;
    }

    public String getSetupClass()
    {
        return getClass().getName();
    }

    public void injectData(Map<String, Object> data) {}

    public Void call()
    {
        load();

        return null;
    }

    public String getAccessTransformerClass()
    {
        return null;
    }
}
