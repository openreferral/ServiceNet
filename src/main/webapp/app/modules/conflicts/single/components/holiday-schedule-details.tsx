import React from 'react';
import '../single-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { AdditionalDetails } from './additional-details';
import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';

export interface IHolidayScheduleDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  schedule: IHolidaySchedule;
}

export class HolidayScheduleDetails extends React.Component<IHolidayScheduleDetailsProp> {
  getTextField = (schedule, fieldName) => ({
    type: 'text',
    fieldName,
    defaultValue: schedule[fieldName]
  });

  render() {
    const schedule = this.props.schedule ? this.props.schedule : {};
    const fields = [
      {
        type: 'checkbox',
        fieldName: 'closed',
        defaultValue: schedule.closed
      },
      this.getTextField(schedule, 'opensAt'),
      this.getTextField(schedule, 'closesAt'),
      this.getTextField(schedule, 'startDate'),
      this.getTextField(schedule, 'endDate')
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
