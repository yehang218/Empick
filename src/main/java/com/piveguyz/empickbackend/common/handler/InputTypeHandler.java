package com.piveguyz.empickbackend.common.handler;

import com.piveguyz.empickbackend.employment.applicationItem.query.dto.InputType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@MappedTypes(InputType.class)
public class InputTypeHandler extends BaseTypeHandler<InputType> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, InputType parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getCode());
	}

	@Override
	public InputType getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return InputType.fromCode(rs.getInt(columnName));
	}

	@Override
	public InputType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return InputType.fromCode(rs.getInt(columnIndex));
	}

	@Override
	public InputType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return InputType.fromCode(cs.getInt(columnIndex));
	}
}