package wayoftime.bloodmagic.core.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;

public class ResourceUtil
{
	public static Set<Path> gatherResources(String home, String following, Predicate<Path> predicate)
	{
		FileSystem fileSystem = null;
		try
		{
			URL url = ResourceUtil.class.getResource(home);
			if (url != null)
			{
				URI uri = url.toURI();
				Path path;
				if (uri.getScheme().equals("file"))
				{
					path = Paths.get(ResourceUtil.class.getResource(home + "/" + following).toURI());
				} else
				{
					if (!uri.getScheme().equals("jar"))
					{
						BloodMagic.LOGGER.error("Unsupported URI scheme {}", uri.getScheme());
						return Collections.emptySet();
					}

					try
					{
						fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
					} catch (FileSystemAlreadyExistsException e)
					{
						fileSystem = FileSystems.getFileSystem(uri);
					}
					path = fileSystem.getPath(home + "/" + following);
				}

				return Files.walk(path).filter(predicate).collect(Collectors.toSet());
			}
		} catch (IOException | URISyntaxException e)
		{
			e.printStackTrace();
		} finally
		{
			IOUtils.closeQuietly(fileSystem);
		}

		return Collections.emptySet();
	}

	public static Set<Path> gatherResources(String home, String following)
	{
		return gatherResources(home, following, p -> true);
	}

	public static ResourceLocation addContext(ResourceLocation rl, String context)
	{
		return new ResourceLocation(rl.getNamespace(), context + rl.getPath());
	}
}
