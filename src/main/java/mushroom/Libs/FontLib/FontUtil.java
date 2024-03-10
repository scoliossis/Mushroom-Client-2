package mushroom.Libs.FontLib;

import mushroom.GUI.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// MADE BY QUICK (monsoon dev)
// https://github.com/chaarlottte/legacy-pvp-client-tutorial-resources
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// https://www.youtube.com/watch?v=w0hHF2wMBB0 !


public class FontUtil {
    public static volatile int completed;

    private static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font;

        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("mushroom/font/" + location)).getInputStream();
                font = Font.createFont(0, is);
                locationMap.put(location, font);
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, +10);
        }

        return font;
    }

    static String[] fontNames = {"productsans", "comicsans", "tahoma"};
    static ArrayList<MinecraftFontRenderer> fonts = new ArrayList();
    static int maxFontSize = 48;

    public static MinecraftFontRenderer font(String fontName, int fontSize) {
        // for now ill set it here.
        fontName = fontNames[Configs.fontOfClient];
        int fontnum = 0;
        while (fontnum < fontNames.length && !Objects.equals(fontName, fontNames[fontnum])) {
            fontnum++;
        }
        return fonts.get((fontnum*maxFontSize) + fontSize);
    }

    public static void bootstrap() {
        Map<String, Font> locationMap = new HashMap<>();
        for (int i = 0; i < fontNames.length;i++) {
            for (int size = 0; size <= maxFontSize; size++) {
                fonts.add(new MinecraftFontRenderer(getFont(locationMap, fontNames[i] + ".ttf", size), true, true));
            }
        }
    }
}