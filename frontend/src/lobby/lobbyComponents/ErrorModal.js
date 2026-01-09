import { Modal } from 'reactstrap';

export default function ErrorModal({ isOpen, message, onClose }) {
  return (
    <Modal isOpen={isOpen} toggle={onClose} className="error-modal">
      <div className="modal-content-custom" style={{ borderColor: '#FE5B02' }}>
        <h4 style={{ color: '#FE5B02', marginTop: '10px' }}>Error</h4>
        <div style={{ color: 'white', margin: '20px 0' }}>{message}</div>
        <div className="modal-footer-custom">
          <button
            className="normal-button"
            onClick={onClose}
            style={{ minWidth: '100px' }}
          >
            Close
          </button>
        </div>
      </div>
    </Modal>
  );
}
