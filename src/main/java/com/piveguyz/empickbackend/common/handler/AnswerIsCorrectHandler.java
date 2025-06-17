package com.piveguyz.empickbackend.common.handler;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.enums.CorrectType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(CorrectType.class)
public class AnswerIsCorrectHandler  extends BaseTypeHandler<CorrectType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CorrectType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public CorrectType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return CorrectType.fromCode(rs.getInt(columnName));
    }

    @Override
    public CorrectType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return CorrectType.fromCode(rs.getInt(columnIndex));
    }

    @Override
    public CorrectType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return CorrectType.fromCode(cs.getInt(columnIndex));
    }
}
