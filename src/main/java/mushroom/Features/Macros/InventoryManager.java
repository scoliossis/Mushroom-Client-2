package mushroom.Features.Macros;


import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.MillisTimer;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.events.MotionUpdateEvent;
import mushroom.Libs.events.PacketSentEvent;
import mushroom.mixins.ToolClass;
import net.minecraft.item.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Slot;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.stream.Collectors;
import java.util.Comparator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.inventory.GuiInventory;
import java.util.Arrays;
import java.util.List;

import static mushroom.Libs.PlayerLib.mc;

public class InventoryManager {
    private MillisTimer delayTimer = new MillisTimer();
    public int swordSlot = 3;
    public int blockSlot = 1;
    public int pickaxeSlot = 6;
    public int axeSlot = 5;
    public int shovelSlot = 7;
    public int bowSlot = 4;
    public int gappleSlot = 2;
    public int pearlSlot = 8;

    private List<String> dropSkywars = Arrays.asList("Egg", "Snowball", "Poison", "Lava", "Steak", "Enchanting", "Poison", "Dirt", "Frog", "Stick");;


    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if ((Configs.invmanager && mc.currentScreen instanceof GuiInventory) || (mc.currentScreen == null && Configs.invmanagermode == 1)) {
            getBestArmor();
            dropTrash();
            getBestSword();
            getBestTools();
            getBestBlock();
            getBestBow();
            getGapples();
            getPearls();
        }
        else {
            delayTimer.reset();
        }
    }

    @SubscribeEvent
    public void onPacket(final PacketSentEvent event) {
        if (event.packet instanceof C02PacketUseEntity || event.packet instanceof C08PacketPlayerBlockPlacement) {
            delayTimer.reset();
        }
    }

    public void dropTrash() {
        for (final Slot slot : mc.thePlayer.inventoryContainer.inventorySlots) {
            if (slot.getHasStack() && this.canInteract()) {
                if (!this.dropSkywars.stream().anyMatch(a -> a.contains(ChatFormatting.stripFormatting(slot.getStack().getDisplayName())))) {
                    continue;
                }
                this.drop(slot.slotNumber);
            }
        }
    }

    public void getBestArmor() {
        for (int i = 5; i < 9; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && canInteract()) {
                final ItemStack armor = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (!isBestArmor(armor, i)) {
                    this.drop(i);
                }
            }
        }
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && canInteract()) {
                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack.getItem() instanceof ItemArmor) {
                    if (isBestArmor(stack, i)) {
                        this.shiftClick(i);
                    }
                    else {
                        this.drop(i);
                    }
                }
            }
        }
    }

    public static boolean isBestArmor(final ItemStack armor, final int slot) {
        if (!(armor.getItem() instanceof ItemArmor)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemArmor && ((getProtection(is) > getProtection(armor) && slot < 9) || (slot >= 9 && getProtection(is) >= getProtection(armor) && slot != i)) && ((ItemArmor)is.getItem()).armorType == ((ItemArmor)armor.getItem()).armorType) {
                    return false;
                }
            }
        }
        return true;
    }

    public static float getProtection(final ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            final ItemArmor armor = (ItemArmor)stack.getItem();
            prot += (float)(armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0);
            prot += (float)(stack.getMaxDamage() / 1000.0);
        }
        return prot;
    }

    public void getBestSword() {
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && this.canInteract()) {
                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack.getItem() instanceof ItemSword) {
                    if ((stack.getItemDamage() > stack.getMaxDamage()/4)) {
                        this.drop(i);
                    }
                    else if (this.isBestSword(stack, i)) {
                        if (this.getHotbarID(swordSlot) != i) {
                            this.numberClick(i, swordSlot - 1);
                        }
                    }
                    else {
                        this.drop(i);
                    }
                }
            }
        }
    }

    public boolean isBestSword(final ItemStack sword, final int slot) {
        if (!(sword.getItem() instanceof ItemSword)) {
            return false;
        }
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword && ((getToolDamage(is) > getToolDamage(sword) && slot == this.getHotbarID(swordSlot)) || (slot != this.getHotbarID(swordSlot) && getToolDamage(is) >= getToolDamage(sword) && slot != i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void getBestTools() {
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && canInteract()) {
                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack.getItem() instanceof ItemTool && this.getToolHotbarSlot(stack) != 0) {
                    if (this.isBestTool(stack, i)) {
                        if (this.getHotbarID(this.getToolHotbarSlot(stack)) != i) {
                            this.numberClick(i, this.getToolHotbarSlot(stack) - 1);
                        }
                    }
                    else {
                        this.drop(i);
                    }
                }
            }
        }
    }

    public void getBestBow() {
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && canInteract()) {
                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack.getItem() instanceof ItemBow) {
                    if (!mc.thePlayer.inventoryContainer.getSlot(bowSlot-1).getHasStack() || getBowDamage(stack) > getBowDamage(mc.thePlayer.inventoryContainer.getSlot(bowSlot-1).getStack())) {
                        if (this.getHotbarID(bowSlot) != i) {
                            this.numberClick(i, bowSlot - 1);
                        }
                    }
                    else {
                        this.drop(i);
                    }
                }
            }
        }
    }

    public void getGapples() {
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && canInteract()) {
                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack.getItem() instanceof ItemAppleGold) {
                    //if ((mc.thePlayer.inventoryContainer.getSlot(gappleSlot-1).getHasStack() && (stack.stackSize > mc.thePlayer.inventoryContainer.getSlot(gappleSlot-1).getStack().stackSize) || !(mc.thePlayer.inventoryContainer.getSlot(gappleSlot-1).getStack().getItem() instanceof ItemAppleGold)) || !mc.thePlayer.inventoryContainer.getSlot(gappleSlot-1).getHasStack()) {
                        if (this.getHotbarID(gappleSlot) != i) {
                            this.numberClick(i, gappleSlot - 1);
                        }
                    //}
                    //else {
                        // who wants stacks of gapples. (me)
                    //    this.drop(i);
                    //}
                }
            }
        }
    }
    public void getPearls() {
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && canInteract()) {
                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack.getItem() instanceof ItemEnderPearl) {
                    //if ((mc.thePlayer.inventoryContainer.getSlot(pearlSlot-1).getHasStack() && (stack.stackSize > mc.thePlayer.inventoryContainer.getSlot(pearlSlot-1).getStack().stackSize) || !(mc.thePlayer.inventoryContainer.getSlot(pearlSlot-1).getStack().getItem() instanceof ItemEnderPearl)) || !mc.thePlayer.inventoryContainer.getSlot(pearlSlot-1).getHasStack()) {
                        if (this.getHotbarID(pearlSlot) != i) {
                            this.numberClick(i, pearlSlot - 1);
                        }
                    //}
                    //else {
                        // who wants stacks of ender pearls.
                    //    this.drop(i);
                    //}
                }
            }
        }
    }

    public boolean isBestTool(final ItemStack tool, final int slot) {
        if (!(tool.getItem() instanceof ItemTool)) {
            return false;
        }
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (this.getToolHotbarSlot(is) != 0) {
                    if (tool.getItem() instanceof ItemAxe && is.getItem() instanceof ItemAxe) {
                        if ((getMaterial(is) > getMaterial(tool) && slot == this.getHotbarID(axeSlot)) || (slot != this.getHotbarID(axeSlot) && getToolDamage(is) >= getToolDamage(tool) && slot != i)) {
                            return false;
                        }
                    }
                    else if (tool.getItem() instanceof ItemPickaxe && is.getItem() instanceof ItemPickaxe) {
                        if ((getMaterial(is) > getMaterial(tool) && slot == this.getHotbarID(pickaxeSlot)) || (slot != this.getHotbarID(pickaxeSlot) && getToolDamage(is) >= getToolDamage(tool) && slot != i)) {
                            return false;
                        }
                    }
                    else if (tool.getItem() instanceof ItemSpade && is.getItem() instanceof ItemSpade && ((getMaterial(is) > getMaterial(tool) && slot == this.getHotbarID(shovelSlot)) || (slot != this.getHotbarID(pickaxeSlot) && getToolDamage(is) >= getToolDamage(tool) && slot != i))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getToolHotbarSlot(final ItemStack tool) {
        if (tool == null || !(tool.getItem() instanceof ItemTool)) {
            return 0;
        }
        final String toolClass = ((ToolClass)tool.getItem()).getToolClass();
        switch (toolClass) {
            case "pickaxe": {
                return pickaxeSlot;
            }
            case "axe": {
                return axeSlot;
            }
            case "shovel": {
                return shovelSlot;
            }
            default: {
                return 0;
            }
        }
    }

    public static float getMaterial(final ItemStack item) {
        if (item.getItem() instanceof ItemTool) {
            return (float)(((ItemTool)item.getItem()).getToolMaterial().getHarvestLevel() + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, item) * 0.75);
        }
        return 0.0f;
    }

    public void getBestBlock() {
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && this.canInteract()) {
                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).block.isFullBlock() && this.isBestBlock(stack, i) && this.getHotbarID(blockSlot) != i) {
                    this.numberClick(i, blockSlot - 1);
                }
            }
        }
    }

    public boolean isBestBlock(final ItemStack stack, final int slot) {
        if (!(stack.getItem() instanceof ItemBlock) || !((ItemBlock)stack.getItem()).block.isFullBlock()) {
            return false;
        }
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemBlock && ((ItemBlock)is.getItem()).block.isFullBlock() && is.stackSize >= stack.stackSize && slot != this.getHotbarID(blockSlot) && slot != i) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getHotbarID(final int hotbarNumber) {
        return hotbarNumber + 35;
    }

    public static int getBowDamage(final ItemStack bow) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, bow) + EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, bow) * 2;
    }

    public static float getToolDamage(final ItemStack tool) {
        float damage = 0.0f;
        if (tool != null && (tool.getItem() instanceof ItemTool || tool.getItem() instanceof ItemSword)) {
            if (tool.getItem() instanceof ItemSword) {
                damage += 4.0f;
                ++damage;
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

    private boolean canInteract() {
        return this.delayTimer.hasTimePassed((long) Configs.invmanagerdelay);
    }

    public void shiftClick(final int slot) {
        delayTimer.reset();
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
    }

    public void numberClick(final int slot, final int button) {
        delayTimer.reset();
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, button, 2, mc.thePlayer);
    }

    public void drop(final int slot) {
        delayTimer.reset();
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
    }

}