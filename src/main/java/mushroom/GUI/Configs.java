package mushroom.GUI;

public class Configs {

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    // combat
    // ------------------------------------------------------------
    // ------------------------------------------------------------

    @Property(type = Property.Type.BOOLEAN, name = "Combat", description = "")
    public static boolean combat = true;
    @Property(type = Property.Type.BOOLEAN, name = "Killaura", description = "i am become death, destroyer of worlds", parent = "Combat")
    public static boolean killaura = false;
    @Property(type = Property.Type.SELECT, name = "Killaura Mode", description = "i am become death, destroyer of worlds (after i finish rotating)", parent = "Killaura", options = {"switch", "single"})
    public static int killauramode = 0;

    @Property(type = Property.Type.BOOLEAN, name = "Move Fix", description = "we zoom?", parent = "Killaura")
    public static boolean moveFixAura = false;
    @Property(type = Property.Type.NUMBER, name = "Aura Reach", description = "HES 6 BLOCKING AHhhh!", parent = "Killaura", min = 3, max = 6)
    public static float aurareach = 3;
    @Property(type = Property.Type.NUMBER, name = "Rotation Range", description = "how far away from the nearest player before rotating", parent = "Killaura", min = 3, max = 12)
    public static float rotationrange = 6;
    @Property(type = Property.Type.NUMBER, name = "Aura FOV", description = "head snap wwtf??! he hacker", parent = "Killaura", min = 30, max = 360)
    public static float aurafov = 60;
    @Property(type = Property.Type.NUMBER, name = "Aura Hitbox Size", description = "more than 15 = hitboxes", parent = "Killaura", min = 1, max = 20)
    public static float auraaccuracy = 9;
    @Property(type = Property.Type.NUMBER, name = "Average CPS", description = "average cps of aura (±3)", parent = "Killaura", min = 4, max = 20)
    public static float averagecps = 4;
    @Property(type = Property.Type.NUMBER, name = "Switch Delay", description = "i wanna hit 2 people at once!", parent = "Killaura Mode", modereq = 0, max = 30)
    public static float switchdelay = 0;
    @Property(type = Property.Type.SELECT, name = "Killaura Rotations", description = "i am become death, destroyer of worlds (after i finish rotating)", parent = "Killaura", options = {"smooth", "simple", "snap", "none"})
    public static int killaurarotmod = 0;
    @Property(type = Property.Type.NUMBER, name = "Smooth Speed", description = "i wanna rotation slowly :sunglas:", parent = "Killaura Rotations", modereq = 0, min = 1, max = 2)
    public static float smoothspeed = 1;
    @Property(type = Property.Type.NUMBER, name = "Min Rotation", description = "minimum rotation per tick for killaura", parent = "Killaura Rotations", modereq = 1, min = 5, max = 180)
    public static float minrotation = 0;
    @Property(type = Property.Type.NUMBER, name = "Max Rotation", description = "max rotation per tick for killaura (HAS TO BE HIGHER THAN MIN)", parent = "Killaura Rotations", modereq = 1, min = 5, max = 180)
    public static float maxrotation = 0;
    @Property(type = Property.Type.SELECT, name = "Killaura Sorting", description = "i am become death, destroyer of worlds (after i finish rotating)", parent = "Killaura", options = {"distance", "hurt"})
    public static int killaurasorting = 0;
    @Property(type = Property.Type.SELECT, name = "Autoblock Mode", description = "helper pls he hit me :(", parent = "Killaura", options = {"none", "fake", "vanilla", "verus", "custom"})
    public static int autoblockmode = 0;
    @Property(type = Property.Type.BOOLEAN, name = "Show blocking", description = "shows if your blocking serverside or not", parent = "Autoblock Mode", modereq = 4)
    public static boolean showBlocking = true;
    @Property(type = Property.Type.BOOLEAN, name = "Max Hit Delay", description = "always swings after a set amount of time", parent = "Autoblock Mode", modereq = 4)
    public static boolean maxHitDelayB = true;
    @Property(type = Property.Type.NUMBER, name = "Max Hit Delay", description = "always swings after a set amount of time", parent = "Autoblock Mode", modereq = 4, max = 300)
    public static float maxHitDelay = 75;
    @Property(type = Property.Type.BOOLEAN, name = "Player hurt check", description = "unblocks if player is invincible due to player hurttime invincibility", parent = "Autoblock Mode", modereq = 4)
    public static boolean playerHurtTimeCheck = true;
    @Property(type = Property.Type.NUMBER, name = "Player hurt check frames", description = "hurt frames to unblock for", parent = "Player hurt check", modereq = 4, max = 10)
    public static float hurtTimeFrames = 4;

    @Property(type = Property.Type.BOOLEAN, name = "Enemy hurt check", description = "checks if the enemy is invincible and blocks if they are", parent = "Autoblock Mode", modereq = 4)
    public static boolean enemyHurtTimeCheck = true;
    @Property(type = Property.Type.NUMBER, name = "Enemy hurt check frames", description = "how many frames before invincibility ends before unblocking", parent = "Enemy hurt check", modereq = 4, max = 10)
    public static float enemyTimeFrames = 4;

    @Property(type = Property.Type.BOOLEAN, name = "Swing Progress Check", description = "Checks progress of previous hit to see if you can swing and hit again, and unblocks", parent = "Autoblock Mode", modereq = 4)
    public static boolean swingProgressCheck = true;
    @Property(type = Property.Type.NUMBER, name = "Swing check frames", description = "amount of frames before being able to swing again", parent = "Swing Progress Check", modereq = 4, max = 1)
    public static float swingCheckFrames = 1;

