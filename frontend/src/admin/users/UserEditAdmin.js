import { useEffect, useRef } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useFetchResource } from '../../util/useFetchResource';
import FormGenerator from "../../components/formGenerator/formGenerator";
import userFormInputs from './adminComponents/userFormInputs';
import "../../static/css/admin/adminPage.css";
import "../../static/css/admin/userEditAdmin.css";
import "../../static/css/admin/userListAdmin.css";

export default function UserEditAdmin() {
  const { userId } = useParams()
  const navigate = useNavigate()
  const formRef = useRef()
  const { data: auths, getData: getAuths } = useFetchResource()
  const { data: user, getData: getUser } = useFetchResource()

  useEffect(() => {
    const fetchUser = async () => {
      if (auths == null) {
        await getAuths(
          `/api/v1/users/authorities`
        )
      }

      if (userId !== 'new') {
        await getUser(
          `/api/v1/users/${userId}`
        )
      }
    }

    fetchUser()
  }, [userId])

  async function handleSubmit ({ values }) {
    const authority = auths.find((a) => a.id === parseInt(values.authority));
    const body = {
      ...user,
      ...values,
      authority: authority
    }

    const { success } = await getUser(
      userId !== 'new' ? `/api/v1/users/${userId}` : `/api/v1/users`,
      userId !== 'new' ? 'PUT' : 'POST',
      body
    )

    if (success) {
      navigate('/users')
    }
  }

  const inputs = userFormInputs(auths, user, userId !== 'new');

  return auths != null && (
    <div className="user-list-page">
      <div className="admin-page-container user-list-container">
      <h1 className="text-center user-list-title">
        {user?.id ? "Edit User" : "Add User"}
      </h1>
      <div className="auth-form-container">
        <FormGenerator
          ref={formRef}
          inputs={inputs}
          onSubmit={handleSubmit}
          numberOfColumns={1}
          buttonText="Save"
          buttonClassName="user-add-button"
          childrenPosition={-1}
          listenEnterKey={false}
        >
          <a href="/users" className="cancel-link" style={{ marginLeft: '1rem', marginBottom: '100px' }}>Cancel</a>
        </FormGenerator>
      </div>
    </div>
    </div>
  );
}
