import { Input, Label } from "reactstrap";

export default function FriendFormInput({ value, onChange }) {
  return (
    <div className="custom-form-input">
      <Label for="username" className="create-friendship-label">
        Friend's Username
      </Label>
      <Input
        type="text"
        required
        name="username"
        id="username"
        value={value}
        onChange={onChange}
        className="create-friendship-input"
      />
    </div>
  );
}
