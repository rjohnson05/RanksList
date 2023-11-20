import React, { useEffect, useState } from 'react';
import { Row } from 'react-bootstrap';
import IconButton from 'rsuite/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import axios from 'axios';
import {Link, useLocation} from "react-router-dom";
import NavBar from "../MainPage/NavBar";
import StarBorderOutlinedIcon from "@mui/icons-material/StarBorderOutlined";
import CreateIcon from "@mui/icons-material/Create";
import CreateGoalForm from "../CreateGoalPage/CreateGoalForm";
import {Button} from "rsuite";
import './IndividualGoals.css'

export default function Home() {
    const [allGoalsData, setGoals] = useState([]);
    const [allAdsData, setAds] = useState([]);
    let ad_id = Number(useLocation().pathname.split('/').pop());
    const ad = axios.get("http://localhost:8080/ads/" + ad_id);

    function createGoal(adId) {
        CreateGoalForm.adId = adId
    }

    useEffect(() => {
        loadGoalsAds();
    }, []);

    let loadGoalsAds = async () => {
        const goals_data = await axios.get("http://localhost:8080/individual_goals/" + ad_id);
        const ads_data = await axios.get("http://localhost:8080/ads/" + ad_id);
        setAds(ads_data.data);
        setGoals(goals_data.data);
    }

    const deleteGoal = async (goalId) => {
        const response = await axios.delete("http://localhost:8080/individual_goals/" + goalId);

        if (response.data) {
            loadGoalsAds();
        }
    }

    return (
        <div>
            {/*Navbar code came from https://getbootstrap.com/docs/5.0/components/navbar/*/}
            <NavBar />
            <div className="col py-3">
                <Row xs={1}>
                    {[allAdsData].map((ad, id) => (
                        <div className="col border border-5">
                            <p>Name: {ad.name} </p>
                            <p>Price: {ad.price}</p>
                            <p>Description: {ad.description}</p>
                        </div>
                    ))}
                </Row>
            </div>
            <div class="col py-3">
                <Row xs={3}>
                    {allGoalsData.map((goal, id) => (
                        <div class="col border border-5" key={id}>
                            <p>Description: {goal.description}</p>
                            <IconButton aria-label="delete" value={id} onClick={() => deleteGoal(goal.id)}>
                                <DeleteIcon />
                            </IconButton>
                        </div>
                    ))}
                </Row>

                <Link to={'create_goal/' + ad_id}>
                    <Button className="add-goal-button" value={ad_id} onClick={() => createGoal(ad_id)}>Add New Goal</Button>
                </Link>
            </div>
        </div>
    );
}