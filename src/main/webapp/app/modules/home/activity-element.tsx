import './activity-element.scss';

import React from 'react';
import axios from 'axios';
import { Row, Col, Card, CardText, CardBody, CardTitle, CardGroup } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { APP_DATE_FORMAT } from 'app/config/constants';
import HideRecordButton from 'app/shared/layout/hide-record-button';
import { toast } from 'react-toastify';

const ActivityElement = props => {
  const maxConflicts = 3;
  const conflictsToDisplay = props.activity.conflicts.slice(0, maxConflicts);
  const areAllDisplayed = props.activity.conflicts.length <= maxConflicts;

  const hideActivity = event => {
    const matchIds = props.activity.organizationMatches;
    event.preventDefault();
    axios
      .post(`/api/organization-matches/hideList`, matchIds)
      .then(() => {
        toast.success(translate('hiddenMatches.hiddenSuccessfully'));
        document.getElementById(props.activity.organizationId).style.display = 'none';
      })
      .catch(() => {
        toast.error(translate('hiddenMatches.hidingError'));
      });
  };

  return (
    <Row className="activity-row" id={props.activity.organizationId}>
      <Col>
        <CardGroup>
          <Card className="activity-card">
            <CardBody className="activity-card-body">
              <CardTitle className="activity-left-card-title">{props.activity.organizationName}</CardTitle>
            </CardBody>
          </Card>
          <Card className="activity-right-card">
            <HideRecordButton id={`hide-${props.activity.organizationId}`} handleHide={hideActivity} />
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
                {areAllDisplayed ? <div /> : `and ${props.activity.conflicts.length - maxConflicts} more...`}
                {conflictsToDisplay.length > 0 && (
                  <CardText className="activity-right-card-info">
                    <Translate contentKey="serviceNetApp.activity.lastPartnerUpdate" />
                    <TextFormat value={props.activity.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
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
