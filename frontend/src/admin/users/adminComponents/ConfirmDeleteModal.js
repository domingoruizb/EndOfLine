import React from "react";
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from "reactstrap";

export default function ConfirmDeleteModal({ isOpen, toggle, onConfirm }) {
  return (
    <Modal isOpen={isOpen} toggle={toggle}>
      <ModalHeader toggle={toggle} className="user-modal-header">
        Confirm Deletion
      </ModalHeader>
      <ModalBody className="user-modal-body">
        Are you sure you want to delete this user?
      </ModalBody>
      <ModalFooter className="user-modal-footer">
        <Button color="secondary" onClick={toggle} className="user-modal-cancel">
          Cancel
        </Button>
        <Button color="danger" onClick={onConfirm} className="user-modal-delete">
          Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
}
