import {useEffect, useState} from 'react';
import NavBar from "../MainPage/NavBar";
import axios from "axios";
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import {Row} from "react-bootstrap";
import { Link } from 'react-router-dom';
import {Button, Col} from "rsuite";
import './MyCreatedAds.css';

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
            console.log("Deleted Ad");
            loadAds();
        }
    }

    return (
        <div className="created-ads">
            <NavBar />

            <Row sm={4}>
                {createdAdsData.map((ad) => (
                    <Col sm={4}>
                        <div className="card" key={ad.id}>
                            <img src='/blue.jpg' className="card-image" alt="blue"/>
                            <Button className="delete-button" aria-label="delete" value={ad.id} onClick={() => removeAd(ad.id)}>
                                <DeleteIcon className="delete-icon" />
                            </Button>
                            <Link to={"/edit_ad/" + ad.id}>
                                <Button className="edit-button" aria-label="fa-solid fa-pencil" value={ad.id}>
                                    <EditIcon className="edit-icon" />
                                </Button>
                            </Link>
                            <div className="price-box">
                                <p className="price-text">${ad.price}</p>
                            </div>
                            <div className="card-bottom">
                                <h5 className="card-title">{ad.name}</h5>
                            </div>
                        </div>
                    </Col>
                ))}
            </Row>
        </div>
    );
}