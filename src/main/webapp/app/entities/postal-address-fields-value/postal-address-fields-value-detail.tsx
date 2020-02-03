import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './postal-address-fields-value.reducer';
import { IPostalAddressFieldsValue } from 'app/shared/model/postal-address-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPostalAddressFieldsValueDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PostalAddressFieldsValueDetail extends React.Component<IPostalAddressFieldsValueDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { postalAddressFieldsValueEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.postalAddressFieldsValue.detail.title">PostalAddressFieldsValue</Translate> [
            <b>{postalAddressFieldsValueEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="postalAddressField">
                <Translate contentKey="serviceNetApp.postalAddressFieldsValue.postalAddressField">Postal Address Field</Translate>
              </span>
            </dt>
            <dd>{postalAddressFieldsValueEntity.postalAddressField}</dd>
          </dl>
          <Button tag={Link} to="/entity/postal-address-fields-value" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/postal-address-fields-value/${postalAddressFieldsValueEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ postalAddressFieldsValue }: IRootState) => ({
  postalAddressFieldsValueEntity: postalAddressFieldsValue.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PostalAddressFieldsValueDetail);
