import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './holiday-schedule.reducer';
import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHolidayScheduleProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class HolidaySchedule extends React.Component<IHolidayScheduleProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { holidayScheduleList, match } = this.props;
    return (
      <div>
        <h2 id="holiday-schedule-heading">
          <Translate contentKey="serviceNetApp.holidaySchedule.home.title">Holiday Schedules</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.holidaySchedule.home.createLabel">Create new Holiday Schedule</Translate>
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
                  <Translate contentKey="serviceNetApp.holidaySchedule.closed">Closed</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.holidaySchedule.opensAt">Opens At</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.holidaySchedule.closesAt">Closes At</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.holidaySchedule.startDate">Start Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.holidaySchedule.endDate">End Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.holidaySchedule.srvc">Srvc</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.holidaySchedule.location">Location</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {holidayScheduleList.map((holidaySchedule, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${holidaySchedule.id}`} color="link" size="sm">
                      {holidaySchedule.id}
                    </Button>
                  </td>
                  <td>{holidaySchedule.closed ? 'true' : 'false'}</td>
                  <td>{holidaySchedule.opensAt}</td>
                  <td>{holidaySchedule.closesAt}</td>
                  <td>
                    <TextFormat type="date" value={holidaySchedule.startDate} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={holidaySchedule.endDate} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    {holidaySchedule.srvcName ? <Link to={`service/${holidaySchedule.srvcId}`}>{holidaySchedule.srvcName}</Link> : ''}
                  </td>
                  <td>
                    {holidaySchedule.locationName ? (
                      <Link to={`location/${holidaySchedule.locationId}`}>{holidaySchedule.locationName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${holidaySchedule.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${holidaySchedule.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${holidaySchedule.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ holidaySchedule }: IRootState) => ({
  holidayScheduleList: holidaySchedule.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HolidaySchedule);
