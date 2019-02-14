import React from 'react';
import '../../single-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { AdditionalDetails } from '../additional-details';
import { IFunding } from 'app/shared/model/funding.model';

export interface IFundingDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  funding: IFunding;
}

export class FundingDetails extends React.Component<IFundingDetailsProp> {
  getTextField = (funding, fieldName) => ({
    type: 'text',
    fieldName,
    defaultValue: funding[fieldName]
  });

  render() {
    const funding = this.props.funding ? this.props.funding : {};
    const fields = [this.getTextField(funding, 'source')];

    return (
      <AdditionalDetails
        {...this.props}
        fields={fields}
        entityClass={'Funding'}
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
)(FundingDetails);
