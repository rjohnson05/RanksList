import React, { useState } from 'react';

import axios from 'axios';
import {useLocation} from "react-router-dom";
import NavBar from "../MainPage/NavBar";

// Form for creating a new goal that will be added to the specific goal
export default function CreateGoalForm() {
    // Initial data list for an empty form
    // This will update values of this list will change as the form is filled out.
    const [formData, setFormData] = useState({
        name: "",
        description: "",
        adId: Number(useLocation().pathname.split('/').pop())
    });

    // Posts the entered data onto the main page when the "Submit" button is clicked
    const handleSubmit = (e) => {

        axios.post('http://localhost:8080/goals', formData)
            .then((response) => {console.log(response.data)})
            .catch((error) => {console.log(error)});
    }

    // Contains the form elements that will be rendered to the screen
    return (
        <div>
            <NavBar />
        <form onSubmit={handleSubmit}>
            <h1>Add New Goal</h1>
            <label>Description:
                <input type="text" name="description" value = {formData.description}
                       onChange={(e) => setFormData((prev) => ({ ...prev, description: e.target.value }))} />
            </label>
            <input type="hidden" name="adId" value = {formData.adId}
                   onChange={(e) => setFormData((prev) => ({ ...prev, adId: e.target.value }))} />
            <input type="submit" value="Submit" />

            {/*// checkboxes*/}
            <fieldset>
                <legend>Choose your monster's features:</legend>

                <div>
                    <input type="checkbox" id="scales" name="scales"  />
                    <label htmlFor="scales">Scales</label>
                </div>

                <div>
                    <input type="checkbox" id="horns" name="horns" />
                    <label for="horns">Horns</label>
                </div>
            </fieldset>


        </form>
        </div>
    );
}