package edu.carroll.ranks_list.form;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        Pattern special = Pattern.compile ("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

        return (password.length() >= 8) && (password.length() <= 16)
                && !password.equals(password.toLowerCase()) && special.matcher(password).find()
                && !password.contains(" ");
    }
}
