import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './fields-display-settings.reducer';
import { IFieldsDisplaySettings } from 'app/shared/model/fields-display-settings.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFieldsDisplaySettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class FieldsDisplaySettings extends React.Component<IFieldsDisplaySettingsProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { fieldsDisplaySettingsList, match } = this.props;
    return (
      <div>
        <h2 id="fields-display-settings-heading">
          <Translate contentKey="serviceNetApp.fieldsDisplaySettings.home.title">Fields Display Settings</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.fieldsDisplaySettings.home.createLabel">Create a new Fields Display Settings</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {fieldsDisplaySettingsList && fieldsDisplaySettingsList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.name">Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.locationFields">Location Fields</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.organizationFields">Organization Fields</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.physicalAddressFields">Physical Address Fields</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.postalAddressFields">Postal Address Fields</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.serviceFields">Service Fields</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.serviceTaxonomiesDetailsFields">
                      Service Taxonomies Details Fields
                    </Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.contactDetailsFields">Contact Details Fields</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.fieldsDisplaySettings.user">User</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {fieldsDisplaySettingsList.map((fieldsDisplaySettings, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${fieldsDisplaySettings.id}`} color="link" size="sm">
                        {fieldsDisplaySettings.id}
                      </Button>
                    </td>
                    <td>{fieldsDisplaySettings.name}</td>
                    <td>{fieldsDisplaySettings.locationFields}</td>
                    <td>{fieldsDisplaySettings.organizationFields}</td>
                    <td>{fieldsDisplaySettings.physicalAddressFields}</td>
                    <td>{fieldsDisplaySettings.postalAddressFields}</td>
                    <td>{fieldsDisplaySettings.serviceFields}</td>
                    <td>{fieldsDisplaySettings.serviceTaxonomiesDetailsFields}</td>
                    <td>{fieldsDisplaySettings.contactDetailsFields}</td>
                    <td>{fieldsDisplaySettings.userLogin ? fieldsDisplaySettings.userLogin : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${fieldsDisplaySettings.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${fieldsDisplaySettings.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${fieldsDisplaySettings.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="serviceNetApp.fieldsDisplaySettings.home.notFound">No Fields Display Settings found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ fieldsDisplaySettings }: IRootState) => ({
  fieldsDisplaySettingsList: fieldsDisplaySettings.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FieldsDisplaySettings);
