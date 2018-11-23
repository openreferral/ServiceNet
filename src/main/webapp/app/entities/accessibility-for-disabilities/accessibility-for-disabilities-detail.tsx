import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './accessibility-for-disabilities.reducer';
import { IAccessibilityForDisabilities } from 'app/shared/model/accessibility-for-disabilities.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAccessibilityForDisabilitiesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AccessibilityForDisabilitiesDetail extends React.Component<IAccessibilityForDisabilitiesDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { accessibilityForDisabilitiesEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.accessibilityForDisabilities.detail.title">AccessibilityForDisabilities</Translate> [
            <b>{accessibilityForDisabilitiesEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="accessibility">
                <Translate contentKey="serviceNetApp.accessibilityForDisabilities.accessibility">Accessibility</Translate>
              </span>
            </dt>
            <dd>{accessibilityForDisabilitiesEntity.accessibility}</dd>
            <dt>
              <span id="details">
                <Translate contentKey="serviceNetApp.accessibilityForDisabilities.details">Details</Translate>
              </span>
            </dt>
            <dd>{accessibilityForDisabilitiesEntity.details}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.accessibilityForDisabilities.location">Location</Translate>
            </dt>
            <dd>{accessibilityForDisabilitiesEntity.locationName ? accessibilityForDisabilitiesEntity.locationName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/accessibility-for-disabilities" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button
            tag={Link}
            to={`/entity/accessibility-for-disabilities/${accessibilityForDisabilitiesEntity.id}/edit`}
            replace
            color="primary"
          >
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

const mapStateToProps = ({ accessibilityForDisabilities }: IRootState) => ({
  accessibilityForDisabilitiesEntity: accessibilityForDisabilities.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AccessibilityForDisabilitiesDetail);
