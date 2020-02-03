import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { SingleLocationDetails } from './single-location-details';
import { ILocationRecord } from 'app/shared/model/location-record.model';
import _ from 'lodash';

export interface ILocationsDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  columnSize: number;
  locations: ILocationRecord[];
  showClipboard: boolean;
  isAreaOpen: boolean;
  selectLocation?: any;
  matchingLocation?: any;
  matchLocations?: boolean;
  toggleMatchLocations?: any;
  isBaseRecord: boolean;
  settings?: any;
}

export interface ILocationsDetailsState {
  locationNumber: number;
  isAreaOpen: boolean;
}

export class LocationsDetails extends React.Component<ILocationsDetailsProp, ILocationsDetailsState> {
  state: ILocationsDetailsState = {
    locationNumber: 0,
    isAreaOpen: this.props.isAreaOpen
  };

  static sortLocations(locations) {
    return _.sortBy(locations, ['physicalAddress.address1', 'physicalAddress.city']);
  }

  componentDidMount() {
    if (this.props.selectLocation) {
      this.props.selectLocation(this.props.locations[this.state.locationNumber]);
    }
  }

  componentDidUpdate(prevProps) {
    if (prevProps.matchingLocation !== this.props.matchingLocation) {
      this.setState({
        locationNumber: this.getLocationNumber()
      });
    }
  }

  changeRecord = locationNumber => {
    this.setState({ locationNumber });
    if (this.props.selectLocation) {
      const sortedLocations = LocationsDetails.sortLocations(this.props.locations);
      this.props.selectLocation(sortedLocations[locationNumber]);
    }
  };

  getLocationNumber = () => {
    const { matchLocations, matchingLocation, locations } = this.props;
    if (matchLocations && matchingLocation) {
      const idx = _.findIndex(LocationsDetails.sortLocations(locations), l => l.location.id === matchingLocation);
      return idx >= 0 ? idx : this.state.locationNumber;
    } else {
      return this.state.locationNumber;
    }
  };

  render() {
    const { locations, isAreaOpen } = this.props;
    const locationNumber = this.state.locationNumber;
    const sortedLocations = LocationsDetails.sortLocations(locations);
    const record = sortedLocations[locationNumber];
    const locationDetails =
      sortedLocations.length > locationNumber ? (
        <SingleLocationDetails
          {...this.props}
          selectOptions={_.map(sortedLocations, (l, idx) => SingleLocationDetails.getSelectOption(l, idx))}
          changeRecord={this.changeRecord}
          isOnlyOne={sortedLocations.length <= 1}
          record={record}
          locationsCount={`(${locationNumber + 1}/${sortedLocations.length}) `}
          isAreaOpen={isAreaOpen}
          locationNumber={this.state.locationNumber}
        />
      ) : null;

    return locationDetails;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LocationsDetails);
