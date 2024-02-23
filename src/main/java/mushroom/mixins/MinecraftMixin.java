package mushroom.mixins;

import mushroom.Features.Combat.Killaura;
import mushroom.Features.Macros.Fucker;
import mushroom.GUI.ClickGUI;
import mushroom.GUI.Configs;
import mushroom.Libs.events.KeyPressEvent;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import mushroom.mixins.PlayerSPAccessor;

import java.awt.*;

import static mushroom.Libs.PlayerLib.mc;

@Mixin({ Minecraft.class })
public abstract class MinecraftMixin {
    @Shadow
    private Entity renderViewEntity;

    @Inject(method = { "getRenderViewEntity" }, at = { @At("HEAD") })
    public void getRenderViewEntity(final CallbackInfoReturnable<Entity> cir) {
        if (this.renderViewEntity == null || this.renderViewEntity != mc.thePlayer) {
            return;
        }
        if (Killaura.target != null || Configs.scaffold || Fucker.blockMining != null) {
            ((EntityLivingBase)this.renderViewEntity).rotationYawHead = ((PlayerSPAccessor)this.renderViewEntity).getLastReportedYaw();
            ((EntityLivingBase)this.renderViewEntity).renderYawOffset = ((PlayerSPAccessor)this.renderViewEntity).getLastReportedYaw();
        }
    }

    @Inject(method = { "runTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V") })
    public void keyPresses(final CallbackInfo ci) {
        final int k = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey();
        final char aChar = Keyboard.getEventCharacter();
        if (Keyboard.getEventKeyState()) {
            if (MinecraftForge.EVENT_BUS.post(new KeyPressEvent(k, aChar))) {
                return;
            }
            if (mc.currentScreen == null) {
                ClickGUI.handleKeypress(k);
            }
        }
    }


    @Shadow
    public GuiScreen currentScreen;
    @Shadow
    public GameSettings gameSettings;
    @Shadow
    public boolean inGameHasFocus;

    @Shadow
    public MovingObjectPosition objectMouseOver;


    @Shadow
    public EntityPlayerSP thePlayer;
    @Shadow
    public PlayerControllerMP playerController;
    @Shadow
    public WorldClient theWorld;


    @Inject(method = { "sendClickBlockToController" }, at = { @At("RETURN") })
    public void sendClickBlock(final CallbackInfo callbackInfo) {
        final boolean click = this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus;
        if (Configs.fastbreak && click && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            for (int i = 0; i < (int) Configs.maxBlocks; ++i) {
                final BlockPos prevBlockPos = this.objectMouseOver.getBlockPos();
                this.objectMouseOver = this.renderViewEntity.rayTrace((double)this.playerController.getBlockReachDistance(), 1.0f);
                final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                if (this.objectMouseOver == null || blockpos == null || this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || blockpos == prevBlockPos || this.theWorld.getBlockState(blockpos).getBlock().getMaterial() == Material.air) {
                    break;
                }
                this.thePlayer.swingItem();
                this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
            }
        }
    }
}