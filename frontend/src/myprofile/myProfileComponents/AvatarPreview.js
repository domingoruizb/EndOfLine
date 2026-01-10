import React from 'react';

export default function AvatarPreview({ avatar, error }) {
  if (!avatar || error) return null;
  return (
    <div style={{ textAlign: 'center' }}>
      <img
        src={avatar}
        alt="avatar"
        style={{ width: '150px', height: '150px', borderRadius: '50%' }}
      />
    </div>
  );
}
