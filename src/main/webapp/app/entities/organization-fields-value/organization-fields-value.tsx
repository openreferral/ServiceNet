import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './organization-fields-value.reducer';
import { IOrganizationFieldsValue } from 'app/shared/model/organization-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrganizationFieldsValueProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class OrganizationFieldsValue extends React.Component<IOrganizationFieldsValueProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { organizationFieldsValueList, match } = this.props;
    return (
      <div>
        <h2 id="organization-fields-value-heading">
          <Translate contentKey="serviceNetApp.organizationFieldsValue.home.title">Organization Fields Values</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.organizationFieldsValue.home.createLabel">
              Create a new Organization Fields Value
            </Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {organizationFieldsValueList && organizationFieldsValueList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.organizationFieldsValue.organizationField">Organization Field</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {organizationFieldsValueList.map((organizationFieldsValue, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${organizationFieldsValue.id}`} color="link" size="sm">
                        {organizationFieldsValue.id}
                      </Button>
                    </td>
                    <td>
                      <Translate contentKey={`serviceNetApp.OrganizationFields.${organizationFieldsValue.organizationField}`} />
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${organizationFieldsValue.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${organizationFieldsValue.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${organizationFieldsValue.id}/delete`} color="danger" size="sm">
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
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="serviceNetApp.organizationFieldsValue.home.notFound">No Organization Fields Values found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ organizationFieldsValue }: IRootState) => ({
  organizationFieldsValueList: organizationFieldsValue.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrganizationFieldsValue);
