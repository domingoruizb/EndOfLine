import LinkClickButton from './LinkClickButton'
import '../static/css/table.css'

export default function Table ({
    columns,
    rows,
    actions = [],
    ...props
}) {
    return (
        <table
            className='data-table'
            {...props}
        >
            <thead>
                <tr>
                    {
                        columns.map((column, index) => (
                            <th
                                key={index}
                            >
                                {column}
                            </th>
                        ))
                    }
                    {
                        actions.length > 0 && (
                            <th>
                                Actions
                            </th>
                        )
                    }
                </tr>
            </thead>
            <tbody>
                {
                    rows.map((row, rowIndex) => (
                        <tr
                            key={rowIndex}
                        >
                            {
                                row.map((cell, cellIndex) => (
                                    <td
                                        key={cellIndex}
                                    >
                                        {cell}
                                    </td>
                                ))
                            }
                            {
                                actions.length > 0 && (
                                    <td>
                                        <div
                                            className='data-table-actions'
                                        >
                                            {
                                                actions.map((action, actionIndex) => {
                                                    const actionProps = action(rowIndex)
                                                    return (
                                                        <LinkClickButton
                                                            key={actionIndex}
                                                            {...actionProps}
                                                        />
                                                    )
                                                })
                                            }
                                        </div>
                                    </td>
                                )
                            }
                        </tr>
                    ))
                }
            </tbody>
        </table>
    )
}
