package com.piveguyz.empickbackend.common.handler;

import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceRatingResultStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(IntroduceRatingResultStatus.class)
public class IntroduceResultStatusHandler extends BaseTypeHandler<IntroduceRatingResultStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IntroduceRatingResultStatus status, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, status.getCode());
    }

    @Override
    public IntroduceRatingResultStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return IntroduceRatingResultStatus.fromCode(rs.getInt(columnName));
    }

    @Override
    public IntroduceRatingResultStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return IntroduceRatingResultStatus.fromCode(rs.getInt(columnIndex));
    }

    @Override
    public IntroduceRatingResultStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return IntroduceRatingResultStatus.fromCode(cs.getInt(columnIndex));
    }

}
