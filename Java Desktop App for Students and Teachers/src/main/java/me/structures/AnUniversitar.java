package me.structures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class AnUniversitar {
    Integer id;
    private Year anUniversitar;
    private LocalDate dataInceputSem1; //30 sept de ex
    private LocalDate dataInceputSem2;
    private List<Integer> saptamaniVacantaSem1 = new ArrayList<>(); //contorizat de la prima saptamana de facultate din semestru pana la finalul semestrului
    private List<Integer> saptamaniVacantaSem2 = new ArrayList<>(); //contorizat de la prima saptamana de facultate din semestru pana la finalul semestrului



    public AnUniversitar(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Path path = Paths.get(fileName);
        try {
            List<String> linii = Files.readAllLines(path);  //o sa aiba 4 linii
            anUniversitar = Year.parse(linii.get(0));
            String[] celeDouaData = linii.get(1).split(" ");
            dataInceputSem1 = LocalDate.parse(celeDouaData[0], formatter);
            dataInceputSem2 = LocalDate.parse(celeDouaData[1], formatter);
            String[] saptamaniDeVacanta = linii.get(2).split(" ");
            for(String nr: saptamaniDeVacanta) {
                Integer a = Integer.parseInt(nr);
                this.saptamaniVacantaSem1.add(a);
            }

            saptamaniDeVacanta = linii.get(3).split(" ");
            for(String nr: saptamaniDeVacanta) {
                Integer a = Integer.parseInt(nr);
                this.saptamaniVacantaSem2.add(a);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean suntemInSem1() {
        LocalDate datafinalSem1 = LocalDate.parse("2020-01-19");

        if(LocalDate.now().compareTo(datafinalSem1) < 0)
            return true;
        return false;
    }

    public Integer getCurentWeek() {

        Integer numarSaptamanaDeLaInceputulSemestrului = getNrSaptamanaDinSemestru();
        LocalDate datafinalSem1 = LocalDate.parse("2020-01-19");


        Integer rezultat;
        if(LocalDate.now().compareTo(datafinalSem1) < 0) {
            //suntem in sem 1
            List<Integer> vacanteTrecute = this.saptamaniVacantaSem1
                    .stream()
                    .filter(s->s<numarSaptamanaDeLaInceputulSemestrului)
                    .collect(Collectors.toList());
            rezultat = numarSaptamanaDeLaInceputulSemestrului - vacanteTrecute.size();

        } else {
            // suntem in sem 2
            List<Integer> vacanteTrecute = this.saptamaniVacantaSem2
                    .stream()
                    .filter(s->s<numarSaptamanaDeLaInceputulSemestrului)
                    .collect(Collectors.toList());
            rezultat = numarSaptamanaDeLaInceputulSemestrului - vacanteTrecute.size();
        }
        return rezultat;
    }



    public Integer getNrSaptamanaDeStudiuDinSemestruAlDatei(String dataPredare) {
        //Integer saptamanaDinAnCalendaristicAlDatei = Integer.parseInt(new SimpleDateFormat("w").format(Date.from(Instant.parse(dataPredare))));

        LocalDate date = LocalDate.parse(dataPredare);
        TemporalField nrSaptamanaAnn = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        Integer saptamanaDinAnCalendaristicAlDatei = date.get(nrSaptamanaAnn);



        // saptamana in care este data de inceput a sem 1
        TemporalField nrSaptamanaAn = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int saptamanaInceputSem1 = dataInceputSem1.get(nrSaptamanaAn);

        // saptamana in care este data de inceput a sem 2
        TemporalField nrSaptamanaincepSem2 = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int saptamanaInceputSem2 = dataInceputSem2.get(nrSaptamanaincepSem2);

        Integer nrSaptDeLaInceput, rezultat;

        LocalDate datafinalSem1 = LocalDate.parse("2020-01-19");
        if (LocalDate.now().compareTo(datafinalSem1) < 0) {
            //suntem in sem 1
            // daca suntem dupa anul nou
            if(date.getYear() > Integer.parseInt(this.anUniversitar.toString()))
                nrSaptDeLaInceput = 52 + saptamanaDinAnCalendaristicAlDatei - saptamanaInceputSem1;
            else
                nrSaptDeLaInceput = saptamanaDinAnCalendaristicAlDatei - saptamanaInceputSem1;

            List<Integer> vacanteTrecute = this.saptamaniVacantaSem1
                    .stream()
                    .filter(s->s<nrSaptDeLaInceput)
                    .collect(Collectors.toList());
            rezultat = nrSaptDeLaInceput - vacanteTrecute.size();


        } else {
            //suntem in sem 2
            nrSaptDeLaInceput = saptamanaDinAnCalendaristicAlDatei - saptamanaInceputSem2;

            List<Integer> vacanteTrecute = this.saptamaniVacantaSem2
                    .stream()
                    .filter(s->s<nrSaptDeLaInceput)
                    .collect(Collectors.toList());
            rezultat = nrSaptDeLaInceput - vacanteTrecute.size();
        }

        return rezultat + 1;

    }



    public Integer getNrSaptamanaDinSemestru() {
        LocalDate datafinalSem1 = LocalDate.parse("2020-01-19");

        //saptamana curenta din anul calendaristic
        Integer saptamanaCurentaAnCalendaristic = Integer.parseInt(new SimpleDateFormat("w").format(new java.util.Date()));

        // saptamana in care este data de inceput a sem 1
        TemporalField nrSaptamanaAn = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int saptamanaInceputSem1 = dataInceputSem1.get(nrSaptamanaAn);

        // saptamana in care este data de inceput a sem 2
        TemporalField nrSaptamanaincepSem2 = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int saptamanaInceputSem2 = dataInceputSem2.get(nrSaptamanaincepSem2);


        if (LocalDate.now().compareTo(datafinalSem1) < 0) {
            //suntem in sem 1
            //daca suntem dupa anul nou
            if(LocalDate.now().getYear() > Integer.parseInt(this.anUniversitar.toString()))
                return 52 + saptamanaCurentaAnCalendaristic - saptamanaInceputSem1 + 1;
            else
                return saptamanaCurentaAnCalendaristic - saptamanaInceputSem1 + 1;
        } else {
            //suntem in sem 2
            return saptamanaCurentaAnCalendaristic - saptamanaInceputSem2 + 1;
        }
    }

    public List<Integer> getSaptamaniVacantaSem1() {
        return saptamaniVacantaSem1;
    }

    public List<Integer> getSaptamaniVacantaSem2() {
        return saptamaniVacantaSem2;
    }

    public LocalDate getDataInceputSem1() {
        return dataInceputSem1;
    }

    public LocalDate getDataInceputSem2() {
        return dataInceputSem2;
    }

    public Year getAnUniversitar() {
        return anUniversitar;
    }
}
