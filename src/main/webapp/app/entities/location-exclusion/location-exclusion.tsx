import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './location-exclusion.reducer';
import { ILocationExclusion } from 'app/shared/model/location-exclusion.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILocationExclusionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class LocationExclusion extends React.Component<ILocationExclusionProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { locationExclusionList, match } = this.props;
    return (
      <div>
        <h2 id="location-exclusion-heading">
          <Translate contentKey="serviceNetApp.locationExclusion.home.title">Location Exclusions</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.locationExclusion.home.createLabel">Create new Location Exclusion</Translate>
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
                  <Translate contentKey="serviceNetApp.locationExclusion.region">Region</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.locationExclusion.city">City</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.locationExclusion.config">Config</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {locationExclusionList.map((locationExclusion, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${locationExclusion.id}`} color="link" size="sm">
                      {locationExclusion.id}
                    </Button>
                  </td>
                  <td>{locationExclusion.region}</td>
                  <td>{locationExclusion.city}</td>
                  <td>
                    {locationExclusion.configId ? (
                      <Link to={`exclusions-config/${locationExclusion.configId}`}>{locationExclusion.configId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${locationExclusion.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${locationExclusion.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${locationExclusion.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ locationExclusion }: IRootState) => ({
  locationExclusionList: locationExclusion.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LocationExclusion);
