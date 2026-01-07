import React from "react";
import { Link } from "react-router-dom";

export default function AchievementTableRow({ achievement, isUnlocked, isAdmin, onViewPlayers, onDelete }) {
  return (
    <tr>
      <td>
        {achievement.badgeImage && (
          <img
            src={achievement.badgeImage}
            alt={achievement.name}
            onError={e => {
              e.target.src = 'https://cdn-icons-png.flaticon.com/512/5778/5778223.png';
            }}
          />
        )}
      </td>
      <td>{achievement.name}</td>
      <td>{achievement.description}</td>
      <td>{achievement.category || 'N/A'}</td>
      <td>{achievement.threshold || 'N/A'}</td>
      <td
        style={{
          color: isUnlocked ? '#4CAF50' : '#999',
          fontWeight: 'bold',
        }}
      >
        {isUnlocked ? '✓ Unlocked' : '🔒 Locked'}
      </td>
      {isAdmin && (
        <td>
          <div className="admin-buttons" style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
            <Link to={`/achievement-edit/${achievement.id}`}>
              <button className="edit-button">Edit</button>
            </Link>
            <button
              className="view-button"
              onClick={() => onViewPlayers(achievement.id)}
              style={{
                padding: '0.5rem 1rem',
                background: '#b1d12d',
                color: '#000',
                border: 'none',
                borderRadius: '5px',
                cursor: 'pointer',
                fontSize: '0.9rem',
              }}
            >
              Players
            </button>
            <button
              className="delete-button"
              onClick={() => onDelete(achievement.id)}
            >
              Delete
            </button>
          </div>
        </td>
      )}
    </tr>
  );
}
