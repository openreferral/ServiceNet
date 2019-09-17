import './home.scss';

import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Translate, translate, getSortState, IPaginationBaseState } from 'react-jhipster';
import { connect } from 'react-redux';
import ReactGA from 'react-ga';
import axios from 'axios';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Row, Col, Container, Progress, Spinner, Input, Button, Modal, ModalBody, ModalHeader } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { getEntities, reset } from 'app/shared/reducers/activity.reducer';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import ActivityElement from './activity-element';
import SortActivity from './sort-activity';
import { SORT_ARRAY, getSearchPreferences, setSort, setSearchPhrase } from 'app/shared/util/search-utils';
import FilterActivity from './filter-activity';
import SaveActivityFilter from './save-activity-filter';
import _ from 'lodash';
import { updateActivityFilter } from 'app/modules/home/filter-activity.reducer';

const SEARCH_TIMEOUT = 1000;

export interface IHomeProp extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IHomeState extends IPaginationBaseState {
  dropdownOpen: boolean;
  filterCollapseExpanded: boolean;
  saveFilterExpanded: boolean;
  loggingOut: boolean;
  searchPhrase: string;
  typingTimeout: number;
  activityList: [];
  activityFilter: [];
  isOpen: boolean;
}

export class Home extends React.Component<IHomeProp, IHomeState> {
  constructor(props) {
    super(props);

    const { searchPhrase, sort, order } = getSearchPreferences(this.props.account.login);
    this.state = {
      ...getSortState(this.props.location, ITEMS_PER_PAGE),
      sort,
      order,
      dropdownOpen: false,
      filterCollapseExpanded: _.some(this.props.activityFilter, filter => !_.isEmpty(filter)),
      saveFilterExpanded: false,
      loggingOut: this.props.location.state ? this.props.location.state.loggingOut : false,
      searchPhrase,
      typingTimeout: 0,
      activityList: [],
      activityFilter: [],
      isOpen: false
    };
  }

  componentDidMount() {
    this.fetchUserFilterAndSearch();
  }

  componentDidUpdate(prevProps) {
    if (this.props.account.login && !(prevProps.account && prevProps.account.login)) {
      const { searchPhrase, sort, order } = getSearchPreferences(this.props.account.login);
      this.setState({ searchPhrase, sort, order });
    }

    if (this.props.updateSuccess || (this.props.loginSuccess === true && prevProps.loginSuccess === false)) {
      this.fetchUserFilterAndSearch();
    }
  }

  sort = (sort, order) => {
    setSort(this.props.account.login, sort, order);

    ReactGA.event({ category: 'UserActions', action: 'Sorting Records' });

    this.setState({ sort, order }, () => {
      this.reset();
    });
  };

  toggleSort = () => {
    this.setState(prevState => ({
      dropdownOpen: !prevState.dropdownOpen
    }));
  };

  toggleFilter = () => {
    ReactGA.event({ category: 'UserActions', action: 'Clicked Filter' });
    this.setState(prevState => ({
      filterCollapseExpanded: !prevState.filterCollapseExpanded
    }));
  };

  toggleSaveFilter = () => {
    this.setState(prevState => ({
      saveFilterExpanded: !prevState.saveFilterExpanded
    }));
  };

  reset = () => {
    this.props.reset();
    if (!this.state.loggingOut) {
      this.setState({ activePage: 1 }, () => {
        this.getEntities(null);
      });
    } else {
      this.setState({ activePage: 1, loggingOut: false });
    }
  };

