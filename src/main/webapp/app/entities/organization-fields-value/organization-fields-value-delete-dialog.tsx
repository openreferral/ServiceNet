import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IOrganizationFieldsValue } from 'app/shared/model/organization-fields-value.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './organization-fields-value.reducer';

export interface IOrganizationFieldsValueDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrganizationFieldsValueDeleteDialog extends React.Component<IOrganizationFieldsValueDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.organizationFieldsValueEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { organizationFieldsValueEntity } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>
          <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
        </ModalHeader>
        <ModalBody id="serviceNetApp.organizationFieldsValue.delete.question">
          <Translate
            contentKey="serviceNetApp.organizationFieldsValue.delete.question"
            interpolate={{ id: organizationFieldsValueEntity.id }}
          >
            Are you sure you want to delete this OrganizationFieldsValue?
          </Translate>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={this.handleClose}>
            <FontAwesomeIcon icon="ban" />
            &nbsp;
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>
          <Button id="jhi-confirm-delete-organizationFieldsValue" color="danger" onClick={this.confirmDelete}>
            <FontAwesomeIcon icon="trash" />
            &nbsp;
            <Translate contentKey="entity.action.delete">Delete</Translate>
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = ({ organizationFieldsValue }: IRootState) => ({
  organizationFieldsValueEntity: organizationFieldsValue.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrganizationFieldsValueDeleteDialog);
