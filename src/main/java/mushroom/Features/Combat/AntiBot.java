package mushroom.Features.Combat;

import mushroom.GUI.Configs;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.events.WorldJoinEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class AntiBot {
    private static final HashMap<Integer, EntityData> entityData = new HashMap<Integer, EntityData>();

    public static boolean isValidEntity(final Entity entity) {
        if (Configs.antibot && entity instanceof EntityPlayer && entity != Minecraft.getMinecraft().thePlayer) {
            final EntityData data = AntiBot.entityData.get(entity.getEntityId());
            if (data != null) {
                return (!Configs.tabCheck || data.getTabTicks() >= 150) && (!Configs.invisCheck || data.getTicksExisted() - data.getTicksInvisible() >= 150) && (!Configs.NPCcheck || !PlayerLib.isNPC(entity));
            }
        }
        return true;
    }

    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        final EntityData data = AntiBot.entityData.get(event.entity.getEntityId());
        if (data == null) {
            AntiBot.entityData.put(event.entity.getEntityId(), new EntityData(event.entity));
        }
        else {
            AntiBot.entityData.get(event.entity.getEntityId()).update();
        }
    }


    @SubscribeEvent
    public void onWorldJOin(final WorldJoinEvent event) {
        AntiBot.entityData.clear();
    }


    private static class EntityData {
        private int ticksInvisible;
        private int tabTicks;
        private final Entity entity;

        public EntityData(final Entity entity) {
            this.entity = entity;
            this.update();
        }

        public int getTabTicks() {
            return this.tabTicks;
        }

        public int getTicksInvisible() {
            return this.ticksInvisible;
        }

        public int getTicksExisted() {
            return this.entity.ticksExisted;
        }

        public void update() {
            if (this.entity instanceof EntityPlayer && Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.entity.getUniqueID()) != null) {
                this.tabTicks++;
            }
            if (this.entity.isInvisible()) {
                this.ticksInvisible++;
            }
        }
    }
}
