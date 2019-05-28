package com.firesoftitan.play.titanbox.machines;

import com.firesoftitan.play.titanbox.Utilities;
import com.firesoftitan.play.titanbox.containers.ReallyBigContainer;
import com.firesoftitan.play.titanbox.managers.PlayerManager;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class DeathRecovery extends ReallyBigContainer {

	public static DeathRecovery instance;
	public DeathRecovery(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
		instance = this;
	}

	@Override
	public String getInventoryTitle() {
		return "Titan Death Recovery";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.IRON_PICKAXE);
	}

	@Override
	public void registerDefaultRecipes() {}
	
	@Override
	public int getSpeed() {
		return 1;
	}
	
	@Override
	public String getMachineIdentifier() {
		return "TITAN_DEATH_RECOVERY";
	}
	
	public abstract int getSpeedFactor();

	@Override
	protected void tick(Block b) {
		ForceFieldManager forceFieldManager = Utilities.getForceField(b.getLocation());
		if (forceFieldManager == null) return;
		Player player = Bukkit.getPlayer(forceFieldManager.getOwner());
		if (player == null) return;
		if (!player.isOnline()) return;
		PlayerManager playerManager = PlayerManager.getPlayer(player);
		if (playerManager == null) return;
		if (!playerManager.hasTitanDeathRecovery())
		{
			playerManager.setTitanDeathRecovery(b.getLocation());
		}


		if (isProcessing(b)) {
			int timeleft = progress.get(b);
			if (timeleft > 0) {
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
				
				BlockStorage.getInventory(b).replaceExistingItem(23, item);
				
				if (ChargableBlock.isChargable(b)) {
					if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
					ChargableBlock.addCharge(b, -getEnergyConsumption());
					progress.put(b, timeleft - 1);
				}
				else progress.put(b, timeleft - 1);
			}
			else {
				BlockStorage.getInventory(b).replaceExistingItem(23, new CustomItem(new ItemStack(Material.CLOCK), "Waiting for death..."));
				pushItems(b, processing.get(b).getOutput());
				
				progress.remove(b);
				processing.remove(b);
			}
		}
		else {
			MachineRecipe r = null;
			for (int slot: getInputSlots()) {

				ItemStack item = BlockStorage.getInventory(b).getItemInSlot(slot);
				if (item != null) {
					ItemStack newItem = item.clone();
					r = new MachineRecipe(1 * getSpeedFactor(), new ItemStack[] {item}, new ItemStack[] {newItem});
					if (r != null) {
						if (!fits(b, r.getOutput())) return;
						BlockStorage.getInventory(b).replaceExistingItem(slot, null);
						processing.put(b, r);
						progress.put(b, r.getTicks());
						return;
					}
				}
			}
			BlockStorage.getInventory(b).replaceExistingItem(23, new CustomItem(new ItemStack(Material.CLOCK), "Waiting for death..."));


		}
	}
	public void kickOutProcessing()
	{
		for (Block b: processing.keySet()) {
			BlockStorage.getInventory(b).replaceExistingItem(23, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
			pushItems(b, processing.get(b).getOutput());
		}
		progress.clear();
		processing.clear();
	}

}
