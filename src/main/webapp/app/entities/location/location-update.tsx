import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPhysicalAddress } from 'app/shared/model/physical-address.model';
import { getEntities as getPhysicalAddresses } from 'app/entities/physical-address/physical-address.reducer';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
import { getEntities as getPostalAddresses } from 'app/entities/postal-address/postal-address.reducer';
import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';
import { getEntities as getHolidaySchedules } from 'app/entities/holiday-schedule/holiday-schedule.reducer';
import { IOrganization } from 'app/shared/model/organization.model';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { IRegularSchedule } from 'app/shared/model/regular-schedule.model';
import { getEntities as getRegularSchedules } from 'app/entities/regular-schedule/regular-schedule.reducer';
import { IGeocodingResult } from 'app/shared/model/geocoding-result.model';
import { getEntities as getGeocodingResults } from 'app/entities/geocoding-result/geocoding-result.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './location.reducer';
import { ILocation } from 'app/shared/model/location.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILocationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ILocationUpdateState {
  isNew: boolean;
  idsgeocodingResults: any[];
  physicalAddressId: string;
  postalAddressId: string;
  regularScheduleId: string;
  holidayScheduleId: string;
}

export class LocationUpdate extends React.Component<ILocationUpdateProps, ILocationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsgeocodingResults: [],
      physicalAddressId: '0',
      postalAddressId: '0',
      regularScheduleId: '0',
      holidayScheduleId: '0',
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

    this.props.getPhysicalAddresses();
    this.props.getPostalAddresses();
    this.props.getRegularSchedules();
    this.props.getHolidaySchedules();
    this.props.getOrganizations();
    this.props.getGeocodingResults();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { locationEntity } = this.props;
      const entity = {
        ...locationEntity,
        ...values,
        geocodingResults: mapIdList(values.geocodingResults)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/location');
  };

  render() {
    const {
      locationEntity,
      physicalAddresses,
      postalAddresses,
      regularSchedules,
      holidaySchedules,
      organizations,
      geocodingResults,
      loading,
      updating
    } = this.props;
    const { isNew } = this.state;

    const { description } = locationEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.location.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.location.home.createOrEditLabel">Create or edit a Location</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : locationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="location-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="location-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="location-name">
                    <Translate contentKey="serviceNetApp.location.name">Name</Translate>
                  </Label>
                  <AvField
                    id="location-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="alternateNameLabel" for="location-alternateName">
                    <Translate contentKey="serviceNetApp.location.alternateName">Alternate Name</Translate>
                  </Label>
                  <AvField id="location-alternateName" type="text" name="alternateName" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="location-description">
                    <Translate contentKey="serviceNetApp.location.description">Description</Translate>
                  </Label>
                  <AvInput id="location-description" type="textarea" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label for="organization.name">
                    <Translate contentKey="serviceNetApp.location.organization">Organization</Translate>
                  </Label>
                  <AvInput id="location-organization" type="select" className="form-control" name="organizationId">
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
                  <Label id="transportationLabel" for="location-transportation">
                    <Translate contentKey="serviceNetApp.location.transportation">Transportation</Translate>
                  </Label>
                  <AvField id="location-transportation" type="text" name="transportation" />
                </AvGroup>
                <AvGroup>
                  <Label id="latitudeLabel" for="location-latitude">
                    <Translate contentKey="serviceNetApp.location.latitude">Latitude</Translate>
                  </Label>
                  <AvField id="location-latitude" type="string" className="form-control" name="latitude" />
                </AvGroup>
                <AvGroup>
                  <Label id="longitudeLabel" for="location-longitude">
                    <Translate contentKey="serviceNetApp.location.longitude">Longitude</Translate>
                  </Label>
                  <AvField id="location-longitude" type="string" className="form-control" name="longitude" />
                </AvGroup>
                <AvGroup>
                  <Label id="externalDbIdLabel" for="location-externalDbId">
                    <Translate contentKey="serviceNetApp.location.externalDbId">External Db Id</Translate>
                  </Label>
                  <AvField id="location-externalDbId" type="text" name="externalDbId" />
                </AvGroup>
                <AvGroup>
                  <Label id="providerNameLabel" for="location-providerName">
                    <Translate contentKey="serviceNetApp.location.providerName">Provider Name</Translate>
                  </Label>
                  <AvField id="location-providerName" type="text" name="providerName" />
                </AvGroup>
                <AvGroup>
                  <Label for="location-geocodingResults">
                    <Translate contentKey="serviceNetApp.location.geocodingResults">Geocoding Results</Translate>
                  </Label>
                  <AvInput
                    id="location-geocodingResults"
                    type="select"
                    multiple
                    className="form-control"
                    name="geocodingResults"
                    value={locationEntity.geocodingResults && locationEntity.geocodingResults.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {geocodingResults
                      ? geocodingResults.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.address}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/location" replace color="info">
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
  physicalAddresses: storeState.physicalAddress.entities,
  postalAddresses: storeState.postalAddress.entities,
  regularSchedules: storeState.regularSchedule.entities,
  holidaySchedules: storeState.holidaySchedule.entities,
  organizations: storeState.organization.entities,
  geocodingResults: storeState.geocodingResult.entities,
  locationEntity: storeState.location.entity,
  loading: storeState.location.loading,
  updating: storeState.location.updating,
  updateSuccess: storeState.location.updateSuccess
});

const mapDispatchToProps = {
  getPhysicalAddresses,
  getPostalAddresses,
  getRegularSchedules,
  getHolidaySchedules,
  getOrganizations,
  getGeocodingResults,
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
)(LocationUpdate);
