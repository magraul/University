#pragma once
#include "Produs.h"
//#include "Repository.h"

class ActiuneUndo {
public:
	virtual void doUndo() = 0;
	ActiuneUndo()noexcept {}
	virtual ~ActiuneUndo() = default;
};


class UndoAdauga:public ActiuneUndo {
	Produs p;
	Repository& rep;
public:
	UndoAdauga(Repository& rep, const Produs& p) noexcept :rep{ rep }, p{ p }{}
	void doUndo() override {
		rep.remove(p);
	}
};

class UndoSterge:public ActiuneUndo {
	Produs p;
	Repository& rep;
public:
	UndoSterge(Repository& rep, const Produs& p) noexcept :rep{ rep }, p{ p }{}
	void doUndo() override {
		rep.adauga_repo(p);
	}
};


class UndoModifica:public ActiuneUndo {
	Produs prod_de_inlocuit, prod_inlocuitor;
	Repository& rep;
public:
	UndoModifica(Repository& rep, const Produs& prod_de_inlocuit, const Produs& prod_inlocuitor) noexcept : rep{ rep }, prod_de_inlocuit{ prod_de_inlocuit }, prod_inlocuitor{ prod_inlocuitor }{}
	void doUndo() override {
		rep.remove(prod_inlocuitor);
		rep.adauga_repo(prod_de_inlocuit);
	}
};