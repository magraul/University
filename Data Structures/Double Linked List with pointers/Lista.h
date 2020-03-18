#pragma once

typedef int TElem;

typedef struct node {
	TElem value;
	struct node *next, *prev;
}NodeDL;


class IteratorLP;

class Lista {
	friend class IteratorLP;

private:
	/* aici e reprezentarea */

	NodeDL *head;
	NodeDL *tail;
	int dimensiune;

public:
	// constructor
	Lista();

	// returnare dimensiune
	int dim() const;

	// verifica daca lista e vida
	bool vida() const;

	// prima pozitie din lista
	IteratorLP prim() const;

	// returnare element de pe pozitia curenta
	//arunca exceptie daca poz nu e valid
	TElem element(IteratorLP poz) const;

	// modifica element de pe pozitia poz si returneaza vechea valoare
	//arunca exceptie daca poz nu e valid
	TElem modifica(IteratorLP poz, TElem e);

	// adaugare element la sfarsit
	void adaugaSfarsit(TElem e);

	// adaugare element pe o pozitie poz
	//dupa adaugare poz este pozitionat pe elementul adaugat
	//arunca exceptie daca poz nu e valid
	void adauga(IteratorLP poz, TElem e);

	// sterge element de pe o pozitie poz si returneaza elementul sters
	//dupa stergere poz este pozitionat pe elementul de dupa cel sters
	//arunca exceptia daca poz nu e valid
	TElem sterge(IteratorLP poz);

	// cauta element si returneaza prima pozitie pe care apare (sau iterator invalid)
	IteratorLP cauta(TElem e) const;

	//destructor

	~Lista();

};