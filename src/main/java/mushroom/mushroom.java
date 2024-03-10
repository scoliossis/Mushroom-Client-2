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
    // port to java from old version:
    // trigger bot
    // tpaura (maybe)
    // chest esp
    // skull esp (maybe)
    // derp
    // kapow it looked ugly before
    // inv walk, find bypass for hypixel too!!
    // freeze... wtf is this for
    // ghost blocks, I LOVE G KEY
    // high jump <- boringo
    // spider - boring
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
    // discord rpc (idrk how to do this)
    // trajectory's
    // cool font chat
    // auto pot / soup
    // autoplay
    // play statistics or smth
    // microsoft login
    // fix scoreboard not render special chars
    // fix velocity
    // add backtrack
    // add fakelag maybe

    /*
    as of 26/02/24 it has for hypixel:
    - killaura
    - alright autoblock
    - criticals (bad and sometimes self damages...)
    - sprint jump scaffold
    - "noslow" (doesnt work w autoblock or food or bows (FIRE IK)!!)
    - blink nofall
    - omnisprint (u dont need a bypass for this)
    - ground strafe speed
    - pro visuals!!!
     */

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
            // no not really... (later..)
            //"#Fixed Backtrack\n" +
            "#Fixed Reach\n" +
            "#Fixed Fastplace\n" +
            "#Fixed Fastbreak\n" +
            "#Fixed Nuker\n" +
            "#Fixed No Hitboxes\n" +
            "#Fixed Trail\n" +
            "#Fixed Criticals\n" +
            "#Fixed Tower\n" +
            "#Renamed Nuker to Fucker\n" +
            "#Improved Target HUD\n" +
            "\n" +
            "+Added Blink antivoid\n" +
            "+Added Blink\n" +
            "+Added Omnisprint\n" +
            "+Added Animated Main Menu\n" +
            "+Added Outline + 2D ESP\n" +
            "+Added Chams\n" +
            "+Added Nametags + Health Bar\n" +
            "+Added Animations\n" +
            "+Added Sims Crystal\n" +
            "+Added Animations\n" +
            "+Added Model Modifier\n" +
            "+Added Sprint Jump Scaffold\n" +
            "+Added Capes\n" +
            "+Added Jump Circles\n" +
            "+Added Session ID login\n" +
            "+Added Skin / Name Changer\n" +
            "+Added Bed Plates\n" +
            "+Added Custom Scoreboard\n" +
            "+Added More Fonts\n" +
            "+Added IRC\n" +
            "+Added Hypixel Bypasses\n" +
            "\n" +
            "-Removed Not Enough Spanish\n" +
            "-Removed Chat Macros Tab\n" +
            "-Removed No Walls\n" +
            "-Removed CPS Multiplier\n" +
            "-Removed Sumo Fences\n" +
            "-Removed Strafe Module\n" +
            "-Removed Lots Of Random Modules\n" +
            "-Removed Skyblock Features";


    @EventHandler
    public void preInit(FMLPreInitializationEvent var1) {
        if (!new File(sillyfolderpath).exists()) {
            new File(sillyfolderpath).mkdirs();
        }
        if (!new File(sillyfolderpath + "/configs").exists()) new File(sillyfolderpath + "/configs").mkdirs();
        if (!new File(sillyfolderpath + "/accounts").exists()) new File(sillyfolderpath + "/accounts").mkdirs();
        if (!new File(sillyfolderpath + "/extras").exists()) new File(sillyfolderpath + "/extras").mkdirs();

        Display.setTitle("mushroom client " + VERSION);

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
        MinecraftForge.EVENT_BUS.register(new Criticals());
        MinecraftForge.EVENT_BUS.register(new HUD());
        MinecraftForge.EVENT_BUS.register(new ScoreBoard());
        MinecraftForge.EVENT_BUS.register(new IRC());
        MinecraftForge.EVENT_BUS.register(new HealthBars());
        MinecraftForge.EVENT_BUS.register(new JumpCircles());
        MinecraftForge.EVENT_BUS.register(new Trail());

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

    public static boolean loadedExtras = false;
    public static String selectedCat = "Combat";



    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent tick) {
        if (guiToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(guiToOpen);
            guiToOpen = null;
        }


        if (!loadedExtras) {
            try {
                if (!new File(sillyfolderpath + "/extras/sillys.scolio").exists()) new File(sillyfolderpath + "/extras/sillys.scolio").createNewFile();
                else {
                    String fileText = ReadFile(sillyfolderpath + "/extras/sillys.scolio");
                    if (fileText.contains("targetHudX:")) TargetHud.targetHudX = Integer.parseInt(fileText.split("targetHudX:")[1].split(" ")[0]);
                    if (fileText.contains("targetHudY:")) TargetHud.targetHudY = Integer.parseInt(fileText.split("targetHudY:")[1].split(" ")[0]);
                }
            } catch (IOException e) {throw new RuntimeException(e);}

            loadedExtras = true;
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
