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
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IRegularSchedule } from 'app/shared/model/regular-schedule.model';
import { getEntities as getRegularSchedules } from 'app/entities/regular-schedule/regular-schedule.reducer';
import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';
import { getEntities as getHolidaySchedules } from 'app/entities/holiday-schedule/holiday-schedule.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './service-at-location.reducer';
import { IServiceAtLocation } from 'app/shared/model/service-at-location.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IServiceAtLocationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IServiceAtLocationUpdateState {
  isNew: boolean;
  srvcId: string;
  locationId: string;
  regularScheduleId: string;
  holidayScheduleId: string;
}

export class ServiceAtLocationUpdate extends React.Component<IServiceAtLocationUpdateProps, IServiceAtLocationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      srvcId: '0',
      locationId: '0',
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

    this.props.getServices();
    this.props.getLocations();
    this.props.getRegularSchedules();
    this.props.getHolidaySchedules();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { serviceAtLocationEntity } = this.props;
      const entity = {
        ...serviceAtLocationEntity,
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
    this.props.history.push('/entity/service-at-location');
  };

  render() {
    const { serviceAtLocationEntity, services, locations, regularSchedules, holidaySchedules, loading, updating } = this.props;
    const { isNew } = this.state;

    const { description } = serviceAtLocationEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.serviceAtLocation.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.serviceAtLocation.home.createOrEditLabel">Create or edit a ServiceAtLocation</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : serviceAtLocationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="service-at-location-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="serviceNetApp.serviceAtLocation.description">Description</Translate>
                  </Label>
                  <AvInput id="service-at-location-description" type="textarea" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label for="srvc.name">
                    <Translate contentKey="serviceNetApp.serviceAtLocation.srvc">Srvc</Translate>
                  </Label>
                  <AvInput id="service-at-location-srvc" type="select" className="form-control" name="srvcId">
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
                  <Label for="location.name">
                    <Translate contentKey="serviceNetApp.serviceAtLocation.location">Location</Translate>
                  </Label>
                  <AvInput id="service-at-location-location" type="select" className="form-control" name="locationId">
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
                <Button tag={Link} id="cancel-save" to="/entity/service-at-location" replace color="info">
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
  locations: storeState.location.entities,
  regularSchedules: storeState.regularSchedule.entities,
  holidaySchedules: storeState.holidaySchedule.entities,
  serviceAtLocationEntity: storeState.serviceAtLocation.entity,
  loading: storeState.serviceAtLocation.loading,
  updating: storeState.serviceAtLocation.updating,
  updateSuccess: storeState.serviceAtLocation.updateSuccess
});

const mapDispatchToProps = {
  getServices,
  getLocations,
  getRegularSchedules,
  getHolidaySchedules,
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
)(ServiceAtLocationUpdate);
