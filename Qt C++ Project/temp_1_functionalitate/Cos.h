#pragma once
#include "Produs.h"
#include <vector>
#include "Repository.h"
#include "Observer.h"


using std::vector;

class Coss:public Observable
{
	vector<Produs> elems_cos;
	const Repository& repo;



	void actualizare_fis_cos_memo_cos_si_table();

public:

	Coss(const Repository& rep) :repo{ rep } {};
	
	void adauga(const Produs& p) {
		elems_cos.push_back(p);
		//anuntam ca s-a schimbat ceva in cos
		notify();
	}
	
	void goleste() {
		elems_cos.clear();
		notify();
	}

	void golire() {
		elems_cos.clear();
	}

	void add_prod(Produs& p) {
		elems_cos.push_back(p);
	}

	void genereaza(int dim) {
		if (dim <= elems_cos.size())
			throw RepositoryException("Ati introdus un numar final mai mic decat dimensiunea actuala a cosului!\n");
		if (!repo.get_dim_repo())
			throw RepositoryException("Nu exista elemente cu care sa umplem cosul!\n");
		auto toate = repo.get_all();
		while (elems_cos.size() < dim) {
			int index_random = repo.get_random(0, (int)toate.size() - 1);
			elems_cos.push_back(toate[index_random]);
		}
		notify();

		
	}

	vector<Produs>& get_all() {
		return elems_cos;
	}

	void sincronizare_fisier_cos_repo_cos_si_tabel();

	~Coss();
};

