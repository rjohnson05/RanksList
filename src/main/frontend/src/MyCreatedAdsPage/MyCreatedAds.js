import {React, useEffect, useState} from 'react';
import NavBar from "../MainPage/NavBar";
import axios from "axios";
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import {Row} from "react-bootstrap";
import { Link } from 'react-router-dom';

export default function CreatedAds() {
    const [createdAdsData, setCreatedAdsData] = useState([]);

    useEffect(() => {
        loadAds()
    }, []);

    const loadAds = async () => {
        const ads_data = await axios.get("http://localhost:8080/my_ads");
        setCreatedAdsData(ads_data.data);
    }

    const removeAd = async (adId) => {
        const response = await axios.delete("http://localhost:8080/my_ads/" + adId);

        if (response.data) {
            loadAds();
        }
    }

    return (
        <div>
            <NavBar />
            <Row xs={3}>
                {createdAdsData.map((ad, id) => (
                    <div className="col border border-5" key={id}>
                        <p>Name: {ad.name}</p>
                        <p>Price: {ad.price}</p>
                        <p>Description: {ad.description}</p>
                        <IconButton aria-label="delete" value={id} onClick={() => removeAd(ad.id)}>
                            <DeleteIcon />
                        </IconButton>
                        <Link to={"/edit_ad/" + ad.id}>
                            <IconButton aria-label="fa-solid fa-pencil" value={id}>
                                <EditIcon />
                            </IconButton>
                        </Link>
                    </div>
                ))}
            </Row>
        </div>
    );
}