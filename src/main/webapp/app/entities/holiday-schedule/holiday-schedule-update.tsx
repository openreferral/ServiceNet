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
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IServiceAtLocation } from 'app/shared/model/service-at-location.model';
import { getEntities as getServiceAtLocations } from 'app/entities/service-at-location/service-at-location.reducer';
import { getEntity, updateEntity, createEntity, reset } from './holiday-schedule.reducer';
import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHolidayScheduleUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IHolidayScheduleUpdateState {
  isNew: boolean;
  srvcId: string;
  locationId: string;
  serviceAtlocationId: string;
}

export class HolidayScheduleUpdate extends React.Component<IHolidayScheduleUpdateProps, IHolidayScheduleUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      srvcId: '0',
      locationId: '0',
      serviceAtlocationId: '0',
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
    this.props.getServiceAtLocations();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { holidayScheduleEntity } = this.props;
      const entity = {
        ...holidayScheduleEntity,
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
    this.props.history.push('/entity/holiday-schedule');
  };

  render() {
    const { holidayScheduleEntity, services, locations, serviceAtLocations, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.holidaySchedule.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.holidaySchedule.home.createOrEditLabel">Create or edit a HolidaySchedule</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : holidayScheduleEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="holiday-schedule-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="closedLabel" check>
                    <AvInput id="holiday-schedule-closed" type="checkbox" className="form-control" name="closed" />
                    <Translate contentKey="serviceNetApp.holidaySchedule.closed">Closed</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="opensAtLabel" for="opensAt">
                    <Translate contentKey="serviceNetApp.holidaySchedule.opensAt">Opens At</Translate>
                  </Label>
                  <AvField id="holiday-schedule-opensAt" type="text" name="opensAt" />
                </AvGroup>
                <AvGroup>
                  <Label id="closesAtLabel" for="closesAt">
                    <Translate contentKey="serviceNetApp.holidaySchedule.closesAt">Closes At</Translate>
                  </Label>
                  <AvField id="holiday-schedule-closesAt" type="text" name="closesAt" />
                </AvGroup>
                <AvGroup>
                  <Label id="startDateLabel" for="startDate">
                    <Translate contentKey="serviceNetApp.holidaySchedule.startDate">Start Date</Translate>
                  </Label>
                  <AvField
                    id="holiday-schedule-startDate"
                    type="date"
                    className="form-control"
                    name="startDate"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="endDateLabel" for="endDate">
                    <Translate contentKey="serviceNetApp.holidaySchedule.endDate">End Date</Translate>
                  </Label>
                  <AvField
                    id="holiday-schedule-endDate"
                    type="date"
                    className="form-control"
                    name="endDate"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="srvc.name">
                    <Translate contentKey="serviceNetApp.holidaySchedule.srvc">Srvc</Translate>
                  </Label>
                  <AvInput id="holiday-schedule-srvc" type="select" className="form-control" name="srvcId">
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
                    <Translate contentKey="serviceNetApp.holidaySchedule.location">Location</Translate>
                  </Label>
                  <AvInput id="holiday-schedule-location" type="select" className="form-control" name="locationId">
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
                  <Label for="serviceAtlocation.id">
                    <Translate contentKey="serviceNetApp.holidaySchedule.serviceAtlocation">Service Atlocation</Translate>
                  </Label>
                  <AvInput id="holiday-schedule-serviceAtlocation" type="select" className="form-control" name="serviceAtlocationId">
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
                <Button tag={Link} id="cancel-save" to="/entity/holiday-schedule" replace color="info">
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
  serviceAtLocations: storeState.serviceAtLocation.entities,
  holidayScheduleEntity: storeState.holidaySchedule.entity,
  loading: storeState.holidaySchedule.loading,
  updating: storeState.holidaySchedule.updating,
  updateSuccess: storeState.holidaySchedule.updateSuccess
});

const mapDispatchToProps = {
  getServices,
  getLocations,
  getServiceAtLocations,
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
)(HolidayScheduleUpdate);
