package mushroom.mixins;

import com.google.common.collect.Maps;
import mushroom.Features.Combat.Killaura;
import mushroom.GUI.Configs;
import mushroom.Libs.MovementLib;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

import static mushroom.Libs.PlayerLib.mc;


@Mixin({ EntityLivingBase.class })
public class EntityLivingBaseMixin extends EntityMixin {

    @Overwrite
    private int getArmSwingAnimationEnd() {
        // ignore haste
        return 1;
    }
    @Shadow
    public boolean isSwingInProgress;

    public float swingProg;
    @Shadow
    public float swingProgress;


    @Overwrite
    protected void updateArmSwingProgress() {
        float i = this.getArmSwingAnimationEnd();
        if (this.isSwingInProgress) {
            this.swingProg+=Configs.animationsSpeed;
            if (this.swingProg >= i) {
                this.swingProg = 0;
                this.isSwingInProgress = false;
            }
        } else {
            this.swingProg = 0;
        }

        this.swingProgress = this.swingProg / i;
    }

    @Shadow
    protected float getJumpUpwardsMotion() {
        return 0.42F;
    }

    @Shadow
    private final Map<Integer, PotionEffect> activePotionsMap = Maps.newHashMap();

    @Shadow
    public boolean isPotionActive(Potion p_isPotionActive_1_) {
        return this.activePotionsMap.containsKey(p_isPotionActive_1_.id);
    }

    @Shadow
    public PotionEffect getActivePotionEffect(Potion p_getActivePotionEffect_1_) {
        return this.activePotionsMap.get(p_getActivePotionEffect_1_.id);
    }


    @Shadow
    public float moveForward;

    // fixes moving forward when jumping with omnisprint
    @Overwrite
    protected void jump() {
        this.motionY = this.getJumpUpwardsMotion();
        if (this.isPotionActive(Potion.jump)) {
            this.motionY += ((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
        }

        if (this.isSprinting()) {
            float f = MovementLib.getYaw() * 0.017453292F;
            if (!Minecraft.getMinecraft().thePlayer.isUsingItem()) {
                mc.thePlayer.motionX -= MathHelper.sin(f) * 0.2f;
                mc.thePlayer.motionZ += MathHelper.cos(f) * 0.2f;
            }
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(Minecraft.getMinecraft().thePlayer);
    }

}