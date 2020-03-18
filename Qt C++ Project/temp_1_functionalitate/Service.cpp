#include "Service.h"
#include <assert.h>
#include <fstream>
#include <random>
#include <sstream>

using namespace std;


void Service::adauga(const string& nume, const string& tip, const float pret, const string& producator) {
	/*
		functie care creeaza un obiect de tip produs cu datele primite, il valideaza si il trimite la 
		repo pentru adaugare in caz de succes la validare
		post: se adauga in repo obiectul creat, sau se arunca exceptie in caz de eroare la validare
	*/
	
	Produs prod{ nume, tip, pret, producator };
	val.valideaza(prod);
	// produsul a trecut de validare
	
	repo.adauga_repo(prod);
	ActiuniUndo.push_back(make_unique<UndoAdauga>(repo, prod));
}

/*
	Functie care adauga in fsierul cosului produsul dat ca parametru
	prod: Produs existent in repo
*/
void Service::adauga_in_cos(const Produs& prod) {
	val.valideaza(prod);
	repo.adauga_in_cos(prod);
	//coss.sincronizare_fisier_cos_repo_cos_si_tabel();
}

/*
	Functie care returneaza numarul de elemente din cos
*/
int Service::get_dim_cos() {
	return repo.dim_cos();
}

/*
	Functie care apeleaza metoda de incarcare in meorie(repo memorie) din repo a elementelor din fisierul cu produse 
*/
void Service::load_from_file() {
	repo.load_from_file();
}

/*
	Functie care genereaza un numar random din intervalul [min, max]
	pre: min < max
*/
int Service::get_random_from(int min, int max) {

	return repo.get_random(min, max);

}

/*
	Functie care adauga in cos elemente luate aleator din repo
	nr_produse: int - numarul de produse care vor fi in cos dupa umplere
*/
void Service::umple_cos(int nr_produse) {
	if (nr_produse <= get_dim_cos())
		throw RepositoryException("Ati introdus un numar final mai mic decat dimensiunea actuala a cosului!\n");
	if (!repo.size_of_repo())
		throw RepositoryException("Nu exista elemente cu care sa umplem cosul!\n");
	auto toate = getAll();
	while (get_dim_cos() < nr_produse) {
		int index_random = get_random_from(0, (int)toate.size()-1);
		adauga_in_cos(toate[index_random]);
	}
}

/*
	Functie care calculeaza suma produselor din cos
	return: float
*/
float Service::get_suma_curenta_cos() {
	ifstream cos("cos.cvs");
	string linie;
	float suma = 0;
	while (!cos.eof()) {
		getline(cos, linie);
		if (linie == "")
			break;
		auto componente_linie = split(linie, ',');
		suma += stof(componente_linie[2]);
	}
	cos.close();
	return suma;
}

void Service::sterge(const string& nume, const string& producator) {
	/*
		functie care primeste de la ui un nume si un producator si sterge din repo obiectul care le contine
		post: se sterge din repo acel obiect, sau se arunca exceptie in cazul in care nu exista
	*/
	
	Produs p{ repo.cauta(nume, producator) };
	//daca am ajuns aici inseamna ca exista produsul de sters
	repo.remove(p);
	ActiuniUndo.push_back(make_unique<UndoSterge>(repo, p));
}

void Service::modifica(const string& nume_de_modificat, const string& producator_de_modificat, const string& nume_nou, const string& tip_nou, float pret_nou, const string& priducator_nou) {
	/*
		functie care modifica un produs din repo pe baza nunmelui si a producatorului
		creeaza un nou produs care va fi pus in loc
		post: se modifica obiectul dorit sau se arunca exceptie in cazul in care acesta nu este gasit
	*/
	
	Produs prod_de_modificat{ repo.cauta(nume_de_modificat, producator_de_modificat) };
	//daca am ajuns aici inseamna ca exista produsul pe care vrem sa il modificam
	Produs prod_inlocuitor{ nume_nou, tip_nou, pret_nou, priducator_nou };
	val.valideaza(prod_inlocuitor);
	// datele noi sunt valide
	repo.update(prod_de_modificat, prod_inlocuitor);
	ActiuniUndo.push_back(make_unique<UndoModifica>(repo, prod_de_modificat, prod_inlocuitor));
}

