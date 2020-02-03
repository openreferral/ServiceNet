import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './organization-fields-value.reducer';
import { IOrganizationFieldsValue } from 'app/shared/model/organization-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrganizationFieldsValueDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrganizationFieldsValueDetail extends React.Component<IOrganizationFieldsValueDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { organizationFieldsValueEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.organizationFieldsValue.detail.title">OrganizationFieldsValue</Translate> [
            <b>{organizationFieldsValueEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="organizationField">
                <Translate contentKey="serviceNetApp.organizationFieldsValue.organizationField">Organization Field</Translate>
              </span>
            </dt>
            <dd>{organizationFieldsValueEntity.organizationField}</dd>
          </dl>
          <Button tag={Link} to="/entity/organization-fields-value" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/organization-fields-value/${organizationFieldsValueEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ organizationFieldsValue }: IRootState) => ({
  organizationFieldsValueEntity: organizationFieldsValue.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrganizationFieldsValueDetail);
