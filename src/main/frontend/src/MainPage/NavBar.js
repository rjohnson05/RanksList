import React from "react";
import {Link} from "react-router-dom";

export default function NavBar() {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container-fluid">
                <Link to={"/home"} className="navbar-brand">Rank's List</Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <Link to={"/home"} className="nav-link">Home</Link>
                        </li>
                        <li className="nav-item">
                            <Link to={"/starred_ads"} className="nav-link">Starred Ads</Link>
                        </li>
                        <li className="nav-item">
                            <Link to={"/create_ad"} className="nav-link">Create Ad</Link>
                        </li>
                        <li className="nav-item">
                            <Link to={"/my_ads"} className="nav-link">My Created Ads</Link>
                        </li>
                        <li className="nav-item dropdown">
                            <a className="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                Profile
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