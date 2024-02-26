package mushroom.Features.Visual;

import mushroom.Features.Macros.Fucker;
import mushroom.GUI.Configs;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.MathLib;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.RenderLib;
import mushroom.Libs.events.BlockChangeEvent;
import mushroom.Libs.events.ChunkLoadEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

import static mushroom.Libs.PlayerLib.mc;

public class BedESP {

    public static Block blockAtPos(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    static ArrayList<BlockPos> beds = new ArrayList<>();

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        beds.clear();
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkLoadEvent event) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos blockPos = new BlockPos(event.getChunk().xPosition * 16 + x, y, event.getChunk().zPosition * 16 + z);

                    if (blockAtPos(blockPos) instanceof BlockBed) {
                        beds.add(blockPos);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        ArrayList<BlockPos> toRemove = new ArrayList();

        for (int i = 0; i < beds.size(); i++) {
            BlockPos bed = beds.get(i);

            if (bed.getX() >= event.getChunk().xPosition * 16 && bed.getX() < event.getChunk().xPosition * 16 + 16) {
                if (bed.getZ() >= event.getChunk().zPosition * 16 && bed.getZ() < event.getChunk().zPosition * 16 + 16) {
                    toRemove.add(bed);
                }
            }
        }

        beds.removeAll(toRemove);
    }

    // hopefully not too fps taxing...
    // if it is ill add an option to not silently scan for beds if bed esp is not on
    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (event.update.getBlock() instanceof BlockBed) beds.add(event.pos);
        else if (event.old.getBlock() instanceof BlockBed) beds.remove(event.pos);
    }


    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (Configs.bedPlates) {
            if (Configs.bedESP) {
                for (int i = 0; i < beds.size(); i++) {
                    BlockPos bed = beds.get(i);
                    if (Fucker.blockMining != bed) {
                        RenderLib.renderBoundingBox(bed.getX(), bed.getY(), bed.getZ(), 1, 0.6f, new Color(119, 35, 171, 100), event.partialTicks);
                    }
                }
            }


            try {
                if (Configs.bedPlaters) {
                    // new for loop, fuck fps it looks better
                    // (if this runs before the one above it loses color because im stupid or smth and suck at opengl)
                    if (!beds.isEmpty()) {

                        for (int i = 0; i < beds.size(); i++) {
                            BlockPos bed = beds.get(i);

                            ArrayList<BlockPos> blocks = new ArrayList<>();
                            int h = 1;
                            while (!(blockAtPos(new BlockPos(bed.getX(), bed.getY() + h, bed.getZ())) instanceof BlockAir) && (h < 2 || !Configs.bedPlates1Laye)) {
                                blocks.add(new BlockPos(bed.getX(), bed.getY() + h, bed.getZ()));
                                h++;
                            }


                            if (blockAtPos(bed).isBed(mc.theWorld, bed, mc.thePlayer) && !blockAtPos(bed).isBedFoot(mc.theWorld, bed)) {

                                GlStateManager.alphaFunc(516, 0.1f);
                                final double x = (bed.getX() - mc.thePlayer.lastTickPosX - (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * event.partialTicks) + 0.5f;
                                final double y = (bed.getY() - mc.thePlayer.lastTickPosY - (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * event.partialTicks);
                                final double z = (bed.getZ() - mc.thePlayer.lastTickPosZ - (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * event.partialTicks) + 0.5f;
                                final float f = 2f;
                                final float scale = 0.016666668f * f;
                                GlStateManager.pushMatrix();
                                GlStateManager.translate((float)x + 0.0f, (float)y + 1.5f, (float)z);
                                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                                GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
                                GlStateManager.scale(-scale, -scale, scale);
                                GlStateManager.disableLighting();
                                GlStateManager.depthMask(false);
                                GlStateManager.disableDepth();
                                GlStateManager.enableBlend();
                                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                                GlStateManager.enableTexture2D();


                                if (!blocks.isEmpty()) {
                                    RenderLib.drawRoundedRect2(((-10 * blocks.size()) / 2f) - 1, -6, ((10 * blocks.size())) + 1 + blocks.size(), 12, 6, new Color(30, 30, 30, 100).getRGB(), false);

                                    GlStateManager.translate(5, -5.5, 0);
                                    GlStateManager.translate(((-10 * blocks.size()) / 2f) - 6, 0, 0);
                                    GlStateManager.scale(0.7, 0.7, 0);

                                    for (int n = 0; n < blocks.size(); n++) {
                                        ItemStack block = new ItemStack(blockAtPos(blocks.get(n)));
                                        mc.getRenderItem().renderItemAndEffectIntoGUI(block, 0, 0);
                                        // 15.7 isnt a random number, its a long story behind it tho.
                                        GlStateManager.translate(15.7f, 0, 0);
                                    }


                                }

                                GlStateManager.enableTexture2D();
                                GlStateManager.enableDepth();
                                GlStateManager.depthMask(true);
                                GlStateManager.enableLighting();
                                GlStateManager.disableBlend();
                                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                GlStateManager.popMatrix();
                            }
                        }
                    }
                }
            } catch (Exception e){
                // nah this errors all the time fuck this
            }

        }
    }
}
