package com.firesoftitan.play.titanbox.enums;

public enum ButtonEnum {
    TRUE(3, "true"),
    FALSE(2, "false"),
    BLANK(1, "blank"),
    EMPTY(0, "empty");

    private final String name;
    private final Integer value;
    ButtonEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    public static ButtonEnum getType(Boolean type)
    {
        for(ButtonEnum buttonenum: ButtonEnum.values())
        {
            if (buttonenum.name.equals(type.toString().toLowerCase()))
            {
                return buttonenum;
            }
        }
        return ButtonEnum.EMPTY;
    }
    public static ButtonEnum getType(String type)
    {
        for(ButtonEnum buttonenum: ButtonEnum.values())
        {
            if (buttonenum.name.equals(type))
            {
                return buttonenum;
            }
        }
        return ButtonEnum.EMPTY;
    }
    public static ButtonEnum getType(int value)
    {
        for(ButtonEnum buttonenum: ButtonEnum.values())
        {
            if (buttonenum.value == value)
            {
                return buttonenum;
            }
        }
        return ButtonEnum.EMPTY;
    }
}
