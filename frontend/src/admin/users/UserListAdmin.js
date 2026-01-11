import { useEffect, useState } from "react";
import ConfirmDeleteModal from "./adminComponents/ConfirmDeleteModal";
import { useFetchResource } from '../../util/useFetchResource';
import LinkClickButton from "../../components/LinkClickButton";
import Pagination from '../../components/Pagination';
import Table from '../../components/Table';
import "../../static/css/admin/userListAdmin.css";

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

  const rows = data?.content.map(user => [
    user.username,
    user.authority.type
  ])

  const actions = [
    (rowIndex) => ({
      text: 'EDIT',
      link: '/users/' + data.content[rowIndex].id,
      className: 'sm'
    }),
    (rowIndex) => ({
      text: 'DELETE',
      onClick: () => handleDeleteClick(data.content[rowIndex].id),
      className: 'sm danger'
    })
  ]

  return (
    <div className="page-container">
      <div className="info-container">
        <h1 className="info-title">Users</h1>
        <LinkClickButton
          text='Add User'
          link='/users/new'
        />
        {data?.content.length > 0 ? (
          <>
            <Table
              aria-label='users'
              columns={['Username', 'Authority']}
              rows={rows}
              actions={actions}
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
