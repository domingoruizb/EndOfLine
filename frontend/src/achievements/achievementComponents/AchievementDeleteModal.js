import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import '../../static/css/achievements/achievementPlayersModal.css';

const AchievementDeleteModal = ({ isOpen, toggle, onConfirm, achievementName }) => {
  return (
    <Modal isOpen={isOpen} toggle={toggle} size="md" className="achievement-modal">
      <ModalHeader toggle={toggle} className="achievement-modal-header">
        Confirm Deletion
      </ModalHeader>
      <ModalBody className="achievement-modal-body">
        <div className="delete-confirm-message">
          <p>Are you sure you want to delete the achievement <span className="achievement-name">{achievementName}</span>?</p>
        </div>
      </ModalBody>
      <ModalFooter className="achievement-modal-footer">
        <Button color="danger" onClick={onConfirm} className="close-button">
          Delete
        </Button>
        <Button color="secondary" onClick={toggle} className="close-button">
          Cancel
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default AchievementDeleteModal;
