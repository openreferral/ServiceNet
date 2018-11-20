import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { byteSize, Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './location.reducer';
import { ILocation } from 'app/shared/model/location.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILocationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Location extends React.Component<ILocationProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { locationList, match } = this.props;
    return (
      <div>
        <h2 id="location-heading">
          <Translate contentKey="serviceNetApp.location.home.title">Locations</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.location.home.createLabel">Create new Location</Translate>
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
                  <Translate contentKey="serviceNetApp.location.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.location.alternameName">Altername Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.location.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.location.transportation">Transportation</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.location.latitude">Latitude</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.location.longitude">Longitude</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {locationList.map((location, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${location.id}`} color="link" size="sm">
                      {location.id}
                    </Button>
                  </td>
                  <td>{location.name}</td>
                  <td>{location.alternameName}</td>
                  <td>{location.description}</td>
                  <td>{location.transportation}</td>
                  <td>{location.latitude}</td>
                  <td>{location.longitude}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${location.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${location.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${location.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ location }: IRootState) => ({
  locationList: location.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Location);
