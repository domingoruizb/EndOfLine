import React from "react";
import { Link } from "react-router-dom";

export default function AchievementCard({ achievement, isUnlocked, isAdmin, onViewPlayers, onDelete }) {
  return (
    <div
      className={`achievement-card ${isUnlocked ? 'unlocked' : 'locked'}`}
    >
      <div className="achievement-badge">
        {achievement.badgeImage && (
          <img
            src={achievement.badgeImage}
            alt={achievement.name}
            onError={e => {
              e.target.src = 'https://cdn-icons-png.flaticon.com/512/5778/5778223.png';
            }}
          />
        )}
        <div className="achievement-status">
          {isUnlocked ? '✓' : '🔒'}
        </div>
      </div>
      <div className="achievement-name">{achievement.name}</div>
      <div className="achievement-description">{achievement.description}</div>
      <div className="achievement-meta">
        {achievement.category && (
          <span className="achievement-category">{achievement.category}</span>
        )}
        {achievement.threshold && (
          <span className="achievement-threshold">Threshold: {achievement.threshold}</span>
        )}
      </div>
      {isAdmin && (
        <div className="achievement-admin-actions">
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
            View Players
          </button>
          <button
            className="delete-button"
            onClick={() => onDelete(achievement.id)}
          >
            Delete
          </button>
        </div>
      )}
    </div>
  );
}
