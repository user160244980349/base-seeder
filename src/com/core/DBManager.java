package com.core;

import com.core.utilities.Base;
import com.core.utilities.Lo;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.sdb.XOfficeDatabaseDocument;
import com.sun.star.sdbc.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class DBManager {

	private String _filename = null;
	private XComponentLoader _loader = null;
	private XOfficeDatabaseDocument _odbDoc = null;
	private StringBuilder _query = null;

	public DBManager(XComponentLoader loader, String filename) {
		_filename = filename;
		_loader = loader;
        _query = new StringBuilder();
	}

	public void open() {
		_odbDoc = Base.openBaseDoc(_filename, _loader);
	}

	public void runOneToMany() { //ArrayList<Vector<String>> table
        XDataSource dataSource = _odbDoc.getDataSource();
        try {
            XConnection connection = dataSource.getConnection("", "");

            String mainEntity            = "program";
            Vector<String> simpleAttrs   = new Vector<>(Collections.singletonList("example4_program"));
            Vector<String> entities      = new Vector<>(Arrays.asList("direction", "profile", "department", "developer"));
            Vector<String> attrsEntities = new Vector<>(Arrays.asList("name", "name", "name", "name"));
            Vector<String> values        = new Vector<>(Arrays.asList("example_direction", "example_profile", "example_department", "example_developer"));
            Vector<String> attrsMain     = new Vector<>(Arrays.asList("name", "id_direction", "id_profile", "id_department", "id_developer"));

            addToMain(connection, mainEntity, simpleAttrs, entities, attrsEntities, values, attrsMain);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	public void addToMain(XConnection connection, String mainEntity, Vector<String> simpleAttrs, Vector<String> entities,
                               Vector<String> attrsEntities, Vector<String> values, Vector<String> attrsMain) {
        Vector<String> results = new Vector<>(simpleAttrs);

        for (int i = 0; i < entities.size(); i++) {
            Vector<String> a = new Vector<>(Collections.singletonList(attrsEntities.elementAt(i)));
            Vector<String> v = new Vector<>(Collections.singletonList(values.elementAt(i)));
            Integer currentId = addIfNotExists(connection, entities.elementAt(i), a, v);
            results.add(currentId.toString());
        }
        addIfNotExists(connection, mainEntity, attrsMain, results);
    }

    public int addIfNotExists(XConnection connection, String from, Vector<String> attrs, Vector<String> values) {
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
                    System.out.println("No record, adding");
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

    public void insert(XConnection connection, String into, Vector<String> attrs, Vector<String> values) {
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
