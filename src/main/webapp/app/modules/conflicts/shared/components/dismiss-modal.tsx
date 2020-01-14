import '../shared-record-view.scss';

import React from 'react';
import { Translate, translate } from 'react-jhipster';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, Label, Alert, Row, Col } from 'reactstrap';
import { AvForm, AvField } from 'availity-reactstrap-validation';

export interface IDismissModalProps {
  showModal: boolean;
  dismissError: boolean;
  handleClose: any;
  handleDismiss: Function;
}

class DismissModal extends React.Component<IDismissModalProps> {
  handleSubmit = (event, errors, { comment }) => {
    const { handleDismiss } = this.props;
    handleDismiss({ comment });
  };

  render() {
    const { showModal, handleClose, dismissError } = this.props;

    return (
      <Modal isOpen={showModal} toggle={handleClose} backdrop="static" id="dismiss-page" autoFocus={false} contentClassName="dismiss-modal">
        <AvForm onSubmit={this.handleSubmit}>
          <ModalHeader id="dismiss-title" toggle={handleClose}>
            <Translate contentKey="multiRecordView.dismiss.question">
              Just to confirm, is this partner record a complete mismatch you want removed from this list?
            </Translate>
          </ModalHeader>
          <ModalBody>
            <Row>
              <Col md="12">
                <AvField name="comment" type="textarea" placeholder={translate('multiRecordView.dismiss.commentPleceholder')} />
              </Col>
              <Col md="12">
                {dismissError ? (
                  <Alert color="danger">
                    <Translate contentKey="multiRecordView.dismiss.error">Error occurred while removing the match!</Translate>
                  </Alert>
                ) : null}
              </Col>
            </Row>
          </ModalBody>
          <ModalFooter>
            <Button color="primary" type="submit">
              <Translate contentKey="multiRecordView.dismiss.remove">Yes, remove this</Translate>
            </Button>
            <button type="button" onClick={handleClose} className="btn close-button">
              <Translate contentKey="multiRecordView.dismiss.cancel">Cancel</Translate>
            </button>
          </ModalFooter>
        </AvForm>
      </Modal>
    );
  }
}

export default DismissModal;
