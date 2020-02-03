import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './service-taxonomies-details-fields-value.reducer';
import { IServiceTaxonomiesDetailsFieldsValue } from 'app/shared/model/service-taxonomies-details-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceTaxonomiesDetailsFieldsValueProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class ServiceTaxonomiesDetailsFieldsValue extends React.Component<IServiceTaxonomiesDetailsFieldsValueProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { serviceTaxonomiesDetailsFieldsValueList, match } = this.props;
    return (
      <div>
        <h2 id="service-taxonomies-details-fields-value-heading">
          <Translate contentKey="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.home.title">
            Service Taxonomies Details Fields Values
          </Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.home.createLabel">
              Create a new Service Taxonomies Details Fields Value
            </Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {serviceTaxonomiesDetailsFieldsValueList && serviceTaxonomiesDetailsFieldsValueList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.serviceTaxonomiesDetailsField">
                      Service Taxonomies Details Field
                    </Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {serviceTaxonomiesDetailsFieldsValueList.map((serviceTaxonomiesDetailsFieldsValue, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${serviceTaxonomiesDetailsFieldsValue.id}`} color="link" size="sm">
                        {serviceTaxonomiesDetailsFieldsValue.id}
                      </Button>
                    </td>
                    <td>
                      <Translate
                        contentKey={`serviceNetApp.ServiceTaxonomiesDetailsFields.${
                          serviceTaxonomiesDetailsFieldsValue.serviceTaxonomiesDetailsField
                        }`}
                      />
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${serviceTaxonomiesDetailsFieldsValue.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${serviceTaxonomiesDetailsFieldsValue.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${serviceTaxonomiesDetailsFieldsValue.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.home.notFound">
                No Service Taxonomies Details Fields Values found
              </Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ serviceTaxonomiesDetailsFieldsValue }: IRootState) => ({
  serviceTaxonomiesDetailsFieldsValueList: serviceTaxonomiesDetailsFieldsValue.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceTaxonomiesDetailsFieldsValue);
