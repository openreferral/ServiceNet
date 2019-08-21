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
import { getEntities, updateEntity } from './activity-filter.reducer';
import { ITEMS_PER_PAGE_ENTITY, MAX_BUTTONS } from 'app/shared/util/pagination.constants';
import { IActivityFilter } from 'app/shared/model/activity-filter.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IActivityFilterProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IActivityFilterState extends IPaginationBaseState {
  dropdownOpenTop: boolean;
  dropdownOpenBottom: boolean;
  itemsPerPage: number;
}

export class ActivityFilter extends React.Component<IActivityFilterProps, IActivityFilterState> {
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
    const { activePage, sort, order } = this.state;
    this.props.history.push(`${this.props.location.pathname}?page=${activePage}&sort=${sort},${order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.updatePage());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  toggleTop() {
    this.setState({ dropdownOpenTop: !this.state.dropdownOpenTop });
  }

  toggleBottom() {
    this.setState({ dropdownOpenBottom: !this.state.dropdownOpenBottom });
  }

  select = prop => () => {
    this.setState(
      {
        itemsPerPage: prop
      },
      () => this.updatePage()
    );
  };

  updatePage() {
    window.scrollTo(0, 0);
    this.sortEntities();
  }

  render() {
    const { activityFilterList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="activity-filter-heading">
          <Translate contentKey="serviceNetApp.activityFilter.home.title">Activity Filters</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.activityFilter.home.createLabel">Create new Activity Filter</Translate>
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
            maxButtons={MAX_BUTTONS}
          />
        </Row>
        <div className="table-responsive">
          {activityFilterList && activityFilterList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('name')}>
                    <Translate contentKey="serviceNetApp.activityFilter.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('citiesFilterList')}>
                    <Translate contentKey="serviceNetApp.activityFilter.citiesFilterList">Cities Filter List</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('regionFilterList')}>
                    <Translate contentKey="serviceNetApp.activityFilter.regionFilterList">Region Filter List</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('postalCodesFilterList')}>
                    <Translate contentKey="serviceNetApp.activityFilter.postalCodesFilterList">Postal Codes Filter List</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('taxonomiesFilterList')}>
                    <Translate contentKey="serviceNetApp.activityFilter.taxonomiesFilterList">Taxonomies Filter List</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('searchOn')}>
                    <Translate contentKey="serviceNetApp.activityFilter.searchOn">Search On</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('searchFields')}>
                    <Translate contentKey="serviceNetApp.activityFilter.searchFields">Search Fields</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('partnerFilterList')}>
                    <Translate contentKey="serviceNetApp.activityFilter.partnerFilterList">Partner Filter List</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('dateFilter')}>
                    <Translate contentKey="serviceNetApp.activityFilter.dateFilter">Date Filter</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('fromDate')}>
                    <Translate contentKey="serviceNetApp.activityFilter.fromDate">From Date</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('toDate')}>
                    <Translate contentKey="serviceNetApp.activityFilter.toDate">To Date</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('hiddenFilter')}>
                    <Translate contentKey="serviceNetApp.activityFilter.hiddenFilter">Hidden Filter</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('showPartner')}>
                    <Translate contentKey="serviceNetApp.activityFilter.showPartner">Show Partner</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.activityFilter.user">User</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {activityFilterList.map((activityFilter, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${activityFilter.id}`} color="link" size="sm">
                        {activityFilter.id}
                      </Button>
                    </td>
                    <td>{activityFilter.name}</td>
                    <td>{activityFilter.citiesFilterList ? activityFilter.citiesFilterList.join(', ') : ''}</td>
                    <td>{activityFilter.regionFilterList ? activityFilter.regionFilterList.join(', ') : ''}</td>
                    <td>{activityFilter.postalCodesFilterList ? activityFilter.postalCodesFilterList.join(', ') : ''}</td>
                    <td>{activityFilter.taxonomiesFilterList ? activityFilter.taxonomiesFilterList.join(', ') : ''}</td>
                    <td>
                      <Translate contentKey={`serviceNetApp.SearchOn.${activityFilter.searchOn}`} />
                    </td>
                    <td>{activityFilter.searchFields ? activityFilter.searchFields.join(', ') : ''}</td>
                    <td>{activityFilter.partnerFilterList ? activityFilter.partnerFilterList.join(', ') : ''}</td>
                    <td>
                      <Translate contentKey={`serviceNetApp.DateFilter.${activityFilter.dateFilter}`} />
                    </td>
                    <td>
                      <TextFormat type="date" value={activityFilter.fromDate} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>
                      <TextFormat type="date" value={activityFilter.toDate} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>{activityFilter.hiddenFilter ? 'true' : 'false'}</td>
                    <td>{activityFilter.showPartner ? 'true' : 'false'}</td>
                    <td>{activityFilter.userLogin ? activityFilter.userLogin : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${activityFilter.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${activityFilter.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${activityFilter.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="serviceNetApp.activityFilter.home.notFound">No Activity Filters found</Translate>
            </div>
          )}
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
            maxButtons={MAX_BUTTONS}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ activityFilter }: IRootState) => ({
  activityFilterList: activityFilter.entities,
  totalItems: activityFilter.totalItems
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
)(ActivityFilter);
