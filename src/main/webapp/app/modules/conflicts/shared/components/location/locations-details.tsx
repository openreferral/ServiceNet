import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import SingleLocationDetails, { SingleLocationDetails as SingleLocationDetailsClass } from './single-location-details';
import { ILocationRecord } from 'app/shared/model/location-record.model';
import { setOpenLocation } from 'app/modules/conflicts/shared/shared-record-view.reducer';

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
  locationMatches?: any;
  orgId?: string;
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
    this.setCurrentOpenLocation(this.state.locationNumber);
    if (this.props.selectLocation && this.props.isBaseRecord) {
      this.props.selectLocation(this.props.locations[this.state.locationNumber]);
    }
  }

  isBaseRecord = record => {
    const { orgId } = this.props;
    return record.location.organizationId === orgId;
  };

  setCurrentOpenLocation = locationNumber => {
    const { locations } = this.props;
    const sortedLocations = LocationsDetails.sortLocations(locations);
    const record = sortedLocations[locationNumber];
    if (record && this.props.orgId === record.location.organizationId) {
      this.props.setOpenLocation(record.location.id);
    }
  };

  componentDidUpdate(prevProps) {
    if (prevProps.matchingLocation !== this.props.matchingLocation) {
      this.setState({
        locationNumber: this.getLocationNumber()
      });
    } else if (prevProps.activity !== this.props.activity) {
      this.setState({
        locationNumber: 0
      });
    }
  }

  changeRecord = locationNumber => {
    this.setCurrentOpenLocation(locationNumber);
    this.setState({ locationNumber });
    if (this.props.selectLocation) {
      const sortedLocations = LocationsDetails.sortLocations(this.props.locations);
      this.props.selectLocation(sortedLocations[locationNumber]);
    }
  };

  getLocationNumber = () => {
    const { matchLocations, matchingLocation, locations } = this.props;
    if (matchLocations && matchingLocation) {
      const idx = _.findIndex(LocationsDetails.sortLocations(locations), l => l.location.id === matchingLocation.matchingLocation);
      return idx >= 0 ? idx : this.state.locationNumber;
    } else {
      return this.state.locationNumber;
    }
  };

  render() {
    const { locations, isAreaOpen, locationMatches } = this.props;
    const locationNumber = this.state.locationNumber;
    const sortedLocations = LocationsDetails.sortLocations(locations);
    const record = sortedLocations[locationNumber];
    const locationDetails =
      sortedLocations.length > locationNumber ? (
        <SingleLocationDetails
          {...this.props}
          selectOptions={_.map(sortedLocations, (l, idx) => SingleLocationDetailsClass.getSelectOption(l, idx))}
          changeRecord={this.changeRecord}
          isOnlyOne={sortedLocations.length <= 1}
          record={record}
          locationsCount={`(${locationNumber + 1}/${sortedLocations.length}) `}
          isAreaOpen={isAreaOpen}
          locationNumber={this.state.locationNumber}
          locationMatches={locationMatches}
          isBaseRecord={this.isBaseRecord(record)}
        />
      ) : null;

    return locationDetails;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = { setOpenLocation };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LocationsDetails);
