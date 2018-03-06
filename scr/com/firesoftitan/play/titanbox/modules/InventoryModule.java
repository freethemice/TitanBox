package com.firesoftitan.play.titanbox.modules;

import com.firesoftitan.play.titanbox.TitanBox;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.holders.RouterHolder;
import com.firesoftitan.play.titanbox.holders.SendingSlotHolder;
import com.firesoftitan.play.titanbox.machines.StorageUnit;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryModule extends MainModule {

    private List<SendingSlotHolder> slotsSending = new ArrayList<SendingSlotHolder>();
    private List<Integer> slotsPulling = new ArrayList<Integer>();
    private String mode = "pulling";
    private boolean hidebar = false;
    private Inventory myGui = null;
    public List<Integer> bufferSlotsPulling = new ArrayList<Integer>();
    public List<SendingSlotHolder> bufferSlotsSending = new ArrayList<SendingSlotHolder>();
    private int tmpSlot = -1;
    private long lastran = 0;
    private int editingSlot = -1;
    private Location[] secondary;
    private static int secondrySize = 5;
    private boolean didISend = false;
    private boolean reset = false;
    public InventoryModule()
    {
        type = ModuleTypeEnum.Inventory;
    }
    public static void loadConfig()
    {

        if (!TitanBox.config.contains("settings.module.linking.secondarysize"))
        {
            TitanBox.config.setValue("settings.module.linking.secondarysize", secondrySize);
        }
        secondrySize = TitanBox.config.getInt("settings.module.linking.secondarysize");
    }
    @Override
    public void loadInfo()
    {
        super.loadInfo();

        slotsPulling.clear();
        slotsSending.clear();
        bufferSlotsPulling.clear();
        bufferSlotsSending.clear();

        secondary = new Location[secondrySize];

        if (modules.contains("modules." + moduleid + ".slots.hide")) {
            hidebar = modules.getBoolean("modules." + moduleid + ".slots.hide");
        }
        if (modules.contains("modules." + moduleid + ".slots.pulling")) {
            slotsPulling = modules.getIntList("modules." + moduleid + ".slots.pulling");
        }
        if (modules.contains("modules." + moduleid + ".slots.mode")) {
            mode = modules.getString("modules." + moduleid + ".slots.mode");
        }
        if (modules.contains("modules." + moduleid + ".slots.editing")) {
            tmpSlot = modules.getInt("modules." + moduleid + ".slots.editing");
        }
        if (modules.contains("modules." + moduleid + ".slots.filter")) {
            Set<String> keys = modules.getKeys("modules." + moduleid + ".slots.filter");
            for (String key : keys) {
                try {
                    int tmp = Integer.parseInt(key);
                    ItemStack tmp2 = modules.getItem("modules." + moduleid + ".slots.filter." + key);
                    slotsSending.add(new SendingSlotHolder(tmp, tmp2.clone()));
                }
                catch (Exception e)
                {
                    System.out.println("[TitanBox]: Bad Filter!");
                    modules.setValue("modules." + moduleid + ".slots.filter." + key, null);
                }
            }
        }
        for (int i =0; i < secondary.length; i++)
        {
            Location loc = secondary[i];
            if (modules.contains("modules." + moduleid + ".slots.secondary." + i)) {
                secondary[i] = modules.getLocation("modules." + moduleid + ".slots.secondary." + i);
            }

        }
    }

    public int getEditingSlot() {
        return editingSlot;
    }

    public void setEditingSlot(int editingSlot) {
        this.editingSlot = editingSlot;
    }

    public boolean isHidebar() {
        return hidebar;
    }

    public void setHidebar(boolean hidebar) {
        this.hidebar = hidebar;
    }

    @Override
    public void saveInfo()
    {
        super.saveInfo();
        Collections.sort(slotsPulling);
        modules.setValue("modules." + moduleid + ".slots.pulling", slotsPulling);
        modules.setValue("modules." + moduleid + ".slots.mode", mode);
        modules.setValue("modules." + moduleid + ".slots.editing", tmpSlot);
        modules.setValue("modules." + moduleid + ".slots.hide", hidebar);

        modules.setValue("modules." + moduleid + ".slots.filter", null);
        for(SendingSlotHolder i: slotsSending)
        {
            if (i.getItem() == null)
            {
                modules.setValue("modules." + moduleid + ".slots.filter." + i.getSlot(), new ItemStack(Material.AIR));
            }
            else
            {
                modules.setValue("modules." + moduleid + ".slots.filter." + i.getSlot(), i.getItem());
            }


        }
        for (int i =0; i < secondary.length; i++)
        {
            Location loc = secondary[i];
            if (loc != null)
            {
                modules.setValue("modules." + moduleid + ".slots.secondary." + i, loc);
            }
        }
    }
    @Override
    public void clearInfo()
    {
        super.clearInfo();
        if (this.reset) {
            slotsPulling.clear();
            slotsSending.clear();
            bufferSlotsPulling.clear();
            bufferSlotsSending.clear();
            //modules.setValue("modules." + moduleid , null);
        }
    }

    @Override
    public void OpenGui(Player player)
    {
        makeGui(player);
        player.openInventory(this.myGui);
    }

    private void makeGui(Player player) {
        myGui = Bukkit.createInventory(null, 54, this.type.getTitle());
        ItemStack blank = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 12);
        blank.getItemMeta().setDisplayName("-");
        String pushpull = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=";
        String namepp = "sending to";
        ItemStack typesending = TitanBox.getSkull(pushpull);
        typesending = TitanBox.changeName(typesending, namepp);

        pushpull = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYzMzlmZjJlNTM0MmJhMThiZGM0OGE5OWNjYTY1ZDEyM2NlNzgxZDg3ODI3MmY5ZDk2NGVhZDNiOGFkMzcwIn19fQ==";
        namepp = "pulling from";

        ItemStack typepulling = TitanBox.getSkull(pushpull);
        typepulling = TitanBox.changeName(typepulling, namepp);

        ItemStack itemMode = new ItemStack(Material.IRON_INGOT, 1);
        itemMode = TitanBox.changeName(itemMode, "Your Items");

        ItemStack editMode = new ItemStack(Material.SIGN, 1);
        editMode = TitanBox.changeName(editMode, "Edit Mode");

        ItemStack block = new ItemStack(Material.BARRIER, 1);
        ItemStack hideBlock =TitanBox.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjNjNjZjNDJjZGUxZGQxZjVmOTUxNDNlNDcyMjU0ZjdmYWU4ZWNjZmY0ZGM5NDNiMDg2YTk0NGIyN2JkZmIifX19");
        hideBlock = TitanBox.changeName(hideBlock, "Hide Menu Bar");
        hideBlock = TitanBox.addLore(hideBlock, ChatColor.WHITE + "-Shift Click Any Item to Show bar Again");
        if (getLink() != null) {
            block = new ItemStack(getLink().getBlock().getType(), 1, getLink().getBlock().getData());
            if (block.getType() == Material.SKULL) {
                String texture = "";
                try {
                    texture = CustomSkull.getTexture(getLink().getBlock());
                    block = CustomSkull.getItem(texture);
                } catch (Exception e) {

                }
            }
            if (block == null || !TitanBox.isInventory(getLink().getBlock())) {
                setLink(null, player);
                updateGUIClicked(player, this, false);
                block = new ItemStack(Material.BARRIER, 1);
            }
            else
            {
                BlockStorage storage = BlockStorage.getStorage(getLink().getWorld());
                if (storage.hasInventory(getLink())) {
                    BlockMenu menu = BlockStorage.getInventory(getLink());

                    for (int slot : menu.getPreset().getPresetSlots()) {
                        ItemStack guiSlot = menu.getPreset().getItemInSlot(slot);

                        myGui.setItem(slot, guiSlot.clone());

                    }

                }


            }
            for (int slot : slotsPulling) {
                myGui.setItem(slot, typepulling.clone());
            }

            if (!getMode().equalsIgnoreCase("items") && !getMode().equalsIgnoreCase("edit")) {
                for (SendingSlotHolder slot : slotsSending) {
                    myGui.setItem(slot.getSlot(), typesending.clone());
                }
            }
            else
            {
                for (int i = 0; i < 53; i++)
                {
                    int sloty = getIndexOfSlot(i);
                    if (sloty == -1)
                    {
                        if (TitanBox.isEmpty(myGui.getItem(i)))
                        {
                            ItemStack noEdit = new ItemStack(Material.BARRIER, 1);
                            noEdit = TitanBox.changeName(noEdit, "Can't edit this.");
                            noEdit = TitanBox.addLore(noEdit, "You can only change slots that", "are being send items.");
                            myGui.setItem(i, noEdit.clone());
                        }
                    }
                }
                for (SendingSlotHolder slot : slotsSending) {
                    myGui.setItem(slot.getSlot(), slot.getItem());
                }
                hideBlock = TitanBox.changeName(hideBlock, "Back Menu Bar");
                hideBlock = TitanBox.addLore(hideBlock, ChatColor.WHITE + "-Click here to go back to the main menu.");
            }
        }

        if (!hidebar) {
            for (int i = 45; i < 54; i++) {
                myGui.setItem(i, blank.clone());
            }


            //type = TitanBox.addLore(type, ChatColor.YELLOW + "Location: "  + ChatColor.WHITE + getLink().getWorld().getTitle() + ", " + getLink().getBlockX() + ", " + getLink().getBlockY() + ", " + getLink().getBlockZ());
            typepulling = TitanBox.addLore(typepulling, "Click Here To Setup Manualy", "Then Click Any Empty Slot", "or Click Arrow To Remove, above");
            typesending = TitanBox.addLore(typesending, "Click Here To Setup Manualy", "Then Click Any Empty Slot", "or Click Arrow To Remove, above");
            itemMode = TitanBox.addLore(itemMode, "Click Here To Setup Items", "Then Click Any Item In Your Inv", "or Click Slot To Remove, above");
            editMode = TitanBox.addLore(editMode, "Click Here for advanced setting", "Then click the item you want to edit");
            if (!this.getMode().equalsIgnoreCase("edit")) {
                myGui.setItem(46, typesending.clone());
                myGui.setItem(47, typepulling.clone());
                myGui.setItem(48, itemMode.clone());
                myGui.setItem(49, editMode.clone());

            }
            else {

                ItemStack filterItem = getItemNSlotSending(editingSlot);
                if (!TitanBox.isEmpty(filterItem)) {
                    int amount = filterItem.getMaxStackSize();
                    if (modules.contains("modules." + moduleid + ".slots.advance." + editingSlot + ".max")) {
                        amount = modules.getInt("modules." + moduleid + ".slots.advance." + editingSlot + ".max");
                    }
                    ItemStack slot46 = filterItem.clone();
                    slot46 = TitanBox.changeName(slot46, "Number To Send At A Time: " + amount);
                    slot46 = TitanBox.addLore(true, slot46, ChatColor.AQUA + "Left Click: " + ChatColor.WHITE + "Increase", ChatColor.AQUA + "Right Click: " + ChatColor.WHITE + "Decrease", ChatColor.AQUA + "Shift Left Click: " + ChatColor.WHITE + "IncreaseX16", ChatColor.AQUA + "Shift Right Click: " + ChatColor.WHITE + "DecreaseX16");
                    slot46.setAmount(amount);
                    myGui.setItem(46, slot46.clone());


                    int keep = 0;
                    if (modules.contains("modules." + moduleid + ".slots.advance." + editingSlot + ".keep")) {
                        keep = modules.getInt("modules." + moduleid + ".slots.advance." + editingSlot + ".keep");
                    }

                    ItemStack slot47 = TitanBox.instants.getItem("e").clone();
                    if (keep == 0)
                    {
                        slot47 = new ItemStack(Material.BARRIER);
                    }
                    slot47 = TitanBox.changeName(slot47, "Number keep in Storage: " + keep);
                    slot47 = TitanBox.addLore(true, slot47, ChatColor.AQUA + "Left Click: " + ChatColor.WHITE + "Increase", ChatColor.AQUA + "Right Click: " + ChatColor.WHITE + "Decrease", ChatColor.AQUA + "Shift Left Click: " + ChatColor.WHITE + "IncreaseX64", ChatColor.AQUA + "Shift Right Click: " + ChatColor.WHITE + "DecreaseX64");
                    slot47.setAmount(1);
                    if (keep > 1) {
                        slot47.setAmount(keep);
                    }
                    if (keep > 64) {
                        slot47.setAmount(64);
                    }
                    myGui.setItem(47, slot47.clone());

                }


                //myGui.setItem(47, typepulling.clone());
                //myGui.setItem(48, itemMode.clone());
                //myGui.setItem(49, editMode.clone());
            }

            myGui.setItem(52, hideBlock.clone());
            if (this.getMode().equalsIgnoreCase("pulling")) {
                typepulling = TitanBox.addLore(typepulling, "You are in pulling mode!", "you can place and remove pulling slots.");
                myGui.setItem(53, typepulling.clone());
            } else if (this.getMode().equalsIgnoreCase("sending")) {
                typesending = TitanBox.addLore(typesending, "You are in sending mode!", "you can place and remove sending slots.");
                myGui.setItem(53, typesending.clone());
            } else if (this.getMode().equalsIgnoreCase("edit")) {
                typesending = TitanBox.addLore(editMode, "You are in edit mode!", "click sending items", "for advanced options");
                myGui.setItem(53, typesending.clone());
            } else if (this.getMode().equalsIgnoreCase("items")) {
                itemMode = new ItemStack(Material.BARRIER, 1);
                if (tmpSlot != -1) {
                    ItemStack adding = player.getInventory().getItem(tmpSlot);
                    if (!TitanBox.isEmpty(adding)) {
                        itemMode = adding.clone();
                        itemMode.setAmount(1);
                    }
                }
                itemMode = TitanBox.changeName(itemMode, "Your Items");
                itemMode = TitanBox.addLore(itemMode, "You are item mode!", "you can place and remove items slots.");
                myGui.setItem(53, itemMode.clone());
            }
            if (getLink() != null) {
                if (getLink().getBlock().getType() == Material.BREWING_STAND) {
                    block = new ItemStack(Material.BREWING_STAND_ITEM);
                }
            }
            block = TitanBox.changeName(block, "Linked Object");
            if (getLink() == null) {
                TitanBox.addLore(block, ChatColor.YELLOW + "Location: " + ChatColor.WHITE + "not set");
            } else {
                block = TitanBox.addLore(block, ChatColor.YELLOW + "Location: " + ChatColor.WHITE + getLink().getWorld().getName() + ", " + getLink().getBlockX() + ", " + getLink().getBlockY() + ", " + getLink().getBlockZ());
            }

            myGui.setItem(45, block);
        }


    }
    @Override
    public boolean ready()
    {
        if (link == null)
        {
            return false;
        }
        return true;
    }
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public ItemStack getItemNSlotSending(int slot)
    {
        int index = getIndexOfSlot(slot);
        if (slot < 0 || index < 0 || index > slotsSending.size())
        {
            return null;
        }
        return  slotsSending.get(index).getItem();
    }
    public int getIndexOfSlot(int slot)
    {
        for (int i = 0; i < slotsSending.size(); i++)
        {
            if (slotsSending.get(i).getSlot() == slot)
            {
                return i;
            }
        }
        return  -1;
    }
    private boolean doesStorageMatch(Location toCheck)
    {
        if (link != null)
        {
            BlockStorage storage = BlockStorage.getStorage(getLink().getWorld());
            if (storage.hasInventory(getLink())) {
                if (storage.hasInventory(toCheck))
                {
                    BlockMenu menu = BlockStorage.getInventory(getLink());
                    List<Integer> sendingMainSlots = new ArrayList<Integer>();
                    List<Integer> pullinggMainSlots = new ArrayList<Integer>();
                    for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.INSERT, null)) {
                        sendingMainSlots.add(slot);
                    }
                    for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
                        pullinggMainSlots.add(slot);
                    }
                    BlockMenu menuCheck = BlockStorage.getInventory(toCheck);
                    int count = 0;
                    for (int slot : menuCheck.getPreset().getSlotsAccessedByItemTransport(menuCheck, ItemTransportFlow.INSERT, null)) {
                        if (!sendingMainSlots.contains(slot))
                        {
                            return false;
                        }
                        count++;
                    }
                    if (sendingMainSlots.size() != count)
                    {
                        return false;
                    }
                    count = 0;
                    for (int slot : menuCheck.getPreset().getSlotsAccessedByItemTransport(menuCheck, ItemTransportFlow.WITHDRAW, null)) {
                        if (!pullinggMainSlots.contains(slot))
                        {
                            return false;
                        }
                        count++;
                    }
                    if (pullinggMainSlots.size() != count)
                    {
                        return false;
                    }
                    return true;
                }
            }
            else
            {
                Material mat = getLink().getBlock().getType();
                Material checkMat = toCheck.getBlock().getType();
                if (mat == Material.CHEST || mat == Material.TRAPPED_CHEST) {
                    if (checkMat == Material.CHEST || checkMat == Material.TRAPPED_CHEST) {
                        return true;
                    }
                }
                if (mat == Material.DISPENSER || mat == Material.DROPPER)
                {
                    if (checkMat == Material.DISPENSER || checkMat == Material.DROPPER)
                    {
                        return true;
                    }
                }
                if (mat == Material.HOPPER)
                {
                    if (checkMat == Material.HOPPER)
                    {
                        return true;
                    }
                }
                if (mat == Material.FURNACE || mat == Material.BURNING_FURNACE)
                {
                    if (checkMat == Material.FURNACE || checkMat == Material.BURNING_FURNACE)
                    {
                        return true;
                    }
                }
                if (mat == Material.BREWING_STAND)
                {
                    if (mat == Material.BREWING_STAND)
                    {
                        return true;
                    }
                }
                if (mat == checkMat)
                {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void preLoadSlots()
    {
        if (getLink() != null && this.reset) {
            BlockStorage storage = BlockStorage.getStorage(getLink().getWorld());
            if (storage.hasInventory(getLink())) {
                BlockMenu menu = BlockStorage.getInventory(getLink());
                for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.INSERT, null)) {
                    slotsSending.add(new SendingSlotHolder(slot, null));
                }
                for (int slot : menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null)) {
                    slotsPulling.add(slot);
                }
            } else {
                Material mat = getLink().getBlock().getType();
                if (mat == Material.CHEST || mat == Material.TRAPPED_CHEST) {
                    Inventory inv = TitanBox.getVanillaInventoryFor(getLink().getBlock());
                    for (int i = 0; i < inv.getSize(); i++) {
                        slotsPulling.add(i);
                    }
                }
                if (mat == Material.DISPENSER || mat == Material.DROPPER) {
                    for (int i = 0; i < 9; i++) {
                        slotsSending.add(new SendingSlotHolder(i, null));
                    }
                }
                if (mat == Material.HOPPER) {
                    for (int i = 0; i < 5; i++) {
                        slotsSending.add(new SendingSlotHolder(i, null));
                    }
                }
                if (mat == Material.FURNACE || mat == Material.BURNING_FURNACE) {
                    for (int i = 0; i < 2; i++) {
                        slotsSending.add(new SendingSlotHolder(i, null));

                    }
                    slotsPulling.add(2);

                }
                if (mat == Material.BREWING_STAND) {
                    slotsPulling.add(3);
                    slotsSending.add(new SendingSlotHolder(0, null));
                    slotsSending.add(new SendingSlotHolder(1, null));
                    slotsSending.add(new SendingSlotHolder(2, null));
                    slotsSending.add(new SendingSlotHolder(4, null));


                }

            }
            this.reset = false;
        }
    }
    public void setItemAtLocation(Location where, int slot, ItemStack put)
    {
        try {
            BlockStorage storage = BlockStorage.getStorage(where.getWorld());
            if (storage.hasInventory(where)) {
                BlockMenu menu = BlockStorage.getInventory(where);
                if (put != null) {
                    menu.replaceExistingItem(slot, put.clone());
                }
                else
                {
                    menu.replaceExistingItem(slot, null);
                }
            } else {
                Inventory tmp = TitanBox.getVanillaInventoryFor(where.getBlock());
                if (put != null) {
                    tmp.setItem(slot, put.clone());
                }
                else
                {
                    tmp.setItem(slot, null);
                }

            }
        }
        catch (Exception e)
        {
        }
    }
    public ItemStack getItemAtLocation(Location where, int slot)
    {
        try {
            BlockStorage storage = BlockStorage.getStorage(where.getWorld());
            if (storage.hasInventory(where)) {
                BlockMenu menu = BlockStorage.getInventory(where);
                ItemStack tmp = menu.getItemInSlot(slot);
                return tmp.clone();
            } else {
                Inventory tmp = TitanBox.getVanillaInventoryFor(where.getBlock());
                return tmp.getItem(slot).clone();

            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public String getLinkLore()
    {
        if (getLink() == null)
        {
            return ChatColor.WHITE  + "not set";
        }
        else
        {
            Location from = getLink();
            int max = 0;
            for (int i =0; i < secondary.length; i++) {
                Location loc = secondary[i];
                if (loc != null)
                {
                    max++;
                }
            }
            return ChatColor.WHITE + from.getWorld().getName() + ": " + from.getBlockX() + ": " + from.getBlockY() + ": " + from.getBlockZ() + " (Secondary:" + max + "/" + secondary.length +")";
        }
    }
    @Override
    public void runMe(UUID owner)
    {
        startSendingPulling(owner);

        tickPulling(getLink().clone(), owner);

        tickSending(getLink().clone(), owner);

        int Found = 0;
        for(int i = 0; i < secondary.length; i++)
        {
            Location loc = secondary[i];
            if (loc != null)
            {
                Found++;
                tickPulling(loc.clone(), owner);

                tickSending(loc.clone(), owner);

            }
        }

        endSendingPulling(owner);

    }

    private void startSendingPulling(UUID owner)
    {
        if (bufferSlotsSending.size() == 0)
        {
            bufferSlotsSending.addAll(slotsSending);
        }
        if (bufferSlotsPulling.size() == 0)
        {
            bufferSlotsPulling.addAll(slotsPulling);
        }
    }
    private void endSendingPulling(UUID owner)
    {
        if (bufferSlotsSending.size() != 0) {

            if (didISend) {
                SendingSlotHolder mySlot = bufferSlotsSending.get(0);
                mySlot.check();
            }
            didISend = false;
            bufferSlotsSending.remove(0);
        }
        if (bufferSlotsPulling.size() != 0) {
            bufferSlotsPulling.remove(0);
        }
    }
    private void tickSending(Location where, UUID owner)
    {
        if (bufferSlotsSending.size() != 0) {
            SendingSlotHolder mySlot = bufferSlotsSending.get(0);
            int slot = mySlot.getSlot();
            ItemStack bufferItem = this.getItemAtLocation(where, slot);
            ItemStack fillertItem = mySlot.getItem();
            if (!TitanBox.isEmpty(bufferItem)) {
                if (!TitanBox.isItemEqual(bufferItem, fillertItem)) {
                    return;
                }
            }
            int amountMax = 64;
            if (!TitanBox.isEmpty(fillertItem)) {
                amountMax = fillertItem.getMaxStackSize();
            }
            if (modules.contains("modules." + getModuleid() + ".slots.advance." + slot + ".max")) {
                amountMax = modules.getInt("modules." + getModuleid() + ".slots.advance." +slot + ".max");
            }
            int amountKeep = 0;
            if (modules.contains("modules." + getModuleid() + ".slots.advance." + slot + ".keep")) {
                amountKeep = modules.getInt("modules." + getModuleid() + ".slots.advance." +slot + ".keep");
            }
            if (!TitanBox.isEmpty(fillertItem)) {
                for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
                    for(int i = 0; i < stH.getSize(); i++) {
                        ItemStack storagedItem = stH.viewSlot(i);
                        if (!TitanBox.isEmpty(storagedItem)) {
                            if (stH.getStorageCount(i) - amountMax > amountKeep) {
                                if (TitanBox.isItemEqual(fillertItem, storagedItem)) {
                                    int amountneeded = 64;
                                    if (!TitanBox.isEmpty(bufferItem)) {
                                        amountneeded = bufferItem.getMaxStackSize() - bufferItem.getAmount();
                                    }
                                    if (amountneeded > stH.getStorageCount(i))
                                    {
                                        amountneeded = Integer.parseInt(stH.getStorageCount(i).toString());
                                    }
                                    amountneeded = Math.min(amountMax, amountneeded);
                                    if (amountneeded > 0) {
                                        if (mySlot.getLastChecked() + RouterHolder.lagTime < System.currentTimeMillis() || amountneeded >= storagedItem.getMaxStackSize()) {
                                            ItemStack ItemTaken = stH.getItem(i, amountneeded);
                                            didISend =true;
                                            if (!TitanBox.isEmpty(bufferItem) && !TitanBox.isEmpty(ItemTaken)) {
                                                if (storagedItem.getAmount() != storagedItem.getMaxStackSize()) {
                                                    int total = bufferItem.getAmount() + ItemTaken.getAmount();
                                                    bufferItem.setAmount(total);
                                                    this.setItemAtLocation(where, slot, bufferItem.clone());
                                                }
                                            } else {
                                                if (ItemTaken != null) {
                                                    this.setItemAtLocation(where, slot, ItemTaken.clone());
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @Override
    public boolean setLink(Location link, Player player) {
        if (link != null && this.link != null)
        {
            if (link.toString().equals(this.link.toString())) {
                this.link = null;
                secondary = new Location[secondrySize];
                if (player != null) {
                    if (link != null) {
                        player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "inventory(s) unlinked!");
                    }
                }
                return true;
            }
        }
        if (this.link == null) {
            if (TitanBox.isInventory(link.getBlock())) {
                this.link = link;
                this.reset = true;
                secondary = new Location[secondrySize];
                if (player != null) {
                    if (link != null) {
                        player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "inventory linked!");
                    }
                }
                return true;
            }
        }
        else
        {
            if (TitanBox.isInventory(link.getBlock())) {
                if (this.doesStorageMatch(link))
                {
                    int max = 0;
                    for(int i = 0; i < secondary.length; i++) {
                        Location loc = secondary[i];
                        if (loc != null)
                        {
                            max++;
                            if (loc.toString().equals(link.toString()))
                            {
                                secondary[i] = null;
                                if (player != null) {
                                    if (link != null) {
                                        player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "secondary inventory unlinked!");
                                    }
                                }
                                return true;
                            }
                        }
                    }

                    for(int i = 0; i < secondary.length; i++)
                    {
                        Location loc = secondary[i];
                        if (loc == null)
                        {
                            secondary[i] = link;
                            if (player != null) {
                                if (link != null) {
                                    max++;
                                    player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "secondary inventory linked! " + ChatColor.WHITE + max + ChatColor.GREEN  + "/" +  ChatColor.WHITE +secondary.length);
                                }
                            }
                            return true;
                        }
                    }
                    if (player != null) {
                        if (link != null) {
                            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "secondary inventorys max! " + ChatColor.WHITE + secondary.length + ChatColor.GREEN  + "/" +  ChatColor.WHITE +secondary.length);
                        }
                    }
                }
                else
                {
                    if (player != null) {
                        if (link != null) {
                            player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "inventory doesn't match linked inventory(s)!");
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    private void tickPulling(Location where, UUID owner)
    {
        if (bufferSlotsPulling.size() != 0) {
            int slot = bufferSlotsPulling.get(0);

            ItemStack bufferItem = this.getItemAtLocation(where, slot);
            if (!TitanBox.isEmpty(bufferItem)) {
                for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
                    if (stH.getOwner().toString().equals(owner.toString())) {
                        int oldAmount = bufferItem.getAmount();
                        bufferItem = stH.insertItem(bufferItem);
                        if (TitanBox.isEmpty(bufferItem) || bufferItem.getAmount() != oldAmount) {
                            setItemAtLocation(where, slot, bufferItem);
                            break;
                        }
                    }
                }
            }
        }
    }
    public static void updateGUIClicked(Player who, MainModule mh, boolean showgui) {
        mh.saveInfo();
        ItemStack update = MainModule.getItemfromModule(mh);
        ItemStack mainHand = who.getInventory().getItemInMainHand();
        if (mainHand != null)
        {
            if (mainHand.getAmount() > 1)
            {
                mainHand.setAmount(mainHand.getAmount() - 1);
                who.getInventory().setItemInMainHand(update);
                who.getInventory().addItem(mainHand);
            }
            else
            {
                who.getInventory().setItemInMainHand(update);
            }
        }
        else {
            who.getInventory().setItemInMainHand(update);
        }
        if (showgui) {
            mh.OpenGui(who);
        }
    }
    public static void onInventoryClickEvent(InventoryClickEvent event, InventoryModule mh) {
        if (event.getInventory().getName().startsWith(mh.getType().getTitle()))
        {
            if ((event.isShiftClick() && mh.isHidebar()) || (event.getRawSlot() == 52 && !mh.isHidebar()))
            {
                if (mh.getMode().equals("edit"))
                {
                    mh.setEditingSlot(-1);
                    mh.setMode("pulling");
                    updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    return;
                }
                mh.setHidebar(!mh.isHidebar());
                updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                return;
            }
            int slotTo = 54;
            if (!mh.isHidebar()) {
                slotTo = 45;
                if (event.getRawSlot() == 46) {
                    if (mh.getMode().equals("edit"))
                    {
                        ItemStack filterItem = mh.getItemNSlotSending(mh.getEditingSlot());
                        if (!TitanBox.isEmpty(filterItem)) {
                            int amount = filterItem.getMaxStackSize();

                            if (modules.contains("modules." + mh.getModuleid() + ".slots.advance." + mh.getEditingSlot() + ".max")) {
                                amount = modules.getInt("modules." + mh.getModuleid() + ".slots.advance." + mh.getEditingSlot() + ".max");
                            }
                            if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
                                if (event.isShiftClick())
                                {
                                    amount = amount + 16;
                                }
                                else {
                                    amount++;
                                }
                                if (amount > filterItem.getMaxStackSize()) {
                                    amount = filterItem.getMaxStackSize();
                                }
                            }
                            if (event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                                if (event.isShiftClick()) {
                                    amount = amount - 16;
                                }
                                else
                                {
                                    amount--;
                                }
                                if (amount < 1) {
                                    amount = 1;
                                }
                            }
                            modules.setValue("modules." + mh.getModuleid() + ".slots.advance." + mh.getEditingSlot() + ".max", amount);
                        }
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                    else {
                        mh.setMode("sending");
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                }
                if (event.getRawSlot() == 47) {
                    if (mh.getMode().equals("edit"))
                    {
                        ItemStack filterItem = mh.getItemNSlotSending(mh.getEditingSlot());
                        if (!TitanBox.isEmpty(filterItem)) {
                            int  amount = 0;

                            if (modules.contains("modules." + mh.getModuleid() + ".slots.advance." + mh.getEditingSlot() + ".keep")) {
                                amount = modules.getInt("modules." + mh.getModuleid() + ".slots.advance." + mh.getEditingSlot() + ".keep");
                            }
                            if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
                                if (event.isShiftClick())
                                {
                                    amount = amount + 64;
                                }
                                else {
                                    amount++;
                                }
                                if (amount > Integer.MAX_VALUE) {
                                    amount = Integer.MAX_VALUE;
                                }
                            }
                            if (event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                                if (event.isShiftClick()) {
                                    amount = amount - 64;
                                }
                                else
                                {
                                    amount--;
                                }
                                if (amount < 0) {
                                    amount = 0;
                                }
                            }
                            modules.setValue("modules." + mh.getModuleid() + ".slots.advance." + mh.getEditingSlot() + ".keep", amount);
                        }
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                    else {
                        mh.setMode("pulling");
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                }
                if (event.getRawSlot() == 48) {
                    mh.setMode("items");
                    updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                }
                if (event.getRawSlot() == 49) {
                    mh.setMode("edit");
                    updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                }
            }
            if (event.getRawSlot() > -1 && event.getRawSlot() < slotTo)
            {
                if (TitanBox.isEmpty(event.getInventory().getItem(event.getRawSlot()))) {
                    if (mh.getMode().equalsIgnoreCase("pulling"))
                    {
                        mh.slotsPulling.add(event.getRawSlot());
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                    if (mh.getMode().equalsIgnoreCase("sending"))
                    {
                        mh.slotsSending.add(new SendingSlotHolder(event.getRawSlot(), null));
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                    if (mh.getMode().equalsIgnoreCase("items"))
                    {
                        if (mh.tmpSlot != -1)
                        {
                            ItemStack adding = event.getWhoClicked().getInventory().getItem(mh.tmpSlot);
                            if (!TitanBox.isEmpty(adding))
                            {
                                adding = adding.clone();
                                adding.setAmount(1);
                                int index =mh.getIndexOfSlot(event.getRawSlot());
                                if (index > -1)
                                {
                                    mh.slotsSending.remove(index);

                                }
                                mh.slotsSending.add(new SendingSlotHolder(event.getRawSlot(), adding));
                                updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                            }
                            else
                            {
                                mh.tmpSlot = -1;
                                updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                            }
                        }
                    }
                }
                else
                {
                    if (mh.getMode().equalsIgnoreCase("edit"))
                    {
                        mh.setEditingSlot(event.getRawSlot());
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                    if (mh.getMode().equalsIgnoreCase("pulling"))
                    {
                        mh.slotsPulling.remove((Integer)event.getRawSlot());
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                    if (mh.getMode().equalsIgnoreCase("sending"))
                    {
                        int index =mh.getIndexOfSlot(event.getRawSlot());
                        if (index > -1)
                        {
                            mh.slotsSending.remove(index);

                        }
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                    if (mh.getMode().equalsIgnoreCase("items"))
                    {
                        mh.tmpSlot = -1;
                        int index =mh.getIndexOfSlot(event.getRawSlot());
                        if (index > -1)
                        {
                            mh.slotsSending.remove(index);
                            mh.slotsSending.add(new SendingSlotHolder(event.getRawSlot(), null));
                        }
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                }

            }
            if (event.getRawSlot() > 53 && event.getRawSlot() < 90)
            {
                if (mh.getMode().equalsIgnoreCase("items"))
                {
                    ItemStack fillerme = event.getClickedInventory().getItem(event.getSlot());
                    if (!TitanBox.isEmpty(fillerme)) {
                        mh.tmpSlot = event.getSlot();
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                    else
                    {
                        mh.tmpSlot = -1;
                        updateGUIClicked((Player) event.getWhoClicked(), mh, true);
                    }
                }
            }

        }
    }
}
