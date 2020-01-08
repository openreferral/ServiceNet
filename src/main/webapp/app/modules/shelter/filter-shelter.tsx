import React, { ComponentClass, StatelessComponent } from 'react';
import { Button, Col, Container, Row, Collapse, Card, CardBody, TabPane, TabContent, Nav, NavItem, NavLink } from 'reactstrap';
import { Translate } from 'react-jhipster';
import Select from 'react-select';
import { IRootState } from 'app/shared/reducers';
import { updateShelterFilter } from './filter-shelter.reducer';
import { initialState, getLanguages, getDefinedCoverageAreas, getTags } from 'app/entities/option/option.reducer';
import ReactGA from 'react-ga';
import { withScriptjs, withGoogleMap, GoogleMap, Marker } from 'react-google-maps';
import { GOOGLE_API_KEY } from 'app/config/constants';

import { connect } from 'react-redux';

export interface IFilterShelterState {
  selectedCounty: any;
  tags: any;
  shelterFilter: any;
  filtersChanged: boolean;
  showOnlyAvailableBeds: boolean;
  activeTab: string;
  lat: number;
  lng: number;
  applyLocationSearch: boolean;
}

export interface IFilterShelterProps extends StateProps, DispatchProps {
  filterCollapseExpanded: boolean;
  getShelterEntities(): any;
  resetShelterFilter();
}

const withLatLong = (
  wrappedComponent: string | ComponentClass<any> | StatelessComponent<any>
): string | React.ComponentClass<any> | React.StatelessComponent<any> => wrappedComponent;

const Map = withScriptjs(
  withGoogleMap(
    withLatLong(props => (
      <GoogleMap
        defaultZoom={8}
        defaultOptions={{ mapTypeControl: false, streetViewControl: false }}
        onClick={props.onClick}
        center={{ lat: props.latitude || 38.5816, lng: props.longitude || -121.4944 }}
      >
        {props.latitude && props.longitude ? <Marker position={{ lat: props.latitude, lng: props.longitude }} /> : null}
      </GoogleMap>
    ))
  )
);
const mapUrl = 'https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=geometry,drawing,places&key=' + GOOGLE_API_KEY;

export class FilterShelter extends React.Component<IFilterShelterProps, IFilterShelterState> {
  state: IFilterShelterState = {
    selectedCounty: this.props.shelterFilter.definedCoverageAreas.map(county => ({ label: county.value, value: county.value })),
    tags: this.props.shelterFilter.tags.map(tag => ({ label: tag.value, value: tag.value })),
    shelterFilter: [],
    showOnlyAvailableBeds: false,
    filtersChanged: false,
    activeTab: 'optionsTab',
    lat: null,
    lng: null,
    applyLocationSearch: false
  };

  componentDidMount() {
    this.props.getDefinedCoverageAreas();
    this.props.getTags();
  }

  applyFilter = () => {
    ReactGA.event({ category: 'UserActions', action: 'Shelter - Applied Filter' });
    this.props.getShelterEntities();
  };

  resetFilter = () => {
    this.setState({
      selectedCounty: [],
      tags: [],
      showOnlyAvailableBeds: false,
      filtersChanged: true
    });

    const definedCoverageAreas = initialState.definedCoverageAreas.map(county => county.value);
    const tags = initialState.tags.map(tag => tag.value);

    this.props.updateShelterFilter({
      ...this.props.shelterFilter,
      definedCoverageAreas,
      tags,
      showOnlyAvailableBeds: false,
      applyLocationSearch: false,
      latitude: null,
      longitude: null,
      radius: 1
    });

    this.props.resetShelterFilter();
    ReactGA.event({ category: 'UserActions', action: 'Shelter - Filter Reset' });
  };

  handleCountyChange = selectedCounty => {
    this.setState({ selectedCounty, filtersChanged: true });

    const definedCoverageAreas = selectedCounty.map(county => county.value);

    this.props.updateShelterFilter({ ...this.props.shelterFilter, definedCoverageAreas });
  };

