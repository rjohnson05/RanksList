package edu.carroll.ranks_list.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegistrationForm {

    @NotNull
    @Size(min = 6, max = 32)
    private String username;

    @NotNull
    @Size(min = 8, max = 16)
    @Password
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
