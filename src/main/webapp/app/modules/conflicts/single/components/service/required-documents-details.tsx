import React from 'react';
import '../../single-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { AdditionalDetails } from '../additional-details';
import { IRequiredDocument } from 'app/shared/model/required-document.model';
import { Translate } from 'react-jhipster';
import { Badge } from 'reactstrap';

export interface IRequiredDocumentsDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  docs: IRequiredDocument[];
}

export class RequiredDocumentsDetails extends React.Component<IRequiredDocumentsDetailsProp> {
  getTextField = (document, fieldName) => ({
    type: 'text',
    fieldName,
    defaultValue: document[fieldName]
  });

  render() {
    const { docs } = this.props;
    const fields = docs.map(document => this.getTextField(document, 'document'));

    return fields.length > 0 ? (
      <AdditionalDetails
        {...this.props}
        fields={fields}
        entityClass={'RequiredDocument'}
        customHeader={false}
        additionalFields={false}
        toggleAvailable
        isCustomToggle={false}
        customToggleValue={false}
      />
    ) : (
      <h4>
        <Badge color="secondary">
          <Translate contentKey="singleRecordView.details.noRecords" />
        </Badge>
      </h4>
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
)(RequiredDocumentsDetails);
