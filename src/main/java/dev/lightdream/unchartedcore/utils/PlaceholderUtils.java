package dev.lightdream.unchartedcore.utils;

import dev.lightdream.unchartedcore.Main;
import dev.lightdream.unchartedcore.databases.User;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PlaceholderUtils {

    public static String parse(String raw, User user) {
        String parsed = raw;

        if (user != null) {
            parsed = parsed.replace("%player%", user.name);
        }

        return parsed;
    }

    public static List<String> parse(List<String> raw, User user) {
        List<String> parsed = new ArrayList<>();

        for (String line : raw) {
            parsed.add(parse(line, user));
        }

        return parsed;
    }

}
