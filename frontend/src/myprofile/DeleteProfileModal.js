import React from 'react';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

function DeleteProfileModal({ isOpen, toggle, onConfirm }) {
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      centered={true}
      dark
    >
      <ModalHeader
        toggle={toggle}
        style={{
          backgroundColor: '#1e1e1e',
          color: '#FE5B02',
          borderBottom: '1px solid #333'
        }}
      >
        Confirm
      </ModalHeader>
      <ModalBody
        style={{
          backgroundColor: '#2a2a2a',
          color: 'white'
        }}
      >
        If you delete your profile, your account will be removed permanently.
        <br /><br />
        Are you sure you want to continue?
      </ModalBody>
      <ModalFooter
        style={{
          backgroundColor: '#1e1e1e',
          borderTop: '1px solid #333'
        }}
      >
        <Button
          color="secondary"
          onClick={toggle}
          style={{ backgroundColor: '#555', border: 'none' }}
        >
          Cancel
        </Button>{' '}
        <Button
          color="danger"
          onClick={onConfirm}
          style={{ backgroundColor: '#FE5B02', border: 'none', fontWeight: 'bold' }}
        >
          Yes, delete profile
        </Button>
      </ModalFooter>
    </Modal>
  );
}

export default DeleteProfileModal;
