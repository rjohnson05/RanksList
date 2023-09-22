import React, {useEffect, useState} from 'react';
import { useNavigate } from "react-router-dom"
import axios from "axios";

export default function RegistrationForm() {
    const navigate = useNavigate();
    const [registrationStatus, setRegistrationStatus] = useState(0);

    const [formData, setFormData] = useState({
        username: "",
        password: "",
    });

    useEffect(() => {
        // Check whether the username is yet taken
        if (registrationStatus) {
            // Move back to the Login page
            navigate('/login');
        }
    }, [registrationStatus]);

    const handleSubmit = async (e) => {
        e.preventDefault()

        // Push the form data to the database, returning true if successful and false otherwise
        const registrationStatus = axios.post('http://localhost:8080/register', formData)
            .then((response) => setRegistrationStatus(response.data))
            .catch((error) => {console.log("Error: ", error)});
    }

    return (
        <form onSubmit={handleSubmit}>
            <h1>Registration</h1>
            {/*Show user error if the username is already taken*/}
            {!registrationStatus && registrationStatus !== 0 && <p>Username already taken</p>}
            <label>Username
                <input type="text" onChange={(e) => setFormData((prev) => ({...prev, username: e.target.value}))} />
            </label><br />
            <label>Password
                <input type="text" onChange={(e) => setFormData((prev) => ({...prev, password: e.target.value}))} />
            </label><br />
            <input type="submit" name="submit"/>
        </form>
    );
}