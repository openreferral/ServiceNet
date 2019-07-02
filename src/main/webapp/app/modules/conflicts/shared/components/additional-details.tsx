import React from 'react';
import '../shared-record-view.scss';
import { connect } from 'react-redux';
import ScrollToTop from 'react-scroll-up';
import InputField from './input-field';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';
import { Collapse } from 'reactstrap';

export interface IAdditionalDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  fields: any[];
  entityClass: string;
  customHeader: any;
  additionalFields: any;
  toggleAvailable: boolean;
  isCustomToggle: boolean;
  customToggleValue: boolean;
  showClipboard: boolean;
}

export interface IAdditionalDetailsState {
  isAreaOpen: boolean;
}

export class AdditionalDetails extends React.Component<IAdditionalDetailsProp, IAdditionalDetailsState> {
  state: IAdditionalDetailsState = {
    isAreaOpen: true
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  render() {
    const { fields, entityClass, customHeader, additionalFields, isCustomToggle, customToggleValue, toggleAvailable } = this.props;

    return (
      <div>
        {customHeader ? (
          customHeader
        ) : (
          <h4 className="title">
            <div className={toggleAvailable ? 'collapseBtn' : ''} onClick={toggleAvailable ? this.toggleAreaOpen : null}>
              {toggleAvailable ? (
                <div className="collapseIcon">
                  <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
                </div>
              ) : null}
              <Translate contentKey={'singleRecordView.details.title' + entityClass} />
            </div>
          </h4>
        )}
        <Collapse isOpen={isCustomToggle ? customToggleValue : this.state.isAreaOpen}>
          {fields.map((field, i) => (
            <InputField key={i} {...this.props} {...field} />
          ))}
          {additionalFields}
        </Collapse>
        <div className="back-to-top">
          <ScrollToTop showUnder={150} duration={500}>
            <FontAwesomeIcon size="3x" icon={'angle-up'} color="lightblue" />
            <div>
              <Translate contentKey={'multiRecordView.backToTop'} />
            </div>
          </ScrollToTop>
        </div>
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
)(AdditionalDetails);
