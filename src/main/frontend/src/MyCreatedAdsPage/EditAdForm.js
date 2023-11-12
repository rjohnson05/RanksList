import React, {useEffect, useState} from 'react';
import axios from 'axios';
import NavBar from "../MainPage/NavBar";
import {useLocation, useNavigate} from "react-router-dom";
import {useForm} from "react-hook-form";

// Form for creating a new advertisement that will be added to the public stack of cards on the home page
export default function CreateAdForm() {
    const navigate = useNavigate();
    const [firstRender, setFirstRender] = useState(false);
    const [adEdited, setAdEdited] = useState(true);

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
    } = useForm();

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
        console.log("Successful: ", response.data);

        // If ad is edited successfully, navigates back to My Created Ads page
        if (response.data) {
            navigate("/my_ads")
        }
    }

    // Contains the form elements that will be rendered to the screen
    return (
        <div>
            <NavBar />

            <form onSubmit={handleSubmit(onSubmit)}>
                <h1>Edit Advertisement</h1>

                {errors.name?.type === "required" && <p className="errorMsg">A name is required</p>}
                {!adEdited && getValues("name") === originalData.name && getValues("price") === originalData.price && getValues("description") === originalData.description &&
                    <p className="errorMsg">Changes must be made for the ad to be edited</p>}
                {!adEdited && getValues("name") !== originalData.name &&
                    <p className="errorMsg">You already have an advertisement with this name</p>}
                <label>Advertisement Name:
                    <input type="text" name="name" {...register("name", {
                        required: true
                    })} />
                </label>
                <label>Price:
                    <input type="text" name="price" {...register("price")} />
                </label>
                <label>Description:
                    <input type="text" name="description" {...register("description")} />
                </label>
                <input type="submit" value="Submit" />
            </form>
        </div>
    );
}