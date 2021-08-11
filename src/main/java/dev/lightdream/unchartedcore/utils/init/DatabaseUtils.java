package dev.lightdream.unchartedcore.utils.init;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.databases.SignShop;
import dev.lightdream.unchartedcore.databases.User;
import dev.lightdream.unchartedcore.files.config.SQL;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class DatabaseUtils {

    private static Main plugin;

    private static SQL sqlConfig;
    private static ConnectionSource connectionSource;

    private static Dao<User, UUID> userDao;
    private static Dao<SignShop, Integer> signShopDao;

    @Getter
    private static List<User> userList;
    @Getter
    private static List<SignShop> signShopList;

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

        userDao = DaoManager.createDao(connectionSource, User.class);
        signShopDao = DaoManager.createDao(connectionSource, SignShop.class);

        userDao.setAutoCommit(getDatabaseConnection(), false);
        signShopDao.setAutoCommit(getDatabaseConnection(), false);

        userList = getUsers();
        signShopList = getSignShops();
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
        return Collections.emptyList();
    }

    public @NotNull
    static List<SignShop> getSignShops() {
        try {
            return signShopDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
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

    public static @NotNull User getUser(@NotNull UUID uuid) {
        Optional<User> optionalUser = getUserList().stream().filter(user -> user.uuid.equals(uuid)).findFirst();

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User(uuid, Bukkit.getOfflinePlayer(uuid).getName());
        userList.add(user);
        return user;
    }

    public static @Nullable User getUser(@NotNull String name) {
        Optional<User> optionalUser = getUserList().stream().filter(user -> user.name.equals(name)).findFirst();

        return optionalUser.orElse(null);
    }

    public static @Nullable SignShop getSignShop(@NotNull Location location) {
        Optional<SignShop> optionalSignShop = getSignShopList().stream().filter(signShop -> signShop.getLocation().equals(location)).findFirst();

        return optionalSignShop.orElse(null);
    }
}