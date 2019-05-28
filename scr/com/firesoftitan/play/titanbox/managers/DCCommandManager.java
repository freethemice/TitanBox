package com.firesoftitan.play.titanbox.managers;

import com.firesoftitan.play.titanbox.Utilities;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DCCommandManager {
    private Material material;
    private String texture;
    private List<String> commands;
    private List<String> lore;
    private String text;
    private int slot;
    public DCCommandManager()
    {
    }

    public ItemStack getButton()
    {
        ItemStack button;
        try {
            if (material == Material.PLAYER_HEAD && texture != null) {
                button = CustomSkull.getItem(texture);
            }
            else
            {
                button = new ItemStack(this.material);
            }
            if (this.text != null) {
                button = Utilities.changeName(button, this.text);
            }
            if (this.lore != null) {
                button = Utilities.addLore(button, this.lore);
            }
            return button;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ItemStack(Material.LIME_TERRACOTTA);
    }
    public List<String> getCommands() {
        return commands;
    }

    public String getText() {
        return text;
    }

    public List<String> getLore() {
        return lore;
    }
    public void setLore(String... lore) {
        List<String> loreList = new ArrayList<String>();
        for (String l : lore) {
            loreList.add(l);
        }
        this.lore = loreList;
    }
    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setCommands(String... commands) {
        List<String> loreList = new ArrayList<String>();
        for (String l : commands) {
            loreList.add(l);
        }
        this.commands = loreList;
    }
    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getTexture() {
        return texture;
    }

    public void setMaterial(Material material) {
        if (material == null)
        {
            this.material = Material.LIME_TERRACOTTA;
            return;
        }
        this.material = material;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}
