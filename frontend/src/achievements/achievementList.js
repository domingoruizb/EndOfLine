import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import tokenService from '../services/token.service';
import useFetchState from '../util/useFetchState';
import AchievementPlayersModal from './achievementComponents/AchievementPlayersModal';
import AchievementDeleteModal from './achievementComponents/AchievementDeleteModal';
import AchievementCard from './achievementComponents/AchievementCard';
import AchievementTableRow from './achievementComponents/AchievementTableRow';
import '../static/css/achievements/achievementList.css';
import '../static/css/achievements/adminActionsBar.css';

const AchievementList = () => {
  const jwt = tokenService.getLocalAccessToken();
  const [isAdmin, setIsAdmin] = useState(false);
  const [viewMode, setViewMode] = useState('grid');
  const [modal, setModal] = useState(false);
  const [selectedAchievementId, setSelectedAchievementId] = useState(null);
  const [playersWithAchievement, setPlayersWithAchievement] = useState([]);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [deleteAchievementId, setDeleteAchievementId] = useState(null);

  const [achievements, setAchievements] = useFetchState([], `/api/v1/achievements`, jwt);
  const [playerAchievementIds, setPlayerAchievementIds] = useState([]);

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
      const admin = decoded.authorities && decoded.authorities.includes('ADMIN');
      setIsAdmin(admin);
      if (!admin) {
        fetch('/api/v1/playerachievements/ids', {
          headers: { Authorization: `Bearer ${jwt}` },
        })
          .then(res => res.json())
          .then(data => {
            if (Array.isArray(data)) {
              setPlayerAchievementIds(data);
            } else if (data && Array.isArray(data.ids)) {
              setPlayerAchievementIds(data.ids);
            } else {
              setPlayerAchievementIds([]);
            }
          })
          .catch(() => setPlayerAchievementIds([]));
      }
    }
  }, [jwt]);

  const isAchievementUnlocked = (achievementId) => {
    if (isAdmin) {
      return false;
    }
    return Array.isArray(playerAchievementIds) && playerAchievementIds.includes(achievementId);
  };

  const handleDeleteAchievement = (achievementId) => {
    setDeleteAchievementId(achievementId);
    setDeleteModalOpen(true);
  };

  const confirmDeleteAchievement = () => {
    if (!deleteAchievementId) return;
    fetch(`/api/v1/achievements/${deleteAchievementId}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${jwt}` },
    }).then(() => {
      setAchievements(achievements.filter((p) => p.id !== deleteAchievementId));
      setDeleteModalOpen(false);
      setDeleteAchievementId(null);
    });
  };

  const [playersPage, setPlayersPage] = useState(1);
  const [playersTotalPages, setPlayersTotalPages] = useState(1);
  const handleViewPlayers = (achievementId, page = 1) => {
    setSelectedAchievementId(achievementId);
    fetch(`/api/v1/playerachievements/achievement/${achievementId}?page=${page}`, {
      headers: { Authorization: `Bearer ${jwt}` },
    })
      .then((res) => res.json())
      .then((data) => {
        setPlayersWithAchievement(data.content || data);
        setPlayersPage(data.page || 1);
        setPlayersTotalPages(data.totalPages || 1);
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
          <div className="admin-actions-bar">
            <Link to="/achievements/new" className="create-button">Create Achievement</Link>
            <button
              onClick={() => setViewMode('grid')}
              className={`view-toggle-btn${viewMode === 'grid' ? ' active' : ''}`}
            >
              Grid View
            </button>
            <button
              onClick={() => setViewMode('table')}
              className={`view-toggle-btn${viewMode === 'table' ? ' active' : ''}`}
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
                  <AchievementCard
                    key={achievement.id}
                    achievement={achievement}
                    isUnlocked={isAchievementUnlocked(achievement.id)}
                    isAdmin={isAdmin}
                    onViewPlayers={handleViewPlayers}
                    onDelete={handleDeleteAchievement}
                  />
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
                      <AchievementTableRow
                        key={achievement.id}
                        achievement={achievement}
                        isUnlocked={isAchievementUnlocked(achievement.id)}
                        isAdmin={isAdmin}
                        onViewPlayers={handleViewPlayers}
                        onDelete={handleDeleteAchievement}
                      />
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
        page={playersPage}
        totalPages={playersTotalPages}
        onPageChange={(page) => handleViewPlayers(selectedAchievementId, page)}
      />
      <AchievementDeleteModal
        isOpen={deleteModalOpen}
        toggle={() => setDeleteModalOpen(false)}
        onConfirm={confirmDeleteAchievement}
        achievementName={achievements.find((a) => a.id === deleteAchievementId)?.name || 'Achievement'}
      />
    </div>
  );
};

export default AchievementList;