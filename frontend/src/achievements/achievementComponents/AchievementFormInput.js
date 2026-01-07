import React from "react";
import { Input, Label } from "reactstrap";

export default function AchievementFormInput({ label, name, type, value, onChange, required, options, ...rest }) {
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
          <option value="">None</option>
          {options && options.map(opt => (
            <option key={opt} value={opt}>{opt}</option>
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
