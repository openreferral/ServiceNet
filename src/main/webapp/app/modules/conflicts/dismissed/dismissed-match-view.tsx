import './dismissed-matches.scss';

import React from 'react';
import { connect } from 'react-redux';
import { Row, Col } from 'reactstrap';
import { RouteComponentProps, Link } from 'react-router-dom';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import Details from './details';
import { getBaseRecord, getPartnerRecord } from '../shared/shared-record-view.reducer';

export interface IDismissedMatchViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {}

export class DismissedMatchView extends React.Component<IDismissedMatchViewProp> {
  componentDidMount() {
    this.props.getBaseRecord(this.props.orgId);
    this.props.getPartnerRecord(this.props.partnerId);
  }

  render() {
    const { baseRecord, partnerRecord, orgId } = this.props;
    const loading = (
      <Col>
        <h2>
          <Translate contentKey="global.loading" />
        </h2>
      </Col>
    );

    return (
      <div>
        <Link to={`/dismissed-matches/${orgId}`}>
          <FontAwesomeIcon icon="arrow-left" />
          &nbsp;
          <span className="d-none d-md-inline">
            <Translate contentKey="dismissedMatches.back">Back</Translate>
          </span>
        </Link>
        <Row>
          {baseRecord ? (
            <Col sm="6">
              <h2>{baseRecord.organization.name}</h2>
              <h4 className="from">
                <Translate contentKey="dismissedMatches.yourData" />
              </h4>
              <h5>
                <Translate contentKey="dismissedMatches.lastUpdated" />
                <TextFormat value={baseRecord.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
              </h5>
              <Details activity={baseRecord} {...this.props} exclusions={baseRecord.exclusions} isBaseRecord showClipboard={false} />
            </Col>
          ) : (
            loading
          )}
          {partnerRecord ? (
            <Col sm="6">
              <h2>{partnerRecord.organization.name}</h2>
              <h4 className="from">
                <Translate contentKey="dismissedMatches.from" interpolate={{ name: partnerRecord.organization.accountName }} />
              </h4>
              <h5>
                <Translate contentKey="dismissedMatches.lastUpdated" />
                <TextFormat value={partnerRecord.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
              </h5>
              <Details activity={partnerRecord} {...this.props} exclusions={[]} isBaseRecord={false} showClipboard={false} />
            </Col>
          ) : (
            loading
          )}
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState, { match }) => ({
  orgId: match.params.orgId,
  partnerId: match.params.partnerId,
  baseRecord: storeState.sharedRecordView.baseRecord,
  partnerRecord: storeState.sharedRecordView.partnerRecord
});

const mapDispatchToProps = { getBaseRecord, getPartnerRecord };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DismissedMatchView);
