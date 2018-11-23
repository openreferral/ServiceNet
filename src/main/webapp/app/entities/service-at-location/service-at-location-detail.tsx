import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './service-at-location.reducer';
import { IServiceAtLocation } from 'app/shared/model/service-at-location.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceAtLocationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ServiceAtLocationDetail extends React.Component<IServiceAtLocationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { serviceAtLocationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.serviceAtLocation.detail.title">ServiceAtLocation</Translate> [
            <b>{serviceAtLocationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="description">
                <Translate contentKey="serviceNetApp.serviceAtLocation.description">Description</Translate>
              </span>
            </dt>
            <dd>{serviceAtLocationEntity.description}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.serviceAtLocation.srvc">Srvc</Translate>
            </dt>
            <dd>{serviceAtLocationEntity.srvcName ? serviceAtLocationEntity.srvcName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.serviceAtLocation.location">Location</Translate>
            </dt>
            <dd>{serviceAtLocationEntity.locationName ? serviceAtLocationEntity.locationName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/service-at-location" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/service-at-location/${serviceAtLocationEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ serviceAtLocation }: IRootState) => ({
  serviceAtLocationEntity: serviceAtLocation.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceAtLocationDetail);
