import {
    Table
  } from "reactstrap";
    const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
    const achievements = [
        {id:1, name:"Experiencia básica", description:"Si juegas 10 partidas o más",
badgeImage:"https://cdn-icons-png.flaticon.com/512/5243/5243423.png", threshold:"10", metric:"GAMES_PLAYED"},
        {id:2, name:"Explorador", description:"Si juegas 25 partidas o más",
badgeImage:"https://cdn-icons-png.flaticon.com/512/603/603855.png", threshold:"25", metric:"GAMES_PLAYED"},
        {id:3, name:"Experto", description:"Si ganas 20 partidas o más", badgeImage:"https://cdn-icons-png.flaticon.com/512/4737/4737471.png", threshold:"20", metric:"VICTORIES"}
];
    export default function AchievementList() {
    const achievementList =
    achievements.map((a) => {
        return (
            <tr key={a.id}>
                <td className="text-center">{a.name}</td>
                <td className="text-center"> {a.description} </td>
                <td className="text-center">
                <img src={a.badgeImage? a.badgeImage : imgnotfound } alt={a.name}
width="50px"/>
                </td>
                <td className="text-center"> {a.threshold} </td>
                <td className="text-center"> {a.metric} </td>
                <td className="text-center"> --- </td>
                </tr>
        );
    });
    return (
        <div>
        <div className="admin-page-container">
                <h1 className="text-center">Achievements</h1>
                <div>
                    <Table aria-label="achievements" className="mt-4">
                        <thead>
                        <tr>
                            <th className="text-center">Name</th>
                            <th className="text-center">Description</th>
                            <th className="text-center">Image</th>
                            <th className="text-center">Threshold</th>
                            <th className="text-center">Metric</th>
                            <th className="text-center">Actions</th>
                        </tr>
                    </thead>
                    <tbody>{achievementList}</tbody>
                    </Table>
                </div>
        </div>
        </div>
    );
}