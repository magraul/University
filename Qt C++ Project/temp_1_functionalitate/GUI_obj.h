#pragma once
#include <QWidget>
#include "Service.h"
#include <qwidget.h>
#include <qlistwidget.h>
#include <qboxlayout.h>
#include <QHBoxLayout>
#include <QVBoxLayout>
#include <qpushbutton.h>
#include <qlineedit.h>
#include <qformlayout.h>
#include <qlabel.h>
#include <QObject>
#include <vector>
#include <qmessagebox.h>
#include <qapplication.h>
#include <string>
#include "Cos_gui.h"
#include "Cos.h"

class GUI_obj :public QWidget
{
	friend class Cos_CRUD_GUI;
	//std::string aux = "Suma curenta in cos este: " + to_string(serv.get_suma_curenta_cos());
	QListWidget* lista = new QListWidget;
	QPushButton* exit = new QPushButton{ "&Exit" };
	QPushButton* Sterge = new QPushButton{ "&Sterge" };
	QPushButton* Adauga = new QPushButton{ "&Adauga" };
	QPushButton* Modifica = new QPushButton{ "&Update" };
	QPushButton* Cauta = new QPushButton{ "&Cauta" };
	QPushButton* buton__filtrare_dupa_pret = new QPushButton{ "&Pret" };
	QPushButton* buton__filtrare_dupa_nume = new QPushButton{ "&Nume" };
	QPushButton* buton__filtrare_dupa_producator = new QPushButton{ "&Producator" };

	QPushButton* buton__sortare_dupa_nume = new QPushButton{ "&Nume" };
	QPushButton* buton__sortare_dupa_pret = new QPushButton{ "&Pret" };
	QPushButton* buton__sortare_dupa_nume_tip = new QPushButton{ "&Nume+Tip" };

	QPushButton* buton__afisare_toate = new QPushButton{ "&Afisare stoc" };
	QPushButton* buton__frecvente = new QPushButton{ "&Frevente tipuri" };

	QPushButton* Cos = new QPushButton{ "&Adauga in Cos" };

	QLineEdit* nume = new QLineEdit;
	QLineEdit* tip = new QLineEdit;
	QLineEdit* pret = new QLineEdit;
	QLineEdit* producator = new QLineEdit;
	QPushButton* undo = new QPushButton{ "&Undo" };
	QLineEdit* nume_de_updatat = new QLineEdit;
	QLineEdit* producator_de_updatat = new QLineEdit;
	QLineEdit* suma_cos = new QLineEdit;

	//componente cos
	QListWidget* lista_cos = new QListWidget;
	QPushButton* goleste_cos = new QPushButton{ "&Goleste Cos" };
	QPushButton* genereaza_cos = new QPushButton{ "&Genereaza Cos" };
	QPushButton* iesire_din_cos = new QPushButton{ "&Iesire din cos" };
	QLineEdit* dim_generare_cos = new QLineEdit;

	Service& serv;

	void initializare_campuri();
	void conectare_semnale_sloturi();
	void sincronizare_lista_repository(const vector<Produs>& repo);
	void pune_frecvente_in_lista(const map<string, int>&);
	void sincronizare_repo_cos_lista_cos() ;

public:
	GUI_obj() = default;
	GUI_obj(Service serv) :serv{ serv }{
		initializare_campuri();
		sincronizare_lista_repository(serv.getAll());
		conectare_semnale_sloturi();
	}
	~GUI_obj() {}

	//functii

	void gui_adauga();
	void gui_remove();
	void gui_update();
	void gui_undo();
	void gui_cauta();
	void fereastra_cos();
	void add_cos();
	void gol_cos();
	void genere_cos();
	//void add_in_cos();

};