  fetchUserFilterAndSearch = () => {
    axios
      .get('/api/activity-filter/current-user-filter')
      .then(response => {
        if (response && response.data) {
          this.props.updateActivityFilter(response.data);
          this.searchEntities(response.data);
        } else {
          this.reset();
        }
      })
      .catch(() => {
        this.reset();
      });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0 && this.props.totalItems > this.props.activityList.length) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities(null));
    }
  };

  getEntities = activityFilter => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    if (this.props.isAuthenticated) {
      return this.props.getEntities(
        this.state.searchPhrase,
        activePage - 1,
        itemsPerPage,
        `${sort},${order}`,
        activityFilter || this.props.activityFilter
      );
    }
  };

  searchEntities = activityFilter => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities(activityFilter);
    });
  };

  changeSearchPhrase = event => {
    if (this.state.typingTimeout) {
      clearTimeout(this.state.typingTimeout);
    }

    const searchPhrase = event.target.value;
    setSearchPhrase(this.props.account.login, searchPhrase);

    ReactGA.event({ category: 'UserActions', action: 'Searching Records' });

    this.setState({
      activePage: 1,
      searchPhrase,
      typingTimeout: setTimeout(() => {
        this.searchEntities(null);
      }, SEARCH_TIMEOUT)
    });
  };

  clearSearchBar = () => {
    if (this.state.searchPhrase !== '') {
      setSearchPhrase(this.props.account.login, '');

      this.setState({
        activePage: 1,
        searchPhrase: '',
        typingTimeout: setTimeout(() => {
          this.searchEntities(null);
        }, SEARCH_TIMEOUT)
      });
    }
  };

  toggle = event => {
    event.stopPropagation();
    this.setState({ isOpen: !this.state.isOpen });
  };

  handleRecordClick = () => {
    ReactGA.event({ category: 'UserActions', action: 'Clicking On A Record' });
  };

  render() {
    const { account, activityList } = this.props;
    return (
      <div>
        {account && account.login ? null : (
          <div className="not-authorised-container">
            <Modal isOpen={this.state.isOpen} toggle={this.toggle} className="video-modal">
              <ModalHeader toggle={this.toggle} />
              <ModalBody>
                <iframe src="https://www.youtube.com/embed/bfoh5CXzWNk" className="video" />
              </ModalBody>
            </Modal>

            <div className="homepage-top-container d-flex">
              <img src="content/images/background-blue.png" className="homepage-top-bg-img" />
              <div className="col-lg-4 col-md-6 video-button d-flex flex-column justify-content-around align-items-center">
                <div>
                  <img src="content/images/homepage-network.svg" className="video-network-img" />
                </div>
                <h3 className="text-center">Connecting Communities to Weave a Stronger Social Safety Net</h3>
                <Button className="btn-black" onClick={this.toggle}>
                  <FontAwesomeIcon icon="play" size="lg" className="play-button-icon mr-1" />
                  Play Video
                </Button>
              </div>
            </div>
            <div className="homepage-bottom-container d-flex">
              <img src="content/images/background-white.jpg" className="homepage-bottom-bg-img" />
              <div className="col-md-12 welcome-container d-flex flex-column justify-content-around align-items-start">
                <h4 className="bold">Welcome to Benetech Service Net!</h4>
                <p>
                  Service Net is a data collaboration platform that makes it easier for referral agencies and service providers to
                  collaborate on services data and better connect people to the services they need to live and prosper. Service Net is
                  currently in limited release for Bay Area pilot users only
                </p>
                <Link to="/login" className="btn btn-orange">
                  <Translate contentKey="global.messages.info.authenticated.signIn">Existing users sign in</Translate>
                </Link>
                <div>
                  <Translate contentKey="global.messages.info.register.noaccount">Don't have an account yet?</Translate>
                  &nbsp;
                  <Translate contentKey="global.messages.info.register.start">Pilot partners can</Translate>
                  &nbsp;
                  <Link to="/register" className="home-page-link">
                    <Translate contentKey="global.messages.info.register.link">register a new account.</Translate>
                  </Link>
                  &nbsp;
                  <Translate contentKey="global.messages.info.register.end">
                    Make sure you contact Benetech after registering for approval.
                  </Translate>
                </div>
              </div>
            </div>
          </div>
        )}
        {account && account.login ? (
          <Container>
            <InfiniteScroll
              pageStart={this.state.activePage}
              loadMore={this.handleLoadMore}
              hasMore={this.state.activePage - 1 < this.props.links.next}
              loader={<Spinner key={0} color="primary" />}
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
                      placeholder={translate('serviceNetApp.activity.home.search.placeholder-' + this.props.activityFilter.searchOn)}
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
                      {!_.isEmpty(activityList) ? activityList.length : 0} / {this.props.totalItems}
                    </div>
                    <Progress color="info" value={(!_.isEmpty(activityList) ? activityList.length / this.props.totalItems : 0) * 100} />
                  </Col>
                  <Col className="col-auto">
                    <SortActivity
                      dropdownOpen={this.state.dropdownOpen}
                      toggleSort={this.toggleSort}
                      sort={this.state.sort}
                      order={this.state.order}
                      sortFunc={this.sort}
                      values={SORT_ARRAY}
                    />
                  </Col>
                  <Col className="col-auto">
                    <Button color="primary" onClick={this.toggleFilter} style={{ marginBottom: '1rem' }}>
                      <Translate contentKey="serviceNetApp.activity.home.filter.toggle" />
                    </Button>
                  </Col>
                  <Col className="col-auto">
                    <Button color="primary" onClick={this.toggleSaveFilter} style={{ marginBottom: '1rem' }}>
                      <Translate contentKey="serviceNetApp.activity.home.filter.savedFilters" />
                    </Button>
                  </Col>
                </Row>
                <Row>
                  <Col md="12">
                    <SaveActivityFilter saveFilterExpanded={this.state.saveFilterExpanded} getActivityEntities={this.searchEntities} />
                  </Col>
                </Row>
                <Row>
                  <Col md="12">
                    <FilterActivity
                      filterCollapseExpanded={this.state.filterCollapseExpanded}
                      getActivityEntities={this.searchEntities}
                      resetActivityFilter={this.reset}
                    />
                  </Col>
                </Row>
                <Row className="text-center font-weight-bold column-title">
                  <Col className="col-6">
                    <Translate contentKey="serviceNetApp.activity.home.leftColumnTitle" />
                  </Col>
                  <Col className="col-6">
                    <Translate contentKey="serviceNetApp.activity.home.rightColumnTitle" />
                  </Col>
                </Row>
                {!_.isEmpty(activityList) ? (
                  activityList.map((activity, i) => (
                    <Link
                      key={`linkToActivity${i + 1}`}
                      to={`/single-record-view/${activity.organizationId}`}
                      className="alert-link"
                      onClick={this.handleRecordClick}
                    >
                      <ActivityElement activity={activity} />
                    </Link>
                  ))
                ) : (
                  <Row>
                    <Col md="8">
                      <Translate contentKey="serviceNetApp.activity.empty" />
                    </Col>
                  </Row>
                )}
              </Container>
            </InfiniteScroll>
          </Container>
        ) : null}
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  loginSuccess: storeState.authentication.loginSuccess,
  activityList: storeState.activity.entities,
  totalItems: storeState.activity.totalItems,
  links: storeState.activity.links,
  entity: storeState.activity.entity,
  updateSuccess: storeState.activity.updateSuccess,
  activityFilter: storeState.filterActivity.activityFilter
});

const mapDispatchToProps = {
  getSession,
  getEntities,
  reset,
  updateActivityFilter
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
