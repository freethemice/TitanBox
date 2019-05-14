package com.firesoftitan.play.titanbox.enums;

public enum PrivacyEnum {
    PRIVATE(0, "Private"),
    FRIENDS(1, "Friends"),
    PUBLIC(2, "Public");

    private final String name;
    private final Integer value;
    PrivacyEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    public static PrivacyEnum valueOf(int value)
    {
        for(PrivacyEnum buttonenum: PrivacyEnum.values())
        {
            if (buttonenum.value == value)
            {
                return buttonenum;
            }
        }
        return PrivacyEnum.PRIVATE;
    }
    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}
