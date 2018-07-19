package com.core;

import com.core.utilities.Lo;
import com.sun.star.frame.XComponentLoader;

import java.util.Vector;

public class Main {

	public static void main(String args[]) {
		XComponentLoader xComponentLoader   = Lo.loadSocketOffice();
		TableManager tableManager           = new TableManager();
		DBManager dbManager                 = new DBManager(xComponentLoader, args[0]);
        Vector<Configurator> confs          = new Vector<>();
        Configurator conf;

		if (args.length == 5) {
		    conf = new Configurator("program",
                                    "program_software",
                                    "software",
                                    4,
                                    new String[]{"name", "id_direction", "id_profile", "id_department", "id_developer"},
                                    new String[]{"direction", "profile", "department", "developer"},
                                    new String[]{"name", "name", "name", "name", "vendor", "name", "version"},
                                    new String[]{"id_program", "id_software"},
                                    tableManager.GetTable(xComponentLoader, args[1]));
		    confs.add(conf);
            conf = new Configurator("program",
                                    "program_mts",
                                    "mts",
                                    4,
                                    new String[]{"name", "id_direction", "id_profile", "id_department", "id_developer"},
                                    new String[]{"direction", "profile", "department", "developer"},
                                    new String[]{"name", "name", "name", "name", "type", "name"},
                                    new String[]{"id_program", "id_mts"},
                                    tableManager.GetTable(xComponentLoader, args[2]));
            confs.add(conf);
            conf = new Configurator("program",
                                    "program_er",
                                    "er",
                                    4,
                                    new String[]{"name", "id_direction", "id_profile", "id_department", "id_developer"},
                                    new String[]{"direction", "profile", "department", "developer"},
                                    new String[]{"name", "name", "name", "name", "name", "url"},
                                    new String[]{"id_program", "id_er"},
                                    tableManager.GetTable(xComponentLoader, args[3]));
            confs.add(conf);
            conf = new Configurator("program",
                                    "program_task",
                                    "task",
                                    4,
                                    new String[]{"name", "id_direction", "id_profile", "id_department", "id_developer"},
                                    new String[]{"direction", "profile", "department", "developer"},
                                    new String[]{"name", "name", "name", "name", "type", "topic"},
                                    new String[]{"id_program", "id_task"},
                                    tableManager.GetTable(xComponentLoader, args[4]));
            confs.add(conf);
        } else if (args.length == 3) {
            conf = new Configurator("pc",
                                    "",
                                    "",
                                    6,
                                    new String[]{"num", "id_room", "id_os", "id_cpu", "id_ram", "id_gpu", "id_hdd"},
                                    new String[]{"room", "os", "cpu", "ram", "gpu", "hdd"},
                                    new String[]{"num", "name", "name", "name", "name", "name"},
                                    new String[]{},
                                    tableManager.GetTable(xComponentLoader, args[1]));
            confs.add(conf);
            conf = new Configurator("pc",
                                    "pc_program",
                                    "program",
                                    1,
                                    new String[]{"num", "id_room"},
                                    new String[]{"room"},
                                    new String[]{"num", "vendor", "name", "version", "access", "path"},
                                    new String[]{"id_pc", "id_program"},
                                    tableManager.GetTable(xComponentLoader, args[2]));
            confs.add(conf);
        }
		dbManager.open();
        for (Configurator c: confs) {
            dbManager.run(c);
        }
		dbManager.close();
		Lo.closeOffice();
	}
}
