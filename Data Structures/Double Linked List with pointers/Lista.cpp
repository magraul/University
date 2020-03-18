#include "Lista.h"
#include "Iterator.h"
#include <iostream>
#include <exception>
#define _CRTDBG_MAP_ALLOC
#include <crtdbg.h>

Lista::Lista() {
	//ala bunu
	head = tail = nullptr;
	//head->prev = nullptr;
	//tail->next = nullptr;
	//head->next = head->prev = tail->next = tail->prev = nullptr;
}

Lista::~Lista() {
	IteratorLP it(*this);
	while (it.valid() && (dimensiune-1))
	{
		it.urmator();
		free(it.nod_curent->prev);
		--dimensiune;
	}
}

int Lista::dim() const {
	return dimensiune;
}

bool Lista::vida() const {
	return dimensiune == 0;
}

IteratorLP Lista::prim() const {
	IteratorLP it(*this);
	it.nod_curent = head;
	return it;
}

TElem Lista::element(IteratorLP poz) const {
	if (!poz.valid())
		throw std::exception();
	return poz.element();
}

TElem Lista::modifica(IteratorLP poz, TElem e) {
	if (poz.valid()) {
		TElem val = poz.nod_curent->value;
		NodeDL *nod = poz.nod_curent;
		nod->value = e;
		return val;
	}
	else
		throw std::exception();
}

void Lista::adaugaSfarsit(TElem e) {
	++dimensiune;
	NodeDL *neww = (NodeDL *)malloc(sizeof(NodeDL));
	neww->value = e;
	neww->next = neww->prev = nullptr;
	if (tail == nullptr) {
		head = tail = neww;
	}
	else {
		tail->next = neww;
		neww->prev = tail;
		tail = neww;
	}
}

void Lista::adauga(IteratorLP poz, TElem e) {
	if (!poz.valid())
		throw std::exception();
	++dimensiune;
	NodeDL *neww = (NodeDL *)malloc(sizeof(NodeDL));
	neww->value = e;
	neww->next = neww->prev = nullptr;
	if (head) {
		neww->prev = poz.nod_curent->prev;
		poz.nod_curent->prev->next = neww;
		neww->next = poz.nod_curent;
		poz.nod_curent->prev = neww;
		TElem aux = poz.nod_curent->value;
		poz.nod_curent->value = e;
		poz.nod_curent->next->value = aux;
		return;
	}
	head = tail = neww;
}


TElem Lista::sterge(IteratorLP poz) {
	if (poz.valid()) {
		--dimensiune;
		TElem aux = poz.nod_curent->value;
		NodeDL* nod_cure = poz.nod_curent;
		if (nod_cure == head) {
			//NodeDL *head_nou = nod_cure->next;
			//head_nou 
			head = head->next;
			if (head != nullptr)
				head->prev = nullptr;
			else
				tail = nullptr;
			return aux;
		}
		if (tail == nod_cure) {
			tail = tail->prev;
			tail->next = nullptr;
		}
		else {
			nod_cure->prev->next = nod_cure->next;
			nod_cure->next->prev = nod_cure->prev;
		}
		free(nod_cure);
		return aux;
	}
	else throw std::exception();
}

IteratorLP Lista::cauta(TElem e) const {
	IteratorLP it(*this);
	while (it.valid() && it.element() != e)
	{
		it.urmator();
	}
	return it;
}