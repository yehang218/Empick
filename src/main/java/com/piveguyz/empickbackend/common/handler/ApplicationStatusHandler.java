package com.piveguyz.empickbackend.common.handler;

import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicationStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ApplicationStatus.class)
public class ApplicationStatusHandler extends BaseTypeHandler<ApplicationStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ApplicationStatus status, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, status.getCode());
    }

    @Override
    public ApplicationStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return ApplicationStatus.fromCode(rs.getInt(columnName));
    }

    @Override
    public ApplicationStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return ApplicationStatus.fromCode(rs.getInt(columnIndex));
    }

    @Override
    public ApplicationStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ApplicationStatus.fromCode(cs.getInt(columnIndex));
    }
}