  handleShowOnlyAvailableBedsChange = event => {
    const showOnlyAvailableBeds = !this.state.showOnlyAvailableBeds;
    this.setState({ showOnlyAvailableBeds, filtersChanged: true });
    this.props.updateShelterFilter({ ...this.props.shelterFilter, showOnlyAvailableBeds });
  };

  handleTagChange = value => event => {
    const tags = this.state.tags;
    const tagIndex = tags.indexOf(value);
    if (tagIndex > -1) {
      tags.splice(tagIndex, 1);
    } else {
      tags.push(value);
    }
    this.setState({ tags, filtersChanged: true });
    this.props.updateShelterFilter({ ...this.props.shelterFilter, tags });
  };

  isTagSelected = value => this.state.tags.indexOf(value) > -1;

  getRadiusValue = value => {
    const labels = {
      1: '1 mile',
      2: '2 miles',
      3: '3 miles',
      4: '4 miles',
      5: '5 miles',
      10: '10 miles',
      20: '20 miles'
    };

    if (!value) {
      return null;
    }

    return { value, label: labels[value] };
  };

  optionsTab = () => this.changeTab('optionsTab');

  mapTab = () => this.changeTab('mapTab');

  changeTab = tab => {
    if (this.state.activeTab !== tab) this.setState({ activeTab: tab });
  };

  selectLocation = ({ latLng }) => {
    const lat = latLng.lat();
    const lng = latLng.lng();
    this.setState({ lat, lng });
    this.handleLocationChange({ lat, lng });
  };

  getMyCurrentLocation = () => {
    const myPosition = navigator.geolocation.getCurrentPosition(this.setCurrentLocation);
  };

  setCurrentLocation = position => {
    this.setState({ lat: position.coords.latitude, lng: position.coords.longitude });

    this.handleLocationChange({ lat: position.coords.latitude, lng: position.coords.longitude });
  };

  applyLocationSearch = changeEvent => {
    const applyLocationSearch = changeEvent.target.checked;

    this.setState({ filtersChanged: true });

    this.props.updateShelterFilter({ ...this.props.shelterFilter, applyLocationSearch });
  };

  handleRadiusChange = radius => {
    this.setState({ filtersChanged: true });

    this.props.updateShelterFilter({ ...this.props.shelterFilter, radius: radius.value });
  };

  handleLocationChange = ({ lat, lng }) => {
    this.setState({ filtersChanged: true });

    this.props.updateShelterFilter({ ...this.props.shelterFilter, latitude: lat, longitude: lng });
  };

