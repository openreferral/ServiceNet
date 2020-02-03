import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './physical-address-fields-value.reducer';
import { IPhysicalAddressFieldsValue } from 'app/shared/model/physical-address-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPhysicalAddressFieldsValueDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PhysicalAddressFieldsValueDetail extends React.Component<IPhysicalAddressFieldsValueDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { physicalAddressFieldsValueEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.physicalAddressFieldsValue.detail.title">PhysicalAddressFieldsValue</Translate> [
            <b>{physicalAddressFieldsValueEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="physicalAddressField">
                <Translate contentKey="serviceNetApp.physicalAddressFieldsValue.physicalAddressField">Physical Address Field</Translate>
              </span>
            </dt>
            <dd>{physicalAddressFieldsValueEntity.physicalAddressField}</dd>
          </dl>
          <Button tag={Link} to="/entity/physical-address-fields-value" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button
            tag={Link}
            to={`/entity/physical-address-fields-value/${physicalAddressFieldsValueEntity.id}/edit`}
            replace
            color="primary"
          >
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

const mapStateToProps = ({ physicalAddressFieldsValue }: IRootState) => ({
  physicalAddressFieldsValueEntity: physicalAddressFieldsValue.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhysicalAddressFieldsValueDetail);
