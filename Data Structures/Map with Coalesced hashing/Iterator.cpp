#pragma once
#include "Iterator.h"

IteratorDictionar::IteratorDictionar(const Dictionar& c)
	:dict(c)
{
	pozitie_in_lista = -1;
}

TElem IteratorDictionar::element() const {
	//TBA
	return dict.chei[pozitie_in_lista];
}

bool IteratorDictionar::valid() const {
	//TBA
	//return pozitie_cap_lista_curenta != -1 && pozitie_in_lista != -1;
	if (pozitie_in_lista == -1)
		return false;
	return pozitie_in_lista < dict.m;
}

void IteratorDictionar::urmator() {
	//TBA
	/*
	if (dict.urm[pozitie_in_lista] != -1) {
		//mai exista elemente in lista curenta
		pozitie_in_lista = dict.urm[pozitie_in_lista];
	} else {
		//trebuie sa trecem la urmatoarea lista
		pozitie_cap_lista_curenta = dict.urm_capuri_liste[pozitie_cap_lista_curenta];
		pozitie_in_lista = pozitie_cap_lista_curenta;
	}*/

	++pozitie_in_lista;
	while (dict.chei[pozitie_in_lista].first == FREE)
		++pozitie_in_lista;
}

void IteratorDictionar::prim() {
	//TBA
	pozitie_in_lista = 0;
	while (dict.chei[pozitie_in_lista].first == FREE)
		++pozitie_in_lista;
	
}