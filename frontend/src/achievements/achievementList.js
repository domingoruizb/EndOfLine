import {
  	Container,
} from "reactstrap";
import { Link } from "react-router-dom";

import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import deleteFromList from '../util/deleteFromList';
import { useState } from "react";
import getErrorModal from "./../util/getErrorModal"; 
import "./achievementList.css";

const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();

export default function AchievementList () {
	const [message, setMessage] = useState(null);
	const [visible, setVisible] = useState(false);
	const [alerts, setAlerts] = useState([]);

	const [achievements, setAchievements] = useFetchState(
		[],
		`/api/v1/achievements`,
		jwt
	);

  	const achievementList = achievements.map((a) => {
		return (
			<tr key={a.id}>
				<td className="text-center">{a.name}</td>
				<td className="text-center"> {a.description} </td>
				<td className="text-center">
					<img src={a.badgeImage ? a.badgeImage : imgnotfound} alt={a.name}
					width="50px" />
				</td>
				<td className="text-center"> {a.threshold} </td>
				<td className="text-center"> {a.category} </td>
				<td className="text-center">
                    <Link
                        to={`/achievements/` + a.id}
                        className="edit-button"
                        style={{ textDecoration: "none" }}
                    >
                        Edit
                    </Link>
				</td>
				<td className="text-center">
					<button
                        className="delete-button"
						onClick={() =>
							deleteFromList(
								`/api/v1/achievements/${a.id}`,
								a.id,
								[achievements, setAchievements],
								[alerts, setAlerts],
								setMessage,
								setVisible
							)}>
						Delete
					</button>
				</td>
			</tr>
		);
	});

	const modal = getErrorModal(setVisible, visible, message); 

    return (
        <div
            style={{
                backgroundColor: "black",
                color: "white",
                minHeight: "100vh",
                padding: "2rem 0",
                fontFamily: "Inter, Arial, sans-serif",
            }}
        >
            <Container
                className="auth-page-container"
                style={{
                    padding: "0 1rem",
                    maxWidth: 900,
                    background: "none",
                    borderRadius: "1rem",
                    boxShadow: "none",
                }}
            >
                <h1 className="text-center" style={{
                    fontWeight: 800,
                    letterSpacing: "2px",
                    color: "#FE5B02",
                    textShadow: "0 2px 8px #000"
                }}>
                    Achievements
                </h1>
				<div style ={{
					display: 'flex',
    				flexDirection: 'column',     // Para apilar tabla y botón verticalmente
    				alignItems: 'center',        // Centra horizontalmente
    				gap: '20px', }}>
					<table aria-label="achievements" className="mt-4">
						<thead>
						<tr>
							<th className="text-center">Name</th>
							<th className="text-center">Description</th>
							<th className="text-center">Image</th>
							<th className="text-center">Threshold</th>
							<th className="text-center">Category</th>
							<th className="text-center">Actions</th>
						</tr>
						</thead>
						<tbody>{achievementList}</tbody>
					</table>
                    <Link
                        to={`/achievements/new`}
                        className="create-button"
                        style={{ textDecoration: "none" }}
                    >
                        Create achievement
                    </Link>
				</div>
			</Container>
		</div>
	);
} 