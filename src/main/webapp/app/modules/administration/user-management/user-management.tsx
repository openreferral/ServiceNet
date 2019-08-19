import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table, Row, Badge } from 'reactstrap';
import {
  Translate,
  ICrudGetAllAction,
  ICrudPutAction,
  TextFormat,
  JhiPagination,
  getPaginationItemsNumber,
  getSortState,
  IPaginationBaseState
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE, MAX_BUTTONS, FIRST_PAGE } from 'app/shared/util/pagination.constants';
import { getUsers, updateUser } from './user-management.reducer';
import { IRootState } from 'app/shared/reducers';
import _ from 'lodash';
import queryString from 'query-string';

export interface IUserManagementProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}

export class UserManagement extends React.Component<IUserManagementProps, IPaginationBaseState> {
  state: IPaginationBaseState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    if (!_.isEqual(this.props.location.search, '')) {
      const parsed = queryString.parse(this.props.location.search);
      this.setCustomState(parsed.page, parsed.itemsPerPage, parsed.sort);
    } else {
      this.getUsers();
    }
  }

  componentDidUpdate(prevProps) {
    if (!(this.props.location === prevProps.location) && !(this.props.location.search === '')) {
      const parsed = queryString.parse(this.props.location.search);
      this.setCustomState(parsed.page, parsed.itemsPerPage, parsed.sort);
    }
    if (!(this.props.location === prevProps.location) && this.props.location.search === '') {
      this.setCustomState(FIRST_PAGE, ITEMS_PER_PAGE, this.state.sort);
    }
  }

  setCustomState(page, items, sort) {
    this.setState({
      activePage: Number(page),
      itemsPerPage: Number(items),
      ...getSortState(this.props.location, items)
    });
    this.props.getUsers(Number(page) - 1, Number(items), `${sort}`);
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortUsers()
    );
  };

  sortUsers() {
    this.getUsers();
    const { itemsPerPage } = this.state;
    this.props.history.push(
      `${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${
        this.state.order
      }&itemsPerPage=${itemsPerPage}`
    );
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortUsers());

  getUsers = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getUsers(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  toggleActive = user => () => {
    this.props.updateUser({
      ...user,
      activated: !user.activated
    });
  };

  render() {
    const { users, account, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="user-management-page-heading">
          <Translate contentKey="userManagement.home.title">Users</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity">
            <FontAwesomeIcon icon="plus" /> <Translate contentKey="userManagement.home.createLabel">Create a new user</Translate>
          </Link>
        </h2>
        <Table responsive striped>
          <thead>
            <tr>
              <th className="hand" onClick={this.sort('id')}>
                <Translate contentKey="global.field.id">ID</Translate>
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={this.sort('login')}>
                <Translate contentKey="userManagement.login">Login</Translate>
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={this.sort('email')}>
                <Translate contentKey="userManagement.email">Email</Translate>
                <FontAwesomeIcon icon="sort" />
              </th>
              <th />
              <th className="hand" onClick={this.sort('langKey')}>
                <Translate contentKey="userManagement.langKey">Lang Key</Translate>
                <FontAwesomeIcon icon="sort" />
              </th>
              <th>
                <Translate contentKey="userManagement.profiles">Profiles</Translate>
              </th>
              <th className="hand" onClick={this.sort('createdDate')}>
                <Translate contentKey="userManagement.createdDate">Created Date</Translate>
                <FontAwesomeIcon icon="sort" />
              </th>
              <th className="hand" onClick={this.sort('lastModifiedBy')}>
                <Translate contentKey="userManagement.lastModifiedBy">Last Modified By</Translate>
                <FontAwesomeIcon icon="sort" />
              </th>
              <th id="modified-date-sort" className="hand" onClick={this.sort('lastModifiedDate')}>
                <Translate contentKey="userManagement.lastModifiedDate">Last Modified Date</Translate>
                <FontAwesomeIcon icon="sort" />
              </th>
              <th id="modified-date-sort" className="hand" onClick={this.sort('systemAccountName')}>
                <Translate contentKey="userManagement.systemAccount" />
                <FontAwesomeIcon icon="sort" />
              </th>
              <th />
            </tr>
          </thead>
          <tbody>
            {users.map((user, i) => (
              <tr id={user.login} key={`user-${i}`}>
                <td>
                  <Button tag={Link} to={`${match.url}/${user.login}`} color="link" size="sm">
                    {user.id}
                  </Button>
                </td>
                <td>{user.login}</td>
                <td>{user.email}</td>
                <td>
                  {user.activated ? (
                    <Button color="success" onClick={this.toggleActive(user)}>
                      Activated
                    </Button>
                  ) : (
                    <Button color="danger" onClick={this.toggleActive(user)}>
                      Deactivated
                    </Button>
                  )}
                </td>
                <td>{user.langKey}</td>
                <td>
                  {user.authorities
                    ? user.authorities.map((authority, j) => (
                        <div key={`user-auth-${i}-${j}`}>
                          <Badge color="info">{authority}</Badge>
                        </div>
                      ))
                    : null}
                </td>
                <td>
                  <TextFormat value={user.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                </td>
                <td>{user.lastModifiedBy}</td>
                <td>
                  <TextFormat value={user.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                </td>
                <td>{user.systemAccountName}</td>
                <td className="text-right">
                  <div className="btn-group flex-btn-group-container">
                    <Button tag={Link} to={`${match.url}/${user.login}`} color="info" size="sm">
                      <FontAwesomeIcon icon="eye" />{' '}
                      <span className="d-none d-md-inline">
                        <Translate contentKey="entity.action.view">View</Translate>
                      </span>
                    </Button>
                    <Button tag={Link} to={`${match.url}/${user.login}/edit`} color="primary" size="sm">
                      <FontAwesomeIcon icon="pencil-alt" />{' '}
                      <span className="d-none d-md-inline">
                        <Translate contentKey="entity.action.edit">Edit</Translate>
                      </span>
                    </Button>
                    <Button
                      tag={Link}
                      to={`${match.url}/${user.login}/delete`}
                      color="danger"
                      size="sm"
                      disabled={account.login === user.login}
                    >
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
        <Row className="justify-content-center">
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

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  totalItems: storeState.userManagement.totalItems,
  account: storeState.authentication.account
});

const mapDispatchToProps = { getUsers, updateUser };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserManagement);
