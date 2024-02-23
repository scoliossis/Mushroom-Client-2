package mushroom.GUI.Settings;

import mushroom.GUI.Property;
import mushroom.GUI.Setting;

import java.lang.reflect.Field;

public class ColorSetting extends Setting {

    public ColorSetting(Property settingType, Field settingNow) {
        super(settingType, settingNow);
    }

    public boolean set(Object ints) {
        return super.set(ints);
    }
}
