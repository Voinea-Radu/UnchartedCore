package dev.lightdream.unchartedcore.databases;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.files.dto.SignShopEntry;
import dev.lightdream.unchartedcore.modules.signshop.SignShopModule;
import dev.lightdream.unchartedcore.utils.Utils;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

@DatabaseTable(tableName = "sign_shops")
@NoArgsConstructor
public class SignShop {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    public int id;
    @DatabaseField(columnName = "world")
    public String world;
    @DatabaseField(columnName = "x")
    public int x;
    @DatabaseField(columnName = "y")
    public int y;
    @DatabaseField(columnName = "z")
    public int z;
    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "item")
    public String item;

    /**
     * @param world World name
     * @param x     World coordinate X
     * @param y     World coordinate Y
     * @param z     World coordinate Z
     * @param type  SELL/BUY
     * @param item  Material
     */
    public SignShop(String world, int x, int y, int z, String type, String item) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.item = item;
    }

    public SignShop(Location location, String type, String item) {
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.type = type;
        this.item = item;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    /**
     * @return SignShopEntry containing buy/sell price and material
     */
    public SignShopEntry getDetails() {
        try {
            int id = Integer.parseInt(item);
            return SignShopModule.instance.settings.signShopEntries.getOrDefault(id, null);
        } catch (NumberFormatException e) {
            return SignShopModule.instance.settings.getEntryByMaterial(item);
        }
    }

    public void process() {
        Main plugin = Main.instance;
        SignShopEntry data = this.getDetails();
        if (data == null) {
            plugin.getLogger().warning("Sign shop at location " + getLocation() + " has no config associated to it");
            return;
        }
        Block block = Bukkit.getWorld(this.world).getBlockAt(this.getLocation());
        if (!block.getType().equals(Material.SIGN) && !block.getType().equals(Material.WALL_SIGN)) {
            plugin.getLogger().warning("Sign shop at location " + getLocation() + " is not a sign");
            return;
        }
        Sign sign = (Sign) block.getState();

        switch (this.type) {
            case "BUY":
                sign.setLine(0, Utils.color(plugin.getMessages().signBuy));
                sign.setLine(2, Utils.color(plugin.getMessages().signPrice.replace("%price%", String.valueOf(data.buyPrice))));
                sign.setLine(3, Utils.color(plugin.getMessages().signBuyInfo));
                break;
            case "SELL":
                sign.setLine(0, Utils.color(plugin.getMessages().signSell));
                sign.setLine(2, Utils.color(plugin.getMessages().signPrice.replace("%price%", String.valueOf(data.sellPrice))));
                sign.setLine(3, Utils.color(plugin.getMessages().signSellInfo));
                break;
        }
        sign.setLine(1, Utils.color(plugin.getMessages().signItem.replace("%item%", data.material.toString())));
        sign.update(true);
    }
}
