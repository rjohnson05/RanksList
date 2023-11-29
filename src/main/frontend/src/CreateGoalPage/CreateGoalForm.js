import React, { useState } from 'react';
import axios from 'axios';
import {useLocation, useNavigate} from "react-router-dom";
import NavBar from "../MainPage/NavBar";
import {useForm} from "react-hook-form";
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";

// Form for creating a new goal that will be added to the specific goal
export default function CreateGoalForm() {
    const navigate = useNavigate();
    // Initial data list for an empty form
    // This will update values of this list will change as the form is filled out.
    const validationSchema = Yup.object().shape({
        description: Yup.string()
            .required("A description is required")
    });

    const {register,
        handleSubmit,
        formState: { errors }
    } = useForm({mode: "onSubmit", reValidateMode: "onBlur", resolver: yupResolver(validationSchema)});
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
            <div className="form-body">
                <div className="h3 text-muted text-center pt-2">Create New Goal</div>
                <form className="form-group" noValidate onSubmit={handleSubmit(onSubmit)}>
                    <div className="form-group my-4">
                        <textarea placeholder="Description" {...register("description")}
                                  className={`form-control ${errors.description ? 'is-invalid' : ''}`}/>
                        <div className="invalid-feedback">{errors.description?.message}</div>
                    </div>

                    <input type="hidden" name="adId" {...register("adId", {
                        value: adId
                    })} />

                    <input type="submit" className="btn btn-block text-center my-3" value="Create Goal" />
                </form>
            </div>
        </div>
    );
}