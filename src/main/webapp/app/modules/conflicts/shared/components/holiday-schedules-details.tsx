import React from 'react';
import '../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { SingleHolidayScheduleDetails } from './single-holiday-schedule-details';
import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';

export interface IHolidaySchedulesDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  schedules: IHolidaySchedule[];
  columnSize: number;
  showClipboard: boolean;
}

export interface IHolidaySchedulesDetailsState {
  schedulesNumber: number;
}

export class HolidaySchedulesDetails extends React.Component<IHolidaySchedulesDetailsProp, IHolidaySchedulesDetailsState> {
  state: IHolidaySchedulesDetailsState = {
    schedulesNumber: 0
  };

  changeRecord = () => {
    let schedulesNumber = 0;
    if (this.state.schedulesNumber !== this.props.schedules.length - 1) {
      schedulesNumber = this.state.schedulesNumber + 1;
    }
    this.setState({ schedulesNumber });
  };

  render() {
    const { schedules } = this.props;
    const { schedulesNumber } = this.state;
    const schedule = schedules[schedulesNumber];
    const scheduleDetails =
      schedules.length > schedulesNumber ? (
        <SingleHolidayScheduleDetails
          {...this.props}
          changeRecord={this.changeRecord}
          isOnlyOne={schedules.length <= 1}
          schedule={schedule}
          schedulesCount={`(${schedulesNumber + 1}/${schedules.length}) `}
        />
      ) : null;

    return scheduleDetails;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HolidaySchedulesDetails);
