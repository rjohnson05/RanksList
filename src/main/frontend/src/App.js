import React from 'react';
import './App.css';
import Home from './MainPage/Home';
import RegistrationForm from './LoginPage/RegistrationForm'
import LoginForm from './LoginPage/LoginForm';
import Goals from './MainPage/Goals'
import {BrowserRouter, Navigate, Route, Router, Routes} from "react-router-dom";

export default function App() {
  return (
      <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Navigate to='login' />} />
          <Route path='home' element={<Home />} />
          <Route path='login' element={<LoginForm />} />
          <Route path='register' element={<RegistrationForm />} />
            <Route path='goals' element={<Goals />}  />
        </Routes>
      </BrowserRouter>
      </div>
  );
};