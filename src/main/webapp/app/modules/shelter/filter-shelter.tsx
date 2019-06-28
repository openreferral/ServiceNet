import React from 'react';
import { Button, Col, Container, Row, Collapse, Card, CardBody } from 'reactstrap';
import { Translate } from 'react-jhipster';
import Select from 'react-select';
import { IRootState } from 'app/shared/reducers';
import { updateShelterFilter } from './filter-shelter.reducer';
import { initialState, getLanguages, getDefinedCoverageAreas, getTags } from 'app/entities/option/option.reducer';
import ReactGA from 'react-ga';

import { connect } from 'react-redux';

export interface IFilterShelterState {
  selectedCounty: any;
  tags: any;
  shelterFilter: any;
  filtersChanged: boolean;
  showOnlyAvailableBeds: boolean;
}

export interface IFilterShelterProps extends StateProps, DispatchProps {
  filterCollapseExpanded: boolean;
  getShelterEntities(): any;
  resetShelterFilter();
}

export class FilterShelter extends React.Component<IFilterShelterProps, IFilterShelterState> {
  state: IFilterShelterState = {
    selectedCounty: this.props.shelterFilter.definedCoverageAreas.map(county => ({ label: county.value, value: county.value })),
    tags: this.props.shelterFilter.tags.map(tag => ({ label: tag.value, value: tag.value })),
    shelterFilter: [],
    showOnlyAvailableBeds: false,
    filtersChanged: false
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
      showOnlyAvailableBeds: false
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

  render() {
    const { filterCollapseExpanded, definedCoverageAreas, tags } = this.props;
    return (
      <div>
        <Collapse isOpen={filterCollapseExpanded} style={{ marginBottom: '1rem' }}>
          <Card>
            <CardBody>
              <Container>
                <Row>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.shelter.home.filter.county" />
                    <Select value={this.state.selectedCounty} onChange={this.handleCountyChange} options={definedCoverageAreas} isMulti />
                  </Col>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.shelter.home.filter.showOnlyAvailableBeds" />
                    <input checked={this.state.showOnlyAvailableBeds} type="checkbox" onChange={this.handleShowOnlyAvailableBedsChange} />
                  </Col>
                  <Col md="3">
                    <Translate contentKey="serviceNetApp.shelter.home.filter.onlyShowSheltersThat" />
                    {tags
                      ? tags.map(tag => (
                          <div>
                            <input checked={this.isTagSelected(tag.value)} onChange={this.handleTagChange(tag.value)} type="checkbox" />
                            <span className="checkbox-label">{tag.value}</span>
                          </div>
                        ))
                      : null}
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
  definedCoverageAreas: storeState.option.definedCoverageAreas.map(region => ({ label: region.value, value: region.value })),
  tags: storeState.option.tags
});

const mapDispatchToProps = { getLanguages, getDefinedCoverageAreas, getTags, updateShelterFilter };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FilterShelter);
