package edu.carroll.ranks_list.form;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Constraint for user account passwords. Requires all fields marked with the password annotation to be 8-16 characters
 * long, contain at least one capital letter, a number, and a special character.
 *
 * @author Ryan Johnson, Hank Rugg
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        Pattern special = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

        return (password.length() >= 8) && (password.length() <= 16)
                && !password.equals(password.toLowerCase()) && special.matcher(password).find()
                && !password.contains(" ");
    }
}
