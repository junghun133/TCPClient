package com.pjh.client.message;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

public class ValueEnumTypeHandler<E extends ValueEnum> implements TypeHandler<E> {
    private final Class<E> type;

    public ValueEnumTypeHandler(Class<E> type) {
        if (type == null)
            throw new IllegalArgumentException("Type argument cannot be null");

        this.type = type;
    }

    private ValueEnum getValueEnum(int value) {
        for (ValueEnum e : type.getEnumConstants()) {
            if (e.getValue() == value)
                return e;
        }
        return null;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null)
            ps.setNull(i, Types.INTEGER);
        else
            ps.setInt(i, parameter.getValue());
    }

    @Override
    public E getResult(ResultSet rs, String columnName) throws SQLException {
        return (E) getValueEnum(rs.getInt(columnName));
    }

    @Override
    public E getResult(ResultSet rs, int columnIndex) throws SQLException {
        return (E) getValueEnum(rs.getInt(columnIndex));
    }

    @Override
    public E getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (E) getValueEnum(cs.getInt(columnIndex));
    }
}
