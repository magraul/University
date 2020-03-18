#include "Ui_obj.h"
#include<iostream>
#include <fstream>
#include <string>
using std::cout;
using std::cin;
using namespace std;

/* Functie care afiseaza meniul 
 */
void Ui_obj::meniu()
{
	cout << "\n\n";
	cout << "*************************************\n";
	cout << "*            Magazin		    *\n";
	cout << "*************************************\n";
	cout << "* 1. Adauga produs                  *\n";
	cout << "* 2. Stergere                       *\n";
	cout << "* 3. Modificare		            *\n";
	cout << "* 4. Cautare			    *\n";
	cout << "* 5. Filtrare dupa pret             *\n";
	cout << "* 6. Filtrare dupa nume             *\n";
	cout << "* 7. Filtrare dupa producator       *\n";
	cout << "* 8. Sortare dupa nume              *\n";
	cout << "* 9. Sortare dupa pret              *\n";
	cout << "* 10. Sortare dupa nume+tip         *\n";
	cout << "* 11. Afisare produse               *\n";
	cout << "* 12. Filtrare dupa prima litera a numelui\n";
	cout << "* 13. Frecventa tipuri              *\n"; 
	cout << "* 14. Adauga in cos                 *\n";
	cout << "* 15. Goleste cos                   *\n";
	cout << "* 16. Genereaza cos                   *\n";
	cout << "* 17. Undo                          *\n";
	cout << "* 0. EXIT                           *\n";
	cout << "*************************************\n\n";
	cout << "Introduceti comanda: ";
}


/* Functie care cere datele de la utilizator pentru adaugare de produs nou
 */

void Ui_obj::ui_adauga() {
	string nume, tip, producator;
	float pret;
	cout << "Dati numele: "; cin >> nume;
	cout << "Dati tipul: "; cin >> tip;
	cout << "Dati pretul: "; cin >> pret;
	cout << "Dati producatorul: "; cin >> producator;
	/*if (producator.size() == 0) {
		cout << "Pretul nu poate contine cifre!\n";
		cin;
		return;
	}*/
	serv.adauga(nume, tip, pret, producator);
}


/* Functie care afiseaza pe ecran o lista de produse primita ca parametru
 */
void Ui_obj::print(vector<Produs> produse) {
	if (produse.size() == 0)
		cout << "Nu exista produse!\n";
	else {
		for (const auto& pr : produse) {
			cout << "Nume: " + pr.get_nume() + "; Tip: " + pr.get_tip() + "; Pret: " << pr.get_pret() << "; Producator: " + pr.get_producator() + "\n";
		}
	}
}


/* Functie care cere de la utilizaotr datele pentru o stergere de produs
 */
void Ui_obj::ui_sterge() {
	string nume, producator;
	cout << "Introduceti numele si producatorul produsului pe care doriti sa il stergeti:\n";
	cout << "Numele: "; cin >> nume;
	cout << "Producatorul: "; cin >> producator;
	serv.sterge(nume, producator);
}


/* Functie care cere de la utilizator datele pentru o modificare si trimite comanda la service
 */
void Ui_obj::ui_modifica() {
	string nume_de_modificat, nume_producator_de_modificat, nume_nou, tip_nou, producator_nou;
	float pret_nou;
	cout << "Introduceti numele si producatorul produsului pe care doriti sa il modificati:\n";
	cout << "Introduceti numele: "; cin >> nume_de_modificat;
	cout << "Introduceti producatorul: "; cin >> nume_producator_de_modificat;
	cout << "Introduceti date noului produs:\n";
	cout << "Numele: "; cin >> nume_nou;
	cout << "Tipul: "; cin >> tip_nou;
	cout << "Pretul: "; cin >> pret_nou;
	cout << "Producatorul: "; cin >> producator_nou;
	serv.modifica(nume_de_modificat, nume_producator_de_modificat, nume_nou, tip_nou, pret_nou, producator_nou);
}


/* Functie care cere de la utilizator datele pentru o cautare de element si afiseaza un mesaj corespuncator
 */
void Ui_obj::ui_cautare() {
	string nume, producator;
	cout << "Introduceti numele si producatorul produsului pe care doriti sa il cautati:\n";
	cout << "Introduceti numele: "; cin >> nume;
	cout << "Introduceti producatorul: "; cin >> producator;
	Produs p = serv.cauta_prod(nume, producator);
	//daca trece de asta, produsul exita
	printf("Produsul introus exista!\n");
}

/* 
 * Functie care cere de la utilizator numele dupa ce va face filtrarea
 */
void Ui_obj::filtrare_dupa_nume() {
	string nume;
	cout << "Se afiseaza produsele care au ca nume, ce se introduce mai jos\nIntroduceti numele: "; cin >> nume;
	print(serv.produse_cu_numele_dat(nume));
}

