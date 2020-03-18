#include "MultiDictionarOrdonat.h"
#include <algorithm>
using std::min;
using std::find_if;

#define ever ;;

void MDO::adauga(TCheie c, TValoare v) {
	if (dimensiune == 0) {
		element[radacina].cheie = c;
		element[radacina].valori.push_back(v);
		//stanga[radacina] = radacina + 1;
		//dreapta[radacina] = radacina + 2;
		++dimensiune;
		element[radacina].ocupat = true;
		return;
	}

	++dimensiune;

	// avem cel putin un nod
	vector<TValoare> vect = cauta(c);

	if (vect.size() == 0) {
		//nu exista cheia pe care dorim sa o adaugam
		//facem un nod nou si parcurgem pana la nodul care va fi parintele lui
		
		int nod_curent = radacina; //ne pozitionam pe radacina
		
		for(ever) {
			if (stanga[nod_curent] != -1 && relatie(c, element[nod_curent].cheie))
				nod_curent = stanga[nod_curent];
			else if (dreapta[nod_curent] != -1 && relatie(element[nod_curent].cheie, c))
				nod_curent = dreapta[nod_curent];
			else {
				//am ajuns in nodul care va fi parintele celui pe care il adaugam
				if (stanga[nod_curent] == -1 && dreapta[nod_curent] == -1) {
					//verificam relatia
					//nu are copii
					if (relatie(c, element[nod_curent].cheie)) {
						//adaugam in partea stanga
						stanga[nod_curent] = prim_liber;
						element[prim_liber].cheie = c;
						element[prim_liber].valori.push_back(v);
						element[prim_liber].ocupat = true;
						break;
					}
					else {
						//adaugam in partea dreapta
						dreapta[nod_curent] = prim_liber;
						element[prim_liber].cheie = c;
						element[prim_liber].valori.push_back(v);
						element[prim_liber].ocupat = true;
						break;
					}
				}
				else {
					//are un fiu
					//vom adauga pe fiul care este null
					if (stanga[nod_curent] == -1) {
						//adaugare fiu stang
						stanga[nod_curent] = prim_liber;
						element[prim_liber].cheie = c;
						element[prim_liber].valori.push_back(v);
						element[prim_liber].ocupat = true;
						break;

					} else if(dreapta[nod_curent] == -1) {  
						//adaugare fiu drept
						dreapta[nod_curent] = prim_liber;
						element[prim_liber].cheie = c;
						element[prim_liber].valori.push_back(v);
						element[prim_liber].ocupat = true;
						break;
					}
				}
			}

		}
		actualizare_prim_liber();
	}
	else {
		//cheia exista
		int nod_curent = radacina;
		for (ever) {
			if (element[nod_curent].cheie == c)
				break;
			else if (stanga[nod_curent] != -1 && relatie(c, element[nod_curent].cheie))
				nod_curent = stanga[nod_curent];
			else if (dreapta[nod_curent] != -1 && relatie(element[nod_curent].cheie, element[dreapta[nod_curent]].cheie))
				nod_curent = dreapta[nod_curent];
		}
		//ciclul s-a oprit pe nodul cu cheia c
		element[nod_curent].valori.push_back(v);
	}
}

vector<TValoare> MDO::cauta(TCheie c) const
{
	int aux;
	int nod_curent = radacina;//radacina
	for (ever) {
		aux = nod_curent;
		if (element[nod_curent].cheie == c)
			return element[nod_curent].valori;
		if (relatie(c, element[stanga[nod_curent]].cheie) && stanga[nod_curent] != -1) {
			nod_curent = stanga[nod_curent];
		}
		else if (relatie(element[nod_curent].cheie, c) && dreapta[nod_curent] != -1) {
			nod_curent = dreapta[nod_curent];
		}
		if (stanga[nod_curent] == -1 && dreapta[nod_curent] == -1 && element[nod_curent].cheie != c)
			return vector<TValoare>();
		if(aux == nod_curent)
			return vector<TValoare>();
	}
}



