package com.piveguyz.empickbackend.common.handler;


import java.sql.*;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitmentStatus;

public class RecruitmentStatusHandler extends BaseTypeHandler<RecruitmentStatus> {
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, RecruitmentStatus status, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, status.getCode());
	}

	@Override
	public RecruitmentStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
		int code = rs.getInt(columnName);
		return RecruitmentStatus.fromCode(code);
	}

	@Override
	public RecruitmentStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		int code = rs.getInt(columnIndex);
		return RecruitmentStatus.fromCode(code);
	}

	@Override
	public RecruitmentStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		int code = cs.getInt(columnIndex);
		return RecruitmentStatus.fromCode(code);
	}
}
