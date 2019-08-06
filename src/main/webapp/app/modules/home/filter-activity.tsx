import React from 'react';
import { Button, Col, Container, Row, Collapse, Card, CardBody, Input } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import Select from 'react-select';
import { IRootState } from 'app/shared/reducers';
import {
  getPostalCodeList,
  getRegionList,
  getCityList,
  getPartnerList,
  updateActivityFilter,
  getTaxonomyList
} from './filter-activity.reducer';
import ReactGA from 'react-ga';
import { connect } from 'react-redux';
import { ORGANIZATION, SERVICES, LOCATIONS, getSearchFieldOptions, getDefaultSearchFieldOptions } from 'app/modules/home/filter.constants';

export interface IFilterActivityState {
  selectedCity: any;
  selectedCounty: any;
  selectedZip: any;
  selectedPartner: any;
  selectedTaxonomy: any;
  selectedSearchFields: any;
  filtersChanged: boolean;
  searchOn: string;
  dateFilter: any;
  fromDate: any;
  toDate: any;
}

export interface IFilterActivityProps extends StateProps, DispatchProps {
  filterCollapseExpanded: boolean;
  getActivityEntities(): any;
  resetActivityFilter();
}

export class FilterActivity extends React.Component<IFilterActivityProps, IFilterActivityState> {
  state: IFilterActivityState = {
    selectedCity: this.props.activityFilter.citiesFilterList.map(city => ({ label: city, value: city })),
    selectedCounty: this.props.activityFilter.regionFilterList.map(city => ({ label: city, value: city })),
    selectedZip: this.props.activityFilter.postalCodesFilterList.map(city => ({ label: city, value: city })),
    selectedPartner: this.props.activityFilter.partnerFilterList.map(city => ({ label: city.label, value: city.value })),
    selectedTaxonomy: this.props.activityFilter.taxonomiesFilterList.map(taxonomy => ({ label: taxonomy, value: taxonomy })),
    selectedSearchFields: this.props.activityFilter.searchFields.map(taxonomy => ({ label: taxonomy, value: taxonomy })),
    filtersChanged: false,
    searchOn: this.props.activityFilter.searchOn,
    dateFilter: this.props.activityFilter.dateFilter,
    fromDate: this.props.activityFilter.fromDate,
    toDate: this.props.activityFilter.toDate
  };

  componentDidMount() {
    if (!this.props.isLoggingOut) {
      this.getPostalCodeList();
      this.getRegionList();
      this.getCityList();
      this.getPartnerList();
      this.getTaxonomyList();
    }
    if (this.props.isLoggingOut || this.props.hasSessionBeenFetched) {
      this.resetFilter();
    }
  }

  getPartnerList = () => {
    this.props.getPartnerList();
  };
  getPostalCodeList = () => {
    this.props.getPostalCodeList();
  };
  getRegionList = () => {
    this.props.getRegionList();
  };
  getCityList = () => {
    this.props.getCityList();
  };
  getTaxonomyList = () => {
    this.props.getTaxonomyList();
  };

  getDateFilterList = () => [
    { value: 'LAST_7_DAYS', label: translate('serviceNetApp.activity.home.filter.date.last7Days') },
    { value: 'LAST_30_DAYS', label: translate('serviceNetApp.activity.home.filter.date.last30Days') },
    { value: 'DATE_RANGE', label: translate('serviceNetApp.activity.home.filter.date.dateRange') }
  ];

  applyFilter = () => {
    ReactGA.event({ category: 'UserActions', action: 'Applied Filter' });
    this.props.getActivityEntities().then(() => this.setState({ filtersChanged: false }));
  };

