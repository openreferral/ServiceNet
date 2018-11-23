import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { byteSize, Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './service-taxonomy.reducer';
import { IServiceTaxonomy } from 'app/shared/model/service-taxonomy.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceTaxonomyProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class ServiceTaxonomy extends React.Component<IServiceTaxonomyProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { serviceTaxonomyList, match } = this.props;
    return (
      <div>
        <h2 id="service-taxonomy-heading">
          <Translate contentKey="serviceNetApp.serviceTaxonomy.home.title">Service Taxonomies</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.serviceTaxonomy.home.createLabel">Create new Service Taxonomy</Translate>
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
                  <Translate contentKey="serviceNetApp.serviceTaxonomy.taxonomyDetails">Taxonomy Details</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.serviceTaxonomy.srvc">Srvc</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.serviceTaxonomy.taxonomy">Taxonomy</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {serviceTaxonomyList.map((serviceTaxonomy, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${serviceTaxonomy.id}`} color="link" size="sm">
                      {serviceTaxonomy.id}
                    </Button>
                  </td>
                  <td>{serviceTaxonomy.taxonomyDetails}</td>
                  <td>
                    {serviceTaxonomy.srvcName ? <Link to={`service/${serviceTaxonomy.srvcId}`}>{serviceTaxonomy.srvcName}</Link> : ''}
                  </td>
                  <td>
                    {serviceTaxonomy.taxonomyName ? (
                      <Link to={`taxonomy/${serviceTaxonomy.taxonomyId}`}>{serviceTaxonomy.taxonomyName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${serviceTaxonomy.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${serviceTaxonomy.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${serviceTaxonomy.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ serviceTaxonomy }: IRootState) => ({
  serviceTaxonomyList: serviceTaxonomy.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceTaxonomy);
