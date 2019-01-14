import './activity-element.scss';

import React from 'react';
import { Row, Col, Card, CardText, CardBody, CardTitle, CardGroup } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT } from 'app/config/constants';

const ActivityElement = props => (
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
            {props.activity.conflicts.map(conflict => (
              <CardTitle className="activity-right-card-title">
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
                  <Translate contentKey="serviceNetApp.activity.unresolved.conflictPlusOne" interpolate={{ fieldName: conflict.fieldName }}>
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
            <CardText className="activity-left-card-text-date">
              Last updated <TextFormat value={props.activity.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
            </CardText>
          </CardBody>
        </Card>
      </CardGroup>
    </Col>
  </Row>
);

export default ActivityElement;
