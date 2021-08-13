package dev.lightdream.unchartedcore.utils.init;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.databases.*;
import dev.lightdream.unchartedcore.files.config.SQL;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DatabaseUtils {

    private static Main plugin;

    private static SQL sqlConfig;
    private static ConnectionSource connectionSource;

    private static Dao<User, UUID> userDao;
    private static Dao<SignShop, Integer> signShopDao;
    private static Dao<StatSign, Integer> statSignDao;
    private static Dao<PlayerHead, Integer> playerHeadDao;
    private static Dao<Home, Integer> homeDao;
    private static Dao<FactionHomeAllow, Integer> factionHomeAllowDao;

    @Getter
    private static List<User> userList;
    @Getter
    private static List<SignShop> signShopList;
    @Getter
    private static List<StatSign> statSignsList;
    @Getter
    private static List<PlayerHead> playerHeadList;
    @Getter
    private static List<Home> homesList;
    @Getter
    private static List<FactionHomeAllow> factionHomeAllowList;

    public static void init(Main main) throws SQLException {
        plugin = main;
        sqlConfig = plugin.getSql();
        String databaseURL = getDatabaseURL();

        connectionSource = new JdbcConnectionSource(
                databaseURL,
                sqlConfig.username,
                sqlConfig.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, SignShop.class);
        TableUtils.createTableIfNotExists(connectionSource, PlayerHead.class);
        TableUtils.createTableIfNotExists(connectionSource, StatSign.class);
        TableUtils.createTableIfNotExists(connectionSource, Home.class);
        TableUtils.createTableIfNotExists(connectionSource, FactionHomeAllow.class);

        userDao = DaoManager.createDao(connectionSource, User.class);
        signShopDao = DaoManager.createDao(connectionSource, SignShop.class);
        playerHeadDao = DaoManager.createDao(connectionSource, PlayerHead.class);
        statSignDao = DaoManager.createDao(connectionSource, StatSign.class);
        homeDao = DaoManager.createDao(connectionSource, Home.class);
        factionHomeAllowDao = DaoManager.createDao(connectionSource, FactionHomeAllow.class);

        userDao.setAutoCommit(getDatabaseConnection(), false);
        signShopDao.setAutoCommit(getDatabaseConnection(), false);
        playerHeadDao.setAutoCommit(getDatabaseConnection(), false);
        statSignDao.setAutoCommit(getDatabaseConnection(), false);
        homeDao.setAutoCommit(getDatabaseConnection(), false);
        factionHomeAllowDao.setAutoCommit(getDatabaseConnection(), false);

        userList = getUsers();
        signShopList = getSignShops();
        playerHeadList = getPlayerHeads();
        statSignsList = getStatSigns();
        homesList = getHomes();
        factionHomeAllowList = getFactionHomeAllows();
    }

    private @NotNull
    static String getDatabaseURL() {
        switch (sqlConfig.driver) {
            case MYSQL:
            case MARIADB:
            case POSTGRESQL:
                return "jdbc:" + sqlConfig.driver.toString().toLowerCase() + "://" + sqlConfig.host + ":" + sqlConfig.port + "/" + sqlConfig.database + "?useSSL=" + sqlConfig.useSSL + "&autoReconnect=true";
            case SQLSERVER:
                return "jdbc:sqlserver://" + sqlConfig.host + ":" + sqlConfig.port + ";databaseName=" + sqlConfig.database;
            case H2:
                return "jdbc:h2:file:" + sqlConfig.database;
            case SQLITE:
                return "jdbc:sqlite:" + new File(plugin.getDataFolder(), sqlConfig.database + ".db");
            default:
                throw new UnsupportedOperationException("Unsupported driver (how did we get here?): " + sqlConfig.driver.name());
        }
    }

    private static DatabaseConnection getDatabaseConnection() throws SQLException {
        return connectionSource.getReadWriteConnection(null);
    }

    public @NotNull
    static List<User> getUsers() {
        try {
            return userDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static @NotNull List<SignShop> getSignShops() {
        try {
            return signShopDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static @NotNull List<PlayerHead> getPlayerHeads() {
        try {
            return playerHeadDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static @NotNull List<StatSign> getStatSigns() {
        try {
            return statSignDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static @NotNull List<Home> getHomes() {
        try {
            return homeDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static @NotNull List<FactionHomeAllow> getFactionHomeAllows() {
        try {
            return factionHomeAllowDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void saveUsers() {
        try {
            for (User user : userList) {
                userDao.createOrUpdate(user);
            }
            userDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void saveSignShops() {
        try {
            for (SignShop signShop : signShopList) {
                signShopDao.createOrUpdate(signShop);
            }
            signShopDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void savePlayerHeads() {
        try {
            for (PlayerHead playerHead : playerHeadList) {
                playerHeadDao.createOrUpdate(playerHead);
            }
            playerHeadDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void saveStatSigns() {
        try {
            for (StatSign statSign : statSignsList) {
                statSignDao.createOrUpdate(statSign);
            }
            statSignDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    public static void saveHomes() {
        try {
            for (Home home : homesList) {
                homeDao.createOrUpdate(home);
            }
            homeDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void saveFactionHomeAllows() {
        try {
            for (FactionHomeAllow allow : factionHomeAllowList) {
                factionHomeAllowDao.createOrUpdate(allow);
            }
            factionHomeAllowDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static @NotNull User getUser(@NotNull UUID uuid) {
        Optional<User> optionalUser = userList.stream().filter(user -> user.uuid.equals(uuid)).findFirst();

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User(uuid, Bukkit.getOfflinePlayer(uuid).getName());
        userList.add(user);
        return user;
    }

    public static @Nullable User getUser(@NotNull String name) {
        Optional<User> optionalUser = userList.stream().filter(user -> user.name.equals(name)).findFirst();

        return optionalUser.orElse(null);
    }

    public static @Nullable SignShop getSignShop(@NotNull Location location) {
        Optional<SignShop> optionalSignShop = signShopList.stream().filter(signShop -> signShop.getLocation().equals(location)).findFirst();

        return optionalSignShop.orElse(null);
    }

    public static @Nullable PlayerHead getPlayerHead(int id) {
        Optional<PlayerHead> optionalPlayerHead = playerHeadList.stream().filter(playerHead -> playerHead.id == id).findFirst();

        return optionalPlayerHead.orElse(null);
    }

    public static @Nullable PlayerHead getPlayerHead(Location location) {
        Optional<PlayerHead> optionalPlayerHead = playerHeadList.stream().filter(playerHead -> playerHead.onLocation(location)).findFirst();

        return optionalPlayerHead.orElse(null);
    }

    public static @Nullable StatSign getStatSign(@NotNull Location location) {
        Optional<StatSign> optionalStatSign = statSignsList.stream().filter(statSign -> statSign.getLocation().equals(location)).findFirst();

        return optionalStatSign.orElse(null);
    }

    public static @Nullable Home getHome(User user, String name) {
        Optional<Home> optionalHome = homesList.stream().filter(home -> home.owner.equals(user.uuid) && home.name.equals(name)).findFirst();

        return optionalHome.orElse(null);
    }

    public static @NotNull List<Home> getHomes(User user) {
        return homesList.stream().filter(home -> home.owner.equals(user.uuid)).collect(Collectors.toList());
    }

    public static Boolean canHaveHome(String id1, String id2) {
        Optional<FactionHomeAllow> optionalFactionHomeAllow = factionHomeAllowList.stream().filter(allow -> allow.factionId.equals(id1) && allow.allowId.equals(id2)).findFirst();

        return optionalFactionHomeAllow.isPresent();
    }

    public static FactionHomeAllow getFactionHomeAllow(String id1, String id2) {
        Optional<FactionHomeAllow> optionalFactionHomeAllow = factionHomeAllowList.stream().filter(allow -> allow.factionId.equals(id1) && allow.allowId.equals(id2)).findFirst();

        return optionalFactionHomeAllow.orElse(null);
    }

    public static List<FactionHomeAllow> getFactionHomeAllows(String id1) {
        return factionHomeAllowList.stream().filter(allow -> allow.factionId.equals(id1)).collect(Collectors.toList());
    }

}