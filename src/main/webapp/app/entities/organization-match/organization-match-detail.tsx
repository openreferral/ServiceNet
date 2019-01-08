import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './organization-match.reducer';
import { IOrganizationMatch } from 'app/shared/model/organization-match.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrganizationMatchDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrganizationMatchDetail extends React.Component<IOrganizationMatchDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { organizationMatchEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.organizationMatch.detail.title">OrganizationMatch</Translate> [
            <b>{organizationMatchEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="timestamp">
                <Translate contentKey="serviceNetApp.organizationMatch.timestamp">Timestamp</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={organizationMatchEntity.timestamp} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="deleted">
                <Translate contentKey="serviceNetApp.organizationMatch.deleted">Deleted</Translate>
              </span>
            </dt>
            <dd>{organizationMatchEntity.deleted ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.organizationMatch.organizationRecord">Organization Record</Translate>
            </dt>
            <dd>{organizationMatchEntity.organizationRecordName ? organizationMatchEntity.organizationRecordName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.organizationMatch.partnerVersion">Partner Version</Translate>
            </dt>
            <dd>{organizationMatchEntity.partnerVersionName ? organizationMatchEntity.partnerVersionName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/organization-match" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/organization-match/${organizationMatchEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ organizationMatch }: IRootState) => ({
  organizationMatchEntity: organizationMatch.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrganizationMatchDetail);
