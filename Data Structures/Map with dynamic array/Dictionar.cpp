#include "Dictionar.h"
#include "Iterator.h"
#include <iostream>

// aici implementarea operatiilor din Dictionar.h



Dictionar::Dictionar() {
	vector = (TElem *) malloc(sizeof(TElem )*capacitate);
}


IteratorDictionar Dictionar::iterator() const {
	IteratorDictionar *it = new IteratorDictionar(*this);
	return *it;
}

Dictionar::~Dictionar() {
	// TBA
	delete[] this->vector;
}


// restul operatiilor

TValoare Dictionar::adauga(TCheie c, TValoare v) {
	TElem e;
	e.first = c;
	e.second = v;
	if (!dimensiune) {
		vector[dimensiune++] = e;
		return NULL_TVALOARE;
	}
	bool gasit = false;
	for (int i = 0; i < dimensiune; i++) {
		if (vector[i].first == c) {
			gasit = true;
			break;
		}
	}
	if(!gasit){
		//nu mai exista cheia
		if (dimensiune < capacitate) {
			vector[dimensiune++] = e;
		}
		else {
			//redimensionare
			TElem *vect_aux = (TElem *)malloc(sizeof(TElem)*capacitate*2);
			for (int i = 0; i < dimensiune; i++) {
				vect_aux[i] = vector[i];
			}
			vect_aux[capacitate] = e;
			free(vector);
			vector = vect_aux;
			capacitate *= 2;
			++dimensiune;
		}

		return NULL_TVALOARE;
	}
	else {
		//duplicat
		int pos = get_pos_cheie(c);
		if (pos != -1) {
			int aux = vector[pos].second;
			vector[pos].second = v;
			return aux;
		}
		else {
			printf("eroare la duplicat");
		}
	}
}

int Dictionar::get_pos_cheie(TCheie c) {
	for (int i = 0; i <dimensiune; i++) {
		if (vector[i].first == c) {
			return i;
		}
	}
	return -1;
}

TValoare Dictionar::cauta(TCheie c) const {
	for (int i = 0; i < dimensiune; i++) {
		if (vector[i].first == c) {

			return vector[i].second;
		}
	}
	return NULL_TVALOARE;
}

TValoare Dictionar::sterge(TCheie c) {
	int aux;
	int poz_de_sters = -1;
	for (int i = 0; i < dimensiune; i++) {
		if (vector[i].first == c) {
			poz_de_sters = i;
			aux = vector[i].second;
			break;
		}
	}
	if (poz_de_sters >= 0) {
		for (int i = poz_de_sters; i < dimensiune-1; i++) {
			vector[i] = vector[i + 1];
		}
		--dimensiune;
		return aux;
	}
	else {
		return NULL_TVALOARE;
	}
}

//returneaza numarul de perechi (cheie, valoare) din dictionar
int Dictionar::dim() const {
	return dimensiune;
}

//verifica daca dictionarul e vid
bool Dictionar::vid() const {
	return dimensiune == 0;
}
