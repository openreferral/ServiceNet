import './home.scss';

import React from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Translate, getSortState, IPaginationBaseState } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert, Dropdown, DropdownMenu, DropdownToggle, DropdownItem } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { getEntities, reset } from 'app/shared/reducers/activity.reducer';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import ActivityElement from './activity-element';
import SortActivity from './sort-activity';

export interface IHomeProp extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IHomeState extends IPaginationBaseState {
  dropdownOpen: boolean;
}

export class Home extends React.Component<IHomeProp, IHomeState> {
  constructor(props) {
    super(props);

    this.toggleSort = this.toggleSort.bind(this);
    this.state = {
      ...getSortState(this.props.location, ITEMS_PER_PAGE),
      sort: SORT_SIZE_NAME,
      dropdownOpen: false
    };
  }

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate(prevProps) {
    if (this.props.updateSuccess || (this.props.loginSuccess === true && prevProps.loginSuccess === false)) {
      this.reset();
    }
  }

  sort = prop => () => {
    this.setState({ sort: prop }, () => {
      this.reset();
    });
  };

  toggleSort() {
    this.setState(prevState => ({
      dropdownOpen: !prevState.dropdownOpen
    }));
  }

  reset = () => {
    Promise.all([this.props.getSession()]).then(() => {
      this.props.reset();
      this.setState({ activePage: 1 }, () => {
        this.getEntities();
      });
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    if (this.props.isAuthenticated) {
      this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
    }
  };

  render() {
    const { account, activityList } = this.props;
    return (
      <div>
        <Row>
          <Col>
            {account && account.login ? null : (
              <div>
                <Alert color="warning">
                  <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>
                  <Link to="/login" className="alert-link">
                    <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
                  </Link>
                  <Translate contentKey="global.messages.info.authenticated.suffix">
                    , you can try the default accounts:
                    <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
                    <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
                  </Translate>
                </Alert>

                <Alert color="warning">
                  <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>
                  &nbsp;
                  <Link to="/register" className="alert-link">
                    <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
                  </Link>
                </Alert>
              </div>
            )}
          </Col>
        </Row>
        {account && account.login ? (
          <div>
            <Row>
              <Col md="8">
                <h2 id="main-page-title">
                  <Translate contentKey="serviceNetApp.activity.unresolved.title" />
                </h2>
              </Col>
            </Row>
            <Row>
              <Col md="8">
                <SortActivity
                  dropdownOpen={this.state.dropdownOpen}
                  toggleSort={this.toggleSort}
                  sort={this.state.sort}
                  sortFunc={this.sort}
                  values={SORT_ARRAY}
                />
              </Col>
            </Row>
            {activityList.map((activity, i) => (
              <Link key={`linkToActivity${i}`} to={`/single-record-view/${activity.record.organization.id}`} className="alert-link">
                <ActivityElement activity={activity} />
              </Link>
            ))}
            {activityList.length === 0 ? (
              <Row>
                <Col md="8">
                  <Translate contentKey="serviceNetApp.activity.empty" />
                </Col>
              </Row>
            ) : null}
          </div>
        ) : null}
      </div>
    );
  }
}

const SORT_AGE_NAME = 'age';
const SORT_SIZE_NAME = 'size';
const SORT_ARRAY = [SORT_SIZE_NAME, SORT_AGE_NAME];

const mapStateToProps = (storeState: IRootState) => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  loginSuccess: storeState.authentication.loginSuccess,
  activityList: storeState.activity.entities,
  totalItems: storeState.activity.totalItems,
  links: storeState.activity.links,
  entity: storeState.activity.entity,
  updateSuccess: storeState.activity.updateSuccess
});

const mapDispatchToProps = {
  getSession,
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
