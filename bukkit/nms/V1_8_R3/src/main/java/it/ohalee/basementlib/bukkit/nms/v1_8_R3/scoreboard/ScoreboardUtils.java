package it.ohalee.basementlib.bukkit.nms.v1_8_R3.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

public class ScoreboardUtils implements it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardUtils {

    @Override
    public int getCharactersLimits() {
        return 16;
    }

    @Override
    public Object createObjectivePacket(int mode, String name, String displayName) {
        try {
            Object packet = getNMSClass("PacketPlayOutScoreboardObjective").newInstance();
            setFieldValue(packet, "a", name);
            setFieldValue(packet, "d", mode);

            if (mode == 0 || mode == 2) {
                setFieldValue(packet, "b", displayName);

                Class<?> criteria = getNMSClass("IScoreboardCriteria.EnumScoreboardHealthDisplay");
                setFieldValue(packet, "c", criteria.getEnumConstants()[0]);
            }
            return packet;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object createDisplayObjectivePacket(String name) {
        try {
            Object packet = getNMSClass("PacketPlayOutScoreboardObjective").newInstance();
            setFieldValue(packet, "a", 1);
            setFieldValue(packet, "b", name);
            return packet;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object createScorePacket(String name, String line, int score) {
        try {
            Object packet = getNMSClass("PacketPlayOutScoreboardScore").getConstructor(String.class).newInstance(line);
            setFieldValue(packet, "b", name);
            setFieldValue(packet, "c", score);

            Class<?> change = getNMSClass("PacketPlayOutScoreboardScore.EnumScoreboardAction");
            setFieldValue(packet, "d", change.getEnumConstants()[0]);
            return packet;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object destroyScorePacket(String line) {
        try {
            return getNMSClass("PacketPlayOutScoreboardScore").getConstructor(String.class).newInstance(line);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object createTeamPacket(int mode, String name, @Nullable String prefix, @Nullable String suffix) {
        Class<?> packet = getNMSClass("PacketPlayOutScoreboardTeam");

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
        Object packet = createTeamPacket(mode, name, null, null);
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