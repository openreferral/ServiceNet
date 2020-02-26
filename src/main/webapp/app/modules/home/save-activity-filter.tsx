import React from 'react';
import { connect } from 'react-redux';
import { Button, Col, Container, Row, Collapse, Card, CardBody, Input } from 'reactstrap';
import { translate, Translate } from 'react-jhipster';
import Select from 'react-select';
import axios from 'axios';
import { toast } from 'react-toastify';
import { IRootState } from 'app/shared/reducers';
import { updateActivityFilter, getSavedFilters } from './filter-activity.reducer';
import ActivityFilterDeleteConfirmation from 'app/modules/home/activity-filter-delete-confirmation';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export interface ISaveActivityFilterState {
  selectedFilter: any;
  filterName: string;
  deleteDialogOpen: boolean;
}

export interface ISaveActivityFilterProps extends StateProps, DispatchProps {
  saveFilterExpanded: boolean;
  getActivityEntities(any): any;
}

export class SaveActivityFilter extends React.Component<ISaveActivityFilterProps, ISaveActivityFilterState> {
  state: ISaveActivityFilterState = {
    selectedFilter: null,
    filterName: '',
    deleteDialogOpen: false
  };

  componentDidMount() {
    if (!this.props.isLoggingOut) {
      this.props.getSavedFilters();
    }
  }

  saveFilter = () => {
    const url = SERVICENET_API_URL + '/activity-filter/save-user-filter';
    const filter = { ...this.props.activityFilter, name: this.state.filterName, hiddenFilter: false };

    axios
      .post(url, filter)
      .then(() => {
        this.setState({ filterName: '' });
        toast.success(translate('serviceNetApp.activity.home.filter.saveSuccess'));
        this.props.getSavedFilters();
      })
      .catch(error => {
        if (error && error.response && error.response.data) {
          toast.error(translate(error.response.data.message));
        } else {
          toast.error(translate('serviceNetApp.activity.home.filter.saveError'));
        }
      });
  };

  deleteFilter = () => {
    this.setState({
      deleteDialogOpen: true
    });
  };

  handleDeleteDialogClose = () => {
    this.setState({
      deleteDialogOpen: false
    });
  };

  handleConfirmDelete = activityFilterId => {
    const url = `${SERVICENET_API_URL}/activity-filters/${activityFilterId}`;

    axios
      .delete(url)
      .then(() => {
        toast.success(translate('serviceNetApp.activity.home.filter.deleteSuccess'));
        this.props.getSavedFilters();
        this.setState({
          selectedFilter: null,
          deleteDialogOpen: false
        });
      })
      .catch(error => {
        if (error && error.response && error.response.data) {
          toast.error(translate(error.response.data.message));
        } else {
          toast.error(translate('serviceNetApp.activity.home.filter.deleteError'));
        }
        this.setState({
          deleteDialogOpen: false
        });
      });
  };

  saveCurrentFilter = filter => {
    const url = SERVICENET_API_URL + '/activity-filter/current-user-filter';

    axios.post(url, filter);
  };

  handleFilterChange = selectedFilter => {
    this.setState({ selectedFilter, filterName: '' });

    this.props.updateActivityFilter(selectedFilter.value);
    this.props.getActivityEntities(selectedFilter.value);
    this.saveCurrentFilter(selectedFilter.value);
  };

  handleFilterNameChange = event => {
    this.setState({ filterName: event.target.value });
  };

  render() {
    const { saveFilterExpanded, savedFilters } = this.props;

    return (
      <div>
        <Collapse isOpen={saveFilterExpanded} style={{ marginBottom: '1rem' }}>
          <Card>
            <CardBody>
              <Container>
                <Row>
                  <Col md="6">
                    <Translate contentKey="serviceNetApp.activity.home.filter.savedFilters" />
                    <Select value={this.state.selectedFilter} onChange={this.handleFilterChange} options={savedFilters} />
                  </Col>
                  <Col md="6">
                    <Translate contentKey="serviceNetApp.activity.home.filter.filterName" />
                    <Input type="text" className="form-control" value={this.state.filterName} onChange={this.handleFilterNameChange} />
                  </Col>
                </Row>
                <Row>
                  <Col md={{ size: 2 }}>
                    <Button
                      color="danger"
                      onClick={this.deleteFilter}
                      disabled={!this.state.selectedFilter}
                      style={{ marginTop: '1rem' }}
                      block
                    >
                      <Translate contentKey="serviceNetApp.activity.home.filter.deleteFilter" />
                      {this.state.deleteDialogOpen && (
                        <ActivityFilterDeleteConfirmation
                          selectedFilter={this.state.selectedFilter.value}
                          handleClose={this.handleDeleteDialogClose}
                          handleConfirmDelete={this.handleConfirmDelete}
                        />
                      )}
                    </Button>
                  </Col>
                  <Col md={{ size: 2, offset: 8 }}>
                    <Button color="primary" onClick={this.saveFilter} disabled={!this.state.filterName} style={{ marginTop: '1rem' }} block>
                      <Translate contentKey="serviceNetApp.activity.home.filter.saveFilter" />
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
  savedFilters: storeState.filterActivity.savedFilters.map(filter => ({ label: filter.name, value: filter })),
  isLoggingOut: storeState.authentication.loggingOut,
  activityFilter: storeState.filterActivity.activityFilter
});

const mapDispatchToProps = { updateActivityFilter, getSavedFilters };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SaveActivityFilter);
