import './shelters.scss';

import React from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { getSortState, IPaginationBaseState, translate, Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import InfiniteScroll from 'react-infinite-scroller';
import { Row, Col, Container, Progress, Spinner, Input, Button } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { searchEntities } from 'app/entities/shelter/shelter.reducer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import SortShelter from 'app/modules/shelter/sort-shelter';
import FilterShelter from 'app/modules/shelter/filter-shelter';
import ShelterElement from 'app/modules/shelter/shelter-element';
import _ from 'lodash';
import ReactGA from 'react-ga';
import { getSearchPreferences, setShelterSort, setShelterSearchPhrase, SHELTER_SORT_ARRAY } from 'app/shared/util/search-utils';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { getSession } from 'app/shared/reducers/authentication';
import { reset } from 'app/shared/reducers/activity.reducer';

const SEARCH_TIMEOUT = 1000;

export interface ISheltersProp extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ISheltersState extends IPaginationBaseState {
  dropdownOpen: boolean;
  filterCollapseExpanded: boolean;
  loggingOut: boolean;
  searchPhrase: string;
  typingTimeout: number;
  shelterList: [];
  shelterFilter: [];
}

export class Shelters extends React.Component<ISheltersProp, ISheltersState> {
  constructor(props) {
    super(props);
    const { shelterSearchPreferences } = getSearchPreferences(this.props.account.login);
    const { searchPhrase, sort, order } = shelterSearchPreferences;
    this.state = {
      ...getSortState(this.props.location, ITEMS_PER_PAGE),
      sort,
      order,
      dropdownOpen: false,
      filterCollapseExpanded: _.some(this.props.shelterFilter, filter => !_.isEmpty(filter)),
      loggingOut: this.props.location.state ? this.props.location.state.loggingOut : false,
      searchPhrase,
      typingTimeout: 0,
      shelterList: [],
      shelterFilter: []
    };
  }

  componentDidMount() {
    this.getEntities();
  }

  handleLoadMore = () => {
    if (window.pageYOffset > 0 && this.props.totalItems > this.props.shelterList.length) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    if (this.props.isAuthenticated) {
      return this.props.searchEntities(this.state.searchPhrase, activePage - 1, itemsPerPage, `${sort},${order}`, this.props.shelterFilter);
    }
  };

  searchEntities = () => {
    this.props.reset();
    return this.getEntities();
  };

  changeSearchPhrase = event => {
    if (this.state.typingTimeout) {
      clearTimeout(this.state.typingTimeout);
    }

    const searchPhrase = event.target.value;
    setShelterSearchPhrase(this.props.account.login, searchPhrase);

    ReactGA.event({ category: 'UserActions', action: 'Shelter - Searching Records' });

    this.setState({
      searchPhrase,
      typingTimeout: setTimeout(() => {
        this.searchEntities();
      }, SEARCH_TIMEOUT)
    });
  };

  clearSearchBar = () => {
    if (this.state.searchPhrase !== '') {
      setShelterSearchPhrase(this.props.account.login, '');

      this.setState({
        searchPhrase: '',
        typingTimeout: setTimeout(() => {
          this.searchEntities();
        }, SEARCH_TIMEOUT)
      });
    }
  };

  sort = prop => () => {
    setShelterSort(this.props.account.login, prop);

    ReactGA.event({ category: 'UserActions', action: 'Shelter - Sorting Records' });

    this.setState({ sort: prop }, () => {
      this.reset();
    });
  };

  reset = () => {
    this.props.reset();
    if (!this.state.loggingOut) {
      this.setState({ activePage: 1 }, () => {
        this.getEntities();
      });
    } else {
      this.setState({ activePage: 1, loggingOut: false });
    }
  };

  toggleSort = () => {
    this.setState(prevState => ({
      dropdownOpen: !prevState.dropdownOpen
    }));
  };

  toggleFilter = () => {
    ReactGA.event({ category: 'UserActions', action: 'Shelter - Clicked Filter' });
    this.setState(prevState => ({
      filterCollapseExpanded: !prevState.filterCollapseExpanded
    }));
  };

  toggleReset = () => {
    this.reset();
  };

  render() {
    const { shelterList } = this.props;
    return (
      <div>
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
                    placeholder={translate('serviceNetApp.shelter.home.search.placeholder')}
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
                    <Translate contentKey="serviceNetApp.shelter.home.title" />
                  </h2>
                </Col>
                <Col className="col-1">
                  <div className="text-center">
                    {!_.isEmpty(shelterList) ? shelterList.length : 0} / {this.props.totalItems}
                  </div>
                  <Progress color="info" value={(!_.isEmpty(shelterList) ? shelterList.length / this.props.totalItems : 0) * 100} />
                </Col>
                <Col className="col-auto">
                  <SortShelter
                    dropdownOpen={this.state.dropdownOpen}
                    toggleSort={this.toggleSort}
                    sort={this.state.sort}
                    sortFunc={this.sort}
                    values={SHELTER_SORT_ARRAY}
                  />
                </Col>
                <Col className="col-auto">
                  <Button color="primary" onClick={this.toggleFilter} style={{ marginBottom: '1rem' }}>
                    <Translate contentKey="serviceNetApp.shelter.home.filter.toggle" />
                  </Button>
                </Col>
              </Row>
              <Row>
                <Col md="12">
                  <FilterShelter
                    filterCollapseExpanded={this.state.filterCollapseExpanded}
                    getShelterEntities={this.searchEntities}
                    resetShelterFilter={this.toggleReset}
                  />
                </Col>
              </Row>
              {!_.isEmpty(shelterList) ? (
                shelterList.map((shelter, i) => <ShelterElement shelter={shelter} key={i} editable={false} />)
              ) : (
                <Row>
                  <Col md="8">
                    <Translate contentKey="serviceNetApp.shelter.empty" />
                  </Col>
                </Row>
              )}
            </Container>
          </InfiniteScroll>
        </Container>
      </div>
    );
  }
}
const mapStateToProps = (storeState: IRootState) => ({
  account: storeState.authentication.account,
  links: storeState.shelter.links,
  totalItems: storeState.shelter.totalItems,
  shelterList: storeState.shelter.entities,
  isAuthenticated: storeState.authentication.isAuthenticated,
  shelterFilter: storeState.filterShelter.shelterFilter
});

const mapDispatchToProps = {
  getSession,
  searchEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Shelters);
