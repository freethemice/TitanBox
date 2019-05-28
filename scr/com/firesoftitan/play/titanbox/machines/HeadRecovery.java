package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.HeadRecoveryContainer;
import com.firesoftitan.play.titanbox.enums.ItemEnum;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public abstract class HeadRecovery extends HeadRecoveryContainer {;
    public HeadRecovery(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

    }

    @Override
    public String getInventoryTitle() {
        return "&bHead Factory";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.GOLDEN_AXE);
    }

    @Override
    public void registerDefaultRecipes() {}

    public abstract int getSpeed();

    @SuppressWarnings("deprecation")
    protected void tick(Block b) {
        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0 && getSpeed() < 10) {
                ItemStack item = getProgressBar().clone();
                item.setDurability(MachineHelper.getDurability(item, timeleft, processing.get(b).getTicks()));
                ItemMeta im = item.getItemMeta();
                im.setDisplayName(" ");
                List<String> lore = new ArrayList<String>();
                lore.add(MachineHelper.getProgress(timeleft, processing.get(b).getTicks()));
                lore.add("");
                lore.add(MachineHelper.getTimeLeft(timeleft / 2));
                im.setLore(lore);
                item.setItemMeta(im);

                BlockStorage.getInventory(b).replaceExistingItem(22, item);

                if (ChargableBlock.isChargable(b)) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                    ChargableBlock.addCharge(b, -getEnergyConsumption());
                    progress.put(b, timeleft - 1);
                }
                else progress.put(b, timeleft - 1);
            }
            else if (ChargableBlock.isChargable(b)) {
                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                ChargableBlock.addCharge(b, -getEnergyConsumption());

                BlockStorage.getInventory(b).replaceExistingItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " "));
                pushItems(b, processing.get(b).getOutput());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            for (int i = 0; i < 9; i++)
            {
                BlockStorage.getInventory(b).replaceExistingItem(36 + i,new CustomItem(new MaterialData(Material.GREEN_STAINED_GLASS_PANE), " ", new String[0]));
            }
            for (int slot: getInputSlots()) {
                if (!Utilities.isEmpty(BlockStorage.getInventory(b).getItemInSlot(slot))) {
                    ItemStack input = BlockStorage.getInventory(b).getItemInSlot(slot);
                    String name = Utilities.getName(input);
                    if (name.equals("PLAYER_HEAD") && input.getType() == Material.PLAYER_HEAD)
                    {
                        String Texture = CustomSkull.getTexture(input);
                        if (Texture != ItemEnum.UNIT_A.getTexute() && Texture != ItemEnum.UNIT_B.getTexute() &&
                                Texture != ItemEnum.UNIT_C.getTexute() && Texture != ItemEnum.UNIT_D.getTexute() &&
                                Texture != ItemEnum.UNIT_E.getTexute()) {
                            List<ItemStack> heads = Utilities.getByTexture(Texture);
                            slimefunHeads.put(b, heads);
                            for (int i = 0; i < heads.size(); i++) {
                                BlockStorage.getInventory(b).replaceExistingItem(36 + i, heads.get(i).clone());
                            }
                        }
                        /*
                        MachineRecipe r = new MachineRecipe(4 / getSpeed(), new ItemStack[0], new ItemStack[]{items.get(0).getItemFromStorage().clone()});
                        if (!fits(b, r.getOutput())) return;
                        BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
                        processing.put(b, r);
                        progress.put(b, r.getTicks());*/
                        break;
                    }
                    name = Utilities.getName(input, false);

                    if (input.getType() == Material.DISPENSER && name.equals(Utilities.getName(SlimefunItems.BLOCK_PLACER, false)))
                    {
                        List<ItemStack> heads = new ArrayList<ItemStack>();
                        heads.add(SlimefunItems.BLOCK_PLACER.clone());
                        slimefunHeads.put(b, heads);
                        for (int i = 0; i < heads.size(); i++) {
                            BlockStorage.getInventory(b).replaceExistingItem(36 + i, heads.get(i).clone());
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getMachineIdentifier() {
        return "HEAD_RECOVERY";
    }

}
