#include "Produs.h"



bool compara_nume(const Produs& p1, const Produs& p2) {
	/*
		functie care compara doua obiecte de tip Produs dupa campul nume
		pre:p1: produs valid, p2: produs valid
		post: true daca numele lui p1 este alfabetic mai mic decat numele lui p2
	*/
	
	return p1.get_nume() < p2.get_nume();
}

bool compara_pret(const Produs& p1, const Produs& p2) {
	/*
		functie care compara doua obiecte de tip Produs dupa campul pret
		pre:p1,p2 produse valide
		post: true daca pretul lui p1 este mai mic decat pretul lui p2
	*/

	return p1.get_pret() < p2.get_pret();
}