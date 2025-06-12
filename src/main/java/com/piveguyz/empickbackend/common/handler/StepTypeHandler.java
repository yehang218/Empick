package com.piveguyz.empickbackend.common.handler;

import com.piveguyz.empickbackend.employment.recruitmentProcess.command.domain.enums.StepType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@MappedTypes(StepType.class)
public class StepTypeHandler extends BaseTypeHandler<StepType> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, StepType parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getCode());
	}

	@Override
	public StepType getNullableResult(ResultSet rs, String columnName) throws SQLException {
		int code = rs.getInt(columnName);
		return StepType.fromCode(code);
	}

	@Override
	public StepType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		int code = rs.getInt(columnIndex);
		return StepType.fromCode(code);
	}

	@Override
	public StepType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		int code = cs.getInt(columnIndex);
		return StepType.fromCode(code);
	}
}
