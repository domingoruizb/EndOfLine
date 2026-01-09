import React from 'react';

export default function AvatarPreview({ avatar, error }) {
  if (!avatar || error) return null;
  return (
    <div style={{ textAlign: 'center', marginTop: '10px' }}>
      <img
        src={avatar}
        alt="avatar"
        style={{ width: '100px', height: '100px', borderRadius: '50%' }}
      />
    </div>
  );
}
