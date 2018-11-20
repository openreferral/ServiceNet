import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './service-area.reducer';
import { IServiceArea } from 'app/shared/model/service-area.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceAreaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ServiceAreaDetail extends React.Component<IServiceAreaDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { serviceAreaEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.serviceArea.detail.title">ServiceArea</Translate> [<b>{serviceAreaEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="description">
                <Translate contentKey="serviceNetApp.serviceArea.description">Description</Translate>
              </span>
            </dt>
            <dd>{serviceAreaEntity.description}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.serviceArea.srvc">Srvc</Translate>
            </dt>
            <dd>{serviceAreaEntity.srvcName ? serviceAreaEntity.srvcName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/service-area" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/service-area/${serviceAreaEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ serviceArea }: IRootState) => ({
  serviceAreaEntity: serviceArea.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceAreaDetail);
