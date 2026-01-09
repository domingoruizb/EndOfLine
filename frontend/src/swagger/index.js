import { useEffect } from "react"
import SwaggerUI from "swagger-ui-react"
import { useFetchResource } from '../util/useFetchResource'
import "swagger-ui-react/swagger-ui.css"

export default function SwaggerDocs(){
    const { data, getData } = useFetchResource()

    useEffect(() => {
        const fetchDocs = async () => {
            await getData(
                `/v3/api-docs`
            )
        }

        fetchDocs()
    }, [])

    return data != null ? (
        <SwaggerUI
            spec={data}
            url=""
        />
    ) : (
        <div>
            Loading...
        </div>
    )
}