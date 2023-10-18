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
    const [isSelected, setSelected] = useState(false);


    useEffect(() => {
        loadAds();
    }, []);

    const loadAds = async () => {
        const ads_data = await axios.get("http://localhost:8080/ads");
        setAds(ads_data.data);
    }

    const starAd = (adId, selected) => {
        if (!isSelected) {
            axios.put("http://localhost:8080/starred_ads/" + adId)
                .then(response => {console.log("Starred Ad #" + adId)})
                .catch(error => {console.log(error.response.data)})
        } else {
            axios.delete("http://localhost:8080/starred_ads/" + adId)
                .then(response => {console.log("Unstarred Ad #" + adId)})
                .catch(error => {console.log(error.response.data)})
        }
        setSelected(!isSelected);
    }

    return (
        <div>
            {/*Navbar code came from https://getbootstrap.com/docs/5.0/components/navbar/*/}
            <NavBar />
            <div className="col py-3">
                <Row xs={3}>
                    {allAdsData.map((ad, id) => (
                        <Link to={"/individual_goals/" + ad.id}>
                        <div className="col border border-5" key={id}>
                            <p>Name: {ad.name}</p>
                            <p>Price: {ad.price}</p>
                            <p>Description: {ad.description}</p>
                            <IconButton value={id} onClick={() => {starAd(ad.id)}}>
                                {isSelected ? <StarOutlinedIcon /> : <StarBorderOutlinedIcon />}
                            </IconButton>
                        </div>
                        </Link>
                    ))}
                </Row>
            </div>
        </div>
    );
}