bool MDO::sterge(TCheie c, TValoare v) {
	if (dimensiune == 0)
		return false;
	//if (sterge_rec(0, c, v) == -1)
		//return false;
	//--dimensiune;
	//return true;

	/*auto parinte = -1;
	auto curent = radacina;
	bool are_stang = false;
	bool exista_val = false;

	if (radacina == -1)
		return false;

	while (curent != -1) {
		if (element[curent].cheie == c) {
			for (auto val : element[curent].valori) {
				if (val == v) {
					exista_val = true;
					break;
				}
			}
			if (!exista_val)
				return false;
			break;
		}

		parinte = curent;
		if (c < element[curent].cheie) {
			are_stang = true;
			curent = stanga[curent];
		}
		else {
			are_stang = false;
			curent = dreapta[curent];
		}
	}

	if (exista_val) {
		//trebe sters curent cu parintele parinte
		--dimensiune;
		sterge_rec(0, c, v);
		return true;
	} else {
		//nu exista cheia
		return false;
	}

	*/
	
	if (!exista(c))
		return false;

	--dimensiune;

	auto index = get_index(c);

	if (este_frunza(index)) 
		return sterge_frunza(c, v);
		
	if (are_un_copil(index)) {
		return sterge_nod_cu_un_copil(c, v);
	}
	else {
		//are doi copii
		return sterge_nod_cu_doi_copii(c, v);
	}
}

int MDO::dim() const
{
	return dimensiune;
}

bool MDO::vid() const
{
	return dimensiune == 0;
}

IteratorMDO MDO::iterator() const
{
	return IteratorMDO(*this);
}

void MDO::actualizare_prim_liber() {
	while (element[prim_liber].ocupat)
		++prim_liber;
}

int MDO::sterge_rec(int radacina, TCheie c, TValoare v)
{
	return 1;
}

bool MDO::exista(TCheie c)
{
	int nod_curent = radacina;
	while (element[nod_curent].cheie != c && nod_curent != -1) {
		if (relatie(element[stanga[nod_curent]].cheie, element[nod_curent].cheie)) {
			nod_curent = stanga[nod_curent];
		}
		else {
			nod_curent = dreapta[nod_curent];
		}
	}
	if (nod_curent == -1)
		return false;
	return true;
}

bool MDO::este_frunza(int poz)
{

	// poz este o o pozitie valida din arbore

	return stanga[poz] == -1 && dreapta[poz] == -1;
	
}

bool MDO::sterge_frunza(TCheie c, TValoare v)
{
	int nod_curent = radacina;
	int parinte = -1;
	while (element[nod_curent].cheie != c) {
		if (relatie(element[stanga[nod_curent]].cheie, element[nod_curent].cheie)) {
			parinte = nod_curent;
			nod_curent = stanga[nod_curent];
		}
		else {
			parinte = nod_curent;
			nod_curent = dreapta[nod_curent];
		}
	}

	//nod curent = ndul de sters
	//parinte = parintele lui

	auto it = find_if(element[nod_curent].valori.begin(), element[nod_curent].valori.end(), [v](TValoare val) {return v == val; });
	if (it == element[nod_curent].valori.end()) {
		//nu exista valoarea
		++dimensiune;
		return false;
	}
	else {
		if (element[nod_curent].valori.size() == 1) {
			stanga[nod_curent] = dreapta[nod_curent] = stanga[parinte] = dreapta[parinte] = -1;
			element[nod_curent].cheie = INF;
			element[nod_curent].ocupat = false;

			prim_liber = nod_curent;
			actualizare_prim_liber();
		}
		element[nod_curent].valori.erase(it);
		return true;
	}
}

