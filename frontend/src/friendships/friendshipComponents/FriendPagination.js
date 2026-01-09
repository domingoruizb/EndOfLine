export default function FriendPagination({ currentPage, totalPages, setCurrentPage }) {
  return (
    <div className="friend-pagination text-center mt-4">
      <button
        onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
        disabled={currentPage === 1}
        className="friend-pagination-button"
      >
        Previous
      </button>
      <span className="friend-pagination-info">
        Page {currentPage} of {totalPages}
      </span>
      <button
        onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
        disabled={currentPage === totalPages}
        className="friend-pagination-button"
      >
        Next
      </button>
    </div>
  );
}
