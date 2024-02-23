package mushroom.Libs.FontLib;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// MADE BY QUICK (monsoon dev)
// https://github.com/chaarlottte/legacy-pvp-client-tutorial-resources
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// https://www.youtube.com/watch?v=w0hHF2wMBB0 !


@SuppressWarnings("NonAtomicOperationOnVolatileField")
public class FontUtil {
    public static volatile int completed;
    public static MinecraftFontRenderer productsans19;
    public static MinecraftFontRenderer productsans35;
    public static MinecraftFontRenderer productsans40;
    public static MinecraftFontRenderer comicsans10;

    public static MinecraftFontRenderer comicsans19;
    public static MinecraftFontRenderer comicsans23;
    public static MinecraftFontRenderer comicsans35;
    public static MinecraftFontRenderer comicsans46;


    private static Font productsansfont19;
    private static Font productsansfont35;
    private static Font productsansfont40;
    private static Font comicsansfont10;

    private static Font comicsansfont19;
    private static Font comicsansfont23;
    private static Font comicsansfont35;
    private static Font comicsansfont46;


    private static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font = null;

        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("mushroom/font/" + location)).getInputStream();
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

    public static boolean hasLoaded() {
        return completed >= 3;
    }

    public static void bootstrap() {
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            productsansfont19 = getFont(locationMap, "productsans.ttf", 19);
            productsansfont35 = getFont(locationMap, "productsans.ttf", 35);
            productsansfont40 = getFont(locationMap, "productsans.ttf", 40);
            comicsansfont10 = getFont(locationMap, "comicsans.ttf", 15);
            comicsansfont19 = getFont(locationMap, "comicsans.ttf", 19);
            comicsansfont23 = getFont(locationMap, "comicsans.ttf", 23);
            comicsansfont35 = getFont(locationMap, "comicsans.ttf", 35);
            comicsansfont46 = getFont(locationMap, "comicsans.ttf", 46);

            completed++;
        }).start();
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            completed++;
        }).start();
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            completed++;
        }).start();

        while (!hasLoaded()) {
            try {
                //noinspection BusyWait
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        productsans19 = new MinecraftFontRenderer(productsansfont19, true, true);
        productsans35 = new MinecraftFontRenderer(productsansfont35, true, true);
        productsans40 = new MinecraftFontRenderer(productsansfont40, true, true);
        comicsans10 = new MinecraftFontRenderer(comicsansfont10, true, true);
        comicsans19 = new MinecraftFontRenderer(comicsansfont19, true, true);
        comicsans23 = new MinecraftFontRenderer(comicsansfont23, true, true);
        comicsans35 = new MinecraftFontRenderer(comicsansfont35, true, true);
        comicsans46 = new MinecraftFontRenderer(comicsansfont46, true, true);


    }
}