import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './payment-accepted.reducer';
import { IPaymentAccepted } from 'app/shared/model/payment-accepted.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPaymentAcceptedDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PaymentAcceptedDetail extends React.Component<IPaymentAcceptedDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { paymentAcceptedEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.paymentAccepted.detail.title">PaymentAccepted</Translate> [
            <b>{paymentAcceptedEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="payment">
                <Translate contentKey="serviceNetApp.paymentAccepted.payment">Payment</Translate>
              </span>
            </dt>
            <dd>{paymentAcceptedEntity.payment}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.paymentAccepted.srvc">Srvc</Translate>
            </dt>
            <dd>{paymentAcceptedEntity.srvcName ? paymentAcceptedEntity.srvcName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/payment-accepted" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/payment-accepted/${paymentAcceptedEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ paymentAccepted }: IRootState) => ({
  paymentAcceptedEntity: paymentAccepted.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PaymentAcceptedDetail);
