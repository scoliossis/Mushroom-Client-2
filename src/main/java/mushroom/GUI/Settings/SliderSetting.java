package mushroom.GUI.Settings;

import mushroom.GUI.Property;
import mushroom.GUI.Setting;

import net.minecraft.util.MathHelper;

import java.lang.reflect.Field;

public class SliderSetting extends Setting {

    public float min;

    public float max;

    public SliderSetting(Property var1, Field var2) {
        super(var1, var2);
        this.min = var1.min();
        this.max = var1.max();
    }

    public boolean set(Object var1) {
        return super.set(MathHelper.clamp_float((Float) var1, this.min, this.max));
    }

    public float compareTo(Float var1) {
        try {
            return Float.compare((float) this.get(Float.TYPE), var1);
        } catch (Exception var3) {
            return 0;
        }
    }
}
