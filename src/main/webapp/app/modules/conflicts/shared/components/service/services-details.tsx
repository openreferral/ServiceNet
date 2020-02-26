import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { IServiceRecord } from 'app/shared/model/service-record.model';
import SingleServiceDetails from './single-service-details';
import { setOpenService } from 'app/modules/conflicts/shared/shared-record-view.reducer';
import { translate } from 'react-jhipster';
import _ from 'lodash';

export interface IServicesDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  columnSize: number;
  services: IServiceRecord[];
  showClipboard: boolean;
  isAreaOpen: boolean;
  settings?: any;
  orgId?: any;
  serviceMatches?: any;
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

  componentDidMount(): void {
    this.setCurrentOpenService(this.state.serviceNumber);
  }

  isBaseRecord = record => {
    const { orgId } = this.props;
    return record.service.organizationId === orgId;
  };

  setCurrentOpenService = serviceNumber => {
    const { services } = this.props;
    const sortedServices = _.sortBy(services, ['service.name']);
    const record = sortedServices[serviceNumber];
    if (record && this.props.orgId === record.service.organizationId) {
      this.props.setOpenService(record.service.id);
    }
  };

  changeRecord = serviceNumber => {
    this.setCurrentOpenService(serviceNumber);
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

  componentDidUpdate(prevProps) {
    if (prevProps.activity !== this.props.activity) {
      this.setState({
        serviceNumber: 0
      });
    }
  }

  render() {
    const { services, isAreaOpen, serviceMatches } = this.props;
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
          serviceMatches={serviceMatches}
          isBaseRecord={this.isBaseRecord(record)}
          selectedOption={serviceNumber}
        />
      ) : null;

    return serviceDetails;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = { setOpenService };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServicesDetails);
