import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { byteSize, Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './organization-match.reducer';
import { IOrganizationMatch } from 'app/shared/model/organization-match.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrganizationMatchProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class OrganizationMatch extends React.Component<IOrganizationMatchProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { organizationMatchList, match } = this.props;
    return (
      <div>
        <h2 id="organization-match-heading">
          <Translate contentKey="serviceNetApp.organizationMatch.home.title">Organization Matches</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.organizationMatch.home.createLabel">Create new Organization Match</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.fieldName">Field Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.timestamp">Timestamp</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.deleted">Deleted</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.fieldPath">Field Path</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.matchedValue">Matched Value</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.organizationRecord">Organization Record</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organizationMatch.partnerVersion">Partner Version</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {organizationMatchList.map((organizationMatch, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${organizationMatch.id}`} color="link" size="sm">
                      {organizationMatch.id}
                    </Button>
                  </td>
                  <td>
                    <TextFormat type="date" value={organizationMatch.timestamp} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{organizationMatch.deleted ? 'true' : 'false'}</td>
                  <td>
                    {organizationMatch.organizationRecordName ? (
                      <Link to={`organization/${organizationMatch.organizationRecordId}`}>{organizationMatch.organizationRecordName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {organizationMatch.partnerVersionName ? (
                      <Link to={`organization/${organizationMatch.partnerVersionId}`}>{organizationMatch.partnerVersionName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${organizationMatch.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${organizationMatch.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${organizationMatch.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ organizationMatch }: IRootState) => ({
  organizationMatchList: organizationMatch.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrganizationMatch);
