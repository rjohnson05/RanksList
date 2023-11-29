import React, { useState } from 'react';
import {useNavigate} from "react-router-dom";
import axios from 'axios';
import NavBar from "../MainPage/NavBar";
import {useForm} from "react-hook-form";
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";
import '.././BasicForm.css'

// Form for creating a new advertisement that will be added to the public stack of cards on the home page
export default function CreateAdForm() {
    const navigate = useNavigate();
    const [adCreated, setAdCreated] = useState(true);

    const validationSchema = Yup.object().shape({
        name: Yup.string()
            .required("Name is required"),
        price: Yup.string()
            .required("Price is required")
            .matches(/^\d*(\.\d{0,2})?$/, "Price must be a number"),
        description: Yup.string()
    });

    const {register,
        handleSubmit,
        formState: { errors }
    } = useForm({mode: "onSubmit", reValidateMode: "onBlur", resolver: yupResolver(validationSchema)});

    // Posts the entered data onto the main page when the "Submit" button is clicked
    const onSubmit = async (formData) => {
        const response = await axios.post('http://localhost:8080/ads', formData);
        setAdCreated(response.data);

        // Upon successful ad creation, returns to the home page
        if (response.data) {
            navigate('/home');
        }
    }

    // Contains the form elements that will be rendered to the screen
    return (
        <div>
            <NavBar />
            <div className="form-body">
                <div className="h3 text-muted text-center pt-2">Create New Advertisement</div>
                <form className="form-group" noValidate onSubmit={handleSubmit(onSubmit)}>
                    <div className="credentials-error">{!adCreated && <p>You already have an advertisement with this name</p>}</div>

                    <div className="form-group my-4">
                        <input type="text" placeholder="Name" {...register("name")}
                               className={`form-control ${errors.name ? 'is-invalid' : ''}`}/>
                        <div className="invalid-feedback">{errors.name?.message}</div>
                    </div>

                    <div className="form-group my-4">
                        <input type="text" placeholder="Price" {...register("price")}
                               className={`form-control ${errors.price ? 'is-invalid' : ''}`}/>
                        <div className="invalid-feedback">{errors.price?.message}</div>
                    </div>

                    <div className="form-group my-4">
                        <textarea placeholder="Description" {...register("description")}
                               className={`form-control ${errors.description ? 'is-invalid' : ''}`}/>
                        <div className="invalid-feedback">{errors.description?.message}</div>
                    </div>

                    <input type="submit" className="btn btn-block text-center my-3" value="Create Ad" />
                </form>
            </div>
        </div>
    );
}