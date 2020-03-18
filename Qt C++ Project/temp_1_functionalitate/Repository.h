#pragma once
#include"Produs.h"
#include <string>
#include <vector>
#include "Lista.h"
#include <memory>
#include <sstream>
#include <random>


using std::string;
using std::ostream;
using std::vector;
using namespace std;
class AbstractRepository {
public:
	virtual void adauga_repo(const Produs& p) = 0;
	virtual void remove(Produs& p) = 0;
	virtual void update(Produs p1, Produs& p) = 0;
	virtual int size_of_repo() = 0;
	virtual const vector<Produs>& get_all() const = 0;
};


class Repository:public AbstractRepository
{
	vector<Produs> all;
public:
	Repository() = default;

	void adauga_in_cos(const Produs&);
	void load_from_file();
	void write_to_file();
	void adauga_repo(const Produs&) override;
	void replace_repo(vector<Produs> inlocuitor) {
		all.clear();
		all = inlocuitor;
	}
	bool exist(const Produs&);
	Produs cauta(const string &, const string&) const;
	const vector<Produs>& get_all() const override;
	void remove(Produs&) override;
	void update(Produs, Produs&) override;
	int get_random(int min, int max) const {
		mt19937 mt{ random_device{}() };
		uniform_int_distribution<> dist(min, max);
		int rndNr = dist(mt);// numar aleator intre [0,size-1]
		return rndNr;
	}
	void goleste_repo();
	void goleste_cos();
	int dim_cos();
	int size_of_repo() override {
		return (int)all.size();
	}

	int get_dim_repo() const {
		return all.size();
	}

	int pozitie_element(Produs p) {
		int poz = 0;
		for (const auto&el : all) {
			if(p == el)
				return poz;
			poz++;
		}
		if (poz == all.size())
			return -1;
		return 0;
	}

	int poz_elem_dat(Produs p) { return pozitie_element(p); }
};


class RepositoryException;

class RepositoryFile :public Repository {

public:
	RepositoryFile() {
		Repository::load_from_file();
	}
	void adauga_repo(const Produs& p) override {
		Repository::adauga_repo(p);
		Repository::write_to_file();
	}
	void remove(Produs& p) override {
		Repository::remove(p);
		Repository::write_to_file();
	}
	void update(Produs de_inlocuit, Produs& inlocuitor) override {
		Repository::update(de_inlocuit, inlocuitor);
		Repository::write_to_file();
	}

};





class RepositoryException {
	string eroare;
public:
	RepositoryException(string m) :eroare{ m } {};
	string get_eroare() {
		return eroare;
	}
};



void test_repo();

vector<string> split(const string &s, char delim);
