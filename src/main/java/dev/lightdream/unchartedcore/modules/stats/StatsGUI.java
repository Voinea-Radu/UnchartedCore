package dev.lightdream.unchartedcore.modules.stats;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.files.dto.GUIConfig;
import dev.lightdream.unchartedcore.files.dto.Item;
import dev.lightdream.unchartedcore.utils.ItemBuilder;
import dev.lightdream.unchartedcore.utils.Utils;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatsGUI implements InventoryProvider {

    private static GUIConfig config;

    public static SmartInventory getInventory() {
        config = StatsModule.instance.settings.guiConfig;
        return SmartInventory.builder()
                .id(config.id)
                .provider(new StatsGUI())
                .size(config.rows, config.columns)
                .title(config.title)
                .type(InventoryType.valueOf(config.type))
                .parent(null)
                .manager(Main.instance.getInventoryManager())
                .build();
    }

    public static String parse(String raw, User user) {

        if (raw == null) {
            return raw;
        }

        String parsed = raw;

        parsed = parsed.replace("%player%", user.name);
        parsed = parsed.replace("%kills%", String.valueOf(user.kills));
        parsed = parsed.replace("%deaths%", String.valueOf(user.deaths));
        Long millis = user.onlineTime;
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        String onlineTime = days + "d " + hours + "h " + minutes + "m " + seconds + "s";
        parsed = parsed.replace("%online_time%", onlineTime);
        parsed = parsed.replace("%traveled%", String.valueOf(Math.floor(user.traveledDistance)));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(user.joinDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String joinDate = day + ":" + month + ":" + year;
        parsed = parsed.replace("%join_date%", joinDate);

        return parsed;
    }

    public static List<String> parse(List<String> raw, User user) {
        if (raw == null) {
            return raw;
        }
        List<String> parsed = new ArrayList<>();
        raw.forEach(line -> parsed.add(parse(line, user)));
        return parsed;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(ItemBuilder.makeItem(config.fillItem)));
        User user = DatabaseUtils.getUser(player.getUniqueId());
        config.items.forEach(item -> {
            Item builder = item.item.clone();
            builder.displayName = StatsGUI.parse(builder.displayName, user);
            builder.lore = StatsGUI.parse(builder.lore, user);
            builder.headOwner = StatsGUI.parse(builder.headOwner, user);
            contents.set(Utils.getSlotPosition(item.item.slot), ClickableItem.empty(ItemBuilder.makeItem(builder)));
        });
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }


}
