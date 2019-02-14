import React from 'react';
import { Col, Row, Button } from 'reactstrap';
import '../../single-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AdditionalDetails } from '../additional-details';
import { IContact } from 'app/shared/model/contact.model';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface ISingleContactDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  contact: IContact;
  contactsCount: string;
  changeRecord: any;
  isOnlyOne: boolean;
}

export interface ISingleContactDetailsState {
  isAreaOpen: boolean;
}

export class SingleContactDetails extends React.Component<ISingleContactDetailsProp, ISingleContactDetailsState> {
  state: ISingleContactDetailsState = {
    isAreaOpen: false
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  render() {
    const { contact, isOnlyOne } = this.props;
    const customHeader = (
      <h4 className="title">
        <div className="collapseBtn" onClick={this.toggleAreaOpen}>
          <div className="collapseIcon">
            <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
          </div>
          <Translate contentKey="singleRecordView.details.titleContacts" /> {this.props.contactsCount}
        </div>
        {isOnlyOne ? null : (
          <Button className="primary" onClick={this.props.changeRecord}>
            <Translate contentKey="singleRecordView.details.seeAnotherRecord" />
          </Button>
        )}
      </h4>
    );

    const fields = [
      getTextField(contact, 'name'),
      getTextField(contact, 'title'),
      getTextField(contact, 'department'),
      getTextField(contact, 'email')
    ];
    return (
      <Row>
        <Col sm="6">
          <hr />
          <AdditionalDetails
            {...this.props}
            fields={fields}
            entityClass={'Contact'}
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
)(SingleContactDetails);
