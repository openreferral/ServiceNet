import React from 'react';
import { Button, Col, Container, Row, Collapse, Card, CardBody } from 'reactstrap';
import { Translate } from 'react-jhipster';
import Select from 'react-select';
import { IRootState } from 'app/shared/reducers';
import { initialState, getRegionList, updateShelterFilter } from './filter-shelter.reducer';
import ReactGA from 'react-ga';

import { connect } from 'react-redux';

export interface IFilterShelterState {
  selectedCounty: any;
  shelterFilter: any;
  filtersChanged: boolean;
}

export interface IFilterShelterProps extends StateProps, DispatchProps {
  filterCollapseExpanded: boolean;
  getShelterEntities(): any;
  resetShelterFilter();
}

export class FilterShelter extends React.Component<IFilterShelterProps, IFilterShelterState> {
  state: IFilterShelterState = {
    selectedCounty: this.props.shelterFilter.regionFilterList.map(city => ({ label: city, value: city })),
    shelterFilter: [],
    filtersChanged: false
  };

  componentDidMount() {
    this.getRegionList();
  }
  getRegionList = () => {
    this.props.getRegionList();
  };

  applyFilter = () => {
    ReactGA.event({ category: 'UserActions', action: 'Shelter - Applied Filter' });
    this.props.getShelterEntities().then(() => this.setState({ filtersChanged: false }));
  };

  resetFilter = () => {
    this.setState({
      selectedCounty: initialState.regionList,
      filtersChanged: true
    });

    const regionFilterList = initialState.regionList.map(county => county.value);

    this.props.updateShelterFilter({
      ...this.props.shelterFilter,
      regionFilterList
    });

    this.props.resetShelterFilter();
    ReactGA.event({ category: 'UserActions', action: 'Shelter - Filter Reset' });
  };

  handleCountyChange = selectedCounty => {
    this.setState({ selectedCounty, filtersChanged: true });

    const regionFilterList = selectedCounty.map(county => county.value);

    this.props.updateShelterFilter({ ...this.props.shelterFilter, regionFilterList });
  };

  render() {
    const { filterCollapseExpanded, regionList } = this.props;
    return (
      <div>
        <Collapse isOpen={filterCollapseExpanded} style={{ marginBottom: '1rem' }}>
          <Card>
            <CardBody>
              <Container>
                <Row>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.shelter.home.filter.county" />
                    <Select value={this.state.selectedCounty} onChange={this.handleCountyChange} options={regionList} isMulti />
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
  shelterFilter: storeState.filterShelter.shelterFilter,
  regionList: storeState.filterShelter.regionList.map(region => ({ label: region, value: region }))
});

const mapDispatchToProps = { getRegionList, updateShelterFilter };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FilterShelter);