void MDO::stergere_totala_frunza(TCheie c) {
	int nod_curent = radacina;
	int parinte = -1;
	while (element[nod_curent].cheie != c) {
		if (relatie(element[stanga[nod_curent]].cheie, element[nod_curent].cheie)) {
			parinte = nod_curent;
			nod_curent = stanga[nod_curent];
		}
		else {
			parinte = nod_curent;
			nod_curent = dreapta[nod_curent];
		}
	}

	//nod curent = ndul de sters
	//parinte = parintele lui

	stanga[nod_curent] = dreapta[nod_curent] = stanga[parinte] = dreapta[parinte] = -1;
	element[nod_curent].cheie = INF;
	element[nod_curent].ocupat = false;
	element[nod_curent].valori.clear();
}


bool MDO::are_un_copil(int nod)
{
	if (stanga[nod] == -1 && dreapta[nod] != -1)
		return true;
	if (dreapta[nod] == -1 && stanga[nod] != -1)
		return true;
	return false;
}

void MDO::stergere_totala_nod_cu_fiu_drept(TCheie c) {
	int nod_curent = radacina;
	int parinte = -1;
	while (element[nod_curent].cheie != c) {
		if (relatie(element[stanga[nod_curent]].cheie, element[nod_curent].cheie)) {
			parinte = nod_curent;
			nod_curent = stanga[nod_curent];
		}
		else {
			parinte = nod_curent;
			nod_curent = dreapta[nod_curent];
		}
	}

	//nod curent = ndul de sters
	//parinte = parintele lui

	if (stanga[parinte] != -1) {
		//nodul de sters e in stanga parintelui

		//fiul nodului de sters este in dreapta
		stanga[parinte] = dreapta[nod_curent];
		dreapta[nod_curent] = -1;
		element[nod_curent].cheie = INF;
		element[nod_curent].valori.clear();
		element[nod_curent].ocupat = false;
		return;
	}
	else {
		//nodul de sters e in dreapta parintelui

		dreapta[parinte] = dreapta[nod_curent];
		dreapta[nod_curent] = -1;
		element[nod_curent].cheie = INF;
		element[nod_curent].valori.clear();
		element[nod_curent].ocupat = false;
		return;
	}

}

bool MDO::sterge_nod_cu_un_copil(TCheie c, TValoare v)
{

	int nod_curent = radacina;
	int parinte = -1;
	while (element[nod_curent].cheie != c) {
		if (relatie(element[stanga[nod_curent]].cheie, element[nod_curent].cheie)) {
			parinte = nod_curent;
			nod_curent = stanga[nod_curent];
		}
		else {
			parinte = nod_curent;
			nod_curent = dreapta[nod_curent];
		}
	}

	//nod curent = ndul de sters
	//parinte = parintele lui


	auto it = find_if(element[nod_curent].valori.begin(), element[nod_curent].valori.end(), [v](TValoare val) {return v == val; });
	if (it == element[nod_curent].valori.end()) {
		++dimensiune;
		return false;
	}

	//exista valoarea
	if (element[nod_curent].valori.size() > 1) {
		element[nod_curent].valori.erase(it);
		return true;
	}
	else {
		//se sterge si cheia
		if (parinte == -1) {
			//se sterge radacina si are un singur fiu
			if (stanga[radacina] != -1) {
				//noua radacina e fiul stang
				prim_liber = radacina;
				//actualizare_prim_liber();

				element[radacina].valori.clear();
				radacina = stanga[radacina];
				element[radacina].ocupat = false;
				return true;
			}
			else {
				//noua rad e fiul drept
				prim_liber = radacina;
				//actualizare_prim_liber();

				element[radacina].valori.clear();
				radacina = dreapta[radacina];
				element[radacina].ocupat = false;
				return true;
			}
		}
		else {
			if ((stanga[parinte] == nod_curent)) {
				//nodul de sters se afla in sanga parintelui

				if (stanga[nod_curent] != -1) {
					//fiul nodului de sters este in stanga
					stanga[parinte] = stanga[nod_curent];
					stanga[nod_curent] = -1;
					element[nod_curent].cheie = INF;
					element[nod_curent].valori.erase(it);
					element[nod_curent].ocupat = false;
					
					prim_liber = nod_curent;
					//actualizare_prim_liber();

					return true;
				}
				else {
					//fiul nodului de sters este in dreapta
					stanga[parinte] = dreapta[nod_curent];
					dreapta[nod_curent] = -1;
					element[nod_curent].cheie = INF;
					element[nod_curent].valori.erase(it);
					element[nod_curent].ocupat = false;
					
					prim_liber = nod_curent;
					//actualizare_prim_liber();

					return true;
				}
			}
			else {
				//nodul de sters se afla in dreapta parintelui
				if (stanga[nod_curent] != -1) {
					//fiul nodului de sters se afla in stanga iar parintele este la dreapta
					dreapta[parinte] = stanga[nod_curent];
					stanga[nod_curent] = -1;
					element[nod_curent].cheie = INF;
					element[nod_curent].valori.erase(it);
					element[nod_curent].ocupat = false;
					
					prim_liber = nod_curent;
					//actualizare_prim_liber();
					
					return true;
				}
				else {
					//fiul nodului de sters este in dreapta
					dreapta[parinte] = dreapta[nod_curent];
					dreapta[nod_curent] = -1;
					element[nod_curent].cheie = INF;
					element[nod_curent].valori.erase(it);
					element[nod_curent].ocupat = false;
					
					prim_liber = nod_curent;
					//actualizare_prim_liber();
					
					return true;
				}

			}
		}
	}
}

