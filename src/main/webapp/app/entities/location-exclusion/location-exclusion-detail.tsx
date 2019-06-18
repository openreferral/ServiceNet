import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './location-exclusion.reducer';
import { ILocationExclusion } from 'app/shared/model/location-exclusion.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILocationExclusionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class LocationExclusionDetail extends React.Component<ILocationExclusionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { locationExclusionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.locationExclusion.detail.title">LocationExclusion</Translate> [
            <b>{locationExclusionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="region">
                <Translate contentKey="serviceNetApp.locationExclusion.region">Region</Translate>
              </span>
            </dt>
            <dd>{locationExclusionEntity.region}</dd>
            <dt>
              <span id="city">
                <Translate contentKey="serviceNetApp.locationExclusion.city">City</Translate>
              </span>
            </dt>
            <dd>{locationExclusionEntity.city}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.locationExclusion.config">Config</Translate>
            </dt>
            <dd>{locationExclusionEntity.configId ? locationExclusionEntity.configId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/location-exclusion" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/location-exclusion/${locationExclusionEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ locationExclusion }: IRootState) => ({
  locationExclusionEntity: locationExclusion.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LocationExclusionDetail);
