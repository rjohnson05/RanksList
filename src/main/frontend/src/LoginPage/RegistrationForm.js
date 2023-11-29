import React, { useState } from 'react';
import {Link, useNavigate} from "react-router-dom"
import { useForm } from "react-hook-form"
import axios from "axios";
import './LoginPage.css';
import * as Yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";

export default function RegistrationForm() {
    const navigate = useNavigate();
    const [usernameAvailable, setUsernameAvailable] = useState(true);

    const validationSchema = Yup.object().shape({
        username: Yup.string()
            .required("Username is required")
            .min(6, "Username must be 6-32 characters long")
            .max(32, "Username must be 6-32 characters long"),
        password: Yup.string()
            .required("Password is required")
            .min(8, "Password must be 8-16 characters long")
            .max(16, "Password must be 8-16 characters long")
            .matches(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/,
                "Must contain at least 1 uppercase letter, 1 number, and 1 special character")
    });

    const {register,
        handleSubmit,
        formState: { errors }
    } = useForm({mode: "onSumbit", reValidateMode: "onBlur", resolver: yupResolver(validationSchema)});

    const onSubmit = async (formData) => {
        // Push the form data to the database, returning true if successful and false otherwise
        const response = await axios.post('http://localhost:8080/register', formData);
        setUsernameAvailable(response.data);

        // Move back to the Login page if the username is available
        if (response.data) {
            navigate('/login');
        }
    }

    return (
        <div className="login-body">
            <div className="wrapper bg-white">
                <img src="/logo.png" width="200" alt="Logo"/>
                <div className="h3 text-muted text-center pt-2">Registration</div>
                <form className="form-group" noValidate onSubmit={handleSubmit(onSubmit)}>
                    <div className="credentials-error">{!usernameAvailable && <p className="errorMsg">Username already exists</p>}</div>

                    <div className="form-group my-4">
                        <input type="text" placeholder="Username" {...register("username")}
                               className={`form-control ${errors.username ? 'is-invalid' : ''}`}/>
                        <div className="invalid-feedback">{errors.username?.message}</div>
                    </div>

                    <div className="form-group my-4">
                        <input type="password" placeholder="Password" {...register("password")}
                               className={`form-control ${errors.password ? 'is-invalid' : ''}`}/>
                        <div className="invalid-feedback">{errors.password?.message}</div>
                    </div>

                    <input type="submit" className="btn btn-block text-center my-3" value="Register" />
                    <div className="text-center pt-3 text-muted">Already a member?
                        <Link to={"/login"}> Log in</Link>
                    </div>
                </form>
            </div>
        </div>
    );
}