#include "Validator.h"
#include <string>
using std::string;

Validator::Validator()
{
}

/* Functie care valideaza un produs fara sa il modifice
 * pre: produs 
 * post se arunca exceptie daca nu a putut fi validat
 */
void Validator::valideaza(const Produs& p) {
	vector<string> errors;
	if (p.get_nume().size() == 0) errors.push_back("Numele este invalid!");
	if (p.get_tip().size() == 0) errors.push_back("Tipul este invalid!");
	if (p.get_producator().size() == 0) errors.push_back("Producatorul este invalid!");
	if (p.get_pret() < 0.0) errors.push_back("Pretul este invalid!");
	if (errors.size() > 0)
		throw ValidationException(errors);
}
