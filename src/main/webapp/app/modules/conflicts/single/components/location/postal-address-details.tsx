import React from 'react';
import '../../single-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
import { AdditionalDetails } from '../additional-details';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface IPostalAddressDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  address: IPostalAddress;
}

export class PostalAddressDetails extends React.Component<IPostalAddressDetailsProp> {
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
        entityClass={'PostalAddress'}
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
)(PostalAddressDetails);
