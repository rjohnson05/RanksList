import React, { useEffect, useState } from 'react';
import { Row } from 'react-bootstrap';
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import StarBorderOutlinedIcon from '@mui/icons-material/StarBorderOutlined';
import StarOutlinedIcon from '@mui/icons-material/StarOutlined';
import CreateIcon from '@mui/icons-material/Create';
import axios from 'axios';
import {Link} from "react-router-dom";
import NavBar from "./NavBar";
import CreateGoalForm from "../CreateGoalPage/CreateGoalForm";

export default function Home() {
    const [allAdsData, setAds] = useState([]);
    const [isSelected, setSelected] = useState(false);


    useEffect(() => {
        loadAds();
    }, []);

    const loadAds = async () => {
        const ads_data = await axios.get("http://localhost:8080/ads");
        setAds(ads_data.data);
    }

    const saveAd = (adId, selected) => {
        if (!isSelected) {
            axios.put("http://localhost:8080/saved_ads/" + adId)
                .then(response => {console.log("Saved Ad #" + adId)})
                .catch(error => {console.log(error.response.data)})
        } else {
            axios.delete("http://localhost:8080/saved_ads/" + adId)
                .then(response => {console.log("Unsaved Ad #" + adId)})
                .catch(error => {console.log(error.response.data)})
        }

        setSelected(!isSelected);
    }

    const deleteAd = (adId) => {
        console.log(adId);

        axios.delete("http://localhost:8080/ads/" + adId)
            .then(response => {console.log("Deleted ad with ID " + adId)})
            .catch(error => {console.error(error)});
    }

    function createGoal(adId) {
        CreateGoalForm.adId = adId
        }

    return (
        <div>
            {/*Navbar code came from https://getbootstrap.com/docs/5.0/components/navbar/*/}
            <NavBar />

            {/*Sidebar code came from https://dev.to/codeply/bootstrap-5-sidebar-examples-38pb*/}
            <div className="container-fluid">
                <div className="row flex-nowrap">
                    <div className="col-auto col-md-3 col-xl-2 px-sm-2 px-0 bg-dark">
                        <div className="d-flex flex-column align-items-center align-items-sm-start px-3 pt-2 text-white min-vh-100">
                            <a href="/" className="d-flex align-items-center pb-3 mb-md-0 me-md-auto text-white text-decoration-none">
                                <span className="fs-5 d-none d-sm-inline">Menu</span>
                            </a>
                            <ul className="nav nav-pills flex-column mb-sm-auto mb-0 align-items-center align-items-sm-start" id="menu">
                                <li className="nav-item">
                                    <a href="#" className="nav-link align-middle px-0">
                                        <i className="fs-4 bi-house"></i> <span className   ="ms-1 d-none d-sm-inline"><Link to="/goals"> Goals </Link></span>
                                    </a>
                                </li>
                                <li>
                                    <a href="#submenu1" data-bs-toggle="collapse" className="nav-link px-0 align-middle">
                                        <i className="fs-4 bi-speedometer2"></i> <span className="ms-1 d-none d-sm-inline">Dashboard</span> </a>
                                    <ul className="collapse show nav flex-column ms-1" id="submenu1" data-bs-parent="#menu">
                                        <li className="w-100">
                                            <a href="#" className="nav-link px-0"> <span className="d-none d-sm-inline">Item</span> 1 </a>
                                        </li>
                                        <li>
                                            <a href="#" className="nav-link px-0"> <span className="d-none d-sm-inline">Item</span> 2 </a>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <a href="#" className="nav-link px-0 align-middle">
                                        <i className="fs-4 bi-table"></i> <span className="ms-1 d-none d-sm-inline">Orders</span></a>
                                </li>
                                <li>
                                    <a href="#submenu2" data-bs-toggle="collapse" className="nav-link px-0 align-middle ">
                                        <i className="fs-4 bi-bootstrap"></i> <span className="ms-1 d-none d-sm-inline">Bootstrap</span></a>
                                    <ul className="collapse nav flex-column ms-1" id="submenu2" data-bs-parent="#menu">
                                        <li className="w-100">
                                            <a href="#" className="nav-link px-0"> <span className="d-none d-sm-inline">Item</span> 1</a>
                                        </li>
                                        <li>
                                            <a href="#" className="nav-link px-0"> <span className="d-none d-sm-inline">Item</span> 2</a>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <a href="#submenu3" data-bs-toggle="collapse" className="nav-link px-0 align-middle">
                                        <i className="fs-4 bi-grid"></i> <span className="ms-1 d-none d-sm-inline">Products</span> </a>
                                    <ul className="collapse nav flex-column ms-1" id="submenu3" data-bs-parent="#menu">
                                        <li className="w-100">
                                            <a href="#" className="nav-link px-0"> <span className="d-none d-sm-inline">Product</span> 1</a>
                                        </li>
                                        <li>
                                            <a href="#" className="nav-link px-0"> <span className="d-none d-sm-inline">Product</span> 2</a>
                                        </li>
                                        <li>
                                            <a href="#" className="nav-link px-0"> <span className="d-none d-sm-inline">Product</span> 3</a>
                                        </li>
                                        <li>
                                            <a href="#" className="nav-link px-0"> <span className="d-none d-sm-inline">Product</span> 4</a>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <a href="#" className="nav-link px-0 align-middle">
                                        <i className="fs-4 bi-people"></i> <span className="ms-1 d-none d-sm-inline">Customers</span> </a>
                                </li>
                            </ul>
                            <hr />
                            <div className="dropdown pb-4">
                                <a href="#" className="d-flex align-items-center text-white text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                                    <img src="https://github.com/mdo.png" alt="hugenerd" width="30" height="30" className="rounded-circle" />
                                    <span className="d-none d-sm-inline mx-1">Profile</span>
                                    <ul className="dropdown-menu dropdown-menu-dark text-small shadow" aria-labelledby="dropdownUser1">
                                        <li><a className="dropdown-item" href="#">New project...</a></li>
                                        <li><a className="dropdown-item" href="#">Settings</a></li>
                                        <li><a className="dropdown-item" href="#">Profile</a></li>
                                        <li>
                                            <hr className="dropdown-divider" />
                                        </li>
                                        <li><a className="dropdown-item" href="#">Sign out</a></li>
                                    </ul>
                                </a>
                            </div>
                        </div>
                    </div>
                    {/*End of copied sidebar code */}
                    <div className="col py-3">

                        <Row xs={3}>
                            {allAdsData.map((ad, id) => (
                                <div className="col border border-5" key={id}>
                                    <p>Name: {ad.name}</p>
                                    <p>Price: {ad.price}</p>
                                    <p>Description: {ad.description}</p>
                                    <p><Link to={"/individual_goals/" + ad.id}> View Goals </Link></p>
                                    <IconButton value={id} onClick={() => {saveAd(ad.id)}}>
                                        {isSelected ? <StarOutlinedIcon /> : <StarBorderOutlinedIcon />}
                                    </IconButton>
                                    <IconButton value={id} onClick={() => deleteAd(ad.id)}>
                                        <DeleteIcon />
                                    </IconButton>
                                    <IconButton value={id} onClick={() => createGoal(ad.id)}>
                                        <Link to={'create_goal/' + ad.id}>
                                        <CreateIcon />
                                        </Link>
                                    </IconButton>
                                </div>
                            ))}
                        </Row>
                    </div>
                </div>

            <div className="col py-3">

                <Row xs={3}>
                    {allAdsData.map((ad, id) => (
                        <div className="col border border-5" key={id}>
                            <p>Name: {ad.name}</p>
                            <p>Price: {ad.price}</p>
                            <p>Description: {ad.description}</p>
                            <IconButton value={id} onClick={() => {saveAd(ad.id)}}>
                                {isSelected ? <StarOutlinedIcon /> : <StarBorderOutlinedIcon />}
                            </IconButton>
                            <IconButton value={id} onClick={() => deleteAd(ad.id)}>
                                <DeleteIcon />
                            </IconButton>
                        </div>
                    ))}
                </Row>
            </div>
        </div>
        </div>

    );
}