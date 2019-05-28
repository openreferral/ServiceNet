import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { SingleLocationDetails } from './single-location-details';
import { ILocationRecord } from 'app/shared/model/location-record.model';

export interface ILocationsDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  columnSize: number;
  locations: ILocationRecord[];
  showClipboard: boolean;
}

export interface ILocationsDetailsState {
  locationsNumber: number;
}

export class LocationsDetails extends React.Component<ILocationsDetailsProp, ILocationsDetailsState> {
  state: ILocationsDetailsState = {
    locationsNumber: 0
  };

  changeRecord = () => {
    let locationsNumber = 0;
    if (this.state.locationsNumber !== this.props.locations.length - 1) {
      locationsNumber = this.state.locationsNumber + 1;
    }
    this.setState({ locationsNumber });
  };

  render() {
    const { locations } = this.props;
    const { locationsNumber } = this.state;
    const record = locations[locationsNumber];
    const locationDetails =
      locations.length > locationsNumber ? (
        <SingleLocationDetails
          {...this.props}
          changeRecord={this.changeRecord}
          isOnlyOne={locations.length <= 1}
          record={record}
          locationsCount={`(${locationsNumber + 1}/${locations.length}) `}
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
