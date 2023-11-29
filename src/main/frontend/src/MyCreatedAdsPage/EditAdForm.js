import React, {useEffect, useState} from 'react';
import axios from 'axios';
import NavBar from "../MainPage/NavBar";
import {useLocation, useNavigate} from "react-router-dom";
import {useForm} from "react-hook-form";
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";

// Form for creating a new advertisement that will be added to the public stack of cards on the home page
export default function CreateAdForm() {
    const navigate = useNavigate();
    const [firstRender, setFirstRender] = useState(false);
    const [adEdited, setAdEdited] = useState(true);

    const validationSchema = Yup.object().shape({
        name: Yup.string()
            .required("Name is required"),
        price: Yup.string()
            .required("Price is required")
            .matches(/^\d*(\.\d{0,2})?$/, "Price must be a number"),
        description: Yup.string()
    });

    const [originalData, setOriginalData] = useState({
        id: Number(useLocation().pathname.split('/').pop()),
        name: "",
        price: "",
        description: ""
    });

    const {
        register,
        handleSubmit,
        getValues,
        setValue,
        formState: {errors}
    } = useForm({mode: "onBlur", reValidateMode: "onBlur", resolver: yupResolver(validationSchema)});

    useEffect(() => {
        if (!firstRender) {
            loadOriginalData();
            setFirstRender(true);
        }
    });

    const loadOriginalData = async () => {
        const ad_data = await axios.get("http://localhost:8080/ads/" + originalData.id);
        setOriginalData(ad_data.data);
        setValue('name', ad_data.data.name);
        setValue('price', ad_data.data.price);
        setValue('description', ad_data.data.description);
    }

    // Posts the entered data onto the main page when the "Submit" button is clicked
    const onSubmit = async (formData) => {
        const response = await axios.put('http://localhost:8080/edit_ad/' + originalData.id, formData);
        setAdEdited(response.data);

        // If ad is edited successfully, navigates back to My Created Ads page
        if (response.data) {
            navigate("/my_ads")
        }
    }

    // Contains the form elements that will be rendered to the screen
    return (
        <div>
            <NavBar />

            <div className="form-body">
                <div className="h3 text-muted text-center pt-2">Edit Advertisement</div>
                <form className="form-group" noValidate onSubmit={handleSubmit(onSubmit)}>
                    <div className="credentials-error">
                        {!adEdited && getValues("name") === originalData.name && parseFloat(getValues("price")) === originalData.price && getValues("description") === originalData.description &&
                            <p>Changes must be made for the ad to be edited</p>}
                    </div>
                    <div className="credentials-error">
                        {!adEdited && getValues("name") !== originalData.name &&
                            <p className="errorMsg">You already have an advertisement with this name</p>}</div>

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