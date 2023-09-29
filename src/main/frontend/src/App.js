import React from 'react';
import './App.css';
import Home from './MainPage/Home';
import RegistrationForm from './LoginPage/RegistrationForm'
import LoginForm from './LoginPage/LoginForm';
import Goals from './MainPage/Goals'
import {BrowserRouter, Navigate, Route, Router, Routes} from "react-router-dom";
import CreateAdForm from "./CreateAdPage/CreateAdForm";
import SavedAds from "./SavedAdsPage/SavedAds";

export default function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Routes>'
                    <Route path='/' element={<Navigate to='login' />} />
                    <Route path='home' element={<Home />} />
                    <Route path='login' element={<LoginForm />} />
                    <Route path='register' element={<RegistrationForm />} />
                    <Route path='create_ad' element={<CreateAdForm />} />
                    <Route path='goals' element={<Goals />} />
                    <Route path='saved_ads' element={<SavedAds />} />
                </Routes>
            </BrowserRouter>
        </div>
    );
};