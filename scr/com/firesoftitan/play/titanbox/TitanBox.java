package com.firesoftitan.play.titanbox;

import com.firesoftitan.play.titanbox.enums.ItemEnum;
import com.firesoftitan.play.titanbox.enums.ModuleTypeEnum;
import com.firesoftitan.play.titanbox.guis.DataCenterGUI;
import com.firesoftitan.play.titanbox.guis.MailboxGUI;
import com.firesoftitan.play.titanbox.guis.PickAPlayerGUI;
import com.firesoftitan.play.titanbox.items.ChestMover;
import com.firesoftitan.play.titanbox.listeners.*;
import com.firesoftitan.play.titanbox.machines.*;
import com.firesoftitan.play.titanbox.managers.*;
import com.firesoftitan.play.titanbox.managers.protection.ForceFieldManager;
import com.firesoftitan.play.titanbox.managers.protection.PlayerProtectionManager;
import com.firesoftitan.play.titanbox.managers.protection.WorldManager;
import com.firesoftitan.play.titanbox.modules.MainModule;
import com.firesoftitan.play.titanbox.runnables.*;
import com.firesoftitan.play.titansql.CallbackResults;
import com.firesoftitan.play.titansql.DataTypeEnum;
import com.firesoftitan.play.titansql.ResultData;
import com.firesoftitan.play.titansql.Table;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.minecraft.server.v1_14_R1.MinecraftServer;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

public class TitanBox extends JavaPlugin
{
    public static SlimefunSetup setup;
    public static TitanBox instants;

    public static MainListener listen;
    public static ArmorListener armorListener;
    public static ProtectionListener prolisten;
    public static XRayListener xraylisten;
    public static ShopListener shoplisten;

    public static Config settings = new Config("plugins" + File.separator + "TitanBox" + File.separator  + "settings.yml");

    public static Table mailbox_SQL;

    private RouterRunable RouterTimer = new RouterRunable();
    private SaverRunable SaverTimer = new SaverRunable();

    public static List<UUID> bypassCommands = new ArrayList<UUID>();
    public static List<UUID> bypassProtection = new ArrayList<UUID>();

    public List<ForceFieldManager> adminList = new ArrayList<ForceFieldManager>();

    public HashMap<UUID, PickAPlayerGUI> pickPlayer = new HashMap<UUID, PickAPlayerGUI>();
    public HashMap<UUID, ChatGetterRunnable> chatPlayer = new HashMap<UUID, ChatGetterRunnable>();

    public static List<String> packagesIDS = new ArrayList<String>();

    public ChestMover chestMover = new ChestMover();

    public static long isRunning = 0;

    public UpdateChecker updateChecker;

    public void getNextMessage(Player player, ChatGetterRunnable chatGetterRunnable, String message)
    {
        this.chatPlayer.put(player.getUniqueId(), chatGetterRunnable);
        player.sendMessage(ChatColor.AQUA + message);
    }

