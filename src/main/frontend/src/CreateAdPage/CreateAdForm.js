import React, { useState } from 'react';
import {useNavigate} from "react-router-dom";
import axios from 'axios';
import NavBar from "../MainPage/NavBar";
import {useForm} from "react-hook-form";

// Form for creating a new advertisement that will be added to the public stack of cards on the home page
export default function CreateAdForm() {
    const navigate = useNavigate();
    const [adCreated, setAdCreated] = useState(true);
    const {register,
        handleSubmit,
        formState: { errors }
    } = useForm({mode: "onBlur", reValidateMode: "onBlur"});

    // Posts the entered data onto the main page when the "Submit" button is clicked
    const onSubmit = async (formData) => {
        console.log(formData);
        const response = await axios.post('http://localhost:8080/ads', formData);
        console.log(response);
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
            <form onSubmit={handleSubmit(onSubmit)}>
                <h1>Create New Advertisement</h1>
                {errors.name?.type === "required" && <p className="errorMsg">A name is required</p>}
                {!adCreated && <p className="errorMsg">You already have an advertisement with this name</p>}
                <label>Advertisement Name:
                    <input type="text" name="name"
                           {...register("name",
                               {required: true})} />
                </label><br/>
                {errors.price?.type === "required" && <p className="errorMsg">A price is required</p>}
                {errors.price?.type === "checkIsNumber" && <p className="errorMsg">Price must be a number</p>}
                {errors.price?.type === "checkNoCommas" && <p className="errorMsg">Price cannot contain commas</p>}
                <label>Price:
                    <input type="text" name="price"
                           {...register("price",
                               {required: true, value: 0,
                                   validate: {checkIsNumber: (value) => !/[^0-9,.]/.test(value),
                                   checkNoCommas: (value) => !value.includes(',')}})} />
                </label><br/>
                <label>Description:
                    <textarea rows="5" cols="40" name="description"
                           {...register("description")} />
                </label><br/>
                <input type="submit" value="Submit" />
            </form>
        </div>
    );
}