/*
	Functie care afiseaza produsele a caror nume incepe cu o litera daca de la tastatura
*/
void Ui_obj::filtrare_dupa_prima_litera() {
	char prima_litera;
	cout << "Se afiseaza produsele a caror nume incepe cu o litera data\nDati litera: ";
	cin >> prima_litera;
	vector<Produs> rez = serv.produse_a_caror_nume_incepe_cu_o_litera(prima_litera);
	if (rez.size())
		print(rez);
	else
		cout << "Nu exista astfel de produse!\n";
}

/* Functie care cere de la utilizator producatorul dupa ce va face filtrarea
 */
void Ui_obj::filtrare_dupa_producator() {
	string producator;
	cout << "Se afiseaza produsele care au ca nume, ce se introduce mai jos\nIntroduceti numele: "; cin >> producator;
	print(serv.produse_cu_producator_dat(producator));
}

/*
	Functie care preia de la utilizator numele unui produs care va fi adaugat in cos
*/
void Ui_obj::adauga_in_cos() {
	string nume;
	cout << "Dati numele: "; cin >> nume;
	Produs prod{ serv.cauta_prod_pentru_cos(nume) };
	serv.adauga_in_cos(prod);
}



/* Functie care apeleaza functiile neecsare golirii repo ului
 */
void Ui_obj::elibereaza() {
	serv.s_goleste_repo();
}

/*
	Functie care apeleaza raportul cu frecventa tipurilor
*/
void Ui_obj::frecvente() {
	auto dict = serv.produse_frecventa();
	for (auto dic : dict) {
		cout << dic.first << "  apare de: ";
		cout << dic.second << "\n";
	}
}

/*
	Functie care goleste cosul
*/
void Ui_obj::goleste_cos() {
	serv.goleste_cos();
}

/*
	Functie care cere de la utilizatir numarul de elemente poe care le direste in cos
*/
void Ui_obj::genereaza_cos() {
	int nr_produse;
	cout << "Momentan exista " << to_string(serv.get_dim_cos()) << " produse in cos\n";
	cout << "Introduceti numarul de produse care doriti sa fie in cos la final: "; cin >> nr_produse;
	serv.umple_cos(nr_produse);
}

/*
	Functie care sincronizeaza repo din memorie cu repo file
*/
void Ui_obj::actualizare_repo_file() {
	ofstream produse("produse.cvs");
	for (const auto& prod : serv.getAll()) {
		auto linie_de_pus_in_fisier = prod.get_nume() + "," + prod.get_tip() + "," + to_string(prod.get_pret()) + "," + prod.get_producator() + "\n";
		produse << linie_de_pus_in_fisier;
	}
	produse.close();
}



void Ui_obj::load_from_file() {
	serv.load_from_file();
}

void Ui_obj::undo() {
	serv.undo();
}



/* Functie care ruleaza aplicatia si cere de la utilizator date si le trimite la service
 */
void Ui_obj::run() {
	//load from file
	///load_from_file();

	while (true) {
		///actualizare_repo_file();
		meniu();
		int command;
		//cout << "\n";
		cin >> command;
		try {
			//system("CLS");
			cout << "Suma curenta in cos este: " << to_string(serv.get_suma_curenta_cos()) << "\n";
			switch (command) {
			case 1:
				ui_adauga();
				break;
			case 2:
				ui_sterge();
				break;
			case 3:
				ui_modifica();
				break;
			case 4:
				ui_cautare();
				break;
			case 5:
				float pret;
				cout << "Se afiseaza produsele sub un pret dat\nDati pretul: "; cin >> pret;
				print(serv.produse_mai_mici_decat_un_pret(pret));
				break;
			case 6:
				filtrare_dupa_nume();
				break;
			case 7:
				filtrare_dupa_producator();
				break;
			case 8:
				cout << "Produsele sortate dupa nume sunt:\n";
				print(serv.sorteaza_dupa_nume());
				break;
			case 9:
				cout << "Produsele sortate dupa pret sunt:\n";
				print(serv.sorteaza_dupa_pret());
				break;
			case 10:
				cout << "Produsele sortate dupa nume si tip sunt:\n";
				print(serv.sorteaza_dupa_nume_si_tip());
				break;
			case 11:
				print(serv.getAll());
				break;
			case 12:
				filtrare_dupa_prima_litera();
				break;
			case 13:
				frecvente();
				break;
			case 14:
				adauga_in_cos();
				break;
			case 15:
				goleste_cos();
				break;
			case 16:
				genereaza_cos();
				break;
			case 17:
				undo();
				break;
			case 0:
				elibereaza();
				cout << "\nEXIT\n";
				
				return;

			default:
				cout << "Comanda invalida\n";
				exit(1);
			}
		}
		catch (RepositoryException& ex) {
			cout << ex.get_eroare() << '\n';
		}
		catch (ValidationException& ex) {
			cout << ex.get_eroare();
		}
	}
}