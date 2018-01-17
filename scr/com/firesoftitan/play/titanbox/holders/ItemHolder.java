package com.firesoftitan.play.titanbox.holders;

import com.firesoftitan.play.titanbox.TitanBox;
import org.bukkit.inventory.ItemStack;

public enum ItemHolder {
    UNIT_A(9, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTAxNGY4NzEyZjU1OGQyMGMxNDc3NjEwZDgyZWMzMjY3MzEyNDY0N2ViMTA3NjQzN2RkMmNkZDJhODYzN2U0YSJ9fX0="),
    UNIT_B(18, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2YxOWJkZmRmYWEzOTk0OWRhMDY2N2Y0YTg5ZjM2NDI2YmE4NjkyYWVmNTFmYzYwNTFhOTgwMjMyN2RhNyJ9fX0="),
    UNIT_C(27, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNiZGJhZWRkN2Q2NDQ0ZTc5YWE4MjIyZjg5ODEyNDAyMDRjYzNjYzFjOTY1NTExODY4NzYxOGRiOGNlYyJ9fX0="),
    UNIT_D(36, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBjNDRkMWE4Y2I0ZDdhY2JmODYxMjJhY2JkOWZjZmFmOWRiZTRhNGVkZjM4OWE4ODdiMjRjZTQzMmEwODQ0YSJ9fX0="),
    UNIT_E(45, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWM5NmJlNzg4NmViN2RmNzU1MjVhMzYzZTVmNTQ5NjI2YzIxMzg4ZjBmZGE5ODhhNmU4YmY0ODdhNTMifX19"),
    ROUTER(-1, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I3YzliNmEyM2YyMWNjYTJiMzYyYjg1YjM2ZGVjZTNkODM4OWUzNjMwMTRkZWZlNWI5MmZmNmVlNjRmMWFlIn19fQ=="),
    UPGRADE(-2, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc4MzQ3NzUzMzZhMmY1Mzg2NzM5OTFiZTA3NmM4NzRjN2ZhZDExYjdiN2Y0ZjMyMTQzYjYzM2UwNDMxMjEifX19"),
    WATERPUMP(-3, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI3YWU1ZDM3MWYwZGVmYmY3MWExZGY4MWE3ZjVmMTM3Zjc2ZDUzNzg0NDQ2Yzc5YzY2NzIxOGQyOGZlYzRkIn19fQ=="),
    LAVAPUMP(-4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M3NTM1NzFlYjVkZTFjYjM2YjU5M2E5MGY5OTcxZTI3OGFkMzdkZWVhN2Q5OGFjZmFhZWUyZTU5ZjNmYSJ9fX0="),
    ICEPUMP(-5, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDQxNjQ2YzAzZjhiNGNkYmJmODFmNzE2NGRkNjNhMjljOTYzYTZjNmNlYmZlMWNhZjE5YTJlZTkyYyJ9fX0="),
    ITEMPUMP(-6, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdiNDE2ZDM2Yjc1NjE3Mzk4ZDdiYjNjZThkYjhhNzRiZDVlMzE0MTgyNTM3NzhjN2E2ZmU4NTQ5ODY3MSJ9fX0="),
    KILLERPUMP(-7, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I4NTJiYTE1ODRkYTllNTcxNDg1OTk5NTQ1MWU0Yjk0NzQ4YzRkZDYzYWU0NTQzYzE1ZjlmOGFlYzY1YzgifX19"),
    BACKPACK(-8, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODhjZDQyZjkyMDYxZWQ4OTZjZTVjNDRmYzg4ODllYmI2NGFiMjY4ZWQ1ZWZmYzVjZjY1MWEyMDliYTA4YjQzIn19fQ=="),
    STORAGE(-9,"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmU1NzkzZjBjYzQwYTkzNjgyNTI3MTRiYzUyNjNhNWMzZGYyMjMzYmRkZjhhNTdlM2Q4ZDNmNTRhZjY3MjZjIn19fQ==");

    private final int size;
    private final String texute;
    private final ItemStack item;

    ItemHolder(int size, String texute)
    {
        this.size = size;
        this.texute = texute;
        this.item = TitanBox.getSkull(texute);
    }
    public static ItemHolder getBySize(int size)
    {
        for (ItemHolder ih: ItemHolder.values())
        {
            if (ih.size == size)
            {
                return ih;
            }
        }
        return null;
    }
    public int getSize() {
        return size;
    }

    public ItemStack getItem() {
        ItemStack tmp = item.clone();
        tmp.setAmount(1);
        return tmp;
    }

    public String getTexute() {
        return texute;
    }

}

