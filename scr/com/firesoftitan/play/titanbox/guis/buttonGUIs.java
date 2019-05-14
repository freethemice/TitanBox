package com.firesoftitan.play.titanbox.guis;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.enums.ButtonEnum;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ButtonGUIs {

    private ItemStack materialTrue = null;
    private ItemStack materialFalse = null;
    private String textureTrue = "";
    private String textureFalse = "";
    private String nameTrue = "";
    private String nameFalse = "";
    private int slot = -1;
    private List<String> lore = new ArrayList<String>();
    private MainGUI parent;
    private ButtonEnum type;
    private ItemStack blank = new ItemStack(Material.BROWN_STAINED_GLASS_PANE);


    public ButtonGUIs(MainGUI parent, int slot)
    {
        this.parent = parent;
        this.blank = Utilities.changeName(this.blank,"-");
        this.type = ButtonEnum.EMPTY;
        this.slot = slot;
    }

    public void setBlank(ItemStack blank) {
        this.blank = blank;
    }

    public MainGUI getParent() {
        return parent;
    }

    public void update()
    {
        Inventory inv = parent.getMyGui();
        inv.setItem(getSlot(), getButton());
        //parent.showGUI();
    }
    public int getSlot() {
        return slot;
    }

    public List<String> getLore() {
        return lore;
    }
    public void setMaterialFalse(ItemStack materialFalse) {
        this.materialFalse = materialFalse.clone();
    }

    public void setMaterialTrue(ItemStack materialTrue) {
        this.materialTrue = materialTrue.clone();
    }
    public void setToggle(ButtonEnum bEnum)
    {
        type = bEnum;
        parent.getMyGui().setItem(getSlot(), getButton());
    }
    public ButtonEnum getToggle() {
        return type;
    }

    public String getName() {
        switch (type)
        {
            case TRUE:
                return nameTrue;
            case FALSE:
                return nameFalse;
            case BLANK:
                return "-";
            case EMPTY:
                return null;
        }
        return null;
    }

    public void addLore(String... lores)
    {
        List<String> lore = new ArrayList<String>();
        for (String l : lores) {
            lore.add(l);
        }
        this.lore = lore;
    }
    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setNameTrue(String nameTrue) {
        this.nameTrue = nameTrue;
    }

    public void setNameFalse(String nameFalse) {
        this.nameFalse = nameFalse;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setTextureTrue(String textureTrue) {
        this.textureTrue = textureTrue;
        this.materialTrue = null;
    }

    public void setTextureFalse(String textureFalse) {
        this.textureFalse = textureFalse;
        this.materialFalse = null;
    }
    public ItemStack getButton()
    {
        ItemStack battery = null;

        switch (type)
        {
            case TRUE:

                if (materialTrue == null)
                {
                    battery = Utilities.getSkull(textureTrue);
                }
                else
                {
                    battery = materialTrue.clone();
                }
                battery = Utilities.changeName(battery, this.getName());
                battery = Utilities.addLore(true, battery, this.getLore());
                return battery.clone();
            case FALSE:
                if (materialFalse == null)
                {
                    battery = Utilities.getSkull(textureFalse);
                }
                else
                {
                    battery = materialFalse.clone();
                }
                battery = Utilities.changeName(battery, this.getName());
                battery = Utilities.addLore(true, battery, this.getLore());
                return battery.clone();
            case BLANK:
                battery = Utilities.changeName(blank.clone(), this.getName());
                battery = Utilities.clearLore(battery);
                return battery.clone();
            case EMPTY:
                return null;
        }
        return null;
    }
}
