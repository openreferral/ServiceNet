import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IBeds } from 'app/shared/model/beds.model';
import { getEntities as getBeds } from 'app/entities/beds/beds.reducer';
import { IOption } from 'app/shared/model/option.model';
import { getLanguages, getDefinedCoverageAreas, getTags } from 'app/entities/option/option.reducer';
import { getEntity, updateEntity, createEntity, reset } from './shelter.reducer';
import { IShelter } from 'app/shared/model/shelter.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { AUTHORITIES } from '../../config/constants';

export interface IShelterUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IShelterUpdateState {
  isNew: boolean;
  idstags: any[];
  bedsId: string;
  languageId: string;
  definedCoverageAreaId: string;
  phones: object[];
  emails: string[];
}

export class ShelterUpdate extends React.Component<IShelterUpdateProps, IShelterUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idstags: [],
      bedsId: '0',
      languageId: '0',
      definedCoverageAreaId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id,
      phones: [{}],
      emails: ['']
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.shelterEntity && nextProps.shelterEntity !== this.props.shelterEntity) {
      const phones = nextProps.shelterEntity.phones || [];
      phones.push({});
      const emails = nextProps.shelterEntity.emails || [];
      emails.push('');
      this.setState({
        phones,
        emails
      });
    }
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.goBack();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else if (
      this.props.account.authorities.indexOf(AUTHORITIES.ADMIN) > -1 ||
      this.props.account.shelters.indexOf(this.props.match.params.id) > -1
    ) {
      this.props.getEntity(this.props.match.params.id);
    } else {
      this.props.history.replace('/shelters');
    }

    this.props.getBeds();
    this.props.getLanguages();
    this.props.getDefinedCoverageAreas();
    this.props.getTags();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { shelterEntity } = this.props;
      const entity = {
        ...shelterEntity,
        ...values,
        tags: mapIdList(values.tags),
        languages: mapIdList(values.languages),
        definedCoverageAreas: mapIdList(values.definedCoverageAreas),
        phones: this.state.phones,
        emails: this.state.emails
      };
      const { availableBeds, waitlist } = values;

      if (this.state.isNew) {
        entity.beds = { availableBeds, waitlist };
        this.props.createEntity(entity);
      } else {
        const beds = shelterEntity.beds;
        if (!beds || (beds.availableBeds || '') !== availableBeds || (beds.waitlist || '') !== waitlist) {
          entity.beds = { availableBeds, waitlist };
        }
        this.props.updateEntity(entity);
      }
    }
  };

  onPhoneChange = (index, property) => event => {
    const newValue = event.target.value;
    const phones = this.state.phones;
    phones[index][property] = newValue;
    if (newValue && index === this.state.phones.length - 1) {
      phones.push({});
    }
    this.setState({ phones });
  };

  removePhone = index => event => {
    const phones = this.state.phones;
    phones.splice(index, 1);
    this.setState({ phones });
  };

  onEmailChange = index => event => {
    const newValue = event.target.value;
    const emails = this.state.emails;
    emails[index] = newValue;
    if (newValue && index === this.state.emails.length - 1) {
      emails.push('');
    }
    this.setState({ emails });
  };

  removeEmail = index => event => {
    const emails = this.state.emails;
    emails.splice(index, 1);
    this.setState({ emails });
  };

  goBack = () => {
    this.props.history.goBack();
  };

  render() {
    const { shelterEntity, beds, languages, definedCoverageAreas, tags, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.shelter.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.shelter.home.createOrEditLabel">Create or edit a Shelter</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : shelterEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="shelter-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="agencyNameLabel" for="agencyName">
                    <Translate contentKey="serviceNetApp.shelter.agencyName">Agency Name</Translate>
                  </Label>
                  <AvField id="shelter-agencyName" type="text" name="agencyName" />
                </AvGroup>
                <AvGroup>
                  <Label id="programNameLabel" for="programName">
                    <Translate contentKey="serviceNetApp.shelter.programName">Program Name</Translate>
                  </Label>
                  <AvField id="shelter-programName" type="text" name="programName" />
                </AvGroup>
                <AvGroup>
                  <Label id="alternateNameLabel" for="alternateName">
                    <Translate contentKey="serviceNetApp.shelter.alternateName">Alternate Name</Translate>
                  </Label>
                  <AvField id="shelter-alternateName" type="text" name="alternateName" />
                </AvGroup>
                <AvGroup>
                  <Label id="websiteLabel" for="website">
                    <Translate contentKey="serviceNetApp.shelter.website">Website</Translate>
                  </Label>
                  <AvField id="shelter-website" type="text" name="website" />
                </AvGroup>
                <AvGroup>
                  <Label id="eligibilityDetailsLabel" for="eligibilityDetails">
                    <Translate contentKey="serviceNetApp.shelter.eligibilityDetails">Eligibility Details</Translate>
                  </Label>
                  <AvField id="shelter-eligibilityDetails" type="text" name="eligibilityDetails" />
                </AvGroup>
                <AvGroup>
                  <Label id="documentsRequiredLabel" for="documentsRequired">
                    <Translate contentKey="serviceNetApp.shelter.documentsRequired">Documents Required</Translate>
                  </Label>
                  <AvField id="shelter-documentsRequired" type="text" name="documentsRequired" />
                </AvGroup>
                <AvGroup>
                  <Label id="applicationProcessLabel" for="applicationProcess">
                    <Translate contentKey="serviceNetApp.shelter.applicationProcess">Application Process</Translate>
                  </Label>
                  <AvField id="shelter-applicationProcess" type="text" name="applicationProcess" />
                </AvGroup>
                <AvGroup>
                  <Label id="feesLabel" for="fees">
                    <Translate contentKey="serviceNetApp.shelter.fees">Fees</Translate>
                  </Label>
                  <AvField id="shelter-fees" type="text" name="fees" />
                </AvGroup>
                <AvGroup>
                  <Label id="programHoursLabel" for="programHours">
                    <Translate contentKey="serviceNetApp.shelter.programHours">Program Hours</Translate>
                  </Label>
                  <AvField id="shelter-programHours" type="text" name="programHours" />
                </AvGroup>
                <AvGroup>
                  <Label id="holidayScheduleLabel" for="holidaySchedule">
                    <Translate contentKey="serviceNetApp.shelter.holidaySchedule">Holiday Schedule</Translate>
                  </Label>
                  <AvField id="shelter-holidaySchedule" type="text" name="holidaySchedule" />
                </AvGroup>
                <AvGroup>
                  <Label id="address1Label" for="address1">
                    <Translate contentKey="serviceNetApp.shelter.address1">Address 1</Translate>
                  </Label>
                  <AvField id="shelter-address1" type="text" name="address1" />
                </AvGroup>
                <AvGroup>
                  <Label id="address2Label" for="address2">
                    <Translate contentKey="serviceNetApp.shelter.address2">Address 2</Translate>
                  </Label>
                  <AvField id="shelter-address2" type="text" name="address2" />
                </AvGroup>
                <AvGroup>
                  <Label id="cityLabel" for="city">
                    <Translate contentKey="serviceNetApp.shelter.city">City</Translate>
                  </Label>
                  <AvField id="shelter-city" type="text" name="city" />
                </AvGroup>
                <AvGroup>
                  <Label id="zipcodeLabel" for="zipcode">
                    <Translate contentKey="serviceNetApp.shelter.zipcode">Zipcode</Translate>
                  </Label>
                  <AvField id="shelter-zipcode" type="text" name="zipcode" />
                </AvGroup>
                <AvGroup>
                  <Label id="locationDescriptionLabel" for="locationDescription">
                    <Translate contentKey="serviceNetApp.shelter.locationDescription">Location Description</Translate>
                  </Label>
                  <AvField id="shelter-locationDescription" type="text" name="locationDescription" />
                </AvGroup>
                <AvGroup>
                  <Label id="busServiceLabel" for="busService">
                    <Translate contentKey="serviceNetApp.shelter.busService">Bus Service</Translate>
                  </Label>
                  <AvField id="shelter-busService" type="text" name="busService" />
                </AvGroup>
                <AvGroup>
                  <Label id="transportationLabel" for="transportation">
                    <Translate contentKey="serviceNetApp.shelter.transportation">Transportation</Translate>
                  </Label>
                  <AvField id="shelter-transportation" type="text" name="transportation" />
                </AvGroup>
                <AvGroup>
                  <Label id="disabilityAccessLabel" for="disabilityAccess">
                    <Translate contentKey="serviceNetApp.shelter.disabilityAccess">Disability Access</Translate>
                  </Label>
                  <AvField id="shelter-disabilityAccess" type="text" name="disabilityAccess" />
                </AvGroup>
                <AvGroup>
                  <Label id="availableBedsLabel" for="availableBeds">
                    <Translate contentKey="serviceNetApp.beds.availableBeds">Available Beds</Translate>
                  </Label>
                  <AvField
                    id="availableBeds"
                    type="number"
                    className="form-control"
                    name="availableBeds"
                    value={!isNew && shelterEntity.beds && shelterEntity.beds.availableBeds}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="waitlistLabel" for="waitlist">
                    <Translate contentKey="serviceNetApp.beds.waitlist">Waitlist</Translate>
                  </Label>
                  <AvField
                    id="waitlist"
                    type="number"
                    className="form-control"
                    name="waitlist"
                    value={!isNew && shelterEntity.beds && shelterEntity.beds.waitlist}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="options">
                    <Translate contentKey="serviceNetApp.shelter.tags">Tags</Translate>
                  </Label>
                  <AvInput
                    id="shelter-tags"
                    type="select"
                    multiple
                    className="form-control"
                    name="tags"
                    value={shelterEntity.tags && shelterEntity.tags.map(e => e.id)}
                  >
                    {tags
                      ? tags.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.value}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="options">
                    <Translate contentKey="serviceNetApp.shelter.languages">Languages</Translate>
                  </Label>
                  <AvInput
                    id="shelter-languages"
                    type="select"
                    multiple
                    className="form-control"
                    name="languages"
                    value={shelterEntity.languages && shelterEntity.languages.map(e => e.id)}
                  >
                    {languages
                      ? languages.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.value}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="options">
                    <Translate contentKey="serviceNetApp.shelter.definedCoverageAreas">Defined Coverage Areas</Translate>
                  </Label>
                  <AvInput
                    id="shelter-definedCoverageAreas"
                    type="select"
                    multiple
                    className="form-control"
                    name="definedCoverageAreas"
                    value={shelterEntity.definedCoverageAreas && shelterEntity.definedCoverageAreas.map(e => e.id)}
                  >
                    {definedCoverageAreas
                      ? definedCoverageAreas.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.value}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Label>
                  <Translate contentKey="serviceNetApp.shelter.phones">Phones</Translate>
                </Label>
                <table className="table">
                  <thead>
                    <tr>
                      <th scope="col">#</th>
                      <th scope="col">Number</th>
                      <th scope="col">Type</th>
                      <th scope="col">Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {this.state.phones.map((phone, index) => (
                      <tr>
                        <th scope="row">{index}</th>
                        <td>
                          <input
                            className="form-control"
                            type="text"
                            value={phone['number'] || ''}
                            onChange={this.onPhoneChange(index, 'number')}
                          />
                        </td>
                        <td>
                          <input
                            className="form-control"
                            type="text"
                            value={phone['type'] || ''}
                            onChange={this.onPhoneChange(index, 'type')}
                          />
                        </td>
                        <td>
                          {phone['number'] && (
                            <button type="button" className="btn btn-link" onClick={this.removePhone(index)}>
                              Remove
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                <Label>
                  <Translate contentKey="serviceNetApp.shelter.emails">Emails</Translate>
                </Label>
                <table className="table">
                  <thead>
                    <tr>
                      <th scope="col">#</th>
                      <th scope="col">Email</th>
                      <th scope="col">Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {this.state.emails.map((email, index) => (
                      <tr>
                        <th scope="row">{index}</th>
                        <td>
                          <input className="form-control" type="text" value={email || ''} onChange={this.onEmailChange(index)} />
                        </td>
                        <td>
                          {email && (
                            <button type="button" className="btn btn-link" onClick={this.removeEmail(index)}>
                              Remove
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                <Button onClick={this.goBack} id="cancel-save" replace color="info">
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
  beds: storeState.beds.entities,
  languages: storeState.option.languages,
  definedCoverageAreas: storeState.option.definedCoverageAreas,
  tags: storeState.option.tags,
  shelterEntity: storeState.shelter.entity,
  loading: storeState.shelter.loading,
  updating: storeState.shelter.updating,
  updateSuccess: storeState.shelter.updateSuccess,
  account: storeState.authentication.account
});

const mapDispatchToProps = {
  getBeds,
  getLanguages,
  getDefinedCoverageAreas,
  getTags,
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
)(ShelterUpdate);
