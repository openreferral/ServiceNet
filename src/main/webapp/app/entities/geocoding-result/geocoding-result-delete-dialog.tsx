import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGeocodingResult } from 'app/shared/model/geocoding-result.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './geocoding-result.reducer';

export interface IGeocodingResultDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GeocodingResultDeleteDialog extends React.Component<IGeocodingResultDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.geocodingResultEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { geocodingResultEntity } = this.props;
    return (
      <Modal isOpen toggle={this.handleClose}>
        <ModalHeader toggle={this.handleClose}>
          <Translate contentKey="entity.delete.title" />
        </ModalHeader>
        <ModalBody id="serviceNetApp.geocodingResult.delete.question">
          <Translate contentKey="serviceNetApp.geocodingResult.delete.question" interpolate={{ id: geocodingResultEntity.id }} />
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={this.handleClose}>
            <FontAwesomeIcon icon="ban" />
            &nbsp;
            <Translate contentKey="entity.action.cancel" />
          </Button>
          <Button id="jhi-confirm-delete-geocodingResult" color="danger" onClick={this.confirmDelete}>
            <FontAwesomeIcon icon="trash" />
            &nbsp;
            <Translate contentKey="entity.action.delete" />
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

const mapStateToProps = ({ geocodingResult }: IRootState) => ({
  geocodingResultEntity: geocodingResult.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GeocodingResultDeleteDialog);
