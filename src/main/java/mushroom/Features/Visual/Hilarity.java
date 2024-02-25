package mushroom.Features.Visual;

import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class Hilarity {

    int i;
    String[] Hilaritynames = {"&d[DUMBASS] pewing", "&b[SWOG] swig", "&c[ADMIN] Jayavarmen", "&c[ADMIN] Minikloon", "&c[ADMIN] Ladybleu", "&c[OWNER] hypixel", "&c[OWNER] Rezzus", "&c[ADMIN] Plancke", "&c[ADMIN] aPunch", "&b[MVP&0+&b] Dctr", "&c[ADMIN] Thorlon", "&c[ADMIN] Froz3n", "&c[ADMIN] ChiLynn", "&c[Minister] 2nfg", "&b[MVP&0+&b] Strongchaff", "&b[MVP&0+&b] Guait", "&b[MVP&0+&b] A0TD", "&b[MVP&0+&b] Blaze", "&b[MVP&0+&b] Slime", "&d[queer] Lxnze", "&c[&fYOUTUBE&c] Mushroom", "&c[&fYOUTUBE&c] Frozie", "&c[&fYOUTUBE&c] Refraction", "&c[&fYOUTUBE&c] K9L", "&c[&fYOUTUBE&c] 56ms", "&c[&fYOUTUBE&c] AverageSweat", "&c[&fYOUTUBE&c] Vegantoes", "&c[&fYOUTUBE&c] __Zova", "&c[&dGAMER&c] iLoveRat", "&2[&fMUSHROOM&2] Scale", "&b[rise] Alan Wood", "&e[skid] kulovhax", "&d[0 fps] ijustcheat", "&7[ratter] oslurpie", "&d[nice ass] nwp", "&b[&5X&8eon&b] &cOctoSplash", "&c[money cat] riel"};
    String[] Hilaritymessages = {"your gay\n&dFrom &b[MVP&2+&b] Elg_AF&7: you're*", "Your flagging.\n&dFrom &b[MVP&2+&b] Elg_AF&7: you're*", "watchdog exists", "your getting banned :)\n&dFrom &b[MVP&2+&b] Elg_AF&7: you're*", "any last words?", "x3 nuzzles! pounces on you uwu you so warm (ooo) Couldn’t help but notice your buldge from across the floor Nuzzles yo' necky wecky-tilda murr-tilda hehe Unzips yo baggy ass pants, oof baby you so musky Take me home, pet me, 'n’ make me yours and don't forget to stuff me! See me wag my widdle baby tail all for your buldgy-wuldgy! Kissies 'n' lickies yo neck I hope daddy likies Nuzzles 'n' wuzzles yo chest (yuh) I be (yeah) gettin' thirsty Hey, I got a little itch, you think you can help me? Only seven inches long uwu PLEASE ADOPT ME Paws on your buldge as I lick my lips (uwu punish me please) 'Boutta hit ’em with this furry shit (he don’t see it comin'). Dowoes youwu lick my sowong.", "you really thought that wasnt detectable?", "hey whats mushroom client? ;)", "du kannst nichts daher ist dein vatter milch hollen", "+44 7523 305452 call me ;3", "t.me/escamas1337 !!", "lesbiananimegirl on discord", "https://github.com/scoliossis/MushroomClient2"};


    @SubscribeEvent
    public void keypressed(InputEvent.KeyInputEvent event) {
        if (Configs.hilarity) {
            i++;
            if (i >= Configs.hilaritytimer) {
                ChatLib.chat("&dFrom " + Hilaritynames[(int) Math.floor(Math.random() * Hilaritynames.length)] + "&7: " + Hilaritymessages[(int) Math.floor(Math.random() * Hilaritymessages.length)]);
                i=0;
            }
        }
    }
}
