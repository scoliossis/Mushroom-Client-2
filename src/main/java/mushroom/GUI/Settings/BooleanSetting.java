package mushroom.GUI.Settings;

import mushroom.GUI.Property;
import mushroom.GUI.Setting;

import java.lang.reflect.Field;
import java.util.Iterator;

public class BooleanSetting extends Setting {

    public Property.Type type;

    public BooleanSetting(Property var1, Field var2, Property.Type var3) {
        super(var1, var2);
        this.type = var3;
    }

    public boolean set(Object bool) {
        try {
            return super.set(bool);
        } catch (Exception e) {
            System.out.println("Failed to set " + this.name + " to " + bool);
            e.printStackTrace();
            return false;
        }
    }
}
