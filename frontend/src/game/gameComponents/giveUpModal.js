import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

function GiveUpModal({ isOpen, toggle, onConfirm }) {
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
                If you give up, you will forfeit the game and your opponent will be declared the winner.
                <br/><br/>
                Are you sure you want to end the game?
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
                    Yes, Give Up
                </Button>
            </ModalFooter>
        </Modal>
    );
}

export default GiveUpModal;