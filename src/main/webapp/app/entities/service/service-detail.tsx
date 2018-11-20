import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './service.reducer';
import { IService } from 'app/shared/model/service.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ServiceDetail extends React.Component<IServiceDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { serviceEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.service.detail.title">Service</Translate> [<b>{serviceEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="serviceNetApp.service.name">Name</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.name}</dd>
            <dt>
              <span id="alternateName">
                <Translate contentKey="serviceNetApp.service.alternateName">Alternate Name</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.alternateName}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="serviceNetApp.service.description">Description</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.description}</dd>
            <dt>
              <span id="url">
                <Translate contentKey="serviceNetApp.service.url">Url</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.url}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="serviceNetApp.service.email">Email</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.email}</dd>
            <dt>
              <span id="status">
                <Translate contentKey="serviceNetApp.service.status">Status</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.status}</dd>
            <dt>
              <span id="interpretationServices">
                <Translate contentKey="serviceNetApp.service.interpretationServices">Interpretation Services</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.interpretationServices}</dd>
            <dt>
              <span id="applicationProcess">
                <Translate contentKey="serviceNetApp.service.applicationProcess">Application Process</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.applicationProcess}</dd>
            <dt>
              <span id="waitTime">
                <Translate contentKey="serviceNetApp.service.waitTime">Wait Time</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.waitTime}</dd>
            <dt>
              <span id="fees">
                <Translate contentKey="serviceNetApp.service.fees">Fees</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.fees}</dd>
            <dt>
              <span id="accreditations">
                <Translate contentKey="serviceNetApp.service.accreditations">Accreditations</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.accreditations}</dd>
            <dt>
              <span id="licenses">
                <Translate contentKey="serviceNetApp.service.licenses">Licenses</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.licenses}</dd>
            <dt>
              <span id="type">
                <Translate contentKey="serviceNetApp.service.type">Type</Translate>
              </span>
            </dt>
            <dd>{serviceEntity.type}</dd>
            <dt>
              <span id="updatedAt">
                <Translate contentKey="serviceNetApp.service.updatedAt">Updated At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={serviceEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="serviceNetApp.service.organization">Organization</Translate>
            </dt>
            <dd>{serviceEntity.organizationName ? serviceEntity.organizationName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.service.program">Program</Translate>
            </dt>
            <dd>{serviceEntity.programName ? serviceEntity.programName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/service" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/service/${serviceEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ service }: IRootState) => ({
  serviceEntity: service.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceDetail);
