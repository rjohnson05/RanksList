import React, { useEffect, useState } from 'react';
import { Row } from 'react-bootstrap';
import IconButton from '@mui/material/IconButton';
import StarBorderOutlinedIcon from '@mui/icons-material/StarBorderOutlined';
import StarOutlinedIcon from '@mui/icons-material/StarOutlined';
import axios from 'axios';
import {Link} from "react-router-dom";
import NavBar from "./NavBar";

export default function Home() {
    const [allAdsData, setAds] = useState([]);

    useEffect(() => {
        loadAds();
    }, []);

    const loadAds = async () => {
        const ads_data = await axios.get("http://localhost:8080/ads");
        setAds(ads_data.data);
    }

    const loadStarredStatus = async (adId) => {
        console.log("Running loadStarredStatus()");
        const starred_status = await axios.get("http://localhost:8080/ad_starred/" + adId)
        if (starred_status.data) {
            console.log("Starred Status: " + starred_status.data);
            return starred_status.data
        }
    }

    const starAd = async (adId) => {
        const response = await axios.put("http://localhost:8080/starred_ads/" + adId);
        if (response.data) {
            console.log("Starred: " + response.data);
            loadAds();
            loadStarredStatus(adId);
        }
    }

    return (
        <div>
            {/*Navbar code came from https://getbootstrap.com/docs/5.0/components/navbar/*/}
            <NavBar />
            <div className="col py-3">
                <Row xs={3}>
                    {allAdsData.map((ad, index) => (
                            <div className="col border border-5" key={ad.id}>
                        <Link to={"/individual_goals/" + ad.id}>
                            <p>Name: {ad.name}</p>
                            <p>Price: {ad.price}</p>
                            <p>Description: {ad.description}</p>
                        </Link>
                            <IconButton value={ad.id} onClick={() => {starAd(ad.id)}}>
                                {!loadStarredStatus(ad.id) ? <StarOutlinedIcon /> : <StarBorderOutlinedIcon />}
                            </IconButton>
                        </div>
                    ))}
                </Row>
            </div>
        </div>
    );
}