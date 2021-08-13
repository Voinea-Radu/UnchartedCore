package dev.lightdream.unchartedcore.modules.homes;

import com.massivecraft.factions.*;
import com.massivecraft.factions.perms.Role;
import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.commands.Command;
import dev.lightdream.unchartedcore.databases.Home;
import dev.lightdream.unchartedcore.modules.CoreModule;
import dev.lightdream.unchartedcore.utils.init.DatabaseUtils;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomesModule extends CoreModule {

    public HomesModule(Main plugin) {
        super(plugin, "TempHomesModule");
    }

    @Override
    public Listener registerEventListeners() {
        return null;
    }

    @Override
    public List<Command> registerCommands() {
        return Arrays.asList(
                new HomeCommand(plugin),
                new AllowHomeCommand(plugin),
                new AddHomeCommand(plugin)
        );
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        List<Home> toRemove = new ArrayList<>();
        for (Home home : DatabaseUtils.getHomesList()) {
            FLocation fLocation = new FLocation(home.getLocation());
            FPlayer fPlayer = FPlayers.getInstance().getById(home.owner.toString());
            Faction playerFaction = fPlayer.getFaction();
            Faction positionFaction = Board.getInstance().getFactionAt(fLocation);
            if (positionFaction.isWilderness()) {
                continue;
            }
            if (positionFaction == playerFaction) {
                if (fPlayer.getRole().isAtLeast(Role.COLEADER)) {
                    continue;
                }
                if (DatabaseUtils.canHaveHome(playerFaction.getId(), fPlayer.getRole().name())) {
                    continue;
                }
            }
            if (DatabaseUtils.canHaveHome(playerFaction.getId(), positionFaction.getId())) {
                continue;
            }
            toRemove.add(home);
        }
        toRemove.forEach(home -> DatabaseUtils.getHomesList().remove(home));
        DatabaseUtils.saveHomes();
        DatabaseUtils.saveFactionHomeAllows();
    }

    @Override
    public void loadConfigs() {

    }
}
