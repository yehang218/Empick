package com.piveguyz.empickbackend.common.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import com.piveguyz.empickbackend.employment.recruitment.command.domain.enums.RecruitType;

@MappedTypes(RecruitType.class)
public class RecruitTypeHandler extends BaseTypeHandler<RecruitType> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, RecruitType type, JdbcType jdbcType)
		throws SQLException {
		ps.setInt(i, type.getCode());
	}

	@Override
	public RecruitType getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return RecruitType.fromCode(rs.getInt(columnName));
	}

	@Override
	public RecruitType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return RecruitType.fromCode(rs.getInt(columnIndex));
	}

	@Override
	public RecruitType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return RecruitType.fromCode(cs.getInt(columnIndex));
	}
}
