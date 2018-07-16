package com.core;

import com.core.utilities.Lo;
import com.sun.star.frame.XComponentLoader;

public class Main {

	public static void main(String args[]) {
		XComponentLoader xComponentLoader   = Lo.loadSocketOffice();
		TableManager tableManager           = new TableManager();
		DBManager dbManager                 = new DBManager(xComponentLoader, args[0]);

		Configurator conf1 = new Configurator(  "program",
												"program_software",
												"software",
												4,
												new String[] {"name", "id_direction", "id_profile", "id_department", "id_developer"},
												new String[] {"direction", "profile", "department", "developer"},
												new String[] {"name", "name", "name", "name", "vendor", "name", "version"},
												new String[] {"id_program", "id_software"},
												tableManager.GetTable(xComponentLoader, args[1]));
		Configurator conf2 = new Configurator(  "program",
												"program_mts",
												"mts",
												4,
												new String[] {"name", "id_direction", "id_profile", "id_department", "id_developer"},
												new String[] {"direction", "profile", "department", "developer"},
												new String[] {"name", "name", "name", "name", "type", "name"},
												new String[] {"id_program", "id_mts"},
												tableManager.GetTable(xComponentLoader, args[2]));
		Configurator conf3 = new Configurator(  "program",
												"program_er",
												"er",
												4,
												new String[] {"name", "id_direction", "id_profile", "id_department", "id_developer"},
												new String[] {"direction", "profile", "department", "developer"},
												new String[] {"name", "name", "name", "name", "name", "url"},
												new String[] {"id_program", "id_er"},
												tableManager.GetTable(xComponentLoader, args[3]));
		Configurator conf4 = new Configurator(  "program",
												"program_task",
												"task",
												4,
												new String[]{"name", "id_direction", "id_profile", "id_department", "id_developer"},
												new String[]{"direction", "profile", "department", "developer"},
												new String[]{"name", "name", "name", "name", "type", "topic"},
												new String[]{"id_program", "id_task"},
												tableManager.GetTable(xComponentLoader, args[4]));

		dbManager.open();

		dbManager.run(conf1);
		dbManager.run(conf2);
		dbManager.run(conf3);
		dbManager.run(conf4);

		dbManager.close();
		Lo.closeOffice();
	}
}
