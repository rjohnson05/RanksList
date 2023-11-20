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
    } = useForm({mode: "onBlur", reValidateMode: "onBlur"});

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

            <form onSubmit={handleSubmit(onSubmit)}>
                <h1>Edit Advertisement</h1>

                {errors.name?.type === "required" && <p className="errorMsg">A name is required</p>}
                {!adEdited && getValues("name") === originalData.name && parseFloat(getValues("price")) === originalData.price && getValues("description") === originalData.description &&
                    <p className="errorMsg">Changes must be made for the ad to be edited</p>}
                {!adEdited && getValues("name") !== originalData.name &&
                    <p className="errorMsg">You already have an advertisement with this name</p>}
                <label>Advertisement Name:
                    <input type="text" name="name" {...register("name", {
                        required: true
                    })} />
                </label><br/>
                {errors.price?.type === "required" && <p className="errorMsg">A price is required</p>}
                {errors.price?.type === "checkIsNumber" && <p className="errorMsg">Price must be a number</p>}
                {errors.price?.type === "checkNoCommas" && <p className="errorMsg">Price cannot contain commas</p>}
                <label>Price:
                    <input type="text" name="price" {...register("price", {
                        required: true,
                        validate: {checkIsNumber: (value) => !/[^0-9,.]/.test(value),
                            checkNoCommas: (value) => !value.toString().includes(',')}
                    })} />
                </label><br/>
                <label>Description:
                    <textarea rows="5" cols="40" name="description" {...register("description")} />
                </label><br/>
                <input type="submit" value="Submit" />
            </form>
        </div>
    );
}