import './home.scss';

import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Translate, translate, getSortState, IPaginationBaseState } from 'react-jhipster';
import { connect } from 'react-redux';
import ReactGA from 'react-ga';

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
import _ from 'lodash';

const SEARCH_TIMEOUT = 1000;

export interface IHomeProp extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IHomeState extends IPaginationBaseState {
  dropdownOpen: boolean;
  filterCollapseExpanded: boolean;
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
      loggingOut: this.props.location.state ? this.props.location.state.loggingOut : false,
      searchPhrase,
      typingTimeout: 0,
      activityList: [],
      activityFilter: [],
      isOpen: false
    };
  }

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate(prevProps) {
    if (this.props.account.login && !(prevProps.account && prevProps.account.login)) {
      const { searchPhrase, sort, order } = getSearchPreferences(this.props.account.login);
      this.setState({ searchPhrase, sort, order });
    }

    if (this.props.updateSuccess || (this.props.loginSuccess === true && prevProps.loginSuccess === false)) {
      this.reset();
    }
  }

  sort = prop => () => {
    setSort(this.props.account.login, prop);

    ReactGA.event({ category: 'UserActions', action: 'Sorting Records' });

    this.setState({ sort: prop }, () => {
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
    if (window.pageYOffset > 0 && this.props.totalItems > this.props.activityList.length) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    if (this.props.isAuthenticated) {
      return this.props.getEntities(this.state.searchPhrase, activePage - 1, itemsPerPage, `${sort},${order}`, this.props.activityFilter);
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

    const searchPhrase = event.target.value;
    setSearchPhrase(this.props.account.login, searchPhrase);

    ReactGA.event({ category: 'UserActions', action: 'Searching Records' });

    this.setState({
      searchPhrase,
      typingTimeout: setTimeout(() => {
        this.searchEntities();
      }, SEARCH_TIMEOUT)
    });
  };

  clearSearchBar = () => {
    if (this.state.searchPhrase !== '') {
      setSearchPhrase(this.props.account.login, '');

      this.setState({
        searchPhrase: '',
        typingTimeout: setTimeout(() => {
          this.searchEntities();
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
                <h3 className="text-center">Connecting Comminities to Weave a Stronger Social Safety Net</h3>
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
                      placeholder={translate('serviceNetApp.activity.home.search.placeholder')}
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
                    <Progress color="info" value={(!_.isEmpty(activityList) ? activityList.length : 0 / this.props.totalItems) * 100} />
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
                  <Col className="col-auto">
                    <Button color="primary" onClick={this.toggleFilter} style={{ marginBottom: '1rem' }}>
                      <Translate contentKey="serviceNetApp.activity.home.filter.toggle" />
                    </Button>
                  </Col>
                </Row>
                <Row>
                  <Col md="12">
                    <FilterActivity filterCollapseExpanded={this.state.filterCollapseExpanded} getActivityEntities={this.searchEntities} />
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
                      to={`/single-record-view/${activity.record.organization.id}`}
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
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
