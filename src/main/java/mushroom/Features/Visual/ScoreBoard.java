package mushroom.Features.Visual;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import mushroom.GUI.Configs;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.RenderLib;
import mushroom.Libs.events.ScoreboardRenderEvent;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class ScoreBoard {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDraw(final ScoreboardRenderEvent event) {
        if (!Configs.Scoreboard) {
            return;
        }
        event.setCanceled(true);
        this.renderScoreboard(event.objective, event.resolution);
    }

    private void renderScoreboard(ScoreObjective objective, ScaledResolution p_180475_2_) {
        final Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        final List<Score> list = collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList());
        if (list.size() > 15) {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        }
        else {
            collection = list;
        }
        float width = (float) FontUtil.productsans19.getStringWidth(objective.getDisplayName());
        final int fontHeight = (FontUtil.productsans19.getHeight() + 2);
        for (final Score score : collection) {
            final ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            final String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            width = (float) Math.max(width, FontUtil.productsans19.getStringWidth(s));
        }
        final float i1 = (float)(collection.size() * fontHeight);
        float j1 = p_180475_2_.getScaledHeight() / 2.0f + i1 / 3.0f;

        final float k1 = 3.0f;
        final float l1 = p_180475_2_.getScaledWidth() - width - k1;
        final float m = p_180475_2_.getScaledWidth() - k1 + 2.0f;

        RenderLib.drawRoundedRect2(l1 - 2.0f, j1 - collection.size() * fontHeight - fontHeight - 3.0f, m - (l1 - 2.0f), fontHeight * (collection.size() + 1) + 4, 10f, new Color(21, 21, 21, 120).getRGB(), false);
        RenderLib.drawGradientOutlinedRoundedRect(l1 - 2.0f, j1 - collection.size() * fontHeight - fontHeight - 3.0f, m - (l1 - 2.0f), (float)(fontHeight * (collection.size() + 1) + 4), 10f, 2.0f, new Color(175, 137, 201).getRGB(), new Color(113, 46, 157).getRGB(),  new Color(41, 57, 189).getRGB(), new Color(31, 12, 166).getRGB());

        int i3 = 0;
        for (final Score score2 : collection) {
            i3++;
            final ScorePlayerTeam scoreplayerteam2 = scoreboard.getPlayersTeam(score2.getPlayerName());
            String s2 = ScorePlayerTeam.formatPlayerName(scoreplayerteam2, score2.getPlayerName());
            if (s2.contains("§ewww.hypixel.ne\ud83c\udf82§et")) {
                s2 = s2.replaceAll("§ewww.hypixel.ne\ud83c\udf82§et", "mushroom");
            }
            final float k2 = j1 - i3 * fontHeight;
            final Matcher matcher = Pattern.compile("[0-9][0-9]/[0-9][0-9]/[0-9][0-9]").matcher(s2);
            if (Configs.hideLobby && matcher.find()) {
                s2 = ChatFormatting.GRAY + matcher.group();
            }
            if (s2.equals("mushroom")) FontUtil.productsans19.drawCenteredStringWithShadow(s2, l1 + width / 2.0f, k2, new Color(171, 120, 206).getRGB());
            else FontUtil.productsans19.drawString(s2, l1, k2, 553648127);
            if (i3 == collection.size()) {
                final String s3 = objective.getDisplayName();
                FontUtil.productsans19.drawString(s3, l1 + width / 2.0f - FontUtil.productsans19.getStringWidth(s3) / 2.0f, k2 - fontHeight, Color.white.getRGB());
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }


    //boolean dragging = false;
    //@SubscribeEvent
    //public void onChatEvent(final GuiChatEvent event) {
    //    if (!Configs.Scoreboard) {
    //        return;
    //    }
    //
    //    if (event instanceof GuiChatEvent.MouseClicked) {
    //        if (event.mouseX >= scoreBoardX && event.mouseX <= scoreBoardX + 200 && event.mouseY >= scoreBoardY && event.mouseY <= scoreBoardY + 200) {
    //            dragging = true;
    //            scoreBoardX = event.mouseX - scoreBoardX;
    //            scoreBoardY = event.mouseY - scoreBoardY;
    //        }
    //    }
    //
    //    else if (event instanceof GuiChatEvent.Closed) {
    //        dragging = false;
    //    }
    //    else if (event instanceof GuiChatEvent.MouseReleased) {
    //        dragging = false;
    //    }
    //}

}
