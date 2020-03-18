#pragma once
#include <iostream>
#include "Service.h"
using std::cout;
class Ui_obj
{
	Service& serv;
public:
	Ui_obj() = default;
	Ui_obj(Service& serv) :serv{ serv } {};
	Ui_obj(const Ui_obj& ot) :serv{ ot.serv } {
		cout << "copie ui!\n";
	}
	void run();
	void adauga_in_cos();
	void goleste_cos();
	void genereaza_cos();
	void actualizare_repo_file();
	void load_from_file();
	void undo();
	void meniu();
	void ui_adauga();
	void print(vector<Produs>);
	void ui_sterge();
	void ui_modifica();
	void ui_cautare();
	void filtrare_dupa_nume();
	void filtrare_dupa_producator();
	void elibereaza();
	void frecvente();
	void filtrare_dupa_prima_litera();
};

