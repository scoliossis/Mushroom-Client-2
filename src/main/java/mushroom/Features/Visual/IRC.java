package mushroom.Features.Visual;

import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.IRC.ShroomIrcClient;
import mushroom.Libs.events.PacketSentEvent;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

import static mushroom.Libs.PlayerLib.mc;

public class IRC {

    // SHOUTOUT TO 112BATMAN FOR MAKING THIS!!!
    //https://gist.github.com/112batman/0d7b147b42d065ce72936bd27477df38
    private final ShroomIrcClient client;

    ShroomIrcClient c;

    public IRC() {
        try {
            c = new ShroomIrcClient(Configs.ircChatName, "Mushroom Client", this::onMessage);
        } catch (Exception ex) {
            c = null;
            ex.printStackTrace();
            System.out.println("Failed to create IRC client");
        }
        client = c;
    }

    boolean ircon = true;
    String currentname = Configs.ircChatName;
    @SubscribeEvent
    public void clientTick(TickEvent.PlayerTickEvent e) {
        //if (!Configs.ircChat && ircon) client.stop();
        //if (Configs.ircChat && !ircon) client.start();
        if (ShroomIrcClient.loaded) {
            try {
                currentname = Configs.ircChatName;
                ShroomIrcClient.loaded = false;
                ChatLib.chat("§3you in the irc ;3");
                ChatLib.chat("§d"+Configs.ircChatName + " has joined the irc.");
                client.sendMessage("§d"+Configs.ircChatName + " has joined the irc.");
            } catch (Exception ex) {
                System.out.println("§cfailed to send join IRC message :(");
            }
        }

        if (mc.currentScreen == null) {

            if (!Objects.equals(Configs.ircChatName, currentname)) {
                try {
                    ChatLib.chat("changing irc name!!");
                    c = new ShroomIrcClient(Configs.ircChatName, "Mushroom Client", this::onMessage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            currentname = Configs.ircChatName;
        }


        if (Objects.equals(Configs.ircChatName, "$test")) Configs.ircChatName = mc.thePlayer.getName();
        ircon = Configs.ircChat;
    }

    private void onMessage(ShroomIrcClient.ShroomMessage msg) {
        ChatLib.chat("§9§lIRC > §d" + msg.username + "§7: " + msg.message);
    }

    @SubscribeEvent
    public void onPacketSend(PacketSentEvent e) {
        if(e.packet instanceof C01PacketChatMessage) {
            C01PacketChatMessage p = (C01PacketChatMessage) e.packet;
            if(p.getMessage().startsWith(".irc") || p.getMessage().startsWith(".i") || p.getMessage().startsWith("#i")) {
                String messageStart = p.getMessage().split(" ")[0];
                e.setCanceled(true);
                String[] parts = p.getMessage().split(messageStart + " ");
                if(parts.length > 1) {
                    try {
                        client.sendMessage(parts[1]);
                        ChatLib.chat("§9§lIRC > §d" + Configs.ircChatName + "§7: " +  parts[1]);
                    } catch (Exception ex) {
                        ChatLib.chat("§circ done brokie :( attempting to reconnect");
                        try {
                            client.stop();
                            c = new ShroomIrcClient(Configs.ircChatName, "Mushroom Client", this::onMessage);
                            client.start();
                        } catch (Exception exception) {
                            throw new RuntimeException(exception);
                        }
                    }
                }
                else {
                    ChatLib.chat("§cyo put some sustenance in yo message");
                }
            }
        }
    }
}