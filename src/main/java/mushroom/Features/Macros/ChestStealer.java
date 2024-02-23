package mushroom.Features.Macros;

import com.mojang.realmsclient.gui.ChatFormatting;
import mushroom.GUI.Configs;
import mushroom.Libs.MillisTimer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static mushroom.Libs.PlayerLib.mc;

public class ChestStealer {
    public MillisTimer timer = new MillisTimer();

    @SubscribeEvent
    public void onGui(final GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiChest && Configs.cheststealer) {
            final Container container = ((GuiChest)event.gui).inventorySlots;
            if (container instanceof ContainerChest && (!Configs.namecheck || ChatFormatting.stripFormatting(((ContainerChest)container).getLowerChestInventory().getDisplayName().getFormattedText()).equals("Chest") || ChatFormatting.stripFormatting(((ContainerChest)container).getLowerChestInventory().getDisplayName().getFormattedText()).equals("LOW"))) {
                for (int i = 0; i < ((ContainerChest)container).getLowerChestInventory().getSizeInventory(); ++i) {
                    if (container.getSlot(i).getHasStack() && timer.hasTimePassed((long) Configs.delaybetweenitems)) {
                        final Item item = container.getSlot(i).getStack().getItem();
                        if (Configs.stealtrash || item instanceof ItemEnderPearl || item instanceof ItemTool || item instanceof ItemArmor || item instanceof ItemBow || item instanceof ItemPotion || item == Items.arrow || item instanceof ItemAppleGold || item instanceof ItemSword || item instanceof ItemBlock) {
                            mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                            timer.reset();
                            return;
                        }
                    }
                }
                for (int i = 0; i < ((ContainerChest)container).getLowerChestInventory().getSizeInventory(); ++i) {
                    if (container.getSlot(i).getHasStack()) {
                        final Item item = container.getSlot(i).getStack().getItem();
                        if (Configs.stealtrash || item instanceof ItemEnderPearl || item instanceof ItemTool || item instanceof ItemArmor || item instanceof ItemBow || item instanceof ItemPotion || item == Items.arrow || item instanceof ItemAppleGold || item instanceof ItemSword || item instanceof ItemBlock) {
                            return;
                        }
                    }
                }
                if (Configs.autoclose) {
                    mc.thePlayer.closeScreen();
                }
            }
        }
    }
}
