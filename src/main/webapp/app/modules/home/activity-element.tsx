import './activity-element.scss';

import React from 'react';
import { Row, Col, Card, CardText, CardBody, CardTitle, CardGroup } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT } from 'app/config/constants';

const ActivityElement = props => {
  const maxConflicts = 3;
  const conflictsToDisplay = props.activity.record.conflicts.slice(0, maxConflicts);
  const areAllDisplayed = props.activity.record.conflicts.length <= maxConflicts;

  return (
    <Row className="activity-row">
      <Col>
        <CardGroup>
          <Card className="activity-card">
            <CardBody className="activity-card-body">
              <CardTitle className="activity-left-card-title">{props.activity.record.organization.name}</CardTitle>
              {props.activity.record.organization.locationName && (
                <CardTitle className="activity-left-card-title">
                  <FontAwesomeIcon icon="map-marker-alt" className="mr-1" />
                  {props.activity.record.organization.locationName}
                </CardTitle>
              )}
            </CardBody>
          </Card>
          <Card className="activity-right-card">
            <CardBody>
              {conflictsToDisplay.map((conflict, i) => (
                <CardTitle className="activity-right-card-title" key={`activityCard${i}`}>
                  {conflict.partnerName}
                  <Translate
                    contentKey="serviceNetApp.activity.unresolved.conflictPlusZero"
                    interpolate={{ fieldName: conflict.fieldName }}
                  >
                    has a conflicting {conflict.fieldName}
                  </Translate>
                </CardTitle>
              ))}
              <div className="info-container">
                {areAllDisplayed ? <div /> : `and ${props.activity.record.conflicts.length - maxConflicts} more...`}
                {conflictsToDisplay.length > 0 && (
                  <CardText className="activity-right-card-info">
                    Last updated <TextFormat value={props.activity.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                  </CardText>
                )}
              </div>
              {conflictsToDisplay.length === 0 && (
                <CardText className="activity-right-card-info">
                  <Translate contentKey="serviceNetApp.activity.noConflicts" />
                </CardText>
              )}
            </CardBody>
          </Card>
        </CardGroup>
      </Col>
    </Row>
  );
};

export default ActivityElement;
