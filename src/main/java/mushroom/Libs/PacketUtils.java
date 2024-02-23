package mushroom.Libs;

import java.lang.reflect.Field;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.Packet;
import java.util.ArrayList;

import static mushroom.Libs.PlayerLib.mc;

public class PacketUtils
{
    public static ArrayList<Packet<?>> noEvent;

    public static void sendPacketNoEvent(final Packet<?> packet) {
        PacketUtils.noEvent.add(packet);
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public static C03PacketPlayer.C06PacketPlayerPosLook getResponse(final S08PacketPlayerPosLook packet) {
        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();
        float yaw = packet.getYaw();
        float pitch = packet.getPitch();
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) {
            x += mc.thePlayer.posX;
        }
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
            y += mc.thePlayer.posY;
        }
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
            z += mc.thePlayer.posZ;
        }
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) {
            pitch += mc.thePlayer.rotationPitch;
        }
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) {
            yaw += mc.thePlayer.rotationYaw;
        }
        return new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, yaw % 360.0f, pitch % 360.0f, false);
    }

    public static String packetToString(final Packet<?> packet) {
        final StringBuilder postfix = new StringBuilder();
        boolean first = true;
        for (final Field field : packet.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                postfix.append(first ? "" : ", ").append(field.get(packet));
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            first = false;
        }
        return packet.getClass().getSimpleName() + String.format("{%s}", postfix);
    }

    static {
        PacketUtils.noEvent = new ArrayList<Packet<?>>();
    }
}