    @Property(type = Property.Type.NUMBER, name = "Reblock Time Min", description = "time before reblocking", parent = "Autoblock Mode", modereq = 4, max = 300)
    public static float reblockTimeMin = 10;
    @Property(type = Property.Type.NUMBER, name = "Swing Cooldown", description = "time before hitting after block", parent = "Autoblock Mode", modereq = 4, max = 300)
    public static float swingCooldown = 50;
    @Property(type = Property.Type.NUMBER, name = "Reblock Time Max", description = "time before unreblocking", parent = "Autoblock Mode", modereq = 4, max = 300)
    public static float reblockTimeMax = 200;
    @Property(type = Property.Type.BOOLEAN, name = "Autoblock Slowdown", description = "turn this on to bypass anything", parent = "Killaura")
    public static boolean autoblockSlowDown = true;
    @Property(type = Property.Type.BOOLEAN, name = "Swing On Rot", description = "swing on rotation", parent = "Killaura")
    public static boolean swingonrot = false;
    @Property(type = Property.Type.BOOLEAN, name = "Sword Only", description = "only be silly if holding sword", parent = "Killaura")
    public static boolean swordonly = false;
    @Property(type = Property.Type.BOOLEAN, name = "Mouse Down", description = "only if clicking", parent = "Killaura")
    public static boolean mousedown = false;
    @Property(type = Property.Type.BOOLEAN, name = "Invisibles", description = "attack wizards", parent = "Killaura")
    public static boolean invisibles = false;
    @Property(type = Property.Type.BOOLEAN, name = "Raycast", description = "check if through walls", parent = "Killaura")
    public static boolean raytrace = false;

    @Property(type = Property.Type.BOOLEAN, name = "People", description = "beat up real human beings?", parent = "Killaura")
    public static boolean people = false;
    @Property(type = Property.Type.BOOLEAN, name = "Mobs", description = "beat animals too!", parent = "Killaura")
    public static boolean mobs = false;
    @Property(type = Property.Type.BOOLEAN, name = "Teams", description = "dont want to beat up your friends?", parent = "Killaura")
    public static boolean teams = false;
    @Property(type = Property.Type.BOOLEAN, name = "Hit Boxes", description = "expands player hitboxes", parent = "Combat")
    public static boolean hitboxes = false;
    @Property(type = Property.Type.NUMBER, name = "Hit Boxes Expansion", description = "expands player hitboxes by a set amount", parent = "Hit Boxes", max = 4)
    public static float hitboxesExpand = 0.1f;
    @Property(type = Property.Type.BOOLEAN, name = "Block Hit", description = "allows you to hit while blocking", parent = "Combat")
    public static boolean blockhit = false;
    @Property(type = Property.Type.BOOLEAN, name = "Reach", description = "extends your reach", parent = "Combat")
    public static boolean reach = false;
    @Property(type = Property.Type.NUMBER, name = "Reach Distance", description = "how far u wanna reach?", parent = "Reach", max = 6)
    public static float reachdistance = 3;
    @Property(type = Property.Type.NUMBER, name = "Blocks Reach Distance", description = "how far u wanna reach blocks?", parent = "Reach", max = 6)
    public static float blockreachdistance = 5;
    //@Property(type = Property.Type.BOOLEAN, name = "Aimbot", description = "locks on to opponents within reach", parent = "Combat")
    //public static boolean aimbot = false;
    @Property(type = Property.Type.BOOLEAN, name = "Backtrack", description = "allows you to hit players from a previous position they were in (dont use)", parent = "Combat")
    public static boolean backtrack = false;
    @Property(type = Property.Type.NUMBER, name = "Backtrack Time", description = "how far do you want to time travel", parent = "Backtrack", min = 1, max = 50)
    public static float backtracktime = 10;
    @Property(type = Property.Type.NUMBER, name = "Backtrack Reach", description = "*slap*", parent = "Backtrack", min = 3, max = 6)
    public static float backtrackreach = 6;
    //@Property(type = Property.Type.BOOLEAN, name = "Triggerbot", description = "hits players you are looking at within reach", parent = "Combat")
    //public static boolean triggerbot = false;

    // might add this one back
    //@Property(type = Property.Type.BOOLEAN, name = "Sumo Fences", description = "puts fences around the hypixel sumo arena", parent = "Combat")
    //public static boolean sumofences = false;


    //@Property(type = Property.Type.BOOLEAN, name = "TP Aura", description = "killauras players from up to 64 blocks away", parent = "Combat")
    //public static boolean tpaura = false;
    @Property(type = Property.Type.BOOLEAN, name = "Velocity", description = "modifies knockback taken", parent = "Combat")
    public static boolean velocity = false;
    @Property(type = Property.Type.NUMBER, name = "Horizontal", description = "modifies horizontal taken", parent = "Velocity", max = 2)
    public static float horizontalmodifier = 0;
    @Property(type = Property.Type.NUMBER, name = "Vertical", description = "modifies vertical taken", parent = "Velocity", max = 2)
    public static float verticalmodifier = 1;
    @Property(type = Property.Type.SELECT, name = "Velocity Mode", description = "what mode", parent = "Velocity", options = {"vanilla", "delayed", "boost", "delayed boost", "bhop"})
    public static int velocitymode = 0;
    @Property(type = Property.Type.NUMBER, name = "Velocity Delay", description = "modifies vertical taken", parent = "Velocity Mode", modereq = 1, max = 10)
    public static float velocitydelay = 1;
    @Property(type = Property.Type.NUMBER, name = "Boost Speed", description = "modifies vertical taken", parent = "Velocity Mode", modereq = 2, max = 5)
    public static float velocityboostspeed = 1;

    @Property(type = Property.Type.NUMBER, name = "Boost Delay", description = "ZOOOOOOOOM", parent = "Velocity Mode", modereq = 3, max = 10)
    public static float velocityboostdelay = 1;
    @Property(type = Property.Type.NUMBER, name = "Delayed Boost Speed", description = "ZOOOOOOOOM?!?!?", parent = "Velocity Mode", modereq = 3, max = 5)
    public static float delayedboostspeed = 1;
    @Property(type = Property.Type.NUMBER, name = "Velo Bhop Speed", description = "ZOOOOOOOOM?!?!?", parent = "Velocity Mode", modereq = 4, max = 10)
    public static float velobhopspeed = 1;
    @Property(type = Property.Type.BOOLEAN, name = "Velo Toggle Timer", description = "zoom", parent = "Velocity Mode", modereq = 4)
    public static boolean velotoggletimer = false;
    @Property(type = Property.Type.NUMBER, name = "Velo Timer Speed", description = "ZOOOOOOOOM?!?!?", parent = "Velo Toggle Timer", max = 5)
    public static float velotimerspeed = 1;

