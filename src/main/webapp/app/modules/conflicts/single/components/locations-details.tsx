import React from 'react';
import '../single-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { SingleLocationDetails } from './single-location-details';

export interface ILocationsDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
}

export interface ILocationsDetailsState {
  locations: any[];
  locationsNumber: number;
}

export class LocationsDetails extends React.Component<ILocationsDetailsProp, ILocationsDetailsState> {
  state: ILocationsDetailsState = {
    locations: this.props.activity.record.locations,
    locationsNumber: 0
  };

  changeRecord = () => {
    let locationsNumber = 0;
    if (this.state.locationsNumber !== this.state.locations.length - 1) {
      locationsNumber = this.state.locationsNumber + 1;
    }
    this.setState({ locationsNumber });
  };

  render() {
    const { locations, locationsNumber } = this.state;
    const locationDetails =
      locations.length > locationsNumber ? (
        <SingleLocationDetails
          {...this.props}
          changeRecord={this.changeRecord}
          isOnlyOne={locations.length <= 1}
          location={locations[locationsNumber].location}
          physicalAddress={locations[locationsNumber].physicalAddress ? locations[locationsNumber].physicalAddress : {}}
          postalAddress={locations[locationsNumber].postalAddress ? locations[locationsNumber].postalAddress : {}}
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
