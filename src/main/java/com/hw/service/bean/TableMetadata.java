/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.hw.service.bean;

import lombok.Data;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.mapping.ForeignKey;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.hibernate.internal.CoreLogging.messageLogger;

/**
 * JDBC table metadata
 *
 * @author Christoph Sturm
 * @author Max Rydahl Andersen
 */
@Data
public class TableMetadata {
    private static final CoreMessageLogger LOG = messageLogger(TableMetadata.class);

    private final String catalog;
    private final String schema;
    private final String name;
    private final Map<String, ColumnMetadata> columns = new HashMap<>();
    private final Map<String, ForeignKeyMetadata> foreignKeys = new HashMap<>();
    private final Map<String, IndexMetadata> indexes = new HashMap<>();
    private final Map<String, PrimaryKeyMetaData> primaryKeys = new HashMap<>();

    public TableMetadata(ResultSet rs, DatabaseMetaData meta, boolean extras) throws SQLException {
        catalog = rs.getString("TABLE_CAT");
        schema = rs.getString("TABLE_SCHEM");
        name = rs.getString("TABLE_NAME");
        initPrimaryKeys(meta);
        initColumns(meta);
        if (extras) {
            initForeignKeys(meta);
            initIndexes(meta);
        }
        String cat = catalog == null ? "" : catalog + '.';
        String schem = schema == null ? "" : schema + '.';
        LOG.tableFound(cat + schem + name);
        LOG.columns(columns.keySet());
        if (extras) {
            LOG.foreignKeys(foreignKeys.keySet());
            LOG.indexes(indexes.keySet());
        }
    }

    public ColumnMetadata getColumnMetadata(String columnName) {
        return columns.get(columnName.toLowerCase(Locale.ROOT));
    }

    public ForeignKeyMetadata getForeignKeyMetadata(String keyName) {
        return foreignKeys.get(keyName.toLowerCase(Locale.ROOT));
    }

    public ForeignKeyMetadata getForeignKeyMetadata(ForeignKey fk) {
        for (ForeignKeyMetadata existingFk : foreignKeys.values()) {
            if (existingFk.matches(fk)) {
                return existingFk;
            }
        }
        return null;
    }

    public IndexMetadata getIndexMetadata(String indexName) {
        return indexes.get(indexName.toLowerCase(Locale.ROOT));
    }

    private void addForeignKey(ResultSet rs) throws SQLException {
        String fk = rs.getString("FK_NAME");

        if (fk == null) {
            return;
        }

        ForeignKeyMetadata info = getForeignKeyMetadata(fk);
        if (info == null) {
            info = new ForeignKeyMetadata(rs);
            foreignKeys.put(info.getName().toLowerCase(Locale.ROOT), info);
        }

        info.addReference(rs);
    }

    private void addIndex(ResultSet rs) throws SQLException {
        String index = rs.getString("INDEX_NAME");

        if (index == null) {
            return;
        }

        IndexMetadata info = getIndexMetadata(index);
        if (info == null) {
            info = new IndexMetadata(rs);
            indexes.put(info.getName().toLowerCase(Locale.ROOT), info);
        }
    }

    public void addColumn(ResultSet rs) throws SQLException {
        String column = rs.getString("COLUMN_NAME");

        if (column == null) {
            return;
        }

        if (getColumnMetadata(column) == null) {
            ColumnMetadata info = new ColumnMetadata(rs, primaryKeys);
            columns.put(info.getName().toLowerCase(Locale.ROOT), info);
        }
    }

    private void addPrimaryKey(ResultSet rs) throws SQLException {
        PrimaryKeyMetaData primaryKey = new PrimaryKeyMetaData(rs);
        primaryKeys.put(primaryKey.getColumnName(), primaryKey);
    }

    private void initForeignKeys(DatabaseMetaData meta) throws SQLException {
        ResultSet rs = null;

        try {
            rs = meta.getImportedKeys(catalog, schema, name);
            while (rs.next()) {
                addForeignKey(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    private void initPrimaryKeys(DatabaseMetaData meta) throws SQLException {
        ResultSet rs = null;

        try {
            rs = meta.getPrimaryKeys(catalog, schema, name);

            while (rs.next()) {
                addPrimaryKey(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    private void initIndexes(DatabaseMetaData meta) throws SQLException {
        ResultSet rs = null;

        try {
            rs = meta.getIndexInfo(catalog, schema, name, false, true);

            while (rs.next()) {
                if (rs.getShort("TYPE") == DatabaseMetaData.tableIndexStatistic) {
                    continue;
                }
                addIndex(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    private void initColumns(DatabaseMetaData meta) throws SQLException {
        ResultSet rs = null;

        try {
            rs = meta.getColumns(catalog, schema, name, "%");
            while (rs.next()) {
                addColumn(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }
}
