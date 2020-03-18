#include "GUI_obj.h"
#include <fstream>
#include <qlineedit.h>
#include <qglobal.h>

void GUI_obj::initializare_campuri() {

	QHBoxLayout *LayoutPrincipal = new QHBoxLayout;
	this->setLayout(LayoutPrincipal);

	//CREEM UN WIDGET PENTRU PARTEA STANGA
	QVBoxLayout* LayoutStanga = new QVBoxLayout;
	QWidget* butoane_si_chenar_entitati = new QWidget;
	butoane_si_chenar_entitati->setLayout(LayoutStanga);

	//creem chenarul cu toate produsele existente
	LayoutStanga->addWidget(lista);

	// adaugam la layoutul stanga un widget cu toate butoanele
	QWidget* butoane = new QWidget;
	QHBoxLayout* layout_butoane = new QHBoxLayout;
	butoane->setLayout(layout_butoane);
	layout_butoane->addWidget(Adauga);
	layout_butoane->addWidget(Sterge);
	layout_butoane->addWidget(Modifica);
	layout_butoane->addWidget(Cauta);

	//adaugam widgetul cu butoate la widgetul stanga
	LayoutStanga->addWidget(butoane);

	//adaugam widgetl pentru stanga la layoutul mare
	LayoutPrincipal->addWidget(butoane_si_chenar_entitati);

	//creem widgetul pentru dreapta layoutului mare
	QVBoxLayout* layout_dreapta = new QVBoxLayout;
	QWidget* widget_dreapta = new QWidget;
	widget_dreapta->setLayout(layout_dreapta);

	//populam layout dreapta

	//facem hlayout cu butonul de cos
	QHBoxLayout* lay_cos = new QHBoxLayout;
	QWidget* wid_cos = new QWidget;
	wid_cos->setLayout(lay_cos);

	//form layout pentru casuta cu suma din cos

	suma_cos->setReadOnly(true);

	QFormLayout* casuta_suma_cos = new QFormLayout;
	QWidget* wid_casute = new QWidget;
	wid_casute->setLayout(casuta_suma_cos);

	casuta_suma_cos->addRow(new QLabel("Suma din cos:"), suma_cos);

	lay_cos->addStretch();
	lay_cos->addWidget(wid_casute);
	lay_cos->addWidget(Cos);
	Cos->setFixedSize(QSize(120, 70));

	layout_dreapta->addWidget(wid_cos);

	//widget cu casutele de text
	QFormLayout* lay_casute_text = new QFormLayout;
	QWidget* wid_casute_text = new QWidget;
	wid_casute_text->setLayout(lay_casute_text);

	lay_casute_text->addRow(new QLabel("Nume: "), nume);
	lay_casute_text->addRow(new QLabel("Tip: "), tip);
	lay_casute_text->addRow(new QLabel("Pret: "), pret);
	lay_casute_text->addRow(new QLabel("Producator: "), producator);
	lay_casute_text->addRow(new QLabel("Produsul la care se face Update:"));
	lay_casute_text->addRow(new QLabel("Nume: "), nume_de_updatat);
	lay_casute_text->addRow(new QLabel("Producator: "), producator_de_updatat);

	layout_dreapta->addWidget(wid_casute_text);

	//adaugam butoanele de filtrari sortari
	QFormLayout* lay_butoane_sortari_filtrari = new QFormLayout;
	QWidget* wid_but_sort_filt = new QWidget;
	wid_but_sort_filt->setLayout(lay_butoane_sortari_filtrari);
	lay_butoane_sortari_filtrari->addRow(new QLabel("Filtrare pentru:"));

	//facem hlayout cu cele trei butoane
	QHBoxLayout* lay_butonae_filtrari = new QHBoxLayout;
	QWidget* wid_but_filtrari = new QWidget;
	wid_but_filtrari->setLayout(lay_butonae_filtrari);
	

	lay_butonae_filtrari->addWidget(buton__filtrare_dupa_nume);
	lay_butonae_filtrari->addWidget(buton__filtrare_dupa_pret);
	lay_butonae_filtrari->addWidget(buton__filtrare_dupa_producator);

	lay_butoane_sortari_filtrari->addRow(wid_but_filtrari);

	lay_butoane_sortari_filtrari->addRow(new QLabel("Sortare dupa:"));
	//facem hlayout cu cele trei butoane de sortare
	QHBoxLayout* lay_butonae_sortari = new QHBoxLayout;
	QWidget* wid_but_sortari = new QWidget;
	wid_but_sortari->setLayout(lay_butonae_sortari);

	lay_butonae_sortari->addWidget(buton__sortare_dupa_nume);
	lay_butonae_sortari->addWidget(buton__sortare_dupa_pret);
	lay_butonae_sortari->addWidget(buton__sortare_dupa_nume_tip);

	lay_butoane_sortari_filtrari->addWidget(wid_but_sortari);


	//facem hlayout cu butoanele de afisare toate produele si frecvente
	QHBoxLayout* lay_butonae_afisare_toate = new QHBoxLayout;
	QWidget* wid_but_afisare = new QWidget;
	wid_but_afisare->setLayout(lay_butonae_afisare_toate);

	lay_butonae_afisare_toate->addWidget(buton__afisare_toate);
	lay_butonae_afisare_toate->addWidget(buton__frecvente);

	

	layout_dreapta->addWidget(wid_but_sort_filt);
	layout_dreapta->addWidget(wid_but_afisare);
	layout_dreapta->addStretch();
	//layout_dreapta->addWidget(wid_cos);
	layout_dreapta->addStretch();
	layout_dreapta->addWidget(undo);
	//adaugam la layoutul central widget dreapta
	LayoutPrincipal->addWidget(widget_dreapta);
}

