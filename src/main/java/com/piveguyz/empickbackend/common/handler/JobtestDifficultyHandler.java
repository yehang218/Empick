package com.piveguyz.empickbackend.common.handler;

import com.piveguyz.empickbackend.employment.jobtests.common.enums.JobtestDifficulty;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(JobtestDifficulty.class)
public class JobtestDifficultyHandler extends BaseTypeHandler<JobtestDifficulty> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JobtestDifficulty parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public JobtestDifficulty getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JobtestDifficulty.fromCode(rs.getInt(columnName));
    }

    @Override
    public JobtestDifficulty getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JobtestDifficulty.fromCode(rs.getInt(columnIndex));
    }

    @Override
    public JobtestDifficulty getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JobtestDifficulty.fromCode(cs.getInt(columnIndex));
    }
}
