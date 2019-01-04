import React from 'react';
import { Row, Col, Card, CardText, CardBody, CardTitle, CardGroup, Badge } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_DATE_FORMAT } from 'app/config/constants';

const ActivityElement = props => (
  <Row style={{ marginBottom: '15px' }}>
    <Col>
      <CardGroup>
        <Card style={{ marginBottom: '0px' }}>
          <CardBody>
            <CardTitle style={{ color: '#66c4ff' }}>
              {props.activity.organization.name} <FontAwesomeIcon icon="angle-right" />
            </CardTitle>
            <CardTitle>
              <FontAwesomeIcon icon="map-marker-alt" /> {props.activity.organization.locationName}
            </CardTitle>
          </CardBody>
        </Card>
        <Card style={{ backgroundColor: '#f4f4f4' }}>
          <CardBody>
            {props.activity.conflicts.map(conflict => (
              <CardTitle style={{ color: '#66c4ff' }}>
                {conflict.ownerName} {conflict.acceptedThisChanges.length > 0 && <b>+{conflict.acceptedThisChanges.length}</b>}
                {conflict.acceptedThisChanges.length > 1 && (
                  <Translate
                    contentKey="serviceNetApp.activity.unresolved.conflictsPlusMany"
                    interpolate={{ fieldName: conflict.fieldName }}
                  >
                    partners have a conflicting {conflict.fieldName}
                  </Translate>
                )}
                {conflict.acceptedThisChanges.length === 1 && (
                  <Translate contentKey="serviceNetApp.activity.unresolved.conflictPlusOne" interpolate={{ fieldName: conflict.fieldName }}>
                    partner have a conflicting {conflict.fieldName}
                  </Translate>
                )}
                {conflict.acceptedThisChanges.length === 0 && (
                  <Translate
                    contentKey="serviceNetApp.activity.unresolved.conflictPlusZero"
                    interpolate={{ fieldName: conflict.fieldName }}
                  >
                    has a conflicting {conflict.fieldName}
                  </Translate>
                )}
              </CardTitle>
            ))}
            <CardText style={{ color: '#6d6c6c', position: 'absolute', bottom: 5 }}>
              Last updated <TextFormat value={props.activity.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
            </CardText>
          </CardBody>
        </Card>
      </CardGroup>
    </Col>
  </Row>
);

export default ActivityElement;
