import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './holiday-schedule.reducer';
import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHolidayScheduleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class HolidayScheduleDetail extends React.Component<IHolidayScheduleDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { holidayScheduleEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.holidaySchedule.detail.title">HolidaySchedule</Translate> [
            <b>{holidayScheduleEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="closed">
                <Translate contentKey="serviceNetApp.holidaySchedule.closed">Closed</Translate>
              </span>
            </dt>
            <dd>{holidayScheduleEntity.closed ? 'true' : 'false'}</dd>
            <dt>
              <span id="opensAt">
                <Translate contentKey="serviceNetApp.holidaySchedule.opensAt">Opens At</Translate>
              </span>
            </dt>
            <dd>{holidayScheduleEntity.opensAt}</dd>
            <dt>
              <span id="closesAt">
                <Translate contentKey="serviceNetApp.holidaySchedule.closesAt">Closes At</Translate>
              </span>
            </dt>
            <dd>{holidayScheduleEntity.closesAt}</dd>
            <dt>
              <span id="startDate">
                <Translate contentKey="serviceNetApp.holidaySchedule.startDate">Start Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={holidayScheduleEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="endDate">
                <Translate contentKey="serviceNetApp.holidaySchedule.endDate">End Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={holidayScheduleEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="serviceNetApp.holidaySchedule.srvc">Srvc</Translate>
            </dt>
            <dd>{holidayScheduleEntity.srvcName ? holidayScheduleEntity.srvcName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.holidaySchedule.location">Location</Translate>
            </dt>
            <dd>{holidayScheduleEntity.locationName ? holidayScheduleEntity.locationName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.holidaySchedule.serviceAtlocation">Service Atlocation</Translate>
            </dt>
            <dd>{holidayScheduleEntity.serviceAtlocationId ? holidayScheduleEntity.serviceAtlocationId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/holiday-schedule" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/holiday-schedule/${holidayScheduleEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ holidaySchedule }: IRootState) => ({
  holidayScheduleEntity: holidaySchedule.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HolidayScheduleDetail);
