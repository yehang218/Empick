package com.piveguyz.empickbackend.common.handler;

import com.piveguyz.empickbackend.approvals.approval.command.domain.enums.ApprovalStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class ApprovalStatusTypeHandler extends BaseTypeHandler<ApprovalStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ApprovalStatus status, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, status.getCode());
    }

    @Override
    public ApprovalStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName);
        return rs.wasNull() ? null : ApprovalStatus.fromCode(code);
    }

    @Override
    public ApprovalStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return rs.wasNull() ? null : ApprovalStatus.fromCode(code);
    }

    @Override
    public ApprovalStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);
        return cs.wasNull() ? null : ApprovalStatus.fromCode(code);
    }
}
