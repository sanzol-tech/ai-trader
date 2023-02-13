package aitrader.ui.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import aitrader.util.Properties;

public class UIConfig{	private static String skinMode = "light";
	public static String getSkinMode()
	{
		return skinMode;
	}

	public static void setSkinMode(String skinMode)
	{
		UIConfig.skinMode = skinMode;
	}

	public static boolean load() throws IOException
	{
		Path path = Paths.get(UIConstants.DEFAULT_USER_FOLDER, UIConstants.UICONFIG_FILENAME);
		if (!path.toFile().exists())
		{
			UILog.error("File " + UIConstants.UICONFIG_FILENAME + " does not exist");
			return false;
		}

		Properties props = Properties.create(false).load(path);
		skinMode = props.getValue("skinMode");

		return true;
	}

	public static void save() throws IOException
	{
		Path path = Paths.get(UIConstants.DEFAULT_USER_FOLDER, UIConstants.UICONFIG_FILENAME);
		Properties.create(false)
			.put("skinMode", skinMode)
			.save(path);
	}

}
