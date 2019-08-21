import React from 'react';
import { Button, Col, Container, Row, Collapse, Card, CardBody, Input } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import Select from 'react-select';
import axios from 'axios';
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
  filtersChanged: boolean;
}

export interface IFilterActivityProps extends StateProps, DispatchProps {
  filterCollapseExpanded: boolean;
  getActivityEntities(any): any;
  resetActivityFilter();
}

export class FilterActivity extends React.Component<IFilterActivityProps, IFilterActivityState> {
  state: IFilterActivityState = {
    filtersChanged: false
  };

  componentDidMount() {
    if (!this.props.isLoggingOut) {
      this.getPostalCodeList();
      this.getRegionList();
      this.getCityList();
      this.getPartnerList();
      this.getTaxonomyList();
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

  getDateFilterValue = value => {
    const labels = {
      LAST_7_DAYS: translate('serviceNetApp.activity.home.filter.date.last7Days'),
      LAST_30_DAYS: translate('serviceNetApp.activity.home.filter.date.last30Days'),
      DATE_RANGE: translate('serviceNetApp.activity.home.filter.date.dateRange')
    };

    if (!value) {
      return null;
    }

    return { value, label: labels[value] };
  };

  applyFilter = () => {
    ReactGA.event({ category: 'UserActions', action: 'Applied Filter' });
    this.props.getActivityEntities(null);
    this.setState({ filtersChanged: false });

    this.saveCurrentFilter({ ...this.props.activityFilter, hiddenFilter: false });
  };

  resetFilter = () => {
    const searchFieldOptions = getDefaultSearchFieldOptions();
    const filter = {
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
      toDate: '',
      showPartner: false
    };

    this.props.updateActivityFilter(filter);

    this.props.resetActivityFilter();
    ReactGA.event({ category: 'UserActions', action: 'Filter Reset' });
    this.saveCurrentFilter(filter);
  };

  saveCurrentFilter = filter => {
    const url = 'api/activity-filter/current-user-filter';

    axios.post(url, filter);
  };

  handleCityChange = selectedCity => {
    this.setState({ filtersChanged: true });

    const citiesFilterList = selectedCity.map(city => city.value);

    this.props.updateActivityFilter({ ...this.props.activityFilter, citiesFilterList });
  };

  handleCountyChange = selectedCounty => {
    this.setState({ filtersChanged: true });

    const regionFilterList = selectedCounty.map(county => county.value);

    this.props.updateActivityFilter({ ...this.props.activityFilter, regionFilterList });
  };

  handleZipChange = selectedZip => {
    this.setState({ filtersChanged: true });

    const postalCodesFilterList = selectedZip.map(zip => zip.value);

    this.props.updateActivityFilter({ ...this.props.activityFilter, postalCodesFilterList });
  };

  handleTaxonomyChange = selectedTaxonomy => {
    this.setState({ filtersChanged: true });

    const taxonomiesFilterList = selectedTaxonomy.map(taxonomy => taxonomy.value);

    this.props.updateActivityFilter({ ...this.props.activityFilter, taxonomiesFilterList });
  };

  handleSearchFieldsChange = selectedSearchFields => {
    this.setState({ filtersChanged: true });

    const searchFields = selectedSearchFields.map(f => f.value);

    this.props.updateActivityFilter({ ...this.props.activityFilter, searchFields });
  };

  handlePartnerChange = selectedPartner => {
    this.setState({ filtersChanged: true });

    const partnerFilterList = selectedPartner;

    this.props.updateActivityFilter({ ...this.props.activityFilter, partnerFilterList });
  };

  handleSearchOnChange = changeEvent => {
    const searchOn = changeEvent.target.value;
    const selectedSearchFields = getDefaultSearchFieldOptions();

    this.setState({ filtersChanged: true });

    const searchFields = selectedSearchFields.map(f => f.value);
    this.props.updateActivityFilter({ ...this.props.activityFilter, searchOn, searchFields });
  };

  handleDateFilterChange = dateFilter => {
    this.setState({ filtersChanged: true });

    this.props.updateActivityFilter({ ...this.props.activityFilter, dateFilter: dateFilter.value });
  };

  handleFromDateChange = changeEvent => {
    const fromDate = changeEvent.target.value;

    this.setState({ filtersChanged: true });

    this.props.updateActivityFilter({ ...this.props.activityFilter, fromDate });
  };

  handleToDateChange = changeEvent => {
    const toDate = changeEvent.target.value;

    this.setState({ filtersChanged: true });

    this.props.updateActivityFilter({ ...this.props.activityFilter, toDate });
  };

  handleOnlyShowMatchingChange = changeEvent => {
    const onlyShowMatching = changeEvent.target.checked;

    this.setState({ filtersChanged: true });

    this.props.updateActivityFilter({ ...this.props.activityFilter, showPartner: !onlyShowMatching });
  };

  render() {
    const { filterCollapseExpanded, postalCodeList, cityList, regionList, partnerList, taxonomyList } = this.props;
    const searchFieldList = getSearchFieldOptions(this.props.searchOn);
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
                        checked={this.props.searchOn === ORGANIZATION}
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
                        checked={this.props.searchOn === SERVICES}
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
                        checked={this.props.searchOn === LOCATIONS}
                      />
                      <label className="form-check-label" htmlFor="locRadio">
                        <Translate contentKey="serviceNetApp.activity.home.filter.locations" />
                      </label>
                    </div>
                    <div>
                      <Translate contentKey="serviceNetApp.activity.home.filter.searchFields" />
                      <Select
                        value={this.props.selectedSearchFields}
                        onChange={this.handleSearchFieldsChange}
                        options={searchFieldList}
                        isMulti
                      />
                    </div>
                  </Col>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.city" />
                    <Select value={this.props.selectedCity} onChange={this.handleCityChange} options={cityList} isMulti />
                  </Col>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.county" />
                    <Select value={this.props.selectedCounty} onChange={this.handleCountyChange} options={regionList} isMulti />
                  </Col>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.zip" />
                    <Select value={this.props.selectedZip} onChange={this.handleZipChange} options={postalCodeList} isMulti />
                  </Col>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.partner" />
                    <Select value={this.props.selectedPartner} onChange={this.handlePartnerChange} options={partnerList} isMulti />
                    <div className="form-check form-check-inline">
                      <input
                        type="checkbox"
                        id="onlyShowMatchingCheckbox"
                        className="form-check-input"
                        onChange={this.handleOnlyShowMatchingChange}
                        checked={this.props.onlyShowMatching}
                      />
                      <label className="form-check-label" htmlFor="onlyShowMatchingCheckbox">
                        <Translate contentKey="serviceNetApp.activity.home.filter.onlyShowMatching" />
                      </label>
                    </div>
                  </Col>
                </Row>
                <Row>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.taxonomy" />
                    <Select value={this.props.selectedTaxonomy} onChange={this.handleTaxonomyChange} options={taxonomyList} isMulti />
                  </Col>
                </Row>
                <Row>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.activity.home.filter.dateFilter" />
                    <Select
                      value={this.getDateFilterValue(this.props.dateFilter)}
                      onChange={this.handleDateFilterChange}
                      options={this.getDateFilterList()}
                    />
                  </Col>
                  {this.props.dateFilter !== 'DATE_RANGE'
                    ? null
                    : [
                        <Col key="fromDate" md="3">
                          <Translate contentKey="serviceNetApp.activity.home.filter.from" />
                          <Input
                            type="date"
                            value={this.props.fromDate}
                            onChange={this.handleFromDateChange}
                            name="fromDate"
                            id="fromDate"
                          />
                        </Col>,
                        <Col key="toDate" md="3">
                          <Translate contentKey="serviceNetApp.activity.home.filter.to" />
                          <Input type="date" value={this.props.toDate} onChange={this.handleToDateChange} name="toDate" id="toDate" />
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
                    <Button color="primary" onClick={this.resetFilter} style={{ marginTop: '1rem' }} block>
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
  regionList: storeState.filterActivity.regionList.map(region => ({ label: region, value: region })),
  cityList: storeState.filterActivity.cityList.map(city => ({ label: city, value: city })),
  taxonomyList: storeState.filterActivity.taxonomyList.map(taxonomy => ({ label: taxonomy, value: taxonomy })),
  partnerList: storeState.filterActivity.partnerList.map(partner => ({ label: partner.name, value: partner.id })),
  activityFilter: storeState.filterActivity.activityFilter,
  selectedCity: storeState.filterActivity.activityFilter.citiesFilterList.map(city => ({ label: city, value: city })),
  selectedCounty: storeState.filterActivity.activityFilter.regionFilterList.map(county => ({ label: county, value: county })),
  selectedZip: storeState.filterActivity.activityFilter.postalCodesFilterList.map(code => ({ label: code, value: code })),
  selectedPartner: storeState.filterActivity.activityFilter.partnerFilterList.map(partner => ({
    label: partner.label,
    value: partner.value
  })),
  selectedTaxonomy: storeState.filterActivity.activityFilter.taxonomiesFilterList.map(taxonomy => ({ label: taxonomy, value: taxonomy })),
  selectedSearchFields: storeState.filterActivity.activityFilter.searchFields.map(field => ({ label: field, value: field })),
  searchOn: storeState.filterActivity.activityFilter.searchOn,
  dateFilter: storeState.filterActivity.activityFilter.dateFilter,
  fromDate: storeState.filterActivity.activityFilter.fromDate,
  toDate: storeState.filterActivity.activityFilter.toDate,
  onlyShowMatching: !storeState.filterActivity.activityFilter.showPartner
});

const mapDispatchToProps = { getPostalCodeList, getRegionList, getCityList, getPartnerList, getTaxonomyList, updateActivityFilter };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FilterActivity);
