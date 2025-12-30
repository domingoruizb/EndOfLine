import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import tokenService from '../services/token.service';
import useFetchState from '../util/useFetchState';
import AchievementPlayersModal from './AchievementPlayersModal';
import '../static/css/achievementList.css';

const AchievementList = () => {
  const jwt = tokenService.getLocalAccessToken();
  const [isAdmin, setIsAdmin] = useState(false);
  const [viewMode, setViewMode] = useState('grid');
  const [modal, setModal] = useState(false);
  const [selectedAchievementId, setSelectedAchievementId] = useState(null);
  const [playersWithAchievement, setPlayersWithAchievement] = useState([]);

  const [achievements, setAchievements] = useFetchState([], `/api/v1/achievements`, jwt);
  const [playerAchievements] = useFetchState(
    [],
    `/api/v1/playerachievements`,
    jwt
  );

  useEffect(() => {
    if (jwt) {
      const base64Url = jwt.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      const decoded = JSON.parse(jsonPayload);
      setIsAdmin(decoded.authorities && decoded.authorities.includes('ADMIN'));
    }
  }, [jwt]);

  const isAchievementUnlocked = (achievementId) => {
    return playerAchievements.some((pa) => pa.achievementId === achievementId);
  };

  const handleDeleteAchievement = (achievementId) => {
    if (window.confirm('Are you sure you want to delete this achievement?')) {
      fetch(`/api/v1/achievements/${achievementId}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${jwt}` },
      }).then(() => {
        setAchievements(achievements.filter((p) => p.id !== achievementId));
      });
    }
  };

  const handleViewPlayers = (achievementId) => {
    setSelectedAchievementId(achievementId);
    fetch(`/api/v1/playerachievements/achievement/${achievementId}`, {
      headers: { Authorization: `Bearer ${jwt}` },
    })
      .then((res) => res.json())
      .then((data) => {
        setPlayersWithAchievement(data);
        setModal(true);
      })
      .catch((err) => console.error('Error fetching players:', err));
  };

  const toggleModal = () => {
    setModal(!modal);
  };

  return (
    <div className="achievements-page">
      <div className="achievements-container">
        <h1>ACHIEVEMENTS</h1>

        {isAdmin && (
          <div style={{ textAlign: 'center', marginBottom: '1.5rem' }}>
            <Link to="/achievements/new" style={{ marginRight: '1rem' }}>
              <button className="create-button">Create Achievement</button>
            </Link>
            <button
              onClick={() => setViewMode('grid')}
              style={{
                margin: '0 0.5rem',
                padding: '0.5rem 1rem',
                background: viewMode === 'grid' ? '#fe5b02' : 'transparent',
                border: '2px solid #fe5b02',
                color: viewMode === 'grid' ? 'black' : '#fe5b02',
                borderRadius: '5px',
                cursor: 'pointer',
                fontWeight: 'bold',
                transition: 'all 0.2s ease',
              }}
            >
              Grid View
            </button>
            <button
              onClick={() => setViewMode('table')}
              style={{
                margin: '0 0.5rem',
                padding: '0.5rem 1rem',
                background: viewMode === 'table' ? '#fe5b02' : 'transparent',
                border: '2px solid #fe5b02',
                color: viewMode === 'table' ? 'black' : '#fe5b02',
                borderRadius: '5px',
                cursor: 'pointer',
                fontWeight: 'bold',
                transition: 'all 0.2s ease',
              }}
            >
              Table View
            </button>
          </div>
        )}

        {achievements && achievements.length > 0 ? (
          <>
            {viewMode === 'grid' ? (
              <div className="achievements-grid">
                {achievements.map((achievement) => (
                  <div
                    key={achievement.id}
                    className={`achievement-card ${
                      isAchievementUnlocked(achievement.id) ? 'unlocked' : 'locked'
                    }`}
                  >
                    <div className="achievement-badge">
                      {achievement.badgeImage && (
                        <img
                          src={achievement.badgeImage}
                          alt={achievement.name}
                          onError={(e) => {
                            e.target.src =
                              'https://cdn-icons-png.flaticon.com/512/5778/5778223.png';
                          }}
                        />
                      )}
                      <div className="achievement-status">
                        {isAchievementUnlocked(achievement.id) ? '✓' : '🔒'}
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
                      <div
                        style={{
                          marginTop: '1rem',
                          paddingTop: '1rem',
                          borderTop: '1px solid rgba(254, 91, 2, 0.1)',
                          display: 'flex',
                          gap: '0.5rem',
                          justifyContent: 'center',
                          flexWrap: 'wrap',
                        }}
                      >
                        <Link to={`/achievements/${achievement.id}`}>
                          <button className="edit-button">Edit</button>
                        </Link>
                        <button
                          className="view-button"
                          onClick={() => handleViewPlayers(achievement.id)}
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
                          onClick={() => handleDeleteAchievement(achievement.id)}
                        >
                          Delete
                        </button>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            ) : (
              <table className="achievements-table">
                <thead>
                  <tr>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Category</th>
                    <th>Threshold</th>
                    <th>Status</th>
                    {isAdmin && <th>Actions</th>}
                  </tr>
                </thead>
                <tbody>
                  {achievements.map((achievement) => (
                    <tr key={achievement.id}>
                      <td>
                        {achievement.badgeImage && (
                          <img
                            src={achievement.badgeImage}
                            alt={achievement.name}
                            onError={(e) => {
                              e.target.src =
                                'https://cdn-icons-png.flaticon.com/512/5778/5778223.png';
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
                          color: isAchievementUnlocked(achievement.id) ? '#4CAF50' : '#999',
                          fontWeight: 'bold',
                        }}
                      >
                        {isAchievementUnlocked(achievement.id) ? '✓ Unlocked' : '🔒 Locked'}
                      </td>
                      {isAdmin && (
                        <td>
                          <div className="admin-buttons" style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                            <Link to={`/achievements/${achievement.id}`}>
                              <button className="edit-button">Edit</button>
                            </Link>
                            <button
                              className="view-button"
                              onClick={() => handleViewPlayers(achievement.id)}
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
                              onClick={() => handleDeleteAchievement(achievement.id)}
                            >
                              Delete
                            </button>
                          </div>
                        </td>
                      )}
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </>
        ) : (
          <div className="no-achievements">No achievements found.</div>
        )}
      </div>

      <AchievementPlayersModal
        isOpen={modal}
        toggle={toggleModal}
        players={playersWithAchievement}
        achievementName={achievements.find((a) => a.id === selectedAchievementId)?.name || 'Achievement'}
      />
    </div>
  );
};

export default AchievementList; 