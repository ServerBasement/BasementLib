package it.ohalee.basementlib.bukkit.placeholders;

import it.ohalee.basementlib.api.BasementLib;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class BasementPlaceholder extends PlaceholderExpansion {

    private final BasementLib basement;

    public BasementPlaceholder(BasementLib basement) {
        this.basement = basement;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "basement";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ohAlee";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        //Usage: %basement_counter_SERVER%
        if (params.startsWith("counter_")) {
            if (basement.serverManager() == null) return "REDIS NOT CONNECTED OR NOT ENABLED";
            return Integer.toString(basement.serverManager().getOnlinePlayers(params.replaceFirst("counter_", "")));
        }
        return null;
    }
}
