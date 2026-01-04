import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import '../static/css/achievements/achievementPlayersModal.css';

const AchievementPlayersModal = ({ isOpen, toggle, players, achievementName }) => {
  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
      const date = new Date(dateString);
      if (isNaN(date.getTime())) return 'N/A';
      return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (e) {
      return 'N/A';
    }
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle} size="lg" className="achievement-modal">
      <ModalHeader toggle={toggle} className="achievement-modal-header">
        Players who achieved: <span className="achievement-name">{achievementName}</span>
      </ModalHeader>
      <ModalBody className="achievement-modal-body">
        {players && players.length > 0 ? (
          <div className="players-table-wrapper">
            <table className="players-table">
              <thead>
                <tr>
                  <th>Player</th>
                  <th>Achieved At</th>
                </tr>
              </thead>
              <tbody>
                {players.map((pa) => (
                  <tr key={pa.id} className="player-row">
                    <td className="player-name">{pa.userName}</td>
                    <td className="achieved-date">{formatDate(pa.achievedAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="no-players-message">
            <p>No players have achieved this yet.</p>
          </div>
        )}
      </ModalBody>
      <ModalFooter className="achievement-modal-footer">
        <Button color="secondary" onClick={toggle} className="close-button">
          Close
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default AchievementPlayersModal;
