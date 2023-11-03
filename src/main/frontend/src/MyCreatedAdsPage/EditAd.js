import React, {useEffect, useState} from 'react';
import axios from 'axios';
import NavBar from "../MainPage/NavBar";
import {Navigate, useLocation, useNavigate} from "react-router-dom";

// Form for creating a new advertisement that will be added to the public stack of cards on the home page
export default function CreateAdForm() {
    const navigate = useNavigate();
    const [firstRender, setFirstRender] = useState(false);
    // Initial data list for an empty form
    // This will values of this list will change as the form is filled out.
    const [formData, setFormData] = useState({
        id: Number(useLocation().pathname.split('/').pop()),
        name: "",
        price: 0,
        image: [],
        description: ""
    });

    useEffect(() => {
        if (!firstRender) {
            loadFormData();
            setFirstRender(true);
        }
    });

    const loadFormData = async () => {
        const ad_data = await axios.get("http://localhost:8080/ads/" + formData.id);
        setFormData(ad_data.data);
    }

    // Posts the entered data onto the main page when the "Submit" button is clicked
    const handleSubmit = () => {
        axios.put('http://localhost:8080/edit_ad/' + formData.id, formData)
            .then((response) => {console.log(response.data)})
            .catch((error) => {console.log(error)});

        navigate("/my_ads")
    }

    // Contains the form elements that will be rendered to the screen
    return (
        <div>
            <NavBar />

            <form onSubmit={handleSubmit}>
                <h1>Edit Advertisement</h1>

                <label>Advertisement Name:
                    <input type="text" name="name" value = {formData.name}
                           onChange={(e) => setFormData((prev) => ({ ...prev, name: e.target.value }))} />
                </label>
                <label>Price:
                    <input type="text" name="price" value = {formData.price}
                           onChange={(e) => setFormData((prev) => ({ ...prev, price: e.target.value }))} />
                </label>
                <label>Description:
                    <input type="text" name="description" value = {formData.description}
                           onChange={(e) => setFormData((prev) => ({ ...prev, description: e.target.value }))} />
                </label>
                <input type="submit" value="Submit" />
            </form>
        </div>
    );
}