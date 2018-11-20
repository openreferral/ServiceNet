import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './physical-address.reducer';
import { IPhysicalAddress } from 'app/shared/model/physical-address.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPhysicalAddressDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PhysicalAddressDetail extends React.Component<IPhysicalAddressDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { physicalAddressEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.physicalAddress.detail.title">PhysicalAddress</Translate> [
            <b>{physicalAddressEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="attention">
                <Translate contentKey="serviceNetApp.physicalAddress.attention">Attention</Translate>
              </span>
            </dt>
            <dd>{physicalAddressEntity.attention}</dd>
            <dt>
              <span id="address1">
                <Translate contentKey="serviceNetApp.physicalAddress.address1">Address 1</Translate>
              </span>
            </dt>
            <dd>{physicalAddressEntity.address1}</dd>
            <dt>
              <span id="city">
                <Translate contentKey="serviceNetApp.physicalAddress.city">City</Translate>
              </span>
            </dt>
            <dd>{physicalAddressEntity.city}</dd>
            <dt>
              <span id="region">
                <Translate contentKey="serviceNetApp.physicalAddress.region">Region</Translate>
              </span>
            </dt>
            <dd>{physicalAddressEntity.region}</dd>
            <dt>
              <span id="stateProvince">
                <Translate contentKey="serviceNetApp.physicalAddress.stateProvince">State Province</Translate>
              </span>
            </dt>
            <dd>{physicalAddressEntity.stateProvince}</dd>
            <dt>
              <span id="postalCode">
                <Translate contentKey="serviceNetApp.physicalAddress.postalCode">Postal Code</Translate>
              </span>
            </dt>
            <dd>{physicalAddressEntity.postalCode}</dd>
            <dt>
              <span id="country">
                <Translate contentKey="serviceNetApp.physicalAddress.country">Country</Translate>
              </span>
            </dt>
            <dd>{physicalAddressEntity.country}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.physicalAddress.location">Location</Translate>
            </dt>
            <dd>{physicalAddressEntity.locationName ? physicalAddressEntity.locationName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/physical-address" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/physical-address/${physicalAddressEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ physicalAddress }: IRootState) => ({
  physicalAddressEntity: physicalAddress.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhysicalAddressDetail);
