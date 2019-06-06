import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shelter.reducer';
import { IShelter } from 'app/shared/model/shelter.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShelterDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ShelterDetail extends React.Component<IShelterDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shelterEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.shelter.detail.title">Shelter</Translate> [<b>{shelterEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="agencyName">
                <Translate contentKey="serviceNetApp.shelter.agencyName">Agency Name</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.agencyName}</dd>
            <dt>
              <span id="programName">
                <Translate contentKey="serviceNetApp.shelter.programName">Program Name</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.programName}</dd>
            <dt>
              <span id="alternateName">
                <Translate contentKey="serviceNetApp.shelter.alternateName">Alternate Name</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.alternateName}</dd>
            <dt>
              <span id="website">
                <Translate contentKey="serviceNetApp.shelter.website">Website</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.website}</dd>
            <dt>
              <span id="eligibilityDetails">
                <Translate contentKey="serviceNetApp.shelter.eligibilityDetails">Eligibility Details</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.eligibilityDetails}</dd>
            <dt>
              <span id="documentsRequired">
                <Translate contentKey="serviceNetApp.shelter.documentsRequired">Documents Required</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.documentsRequired}</dd>
            <dt>
              <span id="applicationProcess">
                <Translate contentKey="serviceNetApp.shelter.applicationProcess">Application Process</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.applicationProcess}</dd>
            <dt>
              <span id="fees">
                <Translate contentKey="serviceNetApp.shelter.fees">Fees</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.fees}</dd>
            <dt>
              <span id="programHours">
                <Translate contentKey="serviceNetApp.shelter.programHours">Program Hours</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.programHours}</dd>
            <dt>
              <span id="holidaySchedule">
                <Translate contentKey="serviceNetApp.shelter.holidaySchedule">Holiday Schedule</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.holidaySchedule}</dd>
            <dt>
              <span id="address1">
                <Translate contentKey="serviceNetApp.shelter.address1">Address 1</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.address1}</dd>
            <dt>
              <span id="address2">
                <Translate contentKey="serviceNetApp.shelter.address2">Address 2</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.address2}</dd>
            <dt>
              <span id="city">
                <Translate contentKey="serviceNetApp.shelter.city">City</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.city}</dd>
            <dt>
              <span id="zipcode">
                <Translate contentKey="serviceNetApp.shelter.zipcode">Zipcode</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.zipcode}</dd>
            <dt>
              <span id="locationDescription">
                <Translate contentKey="serviceNetApp.shelter.locationDescription">Location Description</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.locationDescription}</dd>
            <dt>
              <span id="busService">
                <Translate contentKey="serviceNetApp.shelter.busService">Bus Service</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.busService}</dd>
            <dt>
              <span id="transportation">
                <Translate contentKey="serviceNetApp.shelter.transportation">Transportation</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.transportation}</dd>
            <dt>
              <span id="disabilityAccess">
                <Translate contentKey="serviceNetApp.shelter.disabilityAccess">Disability Access</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.disabilityAccess}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.shelter.availableBeds">Available Beds</Translate>
            </dt>
            <dd>{shelterEntity.beds ? shelterEntity.beds.availableBeds : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.shelter.waitlist">Waitlist</Translate>
            </dt>
            <dd>{shelterEntity.beds ? shelterEntity.beds.waitlist : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.shelter.tags">Tags</Translate>
            </dt>
            <dd>
              {shelterEntity.tags
                ? shelterEntity.tags.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.value}</a>
                      {i === shelterEntity.tags.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
            <dt>
              <Translate contentKey="serviceNetApp.shelter.languages">Languages</Translate>
            </dt>
            <dd>
              {shelterEntity.languages
                ? shelterEntity.languages.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.value}</a>
                      {i === shelterEntity.languages.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
            <dt>
              <Translate contentKey="serviceNetApp.shelter.definedCoverageAreas">Defined Coverage Areas</Translate>
            </dt>
            <dd>
              {shelterEntity.definedCoverageAreas
                ? shelterEntity.definedCoverageAreas.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.value}</a>
                      {i === shelterEntity.definedCoverageAreas.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
            <dt>
              <span id="email">
                <Translate contentKey="serviceNetApp.shelter.phones">Phones</Translate>
              </span>
            </dt>
            <dd>
              {shelterEntity.phones
                ? shelterEntity.phones.map((val, i) => (
                    <span>
                      {val.number} {val.type},{' '}
                    </span>
                  ))
                : null}
            </dd>
            <dt>
              <span id="email">
                <Translate contentKey="serviceNetApp.shelter.emails">Emails</Translate>
              </span>
            </dt>
            <dd>{shelterEntity.emails ? shelterEntity.emails.join(', ') : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/shelter" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/shelter/${shelterEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ shelter }: IRootState) => ({
  shelterEntity: shelter.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShelterDetail);
