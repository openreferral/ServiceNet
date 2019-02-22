import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './geocoding-result.reducer';
import { IGeocodingResult } from 'app/shared/model/geocoding-result.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGeocodingResultDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GeocodingResultDetail extends React.Component<IGeocodingResultDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { geocodingResultEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.geocodingResult.detail.title" /> [<b>{geocodingResultEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="address">
                <Translate contentKey="serviceNetApp.geocodingResult.address" />
              </span>
            </dt>
            <dd>{geocodingResultEntity.address}</dd>
            <dt>
              <span id="latitude">
                <Translate contentKey="serviceNetApp.geocodingResult.latitude" />
              </span>
            </dt>
            <dd>{geocodingResultEntity.latitude}</dd>
            <dt>
              <span id="longitude">
                <Translate contentKey="serviceNetApp.geocodingResult.longitude" />
              </span>
            </dt>
            <dd>{geocodingResultEntity.longitude}</dd>
          </dl>
          <Button tag={Link} to="/entity/geocoding-result" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back" />
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/geocoding-result/${geocodingResultEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit" />
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ geocodingResult }: IRootState) => ({
  geocodingResultEntity: geocodingResult.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GeocodingResultDetail);
