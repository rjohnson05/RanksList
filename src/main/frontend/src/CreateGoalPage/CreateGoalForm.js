import React, { useState } from 'react';
import axios from 'axios';
import {useLocation, useNavigate} from "react-router-dom";
import NavBar from "../MainPage/NavBar";
import {useForm} from "react-hook-form";

// Form for creating a new goal that will be added to the specific goal
export default function CreateGoalForm() {
    const navigate = useNavigate();
    // Initial data list for an empty form
    // This will update values of this list will change as the form is filled out.
    const {register,
        handleSubmit,
        formState: { errors }
    } = useForm({mode: "onBlur", reValidateMode: "onBlur"});
    const adId = Number(useLocation().pathname.split('/').pop());

    // Posts the entered data onto the main page when the "Submit" button is clicked
    const onSubmit = async (formData) => {
        const response = await axios.post('http://localhost:8080/goals', formData);

        if (response.data) {
            navigate('/individual_goals/' + adId);
        }
    }

    // Contains the form elements that will be rendered to the screen
    return (
        <div>
            <NavBar />
            <form onSubmit={handleSubmit(onSubmit)}>
                <h1>Add New Goal</h1>
                {errors.description?.type === "required" && <p className="errorMsg">A description is required</p>}
                <label>Description:
                    <input type="text" name="description" {...register("description", {
                        required: true
                    })} />
                </label>
                <input type="hidden" name="adId" {...register("adId", {
                    value: adId
                })} />

                <input type="submit" value="Submit" />
            </form>
        </div>
    );
}