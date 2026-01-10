import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Button } from "reactstrap";
import ConfirmDeleteModal from "./adminComponents/ConfirmDeleteModal";
import UserList from './adminComponents/UserList';
import { useFetchResource } from '../../util/useFetchResource';
import "../../static/css/admin/userListAdmin.css";
import "../../static/css/admin/userListAdmin.css";
import LinkClickButton from "../../components/LinkClickButton";

export default function UserListAdmin() {
  const [confirmUserId, setConfirmUserId] = useState(null);
  const [page, setPage] = useState(0);
  const { data, getData } = useFetchResource()
  const itemsPerPage = 10;

  useEffect(() => {
    const fetchUsers = async () => {
      await getData(
        `/api/v1/users?page=${page}&size=${itemsPerPage}`
      )
    }

    fetchUsers()
  }, [confirmUserId, page])

  const handleDeleteClick = (userId) => {
    setConfirmUserId(userId);
  };

  const confirmDelete = async () => {
    await getData(
      `/api/v1/users/${confirmUserId}`,
      'DELETE'
    )

    setConfirmUserId(null);
  };

  const cancelDelete = () => {
    setConfirmUserId(null);
  };

  return (
    <div className="page-container">
      <div className="info-container">
        <h1 className="info-title">Users</h1>
        <div className="text-center user-list-actions">
          <LinkClickButton
            text='Add User'
            link='/users/new'
          />
        </div>
        {data?.content.length > 0 ? (
          <>
            <table aria-label="users" className="mt-4 text-white user-table">
              <thead>
                <tr>
                  <th className="text-center">Username</th>
                  <th className="text-center">Authority</th>
                  <th className="text-center">Actions</th>
                </tr>
              </thead>
              <tbody>
                <UserList
                  users={data?.content}
                  handleDeleteClick={handleDeleteClick}
                />
              </tbody>
            </table>
            {data?.pages > 1 && (
              <div className="text-center mt-4 pagination-container">
                <button
                  onClick={() => setPage(prev => Math.max(prev - 1, 0))}
                  disabled={!data?.hasPrevious}
                  className="pagination-button"
                >
                  Previous
                </button>
                <span className="pagination-info">
                  Page {page + 1} of {data?.pages}
                </span>
                <button
                  onClick={() => setPage(prev => Math.min(prev + 1, data?.pages - 1))}
                  disabled={!data?.hasNext}
                  className="pagination-button"
                >
                  Next
                </button>
              </div>
            )}
          </>
        ) : (
          <p className="text-center text-white mt-4">No users found.</p>
        )}
        <ConfirmDeleteModal
          isOpen={confirmUserId !== null}
          toggle={cancelDelete}
          onConfirm={confirmDelete}
        />
      </div>
    </div>
  );
}