    public void onEnable()
    {
        instants = this;

        listen = new MainListener();
        listen.registerEvents();

        prolisten = new ProtectionListener();
        prolisten.registerEvents();

        armorListener = new ArmorListener();
        armorListener.registerEvents();

        xraylisten = new XRayListener();
        xraylisten.registerEvents();
        XRayManager.loadConfig();

        EggsManager.loadConfig();

        shoplisten = new ShopListener();
        shoplisten.registerEvents();

        updateChecker = new UpdateChecker(this, "65264");
        updateChecker.runTaskLater(this, 3*20);

        loadConfig();

        ConfigManager.loadConfig();

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();

        NPCManager.load();

        WorldManager.setupTable();

        setup = new SlimefunSetup();

        WorldManager.loadProtection();

        DataCenterGUI.load_config();

        setupTable();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MainRunnable(),25, 25);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ForceFieldRunnable(), 15, 15);

        Timer getPlayers = new Timer();
        getPlayers.schedule(new TimerTask() {
            @Override
            public void run() {
                PickAPlayerGUI pickAPlayerGUI = new PickAPlayerGUI(null, new PickAPlayerRunnable() {
                    @Override
                    public void run() {
                        //this is to remove the lag for the first time someone us it.
                    }
                });
                pickAPlayerGUI.buildGUI(0);
                pickAPlayerGUI = null;
            }
        }, 10);



    }
    public static void runCommands(Player player, int amount, Location location, List<String> commands)
    {
        for(String command: commands)
        {
            runCommand(player, amount, location, command);
        }
    }
    public static void runCommand(Player player, int amount, Location location, String commands)
    {
        commands = replaceAllPlaceHolders(player, amount, location, commands);
        if (commands.startsWith("@player "))
        {
            commands = commands.replace("@player ", "");
            player.sendMessage(commands);
        }
        else
        {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands);
        }
    }
    public static String replaceAllPlaceHolders(Player player, int amount, Location location, String command)
    {
        if (player != null) {
            command = command.replace("<name>", player.getName());
            command = command.replace("<uuid>", player.getUniqueId().toString());
        }
        if (location != null) {
            command = command.replace("<x>", location.getBlockX() + "");
            command = command.replace("<y>", location.getBlockY() + "");
            command = command.replace("<z>", location.getBlockZ() + "");
            command = command.replace("<world>", location.getWorld().getName());
        }
        command = command.replace("<amount>", amount +"");
        command = ChatColor.translateAlternateColorCodes('&', command);
        return command;
    }
    public static void setupTable()
    {
        mailbox_SQL = new Table("tb_mailbox");
        mailbox_SQL.addDataType("id", DataTypeEnum.CHARARRAY, true, false, true);
        mailbox_SQL.addDataType("topost", DataTypeEnum.UUID, false, false, false);
        mailbox_SQL.addDataType("frompost", DataTypeEnum.UUID, false, false, false);
        mailbox_SQL.addDataType("sentpost", DataTypeEnum.LONG, false, false, false);
        mailbox_SQL.addDataType("item", DataTypeEnum.ITEMSTACK, false, false, false);
        mailbox_SQL.addDataType("bulk", DataTypeEnum.BOOLEAN, false, false, false);
        mailbox_SQL.addDataType("received", DataTypeEnum.STRINGLIST, false, false, false);
        mailbox_SQL.createTable();

        mailbox_SQL.search(new CallbackResults() {
            @Override
            public void onResult(List<HashMap<String, ResultData>> results) {
                if (results != null && results.size() > 0) {
                    for(int i = 0; i < results.size(); i++)
                    {
                        HashMap<String, ResultData> vaules = results.get(i);
                        String id =vaules.get("id").getString();
                        TitanBox.packagesIDS.add(id);

                        UUID to = vaules.get("topost").getUUID();
                        UUID from = vaules.get("frompost").getUUID();
                        long sent = vaules.get("sentpost").getLong();
                        ItemStack item = vaules.get("item").getItemStack();

                        if (vaules.get("bulk").getBoolean() == null || !vaules.get("bulk").getBoolean()) {
                            MailboxGUI mailboxGUI = MailboxGUI.getMailBox(to);
                            mailboxGUI.loadData(vaules);
                        }
                        else {
                            List<String> uuidsToBe = vaules.get("received").getStringList();
                            BulkPackageManager packageHolder = new BulkPackageManager(from, to, item.clone(), sent, id);
                            if (uuidsToBe != null) {
                                for (String uuidS : uuidsToBe) {
                                    packageHolder.playerReceived(UUID.fromString(uuidS));
                                }
                            }
                            BulkPackageManager.addNewPackage(packageHolder);


                        }
                    }
                }
                for(OfflinePlayer offlinePlayer: Bukkit.getOfflinePlayers())
                {
                    MailboxGUI.getMailBox(offlinePlayer.getUniqueId());

                }
            }
        });
    }

    public boolean checkforTitanStone(ItemStack itemA)
    {
        if (itemA != null) {
            if (itemA.getItemMeta() != null) {
                if (itemA.getItemMeta().hasDisplayName() && itemA.getItemMeta().hasLore()) {
                    if (itemA.getItemMeta().getDisplayName().equals(SlimefunItemsManager.TitanStone.getItemMeta().getDisplayName())) {
                        if (Utilities.equalsLore(itemA.getItemMeta().getLore(), SlimefunItemsManager.TitanStone.getItemMeta().getLore())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    private void loadConfig()
    {
        //ChargableBlock.registerChargableBlock("FOTStorageUnti", 100, true);
        EnergyNet.registerComponent("FOTStorageUnti", EnergyNet.NetworkComponent.CONSUMER);

        StorageUnit.loadStorage();

//        MainModule.loadAllModules();

        //RouterManager.loadAllRouters();

        Elevator.loadAllElevators();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, SaverTimer, 12000, 12000);

        //Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, RouterTimer, 200, RouterManager.speed);

    }
    public void checkRegisterdPower()
    {
        if (EnergyNet.getComponent("FOTStorageUnti") == null)
        {
            EnergyNet.registerComponent("FOTStorageUnti", EnergyNet.NetworkComponent.CONSUMER);
        }
    }
    public boolean isInField(Location ofBlock) {
        ForceFieldManager ffH = WorldManager.getWorldHolder(ofBlock.getWorld()).getFieldIn(ofBlock);
        if (ffH != null)
        {
            return true;
        }
        return false;
    }
    public boolean isBlockPlayer(Location ofBlock, Player player) {
        if (player == null) return false;
        ForceFieldManager ffH = WorldManager.getWorldHolder(ofBlock.getWorld()).getFieldIn(ofBlock);
        if (ffH != null)
        {
            if (!ffH.hasRights(player.getUniqueId()))
            {
                return true;
            }
        }
        return false;
    }
    private void setDefault(String name) {


    }

    public void saveEveryThing()
    {
        ItemRecovery.instance.kickOutProcessing();
        DeathRecovery.instance.kickOutProcessing();
        for(WorldManager worldManager : WorldManager.getAll())
        {
            worldManager.Save();
        }
        StorageUnit.saveStorage();
        Pumps.savePumps();
        Elevator.saveElevators();
        BackpackRecover.saveRecovers();
        MainModule.saveALL();
        ItemRoutingRouter.saveAll();

        MailboxGUI.saveALL();

        BulkPackageManager.saveAll();

        BarcodeManager.save();

        ConfigManager.save();
        settings.save();
    }

    public void onDisable()
    {
        System.out.println("[TitanBox]: Disabling...");
        ejectEveryThing();
        saveEveryThing();
        System.out.println("[TitanBox]: Disabled.");
    }
    public void ejectEveryThing()
    {
        List<Block> removeme = new ArrayList<Block>();
        for(Block block: AContainer.processing.keySet())
        {
            ItemStack[] item = AContainer.processing.get(block).getOutput().clone();
            String id = BlockStorage.checkID(block);
            BlockMenu inventory = BlockStorage.getInventory(block);
            SlimefunItem slimefunItem = AContainer.getByID(id);
            if (slimefunItem instanceof AContainer)
            {
                AContainer container = (AContainer)slimefunItem;
                inventory.replaceExistingItem(22, new CustomItem(new MaterialData(Material.BLACK_STAINED_GLASS_PANE), " "));

                try {
                    Method method = AContainer.class.getDeclaredMethod("pushItems", new Class[]{Block.class, ItemStack[].class});
                    method.setAccessible(true);
                    method.invoke(container, block, item);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                removeme.add(block);
            }
        }
        for(Block block: removeme)
        {
            AContainer.progress.remove(block);
            AContainer.processing.remove(block);
        }
    }


    public static boolean checkStorageForItem(UUID owner,ItemStack toInsert)
    {
        toInsert = toInsert.clone();
        for (StorageUnit stH : StorageUnit.getStorageFromOwner(owner)) {
            if (stH.getOwner().toString().equals(owner.toString())) {
                for (int i =0; i < stH.getSize(); i++) {
                    ItemStack stack = stH.viewSlot(i);
                    if (Utilities.isItemEqual(toInsert, stack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<ItemStack> loadListItemStack(Config config, String key)
    {
        List<ItemStack> goingOut = new ArrayList<ItemStack>();
        try {
            List<?> items = config.getConfiguration().getList(key);
            for (Object obj: items)
            {
                if (obj instanceof  ItemStack)
                {
                    goingOut.add((ItemStack)obj);
                }
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
        }
        return  goingOut;
    }

    public static boolean isExpensive(ItemStack mat)
    {
        return isExpensive(mat.getType());
    }
    public static boolean isExpensive(Material mat)
    {
        switch (mat)
        {
            case BEDROCK:
                return true;
            case BEACON:
                return true;
            case SKELETON_SKULL:L:
                return true;
            case SKELETON_WALL_SKULL:
                return true;
            case BOOKSHELF:
                return true;
            case BOOK:
                return true;
            case WRITABLE_BOOK:
                return true;
            case ENCHANTED_BOOK:
                return true;
            case KNOWLEDGE_BOOK:
                return true;
            case WRITTEN_BOOK:
                return true;
            case ANVIL:
                return true;
            case POTION:
                return true;
            case SPAWNER:
                return true;
            case CHEST:
                return true;
            case DRAGON_EGG:
                return true;
            case DRAGON_BREATH:
                return true;
            case ENDER_CHEST:
                return true;
            case NETHER_STAR:
                return true;
            case FURNACE:
                return true;
            case CRAFTING_TABLE:
                return true;
            case DISPENSER:
                return true;
            case DROPPER:
                return true;
            case BUCKET:
                return true;
            case WATER_BUCKET:
                return true;
            case LAVA_BUCKET:
                return true;
            case BREWING_STAND:
                return true;
            case HOPPER:
                return true;
            case ENCHANTING_TABLE:
                return true;
            case EXPERIENCE_BOTTLE:
                return true;
            case TRIPWIRE_HOOK:
                return true;
        }
        if (mat.toString().contains("SKULL"))
        {
            return true;
        }
        if (mat.toString().contains("BOOK"))
        {
            return true;
        }
        if (mat.toString().contains("BED"))
        {
            return true;
        }
        if (mat.toString().contains("DOOR"))
        {
            return true;
        }
        if (mat.toString().contains("BLOCK"))
        {
            return true;
        }
        if (mat.toString().contains("ORE"))
        {
            return true;
        }
        if (mat.toString().contains("SHULKER"))
        {
            return true;
        }
        return false;
    }

    private String[] addRightEmptyLore(int size)
    {
        List<String> lore = new ArrayList<String>();
        String tmp = "";
        ChatColor chatColor = ChatColor.GRAY;
        lore.add(ChatColor.YELLOW + " " + ChatColor.STRIKETHROUGH + "---------------------------");
        for(int i = 0; i < size; i++)
        {
            tmp = tmp + chatColor+ "empty ";
            if (tmp.length() > 60)
            {
                lore.add(tmp);
                tmp = "";
            }
            if (chatColor.equals(ChatColor.GRAY))
            {
                chatColor = ChatColor.DARK_GRAY;
            }else {
                chatColor = chatColor.GRAY;
            }
        }
        if (!tmp.equals("")) {
            lore.add(tmp);
        }
        lore.add(ChatColor.BLUE + " " + ChatColor.STRIKETHROUGH + "---------------------------");
        lore.add(ChatColor.BLUE + "" + size + " J/s");
        String[] out = new String[lore.size()];
        out = lore.toArray(out);
        return out;
    }
    public static boolean isSuperItemHoler(ItemStack  toCheck)
    {
        if (!Utilities.isEmpty(toCheck)) {

            ItemMeta itemMeta = Utilities.getItemMeta(toCheck);
            if (itemMeta.hasDisplayName()) {
                if (itemMeta.getDisplayName().equals(ChatColor.YELLOW + "Super Item Mover"))
                {
                    return true;
                }
            }
            if (itemMeta.hasLore())
            {
                List<String> lore = itemMeta.getLore();
                for (String s : lore) {
                    if (s.startsWith(ChatColor.YELLOW + "   ") || ChatColor.stripColor(s).startsWith("   ")) {
                        return true;
                    }
                }
            }

        }
        return false;
    }
    public static Object[] returnSuperItemHolder(ItemStack SuperItemHolder)
    {
        if (!Utilities.isEmpty(SuperItemHolder)) {
            if (TitanBox.isSuperItemHoler(SuperItemHolder)) {
                SuperItemHolder = SuperItemHolder.clone();
                if (SuperItemHolder.hasItemMeta()) {
                    ItemMeta itemMeta = SuperItemHolder.getItemMeta();
                    if (itemMeta.hasLore()) {
                        String name = "error";
                        Long amount = Long.valueOf(0);
                        List<String> lore = itemMeta.getLore();
                        List<String> Reaklore = new ArrayList<String>();
                        for (String s : lore) {
                            String noColoerS = ChatColor.stripColor(s);
                            if (noColoerS.startsWith("   Name: ")) {
                                String value = s.replace(ChatColor.YELLOW + "   Name: " + ChatColor.WHITE, "");
                                name = value;
                            }
                            if (noColoerS.startsWith("   Barcode: ")) {
                                String value = noColoerS.replace("   Barcode: ", "");
                                amount = Long.valueOf(value);
                            }
                            if (!s.startsWith(ChatColor.YELLOW + "   ") && !noColoerS.startsWith("   ")) {
                                Reaklore.add(s);
                            }
                        }
                        ItemStack itemStackOut = new ItemStack(SuperItemHolder.getType(), 1, SuperItemHolder.getDurability());
                        if (!name.equalsIgnoreCase("normal")) {
                            itemStackOut = SuperItemHolder.clone();
                            itemStackOut.setAmount(1);
                            itemStackOut = Utilities.changeName(itemStackOut, name);
                        }
                        itemStackOut = Utilities.clearLore(itemStackOut);
                        if (Reaklore.size() > 0) {
                            itemStackOut = Utilities.addLore(itemStackOut, Reaklore);
                        }
                        Object[] tmpout = {itemStackOut.clone(), amount};
                        return tmpout;

                    }
                }
            }
        }
        return  null;
    }
    public static ItemStack getSuperItemHolder(ItemStack mat, Long amount)
    {
        String name = "Normal";
        if (!Utilities.isEmpty(mat))
        {
            if (!TitanBox.isSuperItemHoler(mat)) {
                mat = mat.clone();
                if (mat.hasItemMeta()) {
                    ItemMeta itemMeta = mat.getItemMeta();
                    if (itemMeta.hasDisplayName()) {
                        name = itemMeta.getDisplayName();
                    }
                }
                mat.setAmount(1);
                mat = Utilities.changeName(mat, ChatColor.YELLOW + "Super Item Mover");
                mat = Utilities.addLore(mat, ChatColor.YELLOW + "   Name: " + ChatColor.WHITE + name, ChatColor.YELLOW + "   Amount: " + ChatColor.WHITE + Utilities.format(amount), ChatColor.YELLOW + "   Barcode: " + ChatColor.MAGIC + amount);
                return mat.clone();
            }
        }
        return  null;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        try {
            if (sender instanceof Player) {
                if (label.equalsIgnoreCase("home")) {
                    PlayerProtectionManager playerProtectionManager = PlayerProtectionManager.getPlayer(((Player) sender).getUniqueId());
                    if (playerProtectionManager.count() > 0) {
                        List<ForceFieldManager> forceFieldManagers = new ArrayList<ForceFieldManager>(playerProtectionManager.getForceFields());
                        Location location = forceFieldManagers.get(0).getLocation().clone();
                        Utilities.startTeleport((Player) sender, location, 15);
                        ((Player) sender).closeInventory();
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You have no Force Fields to go home to.");
                    }
                }
                if (label.equalsIgnoreCase("override")) {
                    if (bypassProtection.contains(((Player) sender).getUniqueId()))
                    {
                        bypassProtection.remove(((Player) sender).getUniqueId());
                        sender.sendMessage(ChatColor.GREEN + "You are no longer over riding protections, this makes you sad =(");
                    }
                    else
                    {
                        bypassProtection.add(((Player) sender).getUniqueId());
                        sender.sendMessage(ChatColor.RED + "You are over riding ALL protections on the server, You fell the power flow threw you.");
                    }
                    return true;
                }
                if (label.equalsIgnoreCase("titanbox") || label.equalsIgnoreCase("tb")) {
                    if (args.length > 0) {

                        if (args[0].equalsIgnoreCase("info")) {
                            if (sender.hasPermission("titanbox.admin")) {
                                ItemStack mainHand = ((Player) sender).getInventory().getItemInMainHand();
                                if (!Utilities.isEmpty(mainHand))
                                {
                                    NBTTagCompound nbtTagCompound = Utilities.getNBTTag(mainHand);
                                    if (nbtTagCompound != null)
                                    {
                                        int i = 0;
                                        for(String key: nbtTagCompound.getKeys())
                                        {
                                            sender.sendMessage(i + ":" + key );
                                            sender.sendMessage(i + ":" + nbtTagCompound.get(key) );
                                            i++;
                                        }
                                    }
                                }
                            }
                        }
                        if (args[0].equalsIgnoreCase("save")) {
                            if (sender.hasPermission("titanbox.admin")) {
                                saveEveryThing();
                            }
                        }
                        if (args[0].equalsIgnoreCase("reload")) {
                            if (sender.hasPermission("titanbox.admin")) {
                                ConfigManager.reload();
                                ConfigManager.loadConfig();
                                settings.reload();
                                RouterManager.loadConfig();
                                sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Config reloaded, Server must be restart to update already loaded Modules!");
                            }
                        }
                        if (args[0].equalsIgnoreCase("restart")) {
                            if (sender.hasPermission("titanbox.admin")) {
                                if (System.currentTimeMillis() - TitanBox.isRunning > 1000) {
                                    Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, RouterTimer, 200, ConfigManager.getRouter_Speed());
                                    sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + "Restarted!");
                                }
                                sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.GREEN + Utilities.convertToTimePasted(TitanBox.isRunning));
                            }
                        }
                        if (args[0].equalsIgnoreCase("SIM")) {
                            if (sender.hasPermission("titanbox.admin")) {
                                Long amount = Long.valueOf(args[1]);
                                ItemStack toCheat = ((Player) sender).getInventory().getItemInMainHand();
                                if (!Utilities.isEmpty(toCheat)) {
                                    ItemStack gotit = TitanBox.getSuperItemHolder(toCheat, amount);
                                    if (!Utilities.isEmpty(gotit)) {
                                        ((Player) sender).getInventory().addItem(gotit.clone());
                                        sender.sendMessage(ChatColor.GREEN + "[TitanBox]: " + ChatColor.GREEN + "Done sir.");
                                    }
                                }

                            }
                        }
                        if (args[0].equalsIgnoreCase("give")) {
                            if (sender.hasPermission("titanbox.admin")) {
                                if (args.length > 2) {
                                    giveItems((Player) sender, args[1], args[2]);
                                } else {
                                    giveItems((Player) sender, args[1]);
                                }
                            }
                        }
                    }
                }

            }
            else
            {
                try {
                    if (label.equalsIgnoreCase("titanbox") || label.equalsIgnoreCase("tb")) {
                        if (args[0].equalsIgnoreCase("command")) {
                            Location location = new Location(Bukkit.getWorld(args[1]), 0, 123,0);
                            String command = "";
                            for (int i = 2; i < args.length; i++)
                            {
                                command = args[i] + " ";
                            }
                            NPCManager.sendCommand(location, command);
                        }
                        if (args[0].equalsIgnoreCase("give")) {
                            Player player = Bukkit.getPlayer(args[args.length - 1]);
                            if (args.length > 2)
                            {
                                giveItems(player, args[1], args[2]);
                            }
                            else {
                                giveItems(player, args[1]);
                            }

                            System.out.println("[TitanBox]: " +"Item Sent!");
                        }
                        if (args[0].equalsIgnoreCase("restart"))
                        {
                            if (System.currentTimeMillis() - TitanBox.isRunning > 1000) {
                                Bukkit.getScheduler().scheduleSyncRepeatingTask(TitanBox.instants, RouterTimer, 200, ConfigManager.getRouter_Speed());
                                sender.sendMessage( "[TitanBox]: " + "Restarted!");
                            }
                            sender.sendMessage("[TitanBox]: " + Utilities.convertToTimePasted(TitanBox.isRunning));
                        }
                    }
                }
                catch (Exception e)
                {
                    System.out.println("[TitanBox]: " + "Error try /tb give item player");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
    public static boolean isUpgradeDevice(Player player, ItemStack item )
    {
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW + "Upgrade Device")) {
                        if (!BarcodeManager.hasBarcode(item))
                        {
                            //"This is an invalid device! Most likely an old one.");
                            return false;
                        }
                        String barcode = BarcodeManager.scanBarcode(item);
                        if (barcode == null)
                        {
                            // "This is an invalid device! Most likely an old one.");
                            return false;
                        }
                        boolean barcodeTrue = Boolean.valueOf(barcode);
                        if (barcodeTrue)
                        {
                            TitanBox.duppedAlert(player, item);
                            return false;
                        }
                        BarcodeManager.setBarcodeTrue(item, player);
                        //"Upgrade completed! " + ChatColor.WHITE  + tmp.getSize() + ChatColor.GREEN + "/" + ChatColor.WHITE + 45);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public ItemStack getItem(String whatItem)
    {
        return getItem(whatItem, null);
    }
    public ItemStack getItem(String whatItem, String sub)
    {
            return getItem(whatItem, sub, null);
    }
    public ItemStack getItem(String whatItem, String sub, Player giveFor)
    {
        if (whatItem.equalsIgnoreCase("a") || whatItem.equalsIgnoreCase("b") ||whatItem.equalsIgnoreCase("c") || whatItem.equalsIgnoreCase("d") || whatItem.equalsIgnoreCase("e"))
        {
            try {
                ItemEnum me = ItemEnum.valueOf("UNIT_" + whatItem.toUpperCase());
                if (me != null) {
                    ItemStack placeMe = me.getItem();
                    placeMe = Utilities.changeName(placeMe, ChatColor.YELLOW + "New Storage Unit, Size: " + me.getSize());
                    placeMe = Utilities.addLore(placeMe, addRightEmptyLore(me.getSize()));
                    return  placeMe.clone();
                }
            } catch (Exception e) {

            }
        }
        if (whatItem.equalsIgnoreCase("network"))
        {
            ItemStack placeMe = NetworkMonitor.getMeAsDrop();
            return  placeMe.clone();

        }
        if (whatItem.equalsIgnoreCase("module"))
        {
            ModuleTypeEnum type = ModuleTypeEnum.Inventory;
            if (sub !=null)
            {
                type = ModuleTypeEnum.valueOf(sub);
            }
            ItemStack placeMe = MainModule.getNewModule(type);
            return  placeMe.clone();

        }
        if (whatItem.equalsIgnoreCase("router"))
        {
            ItemEnum me = ItemEnum.ROUTER;
            if (me != null) {
                ItemStack placeMe = me.getItem();
                placeMe = Utilities.changeName(placeMe, ChatColor.YELLOW + "Item Routing Router");
                placeMe = Utilities.addLore(placeMe,  "Links: " + ChatColor.WHITE + "Slimefun, Chest, and Storage units", ChatColor.WHITE + "45 J/s");

                return  placeMe.clone();
            }
        }
        if (whatItem.equalsIgnoreCase("upgrade"))
        {
            ItemEnum me = ItemEnum.UPGRADE;
            if (me != null && giveFor != null) {
                ItemStack placeMe = me.getItem();
                placeMe = Utilities.changeName(placeMe, ChatColor.YELLOW + "Upgrade Device");
                placeMe = Utilities.addLore(placeMe,  "Used On: " + ChatColor.WHITE + "Storage Unit, and Routers", ChatColor.WHITE + "Hold in main hand and click block thats placed!");
                placeMe = BarcodeManager.getNewBarcode(placeMe);
                placeMe = Utilities.addLore(placeMe, ChatColor.YELLOW  + "User: " + ChatColor.WHITE + giveFor.getName(), ChatColor.GRAY + "If this item is dupped the above user will be banned!");
                return  placeMe.clone();
            }
        }
        if (whatItem.equalsIgnoreCase("placer") ||whatItem.equalsIgnoreCase("killer") || whatItem.equalsIgnoreCase("water") || whatItem.equalsIgnoreCase("lava") || whatItem.equalsIgnoreCase("ice") || whatItem.equalsIgnoreCase("item"))
        {
            ItemStack pump = Pumps.getMeAsDrop(whatItem);
            return  pump.clone();
        }
        if (whatItem.equalsIgnoreCase("elevator"))
        {
            ItemStack elevator = Elevator.getMeAsDrop();
            return  elevator.clone();

        }
        if (whatItem.equalsIgnoreCase("backpack"))
        {
            ItemStack elevator = BackpackRecover.getMeAsDrop();
            return  elevator.clone();

        }
        if (whatItem.equalsIgnoreCase("storage"))
        {
            ItemStack elevator = StorageRecover.getMeAsDrop();
            return  elevator.clone();

        }
        return null;
    }
    public static void duppedAlert(Player player, ItemStack itemStack)
    {
        player.sendMessage(ChatColor.RED + "[TitanBox]: " + ChatColor.GREEN + "This is an invalid device! It has been duped and you have been reported.");
        String message = "";
        message = message  + "-----StartReport-----" + "<br>";
        message = message  + player.getName() + " has used a dupped Upgrade Device!" + "<br>";
        message = message  + "" + "<br>";
        if (itemStack.hasItemMeta())
        {
            if (itemStack.getItemMeta().hasDisplayName())
            {
                message = message  + "Item: " + itemStack.getItemMeta().getDisplayName() + "<br>";
            }
            if (itemStack.getItemMeta().hasLore())
            {
                int i = 0;
                for (String lore: itemStack.getItemMeta().getLore())
                {
                    i++;
                    String list = "Line " + i + ": " + ChatColor.stripColor(lore);
                    message = message  + list + "<br>";
                }
            }
        }
        message = message  + "-----EndReport-----" + "<br>";
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(ConfigManager.getAdminAlerts());
        if (offlinePlayer != null) {
            MailboxGUI.mailLetter(player, offlinePlayer.getUniqueId(), message);
        }

    }
    public void giveItems(Player sender, String whatItem) {
        giveItems(sender, whatItem, null);
    }
    public void giveItems(Player sender, String whatItem, String sub) {
        ItemStack item = getItem(whatItem,sub, sender);
        if (!Utilities.isEmpty(item)) {
            sender.getInventory().addItem(item);
        }
    }


}

/* Location:           D:\Daniel\Downloads\WildTp-1.2.jar
 * Qualified Name:     MainClass
 * JD-Core Version:    0.6.2
 */