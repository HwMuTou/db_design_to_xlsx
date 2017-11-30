/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.hw.service.bean;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC index metadata
 * @author Christoph Sturm
 */
@Data
public class IndexMetadata {
	private final String name;
	private final String columnName;
	private final String type;
	private final String unique;

	IndexMetadata(ResultSet rs) throws SQLException {
		name = rs.getString("INDEX_NAME");
		type = rs.getString("type");
		unique = rs.getString("non_unique");
		columnName = rs.getString("column_name");
	}
}
