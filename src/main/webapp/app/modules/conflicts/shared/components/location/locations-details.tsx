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

  componentDidMount() {
    if (this.props.selectLocation) {
      this.props.selectLocation(this.props.locations[this.state.locationNumber]);
    }
  }

  changeRecord = locationNumber => {
    this.setState({ locationNumber });
    if (this.props.selectLocation) {
      this.props.selectLocation(this.props.locations[locationNumber]);
    }
  };

  getLocationNumber = () => {
    const { matchLocations, matchingLocation, locations } = this.props;
    if (matchLocations && matchingLocation) {
      const idx = _.findIndex(locations, l => l.location.id === matchingLocation);
      return idx >= 0 ? idx : this.state.locationNumber;
    } else {
      return this.state.locationNumber;
    }
  };

  render() {
    const { locations, isAreaOpen } = this.props;
    const locationNumber = this.getLocationNumber();
    const record = locations[locationNumber];
    const locationDetails =
      locations.length > locationNumber ? (
        <SingleLocationDetails
          {...this.props}
          selectOptions={_.map(locations, (l, idx) => ({
            value: idx,
            label: l.location.name || `${l.physicalAddress.address1} ${l.physicalAddress.city}`
          }))}
          changeRecord={this.changeRecord}
          isOnlyOne={locations.length <= 1}
          record={record}
          locationsCount={`(${locationNumber + 1}/${locations.length}) `}
          isAreaOpen={isAreaOpen}
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