    @Property(type = Property.Type.BOOLEAN, name = "Anti Bot", description = "i do NOT wanna hit npcs.", parent = "Combat")
    public static boolean antibot = false;
    @Property(type = Property.Type.BOOLEAN, name = "Tab Check", description = "checks ticks existed in tab", parent = "Anti Bot")
    public static boolean tabCheck = false;
    @Property(type = Property.Type.BOOLEAN, name = "Invis Check", description = "checks invis ticks", parent = "Anti Bot")
    public static boolean invisCheck = false;
    @Property(type = Property.Type.BOOLEAN, name = "NPC Check", description = "this guy is FAKE", parent = "Anti Bot")
    public static boolean NPCcheck = false;

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    // visuals
    // ------------------------------------------------------------
    // ------------------------------------------------------------


    @Property(type = Property.Type.BOOLEAN, name = "Visual", description = "")
    public static boolean visuals = true;

    @Property(type = Property.Type.BOOLEAN, name = "Array List", description = "shows toggled modules", parent = "Visual")
    public static boolean arraylist = true;

    @Property(type = Property.Type.SELECT, name = "Color", description = "color theme of the arraylist", parent = "Array List", options = {"theme", "tenacity", "hacker", "mushroom", "bubblegum", "custom"})
    public static int arraycolor = 3;

    @Property(type = Property.Type.COLOR, name = "Custom Color", description = "color of arraylist", parent = "Color", modereq = 5)
    public static int[] arraylistCustCol = new int[] {255, 0, 0, 255, 255, 0, 0, 0};
    @Property(type = Property.Type.COLOR, name = "Custom Color 2", description = "color of arraylist", parent = "Color", modereq = 5)
    public static int[] arraylistCustCol2 = new int[] {255, 255, 255, 255, 255, 0, 0, 0};

    @Property(type = Property.Type.BOOLEAN, name = "Show Mode", description = "show the mode of settings", parent = "Array List")
    public static boolean showmode = true;
    @Property(type = Property.Type.SELECT, name = "Sideline", description = "draws a line on the side of the array list", parent = "Array List", options = {"none", "cool", "joined", "surround"})
    public static int sideline = 1;
    @Property(type = Property.Type.BOOLEAN, name = "No Spaces", description = "no spaces in the words", parent = "Array List")
    public static boolean nospacesarraylist = false;
    @Property(type = Property.Type.BOOLEAN, name = "No Capitals", description = "no capitals in the words", parent = "Array List")
    public static boolean nocapitalssarraylist = true;
    @Property(type = Property.Type.SELECT, name = "Array Position", description = "position of the array list", parent = "Array List", options = {"left", "right"})
    public static int arraylistpos = 1;

    @Property(type = Property.Type.SELECT, name = "Array Sorting", description = "what order the array list is displayed in", parent = "Array List", options = {"alphabet", "size", "category"})
    public static int arraysort = 1;

    @Property(type = Property.Type.NUMBER, name = "Array Fade Speed", description = "whether or not the array list should be animated", parent = "Array List", max = 5)
    public static float arraylistspeed = 1;

    @Property(type = Property.Type.NUMBER, name = "Fade Center", description = "wow!", parent = "Array List", min = 10, max = 500)
    public static float arrayfadecentre = 100;

    @Property(type = Property.Type.BOOLEAN, name = "Click GUI", description = "this gui", parent = "Visual")
    public static boolean clickgui = false;
    @Property(type = Property.Type.SELECT, name = "GUI Color", description = "color of the gui", parent = "Click GUI", options = {"tenacity", "hacker", "mushroom", "bubblegum", "custom"})
    public static int cguicolor = 3;

    @Property(type = Property.Type.NUMBER, name = "Buttons Width", description = "width of buttons", parent = "Click GUI", min = 25, max = 200)
    public static float buttonsWidth = 150;
    @Property(type = Property.Type.NUMBER, name = "Buttons Height", description = "gui color going up and down!", parent = "Click GUI", min = 5, max = 30)
    public static float buttonsHeight = 15;

    @Property(type = Property.Type.COLOR, name = "Background Color", description = "color of normal buttons", parent = "GUI Color", modereq = 4)
    public static int[] clickguiCustomColorBack = new int[] {21, 21, 21, 255, 255, 0, 0, 0};
    @Property(type = Property.Type.COLOR, name = "Background Color 2", description = "faded color of normal buttons", parent = "GUI Color", modereq = 4)
    public static int[] clickguiCustomColorBack2 = new int[] {21, 21, 21, 255, 255, 0, 0, 0};

    @Property(type = Property.Type.COLOR, name = "Toggled Color", description = "color of on buttons", parent = "GUI Color", modereq = 4)
    public static int[] clickguiCustomColorTog = new int[] {255, 0, 0, 255, 255, 0, 0, 0};
    @Property(type = Property.Type.COLOR, name = "Toggled Color 2", description = "faded color of on buttons", parent = "GUI Color", modereq = 4)
    public static int[] clickguiCustomColorTog2 = new int[] {255, 255, 255, 255, 255, 0, 0, 0};

    @Property(type = Property.Type.COLOR, name = "Surround Color", description = "color of border around buttons", parent = "GUI Color", modereq = 4)
    public static int[] clickguiCustomColorSur = new int[] {255, 0, 0, 255, 255, 0, 0, 0};
    @Property(type = Property.Type.COLOR, name = "Surround Color 2", description = "faded color of border around buttons", parent = "GUI Color", modereq = 4)
    public static int[] clickguiCustomColorSur2 = new int[] {255, 255, 255, 255, 255, 0, 0, 0};


    @Property(type = Property.Type.BOOLEAN, name = "Anime Girls", description = "shows silly little anime girls in the bottom right", parent = "Click GUI")
    public static boolean animegirlsinGUi = true;
    @Property(type = Property.Type.SELECT, name = "Anime Girl", description = "whats one you want!!", parent = "Click GUI", options = {"purple hair", "red hair", "bread", "pink hair", "catgirl"})
    public static int animegirl = 1;
    @Property(type = Property.Type.BOOLEAN, name = "No Background", description = "removes the gray background from the gui", parent = "Click GUI")
    public static boolean noBackground = false;

