package mushroom.GUI;

import mushroom.GUI.Property;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class Setting {

    public Setting parent = null;

    public Property annotation;


    public Field field;

    public ArrayList sons = new ArrayList();

    public String name;
    public String description;
    public int modereq;
    public int keybindint;
    public String keybindchar;

    public Setting(Property var1, Field var2) {
        this.annotation = var1;
        this.field = var2;
        this.name = var1.name();
        this.description = var1.description();
        this.modereq = var1.modereq();
        this.keybindint = var1.keybindint();
        this.keybindchar = var1.keybindchar();
    }

    public int hashCode() {
        return this.name.hashCode();
    }


    public boolean equals(Object var1) {
        return var1 instanceof Setting && ((Setting) var1).name.equals(this.name);
    }


    public boolean set(Object var1) {
        try {
            this.field.set(var1.getClass(), var1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList getSons() {
        ArrayList Sons = new ArrayList();
        Iterator sonsIterator = this.sons.iterator();

        Setting set;
        while (sonsIterator.hasNext()) {
            set = (Setting) sonsIterator.next();
            if (set.sons.size() == 0) {
                Sons.add(set);
            }
        }

        sonsIterator = this.sons.iterator();

        while (sonsIterator.hasNext()) {
            set = (Setting) sonsIterator.next();
            if (set.sons.size() != 0) {
                Sons.add(set);
                Sons.addAll(set.getSons());
            }
        }

        return Sons;
    }

    public Object get(Class Class) {
        try {
            return Class.cast(this.field.get(Object.class));
        } catch (Exception var3) {
            return null;
        }
    }
}
