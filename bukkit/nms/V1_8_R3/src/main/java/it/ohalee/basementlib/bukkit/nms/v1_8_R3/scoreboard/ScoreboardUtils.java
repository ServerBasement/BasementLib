package it.ohalee.basementlib.bukkit.nms.v1_8_R3.scoreboard;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Collections;

public class ScoreboardUtils implements it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardUtils {

    @Override
    public int getCharactersLimits() {
        return 16;
    }

    @Override
    public Object createObjectivePacket(int mode, String name, String displayName) {
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        setFieldValue(packet, "a", name);
        setFieldValue(packet, "d", mode);

        if (mode == 0 || mode == 2) {
            setFieldValue(packet, "b", displayName);

            setFieldValue(packet, "c", net.minecraft.server.v1_8_R3.IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        }
        return packet;
    }

    @Override
    public Object createDisplayObjectivePacket(String name) {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
        setFieldValue(packet, "a", 1);
        setFieldValue(packet, "b", name);
        return packet;
    }

    @Override
    public Object createScorePacket(String name, String line, int score) {
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);
        setFieldValue(packet, "b", name);
        setFieldValue(packet, "c", score);
        setFieldValue(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
        return packet;
    }

    @Override
    public Object destroyScorePacket(String line) {
        return new PacketPlayOutScoreboardScore(line);
    }

    @Override
    public PacketPlayOutScoreboardTeam createTeamPacket(int mode, String name, String prefix, String suffix) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

        setFieldValue(packet, "h", mode);
        setFieldValue(packet, "a", name);
        if (prefix != null) setFieldValue(packet, "c", prefix); // prefix
        if (suffix != null) setFieldValue(packet, "d", suffix); // suffix
        setFieldValue(packet, "i", 0);
        setFieldValue(packet, "f", 0);

        return packet;
    }

    @Override
    public Object createUpdateUserPacket(int mode, String name, String user) {
        PacketPlayOutScoreboardTeam packet = createTeamPacket(mode, name, null, null);
        setFieldValue(packet, "g", Collections.singletonList(user));
        return packet;
    }

    @Override
    public void sendPackets(Player player, Object... packets) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            Method method = playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet"));

            for (Object packet : packets)
                method.invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<?> getNMSClass(String name) {
        // org.bukkit.craftbukkit.v1_8_R3...
        Class<?> aClass = cacheClazz.get(name);
        if (aClass != null) return aClass;

        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            Class<?> clazz = Class.forName("net.minecraft.server." + version + "." + name);
            cacheClazz.put(name, clazz);
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}