    @Property(type = Property.Type.BOOLEAN, name = "Fade Sideways", description = "makes the cool fade effect sideways instead of down", parent = "Click GUI")
    public static boolean fadesideways = false;
    @Property(type = Property.Type.NUMBER, name = "GUI Fade Speed", description = "gui color going up and down!", parent = "Click GUI", max = 10)
    public static float guifadespeed = 3;
    @Property(type = Property.Type.NUMBER, name = "GUI Fade Centre", description = "gui color going up and down!", parent = "Click GUI", min = 25, max = 500)
    public static float guifadecent = 200;

    @Property(type = Property.Type.SELECT, name = "Categorys Text Center", description = "where should the text be in boxes", parent = "Click GUI", options = {"left", "centre", "right"})
    public static int categorystextcenter = 0;
    @Property(type = Property.Type.SELECT, name = "Text Center", description = "where should the text be in boxes", parent = "Click GUI", options = {"left", "centre", "right"})
    public static int textcenter = 0;
    @Property(type = Property.Type.SELECT, name = "Configs Text Center", description = "where should the text be in boxes", parent = "Click GUI", options = {"left", "centre", "right"})
    public static int configstextcenter = 0;
    @Property(type = Property.Type.SELECT, name = "Extras Text Center", description = "where should the text be in boxes, also idfk why i made this one an option, its SO UGLY", parent = "Click GUI", options = {"left", "centre", "right"})
    public static int extrastextcenter = 0;
    @Property(type = Property.Type.BOOLEAN, name = "No Space", description = "no spaces in the gui", parent = "Click GUI")
    public static boolean nospacescgui = false;
    @Property(type = Property.Type.BOOLEAN, name = "No Capital", description = "no capitals in the gui", parent = "Click GUI")
    public static boolean nocapitalscgui = true;

    @Property(type = Property.Type.BOOLEAN, name = "Sides", description = "the line down the side of all the options, on / off", parent = "Click GUI")
    public static boolean sidescgui = true;

    @Property(type = Property.Type.BOOLEAN, name = "Silly Main Menu", description = "dont turn this off it looks awesome", parent = "Visual")
    public static boolean sillymainmenu = true;
    @Property(type = Property.Type.SELECT, name = "Background", description = ":trol:", parent = "Silly Main Menu", options = {"angry birds", "wtf", "lesbians", "spanish", "anime girl"})
    public static int mainmenugif = 4;
    @Property(type = Property.Type.BOOLEAN, name = "Loop Gif", description = "loop gif?!", parent = "Silly Main Menu")
    public static boolean loopgif = true;
    @Property(type = Property.Type.BOOLEAN, name = "Show Array List", description = "i like arraylist", parent = "Silly Main Menu")
    public static boolean showarraylistinmainmenu = false;
    @Property(type = Property.Type.BOOLEAN, name = "Show Change Log", description = "changes since last update", parent = "Silly Main Menu")
    public static boolean showchangelog = true;

    @Property(type = Property.Type.NUMBER, name = "Gif Speed", description = "speed of the gif in the background", parent = "Silly Main Menu", max = 5)
    public static float gifSpeed = 1.5f;

    @Property(type = Property.Type.NUMBER, name = "Buttons Fade Speed", description = "speed of the buttons animation", parent = "Silly Main Menu", min = 1, max = 100)
    public static float butFadeSpeed = 50;

    @Property(type = Property.Type.COLOR, name = "Button Colors", description = "color of buttons on main menu", parent = "Silly Main Menu")
    public static int[] mainMenuButtonColors = new int[] {255, 0, 0, 120, 255, 0, 0, 0};
    @Property(type = Property.Type.COLOR, name = "Button Colors 2", description = "faded color of buttons on main menu", parent = "Silly Main Menu")
    public static int[] mainMenuButtonColors2 = new int[] {255, 255, 255, 120, 255, 0, 0, 0};

    @Property(type = Property.Type.COLOR, name = "Hover Button Colors", description = "color of buttons on main menu when mouse over", parent = "Silly Main Menu", modereq = 4)
    public static int[] mainMenuButtonHoverColors = new int[] {255, 0, 0, 200, 255, 0, 0, 0};
    @Property(type = Property.Type.COLOR, name = "Hover Button Colors 2", description = "faded color of buttons on main menu when mouse over", parent = "Silly Main Menu", modereq = 4)
    public static int[] mainMenuButtonHoverColors2 = new int[] {255, 255, 200, 200, 255, 0, 0, 0};


    @Property(type = Property.Type.BOOLEAN, name = "Player ESP", description = "highlights players through walls", parent = "Visual")
    public static boolean playeresp = true;
    @Property(type = Property.Type.BOOLEAN, name = "Chams", description = "shows players as if they weren't behind a wall", parent = "Player ESP")
    public static boolean chams = true;
    @Property(type = Property.Type.BOOLEAN, name = "Outline", description = "shows outline of players", parent = "Player ESP")
    public static boolean outline = false;
    @Property(type = Property.Type.BOOLEAN, name = "2D", description = "2d square around players", parent = "Player ESP")
    public static boolean twoDee = false;
    @Property(type = Property.Type.BOOLEAN, name = "Chams Health", description = "change color of player depending on health", parent = "Chams")
    public static boolean showhealth = false;
    @Property(type = Property.Type.BOOLEAN, name = "Name Tags", description = "epic name tags", parent = "Visual")
    public static boolean nametags = true;
    @Property(type = Property.Type.BOOLEAN, name = "Show Own Name Tag", description = "yayy", parent = "Name Tags")
    public static boolean showownnametag = true;
    @Property(type = Property.Type.BOOLEAN, name = "Health Bar On Side", description = "raven b4 vibes", parent = "Name Tags")
    public static boolean healthbaronside = true;
    //@Property(type = Property.Type.BOOLEAN, name = "Chest ESP", description = "highlights chests through walls", parent = "Visual")
    //public static boolean chestesp = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Skull ESP", description = "highlights skulls through walls", parent = "Visual")
    //public static boolean skullesp = false;
    @Property(type = Property.Type.BOOLEAN, name = "Nick Hider", description = "replace your name with something silly", parent = "Visual")
    public static boolean nickhider = true;
    @Property(type = Property.Type.TEXT, name = "Fake Name", description = "the silly name in question", parent = "Nick Hider")
    public static String fakename = "mushroom";
    //@Property(type = Property.Type.BOOLEAN, name = "Derp", description = "makes your head spin", parent = "Visual")
    //public static boolean derp = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Trail", description = "draws a trail behind you", parent = "Visual")
    //public static boolean trail = false;
    @Property(type = Property.Type.BOOLEAN, name = "No Hurtcam", description = "removes the camera shake when hit", parent = "Visual")
    public static boolean nohurtcam = false;
    @Property(type = Property.Type.BOOLEAN, name = "Hilarity", description = "lolololol much funny", parent = "Visual")
    public static boolean hilarity = true;
    @Property(type = Property.Type.NUMBER, name = "Hilarity Timer", description = "how much of the funny is too funy?", parent = "Hilarity", min = 1, max = 10000)
    public static float hilaritytimer = 1000;

