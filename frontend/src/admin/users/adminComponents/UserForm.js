
import React, { useRef } from "react";
import FormGenerator from "../../../components/formGenerator/formGenerator";
import userFormInputs from "./userFormInputs";


export default function UserForm({ user, onChange, onSubmit, auths, isEdit }) {
  const formRef = useRef();

  const handleSubmit = ({ values }) => {
    let newValues = { ...values };
    if (!newValues.birthdate) newValues.birthdate = "";
    if (!newValues.name) newValues.name = "";

    // TODO: Figure out the purpose
    if (onChange) {
      Object.entries(newValues).forEach(([name, value]) => {
        onChange({ target: { name, value } });
      });
    }
    if (onSubmit) {
      onSubmit({
        preventDefault: () => {},
        target: { elements: newValues },
        values: newValues,
      });
    }
  };

  return (
    <div>
      <FormGenerator
        ref={formRef}
        inputs={userFormInputs(auths, user, isEdit)}
        onSubmit={handleSubmit}
        numberOfColumns={1}
        buttonText="Save"
        buttonClassName="user-add-button"
        childrenPosition={-1}
      >
        <a href="/users" className="cancel-link" style={{ marginLeft: '1rem', marginBottom: '100px' }}>Cancel</a>
      </FormGenerator>
    </div>
  );
}
