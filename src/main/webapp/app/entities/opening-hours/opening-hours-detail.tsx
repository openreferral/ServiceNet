import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './opening-hours.reducer';
import { IOpeningHours } from 'app/shared/model/opening-hours.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOpeningHoursDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OpeningHoursDetail extends React.Component<IOpeningHoursDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { openingHoursEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.openingHours.detail.title">OpeningHours</Translate> [<b>{openingHoursEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="weekday">
                <Translate contentKey="serviceNetApp.openingHours.weekday">Weekday</Translate>
              </span>
            </dt>
            <dd>{openingHoursEntity.weekday}</dd>
            <dt>
              <span id="opensAt">
                <Translate contentKey="serviceNetApp.openingHours.opensAt">Opens At</Translate>
              </span>
            </dt>
            <dd>{openingHoursEntity.opensAt}</dd>
            <dt>
              <span id="closesAt">
                <Translate contentKey="serviceNetApp.openingHours.closesAt">Closes At</Translate>
              </span>
            </dt>
            <dd>{openingHoursEntity.closesAt}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.openingHours.regularSchedule">Regular Schedule</Translate>
            </dt>
            <dd>{openingHoursEntity.regularScheduleId ? openingHoursEntity.regularScheduleId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/opening-hours" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/opening-hours/${openingHoursEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ openingHours }: IRootState) => ({
  openingHoursEntity: openingHours.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OpeningHoursDetail);
