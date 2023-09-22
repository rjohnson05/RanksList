import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";

export default function RegisterForm() {
    const navigate = useNavigate();
    const [loginStatus, setLoginStatus] = useState(0);
    console.log(loginStatus);
    const [formData, setFormData] = useState({
        username: "",
        password: ""
    })

    useEffect(() => {
        // Check whether the username is yet taken
        if (loginStatus) {
            // Move back to the Login page
            navigate('/home');
        }
    }, [loginStatus]);

    const handleSubmit = (e) => {
        e.preventDefault()

        axios.post('http://localhost:8080/login', formData)
            .then((response) => {setLoginStatus(response.data)})
            .catch((error) => {console.log(error)})
    }

    const handleRegister = (e) => {
        navigate('/register');
    }

    return (
        <form onSubmit={handleSubmit}>
            <h1>Welcome to Rank's List!</h1>
            <label>Username
                <input type="text" onChange={(e) => setFormData((prev) => ({...prev, username: e.target.value}))} />
            </label><br />
            <label>Password
                <input type="text" onChange={(e) => setFormData((prev) => ({...prev, password: e.target.value}))} />
            </label><br />
            <input type="submit" />
            <input type="button"  value="Register" onClick={handleRegister} />
        </form>
    );
}