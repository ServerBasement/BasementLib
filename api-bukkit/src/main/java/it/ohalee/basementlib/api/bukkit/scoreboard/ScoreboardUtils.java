package it.ohalee.basementlib.api.bukkit.scoreboard;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface ScoreboardUtils {

    Map<String, Class<?>> cacheClazz = new HashMap<>();
    Map<KeyField, Field> cacheField = new HashMap<>();

    int getCharactersLimits();

    Object createObjectivePacket(int mode, String name, String displayName);

    Object createDisplayObjectivePacket(String name);

    Object createScorePacket(String name, String line, int score);

    default Object destroyScorePacket(String line) {
        return null;
    }

    Object createTeamPacket(int mode, String name, @Nullable String prefix, @Nullable String suffix);


    default Object getCreateTeamPacket(String name, @Nullable String prefix, @Nullable String suffix) {
        return createTeamPacket(0, name, prefix, suffix);
    }

    default Object getDestroyTeamPacket(String name) {
        return createTeamPacket(1, name, null, null);
    }

    default Object getUpdateTeamPacket(String name, @Nullable String prefix, @Nullable String suffix) {
        return createTeamPacket(2, name, prefix, suffix);
    }

    Object createUpdateUserPacket(int mode, String name, String user);

    void sendPackets(Player player, Object... packets);

    default void setFieldValue(Object object, String fieldName, Object value) {
        try {
            Class<?> classObject = object.getClass();
            KeyField keyField = new KeyField(classObject, fieldName);
            Field field = cacheField.get(keyField);
            if (field == null) {
                field = classObject.getDeclaredField(fieldName);
                field.setAccessible(true);
                cacheField.put(keyField, field);
            }
            Objects.requireNonNull(field).set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiredArgsConstructor
    class KeyField {
        private final Class<?> clazz;
        private final String name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KeyField keyField = (KeyField) o;
            return Objects.equals(clazz, keyField.clazz) && Objects.equals(name, keyField.name);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new Object[] { clazz, name });
        }
    }
}
