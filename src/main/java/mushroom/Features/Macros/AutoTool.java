package mushroom.Features.Macros;

import mushroom.GUI.Configs;
import mushroom.Libs.MillisTimer;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.events.PacketSentEvent;
import mushroom.mixins.C03Accessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;

import static mushroom.Libs.PlayerLib.mc;

public class AutoTool {

    public MillisTimer delay = new MillisTimer();

    @SubscribeEvent
    public void onPacket(PacketSentEvent event) {
        if (!Configs.autotool || mc.thePlayer == null) {
            return;
        }
        if (Configs.autotoolTools && !mc.thePlayer.isUsingItem() && event.packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)event.packet).getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                final Block block = mc.theWorld.getBlockState(((C07PacketPlayerDigging)event.packet).getPosition()).getBlock();
                if (stack != null && block != null && stack.getStrVsBlock(block) > ((mc.thePlayer.inventory.getCurrentItem() == null) ? 1.0f : mc.thePlayer.inventory.getCurrentItem().getStrVsBlock(block))) {
                    mc.thePlayer.inventory.currentItem = i;
                }
            }
            PlayerLib.syncHeldItem();
        }
        else if (this.delay.hasTimePassed(50) && !mc.thePlayer.isUsingItem() && Configs.autotoolWeapon && event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.packet).getAction() == C02PacketUseEntity.Action.ATTACK) {
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                if (stack != null && getToolDamage(stack) > ((mc.thePlayer.inventory.getCurrentItem() == null) ? 0.0f : getToolDamage(mc.thePlayer.inventory.getCurrentItem()))) {
                    mc.thePlayer.inventory.currentItem = i;
                }
            }

            PlayerLib.syncHeldItem();
        }
        if ((event.packet instanceof C09PacketHeldItemChange && mc.thePlayer.inventory.getStackInSlot(((C09PacketHeldItemChange)event.packet).getSlotId()) != null) || (event.packet instanceof C08PacketPlayerBlockPlacement && ((C08PacketPlayerBlockPlacement)event.packet).getStack() != null)) {
            this.delay.reset();
        }
    }

    public static float getToolDamage(final ItemStack tool) {
        float damage = 0.0f;
        if (tool != null && (tool.getItem() instanceof ItemTool || tool.getItem() instanceof ItemSword)) {
            if (tool.getItem() instanceof ItemSword) {
                damage += 4.0f;
            }
            else if (tool.getItem() instanceof ItemAxe) {
                damage += 3.0f;
            }
            else if (tool.getItem() instanceof ItemPickaxe) {
                damage += 2.0f;
            }
            else if (tool.getItem() instanceof ItemSpade) {
                ++damage;
            }
            damage += ((tool.getItem() instanceof ItemTool) ? ((ItemTool)tool.getItem()).getToolMaterial().getDamageVsEntity() : ((ItemSword)tool.getItem()).getDamageVsEntity());
            damage += (float)(1.25 * EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, tool));
            damage += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, tool) * 0.5);
        }
        return damage;
    }
}