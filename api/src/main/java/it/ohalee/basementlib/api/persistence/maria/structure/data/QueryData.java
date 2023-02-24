package it.ohalee.basementlib.api.persistence.maria.structure.data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public interface QueryData {

    boolean next();

    boolean hasNext();

    boolean isBeforeFirst();

    boolean first();

    boolean last();

    byte getByte(String columnLabel);

    byte getByte(int columnIndex);

    short getShort(String columnLabel);

    short getShort(int columnIndex);

    int getInt(String columnLabel);

    int getInt(int columnIndex);

    long getLong(String columnLabel);

    long getLong(int columnIndex);

    float getFloat(String columnLabel);

    float getFloat(int columnIndex);

    double getDouble(String columnLabel);

    double getDouble(int columnIndex);

    boolean getBoolean(String columnLabel);

    boolean getBoolean(int columnIndex);

    Object getObject(String columnLabel);

    Object getObject(int columnIndex);

    String getString(String columnLabel);

    String getString(int columnIndex);

    BigDecimal getBigDecimal(String columnLabel);

    BigDecimal getBigDecimal(int columnIndex);

    Date getDate(String columnLabel);

    Date getDate(int columnIndex);

    Timestamp getTimestamp(String columnLabel);

    Timestamp getTimestamp(int columnIndex);
}
