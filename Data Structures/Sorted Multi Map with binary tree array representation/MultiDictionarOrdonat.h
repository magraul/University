#pragma once
#include <iostream>
#include <vector>
#include "Iterator.h"
//MDO.h


#define INF  -99999;
typedef int TCheie;
typedef int TValoare;

typedef std::pair<TCheie, TValoare> TElem;

typedef bool(*Relatie)(TCheie, TCheie);

using std::vector;

class Nod {
public:
	TCheie cheie;
	vector<TValoare> valori;
	bool ocupat;
};

class MDO {
	friend class IteratorMDO;
private:

	/* aici e reprezentarea */

	Nod element[10000];
	int stanga[10000];
	int dreapta[10000];
	int radacina;
	int prim_liber;
	Relatie relatie;
	int dimensiune;


	void actualizare_prim_liber();
	int sterge_rec(int radacina, TCheie, TValoare);
	bool exista(TCheie);
	bool este_frunza(int);
	bool sterge_frunza(TCheie ,TValoare);
	bool are_un_copil(int);
	bool sterge_nod_cu_un_copil(TCheie, TValoare);
	bool sterge_nod_cu_doi_copii(TCheie, TValoare);
	int get_index(TCheie);
	int get_inorder_succesor(int);
	int minValue(int);
	int maxValue(int);
	void stergere_totala_frunza(TCheie);
	void stergere_totala_nod_cu_fiu_drept(TCheie);
	TCheie get_min()const {
		int r = radacina;
		while (r != -1)
			r = stanga[r];
		return element[r].cheie;
	}

public:

	// constructorul implicit al MultiDictionarului Ordonat
	MDO(Relatie r) :relatie{ r } {
		for (auto& i : stanga)
			i = -1;
		for (auto& i : dreapta)
			i = -1;
		radacina = 0;
		dimensiune = 0;
		prim_liber = 1;
		for (auto& nod : element) {
			nod.ocupat = false;
			nod.cheie = INF;
		}
	}

	// adauga o pereche (cheie, valoare) in MDO
	void adauga(TCheie c, TValoare v);

	//cauta o cheie si returneaza vectorul de valori asociate
	vector<TValoare> cauta(TCheie c) const;

	//sterge o cheie si o valoare 
	//returneaza adevarat daca s-a gasit cheia si valoarea de sters
	bool sterge(TCheie c, TValoare v);

	//returneaza numarul de perechi (cheie, valoare) din MDO 
	int dim() const;

	//verifica daca MultiDictionarul Ordonat e vid 
	bool vid() const;

	// se returneaza iterator pe MDO
	// iteratorul va returna perechile ordine dupa relatia de ordine
	IteratorMDO iterator() const;

	// destructorul 	
	~MDO() {}



};
