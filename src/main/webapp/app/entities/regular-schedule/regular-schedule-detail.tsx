import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './regular-schedule.reducer';
import { IRegularSchedule } from 'app/shared/model/regular-schedule.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRegularScheduleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RegularScheduleDetail extends React.Component<IRegularScheduleDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { regularScheduleEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.regularSchedule.detail.title">RegularSchedule</Translate> [
            <b>{regularScheduleEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="weekday">
                <Translate contentKey="serviceNetApp.regularSchedule.weekday">Weekday</Translate>
              </span>
            </dt>
            <dd>{regularScheduleEntity.weekday}</dd>
            <dt>
              <span id="opensAt">
                <Translate contentKey="serviceNetApp.regularSchedule.opensAt">Opens At</Translate>
              </span>
            </dt>
            <dd>{regularScheduleEntity.opensAt}</dd>
            <dt>
              <span id="closesAt">
                <Translate contentKey="serviceNetApp.regularSchedule.closesAt">Closes At</Translate>
              </span>
            </dt>
            <dd>{regularScheduleEntity.closesAt}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.regularSchedule.srvc">Srvc</Translate>
            </dt>
            <dd>{regularScheduleEntity.srvcName ? regularScheduleEntity.srvcName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.regularSchedule.location">Location</Translate>
            </dt>
            <dd>{regularScheduleEntity.locationName ? regularScheduleEntity.locationName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.regularSchedule.serviceAtlocation">Service Atlocation</Translate>
            </dt>
            <dd>{regularScheduleEntity.serviceAtlocationId ? regularScheduleEntity.serviceAtlocationId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/regular-schedule" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/regular-schedule/${regularScheduleEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ regularSchedule }: IRootState) => ({
  regularScheduleEntity: regularSchedule.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RegularScheduleDetail);
