package dev.lightdream.unchartedcore.files.config;

import dev.lightdream.unchartedcore.Main;
import lombok.NoArgsConstructor;

@SuppressWarnings("unused")
@NoArgsConstructor
public class SQL {

    public Driver driver = Driver.SQLITE;
    public String host = "localhost";
    public String database = Main.PROJECT_NAME;
    public String username = "";
    public String password = "";
    public int port = 3306;
    public boolean useSSL = false;

    public enum Driver {
        MYSQL,
        MARIADB,
        SQLSERVER,
        POSTGRESQL,
        H2,
        SQLITE
    }

}
