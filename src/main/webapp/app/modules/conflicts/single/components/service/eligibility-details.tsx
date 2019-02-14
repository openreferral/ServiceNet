import React from 'react';
import '../../single-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { AdditionalDetails } from '../additional-details';
import { IEligibility } from 'app/shared/model/eligibility.model';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface IEligibilityDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  eligibility: IEligibility;
}

export class EligibilityDetails extends React.Component<IEligibilityDetailsProp> {
  render() {
    const eligibility = this.props.eligibility ? this.props.eligibility : {};
    const fields = [getTextField(eligibility, 'eligibility')];

    return (
      <AdditionalDetails
        {...this.props}
        fields={fields}
        entityClass={'Eligibility'}
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
)(EligibilityDetails);
