#include "Cos_gui.h"
#include <qlayout.h>
#include <qlabel.h>
#include <fstream>
#include <string>
using std::ifstream;

void Cos_CRUD_GUI::initializare_campuri_cos()
{
	QHBoxLayout* lay_principal_cos = new QHBoxLayout;
	setLayout(lay_principal_cos);

	QWidget* wid_stanga = new QWidget;
	QVBoxLayout* lay_stanga = new QVBoxLayout;
	wid_stanga->setLayout(lay_stanga);

	lay_stanga->addWidget(new QLabel("Produsele din Cos:"));
	lay_stanga->addWidget(tabel);

	//layout partea drepata
	QWidget* wid_dreapta = new QWidget;
	QVBoxLayout* lay_dreapta = new QVBoxLayout;
	wid_dreapta->setLayout(lay_dreapta);

	lay_dreapta->addWidget(goleste_cos);
	lay_dreapta->addWidget(genereaza_cos);
	lay_dreapta->addWidget(new QLabel("Dimensiune finala cos"));
	lay_dreapta->addWidget(dim_generare_cos);
	

	lay_principal_cos->addWidget(wid_stanga);
	lay_principal_cos->addWidget(wid_dreapta);

}

void Cos_CRUD_GUI::conectare_semnale_sloturi_cos()
{
	const auto _17 = QObject::connect(goleste_cos, &QPushButton::clicked, this, &Cos_CRUD_GUI::gol_cos);
	const auto _18 = QObject::connect(genereaza_cos, &QPushButton::clicked, this, &Cos_CRUD_GUI::genere_cos);
}


void Cos_CRUD_GUI::gol_cos()
{
	cos.goleste();
	sincronizare_repo_cos_tabel(cos.get_all());
}

void Cos_CRUD_GUI::genere_cos()
{
	cos.genereaza(dim_generare_cos->text().toInt());
	sincronizare_repo_cos_tabel(cos.get_all());
}



void Cos_CRUD_GUI::sincronizare_repo_cos_tabel(const vector<Produs>& toate) {
	tabel->setColumnCount(4);
	tabel->setRowCount(toate.size());
	tabel->clear();
	int linie = 0;
	for (auto& p : toate) {
		tabel->setItem(linie, 0, new QTableWidgetItem(QString::fromStdString(p.get_nume())));
		tabel->setItem(linie, 1, new QTableWidgetItem(QString::fromStdString(p.get_tip())));
		tabel->setItem(linie, 2, new QTableWidgetItem(QString::fromStdString(to_string(p.get_pret()))));
		tabel->setItem(linie, 3, new QTableWidgetItem(QString::fromStdString(p.get_producator())));
		++linie;
	}
}