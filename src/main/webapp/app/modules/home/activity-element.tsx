import './activity-element.scss';

import React from 'react';
import { Row, Col, Card, CardText, CardBody, CardTitle, CardGroup } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT } from 'app/config/constants';

const ActivityElement = props => {
  const maxConflicts = 3;
  const conflictsToDisplay = props.activity.conflicts.slice(0, maxConflicts);
  const areAllDisplayed = props.activity.conflicts.length <= maxConflicts;

  return (
    <Row className="activity-row">
      <Col>
        <CardGroup>
          <Card className="activity-card">
            <CardBody>
              <CardTitle className="activity-left-card-title">
                {props.activity.organization.name} <FontAwesomeIcon icon="angle-right" />
              </CardTitle>
              <CardTitle>
                <FontAwesomeIcon icon="map-marker-alt" /> {props.activity.organization.locationName}
              </CardTitle>
            </CardBody>
          </Card>
          <Card className="activity-right-card">
            <CardBody>
              {conflictsToDisplay.map((conflict, i) => (
                <CardTitle className="activity-right-card-title" key={`activityCard${i}`}>
                  {conflict.firstAcceptedName} {conflict.acceptedThisChange.length > 1 && <b>+{conflict.acceptedThisChange.length}</b>}
                  {conflict.acceptedThisChange.length > 2 && (
                    <Translate
                      contentKey="serviceNetApp.activity.unresolved.conflictsPlusMany"
                      interpolate={{ fieldName: conflict.fieldName }}
                    >
                      partners have a conflicting {conflict.fieldName}
                    </Translate>
                  )}
                  {conflict.acceptedThisChange.length === 2 && (
                    <Translate
                      contentKey="serviceNetApp.activity.unresolved.conflictPlusOne"
                      interpolate={{ fieldName: conflict.fieldName }}
                    >
                      partner have a conflicting {conflict.fieldName}
                    </Translate>
                  )}
                  {conflict.acceptedThisChange.length === 1 && (
                    <Translate
                      contentKey="serviceNetApp.activity.unresolved.conflictPlusZero"
                      interpolate={{ fieldName: conflict.fieldName }}
                    >
                      has a conflicting {conflict.fieldName}
                    </Translate>
                  )}
                </CardTitle>
              ))}
              {areAllDisplayed ? null : `and ${props.activity.conflicts.length - maxConflicts} more...`}
              <CardText className="activity-left-card-text-date">
                Last updated <TextFormat value={props.activity.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
              </CardText>
            </CardBody>
          </Card>
        </CardGroup>
      </Col>
    </Row>
  );
};

export default ActivityElement;
