
import { useEffect, useRef, useState } from "react";
import getErrorModal from "../../util/getErrorModal";
import { useNavigate, useParams } from "react-router-dom";
import { useFetchResource } from '../../util/useFetchResource';
import FormGenerator from "../../components/formGenerator/formGenerator";
import userFormInputs from './adminComponents/userFormInputs';
import LinkClickButton from '../../components/LinkClickButton'

export default function UserEditAdmin() {
  const { userId } = useParams()
  const navigate = useNavigate()
  const formRef = useRef()
  const { data: auths, getData: getAuths } = useFetchResource()
  const { data: user, getData: getUser } = useFetchResource()
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);

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

  const handleSubmit = async ({ values, errors }) => {
    if (errors && Object.values(errors).some(Boolean)) {
      setMessage('Invalid data');
      setVisible(true);
      return;
    }
    const authority = auths.find((a) => a.id === parseInt(values.authority));
    const body = {
      ...user,
      ...values,
      authority: authority
    };
    const { status } = await getUser(
      userId !== 'new' ? `/api/v1/users/${userId}` : `/api/v1/users`,
      userId !== 'new' ? 'PUT' : 'POST',
      body
    );
    if (status === 'success') {
      navigate('/users');
    }
  };

  const inputs = userFormInputs(auths, user, userId !== 'new');

  return auths != null && (
    <div className="page-container">
      <div className="info-container">
      <h1 className="info-title">
        {user?.id ? "Edit User" : "Add User"}
      </h1>
      <div className="form-container">
        {getErrorModal(setVisible, visible, message)}
        <FormGenerator
          ref={formRef}
          inputs={inputs}
          onSubmit={handleSubmit}
          numberOfColumns={1}
          buttonText="SAVE"
          buttonClassName="button"
          childrenPosition={-1}
          listenEnterKey={false}
        >
          <LinkClickButton
            text='CANCEL'
            link='/users'
            className='danger'
          />
        </FormGenerator>
      </div>
    </div>
    </div>
  );
}