    @Property(type = Property.Type.BOOLEAN, name = "Animations", description = "awesome sword blocking animations", parent = "Visual")
    public static boolean animations = false;
    @Property(type = Property.Type.BOOLEAN, name = "Smooth Swing", description = "awesome little swing animation", parent = "Animations")
    public static boolean smoothSwing = true;
    @Property(type = Property.Type.SELECT, name = "Animation Mode", description = "awesome sword blocking animations :sunglas:", parent = "Animations", options = {"1.7", "cool", "spin", "push", "slow", "punch", "wipe", "whack"})
    public static int animationmode = 4;
    @Property(type = Property.Type.NUMBER, name = "Held Item X", description = "offset to render hand", parent = "Animations", min = 0.01f, max = 3)
    public static float animationsX = 1;
    @Property(type = Property.Type.NUMBER, name = "Held Item Y", description = "offset to render hand", parent = "Animations", min = 0.01f, max = 3)
    public static float animationsY = 1;
    @Property(type = Property.Type.NUMBER, name = "Held Item Z", description = "offset to render hand", parent = "Animations", min = 0.01f, max = 3)
    public static float animationsZ = 1;
    @Property(type = Property.Type.NUMBER, name = "Held Item Size", description = "offset to render hand", parent = "Animations", min = 0.01f, max = 1)
    public static float animationsSize = 1;
    @Property(type = Property.Type.NUMBER, name = "Animation Speed", description = "offset to render hand", parent = "Animations", min = 0.01f, max = 1)
    public static float animationsSpeed = 0.4f;
    @Property(type = Property.Type.BOOLEAN, name = "Target Hud", description = "shows info about the person you are fighting", parent = "Visual")
    public static boolean targethud = true;
    @Property(type = Property.Type.BOOLEAN, name = "Follow", description = "if the target hud should move with the target", parent = "Target Hud")
    public static boolean followTargetHud = true;

    //@Property(type = Property.Type.BOOLEAN, name = "Kapow", description = "awesome sword blocking animations", parent = "Visual")
    //public static boolean kapow = false;
    @Property(type = Property.Type.BOOLEAN, name = "Full Bright", description = "the brightness hurt me eye :cry:", parent = "Visual")
    public static boolean fullbright = true;

    @Property(type = Property.Type.BOOLEAN, name = "Sims Crystal", description = "awesome sauce sims crystal", parent = "Visual")
    public static boolean simscryst = true;
    @Property(type = Property.Type.BOOLEAN, name = "Other People", description = "draw sims crystal above other players", parent = "Sims Crystal")
    public static boolean simsESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show In First Person", description = "self explanatory", parent = "Sims Crystal")
    public static boolean firstPersontoo = true;

    @Property(type = Property.Type.SELECT, name = "Crystal mode", description = "heck yeah china hat", parent = "Sims Crystal", options = {"crystal", "china hat"})
    public static int crystalMode = 0;

    @Property(type = Property.Type.NUMBER, name = "Radius", description = "size of crystal in width", parent = "Sims Crystal", min = 0.1f, max = 2)
    public static float radiusCrstal = 0.5f;
    @Property(type = Property.Type.NUMBER, name = "Height", description = "size of crystal in height", parent = "Sims Crystal", min = 0.1f, max = 2)
    public static float heightCrystal = 0.5f;
    @Property(type = Property.Type.NUMBER, name = "Position", description = "how high above the crystal is", parent = "Sims Crystal", max = 5)
    public static float crytalpos = 1;
    @Property(type = Property.Type.NUMBER, name = "Spin Speed", description = "how fast is spins", parent = "Sims Crystal", max = 10)
    public static float spinspeed = 5;
    @Property(type = Property.Type.NUMBER, name = "Angles", description = "how fast is spins", parent = "Sims Crystal", min = 4, max = 90)
    public static float angles = 4;


    @Property(type = Property.Type.BOOLEAN, name = "Model Modifier", description = "choose model size :)", parent = "Visual")
    public static boolean modelModifier = true;
    @Property(type = Property.Type.NUMBER, name = "Model Width", description = "choose model width :)", parent = "Model Modifier", min = 0.01f, max = 5)
    public static float modelWidth = 1;
    @Property(type = Property.Type.NUMBER, name = "Model Depth", description = "choose model depth :)", parent = "Model Modifier", min = 0.01f, max = 5)
    public static float modelDepth = 1;
    @Property(type = Property.Type.NUMBER, name = "Model Height", description = "choose model height :)", parent = "Model Modifier", min = 0.01f, max = 5)
    public static float modelHeight = 1;

    @Property(type = Property.Type.BOOLEAN, name = "Capes", description = "awesome sauce capes", parent = "Visual")
    public static boolean capes = true;
    @Property(type = Property.Type.SELECT, name = "Cape", description = "what cape you want", parent = "Capes", options = {"2011 minecon", "2012 minecon", "2013 minecon", "2015 minecon", "2016 minecon", "silly cape", "women kissing", "wtf", "mushroom", "anime girl"})
    public static int caperNum = 0;

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    // movement
    // ------------------------------------------------------------
    // ------------------------------------------------------------


