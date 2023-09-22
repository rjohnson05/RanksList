import React from 'react';
import './App.css';
import Home from './MainPage/Home';
import { BrowserRouter as Router, Routes, Route, Redirect} from "react-router-dom";
import Goals from './MainPage/Goals'


export default function App() {

    return (
        <div className="App">
            <Router>
                <Routes>
                    <Route path="" element={<Home />} />
                    <Route path="/Goals" element={<Goals />}  />
                    <Route path="/Home" element={<Home />} />
                </Routes>
            </Router>
        </div>
  );
};