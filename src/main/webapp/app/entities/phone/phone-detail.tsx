import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './phone.reducer';
import { IPhone } from 'app/shared/model/phone.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPhoneDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PhoneDetail extends React.Component<IPhoneDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { phoneEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.phone.detail.title">Phone</Translate> [<b>{phoneEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="number">
                <Translate contentKey="serviceNetApp.phone.number">Number</Translate>
              </span>
            </dt>
            <dd>{phoneEntity.number}</dd>
            <dt>
              <span id="extension">
                <Translate contentKey="serviceNetApp.phone.extension">Extension</Translate>
              </span>
            </dt>
            <dd>{phoneEntity.extension}</dd>
            <dt>
              <span id="type">
                <Translate contentKey="serviceNetApp.phone.type">Type</Translate>
              </span>
            </dt>
            <dd>{phoneEntity.type}</dd>
            <dt>
              <span id="language">
                <Translate contentKey="serviceNetApp.phone.language">Language</Translate>
              </span>
            </dt>
            <dd>{phoneEntity.language}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="serviceNetApp.phone.description">Description</Translate>
              </span>
            </dt>
            <dd>{phoneEntity.description}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.phone.location">Location</Translate>
            </dt>
            <dd>{phoneEntity.locationName ? phoneEntity.locationName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.phone.srvc">Srvc</Translate>
            </dt>
            <dd>{phoneEntity.srvcName ? phoneEntity.srvcName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.phone.organization">Organization</Translate>
            </dt>
            <dd>{phoneEntity.organizationName ? phoneEntity.organizationName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.phone.contact">Contact</Translate>
            </dt>
            <dd>{phoneEntity.contactName ? phoneEntity.contactName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.phone.serviceAtLocation">Service At Location</Translate>
            </dt>
            <dd>{phoneEntity.serviceAtLocationId ? phoneEntity.serviceAtLocationId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/phone" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/phone/${phoneEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ phone }: IRootState) => ({
  phoneEntity: phone.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhoneDetail);
