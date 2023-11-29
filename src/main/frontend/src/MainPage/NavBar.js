import React from "react";
import {Link} from "react-router-dom";
import './NavBar.css';

export default function NavBar() {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container-fluid">
                <Link to={"/home"} className="navbar-brand"><img className="nav-logo" src="/logo.png" width="60" alt="Logo"/></Link>
                <Link to={"/home"} className="navbar-brand logo-name">RANK'S LIST</Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul className="navbar-nav ms-auto">
                        <li className="nav-item">
                            <Link to={"/home"} className="nav-link">HOME</Link>
                        </li>
                        <li className="nav-item">
                            <Link to={"/starred_ads"} className="nav-link">STARRED ADS</Link>
                        </li>
                        <li className="nav-item">
                            <Link to={"/create_ad"} className="nav-link">CREATE AD</Link>
                        </li>
                        <li className="nav-item">
                            <Link to={"/my_ads"} className="nav-link">MY CREATED ADS</Link>
                        </li>
                        <li className="nav-item dropdown">
                            <a className="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                PROFILE
                            </a>
                            <ul className="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                <li><Link to={"/edit_password"} className="dropdown-item">Change Password</Link></li>
                                <li><Link to={"/login"} className="dropdown-item">Sign Out</Link></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    );
}