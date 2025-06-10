package com.piveguyz.empickbackend.common.handler;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.enums.JobtestStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(JobtestStatus.class)
public class JobtestStatusHandler extends BaseTypeHandler<JobtestStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JobtestStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public JobtestStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JobtestStatus.fromCode(rs.getInt(columnName));
    }

    @Override
    public JobtestStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JobtestStatus.fromCode(rs.getInt(columnIndex));
    }

    @Override
    public JobtestStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JobtestStatus.fromCode(cs.getInt(columnIndex));
    }
}
