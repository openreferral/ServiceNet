import React from 'react';
import '../single-record-view.scss';
import { connect } from 'react-redux';
import InputField from './input-field';
import { IActivity } from 'app/shared/model/activity.model';
import { IOpeningHours } from 'app/shared/model/opening-hours.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';
import { Collapse, Table } from 'reactstrap';

export interface IOpeningHoursDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  hours: IOpeningHours[];
}

export interface IOpeningHoursDetailsState {
  isAreaOpen: boolean;
}

export class OpeningHoursDetails extends React.Component<IOpeningHoursDetailsProp, IOpeningHoursDetailsState> {
  state: IOpeningHoursDetailsState = {
    isAreaOpen: true
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  mapWeekdayToString = day => {
    switch (day) {
      case 0:
        return 'Monday';
      case 1:
        return 'Tuesday';
      case 2:
        return 'Wednesday';
      case 3:
        return 'Thursday';
      case 4:
        return 'Friday';
      case 5:
        return 'Saturday';
      case 6:
        return 'Sunday';
      default:
        return day;
    }
  };

  render() {
    const { hours } = this.props;
    return (
      <div>
        <h4 className="title">
          <div className="collapseBtn" onClick={this.toggleAreaOpen}>
            <div className="collapseIcon">
              <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
            </div>
            <Translate contentKey="singleRecordView.details.openingHoursTitle" />
          </div>
        </h4>
        <Collapse isOpen={this.state.isAreaOpen}>
          <Table bordered>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="singleRecordView.details.openingHoursWeekday" />
                </th>
                <th>
                  <Translate contentKey="singleRecordView.details.openingHoursOpensAt" />
                </th>
                <th>
                  <Translate contentKey="singleRecordView.details.openingHoursClosesAt" />
                </th>
              </tr>
            </thead>
            <tbody>
              {hours
                ? hours.map((day, i) => (
                    <tr key={i}>
                      <th scope="row">{this.mapWeekdayToString(day.weekday)}</th>
                      <td>{day.opensAt}</td>
                      <td>{day.closesAt}</td>
                    </tr>
                  ))
                : null}
            </tbody>
          </Table>
        </Collapse>
      </div>
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
)(OpeningHoursDetails);
