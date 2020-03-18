#pragma once
#include "Validator.h"
#include <functional>
#include <algorithm>
#include <exception>
#include <map>
#include "Repository.h"
#include "ActiuneUndo.h"
#include "Cos_gui.h"

typedef bool(*CMPF)(const Produs&, const Produs&);

using namespace std;

class Service
{
	Repository &repo;
	Validator &val;

	vector<unique_ptr<ActiuneUndo>> ActiuniUndo;
	vector<Produs> filtreaza(function<bool(const Produs&)> func) const;

public:
	Service() = default;
	Service(Repository& rep, Validator &val) :repo{ rep }, val{ val } {};
	Service(const Service& ot) :repo{ ot.repo }, val{ ot.val } {
		cout << "copie la service!\n";
	}
	void goleste_cos();
	int get_random_from(int, int);
	void adauga(const string&, const string&, float, const string&);
	
	const vector<Produs> getAll() const {
		return repo.get_all();
	}

	void sterge(const string&, const string&);
	void modifica(const string&, const string&, const string&, const string&, float, const string&);
	Produs cauta_prod(const string&, const string&) const;
	Produs cauta_prod_pentru_cos(const string& nume) const;
	vector<Produs> produse_mai_mici_decat_un_pret(float) const;
	vector<Produs> produse_cu_numele_dat(string) const;
	vector<Produs> produse_cu_producator_dat(string) const;
	vector<Produs> sorteaza_dupa_nume();
	vector<Produs> sortare_generala_repo(CMPF);
	vector<Produs> sorteaza_dupa_pret();
	vector<Produs> sorteaza_dupa_nume_si_tip();
	vector<Produs> produse_a_caror_nume_incepe_cu_o_litera(char) const;
	void s_goleste_repo();
	void undo();
	int Service__size_of_repo() {
		return repo.size_of_repo();
	}
	map<string, int> produse_frecventa();
	void adauga_in_cos(const Produs&);
	int get_dim_cos();
	void umple_cos(int);
	float get_suma_curenta_cos();
	void load_from_file();


};

void test_service();