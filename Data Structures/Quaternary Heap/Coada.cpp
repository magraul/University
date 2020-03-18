#include "Coada.h"
#include <algorithm>

using namespace std;

CP::CP(Relatie r) {
	dimensiune = 1;
	R = r;
	elemente.push_back(make_pair(1000, -100000));
}

int calculeaza_pozitie_parinte(int index) {
	return (index - 2) / 4 + 1;
}

bool are_parinte(int index) {
	return index > 1;
}

void CP::adauga(TElem e, TPrioritate p) {
	elemente.push_back(make_pair(e, p));
	if (dimensiune > 1) {
		auto fiu = elemente[dimensiune];
		int index_fiu = dimensiune;
		auto poz_parinte = calculeaza_pozitie_parinte(dimensiune);
		auto parinte = elemente[poz_parinte];
		while (R(fiu.second, parinte.second) && are_parinte(index_fiu)) {
			auto pozitie_parinte_viitor = calculeaza_pozitie_parinte(poz_parinte);
			auto tmp1 = fiu;
			auto tmp = elemente[index_fiu];
			
			fiu = parinte;
			elemente[index_fiu] = elemente[poz_parinte];
			
			parinte = tmp1;
			elemente[poz_parinte] = tmp;
			index_fiu = poz_parinte;
			parinte = elemente[pozitie_parinte_viitor];
			
			poz_parinte = pozitie_parinte_viitor;
			fiu = elemente[index_fiu];
		}
	}
	++dimensiune;
}

Element CP::element() const {
	if (vida())
		throw std::exception();
	return elemente[1];
}


int get_fiu_bun(vector<Element> elemente, int index_parinte) {
	auto fiu3 = 4 * index_parinte;
	auto fiu2 = fiu3 - 1;
	auto fiu1 = fiu3 - 2;
	auto fiu4 = fiu3 + 1;
	//if (elemente[fiu1].second == elemente[fiu2].second == elemente[fiu3].second == elemente[fiu4].second)
		
	int aux = fiu1;
	//int minim = min(elemente[fiu1].second, elemente[fiu2].second, elemente[fiu3].second, elemente[fiu4].second);
	auto min = elemente[fiu1].second;
	
	if (fiu3 <= elemente.size()-1 && min > elemente[fiu3].second) {
		min = elemente[fiu3].second;
		aux = fiu3;
	}
	
	if (fiu2 <= elemente.size() - 1 && min > elemente[fiu2].second) {
		min = elemente[fiu2].second;
		aux = fiu2;
	}
	
	if (fiu4 <= elemente.size() - 1 && min > elemente[fiu4].second) {
		min = elemente[fiu4].second;
		aux = fiu4;
	}
	return aux;
}

int get_index_fiu1(int index_parinte) {
	return 4 * index_parinte - 2;
}

int get_index_fiu2(int index_parinte) {
	return 4 * index_parinte - 1;
}

int get_index_fiu3(int index_parinte) {
	return 4 * index_parinte;
}

int get_index_fiu4(int index_parinte) {
	return 4 * index_parinte + 1;
}


void parcurge(vector<Element>& elemente, int nodeIndex) {
	auto aux = elemente.size() - 1;
	if (aux > 0) {
		int fiu1, fiu2, fiu3, fiu4, minIndex;

		fiu1 = get_index_fiu1(nodeIndex);
		fiu2 = get_index_fiu2(nodeIndex);
		fiu3 = get_index_fiu3(nodeIndex);
		fiu4 = get_index_fiu4(nodeIndex);
		if (fiu1 > aux)
			return;
			/*if (fiu1 >= aux)
				return;
			else
				minIndex = fiu1;*/
		else {
			minIndex = get_fiu_bun(elemente, nodeIndex);
		}
		if (elemente[nodeIndex].second >= elemente[minIndex].second) {
			auto tmp = elemente[minIndex];
			elemente[minIndex] = elemente[nodeIndex];
			elemente[nodeIndex] = tmp;
			parcurge(elemente, minIndex);
		}
	}
}

Element CP::sterge() {

	if (vida())
		throw std::exception();
	auto for_return = elemente[1];
	--dimensiune;
		elemente[1] = elemente[dimensiune];
		elemente.erase(elemente.end() - 1);
		if (dimensiune > 0)
			parcurge(elemente, 1);

	return for_return;
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	if (vida())
		throw std::exception();
	auto for_return = elemente[1];
	
	elemente[1] = elemente[dimensiune-1]; // inlocuim radacina cu ultimul nod adaugat in heap
	elemente.erase(elemente.end() - 1);
	--dimensiune;
	

	int index_parinte = 1;//indexarea incepe de la 1
	auto index_fiu = get_fiu_bun(elemente, index_parinte);//luam fiul cu prioritatea cea mai mica
	
	while (index_fiu < dimensiune)
	{//cat timp fiul nu a ajuns la finalul vectorului
		auto fiu = elemente[index_fiu];
		//interschimbare fiu cu parinte
		auto tmp = elemente[index_fiu];
		elemente[index_fiu] = elemente[index_parinte];
		elemente[index_parinte] = tmp;

		/*index_parinte = index_fiu;
		if (4 * index_parinte - 2 < dimensiune) {
			index_fiu = get_fiu_bun(elemente, index_parinte);
			if (elemente[index_parinte].second < elemente[get_fiu_bun(elemente, index_parinte)].second)
				break;
		}
		else break;
		index_parinte = index_fiu;
		index_fiu = get_fiu_bun(elemente, index_parinte);
	}

	//elemente.erase(elemente.end() - 1);
	return for_return;
}*/