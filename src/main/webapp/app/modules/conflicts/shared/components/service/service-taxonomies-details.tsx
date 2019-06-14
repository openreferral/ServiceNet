import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { AdditionalDetails } from '../additional-details';
import { Translate } from 'react-jhipster';
import { Badge } from 'reactstrap';
import { IServiceTaxonomy } from 'app/shared/model/service-taxonomy.model';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface IServiceTaxonomiesDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  taxonomies: IServiceTaxonomy[];
  showClipboard: boolean;
}

export class ServiceTaxonomiesDetails extends React.Component<IServiceTaxonomiesDetailsProp> {
  render() {
    const { taxonomies } = this.props;
    const fields = taxonomies.map(taxonomy => getTextField(taxonomy, 'taxonomyDetails'));

    return fields.length > 0 ? (
      <AdditionalDetails
        {...this.props}
        fields={fields}
        entityClass={'ServiceTaxonomy'}
        customHeader={false}
        additionalFields={false}
        toggleAvailable
        isCustomToggle={false}
        customToggleValue={false}
      />
    ) : null;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceTaxonomiesDetails);
