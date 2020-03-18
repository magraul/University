#include "Iterator.h"
#include <algorithm>

using namespace std;

#define INF  -99999;

class MDO;

IteratorMDO::IteratorMDO(const MDO& d) :d(d) {
	curent = d.radacina;
	for (auto nod : d.element)
		if(nod.valori.size())
			bst.push_back(nod);
	auto min = d.get_min();
	//sort(bst.begin(), bst.end());
	for (auto& i : bst) {
		for (auto& j : bst)
			if (d.relatie(i.cheie,j.cheie))
				std::swap(i, j);
	}
}

TElem IteratorMDO::element()
{
	/*while (curent != -1)
	{
		stiva.push(curent);
		curent = d.stanga[curent];
	}
	curent = stiva.top();
	stiva.pop();*/
	return std::make_pair(bst[curent].cheie, bst[curent].valori[0]);
	
}

IteratorMDO::~IteratorMDO() {}

void IteratorMDO::prim() {
	curent = d.radacina;
}

void IteratorMDO::urmator() {
	/*/
	if (d.stanga[curent] != -1 && d.relatie(d.element[d.stanga[curent]].cheie, d.element[curent].cheie))
		curent = d.stanga[curent];
	else if (d.dreapta[curent] != -1 && d.relatie(d.element[d.dreapta[curent]].cheie, d.element[curent].cheie))
		curent = d.dreapta[curent];
	else curent = -1;*/
	++curent;
	/*if (d.element[curent].valori.size() == 0) {
		//pozitie neocupata
		while (d.element[curent].valori.size() == 0)
			++curent;
	}*/
}

bool IteratorMDO::valid() const
{
	if (d.dim() == 0)
		return false;
	if (curent >= bst.size())
		return false;
	if (curent == -1)
		return false;
	return true;

	//return !stiva.empty() || curent != -1;
}



