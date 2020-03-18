package me.validators;

import me.entities.Donatie;
import org.controlsfx.validation.ValidationMessage;
import sun.security.validator.ValidatorException;

public class ValidatorDonatii {
    private String errors;
    public void valideaza(Donatie d) throws ValidatorException {
        errors = "";
        if (d.getSumaDonata() <=0F)
            errors += "suma invalida!";
        if(errors.length() > 0)
            throw new ValidatorException(errors);
    }
}
