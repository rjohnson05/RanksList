import React, {useState} from 'react';
import axios from "axios";
import {Link, useNavigate} from "react-router-dom";
import {useForm} from "react-hook-form";
import * as Yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import './LoginPage.css'

export default function LoginPage() {
    const navigate = useNavigate();
    const [loginStatus, setLoginStatus] = useState(true);

    const validationSchema = Yup.object().shape({
        username: Yup.string()
            .required("Username is required")
            .min(6, "Username must be 6-32 characters long")
            .max(32, "Username must be 6-32 characters long"),
        password: Yup.string()
            .required("Password is required")
            .min(8, "Password must 8-16 characters long")
            .max(16, "Password must 8-16 characters long")
    });

    const {register,
        handleSubmit,
        formState: { errors }
    } = useForm({mode: "onSubmit", reValidateMode: "onBlur", resolver: yupResolver(validationSchema)});

    const onSubmit = async (formData) => {
        const response = await axios.post('http://localhost:8080/login', formData);
        setLoginStatus(response.data);

        // Advances to the Home screen if the input credentials are valid
        if (response.data) {
            console.log("Moving to home page");
            navigate('/home');
        }
    }

    return (
        <div className="wrapper bg-white">
            <img src="/logo.png" width="200" alt="Logo"/>
            <div className="h3 text-muted text-center pt-2">Login</div>
            <form className="form-group" noValidate onSubmit={handleSubmit(onSubmit)}>
                <div className="credentials-error">{!loginStatus && <p className="credentials-error">Invalid Credentials Used</p>}</div>

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

                <input type="submit" className="btn btn-block text-center my-3" value="Log In" />
                <div className="text-center pt-3 text-muted">Not a member?
                    <Link to={"/register"}> Sign up</Link>
                </div>
            </form>
        </div>
    );
}