#include "temp_1_functionalitate.h"
#include <QtWidgets/QApplication>
#include "GUI_obj.h"
#include "Ui_obj.h"
#include "Teste.h"
#include "Cos_gui.h"

#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>


void test_all() {
	/*
		functia care apeleaza toate testele
	*/
	test_all_fisier_test();
	test_repo();
	test_service();
}

int main(int argc, char *argv[]) {
	test_all();
	
	QApplication a(argc, argv);
	
	Validator val;
	RepositoryFile repo;
	Coss cos{ repo };
	Cos_CRUD_GUI cos1{ cos };
	Service serv{ repo, val};


	GUI_obj GUI{ serv};

	GUI.show();

	

	cos1.show();

	//Cos_gui_table cos2{ cos };

	///cos2.move(100, 20);
	//cos2.show();



	return a.exec();
	//_CrtDumpMemoryLeaks();
}
