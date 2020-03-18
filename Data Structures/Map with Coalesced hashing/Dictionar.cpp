#include "Dictionar.h"
#include "Iterator.h"
Dictionar::Dictionar() {
	prim_liber = 0;
	m = capac;
	dimensiune = 0;
	urm = new int[capac];
	for (int i=0;i<capac;i++)
		urm[i] = -1;
	//for (auto& i : urm_capuri_liste)
		//i = -1;
	chei = new TElem[capac];
	for (int i=0;i<capac;i++)
		chei[i] = std::make_pair(FREE, FREE);
	prim = -1;
	cap_curent = -1;

}

Dictionar::~Dictionar() {
}

void Dictionar::actualizare_prim_liber() {
	//se actualizează primLiber după ce locația a fost ocupată
	//operația nu este în interfața containerului

	prim_liber = prim_liber + 1;
	while (prim_liber <= m - 1 && chei[prim_liber].first != FREE)
		prim_liber = prim_liber + 1;
}

TValoare Dictionar::adauga(TCheie c, TValoare v) {
	++dimensiune;
	int j;
	int i = quadratic_probing(c, m, 1);
	if (prim == -1)
		prim = i; //prima valoare de dispersie
	
	if (chei[i].first == FREE) {
		//locatie libera, adaugam
		//se face un nou cap de lista

		if (cap_curent == -1) {
			cap_curent = i;
		}
		else {
			//urm_capuri_liste[cap_curent] = i;
			cap_curent = i;
		}

		chei[i].first = c;
		chei[i].second = v;
		if (i == prim_liber)
			this->actualizare_prim_liber();
	} else {
		//coliziune

		//adaugam la finalul listei care este memorata la locatia i

		while (i != -1 && chei[i].first != c) {
			j = i;
			i = urm[i];
		}
		if (i != -1) {
			// dublura cheie
			--dimensiune;
			auto for_return = chei[i].second;
			chei[i].second = v;
			return for_return;
		}
		else {
			//am ajuns la finalul listei curente
			chei[prim_liber].first = c;
			chei[prim_liber].second = v;
			urm[j] = prim_liber;
			actualizare_prim_liber();
		}
	}
	return NULL_TVALOARE;
}

TValoare Dictionar::cauta(TCheie c)
{
	//if (chei[quadratic_probing(c, m, 1)].first == FREE)
		//return NULL_TVALOARE;
	
	/*int j;
	int i = quadratic_probing(c, m, 1);
	if(chei[i].first == FREE)
		return NULL_TVALOARE;
	else {
		while (chei[i].first != c && i != -1) {
			i = urm[i];
		}
		if(i != -1)
			return chei[i].second;
		return NULL_TVALOARE;
	}*/

	int poz;
	for (poz = 0; poz < m; poz++)
		if (chei[poz].first == c)
			return chei[poz].second;
	return NULL_TVALOARE;
}

TValoare Dictionar::sterge(TCheie c) {
	if (dim() == 0)
		return NULL_TVALOARE;
	auto de_sters = cauta(c);
	if (de_sters == NULL_TVALOARE) {
		//nu exista cheia de sters
		return de_sters;
	}
	--dimensiune;
	
	//std::cout<<cnt++<<" " << dimensiune << "\n";
	// cheia de sters exista
	int i = quadratic_probing(c, m, 1);

	if (chei[i].first == c) {
		auto for_return = chei[i].second;
		int aux = i;
		//cheia este capul unei liste
		//tragem toate nodurile din lista aceasta in spate cu o pozitie
		while (urm[i] != -1 && abs(urm[i])<capac) {
			chei[i] = chei[urm[i]];
			aux = i;
			i = urm[i];
		}
		//punem null pe ultima pozitie in lista
		chei[i] = std::make_pair(FREE, FREE);
		urm[aux] = -1;
		return for_return;







		/*
		//cheia este exact pe valoarea de dispersie
		auto for_return = chei[i].second;
		
		if (urm[i] != -1) {
			int j;
			//in i este stocata o lista si trebuie sa modificam lista de urmatori pentru capurile listelor
			for (j = 0; j < m; j++) {
				if (urm_capuri_liste[j] == i) {
					//j pointeaza la i in lista capurilor
					urm_capuri_liste[j] = urm[i];
					urm_capuri_liste[urm[i]] = urm_capuri_liste[i];
					urm_capuri_liste[i] = -1;
				}
			}
		}
		//stergem cheia
		chei[i].first = FREE;
		chei[i].second = FREE;
		urm[i] = -1;
		--dimensiune;
		return for_return;*/
	}
	else {
		//cheia de sters se afla in interiorul unei liste
		//aflam cine poiteaza la ea
		if (urm[i] == -1)
			return de_sters;
		int precedent = i;
		while (chei[i].first != c && i != -1) {
			precedent = i;
			i = urm[i];
		}
		auto for_return = chei[i].second;
		urm[precedent] = urm[i];
		urm[i] = -1;
		chei[i] = std::make_pair(FREE, FREE);
		return for_return;
		
		
		/*if (precedent == -1) {
			auto for_return = chei[i].second;
			urm[i] = urm[urm[i]];
			chei[i] = std::make_pair(FREE, FREE);
			return for_return;
		}
		else {
			auto for_return = chei[urm[i]].second;
			urm[precedent] = urm[i];

		}
		auto for_return = chei[urm[i]].second;
		urm[precedent] = urm[i];
		chei[i] = std::make_pair(FREE, FREE);
		return for_return;
		*/
		
		
		
		
		
		
		
		
		
		
		/*
		int aux;
		//cheia se afla undeva intr-o lista
		//mutam indicele pe pozitia pe care dorim sa o stergem
		while (chei[i].first != c) {
			aux = i;
			i = urm[i];
		}
		ret();
		auto for_return = chei[i].second;
		urm[aux] = urm[i];
		chei[i].first = FREE;
		chei[i].second = FREE;
		urm[i] = -1;
		--dimensiune;
		return for_return;*/
	}
}


int Dictionar::dim() const
{
	return dimensiune;
}

bool Dictionar::vid() const
{
	return dimensiune == 0;
}

IteratorDictionar Dictionar::iterator() const {
	IteratorDictionar *it = new IteratorDictionar(*this);
	if (!vid()) {
		int poz = 0;
		while (poz < m) {
			if (chei[poz].first != FREE)
				break;
			else
				++poz;
		}
		it->pozitie_in_lista = poz;
	}
	return *it;
}


