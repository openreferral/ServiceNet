import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './shelter.reducer';
import { IShelter } from 'app/shared/model/shelter.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShelterProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Shelter extends React.Component<IShelterProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { shelterList, match } = this.props;
    return (
      <div>
        <h2 id="shelter-heading">
          <Translate contentKey="serviceNetApp.shelter.home.title">Shelters</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.shelter.home.createLabel">Create new Shelter</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.agencyName">Agency Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.programName">Program Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.alternateName">Alternate Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.website">Website</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.eligibilityDetails">Eligibility Details</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.documentsRequired">Documents Required</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.applicationProcess">Application Process</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.fees">Fees</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.programHours">Program Hours</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.holidaySchedule">Holiday Schedule</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.address1">Address 1</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.address2">Address 2</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.city">City</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.zipcode">Zipcode</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.locationDescription">Location Description</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.busService">Bus Service</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.transportation">Transportation</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.disabilityAccess">Disability Access</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.availableBeds">Available Beds</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.waitlist">Waitlist</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.tags">Tags</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.languages">Languages</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.definedCoverageAreas">Defined Coverage Areas</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.phones">Phones</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.shelter.emails">Emails</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {shelterList.map((shelter, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${shelter.id}`} color="link" size="sm">
                      {shelter.id}
                    </Button>
                  </td>
                  <td>{shelter.agencyName}</td>
                  <td>{shelter.programName}</td>
                  <td>{shelter.alternateName}</td>
                  <td>{shelter.website}</td>
                  <td>{shelter.eligibilityDetails}</td>
                  <td>{shelter.documentsRequired}</td>
                  <td>{shelter.applicationProcess}</td>
                  <td>{shelter.fees}</td>
                  <td>{shelter.programHours}</td>
                  <td>{shelter.holidaySchedule}</td>
                  <td>{shelter.address1}</td>
                  <td>{shelter.address2}</td>
                  <td>{shelter.city}</td>
                  <td>{shelter.zipcode}</td>
                  <td>{shelter.locationDescription}</td>
                  <td>{shelter.busService}</td>
                  <td>{shelter.transportation}</td>
                  <td>{shelter.disabilityAccess}</td>
                  <td>{shelter.beds ? shelter.beds.availableBeds : ''}</td>
                  <td>{shelter.beds ? shelter.beds.waitlist : ''}</td>
                  <td>
                    {shelter.tags
                      ? shelter.tags.map((val, j) => (
                          <span key={j}>
                            <Link to={`option/${val.id}`}>{val.value}</Link>
                            {j === shelter.tags.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {shelter.languages
                      ? shelter.languages.map((val, j) => (
                          <span key={j}>
                            <Link to={`option/${val.id}`}>{val.value}</Link>
                            {j === shelter.languages.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {shelter.definedCoverageAreas
                      ? shelter.definedCoverageAreas.map((val, j) => (
                          <span key={j}>
                            <Link to={`option/${val.id}`}>{val.value}</Link>
                            {j === shelter.definedCoverageAreas.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {shelter.phones
                      ? shelter.phones.map((val, j) => (
                          <span key={j}>
                            {val.number} {val.type},{' '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>{shelter.emails ? shelter.emails.join(', ') : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${shelter.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shelter.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${shelter.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ shelter }: IRootState) => ({
  shelterList: shelter.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Shelter);
