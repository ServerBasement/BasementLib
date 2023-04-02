package it.ohalee.basementlib.api.bukkit.chat;

import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Colorizer {

    @Setter
    private static ColorAdapter adapter = new ColorAdapter();

    private Colorizer() {
        throw new AssertionError("Nope");
    }

    public static String colorize(String string) {
        return adapter.translateHex(ChatColor.translateAlternateColorCodes('&', string));
    }

    public static List<String> colorize(List<String> strings) {
        return strings.stream().map(Colorizer::colorize).collect(Collectors.toList());
    }

    public static List<String> colorize(String... strings) {
        return Arrays.stream(strings).map(Colorizer::colorize).collect(Collectors.toList());
    }

    public static String translateHex(String msg) {
        return adapter.translateHex(msg);
    }

    public static class ColorAdapter {

        public String translateHex(String msg) {
            return msg;
        }

    }

}
