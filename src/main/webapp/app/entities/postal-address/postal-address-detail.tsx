import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './postal-address.reducer';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPostalAddressDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PostalAddressDetail extends React.Component<IPostalAddressDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { postalAddressEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.postalAddress.detail.title">PostalAddress</Translate> [<b>{postalAddressEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="attention">
                <Translate contentKey="serviceNetApp.postalAddress.attention">Attention</Translate>
              </span>
            </dt>
            <dd>{postalAddressEntity.attention}</dd>
            <dt>
              <span id="address1">
                <Translate contentKey="serviceNetApp.postalAddress.address1">Address 1</Translate>
              </span>
            </dt>
            <dd>{postalAddressEntity.address1}</dd>
            <dt>
              <span id="city">
                <Translate contentKey="serviceNetApp.postalAddress.city">City</Translate>
              </span>
            </dt>
            <dd>{postalAddressEntity.city}</dd>
            <dt>
              <span id="region">
                <Translate contentKey="serviceNetApp.postalAddress.region">Region</Translate>
              </span>
            </dt>
            <dd>{postalAddressEntity.region}</dd>
            <dt>
              <span id="stateProvince">
                <Translate contentKey="serviceNetApp.postalAddress.stateProvince">State Province</Translate>
              </span>
            </dt>
            <dd>{postalAddressEntity.stateProvince}</dd>
            <dt>
              <span id="postalCode">
                <Translate contentKey="serviceNetApp.postalAddress.postalCode">Postal Code</Translate>
              </span>
            </dt>
            <dd>{postalAddressEntity.postalCode}</dd>
            <dt>
              <span id="country">
                <Translate contentKey="serviceNetApp.postalAddress.country">Country</Translate>
              </span>
            </dt>
            <dd>{postalAddressEntity.country}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.postalAddress.location">Location</Translate>
            </dt>
            <dd>{postalAddressEntity.locationName ? postalAddressEntity.locationName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/postal-address" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/postal-address/${postalAddressEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ postalAddress }: IRootState) => ({
  postalAddressEntity: postalAddress.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PostalAddressDetail);