    @Property(type = Property.Type.BOOLEAN, name = "Movement", description = "")
    public static boolean movement = true;

    //@Property(type = Property.Type.BOOLEAN, name = "Inv Walk", description = "allows you to move while in inventory's", parent = "Movement")
    //public static boolean invwalk = false;
    @Property(type = Property.Type.BOOLEAN, name = "Speed", description = "allows you to move faster", parent = "Movement")
    public static boolean speed = false;
    @Property(type = Property.Type.SELECT, name = "Speed Mode", description = "allows you to move faster", parent = "Speed", options = {"vanilla", "ground strafe"})
    public static int speedmode = 0;
    @Property(type = Property.Type.NUMBER, name = "Speed Speed", description = "speed of the speed", parent = "Speed", min = 0.01f, max = 5)
    public static float speedspeed = 3;
    @Property(type = Property.Type.BOOLEAN, name = "Disable On Flag", description = "disables when flagged by anticheat", parent = "Speed")
    public static boolean disablespeedonflag = false;
    @Property(type = Property.Type.BOOLEAN, name = "Timer With Speed", description = "timer while speeding", parent = "Speed")
    public static boolean timeronspeed = false;
    @Property(type = Property.Type.NUMBER, name = "Timer Speed", description = "timer while speeding", parent = "Timer With Speed", min = 1, max = 10)
    public static float timerspeedonspeed = 10;

    @Property(type = Property.Type.BOOLEAN, name = "Fly", description = "take flight", parent = "Movement")
    public static boolean fly = false;

    @Property(type = Property.Type.BOOLEAN, name = "Spoof Ground", description = "take flight badly", parent = "Fly")
    public static boolean spoofground = false;
    @Property(type = Property.Type.SELECT, name = "Fly Mode", description = "what anticheat?!", parent = "Fly", options = {"vanilla", "verusdamage"})
    public static int flymode = 0;
    @Property(type = Property.Type.NUMBER, name = "Horizontal Speed", description = "take flight", parent = "Fly", max = 5)
    public static float flyhorizonstalspeed = 1;
    @Property(type = Property.Type.NUMBER, name = "Vertical Speed", description = "take flight", parent = "Fly", max = 5)
    public static float flyverticlespeed = 1;
    @Property(type = Property.Type.BOOLEAN, name = "Toggle Timer", description = "i want time to freeze while i fly!", parent = "Fly Mode", modereq = 0)
    public static boolean toggletimeronfly = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Freeze", description = "stops you from moving", parent = "Movement")
    //public static boolean freeze = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Ghost Block", description = "removes blocks infront of you upon keybind press", parent = "Movement")
    //public static boolean ghostblock = false;
    //@Property(type = Property.Type.BOOLEAN, name = "High Jump", description = "lets you jump higher", parent = "Movement")
    //public static boolean highjump = false;
    @Property(type = Property.Type.BOOLEAN, name = "No Fall", description = "stops fall damage", parent = "Movement")
    public static boolean nofall = false;
    @Property(type = Property.Type.SELECT, name = "No Fall Mode", description = "what anticheat!", parent = "No Fall", options = {"noground", "groundspoof", "packet", "blink"})
    public static int nofallmode = 0;
    @Property(type = Property.Type.NUMBER, name = "No Fall Height", description = "how high before spoof ground!", parent = "No Fall", max = 6)
    public static float nofallheight = 0;
    //@Property(type = Property.Type.BOOLEAN, name = "Spider", description = "allows you to climb walls", parent = "Movement")
    //public static boolean spider = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Strafe", description = "lets you strafe faster", parent = "Movement")
    //public static boolean strafe = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Target Strafe", description = "hops around targets making you hard to hit", parent = "Movement")
    //public static boolean targetstrafe = false;
    @Property(type = Property.Type.BOOLEAN, name = "No Slow", description = "stops being slowed by swords, bows and consumables", parent = "Movement")
    public static boolean noslow = false;
    @Property(type = Property.Type.SELECT, name = "No Slow Mode", description = "what anticheat!", parent = "No Slow", options = {"vanilla", "verus", "blink"})
    public static int noslowmode = 0;
    @Property(type = Property.Type.NUMBER, name = "Packets Pulse", description = "time between each unblink", parent = "No Slow Mode", min = 1, max = 20, modereq = 2)
    public static float packetspulsenoslow = 20;
    @Property(type = Property.Type.BOOLEAN, name = "Unblock", description = "stops being slowed by swords, bows and consumables", parent = "No Slow Mode", modereq = 2)
    public static boolean unblocknoslow = false;
    @Property(type = Property.Type.BOOLEAN, name = "Swing On Unblock", description = "stops being slowed by swords, bows and consumables", parent = "No Slow Mode", modereq = 2)
    public static boolean swingnoslow = false;
    @Property(type = Property.Type.NUMBER, name = "Sword Speed", description = "i wanna be faster >:(", parent = "No Slow", max = 1)
    public static float noslowswordspeed = 1;
    @Property(type = Property.Type.NUMBER, name = "Bow Speed", description = "i wanna be faster >:(", parent = "No Slow", max = 1)
    public static float noslowbowspeed = 1;
    @Property(type = Property.Type.NUMBER, name = "Eat Speed", description = "i wanna be faster >:(", parent = "No Slow", max = 1)
    public static float nosloweatspeed = 1;
    //@Property(type = Property.Type.BOOLEAN, name = "Step", description = "lets you climb blocks faster", parent = "Movement")
    //public static boolean step = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Sneak", description = "lets you move faster while sneaking", parent = "Movement")
    //public static boolean sneak = false;
    @Property(type = Property.Type.BOOLEAN, name = "Timer", description = "makes time move at a different speed for you", parent = "Movement")
    public static boolean timer = false;
    @Property(type = Property.Type.NUMBER, name = "Timer Speed", description = "i wanna be a snail when i grow up", parent = "Timer", max = 10)
    public static float timerspeed = 0;
    @Property(type = Property.Type.BOOLEAN, name = "Blink", description = "i lagger wtf !!!", parent = "Movement")
    public static boolean blink = false;
    @Property(type = Property.Type.BOOLEAN, name = "Only Pos", description = "zoom", parent = "Blink")
    public static boolean onlypos = false;
    @Property(type = Property.Type.BOOLEAN, name = "Pulse", description = "sends packets in bursts", parent = "Blink")
    public static boolean pulse = false;
    @Property(type = Property.Type.NUMBER, name = "Pulse Ticks", description = "how many!", parent = "Pulse", min = 1, max = 100)
    public static float pulseticks = 10;

