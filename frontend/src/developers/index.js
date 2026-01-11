import { useEffect, useState } from "react";
import Pagination from '../components/Pagination';
import Table from '../components/Table';
import { useFetchResource } from '../util/useFetchResource';

export default function DeveloperList() {
    const [page, setPage] = useState(0);
    const { data, getData } = useFetchResource()
    const itemsPerPage = 5;
    const imgnotfound = "https://cdn-icons-png.flaticon.com/512/48/48639.png";

    const resolvePicture = (d) => {
        return d?.properties?.picUrl || d?.picture || d?.picUrl || imgnotfound;
    };

    useEffect(() => {
        const fetchDevelopers = async () => {
            await getData(
                `/api/v1/developers?page=${page}&size=${itemsPerPage}`
            )
        }

        fetchDevelopers()
    }, [page])

    const rows = data?.content.map(d => [
        d.name,
        d.email,
        <a href={d.url}>{d.url}</a>,
        <img
            src={resolvePicture(d)}
            alt={d?.name || 'Developer'}
            onError={(e) => { e.currentTarget.src = imgnotfound; }}
        />
    ])

    return (
        <div className="page-container">
            <div className="info-container">
                <h1 className="info-title">Developers</h1>
                {data?.content.length > 0 ? (
                    <>
                        <Table
                            aria-label='developers'
                            columns={['Name', 'E-Mail', 'URL', 'Picture']}
                            rows={rows}
                        />
                        {data?.pages > 1 && (
                            <Pagination
                                page={page}
                                setPage={setPage}
                                {...data}
                            />
                        )}
                    </>
                ) : (
                    <p>No developers found.</p>
                )}
            </div>
        </div>
    );
}