  resetFilter = () => {
    const searchFieldOptions = getDefaultSearchFieldOptions();
    this.setState({
      selectedCity: [],
      selectedCounty: [],
      selectedZip: [],
      selectedPartner: [],
      selectedTaxonomy: [],
      selectedSearchFields: searchFieldOptions,
      filtersChanged: true,
      searchOn: ORGANIZATION,
      dateFilter: null,
      fromDate: '',
      toDate: ''
    });

    this.props.updateActivityFilter({
      ...this.props.activityFilter,
      citiesFilterList: [],
      regionFilterList: [],
      postalCodesFilterList: [],
      partnerFilterList: [],
      taxonomiesFilterList: [],
      searchFields: searchFieldOptions.map(o => o.value),
      searchOn: ORGANIZATION,
      dateFilter: null,
      fromDate: '',
      toDate: ''
    });

    this.props.resetActivityFilter();
    ReactGA.event({ category: 'UserActions', action: 'Filter Reset' });
  };

  handleCityChange = selectedCity => {
    this.setState({ selectedCity, filtersChanged: true });

    const citiesFilterList = selectedCity.map(city => city.value);

    this.props.updateActivityFilter({ ...this.props.activityFilter, citiesFilterList });
  };

  handleCountyChange = selectedCounty => {
    this.setState({ selectedCounty, filtersChanged: true });

    const regionFilterList = selectedCounty.map(county => county.value);

    this.props.updateActivityFilter({ ...this.props.activityFilter, regionFilterList });
  };

  handleZipChange = selectedZip => {
    this.setState({ selectedZip, filtersChanged: true });

    const postalCodesFilterList = selectedZip.map(zip => zip.value);

    this.props.updateActivityFilter({ ...this.props.activityFilter, postalCodesFilterList });
  };

  handleTaxonomyChange = selectedTaxonomy => {
    this.setState({ selectedTaxonomy, filtersChanged: true });

    const taxonomiesFilterList = selectedTaxonomy.map(taxonomy => taxonomy.value);

    this.props.updateActivityFilter({ ...this.props.activityFilter, taxonomiesFilterList });
  };

  handleSearchFieldsChange = selectedSearchFields => {
    this.setState({ selectedSearchFields, filtersChanged: true });

    const searchFields = selectedSearchFields.map(f => f.value);

    this.props.updateActivityFilter({ ...this.props.activityFilter, searchFields });
  };

  handlePartnerChange = selectedPartner => {
    this.setState({ selectedPartner, filtersChanged: true });

    const partnerFilterList = selectedPartner;

    this.props.updateActivityFilter({ ...this.props.activityFilter, partnerFilterList });
  };

  handleSearchOnChange = changeEvent => {
    const searchOn = changeEvent.target.value;
    const selectedSearchFields = getDefaultSearchFieldOptions();

    this.setState({ searchOn, selectedSearchFields, filtersChanged: true });

    const searchFields = selectedSearchFields.map(f => f.value);
    this.props.updateActivityFilter({ ...this.props.activityFilter, searchOn, searchFields });
  };

  handleDateFilterChange = dateFilter => {
    this.setState({ dateFilter, filtersChanged: true });

    this.props.updateActivityFilter({ ...this.props.activityFilter, dateFilter: dateFilter.value });
  };

  handleFromDateChange = changeEvent => {
    const fromDate = changeEvent.target.value;

    this.setState({ fromDate, filtersChanged: true });

    this.props.updateActivityFilter({ ...this.props.activityFilter, fromDate });
  };

  handleToDateChange = changeEvent => {
    const toDate = changeEvent.target.value;

    this.setState({ toDate, filtersChanged: true });

    this.props.updateActivityFilter({ ...this.props.activityFilter, toDate });
  };

