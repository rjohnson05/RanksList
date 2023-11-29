import React, {useState} from 'react';
import {useNavigate} from "react-router-dom"
import { useForm } from "react-hook-form"
import axios from "axios";
import NavBar from "../MainPage/NavBar";
import * as Yup from "yup";
import {yupResolver} from "@hookform/resolvers/yup";

export default function EditPasswordForm() {
    const navigate = useNavigate();
    const [correctPassword, setCorrectPassword] = useState(true);

    const validationSchema = Yup.object().shape({
        password: Yup.string()
            .required("Current password is required"),
        newPassword: Yup.string()
            .required("New password is required")
            .min(8, "Password must be 8-16 characters long")
            .max(16, "Password must be 8-16 characters long")
            .matches(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/,
                "Must contain at least 1 uppercase letter, 1 number, and 1 special character")
            .notOneOf([Yup.ref('currentPassword'), null], "Must be different than current password")
    });

    const {
        register,
        handleSubmit,
        formState: {errors}
    } = useForm({mode: "onSubmit", reValidateMode: "onBlur", resolver: yupResolver(validationSchema)});

    const onSubmit = async (formData) => {
        const response = await axios.put('http://localhost:8080/edit_password', formData);
        setCorrectPassword(response.data);

        // If password is edited successfully, navigates back to My Created Ads page
        if (response.data) {
            navigate('/home');
        }
    }

    return (
        <div>
            <NavBar />
            <div className="form-body">
                <div className="h3 text-muted text-center pt-2">Update Password</div>
                <form className="form-group" noValidate onSubmit={handleSubmit(onSubmit)}>
                    <div className="credentials-error">
                        {!correctPassword && <p>Incorrect Password</p>}
                    </div>

                    <div className="form-group my-4">
                        <input type="password" placeholder="Current Password" {...register("password")}
                               className={`form-control ${errors.password ? 'is-invalid' : ''}`}/>
                        <div className="invalid-feedback">{errors.password?.message}</div>
                    </div>

                    <div className="form-group my-4">
                        <input type="password" placeholder="New Password" {...register("newPassword")}
                               className={`form-control ${errors.newPassword ? 'is-invalid' : ''}`}/>
                        <div className="invalid-feedback">{errors.newPassword?.message}</div>
                    </div>

                    <input type="submit" className="btn btn-block text-center my-3" value="Update Password" />
                </form>
            </div>
        </div>


        //
        // <div>
        // <NavBar />
        // <form onSubmit={handleSubmit(onSubmit)}>
        //     <h1>Change Password</h1>
        //     {errors.username?.type === "required" && <p className="errorMsg">Username is required</p>}
        //     {errors.username?.type === "checkLength" && <p className="errorMsg">Username must be between 6-32 characters</p>}
        //     <label>Username
        //         <input type="text" {...register("username",
        //             {required: true,
        //                 validate: {
        //                     checkLength: (value) => value.length >= 6 && value.length <= 32,
        //                 }
        //             })} />
        //     </label><br />
        //     {errors.password?.type === "required" && <p className="errorMsg">Password is required</p>}
        //     {errors.password?.type === "checkLength" && <p className="errorMsg">Password must be between 8-16 characters, and must contain at least one capital letter, a number, and a special character</p>}
        //     {errors.password?.type === "checkCapitalPresence" && <p className="errorMsg">Password must be between 8-16 characters, and must contain at least one capital letter, a number, and a special character</p>}
        //     {errors.password?.type === "checkNumberPresence" && <p className="errorMsg">Password must be between 8-16 characters, and must contain at least one capital letter, a number, and a special character</p>}
        //     {errors.password?.type === "checkSpecialPresence" && <p className="errorMsg">Password must be between 8-16 characters, and must contain at least one capital letter, a number, and a special character</p>}
        //     <label>Old Password
        //         <input type="password" {...register("password",
        //             {required: true,
        //                 validate: {
        //                     checkLength: (value) => value.length >= 8 && value.length <= 16,
        //                     checkCapitalPresence: (value) => /[A-Z]/.test(value),
        //                     checkNumberPresence: (value) => /[1-9]/.test(value),
        //                     checkSpecialPresence: (value) => /[^a-zA-Z1-9]/.test(value)
        //                 }})} />
        //     </label><br />
        //     <label>New Password
        //         <input type="password" {...register("newPassword",
        //             {required: true,
        //                 validate: {
        //                     checkLength: (value) => value.length >= 8 && value.length <= 16,
        //                     checkCapitalPresence: (value) => /[A-Z]/.test(value),
        //                     checkNumberPresence: (value) => /[1-9]/.test(value),
        //                     checkSpecialPresence: (value) => /[^a-zA-Z1-9]/.test(value)
        //                 }})} />
        //     </label><br />
        //     {errors.newPassword?.type === "required" && <p className="errorMsg">New password is required</p>}
        //     {errors.newPassword?.type === "checkLength" && <p className="errorMsg">New password must be between 8-16 characters, and must contain at least one capital letter, a number, and a special character</p>}
        //     {errors.newPassword?.type === "checkCapitalPresence" && <p className="errorMsg">New password must be between 8-16 characters, and must contain at least one capital letter, a number, and a special character</p>}
        //     {errors.newPassword?.type === "checkNumberPresence" && <p className="errorMsg">New password must be between 8-16 characters, and must contain at least one capital letter, a number, and a special character</p>}
        //     {errors.newPassword?.type === "checkSpecialPresence" && <p className="errorMsg">New password must be between 8-16 characters, and must contain at least one capital letter, a number, and a special character</p>}
        //     <input type="submit" name="submit"/>
        // </form>
        // </div>
    );

}