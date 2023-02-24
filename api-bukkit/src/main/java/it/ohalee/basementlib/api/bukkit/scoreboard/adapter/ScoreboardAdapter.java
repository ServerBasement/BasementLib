package it.ohalee.basementlib.api.bukkit.scoreboard.adapter;

import it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ScoreboardAdapter {

    public static Builder builder(JavaPlugin javaPlugin, ScoreboardUtils scoreboardManager) {
        return new Builder(javaPlugin, scoreboardManager);
    }

    public abstract int getCharactersLimits();

    public abstract ScoreboardUtils getPacketWrapper();

    public abstract JavaPlugin getJavaPlugin();

    public abstract void create(String title, Player player);

    public abstract void destroy(Player player);

    public abstract void showLine(Player viewer, int row, String team, String prefix, String suffix);

    public abstract void destroyLine(Player viewer, String team);

    public abstract void updateLine(Player viewer, String oldValue, String newValue, int row);

    public static class Builder {
        private final JavaPlugin javaPlugin;
        private final ScoreboardUtils packetWrapper;


        private Builder(JavaPlugin javaPlugin, ScoreboardUtils packetWrapper) {
            this.javaPlugin = javaPlugin;
            this.packetWrapper = packetWrapper;
        }

        public ScoreboardAdapter build() {
            return new ScoreboardAdapter() {
                @Override
                public JavaPlugin getJavaPlugin() {
                    return javaPlugin;
                }


                @Override
                public ScoreboardUtils getPacketWrapper() {
                    return packetWrapper;
                }

                @Override
                public int getCharactersLimits() {
                    return packetWrapper.getCharactersLimits();
                }

                @Override
                public void create(String title, Player viewer) {
                    packetWrapper.sendPackets(viewer, packetWrapper.createObjectivePacket(0, viewer.getName(), title));
                    packetWrapper.sendPackets(viewer, packetWrapper.createDisplayObjectivePacket(viewer.getName()));
                }

                @Override
                public void destroy(Player viewer) {
                    packetWrapper.sendPackets(viewer, packetWrapper.createObjectivePacket(1, viewer.getName(), null));
                }

                @Override
                public void showLine(Player viewer, int row, String team, String prefix, String suffix) {
                    packetWrapper.sendPackets(viewer, packetWrapper.getCreateTeamPacket(team, prefix, suffix));
                    packetWrapper.sendPackets(viewer, packetWrapper.createScorePacket(viewer.getName(), prefix + suffix, 15 - row));
                }

                @Override
                public void destroyLine(Player viewer, String value) {
                    packetWrapper.sendPackets(viewer, packetWrapper.destroyScorePacket(value));
                }

                @Override
                public void updateLine(Player viewer, String oldValue, String newValue, int row) {
                    packetWrapper.sendPackets(viewer,
                            packetWrapper.destroyScorePacket(oldValue),
                            packetWrapper.createScorePacket(viewer.getName(), newValue, 15 - row));
                }

            };
        }
    }
}
