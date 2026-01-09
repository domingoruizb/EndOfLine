import { Modal } from 'reactstrap';

export default function ConfirmCancelModal({ isOpen, onConfirm, onCancel }) {
  return (
    <Modal isOpen={isOpen} toggle={onCancel} className="exit-popup">
      <div className="modal-content-custom" style={{ borderColor: '#FE5B02' }}>
        <h4 style={{ color: '#FE5B02', marginTop: '10px' }}>Are you sure you want to cancel the game?</h4>
        <div className="modal-footer-custom">
          <button
            className="normal-button"
            onClick={onConfirm}
            style={{ minWidth: '100px', marginRight: '20px' }}
          >
            Yes
          </button>
          <button
            className="caution-button"
            onClick={onCancel}
            style={{ minWidth: '100px' }}
          >
            No
          </button>
        </div>
      </div>
    </Modal>
  );
}
