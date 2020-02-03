import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { IServiceRecord } from 'app/shared/model/service-record.model';
import { SingleServiceDetails } from './single-service-details';
import { translate } from 'react-jhipster';
import _ from 'lodash';

export interface IServicesDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  columnSize: number;
  services: IServiceRecord[];
  showClipboard: boolean;
  isAreaOpen: boolean;
  settings?: any;
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

  getServiceDisplayLabel = missingServiceNameIndex => (s, index) => {
    if (!!s.service && !_.isEmpty(s.service.name)) {
      return { value: index, label: s.service.name };
    }
    missingServiceNameIndex++;
    return {
      value: index,
      label: `${translate('serviceNetApp.service.detail.title')} ${missingServiceNameIndex}`
    };
  };

  render() {
    const { services, isAreaOpen } = this.props;
    const { serviceNumber } = this.state;
    const sortedServices = _.sortBy(services, ['service.name']);
    const record = sortedServices[serviceNumber];
    const missingServiceNameIndex = 0;
    const serviceDetails =
      sortedServices.length > serviceNumber ? (
        <SingleServiceDetails
          {...this.props}
          changeRecord={this.changeRecord}
          selectOptions={_.map(sortedServices, this.getServiceDisplayLabel(missingServiceNameIndex))}
          isOnlyOne={sortedServices.length <= 1}
          record={record}
          servicesCount={`(${serviceNumber + 1}/${sortedServices.length}) `}
          isAreaOpen={isAreaOpen}
          settings={this.props.settings}
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
