import {useEffect, useState} from 'react';
import NavBar from "../MainPage/NavBar";
import axios from "axios";
import {Row} from "react-bootstrap";
import {Button, Col} from "rsuite";
import {Link} from "react-router-dom";
import StarOutlinedIcon from "@mui/icons-material/StarOutlined";
import StarBorderOutlinedIcon from "@mui/icons-material/StarBorderOutlined";
import './StarredAds.css';

export default function StarredAds() {
    const [starredAdsData, setStarredAds] = useState([]);
    const [starredStatus, setStarredStatus] = useState({});

    useEffect(() => {
        loadAds();
    }, []);

    const loadAds = async () => {
        const ads_data = await axios.get("http://localhost:8080/starred_ads");
        setStarredAds(ads_data.data);
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

    const removeAd = async (adId) => {
        const response = await axios.put("http://localhost:8080/starred_ads/" + adId);

        if (response.data != null) {
            loadAds();
        }
    }

    return (
        <div className="starred-ads">
            <NavBar />

            <Row sm={4}>
                {starredAdsData.map((ad) => (
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