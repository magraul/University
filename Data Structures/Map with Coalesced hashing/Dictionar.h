#pragma once
//Dictionar.h
#include <iostream>
typedef int TCheie;
typedef int TValoare;

#define FREE -99999

typedef std::pair<TCheie, TValoare> TElem;

#define NULL_TVALOARE -999999
constexpr auto capac = 60001;
class Dictionar {
	friend class IteratorDictionar;
private:

	/* aici e reprezentarea */
	int m;
	int dimensiune;
	TElem* chei;
	int* urm;
	int prim_liber;
	int prim;
	int cnt = 0;
	//int urm_capuri_liste[capac];// il folosim pentru iterator, pentru a avea o ordine in care apar valorile de dispersie
	int cap_curent;

	void actualizare_prim_liber();

	//functia de dispersie
	int h_prime(TCheie k, int m) {
		return k % m;
	}

	int quadratic_probing(TCheie k, int m, int i)
	{
		k = abs(k);
		int c1, c2 = c1 = 1;
		int val = (h_prime(k, m) + c1 * i + c2 * i*i) % m;
		return val;
	}

public:

	// constructorul implicit al dictionarului
	Dictionar();

	// adauga o pereche (cheie, valoare) in dictionar	
	//daca exista deja cheia in dictionar, inlocuieste valoarea asociata cheii si returneaza vechea valoare
	// daca nu exista cheia, adauga perechea si returneaza null: NULL_TVALOARE
	TValoare adauga(TCheie c, TValoare v);

	//cauta o cheie si returneaza valoarea asociata (daca dictionarul contine cheia) sau null: NULL_TVALOARE
	TValoare cauta(TCheie c);

	//sterge o cheie si returneaza valoarea asociata (daca exista) sau null: NULL_TVALOARE
	TValoare sterge(TCheie c);

	//returneaza numarul de perechi (cheie, valoare) din dictionar 
	int dim() const;

	//verifica daca dictionarul e vid 
	bool vid() const;

	// se returneaza iterator pe dictionar
	IteratorDictionar iterator() const;


	// destructorul dictionarului	
	~Dictionar();

};