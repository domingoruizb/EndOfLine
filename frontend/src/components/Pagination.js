import LinkClickButton from './LinkClickButton'

export default function Pagination ({
    page,
    setPage,
    pages,
    hasPrevious,
    hasNext
}) {
    return (
        <div className="pagination-container">
            <LinkClickButton
                text='Previous'
                onClick={() => setPage(prev => Math.max(prev - 1, 0))}
                disabled={!hasPrevious}
                className='orange md'
            />
            <span className="pagination-info">
                Page {page + 1} of {pages}
            </span>
            <LinkClickButton
                text='Next'
                onClick={() => setPage(prev => Math.min(prev + 1, pages - 1))}
                disabled={!hasNext}
                className='orange md'
            />
        </div>
        
    )
}
