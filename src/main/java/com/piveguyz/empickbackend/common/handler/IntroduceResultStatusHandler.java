package com.piveguyz.empickbackend.common.handler;

import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceResultStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(IntroduceResultStatus.class)
public class IntroduceResultStatusHandler extends BaseTypeHandler<IntroduceResultStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IntroduceResultStatus status, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, status.getCode());
    }

    @Override
    public IntroduceResultStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return IntroduceResultStatus.fromCode(rs.getInt(columnName));
    }

    @Override
    public IntroduceResultStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return IntroduceResultStatus.fromCode(rs.getInt(columnIndex));
    }

    @Override
    public IntroduceResultStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return IntroduceResultStatus.fromCode(cs.getInt(columnIndex));
    }

}
