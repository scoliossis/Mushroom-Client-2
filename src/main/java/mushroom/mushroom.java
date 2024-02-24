package mushroom;

import mushroom.Features.Combat.*;
import mushroom.Features.Macros.*;
import mushroom.Features.Movement.*;
import mushroom.Features.Visual.*;
import mushroom.GUI.ClickGUI;
import mushroom.GUI.Config;
import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.FontLib.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opencl.CL;
import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.Mod.EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static mushroom.Libs.PlayerLib.mc;

@Mod(modid = mushroom.MODID, version = mushroom.VERSION)

public class mushroom extends CommandBase {

    // todo:
    // port to java:
    // trigger bot
    // tpaura (maybe)
    // chest esp
    // skull esp (maybe)
    // derp
    // trail, uhhh full recode of this one ig
    // kapow ^^^^ it looked ugly before
    // inv walk, find bypass for hypixel too!!
    // freeze... wtf is this for
    // ghost blocks, I LOVE G KEY
    // high jump <- boringo
    // spider - boring
    // strafe - ill add as speed mode
    // target strafe, uhhh maybe ill readd after noslow
    // step - fuck this, (idk ill find a bypass)
    // sneak - not finding a bypass for this sadly.
    // chestaura - idk why this isnt added yet
    // skullaura / leveraura
    // w-tap idk if this will bypass
    // machinegun - might not readd
    // bowaimbot - ehhhh maybe later
    // autoclicker - later ig


    // other
    // add freecam
    // add delaybeforesteal on chest stealer
    // custom scoreboard
    // discord rpc (idrk how to do this)
    // trail
    // trajectory's
    // cool font chat
    // auto pot / soup
    // autoplay
    // play statistics or smth
    // fix fonts to be easier to use
    // add client font options
    // store target hud pos, and other stuff


    public static final String MODID = "mushroom";
    public static final String VERSION = "4.0";

    public static String rightclicked = "";

    public static ArrayList settings = Config.collect(Configs.class);

    public static String mainuser = mc.getSession().getProfile().getName();
    public static String mainuuid = mc.getSession().getProfile().getId().toString();
    public static String mainssid = mc.getSession().getToken();

    public static String validsessions = "";
    public static String loadedconfig = "current";

    public static String sillyfolderpath = "config/mushroomclient";


    public static String Changelog = "\u00A7bVersion \u00A7c3.0 \u00A7b-> \u00A7a4.0 \u00A7bchangelog:\n" +
            "#PORTED TO JAVA!!!!!!!!!!!!!!\n" +
            "#Fixed Cool Main Menu\n" +
            "#Fixed Click GUI\n" +
            "#Fixed Array List\n" +
            "#Fixed Scaffold + Killaura Rotations\n" +
            "#Fixed No Fall\n" +
            "#Fixed Backtrack\n" +
            "#Fixed Reach\n" +
            "#Fixed Fastplace\n" +
            "#Fixed Fastbreak\n" +
            "#Fixed Nuker\n" +
            "#Fixed No Hitboxes\n" +
            "#Renamed Nuker to Fucker\n" +
            "\n" +
            "+Added Blink antivoid\n" +
            "+Added Blink\n" +
            "+Added Omnisprint\n" +
            "+Added Animated Main Menu\n" +
            "+Added Outline + 2D ESP\n" +
            "+Added Chams\n" +
            "+Added Nametags + Health Bar\n" +
            "+Added Animations\n" +
            "+Added sims crystal\n" +
            "+Added Animations\n" +
            "+Added Model Modifier\n" +
            "+Added Sprint Jump Scaffold\n" +
            "+Added Capes\n" +
            "\n" +
            "-Removed Not Enough Spanish\n" +
            "-Removed Chat Macros Tab\n" +
            "-Removed No Walls\n" +
            "-Removed CPS Multiplier\n" +
            "-Removed Sumo Fences\n" +
            "-Removed Strafe Module\n" +
            "-Removed Skyblock Features";

