import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { byteSize, Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './organization.reducer';
import { IOrganization } from 'app/shared/model/organization.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrganizationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Organization extends React.Component<IOrganizationProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { organizationList, match } = this.props;
    return (
      <div>
        <h2 id="organization-heading">
          <Translate contentKey="serviceNetApp.organization.home.title">Organizations</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.organization.home.createLabel">Create new Organization</Translate>
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
                  <Translate contentKey="serviceNetApp.organization.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.alternateName">Alternate Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.url">Url</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.taxStatus">Tax Status</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.taxId">Tax Id</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.yearIncorporated">Year Incorporated</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.legalStatus">Legal Status</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.active">Active</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.location">Location</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.replacedBy">Replaced By</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.sourceDocument">Source Document</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.organization.account">Account</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {organizationList.map((organization, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${organization.id}`} color="link" size="sm">
                      {organization.id}
                    </Button>
                  </td>
                  <td>{organization.name}</td>
                  <td>{organization.alternateName}</td>
                  <td>{organization.description}</td>
                  <td>{organization.email}</td>
                  <td>{organization.url}</td>
                  <td>{organization.taxStatus}</td>
                  <td>{organization.taxId}</td>
                  <td>
                    <TextFormat type="date" value={organization.yearIncorporated} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{organization.legalStatus}</td>
                  <td>{organization.active ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={organization.updatedAt} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    {organization.locationName ? <Link to={`location/${organization.locationId}`}>{organization.locationName}</Link> : ''}
                  </td>
                  <td>
                    {organization.replacedById ? (
                      <Link to={`organization/${organization.replacedById}`}>{organization.replacedById}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {organization.sourceDocumentDateUploaded ? (
                      <Link to={`document-upload/${organization.sourceDocumentId}`}>{organization.sourceDocumentDateUploaded}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {organization.accountName ? (
                      <Link to={`system-account/${organization.accountId}`}>{organization.accountName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${organization.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${organization.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${organization.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ organization }: IRootState) => ({
  organizationList: organization.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Organization);
