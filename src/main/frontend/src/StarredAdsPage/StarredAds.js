import {React, useEffect, useState} from 'react';
import NavBar from "../MainPage/NavBar";
import axios from "axios";
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import {Row} from "react-bootstrap";

export default function StarredAds() {
    const [starredAdsData, setStarredAds] = useState([]);

    useEffect(() => {
        loadAds();
    }, []);

    const loadAds = async () => {
        const ads_data = await axios.get("http://localhost:8080/starred_ads");
        setStarredAds(ads_data.data);
    }

    const removeAd = async (adId) => {
        const response = await axios.put("http://localhost:8080/starred_ads/" + adId);

        if (response.data != null) {
            loadAds();
        }
    }

    return (
        <div>
            <NavBar />
            <Row xs={3}>
                {starredAdsData.map((ad) => (
                    <div className="col border border-5" key={ad.id}>
                        <p>Name: {ad.name}</p>
                        <p>Price: {ad.price}</p>
                        <p>Description: {ad.description}</p>
                        <IconButton aria-label="delete" value={ad.id} onClick={() => removeAd(ad.id)}>
                            <DeleteIcon />
                        </IconButton>
                    </div>
                ))}
            </Row>
        </div>
    );
}