import React from 'react';
import './App.css';
import Home from './MainPage/Home';
import RegistrationForm from './LoginPage/RegistrationForm';
import LoginPage from './LoginPage/LoginPage';
import Goals from './MainPage/Goals';
import {HashRouter, Navigate, Route, Routes} from "react-router-dom";
import CreateAdForm from "./CreateAdPage/CreateAdForm";
import IndividualGoals from "./IndividualGoalsPage/IndividualGoals";
import CreateGoalForm from "./CreateGoalPage/CreateGoalForm";
import StarredAds from "./StarredAdsPage/StarredAds";
import MyCreatedAds from "./MyCreatedAdsPage/MyCreatedAds";
import EditAd from "./MyCreatedAdsPage/EditAdForm";
import EditPasswordForm from "./LoginPage/EditPasswordForm";

export default function App() {
    return (
        <div className="App">
            <HashRouter>
                <Routes>
                    <Route path='/' element={<Navigate to='login' />} />
                    <Route path='home' element={<Home />} />
                    <Route path='login' element={<LoginPage />} />
                    <Route path='register' element={<RegistrationForm />} />
                    <Route path='create_ad' element={<CreateAdForm />} />
                    <Route path='goals' element={<Goals />} />
                    <Route path='starred_ads' element={<StarredAds />} />
                    <Route path='edit_ad/*' element={<EditAd />} />
                    <Route path={'my_ads'} element={<MyCreatedAds />} />
                    <Route path={'individual_goals/*'} element={<IndividualGoals />} />
                    <Route path={'individual_goals/create_goal/*'} element={<CreateGoalForm />} />
                    <Route path={'edit_password'} element={<EditPasswordForm/>} />
                </Routes>
            </HashRouter>
        </div>
    );
};