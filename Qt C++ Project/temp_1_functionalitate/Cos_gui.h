#pragma once
#include "Cos.h"
#include "Observer.h"
#include <qwidget.h>
#include <qtablewidget.h>
#include <qpushbutton.h>
#include<vector>
#include <qlistwidget.h>
#include <qlineedit.h>
#include <qlayout.h>

using std::vector;

class Cos_CRUD_GUI :public QWidget, public Observer{
	friend class GUI_obj;
private:
	Coss& cos;
	
	QTableWidget* tabel = new QTableWidget;
	QPushButton* goleste_cos = new QPushButton{ "&Goleste Cos" };
	QPushButton* genereaza_cos = new QPushButton{ "&Genereaza Cos" };
	QLineEdit* dim_generare_cos = new QLineEdit;

	void initializare_campuri_cos();
	void sincronizare_repo_cos_tabel(const vector<Produs>&);
	void conectare_semnale_sloturi_cos();


public:
	Cos_CRUD_GUI(Coss& c) :cos{ c } {
		initializare_campuri_cos();
		sincronizare_repo_cos_tabel(cos.get_all());
		conectare_semnale_sloturi_cos();
	}

	void gol_cos();
	void genere_cos();
	
};