  render() {
    const { filterCollapseExpanded, postalCodeList, cityList, regionList, partnerList, taxonomyList } = this.props;
    const searchFieldList = getSearchFieldOptions(this.state.searchOn);
    return (
      <div>
        <Collapse isOpen={filterCollapseExpanded} style={{ marginBottom: '1rem' }}>
          <Card>
            <CardBody>
              <Container>
                <Row>
                  <Col md="12">
                    <div className="form-check form-check-inline">
                      <Translate contentKey="serviceNetApp.activity.home.filter.searchOn" />:
                    </div>
                    <div className="form-check form-check-inline">
                      <input
                        type="radio"
                        id="orgRadio"
                        name="search-on"
                        value={ORGANIZATION}
                        className="form-check-input"
                        onChange={this.handleSearchOnChange}
                        checked={this.state.searchOn === ORGANIZATION}
                      />
                      <label className="form-check-label" htmlFor="orgRadio">
                        <Translate contentKey="serviceNetApp.activity.home.filter.organization" />
                      </label>
                    </div>
                    <div className="form-check form-check-inline">
                      <input
                        type="radio"
                        id="svcRadio"
                        name="search-on"
                        value={SERVICES}
                        className="form-check-input"
                        onChange={this.handleSearchOnChange}
                        checked={this.state.searchOn === SERVICES}
                      />
                      <label className="form-check-label" htmlFor="svcRadio">
                        <Translate contentKey="serviceNetApp.activity.home.filter.services" />
                      </label>
                    </div>
                    <div className="form-check form-check-inline">
                      <input
                        type="radio"
                        id="locRadio"
                        name="search-on"
                        value={LOCATIONS}
                        className="form-check-input"
                        onChange={this.handleSearchOnChange}
                        checked={this.state.searchOn === LOCATIONS}
                      />
                      <label className="form-check-label" htmlFor="locRadio">
                        <Translate contentKey="serviceNetApp.activity.home.filter.locations" />
                      </label>
                    </div>
                    <div>
                      <Translate contentKey="serviceNetApp.activity.home.filter.searchFields" />
                      <Select
                        value={this.state.selectedSearchFields}
                        onChange={this.handleSearchFieldsChange}
                        options={searchFieldList}
                        isMulti
                      />
                    </div>
                  </Col>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.city" />
                    <Select value={this.state.selectedCity} onChange={this.handleCityChange} options={cityList} isMulti />
                  </Col>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.county" />
                    <Select value={this.state.selectedCounty} onChange={this.handleCountyChange} options={regionList} isMulti />
                  </Col>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.zip" />
                    <Select value={this.state.selectedZip} onChange={this.handleZipChange} options={postalCodeList} isMulti />
                  </Col>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.partner" />
                    <Select value={this.state.selectedPartner} onChange={this.handlePartnerChange} options={partnerList} isMulti />
                  </Col>
                </Row>
                <Row>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.taxonomy" />
                    <Select value={this.state.selectedTaxonomy} onChange={this.handleTaxonomyChange} options={taxonomyList} isMulti />
                  </Col>
                </Row>
                <Row>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.dateFilter" />
                    <Select value={this.state.dateFilter} onChange={this.handleDateFilterChange} options={this.getDateFilterList()} />
                  </Col>
                  {!this.state.dateFilter || this.state.dateFilter.value !== 'DATE_RANGE'
                    ? null
                    : [
                        <Col key="fromDate" md="3">
                          <Translate contentKey="serviceNetApp.activity.home.filter.from" />
                          <Input
                            type="date"
                            value={this.state.fromDate}
                            onChange={this.handleFromDateChange}
                            name="fromDate"
                            id="fromDate"
                          />
                        </Col>,
                        <Col key="toDate" md="3">
                          <Translate contentKey="serviceNetApp.activity.home.filter.to" />
                          <Input type="date" value={this.state.toDate} onChange={this.handleToDateChange} name="toDate" id="toDate" />
                        </Col>
                      ]}
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
              </Container>
            </CardBody>
          </Card>
        </Collapse>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  postalCodeList: storeState.filterActivity.postalCodeList.map(code => ({ label: code, value: code })),
  isLoggingOut: storeState.authentication.loggingOut,
  hasSessionBeenFetched: storeState.authentication.sessionHasBeenFetched,
  regionList: storeState.filterActivity.regionList.map(region => ({ label: region, value: region })),
  cityList: storeState.filterActivity.cityList.map(city => ({ label: city, value: city })),
  taxonomyList: storeState.filterActivity.taxonomyList.map(taxonomy => ({ label: taxonomy, value: taxonomy })),
  partnerList: storeState.filterActivity.partnerList.map(partner => ({ label: partner.name, value: partner.id })),
  activityFilter: storeState.filterActivity.activityFilter
});

const mapDispatchToProps = { getPostalCodeList, getRegionList, getCityList, getPartnerList, getTaxonomyList, updateActivityFilter };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FilterActivity);
