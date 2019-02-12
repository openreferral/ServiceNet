import React from 'react';
import '../single-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
import { AdditionalDetails } from './additional-details';

export interface IPostalAddressDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  address: IPostalAddress;
}

export class PostalAddressDetails extends React.Component<IPostalAddressDetailsProp> {
  getTextField = (address, fieldName) => {
    return {
      type: 'text',
      fieldName: fieldName,
      defaultValue: address[fieldName]
    };
  };

  render() {
    const { address } = this.props;
    const fields = [
      this.getTextField(address, 'attention'),
      this.getTextField(address, 'address1'),
      this.getTextField(address, 'city'),
      this.getTextField(address, 'region'),
      this.getTextField(address, 'stateProvince'),
      this.getTextField(address, 'postalCode'),
      this.getTextField(address, 'country')
    ];

    return (
      <AdditionalDetails
        {...this.props}
        fields={fields}
        entityClass={'PostalAddress'}
        customHeader={false}
        additionalFields={false}
        toggleAvailable={true}
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
