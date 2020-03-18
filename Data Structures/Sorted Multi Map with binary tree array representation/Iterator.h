#pragma once
#include "MultiDictionarOrdonat.h"
#include <stack>
using std::stack;
using std::vector;

typedef int TCheie;
typedef int TValoare;
typedef std::pair<TCheie, TValoare> TElem;

class Nod;

class IteratorMDO {
	friend class MDO;
private:

	//contine o referinta catre containerul pe care il itereaza
	const MDO& d;

	/* aici e reprezentarea specifica a iteratorului*/

	int curent; //elementul curent din container
	stack<int> stiva;
	vector<Nod> bst;

public:
	IteratorMDO(const MDO& c);
	//reseteaza pozitia iteratorului la inceputul containerului
	void prim();

	//muta iteratorul in container
	// arunca exceptie daca iteratorul nu e valid
	void urmator();

	//verifica daca iteratorul e valid (indica un element al containerului)
	bool valid() const;

	//returneaza valoarea elementului din container referit de iterator
	//arunca exceptie daca iteratorul nu e valid
	TElem element();
	~IteratorMDO();
};
