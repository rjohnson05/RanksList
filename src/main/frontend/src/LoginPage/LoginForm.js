import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import { useCookies } from "react-cookie";
import {useForm} from "react-hook-form";
axios.defaults.withCredentials = true;


export default function RegisterForm() {
    const navigate = useNavigate();
    const [loginStatus, setLoginStatus] = useState(true);
    const {register,
        handleSubmit,
        formState: { errors }
    } = useForm({mode: "onBlur", reValidateMode: "onBlur"});

    const onSubmit = async (formData) => {
        const response = await axios.post('http://localhost:8080/login', formData);
        setLoginStatus(response.data);

        // Advances to the Home screen if the input credentials are valid
        if (response.data) {
            navigate('/home');
        }
    }

    const handleRegister = (e) => {
        navigate('/register');
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <h1>Welcome to Rank's List!</h1>
            {!loginStatus && <p className="errorMsg">Invalid Credentials Used</p>}
            {errors.username?.type === "required" && <p className="errorMsg">Username is required</p>}
            {errors.username?.type === "checkLength" && <p className="errorMsg">Username must be between 6-32 characters</p>}
            <label>Username
                <input type="text" {...register("username",
                    {required: true,
                        validate: {
                            checkLength: (value) => value.length >= 6 && value.length <= 32
                        }})} />
            </label><br />
            {errors.password?.type === "required" && <p className="errorMsg">Password is required</p>}
            {errors.password?.type === "checkLength" && <p className="errorMsg">Password must be between 6-32 characters</p>}
            <label>Password
                <input type="password" {...register("password",
                    {required: true,
                        validate: {
                            checkLength: (value) => value.length >= 8 && value.length <= 16
                        }})} />
            </label><br />
            <input type="submit" />
            <input type="button"  value="Register" onClick={handleRegister} />
        </form>
    );
}