import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IService } from 'app/shared/model/service.model';
import { getEntities as getServices } from 'app/entities/service/service.reducer';
import { ITaxonomy } from 'app/shared/model/taxonomy.model';
import { getEntities as getTaxonomies } from 'app/entities/taxonomy/taxonomy.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './service-taxonomy.reducer';
import { IServiceTaxonomy } from 'app/shared/model/service-taxonomy.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IServiceTaxonomyUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IServiceTaxonomyUpdateState {
  isNew: boolean;
  srvcId: string;
  taxonomyId: string;
}

export class ServiceTaxonomyUpdate extends React.Component<IServiceTaxonomyUpdateProps, IServiceTaxonomyUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      srvcId: '0',
      taxonomyId: '0',
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
    this.props.getTaxonomies();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { serviceTaxonomyEntity } = this.props;
      const entity = {
        ...serviceTaxonomyEntity,
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
    this.props.history.push('/entity/service-taxonomy');
  };

  render() {
    const { serviceTaxonomyEntity, services, taxonomies, loading, updating } = this.props;
    const { isNew } = this.state;

    const { taxonomyDetails } = serviceTaxonomyEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.serviceTaxonomy.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.serviceTaxonomy.home.createOrEditLabel">Create or edit a ServiceTaxonomy</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : serviceTaxonomyEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="service-taxonomy-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="taxonomyDetailsLabel" for="taxonomyDetails">
                    <Translate contentKey="serviceNetApp.serviceTaxonomy.taxonomyDetails">Taxonomy Details</Translate>
                  </Label>
                  <AvInput id="service-taxonomy-taxonomyDetails" type="textarea" name="taxonomyDetails" />
                </AvGroup>
                <AvGroup>
                  <Label for="srvc.name">
                    <Translate contentKey="serviceNetApp.serviceTaxonomy.srvc">Srvc</Translate>
                  </Label>
                  <AvInput id="service-taxonomy-srvc" type="select" className="form-control" name="srvcId">
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
                  <Label for="taxonomy.name">
                    <Translate contentKey="serviceNetApp.serviceTaxonomy.taxonomy">Taxonomy</Translate>
                  </Label>
                  <AvInput id="service-taxonomy-taxonomy" type="select" className="form-control" name="taxonomyId">
                    <option value="" key="0" />
                    {taxonomies
                      ? taxonomies.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="externalDbIdLabel" for="externalDbId">
                    <Translate contentKey="serviceNetApp.organization.externalDbId" />
                  </Label>
                  <AvInput id="organization-externalDbId" type="textarea" name="externalDbId" />
                </AvGroup>
                <AvGroup>
                  <Label id="providerNameLabel" for="providerName">
                    <Translate contentKey="serviceNetApp.organization.providerName" />
                  </Label>
                  <AvInput id="organization-providerName" type="textarea" name="providerName" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/service-taxonomy" replace color="info">
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
  taxonomies: storeState.taxonomy.entities,
  serviceTaxonomyEntity: storeState.serviceTaxonomy.entity,
  loading: storeState.serviceTaxonomy.loading,
  updating: storeState.serviceTaxonomy.updating,
  updateSuccess: storeState.serviceTaxonomy.updateSuccess
});

const mapDispatchToProps = {
  getServices,
  getTaxonomies,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceTaxonomyUpdate);
