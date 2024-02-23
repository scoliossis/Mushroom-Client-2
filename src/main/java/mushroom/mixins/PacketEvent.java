package mushroom.mixins;

import mushroom.Libs.PacketUtils;
import mushroom.Libs.events.PacketReceivedEvent;
import mushroom.Libs.events.PacketSentEvent;
import mushroom.Libs.events.WorldJoinEvent;
import net.minecraft.network.play.server.S01PacketJoinGame;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import net.minecraft.util.Vec3;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetworkManager.class })
public abstract class PacketEvent {

    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onSendPacket(final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (!PacketUtils.noEvent.contains(packet) && MinecraftForge.EVENT_BUS.post(new PacketSentEvent(packet))) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("RETURN") }, cancellable = true)
    private void onSendPacketPost(final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (!PacketUtils.noEvent.contains(packet) && MinecraftForge.EVENT_BUS.post(new PacketSentEvent.Post(packet))) {
            callbackInfo.cancel();
        }
        PacketUtils.noEvent.remove(packet);
    }

    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    private void onChannelReadHead(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (packet instanceof S01PacketJoinGame) {
            MinecraftForge.EVENT_BUS.post(new WorldJoinEvent());
        }
        if (!PacketUtils.noEvent.contains(packet) && MinecraftForge.EVENT_BUS.post(new PacketReceivedEvent(packet, context))) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = { "channelRead0" }, at = { @At("RETURN") }, cancellable = true)
    private void onPost(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (!PacketUtils.noEvent.contains(packet) && MinecraftForge.EVENT_BUS.post(new PacketReceivedEvent.Post(packet, context))) {
            callbackInfo.cancel();
        }
        PacketUtils.noEvent.remove(packet);
    }
}