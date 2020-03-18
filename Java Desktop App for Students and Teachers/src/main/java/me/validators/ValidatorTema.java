package me.validators;

import me.entities.Tema;
import me.exceptions.ValidationException;
import me.services.config.ApplicationContext;
import me.structures.AnUniversitar;

import java.time.LocalDate;

public class ValidatorTema implements Validator<Tema> {
    String errors = "";

    AnUniversitar anUniversitar = new AnUniversitar(ApplicationContext.getPROPERTIES().getProperty("data.structure.anUniversitar"));
    @Override
    public void validate(Tema entity) throws ValidationException {
        errors="";
        if(entity.getDescriere().equals(""))
            errors+= "Descriere vida!\n";

        if(entity.getDeadlineWeek() > 14 || entity.getDeadlineWeek() < 1)
            errors += "Deadline Invalid!\n";

        if(entity.getStartWeek() > 13 || entity.getStartWeek() < 1)
            errors += "StartWeek invalid! nu se pot da teme in ultima saptamana !\n";

        if(entity.getDeadlineWeek() < anUniversitar.getCurentWeek())
            errors += "Deadline mai mic decat saptamanan curenta!\n";

        if((anUniversitar.getAnUniversitar().toString().equals(LocalDate.now().getYear()) && anUniversitar.getSaptamaniVacantaSem1().contains(anUniversitar.getNrSaptamanaDinSemestru())) || (anUniversitar.getAnUniversitar().toString().equals(LocalDate.now().getYear()) && anUniversitar.getSaptamaniVacantaSem2().contains(anUniversitar.getNrSaptamanaDinSemestru())))
            errors += "Nu se pot da teme in vacanta!\n";

        if(errors != "")
            throw new ValidationException(errors);
    }
}
