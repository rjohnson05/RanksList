import React, { useEffect, useState } from 'react';
import { Row } from 'react-bootstrap';
import StarBorderOutlinedIcon from '@mui/icons-material/StarBorderOutlined';
import StarOutlinedIcon from '@mui/icons-material/StarOutlined';
import axios from 'axios';
import {Link} from "react-router-dom";
import NavBar from "./NavBar";
import {Button, Col} from "rsuite";
import './Home.css'

export default function Home() {
    const [allAdsData, setAds] = useState([]);
    const [starredStatus, setStarredStatus] = useState({});

    useEffect(() => {
        loadAds();
    }, []);

    const loadAds = async () => {
        const ads_data = await axios.get("http://localhost:8080/ads");
        setAds(ads_data.data);
        // Loading the star data for these ads
        for (let i = 0; i < ads_data.data.length; i++) {
            const adId = ads_data.data[i].id;
            const starStatus = await axios.get("http://localhost:8080/ad_starred/" + adId);
            setStarredStatus(prevState => ({...prevState, [adId]: starStatus.data}))
        }
    }

    const changeStarStatus = async (adId) => {
        const response = await axios.put("http://localhost:8080/starred_ads/" + adId);
        if (response.data != null) {
            loadAds();
        }
    }

    return (
        <div className="home">
            {/*Navbar code came from https://getbootstrap.com/docs/5.0/components/navbar/*/}
            <NavBar />

            <Row sm={4}>
                {allAdsData.map((ad) => (
                    <Col sm={4}>
                        <div className="card" key={ad.id}>
                            <Link className="link" to={"/individual_goals/" + ad.id}>
                                <img src='/blue.jpg' className="card-image" alt="blue"/>
                            </Link>
                            <Button className="star-button" value={ad.id} onClick={() => {changeStarStatus(ad.id)}}>
                                {starredStatus[ad.id] ? <StarOutlinedIcon className="star" /> : <StarBorderOutlinedIcon className="star" />}
                            </Button>
                            <Link className="link" to={"/individual_goals/" + ad.id}>
                                <div className="price-box">
                                    <p className="price-text">${ad.price}</p>
                                </div>
                                <div className="card-bottom">
                                    <h5 className="card-title">{ad.name}</h5>
                                </div>
                            </Link>
                        </div>
                    </Col>
                ))}
            </Row>
        </div>
    );
}