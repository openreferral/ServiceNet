import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './organization-error.reducer';
import { IOrganizationError } from 'app/shared/model/organization-error.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrganizationErrorDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrganizationErrorDetail extends React.Component<IOrganizationErrorDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { organizationErrorEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.organizationError.detail.title">OrganizationError</Translate> [
            <b>{organizationErrorEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="entityName">
                <Translate contentKey="serviceNetApp.organizationError.entityName">Entity Name</Translate>
              </span>
            </dt>
            <dd>{organizationErrorEntity.entityName}</dd>
            <dt>
              <span id="fieldName">
                <Translate contentKey="serviceNetApp.organizationError.fieldName">Field Name</Translate>
              </span>
            </dt>
            <dd>{organizationErrorEntity.fieldName}</dd>
            <dt>
              <span id="externalDbId">
                <Translate contentKey="serviceNetApp.organizationError.externalDbId">External Db Id</Translate>
              </span>
            </dt>
            <dd>{organizationErrorEntity.externalDbId}</dd>
            <dt>
              <span id="cause">
                <Translate contentKey="serviceNetApp.organizationError.cause">Cause</Translate>
              </span>
            </dt>
            <dd>{organizationErrorEntity.cause}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.organizationError.organization">Organization</Translate>
            </dt>
            <dd>{organizationErrorEntity.organization ? organizationErrorEntity.organization.name : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.organizationError.dataImportReport">Data Import Report</Translate>
            </dt>
            <dd>{organizationErrorEntity.dataImportReport ? organizationErrorEntity.dataImportReport.startDate : ''}</dd>
            <dt>
              <span id="invalidValue">
                <Translate contentKey="serviceNetApp.organizationError.invalidValue">Invalid Value</Translate>
              </span>
            </dt>
            <dd>{organizationErrorEntity.invalidValue}</dd>
          </dl>
          <Button tag={Link} to="/entity/organization-error" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/organization-error/${organizationErrorEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ organizationError }: IRootState) => ({
  organizationErrorEntity: organizationError.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrganizationErrorDetail);
