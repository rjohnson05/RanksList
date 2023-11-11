import React, {useEffect, useState} from 'react';
import { useNavigate } from "react-router-dom"
import { useForm } from "react-hook-form"
import axios from "axios";

export default function RegistrationForm() {
    const navigate = useNavigate();
    const [usernameAvailable, setUsernameAvailable] = useState(true);
    const {register,
        handleSubmit,
        formState: { errors }
    } = useForm({mode: "onBlur", reValidateMode: "onBlur"});

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
        <form onSubmit={handleSubmit(onSubmit)}>
            <h1>Registration</h1>
            {errors.username?.type === "required" && <p className="errorMsg">Username is required</p>}
            {errors.username?.type === "checkLength" && <p className="errorMsg">Username must be between 6-32 characters</p>}
            {!usernameAvailable && <p className="errorMsg">Username already exists</p>}
            <label>Username
                <input type="text" {...register("username",
                    {required: true,
                        validate: {
                            checkLength: (value) => value.length >= 6 && value.length <= 32,
                        }
                        })} />
            </label><br />
            {errors.password?.type === "required" && <p className="errorMsg">Password is required</p>}
            {errors.password?.type === "checkLength" && <p className="errorMsg">Password must be between 8-16 characters</p>}
            {errors.password?.type === "checkCapitalPresence" && <p className="errorMsg">Password must contain at least one capital letter</p>}
            {errors.password?.type === "checkNumberPresence" && <p className="errorMsg">Password must contain at least one number</p>}
            {errors.password?.type === "checkSpecialPresence" && <p className="errorMsg">Password must contain at least one special character</p>}
            <label>Password
                <input type="password" {...register("password",
                    {required: true,
                        validate: {
                            checkLength: (value) => value.length >= 8 && value.length <= 16,
                            checkCapitalPresence: (value) => /[A-Z]/.test(value),
                            checkNumberPresence: (value) => /[1-9]/.test(value),
                            checkSpecialPresence: (value) => /[^a-zA-Z1-9]/.test(value)
                        }})} />
            </label><br />
            <input type="submit" name="submit"/>
        </form>
    );
}