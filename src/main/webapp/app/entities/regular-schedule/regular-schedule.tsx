import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './regular-schedule.reducer';
import { IRegularSchedule } from 'app/shared/model/regular-schedule.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRegularScheduleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class RegularSchedule extends React.Component<IRegularScheduleProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { regularScheduleList, match } = this.props;
    return (
      <div>
        <h2 id="regular-schedule-heading">
          <Translate contentKey="serviceNetApp.regularSchedule.home.title">Regular Schedules</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.regularSchedule.home.createLabel">Create new Regular Schedule</Translate>
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
                  <Translate contentKey="serviceNetApp.regularSchedule.srvc">Srvc</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.regularSchedule.location">Location</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {regularScheduleList.map((regularSchedule, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${regularSchedule.id}`} color="link" size="sm">
                      {regularSchedule.id}
                    </Button>
                  </td>
                  <td>
                    {regularSchedule.srvcName ? <Link to={`service/${regularSchedule.srvcId}`}>{regularSchedule.srvcName}</Link> : ''}
                  </td>
                  <td>
                    {regularSchedule.locationName ? (
                      <Link to={`location/${regularSchedule.locationId}`}>{regularSchedule.locationName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${regularSchedule.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${regularSchedule.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${regularSchedule.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ regularSchedule }: IRootState) => ({
  regularScheduleList: regularSchedule.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RegularSchedule);
