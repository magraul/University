#include "Repository.h"
#include <assert.h>
#include <memory>
#include <fstream>

Produs Repository::cauta(const string& nume, const string& producator) const {
	/*
		functie care cauta in repo un produs dupa campul nume si campul producator si returneaza referinta la acel obiect
		post:obiect de tip Produs
	*/
	for (const auto& el : all) {
		if (el.get_nume() == nume && el.get_producator() == producator)
			return el;
	}
	// daca am ajuns aici inseamana ca nu exista elementul in repo si aruncam exceptie
	throw RepositoryException("Nu exista produsul " + nume + " de la producatorul " + producator);
}

bool Repository::exist(const Produs& p) {
	/*
		functie care verifica daca un produs exista in repo
		pre: p Produs
		post: true daca obiectul p exista in repo si false altfel
	*/
	bool exista = false;
	for (const auto& el : all)
		if (el == p)
			exista = true;
	return exista;
}

void Repository::adauga_repo(const Produs& p) {
	/*
		functie care adauga in repo un obiect
		pre: p Produs validat
		post:se adauga p in repo, sau se arunca exceptie in cazul in care p exista deja
	*/
	
	if (exist(p))
		throw RepositoryException("Element deja existent cu numele " + p.get_nume() + " de la producatorul " + p.get_producator());
	//nu mai exista acel element in repo
	all.push_back(p);
}


/*
	Functie care adauga in fisierul  cos un produs
	pre: p Produs validat si existent in repository
*/
void Repository::adauga_in_cos(const Produs& p) {
	ofstream cos;
	cos.open("cos.cvs", std::ios_base::app);
	auto linie_de_pus_in_fisier = p.get_nume() + "," + p.get_tip() + "," + to_string(p.get_pret()) + "," + p.get_producator() + "\n";
	cos << linie_de_pus_in_fisier;
	cos.close();
}


/*
	Functie care incarca din fisierul cu produse, obiecte de tip produs in repositoryul in memorie
	se apeleaza la startul aplicatiei, cand repo memorie este gol
*/
void Repository::load_from_file() {
	ifstream produse("produse.cvs");
	string linie;
	while (!produse.eof()) {
		getline(produse, linie);
		if (linie == "")
			break;
		auto componente_linie = split(linie, ',');
		all.push_back(Produs{ componente_linie[0], componente_linie[1], stof(componente_linie[2]), componente_linie[3] });
	}
	produse.close();
}

void Repository::write_to_file() {
	ofstream produse("produse.cvs");
	for (const auto& prod : all) {
		auto linie_de_pus_in_fisier = prod.get_nume() + "," + prod.get_tip() + "," + to_string(prod.get_pret()) + "," + prod.get_producator() + "\n";
		produse << linie_de_pus_in_fisier;
	}
	produse.close();
}



void Repository::goleste_cos() {
	ofstream o("cos.cvs");
	o.close();
}

const vector<Produs>& Repository::get_all() const {
	/*
		functie care returneaza intreg repositoryul actual ca si referinta la acesta
	*/

	return all;
}

int Repository::dim_cos() {
	ifstream in("cos.cvs");
	string lin;
	int cnt = 0;
	while (!in.eof()) {
		getline(in, lin);
		cnt++;
	}
	in.close();
	return cnt - 1;
}
void Repository::remove(Produs& p) {
	/*
		functie care sterge din repo obiectul p
		pre: p Produs valid
			 se stie ca p exista in repo de cand a fost creat
		post: se sterge din repo obiectul p
	*/
	
	int k = pozitie_element(p);
	all.erase(all.begin() + k);
}

void Repository::update(Produs de_inlouit, Produs& inlocuitor) {
	/*
		functie care inlocuieste un produs dat, cu altul
		pre: de_inlocuit: Produs valid, care va fi inlocuit
			inlocuitor: Produs validat cu care se va inlocuii
		post: in repo in loc de "de_inlocuit" o sa fie "inlocuitor"
	*/
	for (auto& el : all)
		if (el == de_inlouit)
			el = inlocuitor;
}

void Repository::goleste_repo() {
	/*
		functie care goleste repositoryul
		post:size(repo) = 0
	*/
	
	while (all.size()) 
		all.erase(all.begin());
}



void test_adauga() {
	/*
		functia care testeaza adaugarile in repo
	*/
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

	Repository repo;
	repo.write_to_file();
	Produs p = Produs{ "as","aas",4, "tr" };
	repo.adauga_repo(p);
	assert(repo.size_of_repo() == 1);
	p = Produs{ "asb","aasb",4, "trb" };
	repo.adauga_repo(p);
	p = Produs{ "asc","aasc",4, "trc" };
	repo.adauga_repo(p);

	assert(repo.size_of_repo() == 3);
	try {
		p = Produs{ "as", "aas", 4, "tr" };
		repo.adauga_repo(p);	}
	catch (RepositoryException& text) {
		assert(text.get_eroare() == "Element deja existent cu numele as de la producatorul tr");
		//assert(true);
	}
	assert(repo.cauta("as", "tr").get_nume() == "as");
	Produs d = Produs{ "bb","ba",4, "ty" };
	repo.adauga_repo(d);
	assert(repo.size_of_repo() == 4);

	try {
		p = Produs{ "as","asa",4, "tr" };
		repo.adauga_repo(p);	}
	catch (RepositoryException&) {
		assert(true);
	}

	ofstream prod("produse.cvs");
	ifstream aux1("temp.cvs");
	while (!aux1.eof()) {
		getline(aux1, linie);
		if (linie != "")
			prod << linie << "\n";
	}
	prod.close();
	aux1.close();
}

void test_cauta() {
	/*
		functia care testeaza cautarile in repo
	*/
	
	Repository repo;
	Produs p = Produs{ "aa","as", 4, "ad" };
	repo.adauga_repo(p);
	p = Produs{ "br","bt", 4, "av" };
	repo.adauga_repo(p);
	Produs o{ "dsd", "fffdfd", 444, "fdfdf" };
	int aux = repo.poz_elem_dat(o);
	assert(aux == -1);
	p = repo.cauta("aa", "ad");
	assert(p.get_nume() == "aa");
	assert(p.get_producator() == "ad");


	try {
		Produs p1 = repo.cauta("azz", "eigei");	}
	catch (RepositoryException&) {
		assert(true);
	}
}

void test_repo(){
	/*
		functia care testeaza operatiile pe repository
	*/
	
	test_adauga();
	test_cauta();
}

vector<string> split(const string &s, char delim) {
	vector<string> result;
	stringstream ss(s);
	string item;

	while (getline(ss, item, delim)) {
		result.push_back(item);
	}

	return result;
}