#pragma once
#include "Iterator.h"
#include <exception>

IteratorLP::IteratorLP(const Lista& l)
	:list(l)
{
	nod_curent = l.head;
}

TElem IteratorLP::element() const {
	//TBA
	if (!valid())
		throw std::exception();
	return nod_curent->value;
}

bool IteratorLP::valid()const {
	//TBA
	return nod_curent != nullptr;
}

void IteratorLP::urmator() {
	//TBA
	if (!valid())
		throw std::exception();
	nod_curent = nod_curent->next;
	
}

void IteratorLP::prim() {
	//TBA
	nod_curent = list.head;
}