Produs Service::cauta_prod(const string& nume, const string& prod) const {
	/*
		functie care cauta un produs in repo pe baza numelui si a producatorului
	*/
	
	//return repo.cauta(nume, prod);
	auto toate = getAll();
	auto it = std::find_if(toate.begin(), toate.end(), [nume,prod](const Produs& p)->bool {return p.get_nume() == nume && p.get_producator() == prod; });
	if (it == toate.end())
		throw RepositoryException("Element inexistent!\n");
	return *it;
}

/*
	Functie care cauta in repo memorie produs al carui nume este egal cu nume
	return primul Produs care are numele "nume"
*/
Produs Service::cauta_prod_pentru_cos(const string& nume) const {
	auto toate = getAll();
	auto it = std::find_if(toate.begin(), toate.end(), [nume](const Produs& p)->bool {return p.get_nume() == nume; });
	if (it == toate.end())
		throw RepositoryException("Element inexistent!\n");
	return *it;
}

//functia universala de filtrare
vector<Produs> Service::filtreaza(function<bool(const Produs&)> func) const {
	/*
		functie care filtreaza elementele dupa parametrul 'func'
	*/
	vector<Produs> toate = getAll();
	vector<Produs>rez(repo.size_of_repo());
	auto it = std::copy_if(toate.begin(), toate.end(), rez.begin(), func);
	rez.resize(std::distance(rez.begin(), it));
	return rez;
}

vector<Produs> Service::produse_mai_mici_decat_un_pret(float pret) const {
	/*
		functie care returneaza produsele mai mici decat produsul pret
	*/
	return filtreaza([pret](const Produs& p) {
		return p.get_pret() < pret; });
}


vector<Produs> Service::produse_cu_numele_dat(string nume) const {
	/*
		functie care returneaza o lista cu elementele care au campul nume egal cu nume
	*/
	return filtreaza([nume](const Produs& p) {
		return p.get_nume() == nume;
	});
}

vector<Produs> Service::produse_a_caror_nume_incepe_cu_o_litera(char prima_litera) const {
	return filtreaza([prima_litera](const Produs& p) {
		return p.get_nume()[0] == prima_litera;
	});
}

vector<Produs> Service::produse_cu_producator_dat(string producator) const {
	/*
		functie care returneaza o lista cu toate elementele care au un producator egal cu producator
	*/
	
	return filtreaza([producator](const Produs& p) {
		return p.get_producator() == producator;
	});
}

/*
	Functie care formeaza un dictionar cu fiecare tip de produs si frecventa sa de aparitie si il afiseaza
*/
map<string, int> Service::produse_frecventa() {
	auto toate = getAll();
	map<string, int> dictionar;

	for (const auto& el : toate) {
		dictionar[el.get_tip()]++;
	}

	return dictionar;
}

/*
	Functie care formeaza un vector de produse sortat din cele din repo
*/
vector<Produs> Service::sortare_generala_repo(CMPF fct) {
	vector<Produs> rez{ repo.get_all() };
	sort(rez.begin(), rez.end(), fct);
	return rez;
}

/* Functie care sorteaza repo dupa numele fiecarul produs aflat in noduri
 * post:repo va fi sortat
 */
vector<Produs> Service::sorteaza_dupa_nume() {
	return sortare_generala_repo(compara_nume);
}

/* Functie care sorteaza repo dupa pretul fiecarul produs aflat in noduri
 * post:repo va fi sortat
 */
vector<Produs> Service::sorteaza_dupa_pret() {
	return sortare_generala_repo(compara_pret);
}

/* Functie care sorteaza repo dupa numele si tipul fiecarul produs aflat in noduri
 * post:repo va fi sortat
 */
vector<Produs> Service::sorteaza_dupa_nume_si_tip() {
	return sortare_generala_repo([](const Produs& p1, const Produs& p2) {
		if (p1.get_nume() == p2.get_nume()) {
			return p1.get_tip() < p1.get_tip();
		}
		return p1.get_nume() < p2.get_nume();
	});
}

/* Functie care sterge toate elementele din repo
 * post:repo va fi gol
 */
void Service::s_goleste_repo() {
	repo.goleste_repo();
}