void GUI_obj::conectare_semnale_sloturi(){
	const auto _1 = QObject::connect(Adauga, &QPushButton::clicked, this, &GUI_obj::gui_adauga);//butonul de adauga
	const auto _2 = QObject::connect(Sterge, &QPushButton::clicked, this, &GUI_obj::gui_remove);
	const auto _3 = QObject::connect(Modifica, &QPushButton::clicked, this, &GUI_obj::gui_update);
	const auto _4 = QObject::connect(undo, &QPushButton::clicked, this, &GUI_obj::gui_undo);
	const auto _5 = QObject::connect(Cauta, &QPushButton::clicked, this, &GUI_obj::gui_cauta);
	const auto _6 = QObject::connect(lista, &QListWidget::itemSelectionChanged, [&]() {
		if (lista->selectedItems().isEmpty()) {
			nume->setText("");
			producator->setText("");
			pret->setText("");
			tip->setText("");
			return;
		}
		QListWidgetItem* linie_selectata = lista->selectedItems().at(0);
		QString NUME = linie_selectata->text();
		nume->setText(NUME);

		//facem rost de producatorul care este tinut in spate
		auto PROD = linie_selectata->data(Qt::UserRole);
		try {
			auto produs = serv.cauta_prod(NUME.toStdString(), PROD.toString().toStdString());
			producator->setText(PROD.toString());
			tip->setText(QString::fromStdString(produs.get_tip()));
			pret->setText(QString::number(produs.get_pret()));
		}
		catch (RepositoryException&) {		}
	});
	
	const auto _7 = QObject::connect(buton__filtrare_dupa_nume, &QPushButton::clicked, [&]() {
		sincronizare_lista_repository(serv.produse_cu_numele_dat(nume->text().toStdString()));
	});

	const auto _8 = QObject::connect(buton__filtrare_dupa_pret, &QPushButton::clicked, [&]() {
		sincronizare_lista_repository(serv.produse_mai_mici_decat_un_pret(pret->text().toFloat()));
	});

	const auto _9 = QObject::connect(buton__filtrare_dupa_producator, &QPushButton::clicked, [&]() {
		sincronizare_lista_repository(serv.produse_cu_producator_dat(producator->text().toStdString()));
	});

	const auto _10 = QObject::connect(buton__sortare_dupa_nume, &QPushButton::clicked, [&]() {
		sincronizare_lista_repository(serv.sorteaza_dupa_nume());
	});

	const auto _11 = QObject::connect(buton__sortare_dupa_pret, &QPushButton::clicked, [&]() {
		sincronizare_lista_repository(serv.sorteaza_dupa_pret());
	});

	const auto _12 = QObject::connect(buton__sortare_dupa_nume_tip, &QPushButton::clicked, [&]() {
		sincronizare_lista_repository(serv.sorteaza_dupa_nume_si_tip());
	});

	const auto _13 = QObject::connect(buton__afisare_toate, &QPushButton::clicked, [&]() {
		sincronizare_lista_repository(serv.getAll());
	});

	const auto _14 = QObject::connect(buton__frecvente, &QPushButton::clicked, [&]() {
		pune_frecvente_in_lista(serv.produse_frecventa());
	});

	const auto _15 = QObject::connect(Cos, &QPushButton::clicked, this, &GUI_obj::add_cos);
}

void GUI_obj::pune_frecvente_in_lista(const map<string, int>& dictionar) {
	lista->clear();
	for (const auto& tip_str : dictionar) {
		QListWidgetItem* item = new QListWidgetItem(QString::fromStdString(tip_str.first + " apare de " + to_string(tip_str.second) + " ori"), lista);
		item->setIcon(QApplication::style()->standardIcon(QStyle::SP_DialogApplyButton));
	}
}

void GUI_obj::sincronizare_lista_repository(const vector<Produs>& repo) {
	lista->clear();
	suma_cos->setText(QString::number(serv.get_suma_curenta_cos()));
	for (const auto& produs : repo) {
		QListWidgetItem* item = new QListWidgetItem(QString::fromStdString(produs.get_nume()), lista);
		item->setData(Qt::UserRole, QString::fromStdString(produs.get_producator()));
		item->setIcon(QApplication::style()->standardIcon(QStyle::SP_DesktopIcon));
	}
}

void GUI_obj::gui_adauga()
{
	try {
		serv.adauga(nume->text().toStdString(), tip->text().toStdString(), pret->text().toFloat(), producator->text().toStdString());
		this->sincronizare_lista_repository(serv.getAll());
		nume->setText("");
		producator->setText("");
		pret->setText("");
		tip->setText("");
	}
	catch (RepositoryException& e) {
		QMessageBox::warning(nullptr, "Eroare Repository", QString::fromStdString(e.get_eroare()));
	}
	catch (ValidationException& v) {
		QMessageBox::warning(nullptr, "Eroare Validare", QString::fromStdString(v.get_eroare()));
	}
}

void GUI_obj::gui_remove() {
	try {
		serv.sterge(nume->text().toStdString(), producator->text().toStdString());
		this->sincronizare_lista_repository(serv.getAll());
		nume->setText("");
		producator->setText("");
		pret->setText("");
		tip->setText("");
	}
	catch (RepositoryException& e) {
		QMessageBox::warning(nullptr, "Eroare Repository", QString::fromStdString(e.get_eroare()));
	}
}

void GUI_obj::gui_update() {
	try {
		serv.modifica(nume_de_updatat->text().toStdString(), producator_de_updatat->text().toStdString(), nume->text().toStdString(), tip->text().toStdString(), pret->text().toFloat(), producator->text().toStdString());
		this->sincronizare_lista_repository(serv.getAll());
		nume->setText("");
		producator->setText("");
		pret->setText("");
		tip->setText("");
		nume_de_updatat->setText("");
		producator_de_updatat->setText("");
	}
	catch (RepositoryException& e) {
		QMessageBox::warning(nullptr, "Eroare Repository", QString::fromStdString(e.get_eroare()));
	}
	catch (ValidationException& v) {
		QMessageBox::warning(nullptr, "Eroare Validare", QString::fromStdString(v.get_eroare()));
	}
}

void GUI_obj::gui_undo() {
	try {
		serv.undo();
		this->sincronizare_lista_repository(serv.getAll());
	}
	catch (RepositoryException& ) {
		QMessageBox::warning(nullptr, "Undo", "Nu se mai poate face undo!");
	}
}

