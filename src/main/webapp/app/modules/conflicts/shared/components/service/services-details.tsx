import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { IServiceRecord } from 'app/shared/model/service-record.model';
import { SingleServiceDetails } from './single-service-details';

export interface IServicesDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  columnSize: number;
  services: IServiceRecord[];
  showClipboard: boolean;
  isAreaOpen: boolean;
}

export interface IServicesDetailsState {
  serviceNumber: number;
  isAreaOpen: boolean;
}

export class ServicesDetails extends React.Component<IServicesDetailsProp, IServicesDetailsState> {
  state: IServicesDetailsState = {
    serviceNumber: 0,
    isAreaOpen: this.props.isAreaOpen
  };

  changeRecord = offset => {
    let serviceNumber = 0;
    const offsetServiceNumber = this.state.serviceNumber + offset;
    if (offsetServiceNumber < 0) {
      serviceNumber = this.props.services.length - 1;
    } else if (offsetServiceNumber < this.props.services.length) {
      serviceNumber = offsetServiceNumber;
    }
    this.setState({ serviceNumber });
  };

  render() {
    const { services, isAreaOpen } = this.props;
    const { serviceNumber } = this.state;
    const record = services[serviceNumber];
    const serviceDetails =
      services.length > serviceNumber ? (
        <SingleServiceDetails
          {...this.props}
          changeRecord={this.changeRecord}
          isOnlyOne={services.length <= 1}
          record={record}
          servicesCount={`(${serviceNumber + 1}/${services.length}) `}
          isAreaOpen={isAreaOpen}
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
