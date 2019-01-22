import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './confidential-record.reducer';
import { IConfidentialRecord } from 'app/shared/model/confidential-record.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IConfidentialRecordDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ConfidentialRecordDetail extends React.Component<IConfidentialRecordDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { confidentialRecordEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.confidentialRecord.detail.title">ConfidentialRecord</Translate> [
            <b>{confidentialRecordEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="resourceId">
                <Translate contentKey="serviceNetApp.confidentialRecord.resourceId">Resource Id</Translate>
              </span>
            </dt>
            <dd>{confidentialRecordEntity.resourceId}</dd>
            <dt>
              <span id="fields">
                <Translate contentKey="serviceNetApp.confidentialRecord.fields">Fields</Translate>
              </span>
            </dt>
            <dd>{confidentialRecordEntity.fields}</dd>
          </dl>
          <Button tag={Link} to="/entity/confidential-record" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/confidential-record/${confidentialRecordEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ confidentialRecord }: IRootState) => ({
  confidentialRecordEntity: confidentialRecord.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ConfidentialRecordDetail);
