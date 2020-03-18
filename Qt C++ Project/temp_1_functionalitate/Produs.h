#pragma once
#include<iostream>
#include <string>
#include <memory>
using std::string;
using std::cout;

class Produs
{
	string nume;
	string tip;
	float pret;
	string producator;
public:
	Produs() {
		pret = 0;
	}
	Produs(const string n, const string t, const float p, const string pr) :nume{ n }, tip{ t }, pret{ p }, producator{ pr } {};
	Produs(const Produs& ot) :nume{ ot.nume }, tip{ ot.tip }, pret{ ot.pret }, producator{ ot.producator }{
		cout << "copie la produs!\n";
	}
	string get_nume() const {
		return nume;
	}
	string get_tip() const {
		return tip;
	}
	float get_pret() const {
		return pret;
	}
	string get_producator() const {
		return producator;
	}

	void set_nume(string nume_nou) {
		nume = nume_nou;
	}

	virtual bool operator==(const Produs& other) const {
		return other.nume == nume && other.producator == producator;
	}

	virtual void operator=(const Produs& other) {
		nume = other.get_nume();
		tip = other.get_tip();
		pret = other.get_pret();
		producator = other.get_producator();
	}

	inline bool operator()(const Produs& m) const { return &m == this; }



};

bool compara_nume(const Produs&, const Produs&);

bool compara_pret(const Produs&, const Produs&);
