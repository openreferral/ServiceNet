import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IService } from 'app/shared/model/service.model';
import { getEntities as getServices } from 'app/entities/service/service.reducer';
import { getEntity, updateEntity, createEntity, reset } from './required-document.reducer';
import { IRequiredDocument } from 'app/shared/model/required-document.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRequiredDocumentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRequiredDocumentUpdateState {
  isNew: boolean;
  srvcId: string;
}

export class RequiredDocumentUpdate extends React.Component<IRequiredDocumentUpdateProps, IRequiredDocumentUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      srvcId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getServices();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { requiredDocumentEntity } = this.props;
      const entity = {
        ...requiredDocumentEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/required-document');
  };

  render() {
    const { requiredDocumentEntity, services, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.requiredDocument.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.requiredDocument.home.createOrEditLabel">Create or edit a RequiredDocument</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : requiredDocumentEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="required-document-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="documentLabel" for="document">
                    <Translate contentKey="serviceNetApp.requiredDocument.document">Document</Translate>
                  </Label>
                  <AvField
                    id="required-document-document"
                    type="text"
                    name="document"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="srvc.name">
                    <Translate contentKey="serviceNetApp.requiredDocument.srvc">Srvc</Translate>
                  </Label>
                  <AvInput id="required-document-srvc" type="select" className="form-control" name="srvcId">
                    <option value="" key="0" />
                    {services
                      ? services.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="externalDbIdLabel" for="externalDbId">
                    <Translate contentKey="serviceNetApp.requiredDocument.externalDbId" />
                  </Label>
                  <AvInput id="requiredDocument-externalDbId" type="textarea" name="externalDbId" />
                </AvGroup>
                <AvGroup>
                  <Label id="providerNameLabel" for="providerName">
                    <Translate contentKey="serviceNetApp.requiredDocument.providerName" />
                  </Label>
                  <AvInput id="requiredDocument-providerName" type="textarea" name="providerName" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/required-document" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  services: storeState.service.entities,
  requiredDocumentEntity: storeState.requiredDocument.entity,
  loading: storeState.requiredDocument.loading,
  updating: storeState.requiredDocument.updating,
  updateSuccess: storeState.requiredDocument.updateSuccess
});

const mapDispatchToProps = {
  getServices,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RequiredDocumentUpdate);
