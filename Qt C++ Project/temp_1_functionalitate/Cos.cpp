#include "Cos.h"
#include <fstream>




Coss::~Coss()
{

}

void Coss::sincronizare_fisier_cos_repo_cos_si_tabel() {
		ifstream in("cos.cvs");
		string linie;
		elems_cos.clear();
		while (!in.eof()) {
			getline(in, linie);
			auto componente = split(linie, ',');
			elems_cos.push_back(Produs{ componente[0], componente[1], stof(componente[2]), componente[3] });
		}
		in.close();
}
