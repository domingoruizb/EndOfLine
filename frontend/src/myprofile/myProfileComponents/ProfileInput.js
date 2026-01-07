import React from 'react';
import { Input, Label } from 'reactstrap';

export default function ProfileInput({
  id,
  label,
  value,
  onChange,
  error,
  type = 'text',
  required = true,
  ...props
}) {
  return (
    <div className="custom-form-input">
      <Label for={id} className="custom-form-input-label">
        {label}
      </Label>
      <Input
        type={type}
        required={required}
        name={id}
        id={id}
        value={value || ''}
        onChange={onChange}
        className="custom-input"
        {...props}
      />
      {error && <div className="input-error-message">{error}</div>}
    </div>
  );
}
