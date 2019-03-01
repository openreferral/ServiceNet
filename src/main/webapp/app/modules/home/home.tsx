import './home.scss';

import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Translate, getSortState, IPaginationBaseState } from 'react-jhipster';
import { connect } from 'react-redux';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Row, Col, Alert, Container, Progress, Spinner, InputGroup, Input } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { getEntities, reset } from 'app/shared/reducers/activity.reducer';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import ActivityElement from './activity-element';
import SortActivity from './sort-activity';

const SEARCH_TIMEOUT = 1000;

export interface IHomeProp extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IHomeState extends IPaginationBaseState {
  dropdownOpen: boolean;
  loggingOut: boolean;
  searchPhrase: string;
  typingTimeout: number;
}

export class Home extends React.Component<IHomeProp, IHomeState> {
  constructor(props) {
    super(props);

    this.state = {
      ...getSortState(this.props.location, ITEMS_PER_PAGE),
      sort: SORT_RECENTLY_UPDATED,
      order: DEFAULT_SORT_ORDER,
      dropdownOpen: false,
      loggingOut: this.props.location.state ? this.props.location.state.loggingOut : false,
      searchPhrase: '',
      typingTimeout: 0
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

  toggleSort = () => {
    this.setState(prevState => ({
      dropdownOpen: !prevState.dropdownOpen
    }));
  };

  reset = () => {
    this.props.reset();
    if (!this.state.loggingOut) {
      Promise.all([this.props.getSession()]).then(() => {
        this.setState({ activePage: 1 }, () => {
          this.getEntities();
        });
      });
    } else {
      this.setState({ activePage: 1, loggingOut: false });
    }
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    if (this.props.isAuthenticated) {
      this.props.getEntities(this.state.searchPhrase, activePage - 1, itemsPerPage, `${sort},${order}`);
    }
  };

  searchEntities = () => {
    this.props.reset();
    this.getEntities();
  };

  changeSearchPhrase = event => {
    if (this.state.typingTimeout) {
      clearTimeout(this.state.typingTimeout);
    }

    this.setState({
      searchPhrase: event.target.value,
      typingTimeout: setTimeout(() => {
        this.searchEntities();
      }, SEARCH_TIMEOUT)
    });
  };

  clearSearchBar = () => {
    if (this.state.searchPhrase !== '') {
      this.setState({
        searchPhrase: '',
        typingTimeout: setTimeout(() => {
          this.searchEntities();
        }, SEARCH_TIMEOUT)
      });
    }
  };

  render() {
    const { account, activityList } = this.props;
    return (
      <Container>
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
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<Spinner color="primary" />}
            threshold={0}
            initialLoad={false}
          >
            <Container>
              <Row>
                <Col sm="12" className="searchBar">
                  <FontAwesomeIcon icon="search" size="lg" className="searchIcon" />
                  <Input
                    bsSize="lg"
                    className="searchInput"
                    type="search"
                    name="search"
                    id="searchBar"
                    placeholder="Search ServiceNet Activity"
                    value={this.state.searchPhrase}
                    onChange={this.changeSearchPhrase}
                  />
                </Col>
                <div className="searchClearIconContainer" onClick={this.clearSearchBar}>
                  <FontAwesomeIcon icon="times-circle" size="lg" className="searchClearIcon" />
                </div>
              </Row>
              <Row>
                <Col className="col-auto mr-auto">
                  <h2 id="main-page-title">
                    <Translate contentKey="serviceNetApp.activity.unresolved.title" />
                  </h2>
                </Col>
                <Col className="col-1">
                  <div className="text-center">
                    {this.props.activityList.length} / {this.props.totalItems}
                  </div>
                  <Progress color="info" value={(this.props.activityList.length / this.props.totalItems) * 100} />
                </Col>
                <Col className="col-auto">
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
            </Container>
          </InfiniteScroll>
        ) : null}
      </Container>
    );
  }
}

const DEFAULT_SORT_ORDER = 'desc';
const SORT_RECENTLY_UPDATED = 'recent';
const SORT_RECOMMENDED = 'recommended';
const SORT_ARRAY = [SORT_RECENTLY_UPDATED, SORT_RECOMMENDED];

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
