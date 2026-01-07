
import React, { useRef } from "react";
import FormGenerator from "../../../components/formGenerator/formGenerator";
import userFormInputs from "./userFormInputs";

export default function UserForm({ user, onChange, onSubmit, auths, isEdit }) {
  const formRef = useRef();

  const handleSubmit = ({ values }) => {
    if (onChange) {
      Object.entries(values).forEach(([name, value]) => {
        onChange({ target: { name, value } });
      });
    }
    if (onSubmit) {
      onSubmit({
        preventDefault: () => {},
        target: { elements: values },
        values,
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
      >
        <a href="/users" className="cancel-link" style={{ marginLeft: '1rem' }}>Cancel</a>
      </FormGenerator>
    </div>
  );
}
