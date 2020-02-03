import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IServiceTaxonomiesDetailsFieldsValue } from 'app/shared/model/service-taxonomies-details-fields-value.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './service-taxonomies-details-fields-value.reducer';

export interface IServiceTaxonomiesDetailsFieldsValueDeleteDialogProps
  extends StateProps,
    DispatchProps,
    RouteComponentProps<{ id: string }> {}

export class ServiceTaxonomiesDetailsFieldsValueDeleteDialog extends React.Component<
  IServiceTaxonomiesDetailsFieldsValueDeleteDialogProps
> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.serviceTaxonomiesDetailsFieldsValueEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { serviceTaxonomiesDetailsFieldsValueEntity } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>
          <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
        </ModalHeader>
        <ModalBody id="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.delete.question">
          <Translate
            contentKey="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.delete.question"
            interpolate={{ id: serviceTaxonomiesDetailsFieldsValueEntity.id }}
          >
            Are you sure you want to delete this ServiceTaxonomiesDetailsFieldsValue?
          </Translate>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={this.handleClose}>
            <FontAwesomeIcon icon="ban" />
            &nbsp;
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>
          <Button id="jhi-confirm-delete-serviceTaxonomiesDetailsFieldsValue" color="danger" onClick={this.confirmDelete}>
            <FontAwesomeIcon icon="trash" />
            &nbsp;
            <Translate contentKey="entity.action.delete">Delete</Translate>
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = ({ serviceTaxonomiesDetailsFieldsValue }: IRootState) => ({
  serviceTaxonomiesDetailsFieldsValueEntity: serviceTaxonomiesDetailsFieldsValue.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceTaxonomiesDetailsFieldsValueDeleteDialog);
