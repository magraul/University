package me.services;

import javafx.scene.control.PasswordField;
import me.entities.*;
import me.events.EvenimentSchimbare;
import me.events.TipEveniment;
import me.observer.Observable;
import me.observer.Observer;
import me.repositories.AngajatiDBRepository;
import me.repositories.CazuriDBRepository;
import me.repositories.DonatiiDBRepository;
import me.repositories.DonatoriDBRepository;
import me.validators.ValidatorCazuri;
import me.validators.ValidatorDonatii;
import me.validators.ValidatorDonatori;
import org.mindrot.jbcrypt.BCrypt;
import sun.security.validator.ValidatorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service implements Observable<EvenimentSchimbare> {
    private AngajatiDBRepository angajatiDBRepository;
    private CazuriDBRepository cazuriDBRepository;
    private DonatiiDBRepository donatiiDBRepository;
    private DonatoriDBRepository donatoriDBRepository;
    private ValidatorCazuri validatorCazuri;
    private ValidatorDonatii validatorDonatii;
    private ValidatorDonatori validatorDonatori;

    public Service(AngajatiDBRepository angajatiDBRepository, CazuriDBRepository cazuriDBRepository, DonatiiDBRepository donatiiDBRepository, DonatoriDBRepository donatoriDBRepository, ValidatorCazuri validatorCazuri, ValidatorDonatii validatorDonatii, ValidatorDonatori validatorDonatori) {
        this.angajatiDBRepository = angajatiDBRepository;
        this.cazuriDBRepository = cazuriDBRepository;
        this.donatiiDBRepository = donatiiDBRepository;
        this.donatoriDBRepository = donatoriDBRepository;
        this.validatorCazuri = validatorCazuri;
        this.validatorDonatii = validatorDonatii;
        this.validatorDonatori = validatorDonatori;
    }

    private List<Observer<EvenimentSchimbare>> observers = new ArrayList<>();


    public void donatie(Integer cazId, String numeDonator, String adresa, String nrTel, Float suma) throws ValidatorException {
        Integer idDonator;
        if(donatoriDBRepository.findDonatoriDupaNume(numeDonator).size() != 0){
            idDonator = donatoriDBRepository.findDonatoriDupaNume(numeDonator).get(0).getId();
            Donator d = new Donator(numeDonator, nrTel, adresa);
            d.setId(idDonator);
            donatoriDBRepository.update(d);
        } else {
            donatoriDBRepository.save(new Donator(numeDonator, nrTel, adresa));
            idDonator = donatoriDBRepository.findDonatoriDupaNume(numeDonator).get(0).getId();
        }


        Donatie d = new Donatie(suma, idDonator,cazId);
        validatorDonatii.valideaza(d);
        donatiiDBRepository.save(d);
        notifyObservers(new EvenimentSchimbare(TipEveniment.DONATIE));
    }

    public List<Donator> cauta(String numeDonator) {
        return donatoriDBRepository.findDonatoriDupaNume(numeDonator);
    }



    @Override
    public void addObserver(Observer<EvenimentSchimbare> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EvenimentSchimbare> e) {

    }

    @Override
    public void notifyObservers(EvenimentSchimbare t) {
        observers.stream().forEach(x -> x.update(t));
    }

    public Map<String, String> getDataAutentificareAngajati() {
        Map<String, String> for_return = new HashMap<>();
        List<Angajat> angajati = (List<Angajat>) angajatiDBRepository.findAll();
        for (Angajat a : angajati){
            for_return.put(a.getUsername(), a.getPassword());
        }

        return for_return;
    }

    public String encode(String text) {
        return BCrypt.hashpw(text,BCrypt.gensalt());
    }

    public void updateAngajat(AngajatDTO i, String parola) {
        Angajat a = angajatiDBRepository.findAngajatiDupaNume(i.getNume()).get(0);
        a.setPassword(parola);
        a.setUsername(i.getUsername());
        angajatiDBRepository.update(a);
    }

    public List<AngajatDTO> getAllAngajati() {
        List<AngajatDTO> rez = new ArrayList<>();
        List<Angajat> angajatList = (List<Angajat>) angajatiDBRepository.findAll();
        for(Angajat a : angajatList) {
            AngajatDTO angajatDTO = new AngajatDTO(a.getName(), a.getUsername());
            rez.add(angajatDTO);
        }
        return rez;
    }

    public List<String> getListaNumeAngajati() {
        List<String> rez = new ArrayList<>();
        for (Angajat a : angajatiDBRepository.findAll()){
            rez.add(a.getName());
        }
        return rez;
    }

    public Integer getCazId(CazCaritabilDTO caz) {
        return cazuriDBRepository.findCazuriDupaDescriere(caz.getDescriere()).get(0).getId();
    }

    private Float getSumaAdunata(CazCaritabil c) {
        Float rez = 0F;
        for (Donatie d: donatiiDBRepository.findAll()){
            if (d.getCazCaritabil().toString().equals(c.getId().toString())) {
                rez += d.getSumaDonata();
            }
        }
        return rez;
    }

    public List<CazCaritabilDTO> getAllCazuriDTO() {
        List<CazCaritabilDTO> rez = new ArrayList<>();
        List<CazCaritabil> cazCaritabilDTOS = (List<CazCaritabil>) cazuriDBRepository.findAll();
        for (CazCaritabil c : cazCaritabilDTOS) {
            CazCaritabilDTO cazCaritabilDTO = new CazCaritabilDTO(c.getDescription(), getSumaAdunata(c));
            rez.add(cazCaritabilDTO);
        }
        return rez;
    }

    public List<String> getListaNumeDonatori() {
        List<String> rez = new ArrayList<>();

        for (Donator d : donatoriDBRepository.findAll()) {
            rez.add(d.getName());
        }
        return rez;
    }

    public List<String> getRanduriDonatori() {
        List<String> rez = new ArrayList<>();
        for (Donator d : donatoriDBRepository.findAll()) {
            rez.add(d.getName() + "  " + d.getAddress() + "  " + d.getPhoneNumber());
        }

        return rez;
    }
}
