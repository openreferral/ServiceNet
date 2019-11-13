import './shelters.scss';

import React from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { getSortState, IPaginationBaseState, Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import InfiniteScroll from 'react-infinite-scroller';
import { Row, Col, Container, Progress, Spinner } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { searchEntities } from 'app/entities/shelter/shelter.reducer';
import ShelterElement from 'app/modules/shelter/shelter-element';
import _ from 'lodash';
import { getSearchPreferences } from 'app/shared/util/search-utils';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { getSession } from 'app/shared/reducers/authentication';

export interface IMySheltersProp extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IMySheltersState extends IPaginationBaseState {
  dropdownOpen: boolean;
  loggingOut: boolean;
}

export class MyShelters extends React.Component<IMySheltersProp, IMySheltersState> {
  constructor(props) {
    super(props);
    const { shelterSearchPreferences } = getSearchPreferences(this.props.account.login);
    const { sort, order } = shelterSearchPreferences;
    this.state = {
      ...getSortState(this.props.location, ITEMS_PER_PAGE),
      sort,
      order,
      dropdownOpen: false,
      loggingOut: this.props.location.state ? this.props.location.state.loggingOut : false
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
      return this.props.searchEntities('', activePage - 1, itemsPerPage, `${sort},${order}`, { userId: this.props.account.id });
    }
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
              </Row>
              {!_.isEmpty(shelterList) ? (
                shelterList.map((shelter, i) => <ShelterElement shelter={shelter} key={i} editable />)
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
  shelterList: storeState.shelter.myShelters,
  isAuthenticated: storeState.authentication.isAuthenticated,
  shelterFilter: storeState.filterShelter.shelterFilter
});

const mapDispatchToProps = {
  getSession,
  searchEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MyShelters);
