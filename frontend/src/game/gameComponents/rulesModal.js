import React from 'react';
import { Modal, ModalHeader, ModalBody } from 'reactstrap';
import RulesPage from '../../public/rules';

function RulesModal({ isOpen, toggle }) {
  return (
    <Modal isOpen={isOpen} toggle={toggle} centered size="xl" dark>
      <ModalHeader
        toggle={toggle}
        style={{
          backgroundColor: '#1e1e1e',
          color: '#FE5B02',
          borderBottom: '1px solid #333',
          fontWeight: 800,
          letterSpacing: '1px',
        }}
      >
        Rules
      </ModalHeader>
      <ModalBody
        style={{
          backgroundColor: '#2a2a2a',
          color: 'white',
          maxHeight: '80vh',
          overflowY: 'auto',
          padding: '1rem',
        }}
      >
        <RulesPage embed />
      </ModalBody>
    </Modal>
  );
}

export default RulesModal;