void Service::goleste_cos() {
	repo.goleste_cos();
}
/*
	Functie care face apel polimorfic la metoda doUndo din clasa ActiuneUndo
	se va face undo la ulima opeatie pe repository
*/
void Service::undo() {
	if (ActiuniUndo.empty())
		throw RepositoryException("Nu se mai poate face undo!\n");
	ActiuniUndo.back()->doUndo();
	ActiuniUndo.pop_back();
}



/* Functie care testeaza adaugarea in repo apelata din service
 */
void test_adauga_service() {/*
	Repository repo;
	Validator val;
	Service serv{ repo,val };
	serv.adauga("aa", "as", 32, "a");
	assert(serv.Service__size_of_repo() == 1);
	try {
		serv.adauga("as", "ds", -33, "ds");}
	catch (ValidationException& ex) {
		assert(ex.get_eroare() == " Pretul este invalid!");
	}
	
	try {
		serv.adauga("as", "ac", -44, "a");}
	catch (ValidationException&) {
		assert(true);
	}
	try {
		const auto _20 = serv.cauta_prod("dsdsd", "fdffs");
	}
	catch (RepositoryException&) {
		assert(true);
	}
	try {
		const auto _21 = serv.cauta_prod_pentru_cos("dsdsd");
	}
	catch (RepositoryException&) {
		assert(true);
	}
	assert(serv.cauta_prod_pentru_cos("aa").get_pret() == 32);*/

}

/* Functie care testeaza modificarea in repo apelata din service
 */
void test_modificare() {/*
	Repository repo;
	Validator val;
	Service serv{ repo,val };
	serv.adauga("at", "adfd", 2, "ad");
	assert(serv.Service__size_of_repo() == 1);


	serv.modifica("at", "ad", "ttt", "ttt", 20, "ttt");
	Produs p = serv.cauta_prod("ttt", "ttt");
	assert(p.get_nume() == "ttt");
	assert(p.get_producator() == "ttt");
	try{	
		serv.modifica("tttgfgf", "ttt", "dsds", "dsd", 333, "ffd");}
	catch (RepositoryException&) {
		assert(true);
	}*/
}

/* Functie care testeaza filtrarile din repo apelate din service
 */
void test_filtrari() {/*
	Repository repo;
	Validator val;
	Service serv{ repo,val };
	serv.adauga("aar", "ast", 32, "ay");
	serv.adauga("aap", "aso", 32, "au");
	serv.adauga("aag", "asf", 32, "ad");
	serv.adauga("aag", "asd", 32, "as");
	assert(serv.produse_cu_numele_dat("aar").size() == 1);
	assert(serv.produse_cu_producator_dat("au").size() == 1);
	assert(serv.produse_mai_mici_decat_un_pret(55).size() == 4);
	assert(serv.produse_mai_mici_decat_un_pret(11).size() == 0);
	assert(serv.produse_a_caror_nume_incepe_cu_o_litera('a').size() == 4);*/
}

/* Functie care testeaza sortarile 
*/

void test_sortare() {/*
	Repository repo;
	Validator val;
	Service serv{ repo,val };
	serv.adauga("a", "ast", 3, "asa");
	serv.adauga("a", "aso", 322, "au");
	serv.adauga("c", "asf", 3244, "ad");
	serv.adauga("d", "asd", 32, "as");

	auto primul_produs = serv.sorteaza_dupa_pret()[0];
	assert(primul_produs.get_nume() == "a");

	primul_produs = serv.sorteaza_dupa_nume()[0];
	assert(primul_produs.get_nume() == "a");

	primul_produs = serv.sorteaza_dupa_nume_si_tip()[0];
	assert(primul_produs.get_pret() == 3);*/

}

/* Functie care testeaza stergerea in repo apelata din service
 */
void test_sterge_service() {/*
	Repository repo;
	Validator val;
	Service serv{ repo,val };
	serv.adauga("a", "ast", 3, "asa");
	serv.adauga("a", "aso", 322, "au");
	serv.sterge("a", "asa");
	try {
		serv.sterge("fdfd", "dfdfd");}
	catch (RepositoryException &) {
		assert(true);
	}
	serv.s_goleste_repo();*/
}

/* Functie care apeleaza toate functiile de testare pentru service
 */
void test_service() {
	test_adauga_service();
	test_sterge_service();
	test_modificare();
	test_filtrari();
	test_sortare();
}