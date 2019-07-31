import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import {
  Translate,
  translate,
  ICrudGetAllAction,
  TextFormat,
  JhiPagination,
  getPaginationItemsNumber,
  getSortState,
  IPaginationBaseState
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import PageSizeSelector from '../page-size-selector';
import { IRootState } from 'app/shared/reducers';
import { getEntities, updateEntity } from './service-taxonomy.reducer';
import { ITEMS_PER_PAGE_ENTITY } from 'app/shared/util/pagination.constants';
import { IServiceTaxonomy } from 'app/shared/model/service-taxonomy.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceTaxonomyProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IServiceTaxonomyState extends IPaginationBaseState {
  dropdownOpenTop: boolean;
  dropdownOpenBottom: boolean;
  itemsPerPage: number;
}

export class ServiceTaxonomy extends React.Component<IServiceTaxonomyProps, IServiceTaxonomyState> {
  constructor(props) {
    super(props);

    this.toggleTop = this.toggleTop.bind(this);
    this.toggleBottom = this.toggleBottom.bind(this);
    this.select = this.select.bind(this);
    this.state = {
      dropdownOpenTop: false,
      dropdownOpenBottom: false,
      itemsPerPage: ITEMS_PER_PAGE_ENTITY,
      ...getSortState(this.props.location, ITEMS_PER_PAGE_ENTITY)
    };
  }

  componentDidMount() {
    this.getEntities();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
    window.scrollTo(0, 0);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  toggleTop() {
    this.setState(prevState => ({
      dropdownOpenTop: !prevState.dropdownOpenTop
    }));
  }

  toggleBottom() {
    this.setState(prevState => ({
      dropdownOpenBottom: !prevState.dropdownOpenBottom
    }));
  }

  select = prop => () => {
    this.setState(
      {
        itemsPerPage: prop
      },
      () => this.sortEntities()
    );
  };

  render() {
    const { serviceTaxonomyList, match, totalItems } = this.props;
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
        <Row className="justify-content-center">
          <PageSizeSelector
            dropdownOpen={this.state.dropdownOpenTop}
            toggleSelect={this.toggleTop}
            itemsPerPage={this.state.itemsPerPage}
            selectFunc={this.select}
          />
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
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
                <th>
                  <Translate contentKey="serviceNetApp.serviceTaxonomy.externalDbId" />
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.serviceTaxonomy.providerName" />
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
                  <td>{serviceTaxonomy.externalDbId}</td>
                  <td>{serviceTaxonomy.providerName}</td>
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
        <Row className="justify-content-center">
          <PageSizeSelector
            dropdownOpen={this.state.dropdownOpenBottom}
            toggleSelect={this.toggleBottom}
            itemsPerPage={this.state.itemsPerPage}
            selectFunc={this.select}
          />
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ serviceTaxonomy }: IRootState) => ({
  serviceTaxonomyList: serviceTaxonomy.entities,
  totalItems: serviceTaxonomy.totalItems
});

const mapDispatchToProps = {
  getEntities,
  updateEntity
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceTaxonomy);