    @EventHandler
    public void preInit(FMLPreInitializationEvent var1) throws IOException {
        if (!new File(sillyfolderpath).exists()) {
            new File(sillyfolderpath).mkdirs();
        }
        if (!new File(sillyfolderpath + "/configs").exists()) {
            new File(sillyfolderpath + "/configs").mkdirs();
        }
        if (!new File(sillyfolderpath + "/accounts").exists()) {
            new File(sillyfolderpath + "/accounts").mkdirs();
        }

        Display.setTitle("mushroom client 4.0");

        ClientCommandHandler.instance.registerCommand(new mushroom());
        ClientCommandHandler.instance.registerCommand(new Config());
        MinecraftForge.EVENT_BUS.register(this);
        FontUtil.bootstrap();
        MinecraftForge.EVENT_BUS.register(new CoolMainMenu());
        MinecraftForge.EVENT_BUS.register(new ArrayListRender());
        MinecraftForge.EVENT_BUS.register(new Eagle());
        MinecraftForge.EVENT_BUS.register(new FastPlace());
        MinecraftForge.EVENT_BUS.register(new Hilarity());
        MinecraftForge.EVENT_BUS.register(new Velocity());
        MinecraftForge.EVENT_BUS.register(new BlockHit());
        MinecraftForge.EVENT_BUS.register(new Blink());
        MinecraftForge.EVENT_BUS.register(new AntiVoid());
        MinecraftForge.EVENT_BUS.register(new Fly());
        MinecraftForge.EVENT_BUS.register(new Nofall());
        MinecraftForge.EVENT_BUS.register(new Timer());
        MinecraftForge.EVENT_BUS.register(new ChestStealer());
        MinecraftForge.EVENT_BUS.register(new InventoryManager());
        MinecraftForge.EVENT_BUS.register(new Sprint());
        MinecraftForge.EVENT_BUS.register(new Killaura());
        MinecraftForge.EVENT_BUS.register(new Scaffold());
        MinecraftForge.EVENT_BUS.register(new Notifications());
        MinecraftForge.EVENT_BUS.register(new ESP());
        MinecraftForge.EVENT_BUS.register(new NameTags());
        MinecraftForge.EVENT_BUS.register(new TargetHud());
        MinecraftForge.EVENT_BUS.register(new FullBright());
        MinecraftForge.EVENT_BUS.register(new BackTrack());
        MinecraftForge.EVENT_BUS.register(new NoSlow());
        MinecraftForge.EVENT_BUS.register(new Speed());
        MinecraftForge.EVENT_BUS.register(new Sims());
        MinecraftForge.EVENT_BUS.register(new AntiBot());
        MinecraftForge.EVENT_BUS.register(new Fucker());
        MinecraftForge.EVENT_BUS.register(new AutoTool());
        MinecraftForge.EVENT_BUS.register(new BedESP());

        if (Files.exists(Paths.get(sillyfolderpath + "/accounts/sessionids.cfg"))) {
            validsessions = ReadFile(sillyfolderpath + "/accounts/sessionids.cfg");
        }
        Config.load("current.cfg");
        Config.getkeybinds();
    }

    public static String ReadFile(String filepath) {
        try {
            File file = new File(filepath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            while (reader.ready()) {
                content.append(reader.readLine());
            }
            reader.close();
            return content.toString();
        } catch (Exception ignored) {
        }
        return "";
    }

    public static GuiScreen guiToOpen = null;

    public static int arraycolor = 0;
    public static Boolean godown = false;


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent tick) {
        if (guiToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(guiToOpen);
            guiToOpen = null;
        }

    }

    public static float animationframe = 1;

    public static boolean welcome = true;

    public String getCommandName() {
        return "mushroom";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
        // stores previous gui scale then changes scale to 2
        ClickGUI.startGuiScale = mc.gameSettings.guiScale;

        // the text in the gui looks hecka ugly on any other gui scale :(
        mc.gameSettings.guiScale = 2;

        mushroom.guiToOpen = new ClickGUI();
    }

    public ArrayList getCommandAliases() {
        ArrayList<String> var1 = new ArrayList<String>();
        var1.add("mc");
        var1.add("MC");
        var1.add("Mc");
        var1.add("mC");
        var1.add("mushroomclient");
        return var1;
    }

    @Override
    public String getCommandUsage(ICommandSender arg0) {
        ChatLib.chat("haiiii ;3");
        return "";
    }

    public static EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
