import React from 'react';
import '../../single-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { IServiceRecord } from 'app/shared/model/service-record.model';
import { SingleServiceDetails } from './single-service-details';

export interface IServicesDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
}

export interface IServicesDetailsState {
  services: IServiceRecord[];
  servicesNumber: number;
}

export class ServicesDetails extends React.Component<IServicesDetailsProp, IServicesDetailsState> {
  state: IServicesDetailsState = {
    services: this.props.activity.record.services,
    servicesNumber: 0
  };

  changeRecord = () => {
    let servicesNumber = 0;
    if (this.state.servicesNumber !== this.state.services.length - 1) {
      servicesNumber = this.state.servicesNumber + 1;
    }
    this.setState({ servicesNumber });
  };

  render() {
    const { services, servicesNumber } = this.state;
    const record = services[servicesNumber];
    const serviceDetails =
      services.length > servicesNumber ? (
        <SingleServiceDetails
          {...this.props}
          changeRecord={this.changeRecord}
          isOnlyOne={services.length <= 1}
          record={record}
          servicesCount={`(${servicesNumber + 1}/${services.length}) `}
        />
      ) : null;

    return serviceDetails;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServicesDetails);
