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
import { getEntities, updateEntity } from './conflict.reducer';
import { ITEMS_PER_PAGE_ENTITY } from 'app/shared/util/pagination.constants';
import { IConflict } from 'app/shared/model/conflict.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IConflictProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IConflictState extends IPaginationBaseState {
  dropdownOpenTop: boolean;
  dropdownOpenBottom: boolean;
  itemsPerPage: number;
}

export class Conflict extends React.Component<IConflictProps, IConflictState> {
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
    const { conflictList, match, totalItems } = this.props;

    return (
      <div>
        <h2 id="conflict-page-heading">
          <Translate contentKey="serviceNetApp.conflict.home.title">Conflicts</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.conflict.home.createLabel">Create new Conflict</Translate>
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
                  <Translate contentKey="serviceNetApp.conflict.currentValue">Current Value</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.currentValueDate">Current Value Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.offeredValue">Offered Value</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.offeredValueDate">Offered Value Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.fieldName">Field Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.entityPath">Entity Path</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.state">State</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.stateDate">State Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.createdDate">Created Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.resourceId">Resource Id</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.partnerResourceId">Partner Resource Id</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.owner">Owner</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.partner">Partner</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {conflictList.map((conflict, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${conflict.id}`} color="link" size="sm">
                      {conflict.id}
                    </Button>
                  </td>
                  <td>{conflict.currentValue}</td>
                  <td>
                    <TextFormat type="date" value={conflict.currentValueDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{conflict.offeredValue}</td>
                  <td>
                    <TextFormat type="date" value={conflict.offeredValueDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{conflict.fieldName}</td>
                  <td>{conflict.entityPath}</td>
                  <td>
                    <Translate contentKey={`serviceNetApp.ConflictStateEnum.${conflict.state}`} />
                  </td>
                  <td>
                    <TextFormat type="date" value={conflict.stateDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={conflict.createdDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{conflict.resourceId}</td>
                  <td>{conflict.partnerResourceId}</td>
                  <td>{conflict.ownerId ? <Link to={`system-account/${conflict.ownerId}`}>{conflict.ownerId}</Link> : ''}</td>
                  <td>{conflict.partnerId ? <Link to={`system-account/${conflict.partnerId}`}>{conflict.partnerId}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${conflict.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${conflict.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${conflict.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ conflict }: IRootState) => ({
  conflictList: conflict.entities,
  totalItems: conflict.totalItems
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
)(Conflict);
