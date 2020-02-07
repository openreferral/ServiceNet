import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { AdditionalDetails } from '../additional-details';
import { Translate } from 'react-jhipster';
import { Badge } from 'reactstrap';
import { IServiceTaxonomy } from 'app/shared/model/service-taxonomy.model';
import { getTextField } from 'app/shared/util/single-record-view-utils';
import _ from 'lodash';

export interface IServiceTaxonomiesDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  taxonomies: IServiceTaxonomy[];
  showClipboard: boolean;
  settings?: any;
}

export class ServiceTaxonomiesDetails extends React.Component<IServiceTaxonomiesDetailsProp> {
  getFields = fieldsMap => {
    const { settings } = this.props;

    if (settings === undefined || (!!settings && !settings.id)) {
      return _.values(fieldsMap);
    }

    const { serviceTaxonomiesDetailsFields } = settings;

    const fieldsMapKeys = _.keys(fieldsMap);
    const keysFiltered = _.filter(fieldsMapKeys, k => serviceTaxonomiesDetailsFields.indexOf(k) > -1);
    return _.values(_.pick(fieldsMap, keysFiltered));
  };

  render() {
    const { taxonomies } = this.props;
    const taxonomyPills = (
      <div className="taxonomy-pills">
        {taxonomies.map(taxonomy => (
          <span className="badge badge-pill badge-info" title={taxonomy.taxonomyDetails}>
            {taxonomy.taxonomyName || taxonomy.externalDbId}
          </span>
        ))}
      </div>
    );

    return (
      <AdditionalDetails
        {...this.props}
        fields={[]}
        entityClass={'ServiceTaxonomy'}
        customHeader={false}
        additionalFields={taxonomyPills}
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
)(ServiceTaxonomiesDetails);
