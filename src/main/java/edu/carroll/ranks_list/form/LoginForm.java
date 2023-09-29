package edu.carroll.ranks_list.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

public class LoginForm {
    @NotNull
    @Size(min = 6, message = "Username must be at least 6 characters long")
    private String username;

    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
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

    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute String username, BindingResult result) {
        System.out.println("User '" + username + "' attempted login");
        if (result.hasErrors()) {
            return "login";
        }
        return "redirect:/loginSuccess";
    }
}