    @Property(type = Property.Type.BOOLEAN, name = "Anti Void", description = "i do NOT want to fall into the void", parent = "Movement")
    public static boolean antivoid = false;
    @Property(type = Property.Type.BOOLEAN, name = "Sprint", description = "toggle sprint without the toggling", parent = "Movement")
    public static boolean sprint = false;
    @Property(type = Property.Type.SELECT, name = "Sprint Mode", description = "i wanna hack >:(", parent = "Sprint", options = {"legit", "omni", "hypixelomni"})
    public static int sprintmode = 0;

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    // macros
    // ------------------------------------------------------------
    // ------------------------------------------------------------


    @Property(type = Property.Type.BOOLEAN, name = "Macros", description = "")
    public static boolean macros = true;

    //@Property(type = Property.Type.BOOLEAN, name = "Chest Aura", description = "automatically opens nearby chests", parent = "Macros")
    //public static boolean chestaura = false;
    @Property(type = Property.Type.BOOLEAN, name = "Chest Stealer", description = "automatically steals items in opened chests", parent = "Macros")
    public static boolean cheststealer = false;
    @Property(type = Property.Type.BOOLEAN, name = "Name Check", description = "check if actually in a chest not in a random inv", parent = "Chest Stealer")
    public static boolean namecheck = false;
    @Property(type = Property.Type.BOOLEAN, name = "Steal Trash", description = "steal shit that no one wants", parent = "Chest Stealer")
    public static boolean stealtrash = false;
    @Property(type = Property.Type.NUMBER, name = "Delay Between Items", description = "delay between each item being stole", parent = "Chest Stealer", max = 1000)
    public static float delaybetweenitems = 0;
    //@Property(type = Property.Type.NUMBER, name = "Delay Before Steal", description = "delay between each item being stole", parent = "Chest Stealer", max = 1000)
    //public static float delaybeforesteal = 0;
    @Property(type = Property.Type.BOOLEAN, name = "Auto Close", description = "autoclose the chest after finished stealing", parent = "Chest Stealer")
    public static boolean autoclose = false;
    @Property(type = Property.Type.BOOLEAN, name = "Inv Manager", description = "automatically sorts your inventory", parent = "Macros")
    public static boolean invmanager = false;
    @Property(type = Property.Type.SELECT, name = "Mode", description = "open inv or silent flagging :sunglas:", parent = "Inv Manager", options = {"open inv", "always"})
    public static int invmanagermode = 0;
    @Property(type = Property.Type.NUMBER, name = "Delay", description = "delay between clicking items", parent = "Inv Manager", max = 1000)
    public static float invmanagerdelay = 0;
    //@Property(type = Property.Type.BOOLEAN, name = "Skull Aura", description = "automatically clicks nearby skulls", parent = "Macros")
    //public static boolean skullaura = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Lever Aura", description = "automatically flicks nearby levers", parent = "Macros")
    //public static boolean leveraura = false;
    //@Property(type = Property.Type.BOOLEAN, name = "W Tap", description = "causes enemy's to take more knockback", parent = "Macros")
    //public static boolean wtap = false;
    @Property(type = Property.Type.BOOLEAN, name = "Fast Break", description = "speeds up block breaking speed", parent = "Macros")
    public static boolean fastbreak = false;
    @Property(type = Property.Type.NUMBER, name = "Break Speed", description = "fast break speed", parent = "Fast Break", max = 2)
    public static float breakSpeed = 0;
    @Property(type = Property.Type.NUMBER, name = "Max Blocks", description = "max blocks per tick", parent = "Fast Break", min = 1, max = 3)
    public static float maxBlocks = 0;
    @Property(type = Property.Type.BOOLEAN, name = "Fucker", description = "breaks nearby beds", parent = "Macros")
    public static boolean fucker = false;

    @Property(type = Property.Type.SELECT, name = "Fucker Mode", description = "wyd", parent = "Fucker", options = {"vanilla", "instant", "hypixel"})
    public static int fuckerMode = 2;
    @Property(type = Property.Type.BOOLEAN, name = "Fucker Rotations", description = "head snaps to a random point on the bed", parent = "Fucker")
    public static boolean fuckerRotations = true;
    @Property(type = Property.Type.BOOLEAN, name = "Fucker Move Fix", description = "slows you down when bed nuking, helps bypass", parent = "Fucker Rotations")
    public static boolean notAGoodMoveFixFucker = true;
    @Property(type = Property.Type.NUMBER, name = "Fucker Rotations Speed", description = "speed of head snap", parent = "Fucker Rotations", min = 30, max = 150)
    public static float fuckerRotationsSpeed = 150;

