package mushroom.Libs;

import com.mojang.realmsclient.gui.ChatFormatting;
import mushroom.mixins.PlayerControllerAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.scoreboard.Score;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraftforge.client.event.MouseEvent;
import java.nio.ByteBuffer;

import static java.lang.Math.*;

public class PlayerLib {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static boolean inGame() {
        return mc.thePlayer != null && mc.theWorld != null;
    }

    public static boolean isOverAir() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY - 1.0D;
        double z = mc.thePlayer.posZ;
        BlockPos p = new BlockPos(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
        return mc.theWorld.isAirBlock(p);
    }
    public static boolean isOnGround(final double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    public static void setCrouched(boolean tf) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), tf);
    }

    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                    final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        final AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z), mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (boundingBox != null && mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void setMouseButtonState(int mouseButton, boolean held) {
        MouseEvent m = new MouseEvent();

        ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, m, mouseButton, "button");
        ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, m, held, "buttonstate");
        MinecraftForge.EVENT_BUS.post(m);

        ByteBuffer buttons = ObfuscationReflectionHelper.getPrivateValue(Mouse.class, null, "buttons");
        buttons.put(mouseButton, (byte)(held ? 1 : 0));
        ObfuscationReflectionHelper.setPrivateValue(Mouse.class, null, buttons, "buttons");

    }

    public static boolean isOverVoid() {
        return isOverVoid(0.0, 0.0);
    }

    public static boolean isOverVoid(final double xOffset, final double zOffset) {
        final BlockPos block = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        if (!mc.theWorld.isBlockLoaded(block)) {
            return false;
        }
        final AxisAlignedBB player = mc.thePlayer.getEntityBoundingBox().offset(xOffset, 0.0, zOffset);
        return mc.theWorld.getCollidingBoundingBoxes((Entity)mc.thePlayer, new AxisAlignedBB(player.minX, 0.0, player.minZ, player.maxX, player.maxY, player.maxZ)).isEmpty();
    }

    public static boolean lastGround;

    public static void swapToSlot(final int slot) {
        mc.thePlayer.inventory.currentItem = slot;
        syncHeldItem();
    }
    public static void syncHeldItem() {
        final int slot = mc.thePlayer.inventory.currentItem;
        if (slot != ((PlayerControllerAccessor)mc.playerController).getCurrentPlayerItem()) {
            ((PlayerControllerAccessor)mc.playerController).setCurrentPlayerItem(slot);
            PacketUtils.sendPacketNoEvent((Packet<?>)new C09PacketHeldItemChange(slot));
        }
    }

    public static Vec3 getVectorForRotation(final float yaw, final float pitch) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3((double)(f2 * f3), (double)f4, (double)(f * f3));
    }

    public static boolean isTeam(final EntityLivingBase e2) {
        if (!(e2 instanceof EntityPlayer) || e2.getDisplayName().getUnformattedText().length() < 4) {
            return false;
        }
        return mc.thePlayer.getDisplayName().getFormattedText().charAt(2) == '§' && e2.getDisplayName().getFormattedText().charAt(2) == '§' && mc.thePlayer.getDisplayName().getFormattedText().charAt(3) == e2.getDisplayName().getFormattedText().charAt(3);
    }

    public static boolean isNPC(final Entity entity) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return false;
        }
        final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
        return ChatFormatting.stripFormatting(entity.getDisplayName().getUnformattedText()).startsWith("[NPC]") || (entity.getUniqueID().version() == 2 && entityLivingBase.getHealth() == 20.0f && entityLivingBase.getMaxHealth() == 20.0f);
    }

    public static double distanceToPlayer(double x, double y, double z) {
        double dX = mc.thePlayer.posX - x;
        double dZ = mc.thePlayer.posZ - z;
        double dY = mc.thePlayer.posY - y;
        double dis = Math.sqrt((dX * dX) + (dZ * dZ));
        return Math.sqrt((dis * dis) + (dY * dY));
    }

}