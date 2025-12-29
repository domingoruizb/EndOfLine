import { useState } from "react";
import useFetchState from "../util/useFetchState";
import "../static/css/developers/developersList.css";

export default function DeveloperList() {
    const [developers, setDevelopers] = useFetchState(
        [],
        '/api/v1/developers'
    );
    const [page, setPage] = useState(1);
    const itemsPerPage = 5;
    const totalPages = Math.ceil((developers?.length || 0) / itemsPerPage) || 1;
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    const imgnotfound = "https://cdn-icons-png.flaticon.com/512/48/48639.png";
    const visibleDevelopers = developers.slice(start, end);

    const resolvePicture = (d) => {
        return d?.properties?.picUrl || d?.picture || d?.picUrl || imgnotfound;
    };

    const developerList = visibleDevelopers.map((d) => {
        return (
            <tr key={d.id}>
                <td className="text-center">{d.name}</td>
                <td className="text-center"> {d.email} </td>
                <td className="text-center"> <a href={d.url}>{d.url}</a> </td>
                <td className="text-center">
                  <img src={resolvePicture(d)}
alt={d?.name || 'Developer'} width="50px" onError={(e) => { e.currentTarget.src = imgnotfound; }}/>
                </td>
            </tr>
        );
    });

    return (
        <div className="developers-page">
            <div className="developers-content-wrapper">
                <h1 className="developers-title">Developers</h1>
                <div>
                    <table aria-label="developers" className="developers-table">
                        <thead>
                            <tr>
                                <th className="text-center">Name</th>
                                <th className="text-center">e-mail</th>
                                <th className="text-center">URL</th>
                                <th className="text-center">Picture</th>
                            </tr>
                        </thead>
                        <tbody>{developerList}</tbody>
                    </table>
                    {totalPages > 1 && (
                        <div className="developers-pagination">
                            <button
                                className="developers-pagination-button"
                                onClick={() => setPage(prev => Math.max(prev - 1, 1))}
                                disabled={page === 1}
                            >
                                Previous
                            </button>
                            <span className="developers-pagination-info">
                                Page {page} of {totalPages}
                            </span>
                            <button
                                className="developers-pagination-button"
                                onClick={() => setPage(prev => Math.min(prev + 1, totalPages))}
                                disabled={page === totalPages}
                            >
                                Next
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}