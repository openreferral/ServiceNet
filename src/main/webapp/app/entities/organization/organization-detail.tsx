import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './organization.reducer';
import { IOrganization } from 'app/shared/model/organization.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrganizationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrganizationDetail extends React.Component<IOrganizationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { organizationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.organization.detail.title">Organization</Translate> [<b>{organizationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="serviceNetApp.organization.name">Name</Translate>
              </span>
            </dt>
            <dd>{organizationEntity.name}</dd>
            <dt>
              <span id="alternateName">
                <Translate contentKey="serviceNetApp.organization.alternateName">Alternate Name</Translate>
              </span>
            </dt>
            <dd>{organizationEntity.alternateName}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="serviceNetApp.organization.description">Description</Translate>
              </span>
            </dt>
            <dd>{organizationEntity.description}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="serviceNetApp.organization.email">Email</Translate>
              </span>
            </dt>
            <dd>{organizationEntity.email}</dd>
            <dt>
              <span id="url">
                <Translate contentKey="serviceNetApp.organization.url">Url</Translate>
              </span>
            </dt>
            <dd>{organizationEntity.url}</dd>
            <dt>
              <span id="taxStatus">
                <Translate contentKey="serviceNetApp.organization.taxStatus">Tax Status</Translate>
              </span>
            </dt>
            <dd>{organizationEntity.taxStatus}</dd>
            <dt>
              <span id="taxId">
                <Translate contentKey="serviceNetApp.organization.taxId">Tax Id</Translate>
              </span>
            </dt>
            <dd>{organizationEntity.taxId}</dd>
            <dt>
              <span id="yearIncorporated">
                <Translate contentKey="serviceNetApp.organization.yearIncorporated">Year Incorporated</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={organizationEntity.yearIncorporated} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="legalStatus">
                <Translate contentKey="serviceNetApp.organization.legalStatus">Legal Status</Translate>
              </span>
            </dt>
            <dd>{organizationEntity.legalStatus}</dd>
            <dt>
              <span id="active">
                <Translate contentKey="serviceNetApp.organization.active">Active</Translate>
              </span>
            </dt>
            <dd>{organizationEntity.active ? 'true' : 'false'}</dd>
            <dt>
              <span id="updatedAt">
                <Translate contentKey="serviceNetApp.organization.updatedAt">Updated At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={organizationEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="serviceNetApp.organization.replacedBy">Replaced By</Translate>
            </dt>
            <dd>{organizationEntity.replacedById ? organizationEntity.replacedById : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.organization.sourceDocument">Source Document</Translate>
            </dt>
            <dd>{organizationEntity.sourceDocumentDateUploaded ? organizationEntity.sourceDocumentDateUploaded : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.organization.account">Account</Translate>
            </dt>
            <dd>{organizationEntity.accountName ? organizationEntity.accountName : ''}</dd>
            <dt>
              <span id="externalDbId">
                <Translate contentKey="serviceNetApp.organizationEntity.externalDbId" />
              </span>
            </dt>
            <dd>{organizationEntity.externalDbId}</dd>
            <dt>
              <span id="providerName">
                <Translate contentKey="serviceNetApp.organizationEntity.providerName" />
              </span>
            </dt>
            <dd>{organizationEntity.providerName}</dd>
          </dl>
          <Button tag={Link} to="/entity/organization" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/organization/${organizationEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ organization }: IRootState) => ({
  organizationEntity: organization.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrganizationDetail);
