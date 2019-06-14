import React from 'react';
import '../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { AdditionalDetails } from './additional-details';
import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface IHolidayScheduleDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  schedule: IHolidaySchedule;
  showClipboard: boolean;
}

export class HolidayScheduleDetails extends React.Component<IHolidayScheduleDetailsProp> {
  render() {
    const schedule = this.props.schedule ? this.props.schedule : {};
    const fields = [
      {
        type: 'checkbox',
        fieldName: 'closed',
        defaultValue: schedule.closed
      },
      getTextField(schedule, 'opensAt'),
      getTextField(schedule, 'closesAt'),
      getTextField(schedule, 'startDate'),
      getTextField(schedule, 'endDate')
    ];

    return (
      <AdditionalDetails
        {...this.props}
        fields={fields}
        entityClass={'HolidaySchedule'}
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
)(HolidayScheduleDetails);
