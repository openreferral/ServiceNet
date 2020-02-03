import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IContactDetailsFieldsValue } from 'app/shared/model/contact-details-fields-value.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './contact-details-fields-value.reducer';

export interface IContactDetailsFieldsValueDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ContactDetailsFieldsValueDeleteDialog extends React.Component<IContactDetailsFieldsValueDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.contactDetailsFieldsValueEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { contactDetailsFieldsValueEntity } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>
          <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
        </ModalHeader>
        <ModalBody id="serviceNetApp.contactDetailsFieldsValue.delete.question">
          <Translate
            contentKey="serviceNetApp.contactDetailsFieldsValue.delete.question"
            interpolate={{ id: contactDetailsFieldsValueEntity.id }}
          >
            Are you sure you want to delete this ContactDetailsFieldsValue?
          </Translate>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={this.handleClose}>
            <FontAwesomeIcon icon="ban" />
            &nbsp;
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>
          <Button id="jhi-confirm-delete-contactDetailsFieldsValue" color="danger" onClick={this.confirmDelete}>
            <FontAwesomeIcon icon="trash" />
            &nbsp;
            <Translate contentKey="entity.action.delete">Delete</Translate>
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = ({ contactDetailsFieldsValue }: IRootState) => ({
  contactDetailsFieldsValueEntity: contactDetailsFieldsValue.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ContactDetailsFieldsValueDeleteDialog);
