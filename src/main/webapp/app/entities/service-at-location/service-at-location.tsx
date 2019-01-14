import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { byteSize, Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './service-at-location.reducer';
import { IServiceAtLocation } from 'app/shared/model/service-at-location.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceAtLocationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class ServiceAtLocation extends React.Component<IServiceAtLocationProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { serviceAtLocationList, match } = this.props;
    return (
      <div>
        <h2 id="service-at-location-heading">
          <Translate contentKey="serviceNetApp.serviceAtLocation.home.title">Service At Locations</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.serviceAtLocation.home.createLabel">Create new Service At Location</Translate>
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
                  <Translate contentKey="serviceNetApp.serviceAtLocation.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.serviceAtLocation.srvc">Srvc</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.serviceAtLocation.location">Location</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.serviceAtLocation.externalDbId" />
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.serviceAtLocation.providerName" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {serviceAtLocationList.map((serviceAtLocation, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${serviceAtLocation.id}`} color="link" size="sm">
                      {serviceAtLocation.id}
                    </Button>
                  </td>
                  <td>{serviceAtLocation.description}</td>
                  <td>{serviceAtLocation.externalDbId}</td>
                  <td>{serviceAtLocation.providerName}</td>
                  <td>
                    {serviceAtLocation.srvcName ? <Link to={`service/${serviceAtLocation.srvcId}`}>{serviceAtLocation.srvcName}</Link> : ''}
                  </td>
                  <td>
                    {serviceAtLocation.locationName ? (
                      <Link to={`location/${serviceAtLocation.locationId}`}>{serviceAtLocation.locationName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${serviceAtLocation.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${serviceAtLocation.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${serviceAtLocation.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ serviceAtLocation }: IRootState) => ({
  serviceAtLocationList: serviceAtLocation.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceAtLocation);
