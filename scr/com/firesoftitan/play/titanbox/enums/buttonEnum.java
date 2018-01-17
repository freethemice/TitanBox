package com.firesoftitan.play.titanbox.enums;

public enum  buttonEnum {
    TRUE(3, "true"),
    FALSE(2, "false"),
    BLANK(1, "blank"),
    EMPTY(0, "empty");

    private final String name;
    private final Integer value;
    buttonEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }
    public static buttonEnum getType(Boolean type)
    {
        for(buttonEnum buttonenum: buttonEnum.values())
        {
            if (buttonenum.name.equals(type.toString().toLowerCase()))
            {
                return buttonenum;
            }
        }
        return buttonEnum.EMPTY;
    }
    public static buttonEnum getType(String type)
    {
        for(buttonEnum buttonenum: buttonEnum.values())
        {
            if (buttonenum.name.equals(type))
            {
                return buttonenum;
            }
        }
        return buttonEnum.EMPTY;
    }
    public static buttonEnum getType(int value)
    {
        for(buttonEnum buttonenum: buttonEnum.values())
        {
            if (buttonenum.value == value)
            {
                return buttonenum;
            }
        }
        return buttonEnum.EMPTY;
    }
}
