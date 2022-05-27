package sanzol.app.config;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class Styles
{
	public static ImageIcon IMAGE_SEARCH = new FlatSVGIcon(Styles.class.getResource("/resources/search.svg"));
	public static ImageIcon IMAGE_EXECUTE = new FlatSVGIcon(Styles.class.getResource("/resources/execute.svg"));
	public static ImageIcon IMAGE_EXECUTE_LIGHT = new FlatSVGIcon(Styles.class.getResource("/resources/executeLight.svg"));
	public static ImageIcon IMAGE_MOON  = new FlatSVGIcon(Styles.class.getResource("/resources/moon.svg"));
	public static ImageIcon IMAGE_SUN  = new FlatSVGIcon(Styles.class.getResource("/resources/sun.svg"));
	public static ImageIcon IMAGE_COPY  = new FlatSVGIcon(Styles.class.getResource("/resources/copy.svg"));
	public static ImageIcon IMAGE_WRENCH  = new FlatSVGIcon(Styles.class.getResource("/resources/wrench.svg"));
	public static ImageIcon IMAGE_BUG  = new FlatSVGIcon(Styles.class.getResource("/resources/bug.svg"));

	public static Color COLOR_BORDER_LINE;
	public static Color COLOR_LINK;
	public static Color COLOR_TEXT_ALT1;
	public static Color COLOR_TEXT_ALT2;
	public static Color COLOR_TEXT_SHORT;
	public static Color COLOR_TEXT_LONG;
	public static Color COLOR_TEXT_ERROR;
	public static Color COLOR_TEXT_INFO;
	public static Color COLOR_TEXT_AREA_BG;
	public static Color COLOR_TEXT_AREA_FG;
	public static Color COLOR_BTN_SHORT;
	public static Color COLOR_BTN_LONG;

	public static MatteBorder BORDER_UP;
	public static MatteBorder BORDER_DOWN;
	
	public static void applyStyle() throws UnsupportedLookAndFeelException
	{
		if (Config.isDarkMode())
			setNight();
		else
			setLight();
	}

	public void repaintAll()
	{
		/*
		initComponents();			
		for(Window window: Window.getWindows()) {
		    SwingUtilities.updateComponentTreeUI(window);
		}
		*/
	}

	public static void setLight() throws UnsupportedLookAndFeelException
	{
		COLOR_BORDER_LINE = new Color(214, 214, 214);

		COLOR_LINK = new Color(65, 105, 225);
		COLOR_TEXT_ALT1 = new Color(65, 105, 225);
		COLOR_TEXT_ALT2 = new Color(39, 82, 194);

		COLOR_TEXT_SHORT = new Color(153, 0, 0);
		COLOR_TEXT_LONG = new Color(0, 102, 0);

		COLOR_TEXT_ERROR = new Color(255, 0, 0);
		COLOR_TEXT_INFO = new Color(50, 50, 50);

		COLOR_TEXT_AREA_BG = new Color(250, 250, 250);
		COLOR_TEXT_AREA_FG = new Color(0, 74, 127);

		COLOR_BTN_SHORT = new Color(255, 92, 92);
		COLOR_BTN_LONG = new Color(100, 200, 90);

		BORDER_UP = BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER_LINE);
		BORDER_DOWN = BorderFactory.createMatteBorder(1, 0, 1, 0, COLOR_BORDER_LINE);

		// -------------------------------------------------------------------
		UIManager.setLookAndFeel(new FlatLightLaf());
		// -------------------------------------------------------------------
	}

	public static void setNight() throws UnsupportedLookAndFeelException
	{
		COLOR_BORDER_LINE = new Color(46, 80, 112);

		COLOR_LINK = new Color(137, 255, 253);
		COLOR_TEXT_ALT1 = new Color(137, 255, 253);
		COLOR_TEXT_ALT2 = new Color(240, 240, 255);

		COLOR_TEXT_SHORT = new Color(255, 140, 60);
		COLOR_TEXT_LONG = new Color(137, 255, 143);

		COLOR_TEXT_ERROR = new Color(255, 0, 0);
		COLOR_TEXT_INFO = new Color(200, 200, 200);

		COLOR_TEXT_AREA_BG = new Color(20, 29, 45);
		COLOR_TEXT_AREA_FG = new Color(140, 207, 255);

		COLOR_BTN_SHORT = new Color(160, 0, 0);
		COLOR_BTN_LONG = new Color(30, 120, 0);

		BORDER_UP = BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER_LINE);
		BORDER_DOWN = BorderFactory.createMatteBorder(1, 0, 1, 0, COLOR_BORDER_LINE);

		// -------------------------------------------------------------------
		//UIManager.setLookAndFeel(new FlatCarbonIJTheme());
		IntelliJTheme.setup(Styles.class.getResourceAsStream("/resources/theme.json"));
		// -------------------------------------------------------------------
	}

}