  render() {
    const { filterCollapseExpanded, definedCoverageAreas, tags } = this.props;
    const { activeTab } = this.state;
    const radiusOptions = [1, 2, 3, 4, 5, 10, 20].map(number => ({ label: `${number} mile${number > 1 ? 's' : ''}`, value: number }));

    return (
      <div>
        <Collapse isOpen={filterCollapseExpanded} style={{ marginBottom: '1rem' }}>
          <Card>
            <CardBody>
              <Container>
                <Nav tabs>
                  <NavItem>
                    <NavLink className={`filters-tab ${activeTab === 'optionsTab' ? 'active' : ''}`} onClick={this.optionsTab}>
                      <Translate contentKey="serviceNetApp.activity.home.filter.filterOptions" />
                    </NavLink>
                  </NavItem>
                  <NavItem>
                    <NavLink
                      className={`filters-tab ${activeTab === 'mapTab' ? 'active' : ''} ${
                        this.props.applyLocationSearch && !this.props.latitude && !this.props.longitude ? 'missing-loc' : ''
                      }`}
                      onClick={this.mapTab}
                    >
                      <Translate contentKey="serviceNetApp.activity.home.filter.locationChooser" />
                    </NavLink>
                  </NavItem>
                </Nav>
                <TabContent activeTab={activeTab}>
                  <TabPane tabId="optionsTab">
                    <Row className="mt-2">
                      <Col md="3">
                        <Translate contentKey="serviceNetApp.shelter.home.filter.county" />
                        <Select
                          value={this.state.selectedCounty}
                          onChange={this.handleCountyChange}
                          options={definedCoverageAreas}
                          isMulti
                        />
                      </Col>
                      <Col md="3">
                        <Translate contentKey="serviceNetApp.shelter.home.filter.showOnlyAvailableBeds" />
                        <input
                          checked={this.state.showOnlyAvailableBeds}
                          type="checkbox"
                          onChange={this.handleShowOnlyAvailableBedsChange}
                        />
                      </Col>
                      <Col md="3">
                        <Translate contentKey="serviceNetApp.shelter.home.filter.onlyShowSheltersThat" />
                        {tags
                          ? tags.map(tag => (
                              <div key={tag.value}>
                                <input checked={this.isTagSelected(tag.value)} onChange={this.handleTagChange(tag.value)} type="checkbox" />
                                <span className="checkbox-label">{tag.value}</span>
                              </div>
                            ))
                          : null}
                      </Col>
                      <Col md="3">
                        <div className="form-check form-check-inline float-right">
                          <input
                            type="checkbox"
                            id="applyLocationSearch"
                            className="form-check-input"
                            onChange={this.applyLocationSearch}
                            checked={this.props.applyLocationSearch}
                          />
                          <label className="form-check-label" htmlFor="applyLocationSearch">
                            <Translate contentKey="serviceNetApp.activity.home.filter.applyLocationSearch" />
                          </label>
                        </div>
                      </Col>
                    </Row>
                    <Row>
                      <Col md={{ size: 2, offset: 10 }}>
                        <Button
                          color="primary"
                          onClick={this.applyFilter}
                          disabled={!this.state.filtersChanged}
                          style={{ marginTop: '1rem' }}
                          block
                        >
                          <Translate contentKey="serviceNetApp.activity.home.filter.applyFilter" />
                        </Button>
                      </Col>
                      <Col md={{ size: 2, offset: 10 }}>
                        <Button
                          color="primary"
                          onClick={this.resetFilter}
                          disabled={!this.state.filtersChanged}
                          style={{ marginTop: '1rem' }}
                          block
                        >
                          <Translate contentKey="serviceNetApp.activity.home.filter.resetFilter" />
                        </Button>
                      </Col>
                    </Row>
                  </TabPane>
                  <TabPane tabId="mapTab">
                    <Row className="my-2">
                      <Col>
                        <Button className="btn btn-primary" onClick={this.getMyCurrentLocation}>
                          <Translate contentKey="serviceNetApp.activity.home.filter.setMyCurrentLocation" />
                        </Button>
                      </Col>
                      <Col md="4" className="d-flex align-items-center">
                        <Translate contentKey="serviceNetApp.activity.home.filter.radius" />
                        <Select
                          value={this.getRadiusValue(this.props.radius)}
                          onChange={this.handleRadiusChange}
                          options={radiusOptions}
                          className="flex-fill ml-2"
                        />
                      </Col>
                    </Row>
                    <Row>
                      <Col>
                        <Map
                          googleMapURL={mapUrl}
                          latitude={this.props.latitude}
                          longitude={this.props.longitude}
                          loadingElement={<div style={{ height: `100%` }} />}
                          containerElement={<div style={{ height: `400px` }} />}
                          mapElement={<div style={{ height: `100%` }} />}
                          onClick={this.selectLocation}
                        />
                      </Col>
                    </Row>
                  </TabPane>
                </TabContent>
              </Container>
            </CardBody>
          </Card>
        </Collapse>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  shelterFilter: storeState.filterShelter.shelterFilter,
  definedCoverageAreas: storeState.option.definedCoverageAreas.map(region => ({ label: region.value, value: region.value })),
  tags: storeState.option.tags,
  applyLocationSearch: storeState.filterShelter.shelterFilter.applyLocationSearch,
  latitude: storeState.filterShelter.shelterFilter.latitude,
  longitude: storeState.filterShelter.shelterFilter.longitude,
  radius: storeState.filterShelter.shelterFilter.radius
});

const mapDispatchToProps = { getLanguages, getDefinedCoverageAreas, getTags, updateShelterFilter };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FilterShelter);
