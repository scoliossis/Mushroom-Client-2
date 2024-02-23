package mushroom.GUI;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    String parent() default "";

    String description() default "";

    String[] options() default {};

    String name();

    Type type();

    float max() default Float.MAX_VALUE;

    float min() default 0f;

    int modereq() default 60;
    int keybindint() default 999;

    String keybindchar() default " ";

    enum Type {

        BOOLEAN,

        NUMBER,

        SELECT,

        TEXT,
        COLOR
    }
}
