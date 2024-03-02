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
    // backtrack was here at some point
    // (i didnt know what backtrack did or how it worked apparently...)
}
