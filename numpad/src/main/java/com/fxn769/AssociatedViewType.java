package com.fxn769;

/**
 * Created by jasati200 on 3/26/18.
 */

public enum AssociatedViewType {
    edit_text(1),
    text_view(2);

    private final int code;

    AssociatedViewType(int code) {
        this.code = code;
    }

    public static AssociatedViewType of(int code) {
        for (AssociatedViewType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }
}
