import '../shared-record-view.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Modal, ModalHeader } from 'reactstrap';

export interface IDismissModalProps {
  showModal: boolean;
  handleClose: Function;
}

class DismissModal extends React.Component<IDismissModalProps> {
  render() {
    const { showModal, handleClose } = this.props;

    return (
      <Modal isOpen={showModal} toggle={handleClose} backdrop="static" id="dismiss-page" autoFocus={false} contentClassName="dismiss-modal">
        <ModalHeader id="dismiss-title" toggle={handleClose}>
          <Translate contentKey="multiRecordView.dismiss.success">Thanks! Your feedback has been saved.</Translate>
        </ModalHeader>
      </Modal>
    );
  }
}

export default DismissModal;
