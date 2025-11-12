import React from 'react';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

function WinnerModal({ isOpen, toggle, onConfirm }) {
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
                Congratulations!
            </ModalHeader>
            <ModalBody 
                style={{ 
                    backgroundColor: '#2a2a2a', 
                    color: 'white' 
                }}
            >
                CONGRATULATIONS! You have won the game!
                <br/><br/>
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
                    Close
                </Button>
            </ModalFooter>
        </Modal>
    );
}

export default WinnerModal;