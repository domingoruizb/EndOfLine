import React from "react";
import { Form } from "reactstrap";
import UserFormInput from "./UserFormInput";

export default function UserForm({ user, onChange, onSubmit, auths, isEdit }) {
  return (
    <Form onSubmit={onSubmit}>
      <UserFormInput
        label="Username"
        name="username"
        type="text"
        value={user.username || ""}
        onChange={onChange}
        required
      />
      <UserFormInput
        label="Name"
        name="name"
        type="text"
        value={user.name || ""}
        onChange={onChange}
        required
      />
      <UserFormInput
        label="Surname"
        name="surname"
        type="text"
        value={user.surname || ""}
        onChange={onChange}
        required
      />
      <UserFormInput
        label="Email"
        name="email"
        type="text"
        value={user.email || ""}
        onChange={onChange}
        required
      />
      <UserFormInput
        label="BirthDate"
        name="birthdate"
        type="date"
        value={user.birthdate || ""}
        onChange={onChange}
        required
      />
      <UserFormInput
        label="Authority"
        name="authority"
        type="select"
        value={user.authority?.id || ""}
        onChange={onChange}
        required
        options={auths.map(auth => ({ value: auth.id, label: auth.authority }))}
      />
      <div className="custom-button-row">
        <button className="user-add-button" type="submit">Save</button>
        <a href="/users" className="cancel-link">Cancel</a>
      </div>
    </Form>
  );
}
