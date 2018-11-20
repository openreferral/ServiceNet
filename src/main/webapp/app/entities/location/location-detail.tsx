import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './location.reducer';
import { ILocation } from 'app/shared/model/location.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILocationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class LocationDetail extends React.Component<ILocationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { locationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.location.detail.title">Location</Translate> [<b>{locationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="serviceNetApp.location.name">Name</Translate>
              </span>
            </dt>
            <dd>{locationEntity.name}</dd>
            <dt>
              <span id="alternameName">
                <Translate contentKey="serviceNetApp.location.alternameName">Altername Name</Translate>
              </span>
            </dt>
            <dd>{locationEntity.alternameName}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="serviceNetApp.location.description">Description</Translate>
              </span>
            </dt>
            <dd>{locationEntity.description}</dd>
            <dt>
              <span id="transportation">
                <Translate contentKey="serviceNetApp.location.transportation">Transportation</Translate>
              </span>
            </dt>
            <dd>{locationEntity.transportation}</dd>
            <dt>
              <span id="latitude">
                <Translate contentKey="serviceNetApp.location.latitude">Latitude</Translate>
              </span>
            </dt>
            <dd>{locationEntity.latitude}</dd>
            <dt>
              <span id="longitude">
                <Translate contentKey="serviceNetApp.location.longitude">Longitude</Translate>
              </span>
            </dt>
            <dd>{locationEntity.longitude}</dd>
          </dl>
          <Button tag={Link} to="/entity/location" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/location/${locationEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ location }: IRootState) => ({
  locationEntity: location.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LocationDetail);
