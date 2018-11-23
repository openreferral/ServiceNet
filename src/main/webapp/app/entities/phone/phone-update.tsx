import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IService } from 'app/shared/model/service.model';
import { getEntities as getServices } from 'app/entities/service/service.reducer';
import { IOrganization } from 'app/shared/model/organization.model';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { IContact } from 'app/shared/model/contact.model';
import { getEntities as getContacts } from 'app/entities/contact/contact.reducer';
import { IServiceAtLocation } from 'app/shared/model/service-at-location.model';
import { getEntities as getServiceAtLocations } from 'app/entities/service-at-location/service-at-location.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './phone.reducer';
import { IPhone } from 'app/shared/model/phone.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPhoneUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPhoneUpdateState {
  isNew: boolean;
  locationId: string;
  srvcId: string;
  organizationId: string;
  contactId: string;
  serviceAtLocationId: string;
}

export class PhoneUpdate extends React.Component<IPhoneUpdateProps, IPhoneUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      locationId: '0',
      srvcId: '0',
      organizationId: '0',
      contactId: '0',
      serviceAtLocationId: '0',
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

    this.props.getLocations();
    this.props.getServices();
    this.props.getOrganizations();
    this.props.getContacts();
    this.props.getServiceAtLocations();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { phoneEntity } = this.props;
      const entity = {
        ...phoneEntity,
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
    this.props.history.push('/entity/phone');
  };

  render() {
    const { phoneEntity, locations, services, organizations, contacts, serviceAtLocations, loading, updating } = this.props;
    const { isNew } = this.state;

    const { description } = phoneEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.phone.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.phone.home.createOrEditLabel">Create or edit a Phone</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : phoneEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="phone-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="numberLabel" for="number">
                    <Translate contentKey="serviceNetApp.phone.number">Number</Translate>
                  </Label>
                  <AvField
                    id="phone-number"
                    type="text"
                    name="number"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="extensionLabel" for="extension">
                    <Translate contentKey="serviceNetApp.phone.extension">Extension</Translate>
                  </Label>
                  <AvField id="phone-extension" type="string" className="form-control" name="extension" />
                </AvGroup>
                <AvGroup>
                  <Label id="typeLabel" for="type">
                    <Translate contentKey="serviceNetApp.phone.type">Type</Translate>
                  </Label>
                  <AvField id="phone-type" type="text" name="type" />
                </AvGroup>
                <AvGroup>
                  <Label id="languageLabel" for="language">
                    <Translate contentKey="serviceNetApp.phone.language">Language</Translate>
                  </Label>
                  <AvField id="phone-language" type="text" name="language" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="serviceNetApp.phone.description">Description</Translate>
                  </Label>
                  <AvInput id="phone-description" type="textarea" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label for="location.name">
                    <Translate contentKey="serviceNetApp.phone.location">Location</Translate>
                  </Label>
                  <AvInput id="phone-location" type="select" className="form-control" name="locationId">
                    <option value="" key="0" />
                    {locations
                      ? locations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="srvc.name">
                    <Translate contentKey="serviceNetApp.phone.srvc">Srvc</Translate>
                  </Label>
                  <AvInput id="phone-srvc" type="select" className="form-control" name="srvcId">
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
                  <Label for="organization.name">
                    <Translate contentKey="serviceNetApp.phone.organization">Organization</Translate>
                  </Label>
                  <AvInput id="phone-organization" type="select" className="form-control" name="organizationId">
                    <option value="" key="0" />
                    {organizations
                      ? organizations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="contact.name">
                    <Translate contentKey="serviceNetApp.phone.contact">Contact</Translate>
                  </Label>
                  <AvInput id="phone-contact" type="select" className="form-control" name="contactId">
                    <option value="" key="0" />
                    {contacts
                      ? contacts.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="serviceAtLocation.id">
                    <Translate contentKey="serviceNetApp.phone.serviceAtLocation">Service At Location</Translate>
                  </Label>
                  <AvInput id="phone-serviceAtLocation" type="select" className="form-control" name="serviceAtLocationId">
                    <option value="" key="0" />
                    {serviceAtLocations
                      ? serviceAtLocations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/phone" replace color="info">
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
  locations: storeState.location.entities,
  services: storeState.service.entities,
  organizations: storeState.organization.entities,
  contacts: storeState.contact.entities,
  serviceAtLocations: storeState.serviceAtLocation.entities,
  phoneEntity: storeState.phone.entity,
  loading: storeState.phone.loading,
  updating: storeState.phone.updating,
  updateSuccess: storeState.phone.updateSuccess
});

const mapDispatchToProps = {
  getLocations,
  getServices,
  getOrganizations,
  getContacts,
  getServiceAtLocations,
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
)(PhoneUpdate);
