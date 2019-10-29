import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { IServiceRecord } from 'app/shared/model/service-record.model';
import { SingleServiceDetails } from './single-service-details';
import _ from 'lodash';

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

  changeRecord = serviceNumber => {
    this.setState({ serviceNumber });
  };

  render() {
    const { services, isAreaOpen } = this.props;
    const { serviceNumber } = this.state;
    const sortedServices = _.sortBy(services, ['service.name']);
    const record = sortedServices[serviceNumber];
    const serviceDetails =
      sortedServices.length > serviceNumber ? (
        <SingleServiceDetails
          {...this.props}
          changeRecord={this.changeRecord}
          selectOptions={_.map(sortedServices, (s, idx) => ({ value: idx, label: s.service.name }))}
          isOnlyOne={sortedServices.length <= 1}
          record={record}
          servicesCount={`(${serviceNumber + 1}/${sortedServices.length}) `}
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
