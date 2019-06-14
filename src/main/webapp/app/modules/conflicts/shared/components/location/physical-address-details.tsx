import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { IPhysicalAddress } from 'app/shared/model/physical-address.model';
import { AdditionalDetails } from '../additional-details';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface IPhysicalAddressDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  address: IPhysicalAddress;
  showClipboard: boolean;
}

export class PhysicalAddressDetails extends React.Component<IPhysicalAddressDetailsProp> {
  render() {
    const address = this.props.address ? this.props.address : {};
    const fields = [
      getTextField(address, 'attention'),
      getTextField(address, 'address1'),
      getTextField(address, 'city'),
      getTextField(address, 'region'),
      getTextField(address, 'stateProvince'),
      getTextField(address, 'postalCode'),
      getTextField(address, 'country')
    ];

    return (
      <AdditionalDetails
        {...this.props}
        fields={fields}
        entityClass={'PhysicalAddress'}
        customHeader={false}
        additionalFields={false}
        toggleAvailable
        isCustomToggle={false}
        customToggleValue={false}
      />
    );
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhysicalAddressDetails);