void GUI_obj::gui_cauta() {
	try {
		const auto _16 = serv.cauta_prod(nume->text().toStdString(), producator->text().toStdString());
		nume->setText("");
		producator->setText("");
		pret->setText("");
		tip->setText("");
		QMessageBox::warning(nullptr, "Cautare", "Produsul cautat exista!");
	}
	catch (RepositoryException& e) {
		QMessageBox::warning(nullptr, "Eroare Repository", QString::fromStdString(e.get_eroare()));
	}
}



void GUI_obj::fereastra_cos() {
	
	QHBoxLayout* lay_principal_cos = new QHBoxLayout;
	QWidget* wid_cos = new QWidget;
	wid_cos->setLayout(lay_principal_cos);

	QWidget* wid_stanga = new QWidget;
	QVBoxLayout* lay_stanga = new QVBoxLayout;
	wid_stanga->setLayout(lay_stanga);

	lay_stanga->addWidget(new QLabel("Produsele din Cos:"));
	lay_stanga->addWidget(lista_cos);
	
	//layout partea drepata
	QWidget* wid_dreapta = new QWidget;
	QVBoxLayout* lay_dreapta = new QVBoxLayout;
	wid_dreapta->setLayout(lay_dreapta);

	//lay_dreapta->addWidget(adauga_in_cos);
	lay_dreapta->addWidget(goleste_cos);
	lay_dreapta->addWidget(genereaza_cos);
	lay_dreapta->addWidget(new QLabel("Dimensiune finala cos"));
	lay_dreapta->addWidget(dim_generare_cos);
	lay_dreapta->addWidget(iesire_din_cos);

	
	lay_principal_cos->addWidget(wid_stanga);
	lay_principal_cos->addWidget(wid_dreapta);

	//afisam produsele existente
	lista_cos->clear();
	sincronizare_repo_cos_lista_cos();
	
	//conectam butoanele la sloturi
	//const auto _16 = QObject::connect(adauga_in_cos, &QPushButton::clicked, this, &GUI_obj::add_cos);
	const auto _17 = QObject::connect(goleste_cos, &QPushButton::clicked, this, &GUI_obj::gol_cos);
	const auto _18 = QObject::connect(genereaza_cos, &QPushButton::clicked, this, &GUI_obj::genere_cos);
}

void GUI_obj::add_cos() {
	try {
		serv.adauga_in_cos(Produs(nume->text().toStdString(), tip->text().toStdString(), pret->text().toFloat(), producator->text().toStdString()));
		//coss.sincronizare_fisier_cos_repo_cos_si_tabel();
	}
	catch (ValidationException&) {
		QMessageBox::warning(nullptr, "Eroare Validare", "Nu ati selectat nimic!");
	}
	//coss.sincronizare_fisier_cos_repo_cos_si_tabel();
}



void GUI_obj::gol_cos() {
	serv.goleste_cos();
	sincronizare_repo_cos_lista_cos();
}

void GUI_obj::genere_cos() {

	try {
		serv.umple_cos(dim_generare_cos->text().toInt());
	}
	catch (RepositoryException& ex) {
		if(dim_generare_cos->text().isEmpty())
			QMessageBox::warning(nullptr, "Eroare Validare", "Nu ati introdus dimensiunea finala a cosului!");
		else
			QMessageBox::warning(nullptr, "Eroare Repository", QString::fromStdString(ex.get_eroare()));
	}
	sincronizare_repo_cos_lista_cos();
	dim_generare_cos->setText("");
}




void GUI_obj::sincronizare_repo_cos_lista_cos() {
	lista_cos->clear();
	//actualizare_fis_cos_memo_cos_si_table();
	ifstream in("cos.cvs");
	string line;
	while (!in.eof()) {
		getline(in, line);
		if (line != "") {
			std::replace(line.begin(), line.end(), ',', ' ');
			QListWidgetItem* linie_lista = new QListWidgetItem(QString::fromStdString(line), lista_cos);
			linie_lista->setIcon(QApplication::style()->standardIcon(QStyle::SP_ArrowRight));
		}
	}
	in.close();
	suma_cos->setText(QString::number(serv.get_suma_curenta_cos()));
	
}
