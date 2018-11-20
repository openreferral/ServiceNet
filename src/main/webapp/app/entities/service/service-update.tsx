import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IOrganization } from 'app/shared/model/organization.model';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { IProgram } from 'app/shared/model/program.model';
import { getEntities as getPrograms } from 'app/entities/program/program.reducer';
import { IServiceAtLocation } from 'app/shared/model/service-at-location.model';
import { getEntities as getServiceAtLocations } from 'app/entities/service-at-location/service-at-location.reducer';
import { IRegularSchedule } from 'app/shared/model/regular-schedule.model';
import { getEntities as getRegularSchedules } from 'app/entities/regular-schedule/regular-schedule.reducer';
import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';
import { getEntities as getHolidaySchedules } from 'app/entities/holiday-schedule/holiday-schedule.reducer';
import { IFunding } from 'app/shared/model/funding.model';
import { getEntities as getFundings } from 'app/entities/funding/funding.reducer';
import { IEligibility } from 'app/shared/model/eligibility.model';
import { getEntities as getEligibilities } from 'app/entities/eligibility/eligibility.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './service.reducer';
import { IService } from 'app/shared/model/service.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IServiceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IServiceUpdateState {
  isNew: boolean;
  organizationId: string;
  programId: string;
  locationId: string;
  regularScheduleId: string;
  holidayScheduleId: string;
  fundingId: string;
  eligibilityId: string;
}

export class ServiceUpdate extends React.Component<IServiceUpdateProps, IServiceUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      organizationId: '0',
      programId: '0',
      locationId: '0',
      regularScheduleId: '0',
      holidayScheduleId: '0',
      fundingId: '0',
      eligibilityId: '0',
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

    this.props.getOrganizations();
    this.props.getPrograms();
    this.props.getServiceAtLocations();
    this.props.getRegularSchedules();
    this.props.getHolidaySchedules();
    this.props.getFundings();
    this.props.getEligibilities();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.updatedAt = new Date(values.updatedAt);

    if (errors.length === 0) {
      const { serviceEntity } = this.props;
      const entity = {
        ...serviceEntity,
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
    this.props.history.push('/entity/service');
  };

  render() {
    const {
      serviceEntity,
      organizations,
      programs,
      serviceAtLocations,
      regularSchedules,
      holidaySchedules,
      fundings,
      eligibilities,
      loading,
      updating
    } = this.props;
    const { isNew } = this.state;

    const { description, interpretationServices, applicationProcess, waitTime, fees, accreditations, licenses } = serviceEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.service.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.service.home.createOrEditLabel">Create or edit a Service</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : serviceEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="service-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="serviceNetApp.service.name">Name</Translate>
                  </Label>
                  <AvField
                    id="service-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="alternateNameLabel" for="alternateName">
                    <Translate contentKey="serviceNetApp.service.alternateName">Alternate Name</Translate>
                  </Label>
                  <AvField id="service-alternateName" type="text" name="alternateName" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="serviceNetApp.service.description">Description</Translate>
                  </Label>
                  <AvInput id="service-description" type="textarea" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="url">
                    <Translate contentKey="serviceNetApp.service.url">Url</Translate>
                  </Label>
                  <AvField id="service-url" type="text" name="url" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    <Translate contentKey="serviceNetApp.service.email">Email</Translate>
                  </Label>
                  <AvField id="service-email" type="text" name="email" />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="status">
                    <Translate contentKey="serviceNetApp.service.status">Status</Translate>
                  </Label>
                  <AvField id="service-status" type="text" name="status" />
                </AvGroup>
                <AvGroup>
                  <Label id="interpretationServicesLabel" for="interpretationServices">
                    <Translate contentKey="serviceNetApp.service.interpretationServices">Interpretation Services</Translate>
                  </Label>
                  <AvInput id="service-interpretationServices" type="textarea" name="interpretationServices" />
                </AvGroup>
                <AvGroup>
                  <Label id="applicationProcessLabel" for="applicationProcess">
                    <Translate contentKey="serviceNetApp.service.applicationProcess">Application Process</Translate>
                  </Label>
                  <AvInput id="service-applicationProcess" type="textarea" name="applicationProcess" />
                </AvGroup>
                <AvGroup>
                  <Label id="waitTimeLabel" for="waitTime">
                    <Translate contentKey="serviceNetApp.service.waitTime">Wait Time</Translate>
                  </Label>
                  <AvInput id="service-waitTime" type="textarea" name="waitTime" />
                </AvGroup>
                <AvGroup>
                  <Label id="feesLabel" for="fees">
                    <Translate contentKey="serviceNetApp.service.fees">Fees</Translate>
                  </Label>
                  <AvInput id="service-fees" type="textarea" name="fees" />
                </AvGroup>
                <AvGroup>
                  <Label id="accreditationsLabel" for="accreditations">
                    <Translate contentKey="serviceNetApp.service.accreditations">Accreditations</Translate>
                  </Label>
                  <AvInput id="service-accreditations" type="textarea" name="accreditations" />
                </AvGroup>
                <AvGroup>
                  <Label id="licensesLabel" for="licenses">
                    <Translate contentKey="serviceNetApp.service.licenses">Licenses</Translate>
                  </Label>
                  <AvInput id="service-licenses" type="textarea" name="licenses" />
                </AvGroup>
                <AvGroup>
                  <Label id="typeLabel" for="type">
                    <Translate contentKey="serviceNetApp.service.type">Type</Translate>
                  </Label>
                  <AvField id="service-type" type="text" name="type" />
                </AvGroup>
                <AvGroup>
                  <Label id="updatedAtLabel" for="updatedAt">
                    <Translate contentKey="serviceNetApp.service.updatedAt">Updated At</Translate>
                  </Label>
                  <AvInput
                    id="service-updatedAt"
                    type="datetime-local"
                    className="form-control"
                    name="updatedAt"
                    value={isNew ? null : convertDateTimeFromServer(this.props.serviceEntity.updatedAt)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="organization.name">
                    <Translate contentKey="serviceNetApp.service.organization">Organization</Translate>
                  </Label>
                  <AvInput id="service-organization" type="select" className="form-control" name="organizationId">
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
                  <Label for="program.name">
                    <Translate contentKey="serviceNetApp.service.program">Program</Translate>
                  </Label>
                  <AvInput id="service-program" type="select" className="form-control" name="programId">
                    <option value="" key="0" />
                    {programs
                      ? programs.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/service" replace color="info">
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
  organizations: storeState.organization.entities,
  programs: storeState.program.entities,
  serviceAtLocations: storeState.serviceAtLocation.entities,
  regularSchedules: storeState.regularSchedule.entities,
  holidaySchedules: storeState.holidaySchedule.entities,
  fundings: storeState.funding.entities,
  eligibilities: storeState.eligibility.entities,
  serviceEntity: storeState.service.entity,
  loading: storeState.service.loading,
  updating: storeState.service.updating,
  updateSuccess: storeState.service.updateSuccess
});

const mapDispatchToProps = {
  getOrganizations,
  getPrograms,
  getServiceAtLocations,
  getRegularSchedules,
  getHolidaySchedules,
  getFundings,
  getEligibilities,
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
)(ServiceUpdate);
