import React, { useEffect, useState } from 'react';
import { Row } from 'react-bootstrap';
import IconButton from 'rsuite/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import axios from 'axios';
import CreateAdForm from '.././CreateAdPage/CreateAdForm'
import CreateGoalForm from '../CreatedGoalPage/CreateGoalForm'


export default function Home() {
    const [allAdsData, setAds] = useState([]);
    const adId = useState();

    const [allGoalsData, setGoals] = useState([]);
    const goalId = useState();


    useEffect(() => {
        loadGoals();
        //loadAds();
    }, []);

    const loadAds = async () => {
        const ads_data = await axios.get("http://localhost:8080/ads");
        setAds(ads_data.data);
    }

    const loadGoals = async () => {
        const goals_data = await axios.get("http://localhost:8080/goals");
        setGoals(goals_data.data);
    }


    const deleteAd = (adId) => {
        console.log(adId);
        
        axios.delete("http://localhost:8080/ads/" + adId)
            .then(response => {console.log("Deleted ad with ID " + adId)})
            .catch(error => {console.error(error)});
    }

    return (
        // Sidebar code came from https://dev.to/codeply/bootstrap-5-sidebar-examples-38pb
        <div class="container-fluid">
            <div class="row flex-nowrap">
                <div class="col-auto col-md-3 col-xl-2 px-sm-2 px-0 bg-dark">
                    <div class="d-flex flex-column align-items-center align-items-sm-start px-3 pt-2 text-white min-vh-100">
                        <a href="/" class="d-flex align-items-center pb-3 mb-md-0 me-md-auto text-white text-decoration-none">
                            <span class="fs-5 d-none d-sm-inline">Menu</span>
                        </a>
                        <ul class="nav nav-pills flex-column mb-sm-auto mb-0 align-items-center align-items-sm-start" id="menu">
                            <li class="nav-item">
                                <a href="#" class="nav-link align-middle px-0">
                                    <i class="fs-4 bi-house"></i> <span class="ms-1 d-none d-sm-inline">Home</span>
                                </a>
                            </li>
                            <li>
                                <a href="#submenu1" data-bs-toggle="collapse" class="nav-link px-0 align-middle">
                                    <i class="fs-4 bi-speedometer2"></i> <span class="ms-1 d-none d-sm-inline">Dashboard</span> </a>
                                <ul class="collapse show nav flex-column ms-1" id="submenu1" data-bs-parent="#menu">
                                    <li class="w-100">
                                        <a href="#" class="nav-link px-0"> <span class="d-none d-sm-inline">Item</span> 1 </a>
                                    </li>
                                    <li>
                                        <a href="#" class="nav-link px-0"> <span class="d-none d-sm-inline">Item</span> 2 </a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="#" class="nav-link px-0 align-middle">
                                    <i class="fs-4 bi-table"></i> <span class="ms-1 d-none d-sm-inline">Orders</span></a>
                            </li>
                            <li>
                                <a href="#submenu2" data-bs-toggle="collapse" class="nav-link px-0 align-middle ">
                                    <i class="fs-4 bi-bootstrap"></i> <span class="ms-1 d-none d-sm-inline">Bootstrap</span></a>
                                <ul class="collapse nav flex-column ms-1" id="submenu2" data-bs-parent="#menu">
                                    <li class="w-100">
                                        <a href="#" class="nav-link px-0"> <span class="d-none d-sm-inline">Item</span> 1</a>
                                    </li>
                                    <li>
                                        <a href="#" class="nav-link px-0"> <span class="d-none d-sm-inline">Item</span> 2</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="#submenu3" data-bs-toggle="collapse" class="nav-link px-0 align-middle">
                                    <i class="fs-4 bi-grid"></i> <span class="ms-1 d-none d-sm-inline">Products</span> </a>
                                    <ul class="collapse nav flex-column ms-1" id="submenu3" data-bs-parent="#menu">
                                    <li class="w-100">
                                        <a href="#" class="nav-link px-0"> <span class="d-none d-sm-inline">Product</span> 1</a>
                                    </li>
                                    <li>
                                        <a href="#" class="nav-link px-0"> <span class="d-none d-sm-inline">Product</span> 2</a>
                                    </li>
                                    <li>
                                        <a href="#" class="nav-link px-0"> <span class="d-none d-sm-inline">Product</span> 3</a>
                                    </li>
                                    <li>
                                        <a href="#" class="nav-link px-0"> <span class="d-none d-sm-inline">Product</span> 4</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="#" class="nav-link px-0 align-middle">
                                    <i class="fs-4 bi-people"></i> <span class="ms-1 d-none d-sm-inline">Customers</span> </a>
                            </li>
                        </ul>
                        <hr />
                        <div class="dropdown pb-4">
                            <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                                <img src="https://github.com/mdo.png" alt="hugenerd" width="30" height="30" class="rounded-circle" />
                                <span class="d-none d-sm-inline mx-1">loser</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-dark text-small shadow" aria-labelledby="dropdownUser1">
                                <li><a class="dropdown-item" href="#">New project...</a></li>
                                <li><a class="dropdown-item" href="#">Settings</a></li>
                                <li><a class="dropdown-item" href="#">Profile</a></li>
                                <li>
                                    <hr class="dropdown-divider" />
                                </li>
                                <li><a class="dropdown-item" href="#">Sign out</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
        {/* End of copied sidebar code */}
                <div class="col py-3">
                
                    {/*<Row xs={3}>*/}
                    {/*    {allAdsData.map((ad, id) => (*/}
                    {/*        <div class="col border border-5" key={id}>*/}
                    {/*            <p>Name: {ad.name}</p>*/}
                    {/*            <p>Price: {ad.price}</p>*/}
                    {/*            <p>Description: {ad.description}</p>*/}
                    {/*            <IconButton aria-label="delete" value={id} onClick={() => deleteAd(ad.id)}>*/}
                    {/*                <DeleteIcon />*/}
                    {/*            </IconButton>*/}
                    {/*        </div>*/}
                    {/*    ))}*/}
                    {/*</Row>*/}
                    {/*<CreateAdForm />*/}


                    <Row xs={3}>
                        {allGoalsData.map((goal, id) => (
                            <div class="col border border-5" key={id}>
                                <p>Name: {goal.name}</p>
                                <p>Description: {goal.description}</p>
                                <p>Completed: {goal.completed}</p>
                                {/*<IconButton aria-label="delete" value={id} onClick={() => deleteAd(ad.id)}>*/}
                                {/*    <DeleteIcon />*/}
                                {/*</IconButton>*/}
                            </div>
                        ))}
                    </Row>
                    <CreateGoalForm />
                </div>
            </div>
        </div>
    );
}