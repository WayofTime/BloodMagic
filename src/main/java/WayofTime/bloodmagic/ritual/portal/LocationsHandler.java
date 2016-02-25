package WayofTime.bloodmagic.ritual.portal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraftforge.common.DimensionManager;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.teleport.PortalLocation;

public class LocationsHandler implements Serializable
{

    public static final long serialVersionUID = 10102001;
    private static final String fileName = String.valueOf(DimensionManager.getCurrentSaveRootDirectory()) + "/" + Constants.Mod.MODID + "/PortalLocations.dat";
    private static HashMap<String, ArrayList<PortalLocation>> portals;
    private static LocationsHandler locationsHandler;

    private LocationsHandler()
    {
        portals = new HashMap<String, ArrayList<PortalLocation>>();
    }

    public static LocationsHandler getLocationsHandler()
    {
        if (locationsHandler == null || loadFile() == null)
        {
            locationsHandler = new LocationsHandler();
            return locationsHandler;
        } else
        {
            portals = loadFile();
            return locationsHandler;
        }
    }

    private static HashMap<String, ArrayList<PortalLocation>> loadFile()
    {
        HashMap<String, ArrayList<PortalLocation>> map;
        File file = new File(fileName);
        try
        {
            if (!file.exists())
            {
                if (file.getParentFile().mkdir())
                {
                    if (file.createNewFile())
                    {
                        BloodMagicAPI.getLogger().info("Creating " + fileName + " in " + String.valueOf(DimensionManager.getCurrentSaveRootDirectory()));
                    }
                } else if (file.createNewFile())
                {
                    BloodMagicAPI.getLogger().info("Creating " + fileName + " in " + String.valueOf(DimensionManager.getCurrentSaveRootDirectory()));
                }
            }
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            map = (HashMap<String, ArrayList<PortalLocation>>) in.readObject();
            in.close();
            fileIn.close();
            return map;
        } catch (IOException e)
        {
            return null;
        } catch (ClassNotFoundException e)
        {
            BloodMagicAPI.getLogger().error(String.valueOf(file) + " was not found in " + String.valueOf(DimensionManager.getCurrentSaveRootDirectory()));
            return null;
        }
    }

    private static void updateFile(String file, HashMap<String, ArrayList<PortalLocation>> object)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean addLocation(String name, PortalLocation location)
    {
        ArrayList<PortalLocation> portalLocations = portals.get(name);
        if (portalLocations == null)
        {
            portals.put(name, new ArrayList<PortalLocation>());
            updateFile(fileName, portals);
        }
        if (!portals.get(name).isEmpty() && portals.get(name).size() >= 2)
        {
            BloodMagicAPI.getLogger().info("Location " + name + " already exists.");
            updateFile(fileName, portals);
            return false;
        } else
        {
            portals.get(name).add(location);
            BloodMagicAPI.getLogger().info("Adding " + name);
            updateFile(fileName, portals);
            return true;
        }
    }

    public boolean removeLocation(String name, PortalLocation location)
    {
        if (portals.get(name) != null && !portals.get(name).isEmpty())
        {
            if (portals.get(name).contains(location))
            {
                portals.get(name).remove(location);
                BloodMagicAPI.getLogger().info("Removing " + name);
                updateFile(fileName, portals);
                return true;
            } else
            {
                BloodMagicAPI.getLogger().info("No location matching " + name);
                updateFile(fileName, portals);
                return false;
            }
        }
        return false;
    }

    public ArrayList<PortalLocation> getLinkedLocations(String name)
    {
        return portals.get(name);
    }
}