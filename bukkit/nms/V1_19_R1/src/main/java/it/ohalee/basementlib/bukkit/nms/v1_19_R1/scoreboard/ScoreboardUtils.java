package it.ohalee.basementlib.bukkit.nms.v1_19_R1.scoreboard;

import net.minecraft.EnumChatFormat;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class ScoreboardUtils implements it.ohalee.basementlib.api.bukkit.scoreboard.ScoreboardUtils {

    private static Constructor<PacketPlayOutScoreboardTeam> teamConstructor;

    static {
        try {
            teamConstructor = PacketPlayOutScoreboardTeam.class.getDeclaredConstructor(String.class, int.class, Optional.class, Collection.class);
            teamConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final int getCharactersLimits() {
        return 128;
    }

    @Override
    public PacketPlayOutScoreboardObjective createObjectivePacket(int mode, String name, String displayName) {
        try {
            return new PacketPlayOutScoreboardObjective(new ScoreboardObjective(null,
                    name, null, IChatBaseComponent.b(displayName), IScoreboardCriteria.EnumScoreboardHealthDisplay.a), mode);
        } catch (Exception ignored) {
            return null;
        }
    }

    public PacketPlayOutScoreboardDisplayObjective createDisplayObjectivePacket(String name) {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective(1, null);
        setFieldValue(packet, "b", name);
        return packet;
    }

    @Override
    public PacketPlayOutScoreboardScore createScorePacket(String name, String line, int score) {
        return new PacketPlayOutScoreboardScore(ScoreboardServer.Action.a, name, line, score);
    }

    @Override
    public PacketPlayOutScoreboardScore destroyScorePacket(String line) {
        return new PacketPlayOutScoreboardScore(ScoreboardServer.Action.b, "", line, 0);
    }

    @Override
    public PacketPlayOutScoreboardTeam createTeamPacket(int mode, String name, @Nullable String prefix, @Nullable String suffix) {
        PacketPlayOutScoreboardTeam packet = null;
        try {
            PacketPlayOutScoreboardTeam.b options = new PacketPlayOutScoreboardTeam.b(new ScoreboardTeam(new Scoreboard(), ""));
            setFieldValue(options, "a", IChatBaseComponent.b(""));
            setFieldValue(options, "b", IChatBaseComponent.b(prefix != null ? prefix : ""));
            setFieldValue(options, "c", IChatBaseComponent.b(suffix != null ? suffix : ""));
            setFieldValue(options, "d", "always");
            setFieldValue(options, "e", "never");
            setFieldValue(options, "f", EnumChatFormat.v);
            setFieldValue(options, "g", -1);
            packet = teamConstructor.newInstance(name, mode, Optional.of(options), new ArrayList<>());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return packet;
    }

    @Override
    public PacketPlayOutScoreboardTeam createUpdateUserPacket(int mode, String name, String user) {
        PacketPlayOutScoreboardTeam packet = createTeamPacket(mode, name, null, null);
        setFieldValue(packet, "j", Collections.singletonList(user));
        return packet;
    }

    @Override
    public void sendPackets(Player player, Object... packets) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        for (Object packet : packets)
            connection.a((Packet<?>) packet);
    }
}
