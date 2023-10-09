import React, { useEffect, useState } from 'react';
import { Row } from 'react-bootstrap';
import IconButton from 'rsuite/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import axios from 'axios';
import {useLocation} from "react-router-dom";
import NavBar from "../MainPage/NavBar";

export default function Home() {
    const [allGoalsData, setGoals] = useState([]);
    let ad_id = Number(useLocation().pathname.split('/').pop());

    useEffect(() => {
        loadGoals();
    });

    let loadGoals = async () => {
        const goals_data = await axios.get("http://localhost:8080/individual_goals/" + ad_id);
        setGoals(goals_data.data);
    }

    const deleteGoal = (goalId) => {
        axios.delete("http://localhost:8080/individual_goals/" + goalId)
            .then(response => {console.log("Deleted goal with ID " + goalId)})
            .catch(error => {console.error(error)});
    }

    return (
        <div>
            {/*Navbar code came from https://getbootstrap.com/docs/5.0/components/navbar/*/}
            <NavBar />

                <div class="col py-3">
                    <Row xs={3}>
                        {allGoalsData.map((goal, id) => (
                            <div class="col border border-5" key={id}>
                                <p>Name: {goal.name}</p>
                                <p>Description: {goal.description}</p>
                                <IconButton aria-label="delete" value={id} onClick={() => deleteGoal(goal.id)}>
                                    <DeleteIcon />
                                </IconButton>
                            </div>
                        ))}
                    </Row>
                </div>
            </div>
    );
}