bool MDO::sterge_nod_cu_doi_copii(TCheie c, TValoare v)
{
	int nod_curent = radacina;
	int parinte = -1;
	while (element[nod_curent].cheie != c) {
		if (relatie(element[stanga[nod_curent]].cheie, element[nod_curent].cheie)) {
			parinte = nod_curent;
			nod_curent = stanga[nod_curent];
		}
		else {
			parinte = nod_curent;
			nod_curent = dreapta[nod_curent];
		}
	}

	//nod curent = ndul de sters
	//parinte = parintele lui
	
	//nod_curent este nodul pe care se gaeste cheia

	auto it = find_if(element[nod_curent].valori.begin(), element[nod_curent].valori.end(), [v](TValoare val) {return v == val; });
	if (it == element[nod_curent].valori.end()) {
		//nu exista valoarea de sters
		++dimensiune;
		return false;
	}

	if (element[nod_curent].valori.size() == 1) {
		//se sterge cu totul cheia si nodul din arbore

		auto min_din_partea_dreapta = get_inorder_succesor(nod_curent);
		//mutam informatiile utile din succesor in nodul de sters
		element[nod_curent].cheie = element[min_din_partea_dreapta].cheie;
		element[nod_curent].valori = element[min_din_partea_dreapta].valori;
		
		if (este_frunza(min_din_partea_dreapta)) {
			stergere_totala_frunza(element[min_din_partea_dreapta].cheie);
			return true;
		}
		else {
			stergere_totala_nod_cu_fiu_drept(element[min_din_partea_dreapta].cheie);
			return true;
		}

	} else {
		// se sterge doare o valoare fara a se moifica structura arborelui
		element[nod_curent].valori.erase(it);
		return true;
	}

}

int MDO::get_index(TCheie c)
{
	int nod_curent = radacina;
	while (element[nod_curent].cheie != c && nod_curent != -1) {
		if (relatie(element[stanga[nod_curent]].cheie, element[nod_curent].cheie)) {
			nod_curent = stanga[nod_curent];
		}
		else {
			nod_curent = dreapta[nod_curent];
		}
	}
	return nod_curent;
}

int MDO::get_inorder_succesor(int nod)
{
	if (dreapta[nod] != -1)
		return minValue(dreapta[nod]);

	return maxValue(stanga[nod]);
}

int MDO::minValue(int nod)
{
	int curent = nod;
	while (stanga[curent] != -1) {
		curent = stanga[curent];
	}
	return curent;
}

int MDO::maxValue(int nod) {
	int curent = nod;
	while (dreapta[curent] != -1)
		curent = dreapta[curent];
	return curent;
}