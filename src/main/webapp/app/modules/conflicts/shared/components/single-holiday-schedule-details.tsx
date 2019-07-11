import React from 'react';
import { Col, Row } from 'reactstrap';
import '../shared-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AdditionalDetails } from './additional-details';
import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface ISingleHolidayScheduleDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  schedule: IHolidaySchedule;
  schedulesCount: string;
  changeRecord: any;
  isOnlyOne: boolean;
  columnSize: number;
  showClipboard: boolean;
}

export interface ISingleHolidayScheduleDetails {
  isAreaOpen: boolean;
}

export class SingleHolidayScheduleDetails extends React.Component<ISingleHolidayScheduleDetailsProp> {
  state: ISingleHolidayScheduleDetails = {
    isAreaOpen: false
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  changeRecord = offset => () => {
    this.setState({ isAreaOpen: true });
    this.props.changeRecord(offset);
  };

  render() {
    const { schedule, isOnlyOne, columnSize } = this.props;
    const customHeader = (
      <h4 className="title">
        <div className="collapseBtn" onClick={this.toggleAreaOpen}>
          <div className="collapseIcon">
            <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
          </div>
          <Translate contentKey="singleRecordView.details.titleHolidaySchedules" />{' '}
          <span className="text-blue">{this.props.schedulesCount}</span>
        </div>
        {isOnlyOne ? null : (
          <span>
            <span role="button" onClick={this.changeRecord(-1)}>
              <FontAwesomeIcon className="text-blue" icon="chevron-left" /> <Translate contentKey="singleRecordView.details.prev" />
            </span>
            <span role="button" onClick={this.changeRecord(1)}>
              <Translate contentKey="singleRecordView.details.next" /> <FontAwesomeIcon className="text-blue" icon="chevron-right" />
            </span>
          </span>
        )}
      </h4>
    );

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
      <Row>
        <Col sm={columnSize}>
          <hr />
          <AdditionalDetails
            {...this.props}
            fields={fields}
            entityClass={'HolidaySchedule'}
            customHeader={customHeader}
            additionalFields={false}
            toggleAvailable
            isCustomToggle
            customToggleValue={this.state.isAreaOpen}
          />
        </Col>
      </Row>
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
)(SingleHolidayScheduleDetails);
