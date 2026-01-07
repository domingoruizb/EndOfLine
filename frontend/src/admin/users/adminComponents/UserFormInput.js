import React from "react";
import { Input, Label } from "reactstrap";

export default function UserFormInput({ label, name, type, value, onChange, required, options, ...rest }) {
  return (
    <div className="custom-form-input">
      <Label for={name} className="custom-form-input-label">
        {label}
      </Label>
      {type === "select" ? (
        <Input
          type="select"
          name={name}
          id={name}
          value={value || ""}
          onChange={onChange}
          required={required}
          className="custom-input black-select"
          {...rest}
        >
          {options && options.map(opt => (
            <option key={opt.value} value={opt.value}>{opt.label}</option>
          ))}
        </Input>
      ) : (
        <Input
          type={type}
          name={name}
          id={name}
          value={value || ""}
          onChange={onChange}
          required={required}
          className="custom-input"
          {...rest}
        />
      )}
    </div>
  );
}
