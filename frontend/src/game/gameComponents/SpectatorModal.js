import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap'

export default function SpectatorModal ({ isOpen, toggle, onConfirm, onCancel, winnerUsername }) {
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
                    color: '#b1d12d',
                    borderBottom: '1px solid #333'
                }}
            >
                Game Ended
            </ModalHeader>
            <ModalBody
                style={{
                    backgroundColor: '#2a2a2a',
                    color: 'white',
                    textAlign: 'center',
                    fontSize: '1.2rem'
                }}
            >
                The winner is: <strong style={{ color: '#b1d12d' }}>{winnerUsername || 'N/A'}</strong>
                <br/><br/>
                Thanks for spectating!
            </ModalBody>
            <ModalFooter
                style={{
                    backgroundColor: '#1e1e1e',
                    borderTop: '1px solid #333',
                    justifyContent: 'center',
                    gap: '0.5rem'
                }}
            >
                <Button
                    onClick={onConfirm}
                    style={{
                        backgroundColor: '#b1d12d',
                        border: 'none',
                        fontWeight: 'bold',
                        color: '#000'
                    }}
                >
                    Return to Home
                </Button>
            </ModalFooter>
        </Modal>
    )
}
