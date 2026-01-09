import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from "reactstrap";
/**
 * Toggles the visibility state of a modal.
 *
 * @param {function} setVisible - Function to update the visibility state.
 * @param {boolean} visible - The current visibility state of the modal.
 */
function handleVisible(setVisible, visible) {
    setVisible(!visible);
}
/**
 * Function to render an error modal with a given message.
 * The modal includes a close button and a footer button to dismiss the modal.
 *
 * @param {function} setVisible - Function to update the visibility state of the modal.
 * @param {boolean} [visible=false] - The initial visibility state of the modal. Defaults to `false`.
 * @param {string|null} [message=null] - The message to display inside the modal. If no message is provided, the modal is not rendered.
 * 
 * @returns {JSX.Element} - Returns the modal component if a message is provided; otherwise, returns an empty fragment.
 *
 * @example
 * const [visible, setVisible] = useState(false);
 * const modal = getErrorModal(setVisible, visible, "An error occurred!");
 */
export default function getErrorModal(setVisible, visible = false, message = null) {
    if (message) {
        return (
            <Modal isOpen={visible} toggle={() => handleVisible(setVisible, visible)} size="md" className="achievement-modal">
                <ModalHeader toggle={() => handleVisible(setVisible, visible)} className="achievement-modal-header">
                    Alert!
                </ModalHeader>
                <ModalBody className="achievement-modal-body">
                    <div className="delete-confirm-message">
                        <p>{message}</p>
                    </div>
                </ModalBody>
                <ModalFooter className="achievement-modal-footer">
                    <Button color="primary" onClick={() => handleVisible(setVisible, visible)} className="close-button">
                        Close
                    </Button>
                </ModalFooter>
            </Modal>
        );
    } else {
        return <></>;
    }
}