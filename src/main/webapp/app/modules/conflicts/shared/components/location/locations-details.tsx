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
  locationNumber: number;
}

export class LocationsDetails extends React.Component<ILocationsDetailsProp, ILocationsDetailsState> {
  state: ILocationsDetailsState = {
    locationNumber: 0
  };

  changeRecord = offset => {
    let locationNumber = 0;
    const offsetServiceNumber = this.state.locationNumber + offset;
    if (offsetServiceNumber < 0) {
      locationNumber = this.props.locations.length - 1;
    } else if (offsetServiceNumber < this.props.locations.length) {
      locationNumber = offsetServiceNumber;
    }
    this.setState({ locationNumber });
  };

  render() {
    const { locations } = this.props;
    const { locationNumber } = this.state;
    const record = locations[locationNumber];
    const locationDetails =
      locations.length > locationNumber ? (
        <SingleLocationDetails
          {...this.props}
          changeRecord={this.changeRecord}
          isOnlyOne={locations.length <= 1}
          record={record}
          locationsCount={`(${locationNumber + 1}/${locations.length}) `}
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
