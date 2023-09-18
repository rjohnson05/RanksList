import React, { useState } from 'react';
import axios from 'axios';

// Form for creating a new advertisement that will be added to the public stack of cards on the home page
export default function CreateAdForm() {
    // Initial data list for an empty form
    // This will values of this list will change as the form is filled out.
    const [formData, setFormData] = useState({
        name: "",
        price: 0,
        image: [],
        description: ""
    });

    // Posts the entered data onto the main page when the "Submit" button is clicked
    const handleSubmit = (e) => {
        e.preventDefault()
        
        axios.post('http://localhost:8080/ads', formData)
            .then((response) => {console.log(response.data)})
            .catch((error) => {console.log(error)});      
    }

    // Contains the form elements that will be rendered to the screen
    return (
        <form onSubmit={handleSubmit}>
            <h1>Create New Advertisement</h1>

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
    );
}