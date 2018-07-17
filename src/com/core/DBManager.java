package com.core;

import com.core.utilities.Base;
import com.core.utilities.Lo;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.sdb.XOfficeDatabaseDocument;
import com.sun.star.sdbc.*;
import java.util.Collections;
import java.util.Vector;

public class DBManager {

	private String _filename;
	private XComponentLoader _loader;
	private XOfficeDatabaseDocument _odbDoc;
	private StringBuilder _query;

	DBManager(XComponentLoader loader, String filename) {
		_filename = filename;
		_loader = loader;
        _odbDoc = null;
        _query = new StringBuilder();
	}

	public void open() {
		_odbDoc = Base.openBaseDoc(_filename, _loader);
	}

	public void run(Configurator conf) {
        XDataSource dataSource = _odbDoc.getDataSource();
        try {
            XConnection connection = dataSource.getConnection("", "");
            createRecords(connection, conf);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	private void createRecords(XConnection connection, Configurator conf) {
	    for (Vector<String> values: conf._table) {
            Vector<String> attrsEntities = new Vector<>(conf._attrsEntities);
            Vector<String> results = new Vector<>();
            results.add(values.elementAt(0));
            values.remove(0);
            for (int i = 0; i < conf._numAttrsEntities; i++) {
                Vector<String> a = new Vector<>(Collections.singletonList(attrsEntities.elementAt(i)));
                Vector<String> v = new Vector<>(Collections.singletonList(values.elementAt(i)));
                Integer currentId = addIfNotExists(connection, conf._entities.elementAt(i), a, v);
                results.add(currentId.toString());
            }
            for (int i = 0; i < conf._numAttrsEntities; i++) {
                attrsEntities.remove(0);
                values.remove(0);
            }
            Integer idMain = addIfNotExists(connection, conf._mainEntity, conf._attrsMain, results);
            if (!conf._manyToManyPivot.equals("") && !conf._secondaryEntity.equals("")) {
                Integer idSecondary = addIfNotExists(connection, conf._secondaryEntity, attrsEntities, values);
                results.removeAllElements();
                results.add(idMain.toString());
                results.add(idSecondary.toString());
                addIfNotExists(connection, conf._manyToManyPivot, conf._pivotAttrs, results);
            }
	    }
    }

    private int addIfNotExists(XConnection connection, String from, Vector<String> attrs, Vector<String> values) {
        _query.append("SELECT " ).append("*")
                .append(" FROM ").append(from)
                .append(" WHERE ");
        for (int i = 0; i < attrs.size(); i++) {
            _query.append(attrs.elementAt(i)).append(" = '").append(values.elementAt(i)).append("'").append(" AND ");
        }
        _query.setLength(_query.length() - " AND ".length());
        XResultSet rs = Base.executeQuery(_query.toString(), connection);
        _query.setLength(0);
        if (rs != null) {
            try {
                rs.next();
                if (rs.getRow() == 0) {
                    //System.out.println("No record, adding");
                    insert(connection, from, attrs, values);
                    return addIfNotExists(connection, from, attrs, values);
                } else {
                    XRow xRow = Lo.qi(XRow.class, rs);
                    return xRow.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private void insert(XConnection connection, String into, Vector<String> attrs, Vector<String> values) {
        _query.append("INSERT INTO \"").append(into).append("\" (");
        for (String attr : attrs) {
            _query.append("\"").append(attr).append("\",");
        }
        _query.setLength(_query.length() - ",".length());
        _query.append(") VALUES (");
        for (String value : values) {
            if (value.matches("\\d+")) {
                _query.append(value).append(",");
            } else {
                _query.append("'").append(value).append("',");
            }
        }
        _query.setLength(_query.length() - 1);
        _query.append(")");
        Base.exec(_query.toString(), connection);
        _query.setLength(0);
    }

	public void close() {
		Base.closeBaseDoc(_odbDoc);
	}
}
