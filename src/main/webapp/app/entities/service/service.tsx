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
import { getEntities, updateEntity } from './service.reducer';
import { ITEMS_PER_PAGE_ENTITY } from 'app/shared/util/pagination.constants';
import { IService } from 'app/shared/model/service.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServiceProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IServiceState extends IPaginationBaseState {
  dropdownOpenTop: boolean;
  dropdownOpenBottom: boolean;
  itemsPerPage: number;
}

export class Service extends React.Component<IServiceProps, IServiceState> {
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
    const { serviceList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="service-heading">
          <Translate contentKey="serviceNetApp.service.home.title">Services</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.service.home.createLabel">Create new Service</Translate>
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
                  <Translate contentKey="serviceNetApp.service.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.alternateName">Alternate Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.url">Url</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.status">Status</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.interpretationServices">Interpretation Services</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.applicationProcess">Application Process</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.waitTime">Wait Time</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.fees">Fees</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.accreditations">Accreditations</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.licenses">Licenses</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.type">Type</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.organization">Organization</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.program">Program</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.externalDbId" />
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.service.providerName" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {serviceList.map((service, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${service.id}`} color="link" size="sm">
                      {service.id}
                    </Button>
                  </td>
                  <td>{service.name}</td>
                  <td>{service.alternateName}</td>
                  <td>{service.description}</td>
                  <td>{service.url}</td>
                  <td>{service.email}</td>
                  <td>{service.status}</td>
                  <td>{service.interpretationServices}</td>
                  <td>{service.applicationProcess}</td>
                  <td>{service.waitTime}</td>
                  <td>{service.fees}</td>
                  <td>{service.accreditations}</td>
                  <td>{service.licenses}</td>
                  <td>{service.type}</td>
                  <td>{service.externalDbId}</td>
                  <td>{service.providerName}</td>
                  <td>
                    <TextFormat type="date" value={service.updatedAt} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    {service.organizationName ? <Link to={`organization/${service.organizationId}`}>{service.organizationName}</Link> : ''}
                  </td>
                  <td>{service.programName ? <Link to={`program/${service.programId}`}>{service.programName}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${service.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${service.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${service.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ service }: IRootState) => ({
  serviceList: service.entities,
  totalItems: service.totalItems
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
)(Service);