    @Property(type = Property.Type.NUMBER, name = "Fucker Reach", description = "how far we at?", parent = "Fucker", min = 1, max = 6)
    public static float fuckerReach = 3;
    @Property(type = Property.Type.SELECT, name = "Swing Type", description = "how far we at?", parent = "Fucker", options = {"Packet", "Normal"})
    public static int fuckerSwing = 0;
    @Property(type = Property.Type.BOOLEAN, name = "Auto Tool", description = "automatically swaps to the best tool when breaking blocks", parent = "Macros")
    public static boolean autotool = false;
    @Property(type = Property.Type.BOOLEAN, name = "Tools", description = "automatically swaps to the best tool when breaking blocks", parent = "Auto Tool")
    public static boolean autotoolTools = false;
    @Property(type = Property.Type.BOOLEAN, name = "Sword", description = "automatically swaps to the best sword when hitting baddies", parent = "Auto Tool")
    public static boolean autotoolWeapon = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Machine Gun", description = "makes bow fire instantly", parent = "Macros")
    //public static boolean machinegun = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Bow Aimbot", description = "automatically aims for you when shooting a bow", parent = "Macros")
    //public static boolean bowaimbot = false;
    //@Property(type = Property.Type.BOOLEAN, name = "Auto Clicker", description = "automatically clicks for you", parent = "Macros")
    //public static boolean autoclicker = false;
    @Property(type = Property.Type.BOOLEAN, name = "Scaffold", description = "automatically bridges for you", parent = "Macros")
    public static boolean scaffold = false;
    @Property(type = Property.Type.BOOLEAN, name = "Safe Scaffold", description = "safewalks when scaffold is on!", parent = "Scaffold")
    public static boolean safewalkScaffold = true;
    @Property(type = Property.Type.BOOLEAN, name = "Air Safe Scaffold", description = "safewalks when scaffold is on!", parent = "Safe Scaffold")
    public static boolean airSafewalkScaffold = false;
    @Property(type = Property.Type.SELECT, name = "Rotation Pos", description = "where to look when scaffolding", parent = "Scaffold", options = {"closest", "random"})
    public static int rotpos = 0;
    @Property(type = Property.Type.SELECT, name = "Place Time", description = "post probably insta bans on all servers", parent = "Scaffold", options = {"pre", "tick", "post"})
    public static int placetime = 1;
    @Property(type = Property.Type.SELECT, name = "Sprint Modes", description = "i wanna go fast", parent = "Scaffold", options = {"sprint", "semi", "verus", "none", "hypixel1", "hypixel2"})
    public static int scafsprintmode = 0;

    @Property(type = Property.Type.NUMBER, name = "Semi Sprint Speed", description = "nyooom", parent = "Sprint Modes", modereq = 1, max = 1)
    public static float semisprintspeed = 0.5f;

    @Property(type = Property.Type.NUMBER, name = "Scaffold Jump Multiplier", description = "nyooom", parent = "Scaffold", max = 5)
    public static float scaffoldJumpSpeed = 1.7f;

    @Property(type = Property.Type.BOOLEAN, name = "Jump Sprint", description = "nyooom", parent = "Scaffold")
    public static boolean scaffoldJumpSprint = true;

    @Property(type = Property.Type.BOOLEAN, name = "Max Y Velo", description = "max Y velocity to place blocks, stops flags from tower go brrrrr", parent = "Scaffold")
    public static boolean maxYVelo = true;
    @Property(type = Property.Type.NUMBER, name = "Max Y Velocity", description = "max Y velocity to place blocks, stops flags from tower go brrrrr", parent = "Max Y Velo", max = 1)
    public static float maxYVeloc = 0.2f;

    @Property(type = Property.Type.SELECT, name = "Tower Mode", description = "i wanna get high", parent = "Scaffold", options = {"low jump", "normal"})
    public static int towermode = 1;
    @Property(type = Property.Type.SELECT, name = "Block Swap", description = "some anticheats hate packets >:(", parent = "Scaffold", options = {"normal", "packet"})
    public static int blockswap = 0;

    @Property(type = Property.Type.NUMBER, name = "Scaffold Dist", description = "i wanna clutch", parent = "Scaffold", max = 6)
    public static float scaffolddist = 0f;
    @Property(type = Property.Type.NUMBER, name = "Place Min Delay", description = "i wanna be speed", parent = "Scaffold", max = 5)
    public static float placemindelay = 1f;
    @Property(type = Property.Type.NUMBER, name = "Place Max Delay", description = "i wanna be slow", parent = "Scaffold", max = 5)
    public static float placemaxdelay = 1.5f;
    @Property(type = Property.Type.BOOLEAN, name = "Eagle", description = "stops you from falling of blocks", parent = "Macros")
    public static boolean eagle = false;
    @Property(type = Property.Type.SELECT, name = "Eagle Mode", description = "what stop mode", parent = "Eagle", options = {"crouch", "safewalk", "autoplace"})
    public static int eaglemode = 0;
    @Property(type = Property.Type.BOOLEAN, name = "Only Looking Down", description = "only stop if looking down", parent = "Eagle")
    public static boolean onlylookingdown = false;
    @Property(type = Property.Type.NUMBER, name = "Looking Down Min Yaw", description = "minimum yaw for if looking down", parent = "Only Looking Down", min = 0, max = 180)
    public static float lookingdownminyaw = 70;
    @Property(type = Property.Type.NUMBER, name = "Looking Down Max Yaw", description = "maximum yaw for if looking down", parent = "Only Looking Down", min = 0, max = 180)
    public static float lookingdownmaxyaw = 83;
    @Property(type = Property.Type.BOOLEAN, name = "Blocks Only", description = "only stop if holding blocks", parent = "Eagle")
    public static boolean blocksonly = false;
    @Property(type = Property.Type.BOOLEAN, name = "Ground Only", description = "only stop if on ground", parent = "Eagle")
    public static boolean groundonly = false;
    @Property(type = Property.Type.BOOLEAN, name = "Looking At Block Only", description = "only stop if looking at block", parent = "Eagle")
    public static boolean lookingatblockonly = false;
    @Property(type = Property.Type.BOOLEAN, name = "Show If Triggered", description = "shows if you currently would be safe from void even if not on edge", parent = "Eagle")
    public static boolean showifeagletriggered = false;
    @Property(type = Property.Type.BOOLEAN, name = "Toggle Fast Place", description = "toggles fast place when bridging", parent = "Eagle")
    public static boolean togglefastplace = false;
    @Property(type = Property.Type.BOOLEAN, name = "Fast Place", description = "places blocks quickly", parent = "Macros")
    public static boolean fastplace = false;
    @Property(type = Property.Type.SELECT, name = "Fast Place Mode", description = "what mode of placing blocks", parent = "Fast Place", options = {"0 delay", "autoclicker"})
    public static int fastplacemode = 0;
    @Property(type = Property.Type.NUMBER, name = "CPS Of Fast Place", description = "cps of placing blocks (average + max)", parent = "Fast Place Mode", modereq = 1, min = 1, max = 40)
    public static float cpsoffastplace = 1;
    @Property(type = Property.Type.NUMBER, name = "Delay From Start", description = "delay before autoclicker starts", parent = "Fast Place Mode", modereq = 1, max = 500)
    public static float delayfromstart = 300;

}
