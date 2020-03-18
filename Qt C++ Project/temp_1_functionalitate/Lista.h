#pragma once
#include<memory>
#include<cstdio>
using namespace std;
template <typename Tip> class Iterator;


template <typename Tip>class Nod {
private:
	Tip produs;
	shared_ptr<Nod<Tip>> urm;
public:
	Nod(Tip& produs, shared_ptr<Nod<Tip>>urm) :produs{ produs }, urm{ urm }{}
	Tip& get_produs() { return this->produs; }
	void pune_info(Tip& produs_nou) { produs = produs_nou; }
	shared_ptr<Nod<Tip>> get_urmator() const noexcept { return this->urm; }
	void set_urmator(shared_ptr<Nod<Tip>> urmator_nou) noexcept { urm = urmator_nou; }
};

template<typename Tip>
class Lista
{
private:
	friend class Nod<Tip>;
	shared_ptr<Nod<Tip>> head;
	int dimensiune;
public:

	Lista() noexcept;
	Lista(const Lista& ot) noexcept;// constructor de copiere
	Lista& operator=(const Lista& l) noexcept;
	int size() noexcept;
	bool vida() noexcept;
	void pune_la_final(Tip& produs); //adauga in lista la capat un nou nod ce contine produsul dat ca parametru
	
	void sterge_de_pe_pozitie(int pozitie_de_sters);
	
	void set_produs(int pos, Tip& produs_nou);
	
	void pune_produs_pe_pozitie(int pozitie, Tip& produs);
	~Lista() {};
	Tip& produs_de_pe_pozitie(int pos);
	int pozitie_element(Tip);
	auto get_head(){
		return head;
	}
};


template <typename Tip>
Lista<Tip>::Lista()noexcept {
	head = NULL;
	dimensiune = 0;
}

/*
Copy constructor
Input: l - Lista
*/
template <typename Tip>
Lista<Tip>::Lista(const Lista<Tip>& l) noexcept
{
	head = l.head;
	dimensiune = l.dimensiune;
}

/*
Operator =
Input: l - Lista
*/
template <typename Tip>
Lista<Tip>& Lista<Tip>::operator= (const Lista<Tip>& l) noexcept
{
	this->head = l.head;
	this->dimensiune = l.dimensiune;
	return *this;
}

/*
Get the size of list
Input: -
Output: size - int
*/
template <typename Tip>
int Lista<Tip>::size() noexcept
{
	return dimensiune;
}

/*
Return true if list is empty, else return false
*/
template <typename Tip>
bool Lista<Tip>::vida() noexcept
{
	return dimensiune == 0;
}

/*
pune un element nou la finalul listei
pre: produs validat
*/
template<typename Tip>
void Lista<Tip>::pune_la_final(Tip& produs) {
	++dimensiune;
	shared_ptr<Nod<Tip>> precedent = NULL;
	
	auto curent = head;
	if (head) {
		while ((*curent).get_urmator()) {
			precedent = curent;
			curent = (*curent).get_urmator();
		}
	}
	if (precedent)
		(*precedent).set_urmator(make_shared<Nod<Tip>>(produs, curent));
	else {
		head = make_shared<Nod<Tip>>(produs, curent); // lista era goala
	}
}




/*
pune un produs intr-un nod din lista existent
*/
template <typename Tip>
void Lista<Tip>::set_produs(int pos, Tip& produs_nou)
{
	auto curent = head;
	int j = 0;
	while (j < pos)
	{
		curent = curent->get_urmator();
		j++;
	}
	curent->pune_info(produs_nou);
}
/*
inlocuieste informatia din nodul de pe pozitia pos. cu "produs_de_inserat"
*/
template <typename Tip>
void Lista<Tip>::pune_produs_pe_pozitie(int pos, Tip& produs_de_inserat)
{
	shared_ptr<Nod<Tip>>  prec = NULL;
	//auto prec = NULL;
	auto curent = head;
	int j = 0;
	while (j < pos)
	{
		prec = curent;
		curent = (*curent).get_urmator();
		j++;
	}

	if (prec)
		curent->pune_info(produs_de_inserat);
	else
		head->pune_info(produs_de_inserat);
}
/*
	sterge un nod de pe pozitia pos din lista
	pre: pos intreg pozitiv
*/
template <typename Tip>
void Lista<Tip>::sterge_de_pe_pozitie(int pos) {
	--dimensiune;
	int j = 0;
	shared_ptr<Nod<Tip>>  prev = NULL;
	auto curent = head;
	while (j < pos)
	{
		prev = curent;
		curent = (*curent).get_urmator();
		j++;
	}
	if (prev != NULL)
		(*prev).set_urmator((*curent).get_urmator());
	else
		head = (*curent).get_urmator();

}
/*
functie care returneaza un element de tipul Tip de pe pozitia pos in lista
*/
template<typename Tip>
Tip& Lista<Tip>::produs_de_pe_pozitie(int pos) {
	int j = 0;
	auto curent = head;
	while (j < pos) {
		curent = curent->get_urmator();
		j++;
	}
	return curent->get_produs();
}


/*	
 * Returneaza pozitia din lista pe care se afla un element dat ca parametru
 *
 *
 */
template<typename Tip>
int Lista<Tip>::pozitie_element(Tip elem) {
	int j = 0;
	auto curent = head;
	while (1) {
		if (curent->get_produs().get_nume() == elem.get_nume() && curent->get_produs().get_producator() == elem.get_producator())
			return j;
		j++;
		curent = curent->get_urmator();
		if (curent == NULL)
			return -1;
	}
}
