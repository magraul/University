#include "Teste.h"
#include <iostream>
#include <fstream>
#include <string>
#include "Service.h"

using namespace std;

void test_adauga_cos() {
	//salvam cosul actual intr-un fisier pana la terminarea testelor ca sa nu afectam continutul
/*	RepositoryFile rep;
	Validator val;
	Service serv{ rep,val };
	ofstream aux("temp.cvs");
	ifstream cos("cos.cvs");
	string linie;
	while (!cos.eof()) {
		getline(cos, linie);
		if(linie != "")
			aux << linie << "\n";
	}
	aux.close();
	cos.close();

	serv.goleste_cos();
	serv.adauga_in_cos(Produs{ "aa", "aa", 12, "aa" });
	assert(serv.get_dim_cos() == 1);
	serv.adauga_in_cos(Produs{ "aa", "aa", 12, "aa" });
	serv.adauga_in_cos(Produs{ "aa", "aa", 12, "aa" });
	assert(serv.get_dim_cos() == 3);

	//final test cos
	//punem inapoi continutul initial

	ofstream cos1("cos.cvs");
	ifstream aux1("temp.cvs");
	while (!aux1.eof()) {
		getline(aux1, linie);
		if(linie != "")
			cos1 << linie << "\n";
	}
	cos1.close();
	aux1.close();*/
}


void test_random() {/*
	Repository repo;
	Validator val;
	Service serv{ repo, val };
	assert(serv.get_random_from(2, 10) <= 10);*/
}

void test_umple_cos() {/*
	Repository repo;
	Validator val;
	Service serv{ repo, val };

	ofstream aux("temp.cvs");
	ifstream cos("cos.cvs");
	string linie;

	while (!cos.eof()) {
		getline(cos, linie);
		if(linie != "")
			aux << linie << "\n";
	}

	aux.close();
	cos.close();
	try {
		serv.umple_cos(30);
	}
	catch (RepositoryException&) {
		assert(true);
	}

	try {
		serv.umple_cos(-4);
	}
	catch (RepositoryException&) {
		assert(true);
	}
	assert(serv.get_suma_curenta_cos()==0 || serv.get_suma_curenta_cos() != 0);
	serv.load_from_file();
	serv.umple_cos(500);
	assert(serv.get_dim_cos() == 500);
	//final test cos
	//punem inapoi continutul initial

	ofstream cos1("cos.cvs");
	ifstream aux1("temp.cvs");
	while (!aux1.eof()) {
		getline(aux1, linie);
		if(linie != "")
			cos1 << linie << "\n";
	}
	cos1.close();
	aux1.close();*/
}

void test_dictionar() {/*
	Repository repo;
	Validator val;
	Service serv{ repo, val };
	serv.load_from_file();
	assert(serv.produse_frecventa().size()>0);*/
}

void test_undo() {/*
	Repository repo;
	Validator val;
	Service serv{ repo, val };

	serv.adauga("r", "m", 100, "d");
	serv.adauga("ra", "ma", 100, "dd");
	serv.adauga("rau", "mag", 100, "ddd");
	serv.adauga("raul", "magg", 100, "dddd");
	
	int dim = serv.Service__size_of_repo();
	serv.adauga("raultt", "magtt", 100, "ttddd");
	serv.undo();
	assert(serv.Service__size_of_repo() == dim);
	serv.modifica("ra", "dd", "tt", "tt", 13, "tt");
	auto toate = serv.getAll();
	assert(toate[1].get_pret() == 13);
	serv.undo();
	auto tot = serv.getAll();
	assert(tot[1].get_pret() == 100);
	serv.sterge("ra", "dd");
	serv.undo();
	assert(serv.Service__size_of_repo() == dim);
	serv.undo();
	serv.undo();
	serv.undo();
	serv.undo();
	try {
		serv.undo();
	}
	catch (RepositoryException&) {
		assert(true);
	}*/
}

void test_adauga_fis() {/*
	ofstream aux("temp.cvs");
	ifstream in("produse.cvs");
	string linie;

	while (!in.eof()) {
		getline(in, linie);
		if (linie != "")
			aux << linie << "\n";
	}

	aux.close();
	in.close();

	RepositoryFile repo;
	Validator val;
	Service serv{ repo, val };
	repo.goleste_repo();
	repo.adauga_repo(Produs{ "Raul", "Mag", 13, "ddd" });
	assert(repo.size_of_repo() == 1);
	Produs p{ "Raul", "Mag", 13, "ddd" };
	Produs p1{ "aul", "Mg", 13, "dd" };
	repo.remove(p);
	repo.adauga_repo(Produs{ "Raul", "Mag", 13, "ddd" });
	repo.update(p, p1);
	repo.write_to_file();

	ofstream prod("produse.cvs");
	ifstream aux1("temp.cvs");
	while (!aux1.eof()) {
		getline(aux1, linie);
		if (linie != "")
			prod << linie << "\n";
	}
	prod.close();
	aux1.close();*/
}

void test_all_fisier_test() {
	test_adauga_cos();
	test_random();
	test_umple_cos();
	test_dictionar();
	test_undo();
	test_adauga_fis();
}