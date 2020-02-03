import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contact-details-fields-value.reducer';
import { IContactDetailsFieldsValue } from 'app/shared/model/contact-details-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContactDetailsFieldsValueDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ContactDetailsFieldsValueDetail extends React.Component<IContactDetailsFieldsValueDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { contactDetailsFieldsValueEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.contactDetailsFieldsValue.detail.title">ContactDetailsFieldsValue</Translate> [
            <b>{contactDetailsFieldsValueEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="contactDetailsField">
                <Translate contentKey="serviceNetApp.contactDetailsFieldsValue.contactDetailsField">Contact Details Field</Translate>
              </span>
            </dt>
            <dd>{contactDetailsFieldsValueEntity.contactDetailsField}</dd>
          </dl>
          <Button tag={Link} to="/entity/contact-details-fields-value" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/contact-details-fields-value/${contactDetailsFieldsValueEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ contactDetailsFieldsValue }: IRootState) => ({
  contactDetailsFieldsValueEntity: contactDetailsFieldsValue.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ContactDetailsFieldsValueDetail);
