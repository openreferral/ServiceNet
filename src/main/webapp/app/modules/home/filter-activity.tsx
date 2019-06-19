import React from 'react';
import { Button, Col, Container, Row, Collapse, Card, CardBody } from 'reactstrap';
import { Translate } from 'react-jhipster';
import Select from 'react-select';
import { IRootState } from 'app/shared/reducers';
import { initialState } from 'app/modules/home/filter-activity.reducer';
import { getPostalCodeList, getRegionList, getCityList, getPartnerList, updateActivityFilter } from './filter-activity.reducer';
import ReactGA from 'react-ga';

import { RouteComponentProps } from 'react-router-dom';
import { connect } from 'react-redux';
import _ from 'lodash';

export interface IFilterActivityState {
  selectedCity: any;
  selectedCounty: any;
  selectedZip: any;
  selectedPartner: any;
  activityFilter: any;
  filtersChanged: boolean;
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
    activityFilter: [],
    filtersChanged: false
  };

  componentDidMount() {
    this.getPostalCodeList();
    this.getRegionList();
    this.getCityList();
    this.getPartnerList();
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

  applyFilter = () => {
    ReactGA.event({ category: 'UserActions', action: 'Applied Filter' });
    this.props.getActivityEntities().then(() => this.setState({ filtersChanged: false }));
  };

  resetFilter = () => {
    this.setState({
      selectedCity: initialState.cityList,
      selectedCounty: initialState.regionList,
      selectedZip: initialState.postalCodeList,
      selectedPartner: initialState.partnerList,
      filtersChanged: true
    });

    const citiesFilterList = initialState.cityList.map(city => city.value);
    const regionFilterList = initialState.regionList.map(county => county.value);
    const postalCodesFilterList = initialState.postalCodeList.map(zip => zip.value);
    const partnerFilterList = initialState.partnerList.map(partner => partner.value);

    this.props.updateActivityFilter({
      ...this.props.activityFilter,
      citiesFilterList,
      regionFilterList,
      postalCodesFilterList,
      partnerFilterList
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

  handlePartnerChange = selectedPartner => {
    this.setState({ selectedPartner, filtersChanged: true });

    const partnerFilterList = selectedPartner;

    this.props.updateActivityFilter({ ...this.props.activityFilter, partnerFilterList });
  };

  render() {
    const { filterCollapseExpanded, postalCodeList, cityList, regionList, partnerList } = this.props;
    return (
      <div>
        <Collapse isOpen={filterCollapseExpanded} style={{ marginBottom: '1rem' }}>
          <Card>
            <CardBody>
              <Container>
                <Row>
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
  regionList: storeState.filterActivity.regionList.map(region => ({ label: region, value: region })),
  cityList: storeState.filterActivity.cityList.map(city => ({ label: city, value: city })),
  partnerList: storeState.filterActivity.partnerList.map(partner => ({ label: partner.name, value: partner.id })),
  activityFilter: storeState.filterActivity.activityFilter
});

const mapDispatchToProps = { getPostalCodeList, getRegionList, getCityList, getPartnerList, updateActivityFilter };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FilterActivity);
