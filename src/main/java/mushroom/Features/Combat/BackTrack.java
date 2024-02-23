package mushroom.Features.Combat;

import mushroom.GUI.Configs;
import mushroom.Libs.*;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.events.PacketSentEvent;
import mushroom.mixins.PlayerSPAccessor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static mushroom.Libs.PlayerLib.isNPC;
import static mushroom.Libs.PlayerLib.mc;

public class BackTrack {

    boolean locked = false;
    static ArrayList<Double> positionarray = new ArrayList<Double>();
    public static EntityLivingBase entitytoattack;
    boolean swing = false;
    float closest = 10000;

    @SubscribeEvent
    public void BackTrack(PacketSentEvent event) {
        if (PlayerLib.inGame() && mc.currentScreen == null && Configs.backtrack) {
            try {
                if (positionarray.size() > Configs.backtracktime*3) {
                    positionarray.remove(0);
                    positionarray.remove(0);
                    positionarray.remove(0);
                }
                if (locked) {
                    positionarray.add(entitytoattack.posX);
                    positionarray.add(entitytoattack.posY);
                    positionarray.add(entitytoattack.posZ);
                }
                if (Mouse.isButtonDown(0) && !swing) {
                    swing=true;

                    for (int i = 0; i < positionarray.size(); i+=3) {
                        if (entitytoattack.getDistanceToEntity(mc.thePlayer) > PlayerLib.distanceToPlayer(positionarray.get(0), positionarray.get(1), positionarray.get(2))) {
                            if (entitytoattack.getDistanceToEntity(mc.thePlayer) < (float) Configs.backtrackreach) {
                                if (RotationUtils.getYawDiff(RotationUtils.getRotations(positionarray.get(0), positionarray.get(1) + 1.75, positionarray.get(2)), new Rotations(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)) < 20 && RotationUtils.getYawDiff(RotationUtils.getRotations(positionarray.get(0), positionarray.get(1) + 1.75, positionarray.get(2)), new Rotations(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)) > -15 && RotationUtils.getPitchDiff(RotationUtils.getRotations(positionarray.get(0), positionarray.get(1) + 1.75, positionarray.get(2)), new Rotations(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)) < 10 && RotationUtils.getPitchDiff(RotationUtils.getRotations(positionarray.get(0), positionarray.get(1) + 1.75, positionarray.get(2)), new Rotations(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)) > -50) {
                                    mc.thePlayer.swingItem();
                                    mc.playerController.attackEntity(mc.thePlayer, entitytoattack);
                                    return;
                                }
                            }
                        }

                    }
                }

                final List<Entity> validTargets = mc.theWorld.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityPlayer).sorted(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer))).collect(Collectors.toList());

                for (int i = 0; i < validTargets.size(); i++) {
                    float distancetobad=validTargets.get(i).getDistanceToEntity(mc.thePlayer);
                    if (distancetobad > 0.2 && distancetobad < (float) Configs.backtrackreach && !validTargets.get(i).isDead && !validTargets.get(i).noClip) {
                        if (distancetobad < closest) {
                            closest=distancetobad;
                            entitytoattack= (EntityLivingBase) validTargets.get(i);
                        }
                    }
                    if (i == validTargets.size()-1) {
                        if (entitytoattack!=null) {
                            locked=true;
                        }
                        else {closest=10000;locked=false;positionarray.clear(); return;}
                    }
                }
                if (entitytoattack.getDistanceToEntity(mc.thePlayer) > 9 || entitytoattack.isDead || entitytoattack.noClip) {locked=false;closest = 10000; entitytoattack=null; positionarray.clear(); return;}
            }
            catch(Exception p) {
                locked=false;
            }
            if (!Mouse.isButtonDown(0)) {
                swing = false;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Configs.backtrack && locked && entitytoattack.getDistanceToEntity(mc.thePlayer) < (float) Configs.backtrackreach) {

            if (positionarray.size() >= 2) {
                if (entitytoattack.getDistanceToEntity(mc.thePlayer) > PlayerLib.distanceToPlayer(positionarray.get(0), positionarray.get(1), positionarray.get(2))) {

                    RenderLib.renderBoundingBox(positionarray.get(0) - 0.25, positionarray.get(1), positionarray.get(2) - 0.25, 0.7, 2d, new Color(174, 120, 203, 100), event.partialTicks);
                }
            }
        }
    }
}
